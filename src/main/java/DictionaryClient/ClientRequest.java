package DictionaryClient;

enum RequestType {
    SEARCH,
    ADD,
    UPDATE,
    DELETE,
    ERROR,
}
public class ClientRequest {

    private final DictionaryClient.RequestType requestType;
    public String word;
    public String definition;

    RequestType getRequestType(){ return requestType; }

    ClientRequest(DictionaryClient.RequestType requestType, String word, String definition) {
        this.requestType = requestType;
        this.word = word;
        this.definition = definition;
    }

    @Override
    public String toString() {
        return STR."\{requestType.toString()}:\{word}:\{definition}";
    }
}
