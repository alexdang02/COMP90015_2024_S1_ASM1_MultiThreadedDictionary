package DictionaryClient;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class Client implements Runnable {
    private final Socket socket;
    private final BufferedWriter out;
    private final BufferedReader in;

    private ClientResponseListener listener;
    public Client(String serverAddress, int port) throws IOException {
        socket = new Socket(serverAddress, port);
        System.out.println(STR."Connection established to \{serverAddress} on Port \{port}.");
        in = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));
    }

    public void setListener(ClientResponseListener listener) {
        this.listener = listener;
    }

    @Override
    public void run() {
        System.out.println("LISTENING on separate thread");
        try {
            String serverReply;
            while ((serverReply = in.readLine()) != null) {
                System.out.println(STR."PREPROCESSED: \{serverReply}");
                listener.onClientResponseReceived(new ServerReply(serverReply));
            }
        } catch (IOException e) {
            // Handle any errors
            listener.onError(e);
        } finally {
            try {
                close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void close() throws IOException {
        socket.close();
        in.close();
        out.close();
    }

    public synchronized void sendData(ClientRequest request) {
        try {
            System.out.println(STR."REQUEST SENT: \{request.toString()}");
            out.write(request.toString());
            out.newLine();
            out.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



}
