enum RequestType {
    SEARCH,
    ADD,
    UPDATE,
    DELETE,
}
public class ClientRequest {

    private final RequestType requestType;
    public String word;
    public String definition;

    RequestType getRequestType(){ return requestType; }

    ClientRequest(RequestType requestType, String word, String definition) {
        this.requestType = requestType;
        this.word = word;
        this.definition = definition;
    }
}
