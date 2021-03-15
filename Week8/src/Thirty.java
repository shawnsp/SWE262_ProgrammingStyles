//Style #30 - Dataspaces
//    - Existence of one or more units that execute concurrently
//    - Existence of one or more data spaces where concurrent units store and retrieve data
//    - No direct data exchanges between the concurrent units, other than via the data spaces

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class Thirty {
    private static BlockingQueue<String> wordSpace = null;
    private static BlockingQueue<HashMap<String, Integer>> freqSpace = null;
    private static List<String> stopWords = null;

    private static Runnable processWords = () -> {
        System.out.println("[" + Thread.currentThread().getName() + "] "+ " start");
        HashMap<String, Integer> wordFreqs = new HashMap<>();
        String word;
        while (!wordSpace.isEmpty()) {
            try {
                word = wordSpace.poll(1, TimeUnit.SECONDS);
//                System.out.println("[" + Thread.currentThread().getName() + "] "+ word);
            } catch (InterruptedException e) {
                System.out.println(Thread.currentThread().getName() + " time out");
                System.out.println("Time out, no more words");
                break;
            }

            if (!stopWords.contains(word)) {
//                System.out.println("[" + Thread.currentThread().getName() + "] " + word);
                if (wordFreqs.containsKey(word))
                    wordFreqs.replace(word, wordFreqs.get(word) + 1);
                else
                    wordFreqs.put(word, 1);
            }
        }
        try {
            freqSpace.put(wordFreqs);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    };

    public static void main(String[] args) throws FileNotFoundException, InterruptedException {
        wordSpace = new LinkedBlockingQueue<>();
        freqSpace = new LinkedBlockingQueue<>();
        stopWords = new ArrayList<>();

//        String targetFile = args[0];
        String targetFile = "../pride-and-prejudice.txt";

        // load wordSpace queue
        System.out.println("[main] load wordSpace");
        Scanner tf = new Scanner(new File(targetFile));
        while (tf.hasNextLine()) {
            String line = tf.nextLine();
            String[] arrOfStr = line.split("[^A-Za-z0-9]+");
            for (String s : arrOfStr) {
                s = s.toLowerCase();
                if (s != "" && s.length() > 1)
                    wordSpace.put(s);
            }
        }
        tf.close();

        // load stopWords
        System.out.println("[main] load stopWords");
        File stopWordsFile = new File("../stop_words.txt");
        Scanner sw = new Scanner(stopWordsFile);
        stopWords = Arrays.asList(sw.nextLine().split(","));
        sw.close();

        // create 5 worker threads to processWords
        ArrayList<Thread> workers = new ArrayList<>();
        for(int i=0; i<5; i++)
            workers.add(new Thread(processWords));
        // start all workers
        for(Thread t: workers)
            t.start();
        // wait for all workers to finish
        for(Thread t: workers)
            t.join();

        // merge each partial frequency results in freqSpace
        System.out.println("[main] merge partial freqs");
        HashMap<String, Integer> wordFreqs = new HashMap<>();
        while (!freqSpace.isEmpty()) {
            HashMap<String, Integer> freqs = freqSpace.poll();
            for (String key : freqs.keySet()) {
                Integer count;
                if (wordFreqs.containsKey(key))
                    count = wordFreqs.get(key) + freqs.get(key);
                else
                    count = freqs.get(key);
                wordFreqs.put(key, count);
            }
        }

        System.out.println("[main] sort and print wordFreqs");
        ArrayList<Map.Entry<String, Integer>> freqsSorted = new ArrayList<>(wordFreqs.entrySet());
        freqsSorted.sort((a, b) -> b.getValue().compareTo(a.getValue()));
        int count = 0;
        for (Map.Entry<String, Integer> entry : freqsSorted) {
            count++;
            System.out.println(entry.getKey() + "  -  " + entry.getValue());
            if (count == 25)
                break;
        }
    }
}
