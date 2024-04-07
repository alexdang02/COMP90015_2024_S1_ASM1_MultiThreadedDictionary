package DictionaryServer;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Server.java
 * <p>
 * Author: Chi Trung Dang (Student ID: 109862)
 * <p>
 * COMP90015 - Sem 1 - 2024
 * <p>
 * Description: Server Application.
 *              Initialising TCP communication for server-side
 *              Validating commandline arguments.
 */

public class Server {

    public static void main(String[] args) {

        // validate use inputs
        if (!validateArgs(args)) {
            System.exit(1);
        }

        int listeningPort = Integer.parseInt(args[0]);
        String dictionaryFilePath = args[1];

        // load dictionary
        DictionaryService dictionaryService = new DictionaryService(dictionaryFilePath);
        DictionaryController dictionaryController = new DictionaryController(dictionaryService);

        // setting up server
        ServerSocket serverSocket = null;

        try {
            serverSocket = new ServerSocket(listeningPort);
            System.out.println(STR."Dictionary Server is listening on port \{listeningPort}");
            int i = 0;

            while (true) {

                //Accept an incoming client connection request
                Socket clientSocket = serverSocket.accept();
                i++;
                System.out.println(STR."Client conection number \{i} accepted:");
                System.out.println(STR."Remote Hostname: \{clientSocket.getInetAddress().getHostName()}");
                System.out.println(STR."Local Port: \{clientSocket.getLocalPort()}");

                Thread t = new Thread(() -> new ClientHandler(clientSocket, dictionaryController).run());
                t.start();
            }
        } catch (IOException e) {
                System.out.println("An I/O error occurs when opening the socket or accepting client connection");
                System.exit(1);
        } finally {
            if (serverSocket != null) {
                try {
                    // close the server socket
                    serverSocket.close();}
                    catch (IOException e) {
                        System.out.println("Error occurs when closing the socket");
                        System.exit(1);
                    }
                }
            }
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
