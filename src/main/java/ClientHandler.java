import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler implements Runnable{
    private final Socket socket;
    private final DictionaryController controller;

    public ClientHandler(Socket socket, DictionaryController controller) {
        this.socket = socket;
        this.controller = controller;
    }

    @Override
    public void run() {
        try {
            PrintWriter out = new PrintWriter(socket.getOutputStream());
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String clientMessage = in.readLine();
            out.println(STR."Reply: \{clientMessage}");
            out.flush();
            socket.close();

            ClientRequest request =  parseClientMessage(clientMessage);
            ClientReply reply =  controller.requestHandler(request);

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private ClientRequest parseClientMessage(String clientMessage) throws Exception {
        String[] parts = clientMessage.split(":", 3);

        if (parts.length != 3) throw new Exception();


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
            case "QUERY" -> RequestType.SEARCH;
            case "ADD" -> RequestType.ADD;
            case "DELETE" -> RequestType.DELETE;
            case "UPDATE" -> RequestType.UPDATE;
            default -> throw new IllegalStateException(STR."Unexpected request type: \{requestString}. Must be either SEARCH, ADD, DELETE, UPDATE.");
        };
    }

}
