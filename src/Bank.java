import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
public class Bank {
    private final Map<String, Account> accounts = new HashMap<>();
    public Account createSavings(String owner, double initial, double apr) {
        Account acc = new SavingsAccount(owner, initial, apr);
        accounts.put(acc.getAccountId(), acc);
        return acc;
    }
    public Account createCurrent(String owner, double initial, double overdraft) {
        Account acc = new CurrentAccount(owner, initial, overdraft);
        accounts.put(acc.getAccountId(), acc);
        return acc;
    }
    public Optional<Account> getAccount(String accountId) {
        return Optional.ofNullable(accounts.get(accountId));
    }
    public Collection<Account> listAccounts() {
        return accounts.values();
    }
    public boolean transfer(String fromId, String toId, double amount, String note) {
        Account from = accounts.get(fromId);
        Account to = accounts.get(toId);
        if (from == null || to == null) return false;
        boolean ok = from.withdraw(amount, "Transfer to " + toId + " | " + note);
        if (!ok) {
            from.getTransactions().add(new Transaction(Transaction.Type.FAILED, amount, "Transfer failed: insufficient funds"));
            return false;
        }
        to.deposit(amount, "Transfer from " + fromId + " | " + note);
        from.getTransactions().add(new Transaction(Transaction.Type.TRANSFER_OUT, amount, "Transfer out to " + toId));
        to.getTransactions().add(new Transaction(Transaction.Type.TRANSFER_IN, amount, "Transfer in from " + fromId));
        return true;
    }
    public String exportStatementCSV(String accountId) throws IOException {
        Account acc = accounts.get(accountId);
        if (acc == null) throw new IllegalArgumentException("Account not found.");
        File folder = new File("data");
        if (!folder.exists()) folder.mkdirs();
        String filename = String.format("data/statement_%s.csv", accountId.substring(0, Math.min(6, accountId.length())));
        try (FileWriter fw = new FileWriter(filename)) {
            fw.write(acc.toCSV());
        }
        return new File(filename).getAbsolutePath();
    }
}