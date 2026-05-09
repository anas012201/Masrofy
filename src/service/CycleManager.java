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

    public void addTransaction(String d, double a, Transaction.TransactionType t, String c) {
        Transaction newT = new Transaction(c, a, t, d);
        storage.saveTransaction(newT);
        user.adjustBalance((float)a, t);
    }

    public List<Transaction> getHistory() {
        return storage.loadTransactions();
    }

    public double calculateSafeDailyLimit() {
        return user.getCurrentBalance() / 30.0;
    }
}