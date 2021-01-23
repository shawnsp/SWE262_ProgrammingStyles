//Style #5 Procedural
//Larger problem decomposed in procedural abstractions
//Larger problem solved as a sequence of commands, each corresponding to a procedure
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;

public class Five {
    static class Term {
        public String title;
        public int count;

        public Term(String title){
            this.title = title;
            this.count = 1;
        }
    }

    public static String[] stopWordsArray;
    public static ArrayList<String> words = new ArrayList<>();
    public static ArrayList<Term> wordFreq = new ArrayList<>();

    // read words in each of the two files in an array
    public static void readFile(File targetFile, File stopWordsFile){
        System.out.println("Start reading files...");
        try {
            Scanner tf = new Scanner(targetFile);
            Scanner sw = new Scanner(stopWordsFile);
            stopWordsArray = sw.nextLine().split(",");
            while (tf.hasNextLine()) {
                String line = tf.nextLine();
                String[] arrOfStr= line.split("[^A-Za-z0-9]+");
                for (String s: arrOfStr)
                    words.add(s);
            }
            tf.close();
            sw.close();

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            System.out.println("File not found.");
            e.printStackTrace();
        }
//        catch (IOException e) {
//            // TODO Auto-generated catch block
//            System.out.println("Write to file failing.");
//            e.printStackTrace();
//        }
    }

    // filter the words
    public static void filterWord() {
        System.out.println("Start filtering...");
        for (String s: words) {
            s = s.toLowerCase();
            if (s == "" || s.length() < 2)
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
            for (String stopWord: stopWordsArray) {
                if (s.equals(stopWord)) {
                    wanted = false;
                }
            }

            if (added == false && wanted == true)
                wordFreq.add(new Term(s));
        }
    }

    // sort the wordFreq
    public static void sortWordFreq() {
        System.out.println("Start sorting...");
        wordFreq.sort(new Comparator<Term>(){
            @Override
            public int compare(Term t1, Term t2) {
                // descending order (ascending order is the other way around)
                return t2.count-t1.count;
            }
        });
    }

    // output the 25 most frequent words
    public static void ouput() {
        System.out.println("Start writing output...25 most frequent terms...");
        try {
            FileWriter myWriter = new FileWriter("./term_frequency5.txt");
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
    }

    public static void main(String[] args) {
//        File targetFile = new File(args[0]);
        File targetFile = new File("../pride-and-prejudice.txt");
        File stopWordsFile = new File("../stop_words.txt");
        readFile(targetFile, stopWordsFile);
        filterWord();
        sortWordFreq();
        ouput();
    }
}
