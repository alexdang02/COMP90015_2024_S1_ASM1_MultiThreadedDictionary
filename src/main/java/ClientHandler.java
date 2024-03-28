import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.BreakIterator;

public class ClientHandler implements Runnable{
    private final Socket socket;
    private final DictionaryController controller;

    public static class ClientRequestDAO {
        DictionaryController.RequestType requestType;
        String word;
        String definition;

        ClientRequestDAO(DictionaryController.RequestType requestType, String word, String definition){
            this.requestType = requestType;
            this.word = word;
            this.definition = definition;
        }
    }

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

            ClientRequestDAO requestDAO =  parseClientMessage(clientMessage);
            controller.requestHandler(requestDAO);

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private ClientRequestDAO parseClientMessage(String clientMessage) throws Exception {
        String[] parts = clientMessage.split(":", 3);

        if (parts.length != 3) throw new Exception();


        DictionaryController.RequestType requestType = getRequestType(parts[0]);

        if (parts[1].isEmpty()) {
            throw new NullPointerException("Word must not be null");
        }
        if ((requestType == DictionaryController.RequestType.ADD ||
                requestType == DictionaryController.RequestType.UPDATE) &&
                parts[2].isEmpty()){
            throw new NullPointerException(STR."To \{requestType}, definiiton must not be null");
        }

        return new ClientRequestDAO(requestType, parts[1], parts[2]);
    }

    private DictionaryController.RequestType getRequestType(String requestString){
        return switch (requestString.toUpperCase()) {
            case "QUERY" -> DictionaryController.RequestType.SEARCH;
            case "ADD" -> DictionaryController.RequestType.ADD;
            case "DELETE" -> DictionaryController.RequestType.DELETE;
            case "UPDATE" -> DictionaryController.RequestType.UPDATE;
            default -> throw new IllegalStateException(STR."Unexpected request type: \{requestString}. Must be either SEARCH, ADD, DELETE, UPDATE.");
        };
    }

}
