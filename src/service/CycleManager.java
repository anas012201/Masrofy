package service;

import model.*;
import interfaces.*;
import java.util.*;
import java.util.stream.Collectors;

public class CycleManager {
    private User user;
    private IDataStorage storage;
    private IAlertService alertService;

    public CycleManager(User user, IDataStorage storage, IAlertService alertService) {
        this.user = user;
        this.storage = storage;
        this.alertService = alertService;
    }

    public void addTransaction(String desc, double amount, Transaction.TransactionType type, String category) {
        double currentDailyLimit = calculateSafeDailyLimit();

        Transaction t = new Transaction(desc, amount, type, category);
        storage.saveTransaction(t);
        user.adjustBalance((float)amount, type);

        if (type == Transaction.TransactionType.EXPENSE && amount > currentDailyLimit) {
            alertService.sendCycleLimitAlert();
        }

        if (user.getCurrentBalance() < 100) {
            alertService.sendLowBalanceAlert(user.getCurrentBalance());
        }
    }

    public double calculateSafeDailyLimit() {
        long diffInMillies = Math.abs(user.getCycleEndDate().getTime() - System.currentTimeMillis());
        long diffInDays = (diffInMillies / (1000 * 60 * 60 * 24)) + 1;

        if (diffInDays <= 0) return user.getCurrentBalance();

        return user.getCurrentBalance() / diffInDays;
    }

    public Map<String, Double> getExpensesByCategory() {
        return storage.loadTransactions().stream()
                .filter(t -> t.getType() == Transaction.TransactionType.EXPENSE)
                .collect(Collectors.groupingBy(
                        Transaction::getCategory,
                        Collectors.summingDouble(Transaction::getAmount)
                ));
    }

    public double getTotalByType(Transaction.TransactionType type) {
        return storage.loadTransactions().stream()
                .filter(t -> t.getType() == type)
                .mapToDouble(Transaction::getAmount)
                .sum();
    }

    public List<Transaction> getHistory() {
        return storage.loadTransactions();
    }
}