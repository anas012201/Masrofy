package interfaces;

/**
 * IAlertService.java
 *
 * Interface that defines the contract for the alert/notification system.
 *
 * SOLID — Open/Closed Principle:
 *   New alert types (SMS, push notification, email) can be added by
 *   implementing this interface without touching any existing code.
 *
 * Design Pattern — Strategy:
 *   CycleManager depends on this interface, not on any concrete alert
 *   class. The actual delivery mechanism is swappable at runtime.
 *
 * Design Pattern — Observer:
 *   CycleManager acts as the subject. When budget state changes,
 *   it calls these methods to notify the alert service (the observer).
 */
public interface IAlertService {

    /**
     * Called when the user's total spending reaches or exceeds 80%
     * of their total cycle allowance.
     *
     * @param remainingAmount  The EGP amount still available to spend
     */
    void sendLowBalanceAlert(double remainingAmount);

    /**
     * Called when the user's total spending reaches or exceeds 100%
     * of their total cycle allowance (budget fully exhausted).
     */
    void sendCycleLimitAlert();

    /**
     * General-purpose notification method for any custom message.
     *
     * @param message  The message to display to the user
     */
    void notifyUser(String message);
}