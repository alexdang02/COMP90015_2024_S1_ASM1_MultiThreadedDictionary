package DictionaryServer;

/**
 * ClientRequest.java
 * <p>
 * Author: Chi Trung Dang (Student ID: 109862)
 * <p>
 * COMP90015 - Sem 1 - 2024
 * <p>
 * Description: Serve as convenient data type for ClientRequest postprocessing.
 */

enum RequestType {
    SEARCH,
    ADD,
    UPDATE,
    DELETE,
    ERROR,
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

