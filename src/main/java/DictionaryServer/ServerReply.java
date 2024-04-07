package DictionaryServer;
/**
 * ClientReply.java
 * <p>
 * Author: Chi Trung Dang (Student ID: 109862)
 * <p>
 * COMP90015 - Sem 1 - 2024
 * <p>
 * Description: Serve as convenient data type for ClientReply.
 */

enum ReplyCode {
    OK(200),
    CREATED(201),
    FOUND(302),
    BAD_REQUEST(400),
    NOT_FOUND(404),
    CONFLICT(409);

    private final int statusCode;

    ReplyCode(int statusCode){
        this.statusCode = statusCode;
    }
    public int getStatusCode() {
        return statusCode;
    }
}

public class ServerReply {
    private final ReplyCode replyCode;
    private final RequestType requestType;
    private final String replyData;

    public ServerReply(RequestType requestType, ReplyCode replyCode, String replyData){
        this.requestType = requestType;
        this.replyCode = replyCode;
        this.replyData = replyData;
    }

    @Override
    public String toString() {
        return STR."\{requestType.toString()}:\{replyCode.getStatusCode()}:\{replyData}\n";
    }
}
