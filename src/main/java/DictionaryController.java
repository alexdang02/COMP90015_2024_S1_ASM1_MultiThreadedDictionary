public class DictionaryController {
    private DictionaryService service;

    public enum RequestType {
        SEARCH,
        ADD,
        UPDATE,
        DELETE,
    }

    public void setService(DictionaryService service) {
        this.service = service;
    }

    public void requestHandler(RequestType requestType, String word, String definition){
        switch (requestType){
            case SEARCH -> service.getDefinition(word);
            case ADD -> service.addWord(word, definition);
            case DELETE -> service.removeWord(word);
            case UPDATE -> service.updateWord(word, definition);
        }
    }
}
