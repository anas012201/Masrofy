// New file: model/data/IDataStorage.java
package interfaces;
import java.util.List;

public interface IDataStorage {
    int addCycle(double allowance, String startDate, String endDate);
    String[] getActiveCycle();
    boolean hasActiveCycle();
    void endCycle(int cycleId);
    void resetCycle(int cycleId);
    int addTransaction(int cycleId, double amount, String category, String timestamp);
    List<String[]> getAllTransactions(int cycleId);
    List<String[]> getTransactionsByCategory(int cycleId, String category);
    double getTotalSpent(int cycleId);
    void editTransaction(int transId, double newAmount, String newCategory);
    void removeTransaction(int transId);
    void savePin(String pin);
    String getPin();
    boolean checkPin(String enteredPin);
    void setPrivacyLock(boolean isEnabled);
    boolean isPrivacyLockEnabled();
}