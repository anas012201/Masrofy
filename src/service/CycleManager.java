package service;

import model.Transaction;
import model.User;
import interfaces.IDataStorage;
import interfaces.IAlertService;
import java.util.List;

public class CycleManager {
    private User user;
    private IDataStorage storage;
    private IAlertService alertService;

    public CycleManager(User user, IDataStorage storage, IAlertService alertService) {
        this.user = user;
        this.storage = storage;
        this.alertService = alertService;
    }

    public void addTransaction(String date, double amount, Transaction.TransactionType type, String category) {
        Transaction t = new Transaction(category, amount, type, date);
        storage.saveTransaction(t);
        user.adjustBalance((float) amount, type);
        
        if (type == Transaction.TransactionType.EXPENSE) {
            if (amount > calculateSafeDailyLimit()) {
                alertService.sendCycleLimitAlert();
            }
        }
        
        if (user.getCurrentBalance() < 500) {
            alertService.sendLowBalanceAlert(user.getCurrentBalance());
        }
    }

    public double calculateSafeDailyLimit() {
        double currentBalance = user.getCurrentBalance();
        if (currentBalance <= 0) {
            return 0.0;
        }
        return currentBalance / 30.0;
    }

    public List<Transaction> getHistory() {
        return storage.loadTransactions();
    }

    public User getUser() {
        return user;
    }
}