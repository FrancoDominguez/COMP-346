public class Accounts {

    private String accountNumber;
    private String accountType;
    private String firstName;
    private String lastName;
    private double balance;

    Accounts() {
        accountNumber = " ";
        accountType = " ";
        firstName = " ";
        lastName = " ";
        balance = 0.0;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accNumber) {
        accountNumber = accNumber;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accType) {
        accountType = accType;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String fName) {
        firstName = fName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lName) {
        lastName = lName;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double bal) {
        balance = bal;
    }

    public String toString() {
        return ("\n Account number " + getAccountNumber() + "Account type " + getAccountType() + "First name "
                + getFirstName() + "Last Name " + getLastName() + "Balance " + getBalance());
    }

}
