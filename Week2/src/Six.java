//Style #6 pipeline
//Larger problem decomposed in functional abstractions. Functions, according to Mathematics, are relations from inputs to outputs.
//Larger problem solved as a pipeline of function applications

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;

public class Six {
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
    public static ArrayList<ArrayList<String>> readFile(File targetFile, File stopWordsFile){
        System.out.println("Start reading files...");
        ArrayList<ArrayList<String>> twoFiles = new ArrayList<ArrayList<String>>();
        try {
            Scanner tf = new Scanner(targetFile);
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
    }

    // takes a list of two lists and return a filtered wordFreq list
    public static ArrayList<Term> filterWord(ArrayList<ArrayList<String>> twoFiles) {
        System.out.println("Start filtering...");
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
    }

    // takes a wordFreq list and return a sorted wordFreq list
    public static ArrayList<Term> sortWordFreq(ArrayList<Term> wordFreq) {
        System.out.println("Start sorting...");
        wordFreq.sort(new Comparator<Term>(){
            @Override
            public int compare(Term t1, Term t2) {
                // descending order (ascending order is the other way around)
                return t2.count-t1.count;
            }
        });
        return wordFreq;
    }

    // output the 25 most frequent words
    public static void ouput(ArrayList<Term> wordFreq) {
        System.out.println("Start writing output...25 most frequent terms...");
        try {
            FileWriter myWriter = new FileWriter("./term_frequency6.txt");
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
        ouput(sortWordFreq(filterWord(readFile(targetFile, stopWordsFile))));

    }
}
