//Style #4 Monolith
//No abstractions
//No use of library functions
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;

public class Four {
    static class Term {
        public String title;
        public int count;

        public Term(String title){
            this.title = title;
            this.count = 1;
        }
    }
    // check if character is alphanumeric
    public static boolean isAlphaNumeric(char c){
        if (c >= 'a' && c <= 'z')
            return true;
        else if (c >= 'A' && c <= 'Z')
            return true;
        else if (c >= '0' && c <= '9')
            return true;
        else
            return false;
    }

    // check if currWord is already in wordFreq, if so increase its count
    public static boolean inWordFreqList(ArrayList<Term> wordFreq, String currWord) {
        boolean added = false;
        for (Term t: wordFreq) {
            if (t.title.equals(currWord)) {
                t.count++;
                added = true;
                continue;
            }
        }
        return added;
    }

    // check if currWord is a stopword
    public static boolean isStopword(String[] stopWordsArray, String currWord) {
        boolean wanted = true;
        for (String stopWord: stopWordsArray) {
            if (currWord.equals(stopWord)) {
                wanted = false;
            }
        }
        return wanted;
    }

    // write the 25 most frequent words to a file
    public static void readFile(File targetFile, File stopWordsFile){
        try {
            Scanner tf = new Scanner(targetFile);
            Scanner sw = new Scanner(stopWordsFile);
            FileWriter myWriter = new FileWriter("./term_frequency4.txt");

            String[] stopWordsArray = sw.nextLine().split(",");
            ArrayList<Term> wordFreq = new ArrayList<>();
            System.out.println("Start filtering...");
            while (tf.hasNextLine()) {
                String line = tf.nextLine();
                int startIndex = -1;
                int endIndex = 0;
                for (int i =0 ; i< line.length(); i++) {
                    // check if we are currently find a word
                    // startIndex == -1 shows we are not currently finding a word
                    // otherwise, it represents the start index of the word
                    if (startIndex == -1) {
                        // check if character is alphanumeric and set the start word
                        if (isAlphaNumeric(line.charAt(i))) {
                            startIndex = i;
                        }
                    } else {
                        // when the character is not alphanumeric or it is the last index of the line,
                        // we found the end of a word
                        String currWord = "";
                        boolean wordFound = false;
                        boolean resetStartIndex = false;

                        // we found word when i is not alphanumeric or i is the last index of the line
                        if (!isAlphaNumeric(line.charAt(i))) {
                            if ((endIndex - startIndex) > 1) {
                                currWord = line.substring(startIndex, endIndex).toLowerCase();
                                wordFound = true;
                            }
                            resetStartIndex = true;
                        } else {
                            if (i == (line.length() - 1)) {
                                if (line.length() - startIndex > 1) {
                                    currWord = line.substring(startIndex).toLowerCase();
                                    wordFound = true;
                                }
                                // dont need to reset startIndex
                                // because we are going to the next line and it will reset it there
                            }
                        }

                        if (wordFound == true) {
                            // check if current word is in the wordFreq list already
                            boolean added = inWordFreqList(wordFreq, currWord);
                            // check if current word is a stopword
                            boolean wanted = isStopword(stopWordsArray, currWord);
                            // if current word is not a stop word and it is not in the wordFreq list,
                            // we add it to the wordFreq
                            if (added == false && wanted == true)
                                wordFreq.add(new Term(currWord));
                            // update the order of wordFreq when we have more than one word in wordFreq and
                            // the count of any word increases
                            if (wordFreq.size() > 1 && added == true) {
                                wordFreq.sort(new Comparator<Term>() {
                                    @Override
                                    public int compare(Term t1, Term t2) {
                                        // descending order (ascending order is the other way around)
                                        return t2.count - t1.count;
                                    }
                                });
                            }
                        }
                        if (resetStartIndex) {
                            // word found, we reset the startIndex
                            startIndex = -1;
                        }
                    }
                    endIndex += 1;
                }
            }

            // write the sorted 25 most frequent words to a file
            System.out.println("25 most frequent terms:");
            int nMost = 0;
            for (Term t : wordFreq){
                nMost++;
                myWriter.write(t.title + "  -  " + t.count + "\n");
                System.out.println(t.title + "  -  " + t.count);
                if (nMost == 25)
                    break;
            }

            tf.close();
            sw.close();
            myWriter.close();

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            System.out.println("File not found.");
            e.printStackTrace();
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
    }
}
