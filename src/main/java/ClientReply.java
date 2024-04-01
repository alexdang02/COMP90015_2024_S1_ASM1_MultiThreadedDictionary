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

public class ClientReply {
    private final ReplyCode replyCode;
    private final String replyData;

    public ClientReply(ReplyCode replyCode, String replyData){
        this.replyCode = replyCode;
        this.replyData = replyData;
    }

}
