package com.itechart.contacts.db.transaction;

import com.itechart.contacts.exception.TransactionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.itechart.contacts.util.JdbcUtils;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class TransactionManagerImpl extends BaseDataSource implements TransactionManager {

    private static final Logger log = LoggerFactory.getLogger(TransactionManagerImpl.class);

    private static DataSource ds;
    private static ThreadLocal<Connection> connectionHolder = new ThreadLocal<>();

    public TransactionManagerImpl() {
        init();
    }

    private void init() {
        try {
            Context initContext = new InitialContext();
            Context envContext  = (Context) initContext.lookup("java:/comp/env");
            ds = (DataSource) envContext.lookup("jdbc/contacts");
        } catch (NamingException e) {
            log.error("Failed to init context for JDBC configuration", e);
        }
    }

    @Override
    public <T> T executeTransaction(Transaction<T> transaction) throws TransactionException {
        if (ds == null) {
            throw new TransactionException("TransactionManager is not initialized!");
        }

        Connection connection = null;
        try {
            connection = ds.getConnection();
            connection.setAutoCommit(false);
            connectionHolder.set(connection);
            T result = transaction.execute();
            connection.commit();
            return result;
        } catch (SQLException e) {
            JdbcUtils.rollBackQuietly(connection);
            throw new TransactionException("Execute transaction exception", e);
        } finally {
            JdbcUtils.closeQuietly(connection);
            connectionHolder.remove();
        }
    }

    @Override
    public Connection getConnection() {
        return connectionHolder.get();
    }
}