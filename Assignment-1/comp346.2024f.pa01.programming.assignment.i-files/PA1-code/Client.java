
import java.util.Scanner;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.InputMismatchException;

public class Client extends Thread {

    private static int numberOfTransactions;
    private static int maxNbTransactions;
    private static Transactions[] transaction;
    private static Network objNetwork;
    private String clientOperation;

    public Client(String operation) {
        if (operation.equals("sending")) {
            System.out.println("\n Initializing client sending application ...");
            numberOfTransactions = 0;
            maxNbTransactions = 100;
            transaction = new Transactions[maxNbTransactions];
            objNetwork = new Network("client");
            clientOperation = operation;
            System.out.println("\n Initializing the transactions ... ");
            readTransactions();
            System.out.println("\n Connecting client to network ...");
            String cip = objNetwork.getClientIP();
            if (!(objNetwork.connect(cip))) {
                System.out.println("\n Terminating client application, network unavailable");
                System.exit(0);
            }
        } else if (operation.equals("receiving")) {
            System.out.println("\n Initializing client receiving application ...");
            clientOperation = operation;
        }
    }

    public int getNumberOfTransactions() {
        return numberOfTransactions;
    }

    public void setNumberOfTransactions(int nbOfTrans) {
        numberOfTransactions = nbOfTrans;
    }

    public String getClientOperation() {
        return clientOperation;
    }

    public void setClientOperation(String operation) {
        clientOperation = operation;
    }

    public void readTransactions() {
        Scanner inputStream = null;
        int i = 0;

        try {
            inputStream = new Scanner(new FileInputStream("transaction.txt"));
        } catch (FileNotFoundException e) {
            System.out.println("File transaction.txt was not found");
            System.out.println("or could not be opened.");
            System.exit(0);
        }
        while (inputStream.hasNextLine()) {
            try {
                transaction[i] = new Transactions();
                transaction[i].setAccountNumber(inputStream.next());
                transaction[i].setOperationType(inputStream.next());
                transaction[i].setTransactionAmount(inputStream.nextDouble());
                transaction[i].setTransactionStatus("pending");
                i++;
            } catch (InputMismatchException e) {
                System.out.println("Line " + i + "file transactions.txt invalid input");
                System.exit(0);
            }

        }
        setNumberOfTransactions(i);
    }

    public void sendTransactions() {
        int i = 0;
        while (i < getNumberOfTransactions()) {
            while (objNetwork.getInBufferStatus().equals("full")) {
                Thread.yield();
            } /*
               * Alternatively,
               * 
               * transaction[i].setTransactionStatus("sent"); /* Set current transaction
               * status
               */

            objNetwork.send(transaction[i]);
            i++;
        }
    }

    public void receiveTransactions(Transactions transact) {
        int i = 0;

        while (i < getNumberOfTransactions()) {
            while (objNetwork.getOutBufferStatus().equals("empty")) {
                Thread.yield();
            }

            objNetwork.receive(transact);

            System.out.println(transact);
            i++;
        }
    }

    public String toString() {
        return ("\n client IP " + objNetwork.getClientIP() + " Connection status"
                + objNetwork.getClientConnectionStatus() + "Number of transactions " + getNumberOfTransactions());
    }

    public void run() {
        Transactions transact = new Transactions();
        long sendClientStartTime, sendClientEndTime, receiveClientStartTime,
                receiveClientEndTime;
        if (this.clientOperation.equals("sending")) {
            sendClientStartTime = System.currentTimeMillis();
            sendTransactions();
            sendClientEndTime = System.currentTimeMillis();
            System.out.println("\n Terminating client thread (" + this.clientOperation + ") - " + " Running time " +
                    (sendClientEndTime - sendClientStartTime)
                    + " milliseconds");
        }

        if (this.clientOperation.equals("receiving")) {
            receiveClientStartTime = System.currentTimeMillis();
            receiveTransactions(transact);
            objNetwork.disconnect(objNetwork.getClientIP());
            receiveClientEndTime = System.currentTimeMillis();
            System.out.println("\n Terminating client thread (" + this.clientOperation + ") - " + " Running time " +
                    (receiveClientEndTime - receiveClientStartTime)
                    + " milliseconds");
        }
    }
}