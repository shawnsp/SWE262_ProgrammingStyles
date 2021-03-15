// Style #9 Kick Forward
// Each function takes an additional parameter, usually the last, which is another function
// That function parameter is applied at the end of the current function
// That function parameter is given as input what would be the output of the current function
// Larger problem is solved as a pipeline of functions, but where the next function to be applied is given as parameter to the current function

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;

public class Nine {
    // helper class
    private static class Term {
        private String title;
        private int count;

        private Term(String title) {
            this.title = title;
            this.count = 1;
        }
    }

    // interface provided for one method to call another
    interface FunctionCall {
        void call(Object arg, FunctionCall func);
    }

    // return an arraylist of two arraylist that each contains stopwords and words
    private static class ReadFile implements FunctionCall {
        public void call (Object arg, FunctionCall func) {
            System.out.println("Start reading files...");
            ArrayList<ArrayList<String>> twoFiles = new ArrayList<>();
            try {
                File targetFile = (File)arg;
                Scanner tf = new Scanner(targetFile);
                File stopWordsFile = new File("../stop_words.txt");
                Scanner sw = new Scanner(stopWordsFile);
                // store all stopwords in an arraylist
                ArrayList<String> stopWords = new ArrayList<>();
                String[] stopWordsArray = sw.nextLine().split(",");
                for (String s : stopWordsArray)
                    stopWords.add(s);

                // store all words in an arraylist
                ArrayList<String> words = new ArrayList<>();
                while (tf.hasNextLine()) {
                    String line = tf.nextLine();
                    String[] arrOfStr = line.split("[^A-Za-z0-9]+");
                    for (String s : arrOfStr) {
                        if (s != "")
                            words.add(s);
                    }

                }
                twoFiles.add(stopWords);
                twoFiles.add(words);

                tf.close();
                sw.close();
            } catch(
            FileNotFoundException e)

            {
                // TODO Auto-generated catch block
                System.out.println("File not found.");
                e.printStackTrace();
            }

            func.call(twoFiles, new SortFreq());
        }
    }

    // takes a list of two lists and return a filtered wordFreq list
    private static class FilterWord implements FunctionCall {
        public void call (Object arg, FunctionCall func) {
            System.out.println("Start filtering...");
            ArrayList<ArrayList<String>> twoFiles = (ArrayList<ArrayList<String>>) arg;
            ArrayList<Term> wordFreq = new ArrayList<>();
            for (String s : twoFiles.get(1)) {
                s = s.toLowerCase();
                if (s.length() < 2)
                    // jump to next iteration
                    continue;
                boolean added = false;
                for (Term t : wordFreq) {
                    if (t.title.equals(s)) {
                        t.count++;
                        added = true;
                        continue;
                    }
                }
                // check if the word is not a stopword, then it is wanted
                boolean wanted = true;
                for (String stopWord : twoFiles.get(0)) {
                    if (s.equals(stopWord)) {
                        wanted = false;
                    }
                }

                if (added == false && wanted == true)
                    wordFreq.add(new Term(s));
            }
            func.call(wordFreq, new Output());
        }
    }

    // takes a wordFreq list and return a sorted wordFreq list
    private static class SortFreq implements FunctionCall {
        public void call (Object arg, FunctionCall func) {
            System.out.println("Start sorting...");
            ArrayList<Term> wordFreq = (ArrayList<Term>) arg;
            wordFreq.sort(new Comparator<Term>() {
                @Override
                public int compare(Term t1, Term t2) {
                    // descending order (ascending order is the other way around)
                    return t2.count - t1.count;
                }
            });
            func.call(wordFreq, null);
        }
    }

    // output the 25 most frequent words
    private static class Output implements FunctionCall {
        public void call (Object arg, FunctionCall func) {
            System.out.println("Start writing output...25 most frequent terms...");
            ArrayList<Term> wordFreq = (ArrayList<Term>) arg;
            try {
                FileWriter myWriter = new FileWriter("./term_frequency9.txt");
                int nMost = 0;
                for (Term t : wordFreq) {
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
            return;
        }
    }

    public static void main(String[] args) {
//        File targetFile = new File(args[0]);
        File targetFile = new File("../pride-and-prejudice.txt");
        new ReadFile().call(targetFile, new FilterWord());
    }
}
