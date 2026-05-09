package view;

import controller.Controller;
import model.Transaction;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class TransactionHistoryScreen extends JFrame {
    private JTable table;
    private Controller controller;

    public TransactionHistoryScreen() {
        setTitle("Spend Wise - Pro History");
        setSize(450, 750);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(new Color(10, 15, 30));
        mainPanel.setLayout(null);

        String[] cols = {"Amount", "Category", "Date", "Type"};
        table = new JTable(new DefaultTableModel(cols, 0));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane sp = new JScrollPane(table);
        sp.setBounds(20, 40, 400, 340);
        mainPanel.add(sp);

        JButton deleteBtn = new JButton("DELETE SELECTED 🗑");
        deleteBtn.setBackground(new Color(220, 50, 50));
        deleteBtn.setForeground(Color.WHITE);
        deleteBtn.setBounds(20, 400, 400, 40);
        deleteBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                controller.deleteSpecificTransaction(row);
                loadDataFromController(controller);
                JOptionPane.showMessageDialog(this, "Transaction Deleted Successfully!");
            } else {
                JOptionPane.showMessageDialog(this, "Please select a row first.");
            }
        });
        mainPanel.add(deleteBtn);

        JButton addBtn = new JButton("ADD NEW TRANSACTION");
        addBtn.setBackground(new Color(50, 255, 50));
        addBtn.setBounds(20, 450, 400, 50);
        addBtn.addActionListener(e -> {
            new AddTransactionScreen(controller, this).setVisible(true);
        });
        mainPanel.add(addBtn);

        JButton checkBtn = new JButton("CHECK BALANCE & LIMIT");
        checkBtn.setBackground(new Color(20, 30, 50));
        checkBtn.setForeground(new Color(50, 255, 50));
        checkBtn.setBounds(20, 510, 400, 45);
        checkBtn.addActionListener(e -> {
            String msg = "Balance: " + controller.getBalance() + " EGP\nDaily Limit: " + String.format("%.2f", controller.getDailyLimit());
            JOptionPane.showMessageDialog(this, msg);
        });
        mainPanel.add(checkBtn);

        JButton backBtn = new JButton("← BACK TO DASHBOARD");
        backBtn.setBackground(new Color(50, 255, 50));
        backBtn.setBounds(20, 640, 400, 50);
        backBtn.addActionListener(e -> dispose());
        mainPanel.add(backBtn);

        add(mainPanel);
    }

    public void loadDataFromController(Controller c) {
        this.controller = c;
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);
        List<Transaction> list = c.getAllTransactions();
        for (Transaction t : list) {
            String prefix = (t.getType() == Transaction.TransactionType.INCOME) ? "+" : "-";

            model.addRow(new Object[]{
                    prefix + t.getAmount(),
                    t.getCategory(),
                    t.getFormattedDate(),
                    t.getType()
            });
        }
    }
}