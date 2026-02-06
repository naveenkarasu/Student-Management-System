package com.studentmgmt.util;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Database Connection Utility class.
 * Provides methods to obtain database connections using properties file configuration.
 */
public class DBConnection {

    private static String driver;
    private static String url;
    private static String username;
    private static String password;

    static {
        try {
            // Load properties from db.properties file
            Properties props = new Properties();
            InputStream inputStream = DBConnection.class.getClassLoader()
                    .getResourceAsStream("db.properties");

            if (inputStream != null) {
                props.load(inputStream);
                driver = props.getProperty("db.driver");
                url = props.getProperty("db.url");
                username = props.getProperty("db.username");
                password = props.getProperty("db.password");
                inputStream.close();
            } else {
                // Default values if properties file not found
                driver = "com.mysql.jdbc.Driver";
                url = "jdbc:mysql://localhost:3306/student_management_db?useSSL=false";
                username = "root";
                password = "root";
            }

            // Load the MySQL JDBC driver
            Class.forName(driver);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to initialize database connection: " + e.getMessage());
        }
    }

    /**
     * Get a database connection.
     * @return Connection object
     * @throws SQLException if connection fails
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }

    /**
     * Close a database connection safely.
     * @param connection the connection to close
     */
    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Test database connection.
     * @return true if connection is successful, false otherwise
     */
    public static boolean testConnection() {
        Connection conn = null;
        try {
            conn = getConnection();
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            closeConnection(conn);
        }
    }
}
