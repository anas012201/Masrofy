package model.data;

import java.util.List;

/**
 * SQLiteStorage.java
 *
 * This is the class that the rest of the team talks to.
 * It sits on top of DatabaseHelper and converts raw database
 * results into simple, ready-to-use Java objects (String[], List).
 *
 * Rule: No other class should ever import or use DatabaseHelper directly.
 *       Everything must go through SQLiteStorage.
 *
 * Flow:
 *   Controller / CycleManager
 *        ↓
 *   SQLiteStorage       ← this file
 *        ↓
 *   DatabaseHelper
 *        ↓
 *   masroofy.db
 */
public class SQLiteStorage {

    private DatabaseHelper db;

    // ======================================================
    // Constructor
    // ======================================================

    public SQLiteStorage() {
        db = new DatabaseHelper();
        db.createTables();
    }


    // ======================================================
    // CYCLES
    // ======================================================

    /**
     * Adds a new budget cycle.
     *
     * @param allowance  Total budget in EGP
     * @param startDate  Start date (e.g. "2026-05-01")
     * @param endDate    End date   (e.g. "2026-05-30")
     * @return The new cycle's id, or -1 if it failed
     */
    public int addCycle(double allowance, String startDate, String endDate) {
        return db.insertCycle(allowance, startDate, endDate);
    }

    /**
     * Returns the currently active cycle as a simple String array.
     *
     * FIX: Calls db.getActiveCycleData() which returns a plain String[]
     *      with the connection already closed — no more ResultSet leaks
     *      causing stale reads after resetCycle().
     *
     * @return String[] { id, allowance, start_date, end_date }
     *         or null if no active cycle exists.
     */
    public String[] getActiveCycle() {
        return db.getActiveCycleData();
    }

    /**
     * Checks if there is an active cycle running right now.
     *
     * @return true if a cycle exists, false otherwise
     */
    public boolean hasActiveCycle() {
        return getActiveCycle() != null;
    }

    /**
     * Ends the current cycle without deleting it.
     *
     * @param cycleId  The id of the cycle to end
     */
    public void endCycle(int cycleId) {
        db.deactivateCycle(cycleId);
    }

    /**
     * Permanently deletes the cycle and all its transactions (full reset).
     *
     * @param cycleId  The id of the cycle to delete
     */
    public void resetCycle(int cycleId) {
        db.deleteCycle(cycleId);
    }


    // ======================================================
    // TRANSACTIONS
    // ======================================================

    /**
     * Adds a new expense transaction.
     *
     * @param cycleId    The id of the current active cycle
     * @param amount     The expense amount
     * @param category   Category name (Food / Transport / Entertainment / Other)
     * @param timestamp  Date and time string (e.g. "2026-05-08 14:30:00")
     * @return The new transaction's id, or -1 if it failed
     */
    public int addTransaction(int cycleId, double amount, String category, String timestamp) {
        return db.insertTransaction(cycleId, amount, category, timestamp);
    }

    /**
     * Returns all transactions for a given cycle, newest first.
     *
     * FIX: DatabaseHelper now returns List<String[]> directly, so no
     *      ResultSet or connection management is needed here.
     *
     * @param cycleId  The id of the cycle
     * @return A List of String[] { id, amount, category, timestamp }
     *         Empty list if there are no transactions.
     */
    public List<String[]> getAllTransactions(int cycleId) {
        return db.getAllTransactions(cycleId);
    }

    /**
     * Returns transactions filtered by a specific category.
     *
     * FIX: DatabaseHelper now returns List<String[]> directly, so no
     *      ResultSet or connection management is needed here.
     *
     * @param cycleId   The id of the cycle
     * @param category  The category to filter by (e.g. "Food")
     * @return A List of String[] { id, amount, category, timestamp }
     */
    public List<String[]> getTransactionsByCategory(int cycleId, String category) {
        return db.getTransactionsByCategory(cycleId, category);
    }

    /**
     * Returns the total amount spent in a given cycle.
     *
     * @param cycleId  The id of the cycle
     * @return Total spent as a double (0.0 if no transactions exist)
     */
    public double getTotalSpent(int cycleId) {
        return db.getTotalSpent(cycleId);
    }

    /**
     * Updates an existing transaction's amount and category.
     *
     * @param transId      The id of the transaction to update
     * @param newAmount    The new amount
     * @param newCategory  The new category
     */
    public void editTransaction(int transId, double newAmount, String newCategory) {
        db.updateTransaction(transId, newAmount, newCategory);
    }

    /**
     * Deletes a single transaction.
     *
     * @param transId  The id of the transaction to delete
     */
    public void removeTransaction(int transId) {
        db.deleteTransaction(transId);
    }


    // ======================================================
    // SETTINGS
    // ======================================================

    /**
     * Saves a new PIN to the database.
     *
     * @param pin  A 4-digit PIN string
     */
    public void savePin(String pin) {
        db.savePin(pin);
    }

    /**
     * Returns the stored PIN.
     *
     * @return The PIN as a String, or null if not set
     */
    public String getPin() {
        return db.getPin();
    }

    /**
     * Checks if the PIN entered by the user matches the stored one.
     *
     * @param enteredPin  The PIN typed by the user
     * @return true if correct, false if wrong or no PIN is set
     */
    public boolean checkPin(String enteredPin) {
        String savedPin = db.getPin();
        if (savedPin == null) return false;
        return savedPin.equals(enteredPin);
    }

    /**
     * Enables or disables the privacy lock feature.
     *
     * @param isEnabled  true to enable, false to disable
     */
    public void setPrivacyLock(boolean isEnabled) {
        db.setPrivacyLock(isEnabled);
    }

    /**
     * Checks whether the privacy lock is currently enabled.
     *
     * @return true if enabled, false otherwise
     */
    public boolean isPrivacyLockEnabled() {
        return db.isPrivacyLockEnabled();
    }
}
