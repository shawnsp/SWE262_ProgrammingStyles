package words;

import framework.TfWords;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class Words2 implements TfWords {
    ArrayList<String> words = new ArrayList<>();
    @Override
    public ArrayList<String> extract_words(String targetFile) {
        try {
            // store all stopwords in an arraylist
            ArrayList<String> stopWords = new ArrayList<>();
            File stopWordsFile = new File("../stop_words.txt");
            Scanner sw = new Scanner(stopWordsFile);
            String[] stopWordsArray = sw.nextLine().split(",");
            for (String s : stopWordsArray)
                stopWords.add(s.toLowerCase());

            // store all non stopwords
            Scanner tf = new Scanner(new File(targetFile));
            while (tf.hasNextLine()) {
                String line = tf.nextLine();
                String[] arrOfStr = line.split("[^A-Za-z0-9]+");
                for (String s : arrOfStr) {
                    s = s.toLowerCase();
                    if (s != "" && s.length() > 1 && !stopWords.contains(s))
                        words.add(s);
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return words;
    }
}
