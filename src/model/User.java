package model;

import model.Transaction;

public class User {
    private String name;
    private float currentBalance;
    private float totalAllowance;

    public User(String name, float initialBalance) {
        this.name = name;
        this.currentBalance = initialBalance;
        this.totalAllowance = initialBalance;
    }

    public void adjustBalance(float amount, Transaction.TransactionType type) {
        if (type == Transaction.TransactionType.INCOME) {
            this.currentBalance += amount;
        } else {
            this.currentBalance -= amount;
        }
    }

    public String getName() { return name; }
    public float getCurrentBalance() { return currentBalance; }
    public float getTotalAllowance() { return totalAllowance; }
}