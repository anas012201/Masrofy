package interfaces;

import model.Transaction;
import java.util.List;

public interface IDataStorage {
    void saveTransaction(Transaction t);
    List<Transaction> loadTransactions();
}