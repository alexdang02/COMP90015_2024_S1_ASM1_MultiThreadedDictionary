public class DictionaryController {
    private DictionaryService service;

    public enum RequestType {
        SEARCH,
        ADD,
        UPDATE,
        DELETE,
    }

    public DictionaryController(DictionaryService service){
        this.service = service;
    }

    public void requestHandler(ClientHandler.ClientRequestDAO requestDAO){
        String word = requestDAO.word;
        String definition = requestDAO.definition;

        switch (requestDAO.requestType){
            case SEARCH -> service.getDefinition(word);
            case ADD -> service.addWord(word, definition);
            case DELETE -> service.removeWord(word);
            case UPDATE -> service.updateWord(word, definition);
        }
    }
}
