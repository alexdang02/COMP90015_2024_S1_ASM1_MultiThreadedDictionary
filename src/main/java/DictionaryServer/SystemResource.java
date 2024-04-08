package DictionaryServer;

public class SystemResource {

    public long freeMemory;
    public long totalMemory;
    public long usedMemory;
    public int availableCores;

    public SystemResource (){
        Runtime runtime = Runtime.getRuntime();
        freeMemory = runtime.freeMemory();
        totalMemory = runtime.totalMemory();
        usedMemory = totalMemory - freeMemory;
        availableCores = runtime.availableProcessors();
    }

    @Override
    public String toString() {
        return STR."SystemResource{freeMemory=\{freeMemory}, totalMemory=\{totalMemory}, usedMemory=\{usedMemory}, availableCores=\{availableCores}\{'}'}";
    }

    public SystemResource updateSystemResource() {
        return new SystemResource();
    }
}
