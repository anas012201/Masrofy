package service;

import interfaces.IAlertService;
import interfaces.IDataStorage;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * CycleManager.java
 *
 * The core business logic engine for the Masroofy app.
 * Responsible for all financial calculations related to the active cycle.
 */
public class CycleManager {

    // Dependencies — injected via constructor (DIP)
    private final IDataStorage  storage;
    private final IAlertService alertService;

    // ======================================================
    // Constructor
    // ======================================================

    /**
     * @param storage       The data persistence layer (e.g. SQLiteStorage)
     * @param alertService  The notification delivery layer (e.g. EmailAlertService)
     */
    public CycleManager(IDataStorage storage, IAlertService alertService) {
        this.storage      = storage;
        this.alertService = alertService;
    }

    // ======================================================
    // Digit Normalizer Helper
    // ======================================================

    /**
     * Converts Eastern Arabic-Indic digits (٠-٩) and Persian digits (۰-۹)
     * into standard ASCII Western digits (0-9).
     */
    private String normalizeDigits(String str) {
        if (str == null) return null;
        StringBuilder sb = new StringBuilder();
        for (char ch : str.toCharArray()) {
            if (ch >= '٠' && ch <= '٩') {
                sb.append((char) (ch - '٠' + '0'));
            } else if (ch >= '۰' && ch <= '۹') {
                sb.append((char) (ch - '۰' + '0'));
            } else {
                sb.append(ch);
            }
        }
        return sb.toString();
    }

    // ======================================================
    // Safe Daily Limit
    // ======================================================

//    /**
//     * Calculates today's Safe Daily Limit.
//     *
//     * Formula: $\frac{\text{remainingBalance}}{\text{remainingDays}}$
//     *
//     * Uses a minimum of 1 day to prevent division by zero on the
//     * final day of the cycle.
//     *
//     * @return The safe amount to spend today in EGP, or 0 if no cycle.
//     */
//    public double calculateSafeDailyLimit() {
//        String[] cycle = storage.getActiveCycle();
//        if (cycle == null) return 0;
//
//        int    cycleId       = Integer.parseInt(cycle[0]);
//        double allowance     = Double.parseDouble(cycle[1]);
//        double totalSpent    = storage.getTotalSpent(cycleId);
//        double remaining     = allowance - totalSpent;
//
//        LocalDate today   = LocalDate.now();
//
//        // Normalize the date string to recover safely from existing database records
//        String cleanEndDate = normalizeDigits(cycle[3]);
//        LocalDate endDate   = LocalDate.parse(cleanEndDate);
//
//        long remainingDays = ChronoUnit.DAYS.between(today, endDate) + 1;
//        if (remainingDays <= 0) remainingDays = 1;
//
//        return remaining / remainingDays;
//    }
    /**
     * Calculates today's Safe Daily Limit according to State Rules:
     * - If the End Date has passed (ENDED state), return 0.
     * - If the budget is fully exhausted (EXHAUSTED state), return 0.
     * - Otherwise, divide remaining balance by remaining cycle days.
     *
     * @return The safe amount to spend today in EGP, or 0 if no cycle/exhausted/ended.
     */
    public double calculateSafeDailyLimit() {
        String[] cycle = storage.getActiveCycle();
        if (cycle == null) return 0;

        int    cycleId       = Integer.parseInt(cycle[0]);
        double allowance     = Double.parseDouble(cycle[1]);
        double totalSpent    = storage.getTotalSpent(cycleId);
        double remaining     = allowance - totalSpent;

        // 1. Check for EXHAUSTED State: If remaining funds are zero or negative
        if (remaining <= 0) {
            return 0.0;
        }

        LocalDate today = LocalDate.now();
        String cleanEndDate = normalizeDigits(cycle[3]);
        LocalDate endDate   = LocalDate.parse(cleanEndDate);

        // 2. Check for ENDED State: If current system date is past the end date
        if (today.isAfter(endDate)) {
            return 0.0;
        }

        long remainingDays = ChronoUnit.DAYS.between(today, endDate) + 1;
        if (remainingDays <= 0) remainingDays = 1;

        return remaining / remainingDays;
    }



    // ======================================================
    // Rollover
    // ======================================================

    /**
     * Handles the daily rollover logic.
     *
     * @return The new Safe Daily Limit after rollover.
     */
    public double rolloverRemainingBalance() {
        return calculateSafeDailyLimit();
    }

    // ======================================================
    // Threshold Alert Check
    // ======================================================

    /**
     * Checks the current spending percentage and fires the appropriate
     * alert via IAlertService if a threshold has been crossed.
     *
     * Thresholds (from SRS FR-6 and SDS):
     * $\ge 100\%$ → sendCycleLimitAlert()  (budget exhausted)
     * $\ge 80\%$ → sendLowBalanceAlert()  (early warning)
     *
     * @param cycleId   The active cycle's database id
     * @param allowance The total allowance for this cycle
     */
    public void checkThresholdAlertService(int cycleId, double allowance) {
        if (allowance <= 0) return;

        double totalSpent = storage.getTotalSpent(cycleId);
        double remaining  = allowance - totalSpent;
        double percentage = totalSpent / allowance;

        if (percentage >= 1.0) {
            alertService.sendCycleLimitAlert();
        } else if (percentage >= 0.8) {
            alertService.sendLowBalanceAlert(remaining);
        }
    }

    // ======================================================
    // Convenience Getters (used by AppController)
    // ======================================================

    /**
     * Returns the remaining balance for the active cycle.
     *
     * @return Remaining EGP, or 0 if no cycle.
     */
    public double getRemainingBalance() {
        String[] cycle = storage.getActiveCycle();
        if (cycle == null) return 0;

        int    cycleId    = Integer.parseInt(cycle[0]);
        double allowance  = Double.parseDouble(cycle[1]);
        double totalSpent = storage.getTotalSpent(cycleId);
        return allowance - totalSpent;
    }

    /**
     * Returns spending as a ratio between 0.0 and 1.0.
     * Example: 0.8 = 80% of the allowance has been spent.
     *
     * @return Percentage as a decimal, or 0 if no cycle.
     */
    public double getSpentPercentage() {
        String[] cycle = storage.getActiveCycle();
        if (cycle == null) return 0;

        int    cycleId   = Integer.parseInt(cycle[0]);
        double allowance = Double.parseDouble(cycle[1]);
        if (allowance == 0) return 0;

        return storage.getTotalSpent(cycleId) / allowance;
    }

    /**
     * Returns the total amount spent in the active cycle.
     *
     * @return Total spent in EGP, or 0 if no cycle.
     */
    public double getTotalSpent() {
        String[] cycle = storage.getActiveCycle();
        if (cycle == null) return 0;

        int cycleId = Integer.parseInt(cycle[0]);
        return storage.getTotalSpent(cycleId);
    }
}