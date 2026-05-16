package view;

import Controller.AppController;
import javax.swing.*;
import java.awt.*;

/**
 * SettingsScreen.java
 * Handles PIN change, privacy lock toggle, and cycle reset.
 */
public class SettingsScreen extends JFrame {

    private static final Color DARK_BG    = new Color(10, 15, 28);
    private static final Color NEON_GREEN = new Color(57, 255, 20);
    private static final Color CARD_BG    = new Color(22, 27, 34);
    private static final Color DANGER_RED = new Color(255, 69, 58);

    private AppController ctrl;

    public SettingsScreen(AppController ctrl) {
        this.ctrl = ctrl;
        buildUI();
    }

    private void buildUI() {
        setTitle("Masroofy - Settings");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(450, 750);
        setResizable(false);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(null);
        panel.setBackground(DARK_BG);
        setContentPane(panel);

        // Header
        JLabel title = new JLabel("Settings");
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setForeground(Color.WHITE);
        title.setBounds(30, 40, 200, 40);
        panel.add(title);

        // Security section
        JLabel secLabel = new JLabel("SECURITY");
        secLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        secLabel.setForeground(NEON_GREEN);
        secLabel.setBounds(30, 110, 200, 20);
        panel.add(secLabel);

        // Change PIN button
        JButton changePinBtn = makeMenuBtn("🔒  Change Access PIN");
        changePinBtn.setBounds(30, 135, 390, 55);
        changePinBtn.addActionListener(e -> showChangePinDialog());
        panel.add(changePinBtn);

        // Privacy lock toggle
        boolean lockEnabled = ctrl.isPrivacyLockEnabled();
        JToggleButton lockToggle = new JToggleButton(
                lockEnabled ? "Privacy Lock: ON" : "Privacy Lock: OFF", lockEnabled);
        lockToggle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lockToggle.setForeground(lockEnabled ? DARK_BG : Color.WHITE);
        lockToggle.setBackground(lockEnabled ? NEON_GREEN : CARD_BG);
        lockToggle.setFocusPainted(false);
        lockToggle.setBorder(BorderFactory.createLineBorder(NEON_GREEN, 1));
        lockToggle.setBounds(30, 205, 390, 55);
        lockToggle.addActionListener(e -> {
            boolean on = lockToggle.isSelected();
            ctrl.setPrivacyLock(on);
            lockToggle.setText(on ? "Privacy Lock: ON" : "Privacy Lock: OFF");
            lockToggle.setBackground(on ? NEON_GREEN : CARD_BG);
            lockToggle.setForeground(on ? DARK_BG : Color.WHITE);
        });
        panel.add(lockToggle);

        // Data management section
        JLabel dataLabel = new JLabel("DATA MANAGEMENT");
        dataLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        dataLabel.setForeground(NEON_GREEN);
        dataLabel.setBounds(30, 300, 200, 20);
        panel.add(dataLabel);

        // Reset cycle button
        JButton resetBtn = makeMenuBtn("⚠️  Reset Current Cycle");
        resetBtn.setForeground(DANGER_RED);
        resetBtn.setBounds(30, 325, 390, 55);
        resetBtn.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "This will permanently delete all logs for this cycle.\nThis cannot be undone. Continue?",
                    "Confirm Reset", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

            if (confirm == JOptionPane.YES_OPTION) {
                ctrl.resetCurrentCycle();
            }
        });
        panel.add(resetBtn);

        // Back button
        JButton backBtn = new JButton("← BACK TO DASHBOARD");
        backBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        backBtn.setForeground(DARK_BG);
        backBtn.setBackground(NEON_GREEN);
        backBtn.setFocusPainted(false);
        backBtn.setBounds(30, 650, 390, 55);
        backBtn.addActionListener(e -> ctrl.navigateTo("DASHBOARD"));
        panel.add(backBtn);
    }

    private void showChangePinDialog() {
        JPanel dialogPanel = new JPanel(new GridLayout(3, 2, 8, 8));
        JPasswordField newPin     = new JPasswordField();
        JPasswordField confirmPin = new JPasswordField();

        dialogPanel.add(new JLabel("New PIN (4 digits):"));
        dialogPanel.add(newPin);
        dialogPanel.add(new JLabel("Confirm PIN:"));
        dialogPanel.add(confirmPin);

        int result = JOptionPane.showConfirmDialog(this, dialogPanel,
                "Change PIN", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            String p1 = new String(newPin.getPassword());
            String p2 = new String(confirmPin.getPassword());

            if (!p1.equals(p2)) {
                JOptionPane.showMessageDialog(this, "PINs do not match.");
            } else if (p1.length() != 4 || !p1.matches("\\d+")) {
                JOptionPane.showMessageDialog(this, "PIN must be exactly 4 digits.");
            } else {
                ctrl.savePin(p1);
                ctrl.setPrivacyLock(true);
                JOptionPane.showMessageDialog(this, "PIN saved successfully!");
            }
        }
    }

    private JButton makeMenuBtn(String text) {
        JButton btn = new JButton(text);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        btn.setBackground(CARD_BG);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(60, 65, 80), 1),
                BorderFactory.createEmptyBorder(0, 20, 0, 0)));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }
}