import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
public abstract class Account {
    protected final String accountId;
    protected String ownerName;
    protected double balance;
    protected final List<Transaction> transactions;
    public Account(String ownerName, double initialDeposit) {
        this.accountId = UUID.randomUUID().toString();
        this.ownerName = ownerName;
        this.balance = Math.max(0, initialDeposit);
        this.transactions = new ArrayList<>();
        if (initialDeposit > 0) {
            transactions.add(new Transaction(Transaction.Type.DEPOSIT, initialDeposit, "Initial deposit"));
        }
    }
    public String getAccountId() {
        return accountId;
    }
    public String getOwnerName() {
        return ownerName;
    }
    public double getBalance() {
        return balance;
    }
    public List<Transaction> getTransactions() {
        return Collections.unmodifiableList(transactions);
    }
    public void deposit(double amount, String note) {
        if (amount <= 0) throw new IllegalArgumentException("Deposit amount must be positive.");
        balance += amount;
        transactions.add(new Transaction(Transaction.Type.DEPOSIT, amount, note));
    }
    /**
     * Withdraw - may be overridden for account-specific rules (overdraft, fees, etc.)
     * @param amount positive amount to withdraw
     * @param note description
     * @return true if withdrawal successful
     */
    public boolean withdraw(double amount, String note) {
        if (amount <= 0) throw new IllegalArgumentException("Withdraw amount must be positive.");
        if (balance >= amount) {
            balance -= amount;
            transactions.add(new Transaction(Transaction.Type.WITHDRAWAL, amount, note));
            return true;
        } else {
            transactions.add(new Transaction(Transaction.Type.FAILED, amount, "Failed withdrawal: insufficient funds"));
            return false;
        }
    }
    public String statementSummary() {
        return String.format("Account ID: %s | Owner: %s | Balance: %.2f", accountId, ownerName, balance);
    }
    public String toCSV() {
        StringBuilder sb = new StringBuilder();
        sb.append("Account ID,Owner,Type,Amount,Date,Note,BalanceAfter\n");
        double running = 0.0;
        for (Transaction t : transactions) {
            if (t.getType() == Transaction.Type.DEPOSIT) running += t.getAmount();
            else if (t.getType() == Transaction.Type.WITHDRAWAL) running -= t.getAmount();
            sb.append(String.format("%s,%s,%s,%.2f,%s,%s,%.2f\n",
                    accountId,
                    ownerName.replace(",", " "),
                    t.getType(),
                    t.getAmount(),
                    t.getTimestamp(),
                    t.getNote().replace(",", " "),
                    running
            ));
        }
        return sb.toString();
    }
}