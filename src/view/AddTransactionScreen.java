package view;

import controller.Controller;
import model.Transaction;
import javax.swing.*;
import java.awt.*;

public class AddTransactionScreen extends JFrame {
    public AddTransactionScreen(Controller controller, TransactionHistoryScreen historyScreen) {
        setTitle("Add New Transaction");
        setSize(400, 500);
        setLayout(null);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(10, 15, 30));

        JLabel lblAmt = new JLabel("Amount:");
        lblAmt.setForeground(Color.WHITE);
        lblAmt.setBounds(50, 30, 100, 30);
        add(lblAmt);

        JTextField amountField = new JTextField();
        amountField.setBounds(50, 60, 300, 40);
        add(amountField);

        JLabel lblCat = new JLabel("Category (e.g. Food, Salary):");
        lblCat.setForeground(Color.WHITE);
        lblCat.setBounds(50, 120, 200, 30);
        add(lblCat);

        JTextField catField = new JTextField();
        catField.setBounds(50, 150, 300, 40);
        add(catField);

        JLabel lblType = new JLabel("Type:");
        lblType.setForeground(Color.WHITE);
        lblType.setBounds(50, 210, 100, 30);
        add(lblType);

        String[] types = {"EXPENSE", "INCOME"};
        JComboBox<String> typeBox = new JComboBox<>(types);
        typeBox.setBounds(50, 240, 300, 40);
        add(typeBox);

        JButton saveBtn = new JButton("SAVE");
        saveBtn.setBackground(new Color(50, 255, 50));
        saveBtn.setBounds(50, 350, 300, 50);
        saveBtn.addActionListener(e -> {
            try {
                double amount = Double.parseDouble(amountField.getText());
                String category = catField.getText().isEmpty() ? "General" : catField.getText();
                Transaction.TransactionType type = Transaction.TransactionType.valueOf((String)typeBox.getSelectedItem());

                controller.addTransaction("Manual", amount, type, category);
                historyScreen.loadDataFromController(controller);
                dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Please enter a valid amount!");
            }
        });
        add(saveBtn);
    }
}