package view;

import Controller.AppController;
import javax.swing.*;
import java.awt.*;

/**
 * DashboardScreen.java
 * Main screen. Shows balance, daily limit, progress bar.
 * All values come from AppController.
 */
public class DashboardScreen extends JFrame {

    private static final Color DARK_BG    = new Color(10, 15, 28);
    private static final Color NEON_GREEN = new Color(57, 255, 20);
    private static final Color CARD_BG    = new Color(20, 25, 40);
    private static final Color ORANGE     = new Color(255, 159, 64);

    private AppController ctrl;

    // Labels we need to update dynamically
    private JLabel        limitAmount;
    private JLabel        balanceValue;
    private JLabel        progressText;
    private JLabel        cycleLabel;
    private JProgressBar  budgetProgress;

    public DashboardScreen(AppController ctrl) {
        this.ctrl = ctrl;
        buildUI();
        refreshData();   // load real data from DB
    }

    private void buildUI() {
        setTitle("Masroofy - Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(450, 750);
        setResizable(false);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(null);
        panel.setBackground(DARK_BG);
        setContentPane(panel);

        // Header
        JLabel welcome = new JLabel("Good day!");
        welcome.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        welcome.setForeground(new Color(150, 150, 150));
        welcome.setBounds(30, 30, 200, 25);
        panel.add(welcome);

        cycleLabel = new JLabel("Loading...");
        cycleLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        cycleLabel.setForeground(NEON_GREEN);
        cycleLabel.setBounds(30, 55, 250, 25);
        panel.add(cycleLabel);

        // Daily limit card
        JPanel limitCard = new JPanel(null);
        limitCard.setBackground(CARD_BG);
        limitCard.setBounds(30, 100, 390, 170);
        limitCard.setBorder(BorderFactory.createLineBorder(new Color(40, 45, 60), 1));
        panel.add(limitCard);

        JLabel limitTitle = new JLabel("SAFE DAILY LIMIT");
        limitTitle.setFont(new Font("Segoe UI", Font.BOLD, 12));
        limitTitle.setForeground(new Color(120, 130, 150));
        limitTitle.setBounds(0, 25, 390, 20);
        limitTitle.setHorizontalAlignment(SwingConstants.CENTER);
        limitCard.add(limitTitle);

        limitAmount = new JLabel("0.00 EGP");
        limitAmount.setFont(new Font("Segoe UI", Font.BOLD, 42));
        limitAmount.setForeground(NEON_GREEN);
        limitAmount.setBounds(0, 55, 390, 55);
        limitAmount.setHorizontalAlignment(SwingConstants.CENTER);
        limitCard.add(limitAmount);

        JLabel hint = new JLabel("Amount you can spend today safely");
        hint.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        hint.setForeground(new Color(100, 100, 100));
        hint.setBounds(0, 120, 390, 20);
        hint.setHorizontalAlignment(SwingConstants.CENTER);
        limitCard.add(hint);

        // Balance
        JLabel balanceTitle = new JLabel("Total Remaining Balance:");
        balanceTitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        balanceTitle.setForeground(Color.WHITE);
        balanceTitle.setBounds(30, 300, 250, 25);
        panel.add(balanceTitle);

        balanceValue = new JLabel("0.00 EGP");
        balanceValue.setFont(new Font("Segoe UI", Font.BOLD, 26));
        balanceValue.setForeground(Color.WHITE);
        balanceValue.setBounds(30, 325, 300, 35);
        panel.add(balanceValue);

        // Progress bar
        budgetProgress = new JProgressBar(0, 100);
        budgetProgress.setValue(0);
        budgetProgress.setForeground(NEON_GREEN);
        budgetProgress.setBackground(new Color(40, 45, 60));
        budgetProgress.setBorderPainted(false);
        budgetProgress.setBounds(30, 380, 390, 12);
        panel.add(budgetProgress);

        progressText = new JLabel("0% of total budget used");
        progressText.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        progressText.setForeground(new Color(150, 150, 150));
        progressText.setBounds(30, 400, 250, 20);
        panel.add(progressText);

        // Add Expense button
        JButton addBtn = new JButton("+ ADD EXPENSE");
        addBtn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        addBtn.setForeground(DARK_BG);
        addBtn.setBackground(NEON_GREEN);
        addBtn.setFocusPainted(false);
        addBtn.setBorderPainted(false);
        addBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        addBtn.setBounds(30, 460, 390, 60);
        addBtn.addActionListener(e -> ctrl.navigateTo("ADD_EXPENSE"));
        panel.add(addBtn);

        // History button
        JButton historyBtn = new JButton("TRANSACTION HISTORY");
        historyBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        historyBtn.setForeground(Color.WHITE);
        historyBtn.setBackground(CARD_BG);
        historyBtn.setFocusPainted(false);
        historyBtn.setBorder(BorderFactory.createLineBorder(NEON_GREEN, 1));
        historyBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        historyBtn.setBounds(30, 535, 185, 50);
        historyBtn.addActionListener(e -> ctrl.navigateTo("HISTORY"));
        panel.add(historyBtn);

        // Analytics button
        JButton analyticsBtn = new JButton("ANALYTICS");
        analyticsBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        analyticsBtn.setForeground(Color.WHITE);
        analyticsBtn.setBackground(CARD_BG);
        analyticsBtn.setFocusPainted(false);
        analyticsBtn.setBorder(BorderFactory.createLineBorder(NEON_GREEN, 1));
        analyticsBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        analyticsBtn.setBounds(235, 535, 185, 50);
        analyticsBtn.addActionListener(e -> ctrl.navigateTo("ANALYTICS"));
        panel.add(analyticsBtn);

        // Settings button
        JButton settingsBtn = new JButton("⚙ SETTINGS");
        settingsBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        settingsBtn.setForeground(Color.WHITE);
        settingsBtn.setBackground(CARD_BG);
        settingsBtn.setFocusPainted(false);
        settingsBtn.setBorder(BorderFactory.createLineBorder(new Color(60, 65, 80), 1));
        settingsBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        settingsBtn.setBounds(30, 600, 390, 50);
        settingsBtn.addActionListener(e -> ctrl.navigateTo("SETTINGS"));
        panel.add(settingsBtn);
    }

    /**
     * Pulls real data from the controller and updates all labels.
     */
    private void refreshData() {
        String[] cycle = ctrl.getActiveCycle();
        if (cycle != null) {
            cycleLabel.setText("Cycle: " + cycle[2] + " → " + cycle[3]);
        }

        double dailyLimit  = ctrl.getDailyLimit();
        double remaining   = ctrl.getRemainingBalance();
        double percentage  = ctrl.getSpentPercentage();
        int    pct         = (int)(percentage * 100);

        limitAmount.setText(String.format("%.2f EGP", dailyLimit));
        balanceValue.setText(String.format("%.2f EGP", remaining));
        budgetProgress.setValue(Math.min(pct, 100));
        progressText.setText(pct + "% of total budget used");

        // Change color based on usage
        if (pct >= 100) {
            limitAmount.setForeground(Color.RED);
            budgetProgress.setForeground(Color.RED);
        } else if (pct >= 80) {
            limitAmount.setForeground(ORANGE);
            budgetProgress.setForeground(ORANGE);
        } else {
            limitAmount.setForeground(new Color(57, 255, 20));
            budgetProgress.setForeground(new Color(57, 255, 20));
        }
    }
}