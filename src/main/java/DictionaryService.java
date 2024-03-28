import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

public class DictionaryService {
    private final ConcurrentHashMap<String,String> dictionary;

    public DictionaryService (String dictionary_filepath) {
        this.dictionary =loadDictionaryIntoMap(dictionary_filepath);
    }

    public void getDefinition(String word){
        dictionary.get(word);
    }

    public void addWord(String word, String definition){
        dictionary.put(word, definition);
    }

    public void updateWord(String word, String replacedDefinition){
         dictionary.put(word, replacedDefinition);
    }

    public void removeWord(String word){
        dictionary.remove(word);
    }

    private static ConcurrentHashMap<String, String> loadDictionaryIntoMap(String dictionaryFilePath) {
        ConcurrentHashMap<String, String> map = new ConcurrentHashMap<>();
        ObjectMapper mapper = new ObjectMapper();
        int nullEntryCount = 0;
        int totalEntryCount = 0;

        //
        try {
            JsonNode rootNode = mapper.readTree(new File(dictionaryFilePath));
            if (rootNode.isArray()) {
                totalEntryCount = rootNode.size();
                for (JsonNode arrayElement : rootNode) {

                    try {
                        // Extract the word and meaning from the current JSON object.
                        String word = arrayElement.get("word").asText();
                        String meaning = arrayElement.get("description").asText();

                        // Extract the word and meaning from the current JSON object.
                        if (word.isEmpty() || meaning.isEmpty()){
                            throw new NullPointerException();
                        }
                        map.put(word, meaning);
                        // System.out.println(STR."Entry\{word}-\{meaning}");
                    }catch (NullPointerException e){
                        nullEntryCount += 1;
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            System.out.println(STR."Invalid entry count: \{nullEntryCount}");
            System.out.println(STR."Valid entry count: \{totalEntryCount - nullEntryCount}");
        }
        return map;
    }
}
