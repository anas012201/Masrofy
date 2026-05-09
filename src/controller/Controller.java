package controller;

import model.Transaction;
import model.User;
import service.CycleManager;
import interfaces.IDataStorage;
import interfaces.IAlertService;
import javax.swing.JOptionPane;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Controller {
    private CycleManager manager;
    private User user;
    private String uName;
    private float uBudget;

    public Controller(String name, float initialBalance) {
        this.uName = name;
        this.uBudget = initialBalance;
        user = new User(name, initialBalance);
        initDatabaseLogic();
    }

    private void initDatabaseLogic() {
        DatabaseManager.initDatabase();
        IAlertService alertService = new IAlertService() {
            @Override public void sendLowBalanceAlert(float b) { 
                JOptionPane.showMessageDialog(null, "Low Balance: " + b); 
            }
            @Override public void sendCycleLimitAlert() { 
                JOptionPane.showMessageDialog(null, "Warning: Daily Limit Exceeded!"); 
            }
            @Override public void notifyUser(String m) {}
        };

        IDataStorage storage = new IDataStorage() {
            @Override public void saveTransaction(Transaction t) {
                try (Connection conn = DatabaseManager.getConnection();
                     PreparedStatement pstmt = conn.prepareStatement("INSERT INTO transactions (amount, category, type, date) VALUES (?, ?, ?, ?)")) {
                    pstmt.setDouble(1, t.getAmount());
                    pstmt.setString(2, t.getCategory());
                    pstmt.setString(3, t.getType().toString());
                    pstmt.setString(4, t.getFormattedDate());
                    pstmt.executeUpdate();
                } catch (SQLException e) {
                    System.out.println("Save Error: " + e.getMessage());
                }
            }
            @Override public List<Transaction> loadTransactions() {
                List<Transaction> list = new ArrayList<>();
                try (Connection conn = DatabaseManager.getConnection();
                     Statement stmt = conn.createStatement();
                     ResultSet rs = stmt.executeQuery("SELECT * FROM transactions")) {
                    while (rs.next()) {
                        list.add(new Transaction(rs.getString("category"), rs.getDouble("amount"), 
                                     Transaction.TransactionType.valueOf(rs.getString("type")), rs.getString("date")));
                    }
                } catch (SQLException e) {
                    System.out.println("Load Error: " + e.getMessage());
                }
                return list;
            }
            @Override public void deleteTransaction(String date) {}
        };

        user = new User(uName, uBudget);
        List<Transaction> allData = storage.loadTransactions();
        for (Transaction t : allData) {
            user.adjustBalance((float)t.getAmount(), t.getType());
        }

        manager = new CycleManager(user, storage, alertService);
    }

    private String getCurrentDate() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));
    }

    public void addTransaction(String d, double a, Transaction.TransactionType t, String c) {
        String finalDate = (d == null || d.isEmpty() || d.equalsIgnoreCase("Manual")) ? getCurrentDate() : d;
        manager.addTransaction(finalDate, a, t, c);
        initDatabaseLogic();
    }

    public void deleteSpecificTransaction(int index) {
        List<Transaction> list = manager.getHistory();
        if (index >= 0 && index < list.size()) {
            Transaction t = list.get(index);
            try (Connection conn = DatabaseManager.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement("DELETE FROM transactions WHERE amount=? AND date=? AND category=?")) {
                pstmt.setDouble(1, t.getAmount());
                pstmt.setString(2, t.getFormattedDate());
                pstmt.setString(3, t.getCategory());
                pstmt.executeUpdate();
                initDatabaseLogic(); 
            } catch (SQLException e) {
                System.out.println("Delete Error: " + e.getMessage());
            }
        }
    }

    public List<Transaction> getAllTransactions() { return manager.getHistory(); }
    public double getBalance() { return user.getCurrentBalance(); }
    public double getDailyLimit() { return manager.calculateSafeDailyLimit(); }
}