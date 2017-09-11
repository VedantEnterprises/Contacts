package com.itechart.contacts.db.transaction;

import com.itechart.contacts.exception.TransactionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.itechart.contacts.util.JdbcUtils;

import javax.servlet.ServletException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class TransactionManagerImpl extends BaseDataSource implements TransactionManager {

    private static TransactionManager instance = new TransactionManagerImpl();

    private static final Logger log = LoggerFactory.getLogger(TransactionManagerImpl.class);

    private static ThreadLocal<Connection> connectionHolder = new ThreadLocal<>();

    private TransactionManagerImpl() {
    }

    @Override
    public <T> T executeTransaction(Transaction<T> transaction) throws TransactionException, ServletException {
        Connection connection = null;
        try {
            InputStream in = TransactionManagerImpl.class.getClassLoader().getResourceAsStream("/db.properties");
            Properties properties = new Properties();
            properties.load(in);
            String driver = properties.getProperty("driver");
            String url = properties.getProperty("url");
            String user = properties.getProperty("user");
            String password = properties.getProperty("password");
            Class.forName(driver);
            connection = DriverManager.getConnection(url, user, password);
            connection.setAutoCommit(false);
            connectionHolder.set(connection);
            T result = transaction.execute();
            connection.commit();
            return result;
        } catch (TransactionException | SQLException e) {
            JdbcUtils.rollBackQuietly(connection);
            throw new TransactionException("Transaction exception", e);
        } catch (IOException | ClassNotFoundException e) {
            log.error("Failed getting a jdbc connection", e);
            throw new ServletException(e);
        } finally {
            JdbcUtils.closeQuietly(connection);
            connectionHolder.remove();
        }
    }

    @Override
    public Connection getConnection() throws SQLException {
        return connectionHolder.get();
    }

    public static TransactionManager getInstance() {
        return instance;
    }
}