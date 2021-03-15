//Style #22 - Tantrum
// - Every single procedure and function checks the sanity of its arguments and refuses to continue when the arguments are unreasonable
// - All code blocks check for all possible errors, possibly print out context-specific messages when errors occur, and pass the errors up the function call chain

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class TwentyTwo {
    // extract words from file
    public static ArrayList<String> extractWords(File targetFile) throws Exception {
        System.out.println("extractWords...");
        assert targetFile != null : "Why are you giving me an empty file?";

        ArrayList<String> wordList = new ArrayList<>();
        Scanner tf = null;
        try {
            tf = new Scanner(targetFile);
            while (tf.hasNextLine()) {
                String line = tf.nextLine();
                String[] arrOfStr = line.split("[^A-Za-z0-9]+");
                for (String s : arrOfStr) {
                    s = s.toLowerCase();
                    if (s!= "" && s.length() > 1)
                        wordList.add(s);
                }
            }
            tf.close();
            return wordList;
        } catch (FileNotFoundException e) {
            System.out.println("Come on...Where is your file?");
            tf.close();
            throw new Exception();
        }
    }

    // remove stop words from wordList
    public static ArrayList<String> removeStopWords(ArrayList<String> wordList) throws Exception {
        System.out.println("removeStopWords...");
        assert wordList.size() != 0 : "OMG, your word list is empty!";

        ArrayList<String> filteredWordList = new ArrayList<>();
        Scanner sw = null;
        try {
            sw = new Scanner(new File("../stop_words.txt"));
            ArrayList<String> stopWordsArray = new ArrayList<>();
            for (String s : sw.nextLine().split(","))
                stopWordsArray.add(s);
            for (String s: wordList) {
                if (!stopWordsArray.contains(s))
                    filteredWordList.add(s);
            }
            sw.close();
            return filteredWordList;
        } catch (FileNotFoundException e) {
            System.out.println("SRSLY? Now you don't have stop words file?");
            sw.close();
            throw new Exception();
        }
    }

    // count frequency
    public static ArrayList<Map.Entry<String, Integer>> frequencies(ArrayList<String> filteredWordList) {
        System.out.println("frequencies...");
        assert filteredWordList.size() != 0 : "OMG, your word list is empty!";

        HashMap<String, Integer> wordFreq = new HashMap<>();
        for (String w : filteredWordList) {
            if (wordFreq.containsKey(w))
                wordFreq.replace(w, wordFreq.get(w)+1);
            else
                wordFreq.put(w, 1);
        }
        ArrayList<Map.Entry<String, Integer>> wordFreqsList = new ArrayList<>(wordFreq.entrySet());
        return wordFreqsList;
    }

    // sort the wordFreqsList
    public static ArrayList<Map.Entry<String, Integer>> sort(ArrayList<Map.Entry<String, Integer>> wordFreqsList) {
        System.out.println("sort...");
        assert wordFreqsList.size() != 0 : "OMG, your word list is empty!";

        wordFreqsList.sort((a, b) -> b.getValue().compareTo(a.getValue()));
        return wordFreqsList;
    }

    public static void main(String[] args) {
//        assert args.length > 0 : "Hey, where is your target file?";
//        File targetFile = new File(args[0]);

        File targetFile = new File("../pride-and-prejudice.txt");

        try {
            ArrayList<Map.Entry<String, Integer>> wordFreqsList = sort(frequencies(removeStopWords(extractWords(targetFile))));

            System.out.println("\n----top25----");
            int count = 0;
            for (Map.Entry<String, Integer> entry : wordFreqsList) {
                count++;
                System.out.println(entry.getKey() + "  -  " + entry.getValue());
                if (count == 25)
                    break;
            }
        } catch (Exception e) {
            System.out.println("Oh no, my friend...");
            e.printStackTrace();
        }
    }
}
