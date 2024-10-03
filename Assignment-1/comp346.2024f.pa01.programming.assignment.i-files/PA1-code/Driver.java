public class Driver {
    public static void main(String[] args) {

        Network objNetwork = new Network("network");
        Server objServer = new Server();
        Client objClientReceiving = new Client("receiving");
        Client objClientSending = new Client("sending");
        objNetwork.start();
        objServer.start();
        objClientReceiving.start();
        objClientSending.start();
    }
}