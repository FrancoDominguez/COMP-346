import java.util.Scanner;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.InputMismatchException;

public class Server extends Thread {
    int numberOfTransactions; /* Number of transactions handled by the server */
    int numberOfAccounts; /* Number of accounts stored in the server */
    int maxNbAccounts; /* maximum number of transactions */
    Transactions transaction; /* Transaction being processed */
    Network objNetwork; /* Server object to handle network operations */
    Accounts[] account; /* Accounts to be accessed or updated */

    public Server() {
        System.out.println("\n Initializing the server ...");
        numberOfTransactions = 0;
        numberOfAccounts = 0;
        maxNbAccounts = 100;
        transaction = new Transactions();
        account = new Accounts[maxNbAccounts];
        this.objNetwork = new Network("server");
        System.out.println("\n Inializing the Accounts database ...");
        initializeAccounts();
        System.out.println("\n Connecting server to network ...");
        if (!(this.objNetwork.connect(this.objNetwork.getServerIP()))) {
            System.out.println("\n Terminating server application, network unavailable");
            System.exit(0);
        }
    }

    public int getNumberOfTransactions() {
        return numberOfTransactions;
    }

    public void setNumberOfTransactions(int nbOfTrans) {
        numberOfTransactions = nbOfTrans;
    }

    public int getNumberOfAccounts() {
        return numberOfAccounts;
    }

    public void setNumberOfAccounts(int nbOfAcc) {
        numberOfAccounts = nbOfAcc;
    }

    public int getmMxNbAccounts() {
        return maxNbAccounts;
    }

    public void setMaxNbAccounts(int nbOfAcc) {
        maxNbAccounts = nbOfAcc;
    }

    public void initializeAccounts() {
        Scanner inputStream = null;
        int i = 0;

        try {
            inputStream = new Scanner(new FileInputStream("account.txt"));
        } catch (FileNotFoundException e) {
            System.out.println("File account.txt was not found");
            System.out.println("or could not be opened.");
            System.exit(0);
        }
        while (inputStream.hasNextLine()) {
            try {
                account[i] = new Accounts();
                account[i].setAccountNumber(inputStream.next());
                account[i].setAccountType(inputStream.next());
                account[i].setFirstName(inputStream.next());
                account[i].setLastName(inputStream.next());
                account[i].setBalance(inputStream.nextDouble());
            } catch (InputMismatchException e) {
                System.out.println("Line " + i + "file account.txt invalid input");
                System.exit(0);
            }
            i++;
        }
        setNumberOfAccounts(i);

        inputStream.close();
    }

    public int findAccount(String accNumber) {
        int i = 0;

        while (!(account[i].getAccountNumber().equals(accNumber)))
            i++;
        if (i == getNumberOfAccounts())
            return -1;
        else
            return i;
    }

    public boolean processTransactions(Transactions trans) {
        int accIndex;
        double newBalance;

        while ((!this.objNetwork.getClientConnectionStatus().equals("disconnected"))) {

            while ((objNetwork.getInBufferStatus().equals("empty"))) {
                if (objNetwork.getClientConnectionStatus().equals("disconnected")) {
                    break;
                }
                Thread.yield();
            }

            if (!this.objNetwork.getInBufferStatus().equals("empty")) {

                this.objNetwork.transferIn(trans);

                accIndex = findAccount(trans.getAccountNumber());
                if (trans.getOperationType().equals("DEPOSIT")) {
                    newBalance = deposit(accIndex, trans.getTransactionAmount());
                    trans.setTransactionBalance(newBalance);
                    trans.setTransactionStatus("done");

                } else if (trans.getOperationType().equals("WITHDRAW")) {
                    newBalance = withdraw(accIndex, trans.getTransactionAmount());
                    trans.setTransactionBalance(newBalance);
                    trans.setTransactionStatus("done");

                } else if (trans.getOperationType().equals("QUERY")) {
                    newBalance = query(accIndex);
                    trans.setTransactionBalance(newBalance);
                    trans.setTransactionStatus("done");

                }

                while ((objNetwork.getOutBufferStatus().equals("full"))) {
                    Thread.yield();
                }

                this.objNetwork.transferOut(trans);
                setNumberOfTransactions((getNumberOfTransactions() + 1));
            }
        }

        return true;
    }

    public double deposit(int i, double amount) {
        double curBalance;

        curBalance = account[i].getBalance();
        account[i].setBalance(curBalance + amount);
        return account[i].getBalance();
    }

    public double withdraw(int i, double amount) {
        double curBalance;

        curBalance = account[i].getBalance();
        account[i].setBalance(curBalance - amount);
        return account[i].getBalance();
    }

    public double query(int i) {
        double curBalance;

        curBalance = account[i].getBalance();
        return curBalance;
    }

    public String toString() {
        return ("\n server IP " + this.objNetwork.getServerIP() + "connection status "
                + this.objNetwork.getServerConnectionStatus() + "Number of accounts " + getNumberOfAccounts());
    }

    public void run() {
        Transactions trans = new Transactions();
        long serverStartTime, serverEndTime;

        serverStartTime = System.currentTimeMillis();
        processTransactions(trans);
        serverEndTime = System.currentTimeMillis();
        objNetwork.disconnect(objNetwork.getServerIP());

        System.out.println("\n Terminating server thread - " + " Running time " +
                (serverEndTime - serverStartTime)
                + " milliseconds");

    }
}
