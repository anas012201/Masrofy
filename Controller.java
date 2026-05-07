import javax.swing.*;

/**
 * PROJECT: Masroofy - Navigation & State Controller
 * BACK-END TEAM: This class defines the flow of the application.
 */
public class Controller {

    private static JFrame currentFrame;

    public static void main(String[] args) {
        // STEP 1: Bootstrapping
        // BACK-END NOTE: Check if SQLite database exists and has a configured PIN.
        // If (Database.isEmpty()) -> Show SetupScreen
        // Else -> Show PinScreen
        showPinScreen();
    }

    public static void showPinScreen() {
        // Logic to display the PIN screen
        System.out.println("Switching to: PIN_SCREEN");
    }

    public static void showDashboard() {
        /**
         * BACK-END NOTE (State Management):
         * Before showing Dashboard, execute:
         * 1. SELECT * FROM Cycle WHERE isCurrent = 1;
         * 2. Calculate remaining_balance and safe_daily_limit.
         * 3. Pass results to Dashboard UI.
         */
        System.out.println("Switching to: DASHBOARD_MAIN");
    }

    public static void showTransactionHistory() {
        /**
         * BACK-END NOTE:
         * Query: SELECT * FROM Expenses ORDER BY date DESC;
         * Load into JTable.
         */
        System.out.println("Switching to: TRANSACTION_HISTORY");
    }

    /**
     * CENTRAL NAVIGATION METHOD
     * This handles window transitions to keep memory clean.
     */
    public static void navigateTo(JFrame nextFrame) {
        if (currentFrame != null) {
            currentFrame.dispose(); // Close current window to save resources
        }
        currentFrame = nextFrame;
        currentFrame.setVisible(true);
    }
}