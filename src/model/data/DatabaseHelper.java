package model.data;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DatabaseHelper.java
 *
 * Responsibilities:
 * 1. Connecting to the SQLite database
 * 2. Creating tables if they don't exist
 * 3. All CRUD operations (Insert, Read, Update, Delete)
 *
 * NOTE: No other class should talk to SQLite directly.
 * All database access must go through this class only.
 *
 * FIX: Added explicit JDBC driver registration in a static block.
 *      Without this, the driver may silently fail to load in some
 *      JVM environments, causing every DB call to return null/empty
 *      and the app to randomly show the wrong startup screen.
 */
public class DatabaseHelper {

    private static final String DB_NAME = "masroofy.db";
    private static final String DB_URL  = "jdbc:sqlite:" + DB_NAME;

    // ======================================================
    // FIX 1 — Driver Registration
    // ======================================================

    /**
     * This static block runs once when DatabaseHelper is first loaded.
     * It forces the SQLite JDBC driver to register itself with the
     * DriverManager. Without this, the driver is not guaranteed to
     * load, and every DriverManager.getConnection() call silently
     * fails — making hasActiveCycle(), getPin(), etc. all return
     * wrong values and causing the random startup screen bug.
     */
    static {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            System.out.println("FATAL: SQLite JDBC driver not found. " +
                    "Make sure sqlite-jdbc.jar is on the classpath.");
            // Re-throw as unchecked so the app fails fast and clearly
            // instead of silently showing wrong screens.
            throw new RuntimeException("SQLite JDBC driver missing", e);
        }
    }

    // ======================================================
    // Create Tables
    // ======================================================

    public void createTables() {

        String createCycles =
                "CREATE TABLE IF NOT EXISTS cycles (" +
                        "id          INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "allowance   REAL    NOT NULL, " +
                        "start_date  TEXT    NOT NULL, " +
                        "end_date    TEXT    NOT NULL, " +
                        "is_active   INTEGER DEFAULT 1" +
                        ")";

        String createTransactions =
                "CREATE TABLE IF NOT EXISTS transactions (" +
                        "id         INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "cycle_id   INTEGER NOT NULL, " +
                        "amount     REAL    NOT NULL, " +
                        "category   TEXT    NOT NULL, " +
                        "timestamp  TEXT    NOT NULL" +
                        ")";

        String createSettings =
                "CREATE TABLE IF NOT EXISTS settings (" +
                        "id            INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "pin           TEXT, " +
                        "privacy_lock  INTEGER DEFAULT 0" +
                        ")";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {

            stmt.execute(createCycles);
            stmt.execute(createTransactions);
            stmt.execute(createSettings);

            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM settings");
            if (rs.next() && rs.getInt(1) == 0) {
                stmt.execute("INSERT INTO settings (pin, privacy_lock) VALUES (NULL, 0)");
            }

            System.out.println("Tables created successfully.");

        } catch (SQLException e) {
            System.out.println("Error creating tables: " + e.getMessage());
        }
    }


    // ======================================================
    // CYCLES
    // ======================================================

    /**
     * Inserts a new budget cycle.
     * Uses last_insert_rowid() instead of getGeneratedKeys() because
     * getGeneratedKeys() throws "not implemented" on sqlite-jdbc 3.43.
     *
     * @return The new row's id, or -1 if the insert failed.
     */
    public int insertCycle(double allowance, String startDate, String endDate) {
        String sql = "INSERT INTO cycles (allowance, start_date, end_date, is_active) " +
                "VALUES (?, ?, ?, 1)";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setDouble(1, allowance);
            pstmt.setString(2, startDate);
            pstmt.setString(3, endDate);
            pstmt.executeUpdate();

            ResultSet rs = conn.createStatement()
                    .executeQuery("SELECT last_insert_rowid()");
            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            System.out.println("Error inserting cycle: " + e.getMessage());
        }
        return -1;
    }

    /**
     * Returns the currently active cycle as a plain String array.
     * Reads all data and closes the connection before returning —
     * no ResultSet leaks, no stale reads after resetCycle().
     *
     * @return String[] { id, allowance, start_date, end_date }
     *         or null if no active cycle exists.
     */
    public String[] getActiveCycleData() {
        String sql = "SELECT * FROM cycles WHERE is_active = 1 LIMIT 1";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new String[]{
                        String.valueOf(rs.getInt("id")),
                        String.valueOf(rs.getDouble("allowance")),
                        rs.getString("start_date"),
                        rs.getString("end_date")
                };
            }

        } catch (SQLException e) {
            System.out.println("Error getting active cycle: " + e.getMessage());
        }
        return null;
    }

    /**
     * Marks a cycle as inactive (does NOT delete it).
     */
    public void deactivateCycle(int cycleId) {
        String sql = "UPDATE cycles SET is_active = 0 WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, cycleId);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error deactivating cycle: " + e.getMessage());
        }
    }

    /**
     * Permanently deletes a cycle and all its transactions (full reset).
     * Uses a transaction so both deletes succeed or both are rolled back.
     */
    public void deleteCycle(int cycleId) {
        String deleteTransactions = "DELETE FROM transactions WHERE cycle_id = ?";
        String deleteCycle        = "DELETE FROM cycles WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL)) {

            conn.setAutoCommit(false);

            try (PreparedStatement p1 = conn.prepareStatement(deleteTransactions);
                 PreparedStatement p2 = conn.prepareStatement(deleteCycle)) {

                p1.setInt(1, cycleId);
                p1.executeUpdate();

                p2.setInt(1, cycleId);
                p2.executeUpdate();

                conn.commit();
                System.out.println("Cycle deleted successfully.");

            } catch (SQLException e) {
                conn.rollback();
                System.out.println("Error deleting cycle: " + e.getMessage());
            }

        } catch (SQLException e) {
            System.out.println("Connection error: " + e.getMessage());
        }
    }


    // ======================================================
    // TRANSACTIONS
    // ======================================================

    /**
     * Inserts a new expense transaction.
     * Uses last_insert_rowid() — same reason as insertCycle().
     *
     * @return The new row's id, or -1 if the insert failed.
     */
    public int insertTransaction(int cycleId, double amount, String category, String timestamp) {
        String sql = "INSERT INTO transactions (cycle_id, amount, category, timestamp) " +
                "VALUES (?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, cycleId);
            pstmt.setDouble(2, amount);
            pstmt.setString(3, category);
            pstmt.setString(4, timestamp);
            pstmt.executeUpdate();

            ResultSet rs = conn.createStatement()
                    .executeQuery("SELECT last_insert_rowid()");
            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            System.out.println("Error inserting transaction: " + e.getMessage());
        }
        return -1;
    }

    /**
     * Retrieves all transactions for a given cycle, newest first.
     * Returns List<String[]> so the connection is fully closed before
     * the caller sees any data — no ResultSet leaks.
     */
    public List<String[]> getAllTransactions(int cycleId) {
        List<String[]> list = new ArrayList<>();
        String sql = "SELECT * FROM transactions WHERE cycle_id = ? ORDER BY timestamp DESC";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, cycleId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String[] row = new String[4];
                row[0] = String.valueOf(rs.getInt("id"));
                row[1] = String.valueOf(rs.getDouble("amount"));
                row[2] = rs.getString("category");
                row[3] = rs.getString("timestamp");
                list.add(row);
            }

        } catch (SQLException e) {
            System.out.println("Error getting transactions: " + e.getMessage());
        }
        return list;
    }

    /**
     * Retrieves transactions filtered by category.
     */
    public List<String[]> getTransactionsByCategory(int cycleId, String category) {
        List<String[]> list = new ArrayList<>();
        String sql = "SELECT * FROM transactions WHERE cycle_id = ? AND category = ? ORDER BY timestamp DESC";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, cycleId);
            pstmt.setString(2, category);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String[] row = new String[4];
                row[0] = String.valueOf(rs.getInt("id"));
                row[1] = String.valueOf(rs.getDouble("amount"));
                row[2] = rs.getString("category");
                row[3] = rs.getString("timestamp");
                list.add(row);
            }

        } catch (SQLException e) {
            System.out.println("Error filtering transactions: " + e.getMessage());
        }
        return list;
    }

    /**
     * Calculates the total amount spent in a given cycle.
     */
    public double getTotalSpent(int cycleId) {
        String sql = "SELECT SUM(amount) FROM transactions WHERE cycle_id = ?";
        double total = 0;

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, cycleId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next() && rs.getObject(1) != null) {
                total = rs.getDouble(1);
            }

        } catch (SQLException e) {
            System.out.println("Error calculating total: " + e.getMessage());
        }
        return total;
    }

    /**
     * Updates an existing transaction's amount and category.
     */
    public void updateTransaction(int transId, double newAmount, String newCategory) {
        String sql = "UPDATE transactions SET amount = ?, category = ? WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setDouble(1, newAmount);
            pstmt.setString(2, newCategory);
            pstmt.setInt(3, transId);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error updating transaction: " + e.getMessage());
        }
    }

    /**
     * Deletes a single transaction by its id.
     */
    public void deleteTransaction(int transId) {
        String sql = "DELETE FROM transactions WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, transId);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error deleting transaction: " + e.getMessage());
        }
    }


    // ======================================================
    // SETTINGS
    // ======================================================

    public void savePin(String pin) {
        String sql = "UPDATE settings SET pin = ? WHERE id = 1";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, pin);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error saving PIN: " + e.getMessage());
        }
    }

    public String getPin() {
        String sql = "SELECT pin FROM settings WHERE id = 1";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            ResultSet rs = pstmt.executeQuery();
            if (rs.next() && rs.getObject(1) != null) {
                return rs.getString(1);
            }

        } catch (SQLException e) {
            System.out.println("Error fetching PIN: " + e.getMessage());
        }
        return null;
    }

    public void setPrivacyLock(boolean isEnabled) {
        String sql = "UPDATE settings SET privacy_lock = ? WHERE id = 1";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, isEnabled ? 1 : 0);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error updating privacy lock: " + e.getMessage());
        }
    }

    public boolean isPrivacyLockEnabled() {
        String sql = "SELECT privacy_lock FROM settings WHERE id = 1";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) == 1;
            }

        } catch (SQLException e) {
            System.out.println("Error fetching privacy lock: " + e.getMessage());
        }
        return false;
    }
}