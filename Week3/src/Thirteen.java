// Style #13 Closed Maps
// The larger problem is decomposed into 'things' that make sense for the problem domain
// Each 'thing' is a map from keys to values. Some values are procedures/functions.

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Scanner;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

// choose which lambda interface to use by its argument and return types
// https://stackoverflow.com/questions/29945627/java-8-lambda-void-argument/40153253#40153253
//  lambda interface     argument       return
//    Supplier              ()      ->       x
//    Consumer              x       ->      ()
//    Callable              ()      ->       x throws ex
//    Runnable              ()      ->      ()
//    Function              x       ->       y
//    BiFunction            x,y     ->       z
//    Predicate             x       ->      boolean
//    UnaryOperator         x1      ->      x2
//    BinaryOperator        x1,x2   ->      x3

public class Thirteen {
    // Helper class - Term data structure
    private static class Term {
        public String title;
        public int count;

        public Term(String title) {
            this.title = title;
            this.count = 1;
        }
    }

    // words object
    private HashMap<String, Object> wordsObj;
    // stopWords object
    private HashMap<String, Object> stopWordsObj;
    // wordFrequency object
    private HashMap<String, Object> wordFrequencyObj;

    Thirteen() {
        // construct wordsObj
        wordsObj = new HashMap<>();
        wordsObj.put("data", new ArrayList<String>());
        wordsObj.put("init", (Consumer<String>) targetFile -> readWords(targetFile));
        wordsObj.put("words", (Supplier<ArrayList<String>>) () -> (ArrayList<String>)wordsObj.get("data"));

        // construct stopWordsObj
        stopWordsObj = new HashMap<>();
        stopWordsObj.put("stopWords", new String[]{});
        stopWordsObj.put("init", (Runnable) () -> readStopWords());
        stopWordsObj.put("isStopWord", (Function<String, Boolean>) word -> isStopWord(word));

        // construct wordFrequencyObj
        wordFrequencyObj = new HashMap<>();
        wordFrequencyObj.put("freqs", new ArrayList<Term>());
        wordFrequencyObj.put("incrementCount", (Consumer<String>) word -> incrementCount(word));
        wordFrequencyObj.put("sorted", (Supplier<ArrayList<Term>>) () -> { sorted(); return (ArrayList<Term>)wordFrequencyObj.get("freqs"); });
        // 13.2
        wordFrequencyObj.put("top25", (Runnable) () -> top25());
    }

    // method readWords
    private void readWords(String targetFile) {
        try {
            Scanner tf = new Scanner(new File(targetFile));
            ArrayList<String> words = new ArrayList<>();
            while (tf.hasNextLine()) {
                String line = tf.nextLine();
                String[] arrOfStr = line.split("[^A-Za-z0-9]+");
                for (String s : arrOfStr)
                    if (s != "" && s.length() > 1)
                        words.add(s.toLowerCase());
            }
            tf.close();
            wordsObj.put("data", words);
        }
        catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            System.out.println("File not found.");
            e.printStackTrace();
        }
    }

    // method readStopWords
    private void readStopWords(){
        try {
            File stopWordsFile = new File("../stop_words.txt");
            Scanner sw = new Scanner(stopWordsFile);
            stopWordsObj.put("stopWords", sw.nextLine().split(","));
            sw.close();
        }
        catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            System.out.println("File not found.");
            e.printStackTrace();
        }
    }

    // method isStopWord
    private Boolean isStopWord(String word) {
        Boolean matched = false;
        for (String s : (String[]) stopWordsObj.get("stopWords"))
            if (word.equals(s))
                matched = true;
        return matched;
    }

    // method IncrementCount
    private void incrementCount(String word) {
        boolean added = false;
        for (Term t : (ArrayList<Term>) wordFrequencyObj.get("freqs")) {
            if (t.title.equals(word)) {
                t.count++;
                added = true;
                break;
            }
        }
        if (!added)
            ((ArrayList<Term>) wordFrequencyObj.get("freqs")).add(new Term(word));
    }

    // method sorted
    private void sorted(){
        ((ArrayList<Term>) wordFrequencyObj.get("freqs")).sort(new Comparator<Term>(){
            @Override
            public int compare(Term t1, Term t2) {
                // descending order (ascending order is the other way around)
                return t2.count-t1.count;
            }
        });
    }

    // 13.2
    private void top25(){
        try {
            FileWriter myWriter = new FileWriter("./term_frequency13.txt");
            int nMost = 0;
            for (Term t : (ArrayList<Term>)((Supplier)wordFrequencyObj.get("sorted")).get() ){
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


    public static void main(String[] args) {
//        String targetFile = args[0];
        String targetFile = "../pride-and-prejudice.txt";
        Thirteen thirteen = new Thirteen();
        ((Consumer)thirteen.wordsObj.get("init")).accept(targetFile);
        ((Runnable)thirteen.stopWordsObj.get("init")).run();

        for (String s : (ArrayList<String>)(((Supplier)thirteen.wordsObj.get("words")).get()) )
            if ( !(Boolean)((Function)thirteen.stopWordsObj.get("isStopWord")).apply(s) )
                ((Consumer)thirteen.wordFrequencyObj.get("incrementCount")).accept(s);


        // 13.1
//        ArrayList<Term> sortedWordFreqs = (ArrayList<Term>)((Supplier)thirteen.wordFrequencyObj.get("sorted")).get();
//        try {
//            FileWriter myWriter = new FileWriter("./term_frequency13.txt");
//            int nMost = 0;
//            for (Term t : sortedWordFreqs){
//                nMost++;
//                myWriter.write(t.title + "  -  " + t.count + "\n");
//                System.out.println(t.title + "  -  " + t.count);
//                if (nMost == 25)
//                    break;
//            }
//            myWriter.close();
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            System.out.println("Write to file failing.");
//            e.printStackTrace();
//        }


        // 13.2
        ((Runnable)thirteen.wordFrequencyObj.get("top25")).run();
    }
}



