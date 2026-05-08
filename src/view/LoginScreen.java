package view;

import java.awt.*;
import javax.swing.*;

public class LoginScreen {

    private static final Color DARK_BG = new Color(10, 15, 28);
    private static final Color NEON_GREEN = new Color(57, 255, 20);

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> createAndShowGUI());
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("Spend Wise - Login");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(450, 750); 
        frame.setResizable(false); 
        frame.setLocationRelativeTo(null);

        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(DARK_BG);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        backgroundPanel.setLayout(null);
        frame.setContentPane(backgroundPanel);

        
        try {
            ImageIcon originalIcon = new ImageIcon("logo.png");
        
            Image scaledImage = originalIcon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
            JLabel logoLabel = new JLabel(new ImageIcon(scaledImage));
            
            logoLabel.setBounds((frame.getWidth() - 200) / 2, 40, 200, 200);
            backgroundPanel.add(logoLabel);
        } catch (Exception e) {
            System.out.println("image not found");
        }

        // --- 2.app name---
       JLabel titleLabel = new JLabel(
    "<html><div style='text-align: center; font-family: sans-serif;'>" +
        
        "<div style='font-size: 26px; font-weight: bold; color: white;'>" +
            "Spend<span style='color: rgb(139, 195, 74);'>Wise</span>" +
        "</div>" +
        
        "<div style='font-size: 11px; color: #AAAAAA; margin-top: 4px; font-weight: normal;'>" +
            "— Think Smart. <span style='color: rgb(139, 195, 74);'>Spend Wise.</span> —" +
        "</div>" +
    "</div></html>"
);
        titleLabel.setBounds(0, 260, 450, 80); 
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        backgroundPanel.add(titleLabel);

        // --- 3. Enter PIN ---
        JLabel pinTextLabel = new JLabel("Enter PIN:");
        pinTextLabel.setFont(new Font("Monospaced", Font.BOLD, 18));
        pinTextLabel.setForeground(new Color(150, 255, 150));
        pinTextLabel.setBounds(0, 400, 450, 30);
        pinTextLabel.setHorizontalAlignment(SwingConstants.CENTER);
        backgroundPanel.add(pinTextLabel);

        // --- 4. حقل الـ PIN ---
        JPasswordField pinField = new JPasswordField();
        pinField.setFont(new Font("Arial", Font.BOLD, 22));
        pinField.setForeground(NEON_GREEN);
        pinField.setBackground(new Color(15, 20, 35)); 
        pinField.setCaretColor(NEON_GREEN);
        pinField.setHorizontalAlignment(JTextField.CENTER);
        pinField.setBorder(BorderFactory.createLineBorder(NEON_GREEN, 2));
        pinField.setBounds((450 - 180) / 2, 440, 180, 50);
        backgroundPanel.add(pinField);

        // --- 5. زر الدخول ---
        JButton loginBtn = new JButton("Login");
        loginBtn.setFont(new Font("Arial", Font.BOLD, 18));
        loginBtn.setForeground(NEON_GREEN);
        loginBtn.setBackground(DARK_BG);
        loginBtn.setFocusPainted(false);
        loginBtn.setBorder(BorderFactory.createLineBorder(NEON_GREEN, 2));
        loginBtn.setBounds((450 - 130) / 2, 510, 130, 50);
        backgroundPanel.add(loginBtn);

        // --- 6. التذييل (Footer) ---
        JPanel footerPanel = new JPanel();
        footerPanel.setBackground(new Color(5, 5, 10));
        footerPanel.setBounds(0, 650, 450, 100);
        footerPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 50, 20));
        
        JButton editBtn = new JButton("Edit");
        editBtn.setForeground(Color.WHITE);
        editBtn.setContentAreaFilled(false);
        editBtn.setBorderPainted(false);
        
        JButton shareBtn = new JButton("Share");
        shareBtn.setForeground(Color.WHITE);
        shareBtn.setContentAreaFilled(false);
        shareBtn.setBorderPainted(false);

        backgroundPanel.add(footerPanel);

        frame.setVisible(true);
    }
}