public class SavingsAccount extends Account {
    private double annualInterestRate;
    public SavingsAccount(String ownerName, double initialDeposit, double annualInterestRate) {
        super(ownerName, initialDeposit);
        this.annualInterestRate = Math.max(0, annualInterestRate);
    }
    public double getAnnualInterestRate() {
        return annualInterestRate;
    }
    public void setAnnualInterestRate(double rate) {
        this.annualInterestRate = Math.max(0, rate);
    }
    public void applyMonthlyInterest() {
        double monthlyRate = annualInterestRate / 12.0;
        double interest = balance * monthlyRate;
        if (interest > 0) {
            deposit(interest, "Monthly interest");
        }
    }
    public String toString() {
        return "SavingsAccount | " + statementSummary() + String.format(" | APR: %.2f%%", annualInterestRate * 100);
    }
}