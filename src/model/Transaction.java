package model;

import java.util.Date;
import java.text.SimpleDateFormat;

public class Transaction {
    public enum TransactionType { INCOME, EXPENSE }

    private String description;
    private double amount;
    private TransactionType type;
    private String category;
    private Date timestamp;

    public Transaction(String description, double amount, TransactionType type, String category) {
        this.description = description;
        this.amount = amount;
        this.type = type;
        this.category = category;
        this.timestamp = new Date();
    }

    public String getFormattedDate() {
        return new SimpleDateFormat("yyyy-MM-dd").format(timestamp);
    }

    public String getDescription() { return description; }
    public double getAmount() { return amount; }
    public TransactionType getType() { return type; }
    public String getCategory() { return category; }
}