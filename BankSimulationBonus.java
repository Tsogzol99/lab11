import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class BankAccount {
    private int balance = 1000;
    private ArrayList<String> transactionLog = new ArrayList<>();
    
    public synchronized void deposit(int amount) {
        balance += amount;
        String transaction = Thread.currentThread().getName() + " - Deposited " + amount + ", Balance: " + balance;
        transactionLog.add(transaction);
        System.out.println(transaction);
    }
    
    public synchronized void withdraw(int amount) {
        if (balance >= amount) {
            balance -= amount;
            String transaction = Thread.currentThread().getName() + " - Withdrawn " + amount + ", Balance: " + balance;
            transactionLog.add(transaction);
            System.out.println(transaction);
        } else {
            String transaction = Thread.currentThread().getName() + " - Insufficient funds for " + amount;
            transactionLog.add(transaction);
            System.out.println(transaction);
        }
    }
    
    public int getBalance() {
        return balance;
    }
    
    public void printTransactionLog() {
        System.out.println("\n--- Transaction Log ---");
        for (String transaction : transactionLog) {
            System.out.println(transaction);
        }
        System.out.println("---------------------");
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

public class BankSimulationBonus {
    public static void main(String[] args) throws InterruptedException {
        BankAccount account = new BankAccount();
        
        // Using threads with priority
        Thread[] customers = new Thread[3];
        customers[0] = new Thread(new Customer(account, "deposit", 500));
        customers[1] = new Thread(new Customer(account, "withdraw", 700));
        customers[2] = new Thread(new Customer(account, "withdraw", 600));
        
        // Set deposit to high priority
        customers[0].setPriority(Thread.MAX_PRIORITY);
        customers[1].setPriority(Thread.NORM_PRIORITY);
        customers[2].setPriority(Thread.NORM_PRIORITY);
        
        System.out.println("--- Using Threads with Priority ---");
        for (Thread t : customers) {
            t.start();
        }
        
        for (Thread t : customers) {
            t.join();
        }
        
        System.out.println("Final Balance with Threads: " + account.getBalance());
        account.printTransactionLog();
        
        // Reset for executor service example
        account = new BankAccount();
        
        // Using ExecutorService
        System.out.println("\n--- Using ExecutorService ---");
        ExecutorService executor = Executors.newFixedThreadPool(2);
        
        executor.submit(new Customer(account, "deposit", 500));
        executor.submit(new Customer(account, "withdraw", 700));
        executor.submit(new Customer(account, "withdraw", 600));
        
        executor.shutdown();
        
        while (!executor.isTerminated()) {
            Thread.sleep(100);
        }
        
        System.out.println("Final Balance with Executor: " + account.getBalance());
        account.printTransactionLog();
    }
}