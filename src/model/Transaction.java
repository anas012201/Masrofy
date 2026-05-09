package model;

public class Transaction {
    public enum TransactionType { INCOME, EXPENSE }
    private String category;
    private double amount;
    private TransactionType type;
    private String date;

    public Transaction(String category, double amount, TransactionType type, String date) {
        this.category = category;
        this.amount = amount;
        this.type = type;
        this.date = date;
    }

    public String getCategory() { return category; }
    public double getAmount() { return amount; }
    public TransactionType getType() { return type; }
    public String getFormattedDate() { return date; }
}