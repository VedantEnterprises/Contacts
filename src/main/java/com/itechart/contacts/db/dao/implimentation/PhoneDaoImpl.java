package com.itechart.contacts.db.dao.implimentation;

import com.itechart.contacts.db.dao.PhoneDao;
import com.itechart.contacts.db.model.PhoneModel;
import com.itechart.contacts.exception.TransactionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.itechart.contacts.util.JdbcUtils.*;

public class PhoneDaoImpl implements PhoneDao {

    private static final Logger log = LoggerFactory.getLogger(PhoneDaoImpl.class);

    private static final String INSERT_SQL
            = "INSERT INTO phone(contact_id, country_code, operator_code, " +
            "phone_number, phone_type, comment_text) VALUES (?, ?, ?, ?, ?, ?)";

    private static final String UPDATE_SQL
            = "UPDATE phone SET country_code = ?, operator_code = ?, " +
            "phone_number = ?, phone_type = ?, comment_text = ? WHERE id = ?";

    private static final String DELETE_SQL = "DELETE FROM phone WHERE id = ?";

    private static final String SELECT_ALL_SQL
            = "SELECT phone.id, phone.country_code, phone.operator_code, " +
            "phone.phone_number, phone.phone_type, phone.comment_text " +
            "FROM phone WHERE phone.contact_id = ?";

    private DataSource dataSource;

    public PhoneDaoImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public int add(PhoneModel phone) throws TransactionException {
        log.info("Add a new phone to a contact with id: {}", phone.getContactId());

        PreparedStatement stmt = null;
        try {
            Connection connection = dataSource.getConnection();
            stmt = connection.prepareStatement(INSERT_SQL);
            stmt.setInt(1, phone.getContactId());
            stmt.setInt(2, phone.getCountryCode());
            stmt.setInt(3, phone.getOperatorCode());
            stmt.setInt(4, phone.getPhoneNumber());
            stmt.setString(5, phone.getType());
            stmt.setString(6, phone.getComment());
            return stmt.executeUpdate();
        } catch (SQLException e) {
            throw new TransactionException("Insert Phone exception", e);
        } finally {
            closeQuietly(null, stmt);
        }
    }

    @Override
    public int update(PhoneModel phone) throws TransactionException {
        log.info("Update the phone with id: {} inside a contact with id: {}",
                phone.getId(), phone.getContactId());

        PreparedStatement stmt = null;
        try {
            Connection connection = dataSource.getConnection();
            stmt = connection.prepareStatement(UPDATE_SQL);
            stmt.setInt(1, phone.getCountryCode());
            stmt.setInt(2, phone.getOperatorCode());
            stmt.setInt(3, phone.getPhoneNumber());
            stmt.setString(4, phone.getType());
            stmt.setString(5, phone.getComment());
            stmt.setInt(6, phone.getId());
            return stmt.executeUpdate();
        } catch (SQLException e) {
            throw new TransactionException("Update Phone exception", e);
        } finally {
            closeQuietly(null, stmt);
        }
    }

    @Override
    public int delete(int id) throws TransactionException {
        log.info("Delete the phone with id: {}", id);

        PreparedStatement stmt = null;
        try {
            Connection connection = dataSource.getConnection();
            stmt = connection.prepareStatement(DELETE_SQL);
            stmt.setInt(1, id);
            return stmt.executeUpdate();
        } catch (SQLException e) {
            throw new TransactionException("Delete Phone exception", e);
        } finally {
            closeQuietly(null, stmt);
        }
    }

    @Override
    public List<PhoneModel> get(int contactId) throws TransactionException {
        log.info("Get a phones list by a contact id: {}", contactId);

        List<PhoneModel> phones = new ArrayList<>();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            Connection connection = dataSource.getConnection();
            stmt = connection.prepareStatement(SELECT_ALL_SQL);
            stmt.setInt(1, contactId);
            rs = stmt.executeQuery();
            while (rs.next()) {
                PhoneModel phone = new PhoneModel();
                phone.setId(rs.getInt("id"));
                phone.setContactId(contactId);
                phone.setCountryCode(rs.getInt("country_code"));
                phone.setOperatorCode(rs.getInt("operator_code"));
                phone.setPhoneNumber(rs.getInt("phone_number"));
                phone.setType(rs.getString("phone_type"));
                phone.setComment(rs.getString("comment_text"));
                phones.add(phone);
            }
        } catch (SQLException e) {
            throw new TransactionException("Select all Phones exception", e);
        } finally {
            closeQuietly(rs, stmt);
        }
        return phones;
    }
}