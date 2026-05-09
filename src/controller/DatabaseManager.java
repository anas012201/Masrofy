package controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {
    private static final String URL = "jdbc:sqlserver://localhost:1433;databaseName=spendwise_db;integratedSecurity=true;encrypt=true;trustServerCertificate=true;";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    public static void initDatabase() {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            String sql = "IF NOT EXISTS (SELECT * FROM sys.tables WHERE name = 'transactions') " +
                         "CREATE TABLE transactions (" +
                         "amount FLOAT, " +
                         "category NVARCHAR(255), " +
                         "type NVARCHAR(50), " +
                         "date NVARCHAR(50))";
            stmt.executeUpdate(sql);
            System.out.println("Success: Connected to SQL Server!");
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}