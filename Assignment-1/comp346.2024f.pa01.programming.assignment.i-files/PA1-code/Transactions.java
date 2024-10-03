public class Transactions {

    private String accountNumber;
    private String operationType;
    private double transactionAmount;
    private double transactionBalance;
    private String transactionError;
    private String transactionStatus;

    Transactions() {
        accountNumber = " ";
        operationType = " ";
        transactionAmount = 0.00;
        transactionBalance = 0.00;
        transactionError = "none";
        transactionStatus = " ";
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accNumber) {
        accountNumber = accNumber;
    }

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String opType) {
        operationType = opType;
    }

    public double getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(double transAmount) {
        transactionAmount = transAmount;
    }

    public double getTransactionBalance() {
        return transactionBalance;
    }

    public void setTransactionBalance(double transBalance) {
        transactionBalance = transBalance;
    }

    public String getTransactionError() {
        return transactionError;
    }

    public void setTransactionError(String transError) {
        transactionError = transError;
    }

    public String getTransactionStatus() {
        return transactionStatus;
    }

    public void setTransactionStatus(String transStatus) {
        transactionError = transStatus;
    }

    public String toString() {
        return ("\n Account number " + getAccountNumber() + " Account Balance " + getTransactionBalance() + " Message "
                + getTransactionError());
    }

}
