package view;

import Controller.AppController;
import javax.swing.*;
import java.awt.*;

/**
 * BudgetSetupScreen.java
 * Lets the user enter allowance + start/end dates to create a cycle.
 */
public class BudgetSetupScreen extends JFrame {

    private static final Color DARK_BG    = new Color(10, 15, 28);
    private static final Color NEON_GREEN = new Color(57, 255, 20);
    private static final Color FIELD_BG   = new Color(20, 25, 40);

    private AppController ctrl;

    // Date picker combos
    private JComboBox<String> startDay, startMonth, startYear;
    private JComboBox<String> endDay,   endMonth,   endYear;
    private JTextField        budgetField;

    public BudgetSetupScreen(AppController ctrl) {
        this.ctrl = ctrl;
        buildUI();
    }

    private void buildUI() {
        setTitle("Masroofy - Setup");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(450, 750);
        setResizable(false);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(null);
        panel.setBackground(DARK_BG);
        setContentPane(panel);

        // Header
        JLabel header = new JLabel("Initialize Budget");
        header.setFont(new Font("Segoe UI", Font.BOLD, 26));
        header.setForeground(Color.WHITE);
        header.setBounds(0, 50, 450, 40);
        header.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(header);

        // Budget amount
        JLabel lbl1 = new JLabel("Total Allowance (EGP)");
        lbl1.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lbl1.setForeground(NEON_GREEN);
        lbl1.setBounds(50, 130, 350, 25);
        panel.add(lbl1);

        budgetField = new JTextField();
        budgetField.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        budgetField.setForeground(Color.WHITE);
        budgetField.setBackground(FIELD_BG);
        budgetField.setCaretColor(NEON_GREEN);
        budgetField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(60, 65, 80), 1),
                BorderFactory.createEmptyBorder(0, 15, 0, 15)));
        budgetField.setBounds(50, 160, 350, 50);
        panel.add(budgetField);

        // Start date
        JLabel lbl2 = new JLabel("Cycle Start Date");
        lbl2.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lbl2.setForeground(NEON_GREEN);
        lbl2.setBounds(50, 240, 350, 25);
        panel.add(lbl2);

        JPanel startPanel = buildDatePicker(true);
        startPanel.setBounds(50, 270, 350, 45);
        panel.add(startPanel);

        // End date
        JLabel lbl3 = new JLabel("Cycle End Date");
        lbl3.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lbl3.setForeground(NEON_GREEN);
        lbl3.setBounds(50, 345, 350, 25);
        panel.add(lbl3);

        JPanel endPanel = buildDatePicker(false);
        endPanel.setBounds(50, 375, 350, 45);
        panel.add(endPanel);

        // Error label
        JLabel errorLabel = new JLabel("");
        errorLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        errorLabel.setForeground(Color.RED);
        errorLabel.setBounds(50, 440, 350, 20);
        panel.add(errorLabel);

        // Start button
        JButton startBtn = new JButton("START CYCLE");
        startBtn.setFont(new Font("Segoe UI", Font.BOLD, 18));
        startBtn.setForeground(DARK_BG);
        startBtn.setBackground(NEON_GREEN);
        startBtn.setFocusPainted(false);
        startBtn.setBorderPainted(false);
        startBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        startBtn.setBounds(50, 490, 350, 60);
        panel.add(startBtn);

        startBtn.addActionListener(e -> {
            errorLabel.setText("");

            // Validate amount
            double allowance;
            try {
                allowance = Double.parseDouble(budgetField.getText().trim());
                if (allowance <= 0) throw new NumberFormatException();
            } catch (NumberFormatException ex) {
                errorLabel.setText("Please enter a valid positive amount.");
                return;
            }

            // Build date strings
            String startDate = buildDate(startYear, startMonth, startDay);
            String endDate   = buildDate(endYear,   endMonth,   endDay);

            if (endDate.compareTo(startDate) <= 0) {
                errorLabel.setText("End date must be after start date.");
                return;
            }

            boolean ok = ctrl.createCycle(allowance, startDate, endDate);
            if (!ok) {
                errorLabel.setText("Failed to create cycle. Try again.");
            }
        });
    }

    private JPanel buildDatePicker(boolean isStart) {
        JPanel p = new JPanel(new GridLayout(1, 3, 8, 0));
        p.setBackground(DARK_BG);

        String[] days   = new String[31];
        for (int i = 0; i < 31; i++) {
            // Forcing US locale ensures standard 0-9 digits are always generated
            days[i] = String.format(java.util.Locale.US, "%02d", i + 1);
        }
        String[] months = {"01","02","03","04","05","06","07","08","09","10","11","12"};
        String[] years  = {"2026","2027","2028"};

        JComboBox<String> d = styledCombo(days);
        JComboBox<String> m = styledCombo(months);
        JComboBox<String> y = styledCombo(years);

        if (isStart) { startDay = d; startMonth = m; startYear = y; }
        else         { endDay   = d; endMonth   = m; endYear   = y; }

        p.add(d); p.add(m); p.add(y);
        return p;
    }

    private JComboBox<String> styledCombo(String[] items) {
        JComboBox<String> c = new JComboBox<>(items);
        c.setBackground(new Color(20, 25, 40));
        c.setForeground(Color.WHITE);
        c.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        return c;
    }

    private String buildDate(JComboBox<String> y, JComboBox<String> m, JComboBox<String> d) {
        return y.getSelectedItem() + "-" + m.getSelectedItem() + "-" + d.getSelectedItem();
    }
}