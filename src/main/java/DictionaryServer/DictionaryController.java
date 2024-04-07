package DictionaryServer;

/**
 * DictionaryController.java
 * <p>
 * Author: Chi Trung Dang (Student ID: 109862)
 * <p>
 * COMP90015 - Sem 1 - 2024
 * <p>
 * Description: Serve as middleware between client request and dictionary operation
 *              Abstracting away dictionary operation.
 */

public class DictionaryController {
    private final DictionaryService service;

    public DictionaryController(DictionaryService service) {
        this.service = service;
    }

    public ServerReply requestHandler(ClientRequest request) {
        String word = request.word;
        String definition = request.definition;
        String replyData;

        return switch (request.getRequestType()) {
            case SEARCH:
                replyData = service.getDefinition(word);
                yield new ServerReply(RequestType.SEARCH, replyData != null ? ReplyCode.FOUND : ReplyCode.NOT_FOUND, replyData);

            case ADD:
                replyData = service.addWord(word, definition);
                yield new ServerReply(RequestType.ADD, replyData != null ? ReplyCode.CONFLICT : ReplyCode.CREATED, replyData);

            case DELETE:
                replyData = service.removeWord(word);
                yield new ServerReply(RequestType.DELETE, replyData != null ? ReplyCode.OK : ReplyCode.NOT_FOUND, replyData);

            case UPDATE:
                replyData = service.updateWord(word, definition);
                yield new ServerReply(RequestType.UPDATE, replyData != null ? ReplyCode.OK : ReplyCode.NOT_FOUND, replyData);
            case ERROR:
                // NOTE: In case of Bad Request, word component in ClientRequest will be used as explanation returned to users
                // Since error message is stored in word, pass it on to user
                yield new ServerReply(RequestType.ERROR, ReplyCode.BAD_REQUEST, word);
        };


    }
}
