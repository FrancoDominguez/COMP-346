public class Network extends Thread {

    private static int maxNbPackets;
    private static int inputIndexClient, inputIndexServer, outputIndexServer, outputIndexClient;
    private static String clientIP;
    private static String serverIP;
    private static int portID;
    private static String clientConnectionStatus;
    private static String serverConnectionStatus;
    private static Transactions inComingPacket[];
    private static Transactions outGoingPacket[];
    private static String inBufferStatus, outBufferStatus;
    private static String networkStatus;

    public Network(String context) {
        if (context.equals("network")) {
            System.out.println("\n Activating the network ...");
            clientIP = "192.168.2.0";
            serverIP = "216.120.40.10";
            clientConnectionStatus = "idle";
            serverConnectionStatus = "idle";
            portID = 0;
            maxNbPackets = 10;
            inComingPacket = new Transactions[maxNbPackets];
            outGoingPacket = new Transactions[maxNbPackets];
            for (int i = 0; i < maxNbPackets; i++) {
                inComingPacket[i] = new Transactions();
                outGoingPacket[i] = new Transactions();
            }
            inBufferStatus = "empty";
            outBufferStatus = "empty";
            inputIndexClient = 0;
            inputIndexServer = 0;
            outputIndexServer = 0;
            outputIndexClient = 0;
            networkStatus = "active";
        } else
            System.out.println("\n Activating network components for " + context + "...");
    }

    public String getClientIP() {
        return clientIP;
    }

    public void setClientIP(String cip) {
        clientIP = cip;
    }

    public String getServerIP() {
        return serverIP;
    }

    public void setServerIP(String sip) {
        serverIP = sip;
    }

    public String getClientConnectionStatus() {
        return clientConnectionStatus;
    }

    public void setClientConnectionStatus(String connectStatus) {
        clientConnectionStatus = connectStatus;
    }

    public String getServerConnectionStatus() {
        return serverConnectionStatus;
    }

    public void setServerConnectionStatus(String connectStatus) {
        serverConnectionStatus = connectStatus;
    }

    public int getPortID() {
        return portID;
    }

    public void setPortID(int pid) {
        portID = pid;
    }

    public String getInBufferStatus() {
        return inBufferStatus;
    }

    public void setInBufferStatus(String inBufStatus) {
        inBufferStatus = inBufStatus;
    }

    public String getOutBufferStatus() {
        return outBufferStatus;
    }

    public void setOutBufferStatus(String outBufStatus) {
        outBufferStatus = outBufStatus;
    }

    public String getNetworkStatus() {
        return networkStatus;
    }

    public void setNetworkStatus(String netStatus) {
        networkStatus = netStatus;
    }

    public int getinputIndexClient() {
        return inputIndexClient;
    }

    public void setinputIndexClient(int i1) {
        inputIndexClient = i1;
    }

    public int getinputIndexServer() {
        return inputIndexServer;
    }

    public void setinputIndexServer(int i2) {
        inputIndexServer = i2;
    }

    public int getoutputIndexServer() {
        return outputIndexServer;
    }

    public void setoutputIndexServer(int o1) {
        outputIndexServer = o1;
    }

    public int getoutputIndexClient() {
        return outputIndexClient;
    }

    public void setoutputIndexClient(int o2) {
        outputIndexClient = o2;
    }

    public int getMaxNbPackets() {
        return maxNbPackets;
    }

    public void setMaxNbPackets(int maxPackets) {
        maxNbPackets = maxPackets;
    }

    public boolean send(Transactions inPacket) {
        inComingPacket[inputIndexClient].setAccountNumber(inPacket.getAccountNumber());
        inComingPacket[inputIndexClient].setOperationType(inPacket.getOperationType());
        inComingPacket[inputIndexClient].setTransactionAmount(inPacket.getTransactionAmount());
        inComingPacket[inputIndexClient].setTransactionBalance(inPacket.getTransactionBalance());
        inComingPacket[inputIndexClient].setTransactionError(inPacket.getTransactionError());
        inComingPacket[inputIndexClient].setTransactionStatus("transferred");

        setinputIndexClient(((getinputIndexClient() + 1) % getMaxNbPackets()));
        if (getinputIndexClient() == getoutputIndexServer()) {
            setInBufferStatus("full");

        } else
            setInBufferStatus("normal");

        return true;
    }

    public boolean receive(Transactions outPacket) {
        outPacket.setAccountNumber(outGoingPacket[outputIndexClient].getAccountNumber());
        outPacket.setOperationType(outGoingPacket[outputIndexClient].getOperationType());
        outPacket.setTransactionAmount(outGoingPacket[outputIndexClient].getTransactionAmount());
        outPacket.setTransactionBalance(outGoingPacket[outputIndexClient].getTransactionBalance());
        outPacket.setTransactionError(outGoingPacket[outputIndexClient].getTransactionError());
        outPacket.setTransactionStatus("done");

        setoutputIndexClient(((getoutputIndexClient() + 1) % getMaxNbPackets()));
        if (getoutputIndexClient() == getinputIndexServer()) {
            setOutBufferStatus("empty");

        } else
            setOutBufferStatus("normal");

        return true;
    }

    public boolean transferOut(Transactions outPacket) {
        outGoingPacket[inputIndexServer].setAccountNumber(outPacket.getAccountNumber());
        outGoingPacket[inputIndexServer].setOperationType(outPacket.getOperationType());
        outGoingPacket[inputIndexServer].setTransactionAmount(outPacket.getTransactionAmount());
        outGoingPacket[inputIndexServer].setTransactionBalance(outPacket.getTransactionBalance());
        outGoingPacket[inputIndexServer].setTransactionError(outPacket.getTransactionError());
        outGoingPacket[inputIndexServer].setTransactionStatus("transferred");

        setinputIndexServer(((getinputIndexServer() + 1) % getMaxNbPackets()));
        if (getinputIndexServer() == getoutputIndexClient()) {
            setOutBufferStatus("full");

        } else
            setOutBufferStatus("normal");

        return true;
    }

    public boolean transferIn(Transactions inPacket) {
        inPacket.setAccountNumber(inComingPacket[outputIndexServer].getAccountNumber());
        inPacket.setOperationType(inComingPacket[outputIndexServer].getOperationType());
        inPacket.setTransactionAmount(inComingPacket[outputIndexServer].getTransactionAmount());
        inPacket.setTransactionBalance(inComingPacket[outputIndexServer].getTransactionBalance());
        inPacket.setTransactionError(inComingPacket[outputIndexServer].getTransactionError());
        inPacket.setTransactionStatus("received");

        setoutputIndexServer(((getoutputIndexServer() + 1) % getMaxNbPackets()));
        if (getoutputIndexServer() == getinputIndexClient()) {
            setInBufferStatus("empty");

        } else
            setInBufferStatus("normal");

        return true;
    }

    public boolean connect(String IP) {
        if (getNetworkStatus().equals("active")) {
            if (getClientIP().equals(IP)) {
                setClientConnectionStatus("connected");
                setPortID(0);
            } else if (getServerIP().equals(IP)) {
                setServerConnectionStatus("connected");
            }
            return true;
        } else
            return false;
    }

    public boolean disconnect(String IP) {
        if (getNetworkStatus().equals("active")) {
            if (getClientIP().equals(IP)) {
                setClientConnectionStatus("disconnected");
            } else if (getServerIP().equals(IP)) {
                setServerConnectionStatus("disconnected");
            }
            return true;
        } else
            return false;
    }

    public String toString() {
        return ("\n Network status " + getNetworkStatus() + "Input buffer " + getInBufferStatus() + "Output buffer "
                + getOutBufferStatus());
    }

    public void run() {
        while (getClientConnectionStatus().equals("connected") || getServerConnectionStatus().equals("connected")) {
            Thread.yield();
        }
        System.out.println("\n Terminating network thread -  Client Disconnected Server disconnected");
    }
}
