package view;

import controller.Controller;
import controller.DatabaseManager;
import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class WelcomeScreen extends JFrame {
    private JTextField nameField;
    private JTextField budgetField;
    private JButton startBtn;

    public WelcomeScreen() {
        setTitle("Spend Wise - Welcome");
        setSize(400, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(new Color(10, 15, 30));
        mainPanel.setLayout(null);

        JLabel titleLabel = new JLabel("Welcome to Spend Wise", SwingConstants.CENTER);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setBounds(50, 30, 300, 40);
        mainPanel.add(titleLabel);

        JLabel nameLabel = new JLabel("Your Name:");
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setBounds(50, 100, 100, 20);
        mainPanel.add(nameLabel);

        nameField = new JTextField();
        nameField.setBounds(50, 130, 300, 40);
        mainPanel.add(nameField);

        JLabel budgetLabel = new JLabel("Monthly Budget:");
        budgetLabel.setForeground(Color.WHITE);
        budgetLabel.setBounds(50, 200, 150, 20);
        mainPanel.add(budgetLabel);

        budgetField = new JTextField();
        budgetField.setBounds(50, 230, 300, 40);
        mainPanel.add(budgetField);

        startBtn = new JButton("START SYSTEM");
        startBtn.setBackground(new Color(50, 255, 50));
        startBtn.setForeground(Color.BLACK);
        startBtn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        startBtn.setBounds(50, 350, 300, 50);
        startBtn.setFocusPainted(false);

        startBtn.addActionListener(e -> {
            String name = nameField.getText();
            String budgetStr = budgetField.getText();

            if (!name.isEmpty() && !budgetStr.isEmpty()) {
                try (Connection conn = DatabaseManager.getConnection();
                     PreparedStatement pstmt = conn.prepareStatement("INSERT INTO config (name, budget) VALUES (?, ?)")) {

                    float budget = Float.parseFloat(budgetStr);
                    pstmt.setString(1, name);
                    pstmt.setFloat(2, budget);
                    pstmt.executeUpdate();

                    Controller controller = new Controller(name, budget);
                    TransactionHistoryScreen history = new TransactionHistoryScreen();
                    history.loadDataFromController(controller);
                    history.setVisible(true);
                    dispose();

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error: Enter a valid number for budget!");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please fill all fields!");
            }
        });

        mainPanel.add(startBtn);
        add(mainPanel);
    }
}