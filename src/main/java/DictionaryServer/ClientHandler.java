package DictionaryServer;

import java.io.*;
import java.net.Socket;

/**
 * ClientHandler.java
 * <p>
 * Author: Chi Trung Dang (Student ID: 109862)
 * <p>
 * COMP90015 - Sem 1 - 2024
 * <p>
 * Description: ClientHandler receives raw string request from client, process request into ClientRequest,
 *              receive ClientReply from DictionaryService before sending back to client.
 */

public class ClientHandler{
    private final Socket clientSocket;
    private final DictionaryController controller;

    public ClientHandler(Socket clientSocket, DictionaryController controller) {
        this.clientSocket = clientSocket;
        this.controller = controller;
    }

    public void handleClient() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
            String clientMsg;

            while ((clientMsg = in.readLine()) != null) {

                ClientRequest request =  parseClientMessage(clientMsg);
                System.out.println(STR."REQUEST: \{clientMsg}");

                // Save last active time to ThreadWorker
                ((WorkerThread) Thread.currentThread()).setLastActiveTime(System.currentTimeMillis());

                ServerReply reply =  controller.requestHandler(request);
                String replyString = reply.toString();
                System.out.println(STR."REPLY: \{replyString}");
                out.write(replyString);
                out.flush();

            }
        } catch (IOException e) {
            System.out.println("Client disconnect.");
        }
    }

    private ClientRequest parseClientMessage(String clientMessage) {
        // NOTE: In case of Bad Request, word component in ClientRequest will be used as explanation returned to users
        String[] parts = clientMessage.split(":", 3);

        if (parts.length != 3) {
            return new ClientRequest(
                    RequestType.ERROR,
                    "Request must be of the form ACTION:Word:Definition",
                    null
            );
        }

        RequestType requestType = getRequestType(parts[0]);

        if (requestType == RequestType.ERROR) {
            return new ClientRequest(
                    RequestType.ERROR,
                    "ACTION must be one of SEARCH, ADD, DELETE, UPDATE.",
                    null
            );
        }

        if (parts[1].isEmpty()) {
            return new ClientRequest(RequestType.ERROR, "Word must not be null", null);
        }

        if ((requestType == RequestType.ADD || requestType == RequestType.UPDATE)
                && parts[2].isEmpty()) {
            return new ClientRequest(
                    RequestType.ERROR,
                    STR."To \{requestType}, definition must not be null",
                    null
            );
        }

        return new ClientRequest(requestType, parts[1], parts[2]);
    }

    private RequestType getRequestType(String requestString){
        return switch (requestString.toUpperCase()) {
            case "SEARCH" -> RequestType.SEARCH;
            case "ADD" -> RequestType.ADD;
            case "DELETE" -> RequestType.DELETE;
            case "UPDATE" -> RequestType.UPDATE;
            default -> RequestType.ERROR;
        };
    }

}
