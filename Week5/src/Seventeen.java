// A variation of Style #17 Reflection - apply reflection base on code from Style #11 Things
// More info in README.txt

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class Seventeen {
    public static void main(String[] args) throws Exception {
        // WordFrequencyController applies reflection to call different classes and methods.
//        new WordFrequencyController(args[0]).run();
        String targetFile = "../pride-and-prejudice.txt";
        new WordFrequencyController(targetFile).run();

        // use reflection to get each class information
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("Enter a class name to get its information:\n" +
                    "(WordFrequencyController, DataStorageManager, StopWordManager, WordFrequencyManager)\n" +
                    "Or enter X to quit");
            String userInput = sc.nextLine();
            if (userInput.equals("X"))
                break;
            try {
                getClassInfo(userInput);
            } catch (ClassNotFoundException e) {
                System.out.println("Class does not exist.");
            }
            System.out.print("\n");
        }
        sc.close();
    }

    public static void getClassInfo(String userInput) throws ClassNotFoundException {
        Class cls = null;
        cls = Class.forName(userInput);

        if(cls != null){
            Field[] fields = cls.getDeclaredFields();
            for (Field f : fields){
                System.out.println("Found Fields: " + f.getName() + " (type: " + f.getType() + ")");
            }

            Method[] methods = cls.getDeclaredMethods();
            for (Method m : methods){
                System.out.println("Found Methods: " + m.getName());
            }

            Class superClass = cls.getSuperclass();
            System.out.println("Found SuperClass: " + superClass.getName());

            Class[] interfaces = cls.getInterfaces();
            for(Class iface: interfaces){
                System.out.println("Found Interfaces: " + iface.getName());
            }
        }
    }
}


abstract class Things {
    public String getInfo() {
        return this.getClass().getName();
    }
}

class WordFrequencyController extends Things {
    private DataStorageManager storageManager;
    private StopWordManager stopWordManager;
    private WordFrequencyManager wordFreqManager;

    // change variable names
    private Class<DataStorageManager> class_storageManager;
    private Class<StopWordManager> class_stopWordManager;
    private Class<WordFrequencyManager> class_wordFreqManager;

    public WordFrequencyController(String pathToFile) throws Exception {
        class_storageManager = (Class<DataStorageManager>) Class.forName("DataStorageManager");
        class_stopWordManager = (Class<StopWordManager>) Class.forName("StopWordManager");
        class_wordFreqManager = (Class<WordFrequencyManager>) Class.forName("WordFrequencyManager");

        storageManager = class_storageManager.getDeclaredConstructor(String.class).newInstance(pathToFile);
        stopWordManager = class_stopWordManager.getDeclaredConstructor().newInstance();
        wordFreqManager = class_wordFreqManager.getDeclaredConstructor().newInstance();
    }

    public void run() throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        List <String> getWords = (List <String>) class_storageManager.getMethod("getWords").invoke(storageManager);
        Method stop_words = class_stopWordManager.getMethod("isStopWord", String.class);
        Method word_freq = class_wordFreqManager.getMethod("incrementCount", String.class);

        for (String word : getWords) {
            if (!(boolean)stop_words.invoke(stopWordManager, word)) {
                word_freq.invoke(wordFreqManager, word);
            }
        }

        List<WordFrequencyPair> word_freq_sorted = (List<WordFrequencyPair>)class_wordFreqManager.getMethod("sorted").invoke(wordFreqManager);

        FileWriter myWriter = new FileWriter("./term_frequency17.txt");
        int numWordsPrinted = 0;
        for (WordFrequencyPair pair : word_freq_sorted) {
            System.out.println(pair.getWord() + " - " + pair.getFrequency());
            myWriter.write(pair.getWord() + "  -  " + pair.getFrequency() + "\n");
            numWordsPrinted++;
            if (numWordsPrinted >= 25) {
                break;
            }
        }
        myWriter.close();
    }
}

/** Models the contents of the file. */
class DataStorageManager extends Things {
    private List<String> words;

    public DataStorageManager(String pathToFile) throws IOException {
        this.words = new ArrayList<String>();

        Scanner f = new Scanner(new File(pathToFile), "UTF-8");
        try {
            f.useDelimiter("[\\W_]+");
            while (f.hasNext()) {
                this.words.add(f.next().toLowerCase());
            }
        } finally {
            f.close();
        }
    }

    public List<String> getWords() {
        return this.words;
    }

    public String getInfo() {
        return super.getInfo() + ": My major data structure is a " + this.words.getClass().getName();
    }
}

/** Models the stop word filter. */
class StopWordManager extends Things {
    private Set<String> stopWords;

    public StopWordManager() throws IOException {
        this.stopWords = new HashSet<String>();

        Scanner f = new Scanner(new File("../stop_words.txt"), "UTF-8");
        try {
            f.useDelimiter(",");
            while (f.hasNext()) {
                this.stopWords.add(f.next());
            }
        } finally {
            f.close();
        }

        // Add single-letter words
        for (char c = 'a'; c <= 'z'; c++) {
            this.stopWords.add("" + c);
        }
    }

    public boolean isStopWord(String word) {
        return this.stopWords.contains(word);
    }

    public String getInfo() {
        return super.getInfo() + ": My major data structure is a " + this.stopWords.getClass().getName();
    }
}

/** Keeps the word frequency data. */
class WordFrequencyManager extends Things {
    private Map<String, MutableInteger> wordFreqs;

    public WordFrequencyManager() {
        this.wordFreqs = new HashMap<String, MutableInteger>();
    }

    public void incrementCount(String word) {
        MutableInteger count = this.wordFreqs.get(word);
        if (count == null) {
            this.wordFreqs.put(word, new MutableInteger(1));
        } else {
            count.setValue(count.getValue() + 1);
        }
    }

    public List<WordFrequencyPair> sorted() {
        List<WordFrequencyPair> pairs = new ArrayList<WordFrequencyPair>();
        for (Map.Entry<String, MutableInteger> entry : wordFreqs.entrySet()) {
            pairs.add(new WordFrequencyPair(entry.getKey(), entry.getValue().getValue()));
        }
        Collections.sort(pairs);
        Collections.reverse(pairs);
        return pairs;
    }

    public String getInfo() {
        return super.getInfo() + ": My major data structure is a " + this.wordFreqs.getClass().getName();
    }
}

class MutableInteger {
    private int value;

    public MutableInteger(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}

class WordFrequencyPair implements Comparable<WordFrequencyPair> {
    private String word;
    private int frequency;

    public WordFrequencyPair(String word, int frequency) {
        this.word = word;
        this.frequency = frequency;
    }

    public String getWord() {
        return word;
    }

    public int getFrequency() {
        return frequency;
    }

    public int compareTo(WordFrequencyPair other) {
        return this.frequency - other.frequency;
    }

}
