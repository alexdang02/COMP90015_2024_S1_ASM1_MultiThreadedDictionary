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

    public ClientReply requestHandler(ClientRequest request) {
        String word = request.word;
        String definition = request.definition;
        String replyData;

        return switch (request.getRequestType()) {
            case SEARCH:
                replyData = service.getDefinition(word);
                yield new ClientReply(replyData != null ? ReplyCode.FOUND : ReplyCode.NOT_FOUND, replyData);

            case ADD:
                replyData = service.addWord(word, definition);
                yield new ClientReply(replyData == null ? ReplyCode.CREATED : ReplyCode.CONFLICT, replyData);

            case DELETE:
                replyData = service.removeWord(word);
                yield new ClientReply(replyData != null ? ReplyCode.OK : ReplyCode.NOT_FOUND, replyData);

            case UPDATE:
                replyData = service.updateWord(word, definition);
                yield new ClientReply(replyData != null ? ReplyCode.OK : ReplyCode.NOT_FOUND, replyData);
        };


    }
}
