public class CurrentAccount extends Account {
    private double overdraftLimit;
    public CurrentAccount(String ownerName, double initialDeposit, double overdraftLimit) {
        super(ownerName, initialDeposit);
        this.overdraftLimit = Math.max(0, overdraftLimit);
    }
    public boolean withdraw(double amount, String note) {
        if (amount <= 0) throw new IllegalArgumentException("Withdraw amount must be positive.");
        if (balance + overdraftLimit >= amount) {
            balance -= amount;
            transactions.add(new Transaction(Transaction.Type.WITHDRAWAL, amount, note));
            return true;
        } else {
            transactions.add(new Transaction(Transaction.Type.FAILED, amount, "Failed withdrawal: exceeds overdraft limit"));
            return false;
        }
    }
    public double getOverdraftLimit() {
        return overdraftLimit;
    }
    public void setOverdraftLimit(double limit) {
        this.overdraftLimit = Math.max(0, limit);
    }
    public String toString() {
        return "CurrentAccount | " + statementSummary() + String.format(" | Overdraft: %.2f", overdraftLimit);
    }
}