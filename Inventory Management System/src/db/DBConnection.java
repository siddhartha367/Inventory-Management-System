package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConnection {
    // MySQL connection details (without database name for initial connection)
    private static final String URL_ROOT = "jdbc:mysql://localhost:3306/";
    private static final String DB_NAME = "inventory_db";
    private static final String URL = URL_ROOT + DB_NAME;
    private static final String USER = "root";        // change as needed
    private static final String PASSWORD = "sas367"; // change as needed

    // Static block to ensure database and tables exist when class is loaded
    static {
        createDatabaseIfNotExists();
        createTablesIfNotExists();
    }

    private static void createDatabaseIfNotExists() {
        // Connect without specifying a database to execute CREATE DATABASE
        String sql = "CREATE DATABASE IF NOT EXISTS " + DB_NAME;
        try (Connection conn = DriverManager.getConnection(URL_ROOT, USER, PASSWORD);
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql);
            System.out.println("Database ensured: " + DB_NAME);
        } catch (SQLException e) {
            System.err.println("Failed to create database: " + e.getMessage());
        }
    }

    private static void createTablesIfNotExists() {
        String createProductsTable = """
            CREATE TABLE IF NOT EXISTS products (
                id INT PRIMARY KEY AUTO_INCREMENT,
                product_id VARCHAR(50) UNIQUE NOT NULL,
                name VARCHAR(100) NOT NULL,
                quantity INT NOT NULL,
                price DOUBLE NOT NULL,
                supplier VARCHAR(100),
                reorder_level INT NOT NULL
            )
            """;

        String createSalesTable = """
            CREATE TABLE IF NOT EXISTS sales (
                sale_id INT PRIMARY KEY AUTO_INCREMENT,
                product_id VARCHAR(50) NOT NULL,
                quantity_sold INT NOT NULL,
                sale_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                FOREIGN KEY (product_id) REFERENCES products(product_id) ON DELETE CASCADE
            )
            """;

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(createProductsTable);
            stmt.executeUpdate(createSalesTable);
            System.out.println("Tables ensured.");
        } catch (SQLException e) {
            System.err.println("Failed to create tables: " + e.getMessage());
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    // Optional: test the connection and setup
    public static void main(String[] args) {
        try (Connection conn = getConnection()) {
            System.out.println("Connected to database successfully!");
        } catch (SQLException e) {
            System.out.println("Connection failed: " + e.getMessage());
        }
    }
}

/*Use this if there are some issues. But you need to create the database manually. Which have been mentioned in the README file.

package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/inventory_db";
    private static final String USER = "root";      // your MySQL username
    private static final String PASSWORD = "sas367"; // your MySQL password

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC Driver not found.", e);
        }
    }
}*/
