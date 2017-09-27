package com.itechart.contacts.db.dao.implimentation;

import com.itechart.contacts.db.dao.AttachmentDao;
import com.itechart.contacts.db.model.AttachmentModel;
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

public class AttachmentDaoImpl implements AttachmentDao {

    private static final Logger log = LoggerFactory.getLogger(AttachmentDaoImpl.class);

    private static final String INSERT_SQL
            = "INSERT INTO attachment(contact_id, file_uuid, file_name, " +
            "upload_date, comment_text) VALUES (?, ?, ?, ?, ?)";

    private static final String UPDATE_SQL
            = "UPDATE attachment SET file_uuid = ?, file_name = ?, " +
            "upload_date = ?, comment_text = ? WHERE id = ?";

    private static final String DELETE_SQL = "DELETE FROM attachment WHERE id = ?";

    private static final String SELECT_ALL_SQL
            = "SELECT attachment.id, attachment.file_uuid, attachment.file_name, " +
            "attachment.upload_date, attachment.comment_text " +
            "FROM attachment WHERE attachment.contact_id = ?";

    private DataSource dataSource;

    public AttachmentDaoImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public int add(AttachmentModel attachment) throws TransactionException {
        log.info("Add a new attachment to a contact with id: {}", attachment.getContactId());

        PreparedStatement stmt = null;
        try {
            Connection connection = dataSource.getConnection();
            stmt = connection.prepareStatement(INSERT_SQL);
            stmt.setInt(1, attachment.getContactId());
            stmt.setString(2, attachment.getFileUuid());
            stmt.setString(3, attachment.getFileName());
            stmt.setTimestamp(4, attachment.getUploadDate());
            stmt.setString(5, attachment.getComment());
            return stmt.executeUpdate();
        } catch (SQLException e) {
            throw new TransactionException("Insert Attachment exception", e);
        } finally {
            closeQuietly(null, stmt);
        }
    }

    @Override
    public int update(AttachmentModel attachment) throws TransactionException {
        log.info("Update the attachment with id: {} inside a contact with id: {}",
                attachment.getId(), attachment.getContactId());

        PreparedStatement stmt = null;
        try {
            Connection connection = dataSource.getConnection();
            stmt = connection.prepareStatement(UPDATE_SQL);
            stmt.setString(1, attachment.getFileUuid());
            stmt.setString(2, attachment.getFileName());
            stmt.setTimestamp(3, attachment.getUploadDate());
            stmt.setString(4, attachment.getComment());
            return stmt.executeUpdate();
        } catch (SQLException e) {
            throw new TransactionException("Update Attachment exception", e);
        } finally {
            closeQuietly(null, stmt);
        }
    }

    @Override
    public int delete(int id) throws TransactionException {
        log.info("Delete the attachment with id: {}", id);

        PreparedStatement stmt = null;
        try {
            Connection connection = dataSource.getConnection();
            stmt = connection.prepareStatement(DELETE_SQL);
            stmt.setInt(1, id);
            return stmt.executeUpdate();
        } catch (SQLException e) {
            throw new TransactionException("Delete Attachment exception", e);
        } finally {
            closeQuietly(null, stmt);
        }
    }

    @Override
    public List<AttachmentModel> get(int contactId) throws TransactionException {
        log.info("Get an attachments list by a contact id: {}", contactId);

        List<AttachmentModel> attachments = new ArrayList<>();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            Connection connection = dataSource.getConnection();
            stmt = connection.prepareStatement(SELECT_ALL_SQL);
            stmt.setInt(1, contactId);
            rs = stmt.executeQuery();
            while (rs.next()) {
                AttachmentModel attachment = new AttachmentModel();
                attachment.setId(rs.getInt("id"));
                attachment.setContactId(contactId);
                attachment.setFileUuid(rs.getString("file_uuid"));
                attachment.setFileName(rs.getString("file_name"));
                attachment.setUploadDate(rs.getTimestamp("upload_date"));
                attachment.setComment(rs.getString("comment_text"));
                attachments.add(attachment);
            }
        } catch (SQLException e) {
            throw new TransactionException("Select all Attachments exception", e);
        } finally {
            closeQuietly(rs, stmt);
        }
        return attachments;
    }
}