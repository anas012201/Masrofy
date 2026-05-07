package service;

import model.Transaction;
import model.User;
import interfaces.IDataStorage;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CycleManager {
    private User user;
    private IDataStorage storage;

    public CycleManager(User user, IDataStorage storage) {
        this.user = user;
        this.storage = storage;
    }

    public void addTransaction(String desc, double amount, Transaction.TransactionType type, String category) {
        Transaction t = new Transaction(desc, amount, type, category);
        storage.saveTransaction(t);
        user.adjustBalance((float)amount, type);
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