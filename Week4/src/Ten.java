// Style #10 The One
// Existence of an abstraction to which values can be converted.
// This abstraction provides operations to (1) wrap around values, so that they become the abstraction; (2) bind itself to functions, so to establish sequences of functions; and (3) unwrap the value, so to examine the final result.
// Larger problem is solved as a pipeline of functions bound together, with unwrapping happening at the end.
// Particularly for The One style, the bind operation simply calls the given function, giving it the value that it holds, and holds on to the returned value.

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;
import java.util.function.Function;

public class Ten {
    private static class TFTheOne{
        private Object value;

        TFTheOne(Object v){
            this.value = v;
        }

        public TFTheOne bind(Function<Object, Object> func){
            this.value = func.apply(this.value);
            return this;
        }

        public void printme(){
            System.out.println(this.value);
        }
    }

    // helper class
    static class Term {
        public String title;
        public int count;

        public Term(String title){
            this.title = title;
            this.count = 1;
        }
    }

    // return an arraylist of two arraylist that each contains stopwords and words
    private static final Function<Object, Object> readFile = (arg) -> {
        System.out.println("Start reading files...");
        ArrayList<ArrayList<String>> twoFiles = new ArrayList<ArrayList<String>>();
        try {
            File targetFile = (File) arg;
            Scanner tf = new Scanner(targetFile);
            File stopWordsFile = new File("../stop_words.txt");
            Scanner sw = new Scanner(stopWordsFile);
            // store all stopwords in an arraylist
            ArrayList<String> stopWords = new ArrayList<>();
            String[] stopWordsArray = sw.nextLine().split(",");
            for (String s: stopWordsArray)
                stopWords.add(s);

            // store all words in an arraylist
            ArrayList<String> words = new ArrayList<>();
            while (tf.hasNextLine()) {
                String line = tf.nextLine();
                String[] arrOfStr= line.split("[^A-Za-z0-9]+");
                for (String s: arrOfStr){
                    if (s != "")
                        words.add(s);
                }

            }
            twoFiles.add(stopWords);
            twoFiles.add(words);

            tf.close();
            sw.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            System.out.println("File not found.");
            e.printStackTrace();
        }
        return twoFiles;
    };

    // takes a list of two lists and return a filtered wordFreq list
    private static final Function<Object, Object> filterWord = (arg) -> {
        System.out.println("Start filtering...");
        ArrayList<ArrayList<String>> twoFiles = (ArrayList<ArrayList<String>>) arg;
        ArrayList<Term> wordFreq = new ArrayList<>();
        for (String s: twoFiles.get(1)) {
            s = s.toLowerCase();
            if (s.length() < 2)
                // jump to next iteration
                continue;
            boolean added = false;
            for (Term t: wordFreq){
                if (t.title.equals(s)){
                    t.count++;
                    added = true;
                    continue;
                }
            }
            // check if the word is not a stopword, then it is wanted
            boolean wanted = true;
            for (String stopWord: twoFiles.get(0)) {
                if (s.equals(stopWord)) {
                    wanted = false;
                }
            }

            if (added == false && wanted == true)
                wordFreq.add(new Term(s));
        }
        return wordFreq;
    };

    // takes a wordFreq list and return a sorted wordFreq list
    private static final Function<Object, Object> sortFreq = (arg) -> {
        System.out.println("Start sorting...");
        ArrayList<Term> wordFreq = (ArrayList<Term>) arg;
        wordFreq.sort(new Comparator<Term>(){
            @Override
            public int compare(Term t1, Term t2) {
                // descending order (ascending order is the other way around)
                return t2.count-t1.count;
            }
        });
        return wordFreq;
    };

    // output the 25 most frequent words
    private static final Function<Object, Object> output = (arg) -> {
        System.out.println("Start writing output...25 most frequent terms...");
        ArrayList<Term> wordFreq = (ArrayList<Term>) arg;
        try {
            FileWriter myWriter = new FileWriter("./term_frequency10.txt");
            int nMost = 0;
            for (Term t : wordFreq){
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
        return "program ended";
    };

    public static void main(String[] args) {
//        File targetFile = new File(args[0]);
        File targetFile = new File("../pride-and-prejudice.txt");

        TFTheOne theOne = new TFTheOne(targetFile).bind(readFile)
                .bind(filterWord).bind(sortFreq).bind(output);
        theOne.printme();
    }
}
