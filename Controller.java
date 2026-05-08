import model.Transaction;
import model.User;
import service.CycleManager;
import interfaces.IDataStorage;
import interfaces.IAlertService;

import java.util.ArrayList;
import java.util.List;

public class Controller {

    private CycleManager manager;
    private User user;

    public Controller(String name, float initialBalance) {
        init(name, initialBalance);
    }

    // ================= SETUP =================
    private void init(String name, float initialBalance) {

        user = new User(name, initialBalance);

        IAlertService alertService = new IAlertService() {
            @Override
            public void sendLowBalanceAlert(float balance) {
                System.out.println("[ALERT] Low Balance: " + balance);
            }

            @Override
            public void sendCycleLimitAlert() {
                System.out.println("[ALERT] Cycle Limit Exceeded!");
            }

            @Override
            public void notifyUser(String message) {
                System.out.println("[INFO] " + message);
            }
        };

        IDataStorage storage = new IDataStorage() {

            private List<Transaction> list = new ArrayList<>();

            @Override
            public void saveTransaction(Transaction t) {
                list.add(t);
            }

            @Override
            public List<Transaction> loadTransactions() {
                return list;
            }

            @Override
            public void deleteTransaction(String id) {
                list.removeIf(t -> t.getId().equals(id));
            }
        };

        manager = new CycleManager(user, storage, alertService);
    }

    // ================= CORE FUNCTIONS =================

    public void addTransaction(String desc, double amount,
                               Transaction.TransactionType type,
                               String category) {

        manager.addTransaction(desc, amount, type, category);
    }

    public List<Transaction> getAllTransactions() {
        return manager.getHistory();
    }

    public List<Transaction> getByCategory(String category) {

        if (category.equals("All")) {
            return manager.getHistory();
        }

        List<Transaction> result = new ArrayList<>();

        for (Transaction t : manager.getHistory()) {
            if (t.getCategory().equalsIgnoreCase(category)) {
                result.add(t);
            }
        }

        return result;
    }

    public void deleteTransaction(String id) {
        manager.deleteTransaction(id);
    }

    public void updateTransaction(String id, String desc,
                                  double amount, String category) {
        manager.updateTransaction(id, desc, amount, category);
    }

    public double getBalance() {
        return user.getCurrentBalance();
    }

    public double getSafeDailyLimit() {
        return manager.calculateSafeDailyLimit();
    }

    public User getUser() {
        return user;
    }
}