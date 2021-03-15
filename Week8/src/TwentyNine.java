//Style #29 Actors - Similar to the letterbox style, but where the 'things' have independent threads of execution. Constraints:
//    - The larger problem is decomposed into 'things' that make sense for the problem domain
//    - Each 'thing' has a queue meant for other \textit{things} to place messages in it
//    - Each 'thing' is a capsule of data that exposes only its ability to receive messages via the queue
//    - Each 'thing' has its own thread of execution independent of the others.

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class TwentyNine {
    // a thread supper class for all actors
    private static abstract class ActiveWFObject extends Thread {
        private String name = null;
        private BlockingQueue<Object[]> queue = null;
        private Boolean stopMe = null;

        public ActiveWFObject() {
            this.name = this.getClass().getName();
            this.queue = new LinkedBlockingQueue<>();
            this.stopMe = false;
            this.start();
        }

        @Override
        public void run() {
            while (!stopMe) {
                Object[] message = this.queue.poll();
                try {
                    if (message != null) {
                        this.dispatch(message);
                        if (message[0].equals("die"))
                            this.stopMe = true;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        abstract public void dispatch(Object[] message) throws Exception;
        public void end() {this.stopMe = true;}
    }

    // sending message to receiver (placing it in the queue of the receiver)
    private static void send(ActiveWFObject receiver, Object[] message) {
        receiver.queue.add(message);
    }

    // DataStorageManager creates object that reads targetFile
    private static class DataStorageManager extends ActiveWFObject {
        private List<String> data = null;
        private StopWordsManager stopWordManager = null;

        @Override
        public void dispatch(Object[] message) {
            if (message[0].equals("init"))
                init(new Object[]{message[1], message[2]});
            else if (message[0].equals("send_word_freqs"))
                processWords(message[1]);
            else
                // don't know what to do -> forward to next actor
                send(this.stopWordManager, message);
        }

        private void init(Object[] message) {
            data = new ArrayList<>();
            try {
                System.out.println("[DataStorageManager]   init");
                Scanner tf = new Scanner(new File((String) message[0]));
                this.stopWordManager = (StopWordsManager) message[1];
                while (tf.hasNextLine()) {
                    String line = tf.nextLine();
                    String[] arrOfStr = line.split("[^A-Za-z0-9]+");
                    for (String s : arrOfStr){
                        s = s.toLowerCase();
                        if (s!= "" && s.length() > 1)
                            data.add(s);
                    }
                }
                tf.close();
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                System.out.println("File not found.");
                e.printStackTrace();
            }
        }

        private void processWords(Object message) {
            WordFrequencyController recipient = (WordFrequencyController) message;
            for (String word : data)
                send(this.stopWordManager, new Object[]{"filter", word});
            // when DataStorageManager runs out of words, it sends "top25" to StopWordsManager
            send(this.stopWordManager, new Object[]{"top25", recipient});
        }
    }

    // StopWordsManager creates object that reads stopWordsFile
    private static class StopWordsManager extends ActiveWFObject{
        private List<String>stopWords = null;
        private WordFrequencyManager wordFrequencyManager = null;

        @Override
        public void dispatch(Object[] message) {
            if (message[0].equals("init"))
                init(message[1]);
            else if (message[0].equals("filter")) {
                filter(message[1]);
                return;
            }
            else
                send(this.wordFrequencyManager, message);
        }

        private void init(Object message) {
            try {
                System.out.println("[StopWordsManager]   init");
                this.wordFrequencyManager= (WordFrequencyManager) message;
                File stopWordsFile = new File("../stop_words.txt");
                Scanner sw = new Scanner(stopWordsFile);
                stopWords = Arrays.asList(sw.nextLine().split(","));
                sw.close();
            }
            catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                System.out.println("File not found.");
                e.printStackTrace();
            }
        }

        private void filter(Object message) {
            String word = (String) message;
            if (!stopWords.contains(word))
                send(this.wordFrequencyManager, new Object[]{"word", word});
        }
    }

    // WordFrequencyManager creates object that counts word frequency and sort for top 25
    private static class WordFrequencyManager extends ActiveWFObject {
        private HashMap<String, Integer> wordFreqs = null;

        WordFrequencyManager() {
            System.out.println("[WordFrequencyManager]   init");
            wordFreqs = new HashMap<>();
        }

        @Override
        public void dispatch(Object[] message) {
            if (message[0].equals("word"))
                incrementCount(message[1]);
            else if (message[0].equals("top25"))
                top25(message[1]);
        }

        private void incrementCount(Object message) {
            String word = (String) message;

            if (wordFreqs.containsKey(word))
                wordFreqs.replace(word, wordFreqs.get(word)+1);
            else
                wordFreqs.put(word, 1);
        }

        private void top25(Object message) {
            WordFrequencyController recipient = (WordFrequencyController) message;
            ArrayList<Map.Entry<String, Integer>> freqsSorted = new ArrayList<>(wordFreqs.entrySet());
            freqsSorted.sort((a, b) -> b.getValue().compareTo(a.getValue()));
            send(recipient, new Object[]{"top25", freqsSorted});
        }
    }

    // WordFrequencyController run the program and output result
    private static class WordFrequencyController extends ActiveWFObject {
        DataStorageManager dataStorageManager = null;
        @Override
        public void dispatch(Object[] message) throws Exception {
            if (message[0].equals("run"))
                run(message[1]);
            else if (message[0].equals("top25"))
                display(message[1]);
            else
                throw new Exception("Message not understood " + message[0]);
        }

        private void run(Object message) {
            this.dataStorageManager = (DataStorageManager) message;
            send(this.dataStorageManager, new Object[]{"send_word_freqs", this});
        }

        private void display(Object message){
            ArrayList<Map.Entry<String, Integer>> freqsSorted = (ArrayList<Map.Entry<String, Integer>>) message;
            int count = 0;
            for (Map.Entry<String, Integer> entry : freqsSorted) {
                count++;
                System.out.println(entry.getKey() + "  -  " + entry.getValue());
                if (count == 25)
                    break;
            }
            send(this.dataStorageManager, new Object[]{"die"});
            this.end();
        }
    }


    public static void main(String[] args) throws InterruptedException {
//        String targetFile = args[0];
        String targetFile = "../pride-and-prejudice.txt";
        WordFrequencyManager wfm = new WordFrequencyManager();

        StopWordsManager swm = new StopWordsManager();
        send(swm, new Object[]{"init", wfm});

        DataStorageManager dsm = new DataStorageManager();
        send(dsm, new Object[]{"init", targetFile, swm});

         WordFrequencyController wfc = new WordFrequencyController();
         send(wfc, new Object[]{"run", dsm});

        // Wait for the active objects to finish
        Thread[] threads = new ActiveWFObject[]{wfm, swm, dsm, wfc};
        for (Thread t: threads)
            t.join();
    }
}