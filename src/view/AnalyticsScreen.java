package view;

import Controller.AppController;
import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * AnalyticsScreen.java
 * Shows a real pie chart built from actual DB data.
 */
public class AnalyticsScreen extends JFrame {

    private static final Color DARK_BG    = new Color(10, 15, 28);
    private static final Color NEON_GREEN = new Color(57, 255, 20);
    private static final Color CARD_BG    = new Color(20, 25, 40);
    private static final Color[] CHART_COLORS = {
            new Color(57, 255, 20),
            new Color(0, 150, 255),
            new Color(255, 159, 64),
            new Color(255, 99, 132),
            new Color(153, 102, 255),
            new Color(255, 205, 86)
    };

    private AppController ctrl;

    public AnalyticsScreen(AppController ctrl) {
        this.ctrl = ctrl;
        buildUI();
    }

    private void buildUI() {
        setTitle("Masroofy - Analytics");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(450, 800);
        setResizable(false);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(null);
        panel.setBackground(DARK_BG);
        setContentPane(panel);

        // Header
        JLabel header = new JLabel("Spending Insights");
        header.setFont(new Font("Segoe UI", Font.BOLD, 22));
        header.setForeground(Color.WHITE);
        header.setBounds(30, 30, 300, 30);
        panel.add(header);

        // Load real data
        List<String[]> expenses = ctrl.getAllExpenses();
        Map<String, Double> categoryTotals = new LinkedHashMap<>();
        double grandTotal = 0;

        for (String[] row : expenses) {
            String cat    = row[2];
            double amount = Double.parseDouble(row[1]);
            categoryTotals.merge(cat, amount, Double::sum);
            grandTotal += amount;
        }

        // Pie chart
        final Map<String, Double> totals = categoryTotals;
        final double total = grandTotal;

        JPanel chartPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (total == 0) {
                    g2.setColor(Color.GRAY);
                    g2.fillOval(25, 25, 220, 220);
                    g2.setColor(DARK_BG);
                    g2.fillOval(75, 75, 120, 120);
                    g2.setColor(Color.WHITE);
                    g2.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                    g2.drawString("No data", 100, 140);
                    return;
                }

                int startAngle = 0;
                int i = 0;
                for (Map.Entry<String, Double> entry : totals.entrySet()) {
                    int arc = (int) Math.round((entry.getValue() / total) * 360);
                    g2.setColor(CHART_COLORS[i % CHART_COLORS.length]);
                    g2.fillArc(25, 25, 220, 220, startAngle, arc);
                    startAngle += arc;
                    i++;
                }
                // Donut hole
                g2.setColor(DARK_BG);
                g2.fillOval(80, 80, 110, 110);
            }
        };
        chartPanel.setOpaque(false);
        chartPanel.setBounds(85, 80, 270, 270);
        panel.add(chartPanel);

        // Legend
        JPanel legendPanel = new JPanel();
        legendPanel.setLayout(new BoxLayout(legendPanel, BoxLayout.Y_AXIS));
        legendPanel.setBackground(DARK_BG);
        legendPanel.setBounds(30, 370, 390, 220);
        panel.add(legendPanel);

        if (grandTotal == 0) {
            JLabel noData = new JLabel("No expenses recorded yet.");
            noData.setForeground(Color.LIGHT_GRAY);
            noData.setFont(new Font("Segoe UI", Font.ITALIC, 14));
            legendPanel.add(noData);
        } else {
            int i = 0;
            for (Map.Entry<String, Double> entry : categoryTotals.entrySet()) {
                int pct = (int) Math.round((entry.getValue() / grandTotal) * 100);
                String text = entry.getKey() + " — " +
                        String.format("%.2f", entry.getValue()) + " EGP (" + pct + "%)";
                legendPanel.add(makeLegendItem(text, CHART_COLORS[i % CHART_COLORS.length]));
                legendPanel.add(Box.createRigidArea(new Dimension(0, 8)));
                i++;
            }
        }

        // Back button
        JButton backBtn = new JButton("← BACK TO DASHBOARD");
        backBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        backBtn.setForeground(DARK_BG);
        backBtn.setBackground(NEON_GREEN);
        backBtn.setFocusPainted(false);
        backBtn.setBounds(30, 700, 390, 55);
        backBtn.addActionListener(e -> ctrl.navigateTo("DASHBOARD"));
        panel.add(backBtn);
    }

    private JPanel makeLegendItem(String text, Color color) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        p.setBackground(DARK_BG);
        p.setMaximumSize(new Dimension(390, 25));

        JPanel dot = new JPanel();
        dot.setPreferredSize(new Dimension(14, 14));
        dot.setBackground(color);

        JLabel lbl = new JLabel(text);
        lbl.setForeground(Color.WHITE);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        p.add(dot);
        p.add(lbl);
        return p;
    }
}