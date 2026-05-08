import model.Transaction;
import model.User;
import service.CycleManager;
import interfaces.IDataStorage;
import interfaces.IAlertService;
import java.util.*;

public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static CycleManager manager;
    private static User myUser;

    public static void main(String[] args) {
        setupSystem();
        runApp();
    }

    private static void setupSystem() {
        System.out.println("=== Masrofy System ===");
        System.out.print("Enter Your Name: ");
        String name = scanner.nextLine();
        System.out.print("Initial Budget: ");
        float initialBudget = scanner.nextFloat();
        scanner.nextLine();

        myUser = new User(name, initialBudget);

        IAlertService alertService = new IAlertService() {
            @Override
            public void sendLowBalanceAlert(float balance) {
                System.out.println("\n[ALERT] Low Balance: " + balance + " EGP");
            }
            @Override
            public void sendCycleLimitAlert() {
                System.out.println("\n[ALERT] Warning: Daily Limit Exceeded!");
            }
            @Override
            public void notifyUser(String message) {
                System.out.println("\n[INFO] " + message);
            }
        };

        IDataStorage storage = new IDataStorage() {
            private List<Transaction> list = new ArrayList<>();
            @Override
            public void saveTransaction(Transaction t) { list.add(t); }
            @Override
            public List<Transaction> loadTransactions() { return list; }
        };

        manager = new CycleManager(myUser, storage, alertService);
    }

    private static void runApp() {
        while (true) {
            System.out.println("\n1. Income | 2. Expense | 3. Status | 4. History | 5. Exit");
            System.out.print("Selection: ");
            int choice = getIntInput();

            switch (choice) {
                case 1 -> addEntry(Transaction.TransactionType.INCOME);
                case 2 -> addEntry(Transaction.TransactionType.EXPENSE);
                case 3 -> showStatus();
                case 4 -> showHistory();
                case 5 -> System.exit(0);
                default -> System.out.println("Invalid Option!");
            }
        }
    }

    private static void addEntry(Transaction.TransactionType type) {
        System.out.print("Description: ");
        String desc = scanner.nextLine();
        System.out.print("Amount: ");
        double amt = scanner.nextDouble();
        scanner.nextLine();
        System.out.print("Category: ");
        String cat = scanner.nextLine();

        manager.addTransaction(desc, amt, type, cat);
        System.out.println("Transaction Saved.");
    }

    private static void showStatus() {
        System.out.println("\n--- Financial Status ---");
        System.out.println("User: " + myUser.getName());
        System.out.println("Current Balance: " + myUser.getCurrentBalance() + " EGP");
        double dailyLimit = manager.calculateSafeDailyLimit();
        System.out.println("Safe Daily Limit: " + String.format("%.2f", dailyLimit) + " EGP");
        System.out.println("Cycle End Date: " + myUser.getCycleEndDate());
    }

    private static void showHistory() {
        System.out.println("\n--- Full History ---");
        List<Transaction> history = manager.getHistory();
        if (history.isEmpty()) {
            System.out.println("No transactions found.");
        } else {
            for (Transaction t : history) {
                System.out.println("[" + t.getFormattedDate() + "] " +
                        t.getType() + ": " + t.getAmount() + " EGP | " +
                        "Cat: " + t.getCategory() + " | " +
                        "Desc: " + t.getDescription());
            }
        }
    }

    private static int getIntInput() {
        try {
            int input = scanner.nextInt();
            scanner.nextLine();
            return input;
        } catch (Exception e) {
            scanner.nextLine();
            return -1;
        }
    }
}