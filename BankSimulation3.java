import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class BankAccount {
    private int balance = 1000;
    
    public synchronized void deposit(int amount) {
        balance += amount;
        System.out.println(Thread.currentThread().getName() + " - Deposited " + amount + ", Balance: " + balance);
    }
    
    public synchronized void withdraw(int amount) {
        if (balance >= amount) {
            balance -= amount;
            System.out.println(Thread.currentThread().getName() + " - Withdrawn " + amount + ", Balance: " + balance);
        } else {
            System.out.println(Thread.currentThread().getName() + " - Insufficient funds for " + amount);
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

public class BankSimulation3 {
    public static void main(String[] args) throws InterruptedException {
        BankAccount account = new BankAccount();
        
        // Create a thread pool with 2 threads
        ExecutorService executor = Executors.newFixedThreadPool(2);
        
        // Submit tasks to the executor
        executor.submit(new Customer(account, "deposit", 500));
        executor.submit(new Customer(account, "withdraw", 700));
        executor.submit(new Customer(account, "withdraw", 600));
        
        // Shutdown the executor
        executor.shutdown();
        
        // Wait until all tasks are completed
        while (!executor.isTerminated()) {
            Thread.sleep(100);
        }
        
        System.out.println("Final Balance: " + account.getBalance());
    }
}