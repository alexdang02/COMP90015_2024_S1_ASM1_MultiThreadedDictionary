package DictionaryClient;

public interface ClientResponseListener {
    void onClientResponseReceived(ServerReply response);

    void onError(Exception e);
}