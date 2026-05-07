package interfaces;

public interface IAlertService {
    void sendLowBalanceAlert(float balance);
    void sendCycleLimitAlert();
    void notifyUser(String message);
}