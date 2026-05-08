package model;

import java.util.Calendar;
import java.util.Date;

public class User {
    private String name;
    private float currentBalance;
    private float totalAllowance;
    private Date cycleStartDate;
    private Date cycleEndDate;

    public User(String name, float initialBalance) {
        this.name = name;
        this.currentBalance = initialBalance;
        this.totalAllowance = initialBalance;
        this.cycleStartDate = new Date();

        Calendar cal = Calendar.getInstance();
        cal.setTime(this.cycleStartDate);
        cal.add(Calendar.DAY_OF_MONTH, 30);
        this.cycleEndDate = cal.getTime();
    }

    public void adjustBalance(float amount, Transaction.TransactionType type) {
        if (type == Transaction.TransactionType.INCOME) {
            this.currentBalance += amount;
        } else {
            this.currentBalance -= amount;
        }
    }

    public String getName() {
        return name;
    }

    public float getCurrentBalance() {
        return currentBalance;
    }

    public float getTotalAllowance() {
        return totalAllowance;
    }

    public Date getCycleStartDate() {
        return cycleStartDate;
    }

    public Date getCycleEndDate() {
        return cycleEndDate;
    }

    public void setCycleEndDate(Date cycleEndDate) {
        this.cycleEndDate = cycleEndDate;
    }
}