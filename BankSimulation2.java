class BankAccount {
    private int balance = 1000;
    
    public synchronized void deposit(int amount) {
        balance += amount;
        System.out.println("Deposited " + amount + ", Balance: " + balance);
    }
    
    public synchronized void withdraw(int amount) {
        if (balance >= amount) {
            balance -= amount;
            System.out.println("Withdrawn " + amount + ", Balance: " + balance);
        } else {
            System.out.println("Insufficient funds for " + amount);
        }
    }
    
    public int getBalance() {
        return balance;
    }
}

class Customer implements Runnable {
    private BankAccount account;
    private String action;
    private int amount;
    
    public Customer(BankAccount account, String action, int amount) {
        this.account = account;
        this.action = action;
        this.amount = amount;
    }
    
    public void run() {
        if (action.equals("deposit")) {
            account.deposit(amount);
        } else if (action.equals("withdraw")) {
            account.withdraw(amount);
        }
    }
}

public class BankSimulation2 {
    public static void main(String[] args) throws InterruptedException {
        BankAccount account = new BankAccount();
        Thread[] customers = new Thread[3];
        
        customers[0] = new Thread(new Customer(account, "deposit", 500));
        customers[1] = new Thread(new Customer(account, "withdraw", 700));
        customers[2] = new Thread(new Customer(account, "withdraw", 600));
        
        for (Thread t : customers) {
            t.start();
        }
        
        // Wait for all threads to complete
        for (Thread t : customers) {
            t.join();
        }
        
        System.out.println("Final Balance: " + account.getBalance());
    }
}