import java.io.File;


public class Server {
    public static void main(String[] args) {

        // validate use inputs
        if (!validateArgs(args)) {
            System.exit(1);
        }

        int port = Integer.parseInt(args[0]);
        String dictionaryFile = args[1];

        System.out.println(port);
        System.out.println(dictionaryFile);
    }

    private static boolean validateArgs(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: java –jar DictionaryServer.jar <port> <dictionary-file>");
            return false;
        }

        // Validate port
        int port;
        try {
            port = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            System.out.println("Port must be a valid integer.");
            return false;
        }

        if (port <= 1024 || port > 65535) {
            System.out.println("Port must be an integer > 1024 and <= 65535.");
            return false;
        }

        // Validate dictionary file
        File file = new File(args[1]);
        if (!file.exists() || file.length() == 0) {
            System.out.println("Dictionary file must exist and not be empty.");
            return false;
        }

        return true;
    }
}
