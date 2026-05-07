import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddExpenseScreen {

    private static final Color DARK_BG = new Color(15, 15, 25);
    private static final Color NEON_GREEN = new Color(57, 255, 20);
    private static final Color FIELD_BG = new Color(30, 30, 45);
    private static final Color SELECTION_COLOR = new Color(70, 70, 90);
    
    private static JButton selectedButton = null; // Track current selection

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> createAndShowGUI());
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("Spend wise - Expense Management");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(450, 800);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(null);
        mainPanel.setBackground(DARK_BG);
        frame.setContentPane(mainPanel);

        // --- 1. Title ---
        JLabel header = new JLabel("Expense Entry");
        header.setFont(new Font("Monospaced", Font.BOLD, 22));
        header.setForeground(Color.WHITE);
        header.setBounds(20, 20, 200, 30);
        mainPanel.add(header);

        // --- 2. Amount Field ---
        JLabel label1 = new JLabel("AMOUNT (EGP)");
        label1.setFont(new Font("SansSerif", Font.BOLD, 12));
        label1.setForeground(NEON_GREEN);
        label1.setBounds(20, 70, 200, 20);
        mainPanel.add(label1);

        JTextField amountField = new JTextField();
        amountField.setBackground(FIELD_BG);
        amountField.setForeground(Color.WHITE);
        amountField.setFont(new Font("Consolas", Font.BOLD, 26));
        amountField.setBorder(BorderFactory.createLineBorder(new Color(50, 50, 70)));
        amountField.setBounds(20, 95, 395, 55);
        mainPanel.add(amountField);

        // --- 3. Categories with Toggle Logic (Select/Unselect) ---
        JLabel label2 = new JLabel("CHOOSE CATEGORY");
        label2.setFont(new Font("SansSerif", Font.BOLD, 12));
        label2.setForeground(NEON_GREEN);
        label2.setBounds(20, 170, 200, 20);
        mainPanel.add(label2);

        JPanel grid = new JPanel(new GridLayout(0, 3, 8, 8));
        grid.setBackground(DARK_BG);
        grid.setBounds(20, 200, 395, 120);

        String[] categories = {"Food", "Transport", "Shopping", "Bills", "Health", "Other"};
        for (String cat : categories) {
            grid.add(createToggleButton(cat));
        }
        mainPanel.add(grid);

        // --- 4. Category Management ---
        /**
         * BACK-END NOTE: 
         * Edit Button Logic: 
         * 1. Get selectedButton.getText().
         * 2. Open a dialog to rename the category in the SQLite database.
         * 3. Update all past expenses linked to this category name.
         */
        JButton editCatBtn = new JButton("EDIT CATEGORY");
        styleSecondaryBtn(editCatBtn, Color.ORANGE);
        editCatBtn.setBounds(20, 335, 190, 40);
        mainPanel.add(editCatBtn);

        JButton manageBtn = new JButton("MANAGE ALL");
        styleSecondaryBtn(manageBtn, Color.CYAN);
        manageBtn.setBounds(225, 335, 190, 40);
        mainPanel.add(manageBtn);

        // --- 5. Action Buttons ---
        /**
         * BACK-END NOTE:
         * SAVE Action:
         * - Check if an amount exists and a category is selected (selectedButton != null).
         * - INSERT INTO expenses (amount, category, timestamp).
         * - Refresh Dashboard data.
         */
        JButton saveBtn = new JButton("SAVE TRANSACTION");
        saveBtn.setFont(new Font("SansSerif", Font.BOLD, 16));
        saveBtn.setBackground(NEON_GREEN);
        saveBtn.setForeground(Color.BLACK);
        saveBtn.setBounds(20, 580, 395, 60);
        mainPanel.add(saveBtn);

        frame.setVisible(true);
    }

    private static JButton createToggleButton(String text) {
        JButton btn = new JButton(text);
        btn.setBackground(FIELD_BG);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createLineBorder(new Color(60, 60, 80)));

        btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedButton == btn) {
                    // SECOND CLICK: Unselect
                    unselect(btn);
                    selectedButton = null;
                } else {
                    // FIRST CLICK: Select (and unselect previous)
                    if (selectedButton != null) unselect(selectedButton);
                    select(btn);
                    selectedButton = btn;
                }
            }
        });
        return btn;
    }

    private static void select(JButton btn) {
        btn.setBackground(NEON_GREEN);
        btn.setForeground(Color.BLACK);
        btn.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
    }

    private static void unselect(JButton btn) {
        btn.setBackground(FIELD_BG);
        btn.setForeground(Color.WHITE);
        btn.setBorder(BorderFactory.createLineBorder(new Color(60, 60, 80)));
    }

    private static void styleSecondaryBtn(JButton btn, Color accent) {
        btn.setFont(new Font("SansSerif", Font.BOLD, 11));
        btn.setForeground(accent);
        btn.setBackground(DARK_BG);
        btn.setBorder(BorderFactory.createLineBorder(accent, 1));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
}