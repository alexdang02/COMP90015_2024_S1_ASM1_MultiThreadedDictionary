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
       boolean[] optionalArgsAvailable = validateArgs(args);


        int listeningPort = Integer.parseInt(args[0]);
        String dictionaryFilePath = args[1];

        int minThreads = optionalArgsAvailable[0] ? Integer.parseInt(args[2]) : 5;
        int maxThreads = optionalArgsAvailable[1] ? Integer.parseInt(args[3]) : 10;
        int updatedInterval = optionalArgsAvailable[2] ? Integer.parseInt(args[4]) : 10000;
        int timeoutInterval = optionalArgsAvailable[3] ? Integer.parseInt(args[5]) : 5000;
        ThreadPool threadPool = new ThreadPool(minThreads, maxThreads, updatedInterval, timeoutInterval);

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
                System.out.println(STR."Client \{i} connected on Port \{clientSocket.getLocalPort()} at \{clientSocket.getInetAddress().getHostName()}");

                while (true) {
                    try {
                        threadPool.submitTask(() -> {
                            ClientHandler clientHandler = new ClientHandler(clientSocket, dictionaryController);
                            clientHandler.handleClient();
                        });
                        break; // Exit loop on successful submission
                    } catch (InterruptedException e) {
                        System.err.println("Thread interrupted while submitting task to thread pool. Retrying in 5 seconds.");
                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException e2) {
                            Thread.currentThread().interrupt();
                            break;
                        }
                    }
                }

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

    private static boolean[] validateArgs(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage: java -jar jar_file --enable-preview port dictionary_file [-minThreads] [-maxThreads] [-updatedInterval]");
            System.exit(1);
        }

        // Validate port
        int port;
        if (!parseArgument(args[0], "Port")){
            System.exit(1);
        }

        port = Integer.parseInt(args[0]);
        if (port <= 1024 || port > 65535) {
            System.out.println("Port must be an integer > 1024 and <= 65535.");
            System.exit(1);
        }

        // Validate dictionary file
        File file = new File(args[1]);
        if (!file.exists() || file.length() == 0) {
            System.out.println("Dictionary file must exist and not be empty.");
            System.exit(1);
        }

        boolean option1 = false;
        boolean option2 = false;
        boolean option3 = false;
        boolean option4 = false;

        for (int i = 2; i < args.length; i++) {
            switch (args[i]) {
                case "-minThreads":
                    if (parseArgument(args[2], "minThreads")) {
                        option1 = true;
                    }
                    break;
                case "-maxThreads":
                    if (parseArgument(args[3], "maxThreads")) {
                        option2 = true;
                    }
                    break;
                case "-updatedInterval":
                    if (parseArgument(args[4], "updatedInterval")) {
                        option3 = true;
                    }
                    break;
                case "-timeoutInterval":
                    if (parseArgument(args[5], "timeoutInterval")) {
                        option4 = true;
                    }
                    break;
                default:
                    System.out.println(STR."Unrecognized argument: \{args[i]}");
            }
        }

        return new boolean[]{option1, option2, option3, option4};
    }

    private static boolean parseArgument(String args, String argsOption) {
        try {
            Integer.parseInt(args);

        } catch (NumberFormatException e) {
            System.out.println(STR."\{argsOption} must be a valid integer.");
            return false;
        }
        return true;
    }
}
