import Controller.AppController;

/**
 * Main.java
 * Entry point of the application.
 * Just creates the controller and calls start().
 */
public class Main {
    public static void main(String[] args) {
        AppController.getInstance().start();
    }
}



















//import model.Transaction;
//import model.User;
//import service.CycleManager;
//import interfaces.IDataStorage;
//import java.util.*;
//
//public class Main {
//    private static Scanner scanner = new Scanner(System.in);
//    private static CycleManager manager;
//    private static User myUser;
//
//    public static void main(String[] args) {
//        initializeSystem();
//        runAppLoop();
//    }
//
//    private static void initializeSystem() {
//        System.out.println("=== Masrofy System Initializing ===");
//        System.out.print("Enter Your Name: ");
//        String name = scanner.nextLine();
//
//
//        myUser = new User(name, 0);
//
//
//        IDataStorage mockStorage = new IDataStorage() {
//            private List<Transaction> list = new ArrayList<>();
//            @Override
//            public void saveTransaction(Transaction t) { list.add(t); }
//            @Override
//            public List<Transaction> loadTransactions() { return list; }
//        };
//
//        manager = new CycleManager(myUser, mockStorage);
//    }
//
//    private static void runAppLoop() {
//        boolean exit = false;
//        while (!exit) {
//            printMenu();
//            int choice = getIntInput();
//
//            switch (choice) {
//                case 1 -> handleTransaction(Transaction.TransactionType.INCOME);
//                case 2 -> handleTransaction(Transaction.TransactionType.EXPENSE);
//                case 3 -> showFinancialSummary();
//                case 4 -> showCategoryReport();
//                case 5 -> showFullHistory();
//                case 6 -> {
//                    exit = true;
//                    System.out.println("System Closing... Done.");
//                }
//                default -> System.out.println("Invalid option! Try again.");
//            }
//        }
//    }
//
//    private static void printMenu() {
//        System.out.println("\n--- MENU ---");
//        System.out.println("1. Add Income");
//        System.out.println("2. Add Expense");
//        System.out.println("3. Balance Summary");
//        System.out.println("4. Category Report");
//        System.out.println("5. History");
//        System.out.println("6. Exit");
//        System.out.print("Select: ");
//    }
//
//    private static void handleTransaction(Transaction.TransactionType type) {
//        System.out.print("Description: ");
//        String desc = scanner.nextLine();
//        System.out.print("Amount: ");
//        double amount = scanner.nextDouble();
//        scanner.nextLine();
//        System.out.print("Category: ");
//        String category = scanner.nextLine();
//
//        manager.addTransaction(desc, amount, type, category);
//        System.out.println("Successfully Added!");
//    }
//
//    private static void showFinancialSummary() {
//        System.out.println("\n--- FINANCIAL SUMMARY ---");
//        System.out.println("User: " + myUser.getName());
//        double totalIncome = manager.getTotalByType(Transaction.TransactionType.INCOME);
//        double totalExpense = manager.getTotalByType(Transaction.TransactionType.EXPENSE);
//
//        System.out.println("Total Income:   " + totalIncome + " EGP");
//        System.out.println("Total Expenses: " + totalExpense + " EGP");
//        System.out.println("Net Balance:    " + (totalIncome - totalExpense) + " EGP");
//    }
//
//    private static void showCategoryReport() {
//        System.out.println("\n--- EXPENSES BY CATEGORY ---");
//        Map<String, Double> report = manager.getExpensesByCategory();
//        if (report.isEmpty()) {
//            System.out.println("No expenses to group.");
//        } else {
//            report.forEach((cat, sum) -> System.out.println(cat + ": " + sum + " EGP"));
//        }
//    }
//
//    private static void showFullHistory() {
//        System.out.println("\n--- FULL HISTORY ---");
//        List<Transaction> history = manager.getHistory();
//        if (history.isEmpty()) {
//            System.out.println("No data found.");
//        } else {
//            history.forEach(t ->
//                    System.out.println("[" + t.getType() + "] " + t.getCategory() + " - " + t.getAmount() + " (" + t.getDescription() + ")")
//            );
//        }
//    }
//
//    private static int getIntInput() {
//        try {
//            int input = scanner.nextInt();
//            scanner.nextLine();
//            return input;
//        } catch (Exception e) {
//            scanner.nextLine();
//            return -1;
//        }
//    }
//}