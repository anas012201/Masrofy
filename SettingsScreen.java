import javax.swing.*;
import java.awt.*;

public class SettingsScreen {

    private static final Color DARK_BG = new Color(10, 15, 28);
    private static final Color NEON_GREEN = new Color(57, 255, 20);
    private static final Color CARD_BG = new Color(22, 27, 34);
    private static final Color DANGER_RED = new Color(255, 69, 58);

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> createAndShowGUI());
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("Spend Wise - Settings");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(450, 750);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(null);
        mainPanel.setBackground(DARK_BG);
        frame.setContentPane(mainPanel);

        // --- 1. Header ---
        JLabel title = new JLabel("Settings");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(Color.WHITE);
        title.setBounds(30, 40, 200, 40);
        mainPanel.add(title);

        // --- 2. Security Section (Change PIN) ---
        JLabel securityLabel = new JLabel("SECURITY");
        securityLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        securityLabel.setForeground(NEON_GREEN);
        securityLabel.setBounds(30, 110, 200, 20);
        mainPanel.add(securityLabel);

        /**
         * BACK-END NOTE:
         * Logic: Open a dialog to verify OLD_PIN then UPDATE the 'User' table with NEW_PIN.
         */
        JButton changePinBtn = createMenuButton("Change Access PIN", "🔒");
        changePinBtn.setBounds(30, 140, 390, 55);
        mainPanel.add(changePinBtn);

        // --- 3. Preferences (Alert Threshold) ---
        JLabel prefLabel = new JLabel("PREFERENCES");
        prefLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        prefLabel.setForeground(NEON_GREEN);
        prefLabel.setBounds(30, 230, 200, 20);
        mainPanel.add(prefLabel);

        /**
         * BACK-END NOTE:
         * This slider updates the 'alert_threshold' value in Settings table.
         * Default is 80%.
         */
        JSlider thresholdSlider = new JSlider(50, 100, 80);
        thresholdSlider.setBackground(DARK_BG);
        thresholdSlider.setForeground(NEON_GREEN);
        thresholdSlider.setBounds(30, 260, 390, 40);
        mainPanel.add(thresholdSlider);

        JLabel sliderVal = new JLabel("Alert me when I reach 80% of budget");
        sliderVal.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        sliderVal.setForeground(Color.LIGHT_GRAY);
        sliderVal.setBounds(35, 300, 300, 20);
        mainPanel.add(sliderVal);

        // --- 4. Data Management ---
        JLabel dataLabel = new JLabel("DATA MANAGEMENT");
        dataLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        dataLabel.setForeground(NEON_GREEN);
        dataLabel.setBounds(30, 360, 200, 20);
        mainPanel.add(dataLabel);

        JButton exportBtn = createMenuButton("Export Data as CSV", "📊");
        exportBtn.setBounds(30, 390, 390, 55);
        mainPanel.add(exportBtn);

        /**
         * BACK-END NOTE:
         * CRITICAL: 'DROP TABLE' or 'DELETE FROM' all expense data.
         * Must show a confirmation dialog before execution.
         */
        JButton resetBtn = createMenuButton("Reset All Data", "⚠️");
        resetBtn.setForeground(DANGER_RED);
        resetBtn.setBounds(30, 460, 390, 55);
        mainPanel.add(resetBtn);

        // --- 5. Navigation ---
        JButton backBtn = new JButton("Close Settings");
        backBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        backBtn.setForeground(DARK_BG);
        backBtn.setBackground(NEON_GREEN);
        backBtn.setBounds(30, 630, 390, 55);
        backBtn.addActionListener(e -> frame.dispose());
        mainPanel.add(backBtn);

        frame.setVisible(true);
    }

    private static JButton createMenuButton(String text, String icon) {
        JButton btn = new JButton(icon + "  " + text);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        btn.setBackground(CARD_BG);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(60, 65, 80), 1),
                BorderFactory.createEmptyBorder(0, 20, 0, 0)
        ));
        return btn;
    }
}