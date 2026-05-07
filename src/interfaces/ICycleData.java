package interfaces;

import model.Expense;
import java.util.List;

public interface ICycleData {
    float getUserBalance();
    float getTotalBudget();
    List<Expense> getAllExpenses();
}