package com.itechart.contacts.db.dao.implimentation;

import com.itechart.contacts.db.dao.AddressDao;
import com.itechart.contacts.db.model.AddressModel;
import com.itechart.contacts.exception.TransactionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.*;

import static com.itechart.contacts.util.JdbcUtils.*;

public class AddressDaoImpl implements AddressDao {

    private static final Logger log = LoggerFactory.getLogger(AddressDaoImpl.class);

    private static final String INSERT_SQL
            = "INSERT INTO address(country, city, detail_address, zip) VALUES (?, ?, ?, ?)";

    private static final String UPDATE_SQL
            = "UPDATE address SET country = ?, city = ?, detail_address = ?, zip = ? WHERE id = ?";

    private static final String DELETE_SQL = "DELETE FROM address WHERE id = ?";

    private static final String SELECT_SQL
            = "SELECT phone.id, phone.country_code, phone.operator_code, " +
            "phone.phone_number, phone.phone_type, phone.comment_text " +
            "FROM phone WHERE phone.contact_id = ?";

    private DataSource dataSource;

    public AddressDaoImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public int add(AddressModel address) throws TransactionException {
        log.info("Add a new address");

        PreparedStatement stmt = null;
        try {
            Connection connection = dataSource.getConnection();
            stmt = connection.prepareStatement(INSERT_SQL);
            stmt.setString(1, address.getCountry());
            stmt.setString(2, address.getCity());
            stmt.setString(3, address.getDetailAddress());
            if (address.getZip() != 0) {
                stmt.setInt(4, address.getZip());
            } else {
                stmt.setNull(4, Types.INTEGER);
            }
            return stmt.executeUpdate();
        } catch (SQLException e) {
            throw new TransactionException("Insert Address exception", e);
        } finally {
            closeQuietly(null, stmt);
        }
    }

    @Override
    public int update(AddressModel address) throws TransactionException {
        log.info("Update the address with id: {}", address.getId());

        PreparedStatement stmt = null;
        try {
            Connection connection = dataSource.getConnection();
            stmt = connection.prepareStatement(UPDATE_SQL);
            stmt.setString(1, address.getCountry());
            stmt.setString(2, address.getCity());
            stmt.setString(3, address.getDetailAddress());
            if (address.getZip() != 0) {
                stmt.setInt(4, address.getZip());
            } else {
                stmt.setNull(4, Types.INTEGER);
            }
            stmt.setInt(5, address.getId());
            return stmt.executeUpdate();
        } catch (Exception e) {
            throw new TransactionException("Update Address exception", e);
        } finally {
            closeQuietly(null, stmt);
        }
    }

    @Override
    public int delete(int id) throws TransactionException {
        log.info("Delete the address with id: {}", id);

        PreparedStatement stmt = null;
        try {
            Connection connection = dataSource.getConnection();
            stmt = connection.prepareStatement(DELETE_SQL);
            stmt.setInt(1, id);
            return stmt.executeUpdate();
        } catch (Exception e) {
            throw new TransactionException("Delete Address exception", e);
        } finally {
            closeQuietly(null, stmt);
        }
    }

    @Override
    public AddressModel get(int id) throws TransactionException {
        log.info("Get an address by id: {}", id);

        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            Connection connection = dataSource.getConnection();
            stmt = connection.prepareStatement(SELECT_SQL);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();
            if (rs.next()) {
                AddressModel address = new AddressModel();
                address.setCountry(rs.getString("country"));
                address.setCity(rs.getString("city"));
                address.setDetailAddress(rs.getString("detail_address"));
                address.setZip(rs.getInt("zip"));
                return address;
            }
        } catch (Exception e) {
            throw new TransactionException("Select Address exception", e);
        } finally {
            closeQuietly(rs, stmt);
        }
        return null;
    }
}