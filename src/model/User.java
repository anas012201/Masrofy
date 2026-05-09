package model;

import java.util.Date;
import java.util.Calendar;

public class User {
    private String name;
    private float initialBudget;
    private float currentBalance;
    private Date cycleEndDate;

    public User(String name, float initialBudget) {
        this.name = name;
        this.initialBudget = initialBudget;
        this.currentBalance = initialBudget;
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, 1);
        this.cycleEndDate = cal.getTime();
    }

    public void addIncome(float amount) {
        this.currentBalance += amount;
    }

    public void spend(float amount) {
        this.currentBalance -= amount;
    }

    public void adjustBalance(float amount, Transaction.TransactionType type) {
        if (type == Transaction.TransactionType.INCOME) addIncome(amount);
        else spend(amount);
    }

    public String getName() { return name; }
    public float getCurrentBalance() { return currentBalance; }
    public float getTotalAllowance() { return initialBudget; }
    public Date getCycleEndDate() { return cycleEndDate; }
}