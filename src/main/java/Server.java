import java.io.File;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import java.lang.NullPointerException;


public class Server {

    public static void main(String[] args) {

        // validate use inputs
        if (!validateArgs(args)) {
            System.exit(1);
        }

        int port = Integer.parseInt(args[0]);
        String dictionaryFile = args[1];

        System.out.println(port);
        System.out.println(dictionaryFile);

        // load sample dictionary data into ConcurrentHashMap
        ConcurrentHashMap<String, String> map = loadDictionaryIntoMap(dictionaryFile);
    }

    private static boolean validateArgs(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: java –jar DictionaryServer.jar <port> <dictionary-file>");
            return false;
        }

        // Validate port
        int port;
        try {
            port = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            System.out.println("Port must be a valid integer.");
            return false;
        }

        if (port <= 1024 || port > 65535) {
            System.out.println("Port must be an integer > 1024 and <= 65535.");
            return false;
        }

        // Validate dictionary file
        File file = new File(args[1]);
        if (!file.exists() || file.length() == 0) {
            System.out.println("Dictionary file must exist and not be empty.");
            return false;
        }

        return true;
    }

    private static ConcurrentHashMap<String, String> loadDictionaryIntoMap(String fileName) {
        ConcurrentHashMap<String, String> map = new ConcurrentHashMap<>();
        ObjectMapper mapper = new ObjectMapper();
        int nullEntryCount = 0;
        int totalEntryCount = 0;

        //
        try {
            JsonNode rootNode = mapper.readTree(new File(fileName));
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
