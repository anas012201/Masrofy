package Controller;

import interfaces.IAlertService;
import interfaces.IDataStorage;
import model.data.SQLiteStorage;
import service.CycleManager;
import service.EmailAlertService;
import view.*;

import javax.swing.*;
import java.util.List;

/**
 * AppController.java
 * The navigation and wiring hub of the application.
 *
 * Responsibilities (after refactor):
 *   - Hold single instances of IDataStorage, IAlertService, CycleManager
 *   - Handle screen navigation
 *   - Delegate ALL business calculations to CycleManager
 *   - Delegate ALL alert delivery to IAlertService
 *   - Expose simple getter methods to View screens
 *
 * SOLID — Single Responsibility Principle:
 *   AppController no longer calculates daily limits or percentage spent.
 *   That is CycleManager's job.
 *   AppController no longer fires JOptionPane alerts.
 *   That is EmailAlertService's job (behind IAlertService).
 *
 * SOLID — Dependency Inversion Principle:
 *   AppController depends on IDataStorage and IAlertService (abstractions),
 *   not on SQLiteStorage or EmailAlertService (concrete classes).
 *   Concrete implementations are created once here and injected
 *   into CycleManager — nowhere else creates them.
 *
 * FIX (from previous session):
 *   - Removed double SwingUtilities.invokeLater() nesting (race condition)
 *   - Removed stale activeCycleId cache; replaced with getActiveCycleId()
 */
public class AppController {

    // ======================================================
    // Singleton
    // ======================================================
    private static AppController instance;

    public static AppController getInstance() {
        if (instance == null) {
            instance = new AppController();
        }
        return instance;
    }

    // ======================================================
    // Fields — depend on abstractions (DIP)
    // ======================================================
    private final IDataStorage  storage;
    private final IAlertService alertService;
    private final CycleManager  cycleManager;
    private JFrame              currentFrame;

    // ======================================================
    // Constructor
    // ======================================================
    private AppController() {
        // Concrete implementations created here ONLY.
        // Everything else receives the abstraction interfaces.
        storage      = new SQLiteStorage();
        alertService = new EmailAlertService();
        cycleManager = new CycleManager(storage, alertService);
    }

    // ======================================================
    // Helper — always-fresh cycle ID (no stale cache)
    // ======================================================

    /**
     * Reads the active cycle id from the DB every time it is needed.
     * Replaces the old cached "activeCycleId" field which could go
     * stale if the DB wasn't ready when the constructor ran.
     */
    private int getActiveCycleId() {
        String[] cycle = storage.getActiveCycle();
        if (cycle == null) return -1;
        return Integer.parseInt(cycle[0]);
    }

    // ======================================================
    // App Startup
    // ======================================================

    /**
     * Decides which screen to show first.
     *
     * Flow:
     * 1. Privacy lock ON + PIN exists → Login screen
     * 2. No active cycle              → Budget Setup screen
     * 3. Otherwise                    → Dashboard
     *
     * Single invokeLater — DB reads happen synchronously on the EDT,
     * then showScreen() is called once. Fixes the race condition
     * caused by the old double-nested invokeLater.
     */
    public void start() {
        SwingUtilities.invokeLater(() -> {
            boolean lockEnabled = storage.isPrivacyLockEnabled();
            boolean pinExists   = storage.getPin() != null;

            if (lockEnabled && pinExists) {
                showScreen("LOGIN");
            } else if (!storage.hasActiveCycle()) {
                showScreen("SETUP");
            } else {
                showScreen("DASHBOARD");
            }
        });
    }

    // ======================================================
    // Navigation
    // ======================================================

    /**
     * Public navigation method called by View screens.
     * Wraps showScreen() in invokeLater so action listeners can
     * call it safely from any thread.
     */
    public void navigateTo(String screen) {
        SwingUtilities.invokeLater(() -> showScreen(screen));
    }

    /**
     * Disposes the current frame and creates the requested screen.
     * Must always run on the EDT.
     */
    private void showScreen(String screen) {
        if (currentFrame != null) {
            currentFrame.dispose();
            currentFrame = null;
        }

        switch (screen) {
            case "LOGIN"       -> currentFrame = new LoginScreen(this);
            case "SETUP"       -> currentFrame = new BudgetSetupScreen(this);
            case "DASHBOARD"   -> currentFrame = new DashboardScreen(this);
            case "ADD_EXPENSE" -> currentFrame = new AddExpenseScreen(this);
            case "HISTORY"     -> currentFrame = new TransactionHistoryScreen(this);
            case "ANALYTICS"   -> currentFrame = new AnalyticsScreen(this);
            case "SETTINGS"    -> currentFrame = new SettingsScreen(this);
            default            -> System.out.println("Unknown screen: " + screen);
        }

        if (currentFrame != null) {
            currentFrame.setVisible(true);
        }
    }

    // ======================================================
    // Cycle Operations
    // ======================================================

    /**
     * Creates a new budget cycle and navigates to the Dashboard.
     *
     * @return true if successful, false if validation fails.
     */
    public boolean createCycle(double allowance, String startDate, String endDate) {
        if (allowance <= 0)                    return false;
        if (endDate.compareTo(startDate) <= 0) return false;

        int id = storage.addCycle(allowance, startDate, endDate);
        if (id == -1) return false;

        navigateTo("DASHBOARD");
        return true;
    }

    /**
     * Deletes the current cycle and all its transactions,
     * then sends the user back to the Setup screen.
     */
    public void resetCurrentCycle() {
        int id = getActiveCycleId();
        if (id != -1) {
            storage.resetCycle(id);
        }
        navigateTo("SETUP");
    }

    // ======================================================
    // Expense Operations
    // ======================================================

    /**
     * Saves a new expense to the current cycle.
     * Delegates threshold checking to CycleManager.checkThresholdAlertService()
     * which delegates alert delivery to IAlertService.
     * AppController no longer contains any JOptionPane calls.
     *
     * @return true if saved successfully.
     */
    public boolean addExpense(double amount, String category) {
        int cycleId = getActiveCycleId();
        if (cycleId == -1) return false;
        if (amount <= 0)   return false;

        String timestamp = java.time.LocalDateTime.now()
                .format(java.time.format.DateTimeFormatter
                        .ofPattern("yyyy-MM-dd HH:mm:ss"));

        int id = storage.addTransaction(cycleId, amount, category, timestamp);
        if (id == -1) return false;

        // Delegate: CycleManager checks thresholds → IAlertService fires alert
        String[] cycle = storage.getActiveCycle();
        if (cycle != null) {
            double allowance = Double.parseDouble(cycle[1]);
            cycleManager.checkThresholdAlertService(cycleId, allowance);
        }

        return true;
    }

    /** Deletes a single expense by its database id. */
    public void deleteExpense(int transId) {
        storage.removeTransaction(transId);
    }

    /** Edits an existing expense's amount and category. */
    public void editExpense(int transId, double newAmount, String newCategory) {
        storage.editTransaction(transId, newAmount, newCategory);
    }

    // ======================================================
    // Data Getters — delegate calculations to CycleManager
    // ======================================================

    /** Returns the active cycle as String[] {id, allowance, start_date, end_date} or null. */
    public String[] getActiveCycle() {
        return storage.getActiveCycle();
    }

    /** Returns true if a cycle is currently active. */
    public boolean hasActiveCycle() {
        return storage.hasActiveCycle();
    }

    /**
     * Returns today's Safe Daily Limit.
     * Delegated to CycleManager.calculateSafeDailyLimit().
     */
    public double getDailyLimit() {
        return cycleManager.calculateSafeDailyLimit();
    }

    /**
     * Returns the remaining balance.
     * Delegated to CycleManager.getRemainingBalance().
     */
    public double getRemainingBalance() {
        return cycleManager.getRemainingBalance();
    }

    /**
     * Returns the total amount spent in this cycle.
     * Delegated to CycleManager.getTotalSpent().
     */
    public double getTotalSpent() {
        return cycleManager.getTotalSpent();
    }

    /**
     * Returns spending as a ratio between 0.0 and 1.0.
     * Delegated to CycleManager.getSpentPercentage().
     */
    public double getSpentPercentage() {
        return cycleManager.getSpentPercentage();
    }

    /**
     * Returns all transactions for the current cycle, newest first.
     * Each String[]: [0]=id, [1]=amount, [2]=category, [3]=timestamp
     */
    public List<String[]> getAllExpenses() {
        int cycleId = getActiveCycleId();
        if (cycleId == -1) return java.util.Collections.emptyList();
        return storage.getAllTransactions(cycleId);
    }

    /**
     * Returns transactions filtered by category.
     * Each String[]: [0]=id, [1]=amount, [2]=category, [3]=timestamp
     */
    public List<String[]> getExpensesByCategory(String category) {
        int cycleId = getActiveCycleId();
        if (cycleId == -1) return java.util.Collections.emptyList();
        return storage.getTransactionsByCategory(cycleId, category);
    }

    // ======================================================
    // PIN / Security
    // ======================================================

    /** Returns true if the entered PIN matches the stored one. */
    public boolean checkPin(String enteredPin) {
        return storage.checkPin(enteredPin);
    }

    /** Saves a new PIN. Call this from SettingsScreen only. */
    public void savePin(String pin) {
        storage.savePin(pin);
    }

    /** Enables or disables the privacy lock. */
    public void setPrivacyLock(boolean enabled) {
        storage.setPrivacyLock(enabled);
    }

    /** Returns true if the privacy lock is currently enabled. */
    public boolean isPrivacyLockEnabled() {
        return storage.isPrivacyLockEnabled();
    }
}