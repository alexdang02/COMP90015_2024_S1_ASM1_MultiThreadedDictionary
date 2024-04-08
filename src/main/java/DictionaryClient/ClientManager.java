package DictionaryClient;

import java.io.IOException;
import java.security.spec.ECField;

public class ClientManager {

    public static void main(String[] args) {

        // validate command line args
        if (!validateArgs(args)) {
            System.exit(1);
        }

        String serverAddress = args[0];
        int serverPort = Integer.parseInt(args[1]);

        MainWindow mainWindow;
        try {
            Client client = createAndStartClient(serverAddress, serverPort);
            mainWindow = new MainWindow(client);
            mainWindow.frame.setVisible(true);
        } catch (IOException e) {
            System.out.println("ERROR Connecting to server.");
            System.exit(1);
        } catch (Exception e) {
            System.out.println("ERROR Initialising GUI");
        }
    }

    private static Client createAndStartClient(String serverAddress, int serverPort) throws IOException {
        Client client = new Client(
                serverAddress,
                serverPort);
        Thread t = new Thread(client);  // Use method reference for conciseness
        t.start();
        return client;
    }

    private static boolean validateArgs(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage:  java –jar DictionaryClient.jar <server-address> <server-port>");
            return false;
        }

        // Validate server address
        String serverAddress = args[0];
        if (serverAddress.isEmpty()) {
            System.out.println("Server Address must not be null");
            return false;
        }

        // Validate port
        int port;
        try {
            port = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            System.out.println("Port must be a valid integer.");
            return false;
        }

        if (port <= 1024 || port > 65535) {
            System.out.println("Port must be an integer > 1024 and <= 65535.");
            return false;
        }

        return true;
    }

}

