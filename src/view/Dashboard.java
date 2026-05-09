package view;

import model.User;

public class Dashboard {
    public void displaySummary(User user) {
        System.out.println("\n=======================================");
        System.out.println("          MASROFY DASHBOARD");
        System.out.println("=======================================");
        System.out.println("User: " + user.getName());
        System.out.println("Current Balance: " + user.getCurrentBalance() + " EGP");
        System.out.println("Total Allowance: " + user.getTotalAllowance() + " EGP");
        System.out.println("=======================================");
    }
}