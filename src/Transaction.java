import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
public class Transaction {
    public enum Type { DEPOSIT, WITHDRAWAL, TRANSFER_IN, TRANSFER_OUT, FAILED }
    private final Type type;
    private final double amount;
    private final String timestamp;
    private final String note;
    public Transaction(Type type, double amount, String note) {
        this.type = type;
        this.amount = amount;
        this.note = note == null ? "" : note;
        this.timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
    public Type getType() {
        return type;
    }
    public double getAmount() {
        return amount;
    }
    public String getTimestamp() {
        return timestamp;
    }
    public String getNote() {
        return note;
    }
    public String toString() {
        return String.format("[%s] %s : %.2f | %s", timestamp, type, amount, note);
    }
}