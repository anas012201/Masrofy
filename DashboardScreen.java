import javax.swing.*;
import java.awt.*;

public class DashboardScreen {

    private static final Color DARK_BG = new Color(10, 15, 28);
    private static final Color NEON_GREEN = new Color(57, 255, 20);
    private static final Color CARD_BG = new Color(20, 25, 40);

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> createAndShowGUI());
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("Spend Wise - Dashboard");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(450, 750);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(null);
        mainPanel.setBackground(DARK_BG);
        frame.setContentPane(mainPanel);

        // --- 1. Header Section ---
        // BACK-END NOTE: Fetch User Name from session/SQLite (User table)
        JLabel welcomeLabel = new JLabel("Hello, User!"); 
        welcomeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        welcomeLabel.setForeground(new Color(150, 150, 150));
        welcomeLabel.setBounds(30, 30, 200, 25);
        mainPanel.add(welcomeLabel);

        // BACK-END NOTE: Dynamic Cycle Information (Current Month/Year or custom range)
        JLabel cycleLabel = new JLabel("Cycle: May 2026");
        cycleLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        cycleLabel.setForeground(NEON_GREEN);
        cycleLabel.setBounds(30, 55, 250, 25);
        mainPanel.add(cycleLabel);

        // --- 2. Safe Daily Limit Card ---
        JPanel limitCard = new JPanel(null);
        limitCard.setBackground(CARD_BG);
        limitCard.setBounds(30, 100, 390, 180);
        limitCard.setBorder(BorderFactory.createLineBorder(new Color(40, 45, 60), 1));
        
        JLabel limitTitle = new JLabel("SAFE DAILY LIMIT");
        limitTitle.setFont(new Font("Segoe UI", Font.BOLD, 12));
        limitTitle.setForeground(new Color(120, 130, 150));
        limitTitle.setBounds(0, 30, 390, 20);
        limitTitle.setHorizontalAlignment(SwingConstants.CENTER);
        limitCard.add(limitTitle);

        /**
         * BACK-END LOGIC REQUIRED:
         * Formula: limitAmount = (Current Remaining Balance) / (Remaining Days in Cycle)
         * - Remaining Days = Current Date - Cycle End Date (stored in DB).
         * - This value should refresh every time an expense is added.
         */
        float initialValue=150.00f;
        JLabel limitAmount = new JLabel(initialValue+" EGP");
        limitAmount.setFont(new Font("Segoe UI", Font.BOLD, 42));
        limitAmount.setForeground(NEON_GREEN);
        limitAmount.setBounds(0, 60, 390, 50);
        limitAmount.setHorizontalAlignment(SwingConstants.CENTER);
        limitCard.add(limitAmount);

        JLabel limitHint = new JLabel("Amount you can spend today safely");
        limitHint.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        limitHint.setForeground(new Color(100, 100, 100));
        limitHint.setBounds(0, 120, 390, 20);
        limitHint.setHorizontalAlignment(SwingConstants.CENTER);
        limitCard.add(limitHint);
        
        mainPanel.add(limitCard);

        // --- 3. Balance Information ---
        JLabel balanceTitle = new JLabel("Total Remaining Balance:");
        balanceTitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        balanceTitle.setForeground(Color.WHITE);
        balanceTitle.setBounds(30, 310, 250, 25);
        mainPanel.add(balanceTitle);

        /**
         * BACK-END NOTE: 
         * Calculation: Initial Allowance (from Setup) - Total Sum of Expenses (from Expenses table)
         */
        JLabel balanceValue = new JLabel("3,450.00 EGP");
        balanceValue.setFont(new Font("Segoe UI", Font.BOLD, 22));
        balanceValue.setForeground(Color.WHITE);
        balanceValue.setBounds(30, 335, 250, 35);
        mainPanel.add(balanceValue);

        // --- 4. Consumption Progress Bar ---
        /**
         * BACK-END LOGIC REQUIRED:
         * Percentage = (Total Spent / Initial Allowance) * 100
         * If Percentage > 80%, trigger AlertService (as per documentation).
         */
        JProgressBar budgetProgress = new JProgressBar(0, 100);
        budgetProgress.setValue(40); 
        budgetProgress.setForeground(NEON_GREEN);
        budgetProgress.setBackground(new Color(40, 45, 60));
        budgetProgress.setBorderPainted(false);
        budgetProgress.setBounds(30, 390, 390, 12);
        mainPanel.add(budgetProgress);
        
        
        JLabel progressText = new JLabel("40% of total budget used");
        progressText.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        progressText.setForeground(new Color(150, 150, 150));
        progressText.setBounds(30, 410, 250, 20);
        mainPanel.add(progressText);

        // --- 5. Navigation & Linking Buttons ---
        
        /**
         * PAGE NAVIGATION (Back-end/Controller Task):
         * On Click:
         * 1. Open 'Rapid Expense Entry' Dialog/Frame.
         * 2. After submission, REFRESH the Dashboard data (Limit & Balance).
         */
        JButton addBtn = new JButton("+ ADD EXPENSE");
        addBtn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        addBtn.setForeground(DARK_BG);
        addBtn.setBackground(NEON_GREEN);
        addBtn.setFocusPainted(false);
        addBtn.setBorderPainted(false);
        addBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        addBtn.setBounds(30, 480, 390, 60);
        mainPanel.add(addBtn);

        /**
         * PAGE NAVIGATION (Back-end/Controller Task):
         * On Click:
         * 1. Instantiate 'TransactionHistory' Screen.
         * 2. currentFrame.dispose() -> nextFrame.setVisible(true).
         */
        JButton historyBtn = new JButton("TRANSACTION HISTORY");
        historyBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        historyBtn.setForeground(Color.WHITE);
        historyBtn.setBackground(CARD_BG);
        historyBtn.setFocusPainted(false);
        historyBtn.setBorder(BorderFactory.createLineBorder(NEON_GREEN, 1));
        historyBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        historyBtn.setBounds(30, 555, 390, 50);
        mainPanel.add(historyBtn);

        frame.setVisible(true);
    }
}