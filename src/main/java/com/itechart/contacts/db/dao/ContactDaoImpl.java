package com.itechart.contacts.db.dao;

import com.itechart.contacts.db.model.AddressModel;
import com.itechart.contacts.db.model.ContactModel;
import com.itechart.contacts.exception.TransactionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.itechart.contacts.util.JdbcUtils;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ContactDaoImpl implements ContactDao {

    private static ContactDao instance = new ContactDaoImpl();

    private static final Logger log = LoggerFactory.getLogger(ContactDaoImpl.class);

    private static final String INSERT_SQL
            = "INSERT INTO contact(f_name, l_name, m_name, birthday, sex, nationality, " +
            "marital_status, site, email, current_job, address_id, avatar) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    private static final String UPDATE_SQL
            = "UPDATE contact SET f_name = ?, l_name = ?, m_name = ?, birthday = ?, " +
            "sex = ?, nationality = ?, marital_status = ?, site = ?, email = ?, " +
            "current_job = ?, avatar = ? WHERE id = ?";

    private static final String SELECT_SQL
            = "SELECT contact.*, address.* FROM contact INNER JOIN address " +
            "ON contact.address_id = address.id WHERE contact.id = ?";

    private static final String SELECT_ALL_SQL
            = "SELECT contact.id, contact.f_name, contact.l_name, contact.m_name, " +
            "contact.birthday, contact.current_job, address.country, address.city, " +
            "address.detail_address, address.zip FROM contact INNER JOIN address " +
            "ON contact.address_id = address.id LIMIT ? OFFSET ?";

    private static final String DELETE_SQL = "DELETE FROM contact where id = ?";

    private static final String COUNT_SQL = "SELECT COUNT(*) FROM contact";

    private DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public int add(ContactModel contact) throws TransactionException {
        PreparedStatement stmt = null;
        try {
            Connection connection = dataSource.getConnection();
            stmt = connection.prepareStatement(INSERT_SQL);
            stmt.setString(1, contact.getFirstName());
            stmt.setString(2, contact.getLastName());
            stmt.setString(3, contact.getMiddleName());
            stmt.setDate(4, contact.getBirthday());
            stmt.setString(5, contact.getSex());
            stmt.setString(6, contact.getNationality());
            stmt.setString(7, contact.getMaritalStatus());
            stmt.setString(8, contact.getSite());
            stmt.setString(9, contact.getEmail());
            stmt.setString(10, contact.getCurrentJob());
            stmt.setInt(11, contact.getAddress().getId());
            stmt.setString(12, contact.getAvatar());
            return stmt.executeUpdate();
        } catch (SQLException e) {
            throw new TransactionException("Insert Contact exception", e);
        } finally {
            JdbcUtils.closeQuietly(null, stmt);
        }
    }

    @Override
    public int update(ContactModel contact) throws TransactionException {
        PreparedStatement stmt = null;
        try {
            Connection connection = dataSource.getConnection();
            stmt = connection.prepareStatement(UPDATE_SQL);
            stmt.setString(1, contact.getFirstName());
            stmt.setString(2, contact.getLastName());
            stmt.setString(3, contact.getMiddleName());
            stmt.setDate(4, contact.getBirthday());
            stmt.setString(5, contact.getSex());
            stmt.setString(6, contact.getNationality());
            stmt.setString(7, contact.getMaritalStatus());
            stmt.setString(8, contact.getSite());
            stmt.setString(9, contact.getEmail());
            stmt.setString(10, contact.getCurrentJob());
            stmt.setString(11, contact.getAvatar());
            stmt.setInt(12, contact.getId());
            return stmt.executeUpdate();
        } catch (SQLException e) {
            throw new TransactionException("Insert Contact exception", e);
        } finally {
            JdbcUtils.closeQuietly(null, stmt);
        }
    }

    @Override
    public ContactModel get(int id) throws TransactionException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            Connection connection = dataSource.getConnection();
            stmt = connection.prepareStatement(SELECT_SQL);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();
            if (rs.next()) {
                ContactModel contact = new ContactModel(
                        rs.getString("f_name"),
                        rs.getString("l_name"));
                contact.setId(rs.getInt("id"));
                contact.setMiddleName(rs.getString("m_name"));
                contact.setBirthday(rs.getDate("birthday"));
                contact.setSex(rs.getString("sex"));
                contact.setNationality(rs.getString("nationality"));
                contact.setMaritalStatus(rs.getString("marital_status"));
                contact.setSite(rs.getString("site"));
                contact.setEmail(rs.getString("email"));
                contact.setCurrentJob(rs.getString("current_job"));
                contact.setAvatar(rs.getString("avatar"));

                AddressModel address = new AddressModel();
                address.setCountry("country");
                address.setCity(rs.getString("city"));
                address.setDetailAddress(rs.getString("detail_address"));
                address.setZip(rs.getInt("zip"));
                contact.setAddress(address);
                return contact;
            } else{
                throw new TransactionException("No such contact for id = \"" + id + "\".");
            }
        } catch (SQLException e) {
            throw new TransactionException("Select Contact exception", e);
        } finally {
            JdbcUtils.closeQuietly(rs, stmt);
        }
    }

    @Override
    public List<ContactModel> getAll(int page, int amount) throws TransactionException {
        List<ContactModel> contacts = new ArrayList<>();

        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            Connection connection = dataSource.getConnection();
            stmt = connection.prepareStatement(SELECT_ALL_SQL);
            stmt.setInt(1, amount);
            stmt.setInt(2, page - 1);
            rs = stmt.executeQuery();
            while (rs.next()) {
                ContactModel contact = new ContactModel(
                        rs.getString("f_name"),
                        rs.getString("l_name"));
                contact.setId(rs.getInt("id"));
                contact.setMiddleName(rs.getString("m_name"));
                contact.setBirthday(rs.getDate("birthday"));
                contact.setCurrentJob(rs.getString("current_job"));

                AddressModel address = new AddressModel();
                address.setCountry(rs.getString("country"));
                address.setCity(rs.getString("city"));
                address.setDetailAddress(rs.getString("detail_address"));
                address.setZip(rs.getInt("zip"));
                contact.setAddress(address);

                contacts.add(contact);
            }
        } catch (SQLException e) {
            throw new TransactionException("Select all Contacts exception", e);
        } finally {
            JdbcUtils.closeQuietly(rs, stmt);
        }

        return contacts;
    }

    @Override
    public int delete(int id) throws TransactionException {
        PreparedStatement stmt = null;
        try {
            Connection connection = dataSource.getConnection();
            stmt = connection.prepareStatement(DELETE_SQL);
            stmt.setInt(1, id);
            return stmt.executeUpdate();
        } catch (SQLException e) {
            throw new TransactionException("Delete Contact exception", e);
        } finally {
            JdbcUtils.closeQuietly(null, stmt);
        }
    }

    @Override
    public int size() throws TransactionException {
        Statement stmt = null;
        ResultSet rs = null;
        try {
            Connection connection = dataSource.getConnection();
            stmt = connection.createStatement();
            rs = stmt.executeQuery(COUNT_SQL);
            if (rs.next()) {
                return rs.getInt(1);
            } else {
                return 0;
            }
        } catch (SQLException e) {
            throw new TransactionException("Count contacts exception", e);
        } finally {
            JdbcUtils.closeQuietly(null, stmt);
        }
    }

    public static ContactDao getInstance() {
        return instance;
    }
}