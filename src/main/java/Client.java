import java.io.File;

public class Client {

    public static void main(String[] args) {

        // validate use inputs
        if (!validateArgs(args)) {
            System.exit(1);
        }

        String serverAddress = args[0];
        int serverPort = Integer.parseInt(args[1]);

        System.out.println(serverAddress);
        System.out.println(serverPort);
    }

    private static boolean validateArgs(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: java –jar DictionaryClient.jar <server-address> <server-port>");
            return false;
        }

        // Validate address
        String serverAddress = args[0];
        if (serverAddress == null || serverAddress.isEmpty()) {
            System.out.println("Error: Server address cannot be null or empty.");
            System.exit(1);
        }

        // Validate port
        int serverPort;
        try {
            serverPort = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            System.out.println("server-port must be a valid integer.");
            return false;
        }

        if (serverPort <= 1024 || serverPort > 65535) {
            System.out.println("server-port must be an integer > 1024 and <= 65535.");
            return false;
        }

        return true;
    }
}
