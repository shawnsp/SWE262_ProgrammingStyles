package words;

import framework.TfWords;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class Words1 implements TfWords {
    @Override
    public ArrayList<String> extract_words(String targetFile) {
        ArrayList<String> words = new ArrayList<>();
        try {
            // store all stopwords in an arraylist
            ArrayList<String> stopWords = new ArrayList<>();
            File stopWordsFile = new File("../stop_words.txt");
            Scanner sw = new Scanner(stopWordsFile);
            String[] stopWordsArray = sw.nextLine().split(",");
            for (String s : stopWordsArray)
                stopWords.add(s.toLowerCase());

            // store all words in an arraylist
            ArrayList<String> rawWords = new ArrayList<>();
            Scanner tf = new Scanner(new File(targetFile));
            while (tf.hasNextLine()) {
                String line = tf.nextLine();
                String[] arrOfStr = line.split("[^A-Za-z0-9]+");
                for (String s : arrOfStr) {
                    if (s != "")
                        rawWords.add(s.toLowerCase());
                }
            }

            // filter out stopwords
            for(String w : rawWords) {
                if(w.length() > 1 && !stopWords.contains(w)) {
                    words.add(w);
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return words;
    }
}
