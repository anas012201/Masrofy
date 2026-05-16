package view;

import Controller.AppController;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * TransactionHistoryScreen.java
 * Shows all transactions from the DB.
 * Supports delete and category filter.
 */
public class TransactionHistoryScreen extends JFrame {

    private static final Color DARK_BG    = new Color(10, 15, 28);
    private static final Color NEON_GREEN = new Color(57, 255, 20);
    private static final Color FIELD_BG   = new Color(20, 25, 40);

    private AppController    ctrl;
    private DefaultTableModel tableModel;
    private JTable            table;
    private JComboBox<String> filterCombo;

    // Store transaction IDs matching each row so we can delete by id
    private java.util.List<Integer> rowIds = new java.util.ArrayList<>();

    public TransactionHistoryScreen(AppController ctrl) {
        this.ctrl = ctrl;
        buildUI();
        loadData("All");
    }

    private void buildUI() {
        setTitle("Masroofy - History");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(450, 750);
        setResizable(false);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(null);
        panel.setBackground(DARK_BG);
        setContentPane(panel);

        // Header
        JLabel header = new JLabel("Transaction History");
        header.setFont(new Font("Segoe UI", Font.BOLD, 22));
        header.setForeground(Color.WHITE);
        header.setBounds(20, 30, 300, 30);
        panel.add(header);

        // Filter
        JLabel filterLabel = new JLabel("Filter by Category:");
        filterLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        filterLabel.setForeground(NEON_GREEN);
        filterLabel.setBounds(20, 80, 150, 20);
        panel.add(filterLabel);

        String[] cats = {"All", "Food", "Transport", "Shopping", "Bills", "Health", "Other"};
        filterCombo = new JComboBox<>(cats);
        filterCombo.setBackground(FIELD_BG);
        filterCombo.setForeground(Color.WHITE);
        filterCombo.setBounds(20, 105, 390, 40);
        filterCombo.addActionListener(e -> {
            String selected = (String) filterCombo.getSelectedItem();
            loadData(selected);
        });
        panel.add(filterCombo);

        // Table
        String[] columns = {"Amount (EGP)", "Category", "Date & Time"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        table = new JTable(tableModel);
        table.setBackground(FIELD_BG);
        table.setForeground(Color.WHITE);
        table.setGridColor(new Color(40, 45, 60));
        table.setRowHeight(40);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.getTableHeader().setBackground(new Color(30, 35, 50));
        table.getTableHeader().setForeground(NEON_GREEN);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBounds(20, 165, 390, 350);
        scroll.getViewport().setBackground(DARK_BG);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(60, 65, 80)));
        panel.add(scroll);

        // Delete button
        JButton deleteBtn = new JButton("DELETE SELECTED");
        deleteBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        deleteBtn.setForeground(Color.RED);
        deleteBtn.setBackground(DARK_BG);
        deleteBtn.setBorder(BorderFactory.createLineBorder(Color.RED, 1));
        deleteBtn.setBounds(20, 530, 390, 45);
        deleteBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        deleteBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Please select a row to delete.");
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(this,
                    "Delete this transaction? This will update your daily limit.",
                    "Confirm Delete", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                int transId = rowIds.get(row);
                ctrl.deleteExpense(transId);
                loadData((String) filterCombo.getSelectedItem());
            }
        });
        panel.add(deleteBtn);

        // Back button
        JButton backBtn = new JButton("← BACK TO DASHBOARD");
        backBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        backBtn.setForeground(DARK_BG);
        backBtn.setBackground(NEON_GREEN);
        backBtn.setFocusPainted(false);
        backBtn.setBounds(20, 630, 390, 55);
        backBtn.addActionListener(e -> ctrl.navigateTo("DASHBOARD"));
        panel.add(backBtn);
    }

    /**
     * Loads transactions from the controller into the table.
     * @param categoryFilter "All" or a specific category name
     */
    private void loadData(String categoryFilter) {
        tableModel.setRowCount(0);
        rowIds.clear();

        List<String[]> data = categoryFilter.equals("All")
                ? ctrl.getAllExpenses()
                : ctrl.getExpensesByCategory(categoryFilter);

        if (data.isEmpty()) {
            tableModel.addRow(new Object[]{"—", "No transactions found", "—"});
        } else {
            for (String[] row : data) {
                // row: [0]=id, [1]=amount, [2]=category, [3]=timestamp
                rowIds.add(Integer.parseInt(row[0]));
                tableModel.addRow(new Object[]{
                        String.format("%.2f", Double.parseDouble(row[1])),
                        row[2],
                        row[3]
                });
            }
        }
    }
}