import java.io.IOException;
import java.util.Scanner;
public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final Bank bank = new Bank();
    public static void main(String[] args) {
        System.out.println("=== Bank Account Simulation ===");
        seedDemoData();
        boolean running = true;
        while (running) {
            showMenu();
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1" -> createAccount();
                case "2" -> listAccounts();
                case "3" -> deposit();
                case "4" -> withdraw();
                case "5" -> transfer();
                case "6" -> exportStatement();
                case "7" -> applyInterest();
                case "8" -> showAccountDetails();
                case "9" -> {
                    System.out.println("Exiting. Good luck!");
                    running = false;
                }
                default -> System.out.println("Invalid option. Enter a number from the menu.");
            }
        }
    }
    private static void showMenu() {
        System.out.println("\nMenu:");
        System.out.println("1. Create account (Savings or Current)");
        System.out.println("2. List all accounts");
        System.out.println("3. Deposit");
        System.out.println("4. Withdraw");
        System.out.println("5. Transfer between accounts");
        System.out.println("6. Export account statement to CSV (data/)");
        System.out.println("7. Apply monthly interest to Savings accounts (demo)");
        System.out.println("8. Show account details & transactions");
        System.out.println("9. Exit");
        System.out.print("Choose: ");
    }
    private static void seedDemoData() {
        Account s1 = bank.createSavings("Sai Varun", 5000, 0.04);
        Account c1 = bank.createCurrent("Family Business", 2000, 1000);
        s1.deposit(1500, "Salary");
        c1.withdraw(100, "Supplies");
        System.out.println("Created demo accounts. You can create more via menu.");
    }
    private static void createAccount() {
        System.out.print("Type (savings/current): ");
        String type = scanner.nextLine().trim().toLowerCase();
        System.out.print("Owner name: ");
        String owner = scanner.nextLine().trim();
        System.out.print("Initial deposit: ");
        double initial = Double.parseDouble(scanner.nextLine().trim());
        if ("savings".equals(type)) {
            System.out.print("Annual interest rate (max 4% allowed): ");
            String rateInput = scanner.nextLine().trim().replace("%", "");
            double apr = Double.parseDouble(rateInput);
            if (apr > 1) {
                apr = apr / 100.0;
            }
            if (apr > 0.04) {
                System.out.println("Error: Interest rate cannot exceed 4%.");
                return;
            }
            Account acc = bank.createSavings(owner, initial, apr);
            System.out.println("Created: " + acc.getAccountId());
        } else {
            System.out.print("Overdraft limit: ");
            double od = Double.parseDouble(scanner.nextLine().trim());
            Account acc = bank.createCurrent(owner, initial, od);
            System.out.println("Created: " + acc.getAccountId());
        }
    }
    private static void listAccounts() {
        System.out.println("Accounts:");
        for (Account a : bank.listAccounts()) {
            System.out.println(a);
        }
    }
    private static void deposit() {
        try {
            System.out.print("Account ID: ");
            String id = scanner.nextLine().trim();
            Account acc = bank.getAccount(id).orElseThrow(() -> new IllegalArgumentException("Account not found."));
            System.out.print("Amount to deposit: ");
            double amt = Double.parseDouble(scanner.nextLine().trim());
            System.out.print("Note: ");
            String note = scanner.nextLine().trim();
            acc.deposit(amt, note);
            System.out.println("Deposit successful. New balance: " + acc.getBalance());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    private static void withdraw() {
        try {
            System.out.print("Account ID: ");
            String id = scanner.nextLine().trim();
            Account acc = bank.getAccount(id).orElseThrow(() -> new IllegalArgumentException("Account not found."));
            System.out.print("Amount to withdraw: ");
            double amt = Double.parseDouble(scanner.nextLine().trim());
            System.out.print("Note: ");
            String note = scanner.nextLine().trim();
            boolean ok = acc.withdraw(amt, note);
            if (ok) System.out.println("Withdrawal successful. New balance: " + acc.getBalance());
            else System.out.println("Withdrawal failed. Current balance: " + acc.getBalance());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    private static void transfer() {
        try {
            System.out.print("From Account ID: ");
            String from = scanner.nextLine().trim();
            System.out.print("To Account ID: ");
            String to = scanner.nextLine().trim();
            System.out.print("Amount: ");
            double amt = Double.parseDouble(scanner.nextLine().trim());
            System.out.print("Note: ");
            String note = scanner.nextLine().trim();
            boolean ok = bank.transfer(from, to, amt, note);
            if (ok) System.out.println("Transfer completed.");
            else System.out.println("Transfer failed. Check balances and IDs.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    private static void exportStatement() {
        try {
            System.out.print("Account ID: ");
            String id = scanner.nextLine().trim();
            String path = bank.exportStatementCSV(id);
            System.out.println("Exported statement to: " + path);
        } catch (IOException e) {
            System.out.println("IO Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    private static void applyInterest() {
        for (Account a : bank.listAccounts()) {
            if (a instanceof SavingsAccount) {
                ((SavingsAccount) a).applyMonthlyInterest();
            }
        }
        System.out.println("Applied monthly interest to all savings accounts.");
    }
    private static void showAccountDetails() {
        System.out.print("Account ID: ");
        String id = scanner.nextLine().trim();
        try {
            Account a = bank.getAccount(id).orElseThrow(() -> new IllegalArgumentException("Account not found."));
            System.out.println(a);
            System.out.println("Transactions:");
            for (Transaction t : a.getTransactions()) {
                System.out.println("  " + t);
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}