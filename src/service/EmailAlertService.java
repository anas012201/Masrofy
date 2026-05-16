package service;

import interfaces.IAlertService;
import javax.swing.JOptionPane;

/**
 * EmailAlertService.java
 *
 * Concrete implementation of IAlertService.
 * Handles the actual delivery of budget alerts to the user.
 *
 * Currently, delivers alerts via JOptionPane dialogs (local UI popups).
 * The name "EmailAlertService" follows the SDS class diagram naming.
 * It can be extended later to also send email/push notifications
 * without changing any code that depends on IAlertService.
 *
 * SOLID — Single Responsibility Principle:
 *   This class has one job: deliver notifications.
 *   Alert logic was previously mixed into AppController.addExpense(),
 *   violating SRP. It now lives here exclusively.
 *
 * SOLID — Open/Closed Principle:
 *   To add a new delivery channel (e.g. SMS), create a new class
 *   that implements IAlertService. Do not modify this class.
 *
 * Design Pattern — Strategy:
 *   This is one concrete strategy. CycleManager uses IAlertService,
 *   so swapping EmailAlertService for any other implementation
 *   requires zero changes to CycleManager or AppController.
 */
public class EmailAlertService implements IAlertService {

    /**
     * Shows a warning dialog when 80% of the budget has been spent.
     *
     * @param remainingAmount  The EGP amount still available to spend
     */
    @Override
    public void sendLowBalanceAlert(double remainingAmount) {
        JOptionPane.showMessageDialog(
                null,
                String.format(
                        "⚠️ Warning: You have used 80%% of your allowance!\n" +
                                "Remaining: %.2f EGP", remainingAmount),
                "Budget Alert",
                JOptionPane.WARNING_MESSAGE
        );
    }

    /**
     * Shows an error dialog when the budget has been fully exhausted.
     */
    @Override
    public void sendCycleLimitAlert() {
        JOptionPane.showMessageDialog(
                null,
                "⚠️ Budget Exhausted! You have used 100% of your allowance.",
                "Budget Alert",
                JOptionPane.ERROR_MESSAGE
        );
    }

    /**
     * Shows a general-purpose informational dialog.
     *
     * @param message  The message to display
     */
    @Override
    public void notifyUser(String message) {
        JOptionPane.showMessageDialog(
                null,
                message,
                "Masroofy",
                JOptionPane.INFORMATION_MESSAGE
        );
    }
}