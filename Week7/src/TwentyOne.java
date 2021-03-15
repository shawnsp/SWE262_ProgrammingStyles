//Style #21 - Constructivist
// - Every single procedure and function checks the sanity of its arguments and either returns something sensible when the arguments are unreasonable or assigns them reasonable values
// - All code blocks check for possible errors and escape the block when things go wrong, setting the state to something reasonable

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class TwentyOne {
    // extract words from file
    public static ArrayList<String> extractWords(File targetFile) {
        System.out.println("extractWords...");
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
            System.out.println("File not found");
            tf.close();
            return wordList;
        }
    }

    // remove stop words from wordList
    public static ArrayList<String> removeStopWords(ArrayList<String> wordList) {
        System.out.println("removeStopWords...");
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
            System.out.println("Stop words file not found");
            sw.close();
            return wordList;
        }
    }

    // count frequency
    public static ArrayList<Map.Entry<String, Integer>> frequencies(ArrayList<String> filteredWordList) {
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
        wordFreqsList.sort((a, b) -> b.getValue().compareTo(a.getValue()));
        return wordFreqsList;
    }

    public static void main(String[] args) {
        File targetFile;
        if (args.length > 0)
            targetFile = new File(args[0]);
        else {
            System.out.println("No file entered. Running backup file...");
            targetFile = new File("../pride-and-prejudice.txt");
        }

        ArrayList<Map.Entry<String, Integer>> wordFreqsList = sort(frequencies(removeStopWords(extractWords(targetFile))));

        System.out.println("\n----top25----");
        int count = 0;
        for (Map.Entry<String, Integer> entry : wordFreqsList) {
            count++;
            System.out.println(entry.getKey() + "  -  " + entry.getValue());
            if (count == 25)
                break;
        }
    }
}
