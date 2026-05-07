package model;

import java.util.Date;

public class Transaction {
    public enum TransactionType { INCOME, EXPENSE }

    private String description;
    private double amount;
    private TransactionType type;
    private String category;
    private Date date;

    public Transaction(String description, double amount, TransactionType type, String category) {
        this.description = description;
        this.amount = amount;
        this.type = type;
        this.category = category;
        this.date = new Date();
    }

    public String getDescription() { return description; }
    public double getAmount() { return amount; }
    public TransactionType getType() { return type; }
    public String getCategory() { return category; }
}