package model;
import java.util.Date;

public class Expense {
    private String expenseId;
    private float amount;
    private Date date;
    private String note;
    private String categoryId;

    public Expense(String expenseId, float amount, String categoryId, String note) {
        this.expenseId = expenseId;
        this.amount = amount;
        this.categoryId = categoryId;
        this.note = note;
        this.date = new Date();
    }

    public float getAmount() { return amount; }
    public String getCategoryId() { return categoryId; }
    public String getNote() { return note; }
    public Date getDate() { return date; }
}