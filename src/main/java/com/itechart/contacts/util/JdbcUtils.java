package com.itechart.contacts.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;

public class JdbcUtils {

    public static void closeQuietly(AutoCloseable resource) {
        if (resource != null) {
            try {
                resource.close();
            } catch (Exception e) {
                /*NOP*/
            }
        }
    }

    public static void closeQuietly(AutoCloseable... resources) {
        Arrays.stream(resources).forEach(JdbcUtils::closeQuietly);
    }

    public static void closeQuietly(ResultSet rs, Statement stm) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                /*NOP*/
            }
        }

        if (stm != null) {
            try {
                stm.close();
            } catch (SQLException e) {
                /*NOP*/
            }
        }
    }

    public static void rollBackQuietly(Connection connection) {
        if (connection != null) {
            try {
                connection.rollback();
            } catch (SQLException e) {
                /*NOP*/
            }
        }
    }
}