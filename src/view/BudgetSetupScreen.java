package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class BudgetSetupScreen {

    private static final Color DARK_BG = new Color(10, 15, 28);
    private static final Color NEON_GREEN = new Color(57, 255, 20);
    private static final Color FIELD_BG = new Color(15, 20, 35);
    private static final Color HINT_COLOR = new Color(100, 100, 100);

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> createAndShowGUI());
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("Spend Wise - Setup");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(450, 750);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(null);
        mainPanel.setBackground(DARK_BG);
        frame.setContentPane(mainPanel);

        // --- 1. Header Section ---
        JLabel headerLabel = new JLabel("Initialize Budget");
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        headerLabel.setForeground(Color.WHITE);
        headerLabel.setBounds(0, 50, 450, 40);
        headerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(headerLabel);

        // --- 2. Initial Allowance Field ---
        JLabel label1 = new JLabel("Initial Total Allowance (EGP)");
        label1.setFont(new Font("Segoe UI", Font.BOLD, 14));
        label1.setForeground(NEON_GREEN);
        label1.setBounds(50, 140, 350, 25);
        mainPanel.add(label1);

        /**
         * BACK-END NOTE: 
         * This is the 'INITIAL_BUDGET'. 
         * Important: Validate that this is a positive double/decimal before saving to SQLite.
         */
        JTextField budgetField = createHintField("Enter Starting Amount");
        budgetField.setBounds(50, 170, 350, 50);
        mainPanel.add(budgetField);

        // --- 3. Cycle Start Date (Safe Boundaries) ---
        JLabel label2 = new JLabel("Cycle Start Date");
        label2.setFont(new Font("Segoe UI", Font.BOLD, 14));
        label2.setForeground(NEON_GREEN);
        label2.setBounds(50, 250, 350, 25);
        mainPanel.add(label2);

        /**
         * BACK-END NOTE: 
         * Combine the selected Day, Month, and Year into a standardized Date format (e.g., YYYY-MM-DD) 
         * to be stored in the 'Cycle' table.
         */
        JPanel startPicker = createDatePicker();
        startPicker.setBounds(50, 280, 350, 50);
        mainPanel.add(startPicker);

        // --- 4. Cycle End Date (Safe Boundaries) ---
        JLabel label3 = new JLabel("Cycle End Date");
        label3.setFont(new Font("Segoe UI", Font.BOLD, 14));
        label3.setForeground(NEON_GREEN);
        label3.setBounds(50, 360, 350, 25);
        mainPanel.add(label3);

        /**
         * BACK-END NOTE: 
         * Logic Required: Ensure 'End Date' is chronologically AFTER 'Start Date'.
         * This date is crucial for calculating the 'Remaining Days' on the Dashboard.
         */
        JPanel endPicker = createDatePicker();
        endPicker.setBounds(50, 390, 350, 50);
        mainPanel.add(endPicker);

        // --- 5. Action Button (Initialization & Navigation) ---
        /**
         * BACK-END NOTE (Navigation Logic):
         * 1. On Click: Collect all 'Initial' data.
         * 2. Calculate initial Safe Daily Limit: (Initial Budget / Total Days in Cycle).
         * 3. Insert into SQLite 'Settings' or 'Cycle' table.
         * 4. Navigation: thisFrame.dispose() -> new DashboardScreen().setVisible(true).
         */
        JButton startBtn = new JButton("START CYCLE");
        startBtn.setFont(new Font("Segoe UI", Font.BOLD, 18));
        startBtn.setForeground(DARK_BG);
        startBtn.setBackground(NEON_GREEN);
        startBtn.setFocusPainted(false);
        startBtn.setBorderPainted(false);
        startBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        startBtn.setBounds(50, 520, 350, 60);
        mainPanel.add(startBtn);

        frame.setVisible(true);
    }

    private static JTextField createHintField(String hint) {
        JTextField field = new JTextField(hint);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        field.setForeground(HINT_COLOR);
        field.setBackground(FIELD_BG);
        field.setCaretColor(NEON_GREEN);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(60, 65, 80), 1),
                BorderFactory.createEmptyBorder(0, 15, 0, 15)
        ));

        field.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (field.getText().equals(hint)) {
                    field.setText("");
                    field.setForeground(Color.WHITE);
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                if (field.getText().isEmpty()) {
                    field.setText(hint);
                    field.setForeground(HINT_COLOR);
                }
            }
        });
        return field;
    }

    private static JPanel createDatePicker() {
        JPanel panel = new JPanel(new GridLayout(1, 3, 10, 0));
        panel.setBackground(DARK_BG);

        String[] days = new String[31];
        for (int i = 0; i < 31; i++) days[i] = String.format("%02d", i + 1);
        String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        String[] years = {"2026", "2027", "2028"};

        panel.add(createStyledCombo(days));
        panel.add(createStyledCombo(months));
        panel.add(createStyledCombo(years));

        return panel;
    }

    private static JComboBox<String> createStyledCombo(String[] items) {
        JComboBox<String> combo = new JComboBox<>(items);
        combo.setBackground(FIELD_BG);
        combo.setForeground(Color.WHITE);
        combo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        combo.setFocusable(false);
        combo.setBorder(BorderFactory.createLineBorder(new Color(60, 65, 80), 1));
        return combo;
    }
}