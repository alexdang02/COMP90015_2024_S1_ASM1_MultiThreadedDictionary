package DictionaryServer;

import java.io.*;
import java.lang.classfile.attribute.SyntheticAttribute;
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

public class ClientHandler implements Runnable{
    private final Socket clientSocket;
    private final DictionaryController controller;

    public ClientHandler(Socket clientSocket, DictionaryController controller) {
        this.clientSocket = clientSocket;
        this.controller = controller;
    }

    @Override
    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
            String clientMsg;

            while ((clientMsg = in.readLine()) != null) {
                System.out.println(STR."Message from client: \{clientMsg}");
                out.write(STR."Server Ack \{clientMsg}\n");

                ClientRequest request =  parseClientMessage(clientMsg);
                ClientReply reply =  controller.requestHandler(request);
                String replyString = reply.toString();
                out.write(replyString);
                out.flush();
                clientSocket.close();

            }
            System.out.println("Server closed the client connection!!!!! - received null");


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private ClientRequest parseClientMessage(String clientMessage) {
        String[] parts = clientMessage.split(":", 3);

        if (parts.length != 3) {
            System.out.println("Wrong request format");
            System.exit(1);
        }

        RequestType requestType = getRequestType(parts[0]);

        if (parts[1].isEmpty()) {
            throw new NullPointerException("Word must not be null");
        }
        if ((requestType == RequestType.ADD ||
                requestType == RequestType.UPDATE) &&
                parts[2].isEmpty()){
            throw new NullPointerException(STR."To \{requestType}, definiiton must not be null");
        }

        return new ClientRequest(requestType, parts[1], parts[2]);
    }

    private RequestType getRequestType(String requestString){
        return switch (requestString.toUpperCase()) {
            case "SEARCH" -> RequestType.SEARCH;
            case "ADD" -> RequestType.ADD;
            case "DELETE" -> RequestType.DELETE;
            case "UPDATE" -> RequestType.UPDATE;
            default -> throw new IllegalStateException(STR."Unexpected request type: \{requestString}. Must be either SEARCH, ADD, DELETE, UPDATE.");
        };
    }

}
