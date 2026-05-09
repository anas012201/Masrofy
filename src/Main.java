import controller.Controller;
import controller.DatabaseManager;
import view.TransactionHistoryScreen;

public class Main {
    public static void main(String[] args) {
        DatabaseManager.initDatabase();
        Controller controller = new Controller("Anas", 0f);
        TransactionHistoryScreen history = new TransactionHistoryScreen();
        history.loadDataFromController(controller);
        history.setVisible(true);
    }
}