import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class TransactionHistoryScreen {

    private static final Color DARK_BG = new Color(10, 15, 28);
    private static final Color NEON_GREEN = new Color(57, 255, 20);
    private static final Color FIELD_BG = new Color(20, 25, 40);

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> createAndShowGUI());
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("Spend Wise - History");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(450, 750);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(null);
        mainPanel.setBackground(DARK_BG);
        frame.setContentPane(mainPanel);

        // --- 1. Header ---
        JLabel headerLabel = new JLabel("Transaction History");
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        headerLabel.setForeground(Color.WHITE);
        headerLabel.setBounds(20, 30, 250, 30);
        mainPanel.add(headerLabel);

        // --- 2. Filter Section ---
        JLabel filterLabel = new JLabel("Filter by Category:");
        filterLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        filterLabel.setForeground(NEON_GREEN);
        filterLabel.setBounds(20, 80, 150, 20);
        mainPanel.add(filterLabel);

        /**
         * BACK-END NOTE: 
         * Populate this JComboBox with unique categories from the 'Expenses' table.
         * On Selection Change: Re-run the SQL query with a 'WHERE category = ?' clause.
         */
        String[] categories = {"All", "Food", "Transport", "Shopping", "Bills"};
        JComboBox<String> filterCombo = new JComboBox<>(categories);
        filterCombo.setBackground(FIELD_BG);
        filterCombo.setForeground(Color.WHITE);
        filterCombo.setBounds(20, 105, 390, 40);
        mainPanel.add(filterCombo);

        // --- 3. Transactions Table ---
        /**
         * BACK-END NOTE (Database Logic):
         * 1. Query: SELECT id, amount, category, date FROM expenses ORDER BY date DESC.
         * 2. Use a DefaultTableModel to load data into the JTable.
         * 3. Row Selection: Store the 'ID' of the selected row to allow Delete/Edit operations.
         */
        String[] columns = {"Amount", "Category", "Date"};
        Object[][] data = {
            {"50.00", "Food", "2026-05-01"},
            {"120.00", "Transport", "2026-05-02"},
            {"300.00", "Shopping", "2026-05-03"}
        };

        DefaultTableModel model = new DefaultTableModel(data, columns);
        JTable table = new JTable(model);
        table.setBackground(FIELD_BG);
        table.setForeground(Color.WHITE);
        table.setGridColor(new Color(40, 45, 60));
        table.setRowHeight(40);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(20, 170, 390, 350);
        scrollPane.getViewport().setBackground(DARK_BG);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(60, 65, 80)));
        mainPanel.add(scrollPane);

        // --- 4. Action Buttons (Delete / Edit) ---
        /**
         * BACK-END NOTE (CRUD Operations):
         * DELETE: Execute 'DELETE FROM expenses WHERE id = ?' then refresh table & dashboard.
         * EDIT: Open the 'Rapid Expense' dialog pre-filled with this row's data.
         */
        JButton deleteBtn = new JButton("DELETE SELECTED");
        deleteBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        deleteBtn.setForeground(Color.RED);
        deleteBtn.setBackground(DARK_BG);
        deleteBtn.setBorder(BorderFactory.createLineBorder(Color.RED, 1));
        deleteBtn.setBounds(20, 540, 390, 45);
        deleteBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        mainPanel.add(deleteBtn);

        // --- 5. Navigation ---
        /**
         * PAGE NAVIGATION:
         * On Click: dispose() current frame and return to DashboardScreen.
         */
        JButton backBtn = new JButton("← BACK TO DASHBOARD");
        backBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        backBtn.setForeground(DARK_BG);
        backBtn.setBackground(NEON_GREEN);
        backBtn.setFocusPainted(false);
        backBtn.setBounds(20, 630, 390, 55);
        mainPanel.add(backBtn);
        backBtn.action(){
            
        }

        frame.setVisible(true);
    }
}