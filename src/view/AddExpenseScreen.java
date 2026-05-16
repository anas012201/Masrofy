package view;

import Controller.AppController;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * AddExpenseScreen.java
 * User enters an amount and picks a category.
 * Saves via AppController.
 */
public class AddExpenseScreen extends JFrame {

    private static final Color DARK_BG    = new Color(15, 15, 25);
    private static final Color NEON_GREEN = new Color(57, 255, 20);
    private static final Color FIELD_BG   = new Color(30, 30, 45);

    private AppController ctrl;
    private JButton       selectedCategoryBtn = null;
    private JTextField    amountField;

    public AddExpenseScreen(AppController ctrl) {
        this.ctrl = ctrl;
        buildUI();
    }

    private void buildUI() {
        setTitle("Masroofy - Add Expense");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(450, 700);
        setResizable(false);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(null);
        panel.setBackground(DARK_BG);
        setContentPane(panel);

        // Header
        JLabel header = new JLabel("Add Expense");
        header.setFont(new Font("Segoe UI", Font.BOLD, 24));
        header.setForeground(Color.WHITE);
        header.setBounds(20, 25, 250, 35);
        panel.add(header);

        // Amount label
        JLabel lbl1 = new JLabel("AMOUNT (EGP)");
        lbl1.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lbl1.setForeground(NEON_GREEN);
        lbl1.setBounds(20, 80, 200, 20);
        panel.add(lbl1);

        // Amount field
        amountField = new JTextField();
        amountField.setBackground(FIELD_BG);
        amountField.setForeground(Color.WHITE);
        amountField.setFont(new Font("Segoe UI", Font.BOLD, 28));
        amountField.setCaretColor(NEON_GREEN);
        amountField.setBorder(BorderFactory.createLineBorder(new Color(50, 50, 70)));
        amountField.setBounds(20, 105, 395, 60);
        panel.add(amountField);

        // Category label
        JLabel lbl2 = new JLabel("SELECT CATEGORY");
        lbl2.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lbl2.setForeground(NEON_GREEN);
        lbl2.setBounds(20, 190, 200, 20);
        panel.add(lbl2);

        // Category grid
        JPanel grid = new JPanel(new GridLayout(2, 3, 8, 8));
        grid.setBackground(DARK_BG);
        grid.setBounds(20, 215, 395, 110);

        String[] categories = {"Food", "Transport", "Shopping", "Bills", "Health", "Other"};
        for (String cat : categories) {
            grid.add(makeCategoryBtn(cat));
        }
        panel.add(grid);

        // Error label
        JLabel errorLabel = new JLabel("");
        errorLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        errorLabel.setForeground(Color.RED);
        errorLabel.setBounds(20, 340, 395, 20);
        panel.add(errorLabel);

        // Save button
        JButton saveBtn = new JButton("SAVE EXPENSE");
        saveBtn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        saveBtn.setBackground(NEON_GREEN);
        saveBtn.setForeground(DARK_BG);
        saveBtn.setFocusPainted(false);
        saveBtn.setBorderPainted(false);
        saveBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        saveBtn.setBounds(20, 390, 395, 60);
        panel.add(saveBtn);

        saveBtn.addActionListener(e -> {
            errorLabel.setText("");

            // Validate amount
            double amount;
            try {
                amount = Double.parseDouble(amountField.getText().trim());
                if (amount <= 0) throw new NumberFormatException();
            } catch (NumberFormatException ex) {
                errorLabel.setText("Please enter a valid positive amount.");
                return;
            }

            // Validate category
            if (selectedCategoryBtn == null) {
                errorLabel.setText("Please select a category.");
                return;
            }

            String category = selectedCategoryBtn.getText();
            boolean ok = ctrl.addExpense(amount, category);

            if (ok) {
                ctrl.navigateTo("DASHBOARD");
            } else {
                errorLabel.setText("Failed to save. Please try again.");
            }
        });

        // Back button
        JButton backBtn = new JButton("← BACK");
        backBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        backBtn.setForeground(Color.WHITE);
        backBtn.setBackground(new Color(30, 30, 45));
        backBtn.setFocusPainted(false);
        backBtn.setBorder(BorderFactory.createLineBorder(new Color(60, 60, 80)));
        backBtn.setBounds(20, 470, 395, 50);
        backBtn.addActionListener(e -> ctrl.navigateTo("DASHBOARD"));
        panel.add(backBtn);
    }

    private JButton makeCategoryBtn(String text) {
        JButton btn = new JButton(text);
        btn.setBackground(FIELD_BG);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createLineBorder(new Color(60, 60, 80)));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedCategoryBtn == btn) {
                    // Deselect
                    btn.setBackground(FIELD_BG);
                    btn.setForeground(Color.WHITE);
                    selectedCategoryBtn = null;
                } else {
                    // Deselect previous
                    if (selectedCategoryBtn != null) {
                        selectedCategoryBtn.setBackground(FIELD_BG);
                        selectedCategoryBtn.setForeground(Color.WHITE);
                    }
                    // Select this one
                    btn.setBackground(NEON_GREEN);
                    btn.setForeground(new Color(15, 15, 25));
                    selectedCategoryBtn = btn;
                }
            }
        });
        return btn;
    }
}