package interfaces;

import model.Expense;

public interface ICycleOperations {
    void addExpense(Expense expense) throws Exception;
    float calculateSafeDailyLimit();
}