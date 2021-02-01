//Style #12 LetterBox
//The larger problem is decomposed into 'things' that make sense for the problem domain
//Each 'thing' is a capsule of data that exposes one single procedure, namely the ability to receive and dispatch messages that are sent to it
//Message dispatch can result in sending the message to another capsule

// only expose one method each class to outside, this method receive message from outside and process it
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;

public class Twelve {
    public static void main(String[] args) {
//        String targetFile = args[0];
        WordFrequencyController wfc = new WordFrequencyController();
        wfc.dispatch(new String[]{"init", "../pride-and-prejudice.txt"});
        wfc.dispatch(new String[]{"run"});

//        wfc.dispatch(new String[]{"init", targetFile});
//        wfc.dispatch(new String[]{"run"});
    }
}


// WordFrequencyController can initialize all needed objects and run the program
class WordFrequencyController {
    private DataManager dm;
    private StopWordsManager swm;
    private WordFrequencyManager wfm;

    public void dispatch(String[] message) {
        if (message[0].equals("init"))
            init(message[1]);
        else if (message[0].equals("run"))
            run();
        else
            System.out.println("wrong message!");
    }

    private void init(String targetFile) {
        dm = new DataManager();
        swm = new StopWordsManager();
        wfm = new WordFrequencyManager();
        dm.dispatch(new String[]{"init", targetFile});
        swm.dispatch(new String[]{"init"});
    }

    private void run(){
        for (String s : dm.dispatch(new String[]{"words"}))
            if (s != "" && s.length() > 1)
                if (swm.dispatch(new String[]{"isStopWord", s})[0].equals("false"))
                    wfm.dispatch(new String[]{"incrementCount", s});

        wfm.dispatch(new String[]{"sorted"});
        wfm.dispatch(new String[]{"outputTop25"});
    }
}

// DataManager creates object that reads targetFile and store words
class DataManager {
    private ArrayList<String> words = new ArrayList<>();

    public ArrayList<String> dispatch(String[] message) {
        ArrayList<String> data = new ArrayList<>();
        if (message[0].equals("init"))
            init(message[1]);
        else if (message[0].equals("words"))
            data = words;
        else
            System.out.println("wrong message!");
        return data;
    }

    private void init(String targetFile) {
        try {
            System.out.println("[DataManager]   init");
            Scanner tf = new Scanner(new File(targetFile));
            while (tf.hasNextLine()) {
                String line = tf.nextLine();
                String[] arrOfStr = line.split("[^A-Za-z0-9]+");
                for (String s : arrOfStr)
                    words.add(s.toLowerCase());
            }
            tf.close();
        }
        catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            System.out.println("File not found.");
            e.printStackTrace();
        }
    }
}

// StopWordsManager creates object that reads stopWordsFile and store stopWords
class StopWordsManager {
    private String[] stopWords;

    public String[] dispatch(String[] message) {
        String[] data = new String[]{};
        if (message[0].equals("init"))
            init();
        else if (message[0].equals("isStopWord"))
            data = new String[]{isStopWord(message[1])};
        else
            System.out.println("wrong message!");
        return data;
    }

    private void init() {
        try {
            System.out.println("[StopWordsManager]   init");
            File stopWordsFile = new File("../stop_words.txt");
            Scanner sw = new Scanner(stopWordsFile);
            stopWords = sw.nextLine().split(",");
            sw.close();
        }
        catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            System.out.println("File not found.");
            e.printStackTrace();
        }
    }

    private String isStopWord(String word) {
//        System.out.println("[StopWordsManager]   isStopWord");
        String matched = "false";
        for (String s : stopWords)
            if (word.equals(s))
                matched = "true";
        return matched;
    }
}

// WordFrequencyManager creates object that counts word frequency and sort for top 25
class WordFrequencyManager {
    // Helper class - Term data structure
    private static class Term {
        public String title;
        public int count;

        public Term(String title){
            this.title = title;
            this.count = 1;
        }
    }

    private ArrayList<Term> wordFreq;
    WordFrequencyManager(){ System.out.println("[WordFrequencyManager]   init"); wordFreq = new ArrayList<>();}

    public ArrayList<Term> dispatch(String[] message) {
        ArrayList<Term> data = new ArrayList<>();
        if (message[0].equals("incrementCount"))
            incrementCount(message[1]);
        else if (message[0].equals("sorted")) {
            sorted();
            data = wordFreq;
        } else if (message[0].equals("outputTop25"))
            outputTop25();
        else
            System.out.println("wrong message!");
        return data;
    }

    private void incrementCount(String word) {
//        System.out.println("[WordFrequencyManager]   incrementCount");
        boolean added = false;
        for (WordFrequencyManager.Term t : wordFreq) {
            if (t.title.equals(word)) {
                t.count++;
                added = true;
                break;
            }
        }
        if (!added)
            wordFreq.add(new WordFrequencyManager.Term(word));
    }

    private void sorted(){
        System.out.println("[WordFrequencyManager]   sorted");
        wordFreq.sort(new Comparator<Term>(){
            @Override
            public int compare(Term t1, Term t2) {
                // descending order (ascending order is the other way around)
                return t2.count-t1.count;
            }
        });
    }

    private void outputTop25(){
        try {
            System.out.println("[WordFrequencyManager]   outputTop25");
            FileWriter myWriter = new FileWriter("./term_frequency12.txt");
            int nMost = 0;
            for (WordFrequencyManager.Term t : wordFreq){
                nMost++;
                myWriter.write(t.title + "  -  " + t.count + "\n");
                System.out.println(t.title + "  -  " + t.count);
                if (nMost == 25)
                    break;
            }
            myWriter.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            System.out.println("Write to file failing.");
            e.printStackTrace();
        }
    }
}