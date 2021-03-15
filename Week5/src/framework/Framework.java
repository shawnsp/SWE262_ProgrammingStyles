package framework;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Map;
import java.util.Properties;

public class Framework {
    private static TfWords words;
    private static TfFreqs wordFreqs;

    public static void load_plugins() throws Exception{
        Properties prop = new Properties();

        prop.load(new FileInputStream("config.properties"));

        String wordsApp = prop.getProperty("Words");
        String wordFreqsApp = prop.getProperty("WordFreqs");

        URL wordsUrl = new File(wordsApp + ".jar").toURI().toURL();
        URL wordFreqsUrl = new File(wordFreqsApp + ".jar").toURI().toURL();
        URL[] urls = {wordsUrl, wordFreqsUrl};

        URLClassLoader cLoader = new URLClassLoader(urls);

        words = (TfWords) cLoader.loadClass(wordsApp).getDeclaredConstructor().newInstance();
        wordFreqs = (TfFreqs) cLoader.loadClass(wordFreqsApp).getDeclaredConstructor().newInstance();
    }

    public static void main(String[] args) throws Exception {
        load_plugins();
        ArrayList<Map.Entry<String, Integer>> top25WordFreqs = wordFreqs.top25(words.extract_words(args[0]));
        for (Map.Entry<String, Integer> entry : top25WordFreqs)
           System.out.println(entry.getKey() + "  -  " + entry.getValue());
    }
}
