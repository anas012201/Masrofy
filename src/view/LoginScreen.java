package view;

import Controller.AppController;
import javax.swing.*;
import java.awt.*;

/**
 * LoginScreen.java
 * Shows PIN entry screen.
 * Talks to AppController only.
 */
public class LoginScreen extends JFrame {

    private static final Color DARK_BG    = new Color(10, 15, 28);
    private static final Color NEON_GREEN = new Color(57, 255, 20);

    private AppController ctrl;

    public LoginScreen(AppController ctrl) {
        this.ctrl = ctrl;
        buildUI();
    }

    private void buildUI() {
        setTitle("Masroofy - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(450, 750);
        setResizable(false);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(null);
        panel.setBackground(DARK_BG);
        setContentPane(panel);

        // Title
        JLabel title = new JLabel("Masroofy");
        title.setFont(new Font("Segoe UI", Font.BOLD, 36));
        title.setForeground(NEON_GREEN);
        title.setBounds(0, 200, 450, 50);
        title.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(title);

        JLabel sub = new JLabel("Enter your PIN to continue");
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        sub.setForeground(new Color(150, 150, 150));
        sub.setBounds(0, 255, 450, 25);
        sub.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(sub);

        // PIN field
        JPasswordField pinField = new JPasswordField();
        pinField.setFont(new Font("Segoe UI", Font.BOLD, 24));
        pinField.setForeground(NEON_GREEN);
        pinField.setBackground(new Color(20, 25, 40));
        pinField.setCaretColor(NEON_GREEN);
        pinField.setHorizontalAlignment(JTextField.CENTER);
        pinField.setBorder(BorderFactory.createLineBorder(NEON_GREEN, 2));
        pinField.setBounds(125, 320, 200, 55);
        panel.add(pinField);

        // Error label
        JLabel errorLabel = new JLabel("");
        errorLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        errorLabel.setForeground(Color.RED);
        errorLabel.setBounds(0, 385, 450, 20);
        errorLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(errorLabel);

        // Login button
        JButton loginBtn = new JButton("UNLOCK");
        loginBtn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        loginBtn.setForeground(DARK_BG);
        loginBtn.setBackground(NEON_GREEN);
        loginBtn.setFocusPainted(false);
        loginBtn.setBorderPainted(false);
        loginBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginBtn.setBounds(125, 430, 200, 50);
        panel.add(loginBtn);

        // Login action
        loginBtn.addActionListener(e -> {
            String pin = new String(pinField.getPassword());
            if (ctrl.checkPin(pin)) {
                if (ctrl.hasActiveCycle()) {
                    ctrl.navigateTo("DASHBOARD");
                } else {
                    ctrl.navigateTo("SETUP");
                }
            } else {
                errorLabel.setText("Incorrect PIN. Please try again.");
                pinField.setText("");
            }
        });

        // Allow pressing Enter
        pinField.addActionListener(e -> loginBtn.doClick());
    }
}