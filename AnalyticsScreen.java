import javax.swing.*;
import java.awt.*;

public class AnalyticsScreen {

    private static final Color DARK_BG = new Color(10, 15, 28);
    private static final Color NEON_GREEN = new Color(57, 255, 20);
    private static final Color CARD_BG = new Color(20, 25, 40);
    
    // Category Colors for the Chart
    private static final Color[] CHART_COLORS = {
        new Color(57, 255, 20),  // Neon Green
        new Color(0, 150, 255),  // Bright Blue
        new Color(255, 159, 64), // Orange
        new Color(255, 99, 132), // Pink/Red
        new Color(153, 102, 255)// Purple
    };

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> createAndShowGUI());
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("Spend Wise - Analytics");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(450, 800);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(null);
        mainPanel.setBackground(DARK_BG);
        frame.setContentPane(mainPanel);

        // --- 1. Header ---
        JLabel header = new JLabel("Spending Insights");
        header.setFont(new Font("Segoe UI", Font.BOLD, 22));
        header.setForeground(Color.WHITE);
        header.setBounds(30, 30, 250, 30);
        mainPanel.add(header);

        // --- 2. Time Range Selector (Safe Boundaries) ---
        // BACK-END NOTE: Clicking these should refresh the SQL query time range
        JPanel filterPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        filterPanel.setBackground(DARK_BG);
        filterPanel.setBounds(30, 80, 390, 40);
        
        JButton btnWeek = createFilterBtn("This Week", false);
        JButton btnCycle = createFilterBtn("Current Cycle", true);
        filterPanel.add(btnWeek);
        filterPanel.add(btnCycle);
        mainPanel.add(filterPanel);

        // --- 3. Custom Pie Chart Display ---
        /**
         * BACK-END LOGIC REQUIRED:
         * 1. Query: SELECT category, SUM(amount) FROM expenses GROUP BY category.
         * 2. Calculate percentage for each category relative to total spent.
         * 3. Pass data to the PieChart component.
         */
        PieChartPanel chartView = new PieChartPanel();
        chartView.setBounds(75, 150, 300, 300);
        mainPanel.add(chartView);

        // --- 4. Category Breakdown Legend ---
        JPanel legendPanel = new JPanel();
        legendPanel.setLayout(new BoxLayout(legendPanel, BoxLayout.Y_AXIS));
        legendPanel.setBackground(DARK_BG);
        legendPanel.setBounds(30, 470, 390, 180);

        String[] dummyCats = {"Food (45%)", "Transport (20%)", "Bills (15%)", "Health (10%)", "Others (10%)"};
        for (int i = 0; i < dummyCats.length; i++) {
            legendPanel.add(createLegendItem(dummyCats[i], CHART_COLORS[i]));
            legendPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        }
        mainPanel.add(legendPanel);

        // --- 5. Navigation ---
        JButton backBtn = new JButton("BACK TO DASHBOARD");
        backBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        backBtn.setForeground(DARK_BG);
        backBtn.setBackground(NEON_GREEN);
        backBtn.setBounds(30, 680, 390, 55);
        backBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        mainPanel.add(backBtn);

        frame.setVisible(true);
    }

    // Custom Component to draw the Pie Chart
    static class PieChartPanel extends JPanel {
        public PieChartPanel() { setOpaque(false); }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int startAngle = 0;
            // Example proportions (Back-end should provide these as degrees)
            int[] arcAngles = {162, 72, 54, 36, 36}; // Sum = 360 degrees

            for (int i = 0; i < arcAngles.length; i++) {
                g2.setColor(CHART_COLORS[i]);
                g2.fillArc(10, 10, 250, 250, startAngle, arcAngles[i]);
                startAngle += arcAngles[i];
            }

            // Draw a center circle to make it a "Donut Chart" (Modern look)
            g2.setColor(DARK_BG);
            g2.fillOval(75, 75, 120, 120);
        }
    }

    private static JPanel createLegendItem(String text, Color color) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
        p.setBackground(DARK_BG);
        
        JPanel colorBox = new JPanel();
        colorBox.setPreferredSize(new Dimension(15, 15));
        colorBox.setBackground(color);
        
        JLabel label = new JLabel(text);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        p.add(colorBox);
        p.add(label);
        return p;
    }

    private static JButton createFilterBtn(String text, boolean active) {
        JButton b = new JButton(text);
        b.setFocusPainted(false);
        b.setBackground(active ? NEON_GREEN : CARD_BG);
        b.setForeground(active ? DARK_BG : Color.WHITE);
        b.setBorder(BorderFactory.createLineBorder(active ? NEON_GREEN : Color.GRAY));
        return b;
    }
}