// Style #16 Publish-Subscribe
// Larger problem is decomposed into entities using some form of abstraction (objects, modules or similar)
// The entities are never called on directly for actions
// Existence of an infrastructure for publishing and subscribing to events, aka the bulletin board
// Entities post event subscriptions (aka 'wanted') to the bulletin board and publish events (aka 'offered') to the bulletin board. the bulletin board does all the event management and distribution

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Scanner;
import java.util.function.Consumer;

public class Sixteen {
    // Helper class - Term data structure
    private static class Term {
        public String title;
        public int count;

        public Term(String title) {
            this.title = title;
            this.count = 1;
        }
    }

    // EventManager: subscribe handlers which are some class methods that will be run later by publish
    private static class EventManager {
        HashMap<String, ArrayList<Consumer<String[]>>> subscription;
        EventManager() {subscription = new HashMap<>();}

        private void subscribe (String eventType, Consumer<String[]> handler) {
            if (subscription.containsKey(eventType))
                subscription.get(eventType).add(handler);
            else{
                ArrayList<Consumer<String[]>> handlerList = new ArrayList<>();
                handlerList.add(handler);
                subscription.put(eventType, handlerList);
            }
        }

        private void publish (String[] event) {
            String eventType = event[0];
            if (subscription.containsKey(eventType))
                for (Consumer<String[]> handler : subscription.get(eventType))
                    handler.accept(event);
        }
    }


    // DataStorage (handler -> load and produce)
    private static class DataStorage {
        EventManager em;
        ArrayList<String> words;
        DataStorage(EventManager outerEM) {
            em = outerEM;
            em.subscribe("load", (Consumer<String[]>) (event) -> load(event));
            em.subscribe("start", (Consumer<String[]>) (event) -> produceWords(event));
        }

        // read targetFIle and store words
        private void load (String[] event) {
            String targetFile = event[1];
            try {
                Scanner tf = new Scanner(new File(targetFile));
                words = new ArrayList<>();
                while (tf.hasNextLine()) {
                    String line = tf.nextLine();
                    String[] arrOfStr = line.split("[^A-Za-z0-9]+");
                    for (String s : arrOfStr)
                        if (s != "" && s.length() > 1)
                            words.add(s.toLowerCase());
                }
                tf.close();
            }
            catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                System.out.println("File not found.");
                e.printStackTrace();
            }
        }

        private void produceWords (String[] event) {
            // for each word we send it to check at StopWordFilter
            for (String s : words)
                em.publish(new String[]{"word", s});
            em.publish(new String[]{"eof", null});
        }
    }


    // StopWordFilter (handler -> load and word)
    private static class StopWordFilter {
        EventManager em;
        String[] stopWords;
        StopWordFilter(EventManager outerEM) {
            em = outerEM;
            em.subscribe("load", (Consumer<String[]>) (event) -> load(event));
            em.subscribe("word", (Consumer<String[]>) (event) -> isStopWord(event));
        }

        private void load (String[] event) {
            try {
                File stopWordsFile = new File("../stop_words.txt");
                Scanner sw = new Scanner(stopWordsFile);
                stopWords = sw.nextLine().split(",");
                sw.close();
            }
            catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                System.out.println("File not found.");
                e.printStackTrace();
            }
        }

        private void isStopWord(String[] event) {
            boolean matched = false;
            String word = event[1];
            for (String s : stopWords)
                if (word.equals(s))
                    matched = true;
            if (!matched)
                em.publish(new String[]{"validWord", word});
        }
    }

    // WordFrequencyCounter (handler -> incrementCount and output)
    private static class WordFrequencyCounter {
        EventManager em;
        ArrayList<Term> wordFreqs;

        WordFrequencyCounter(EventManager outerEM) {
            em = outerEM;
            wordFreqs = new ArrayList<>();
            em.subscribe("validWord", (Consumer<String[]>) (event) -> incrementCount(event));
            em.subscribe("output", (Consumer<String[]>) (event) -> output(event));
        }

        private void incrementCount(String[] event) {
            boolean added = false;
            String word = event[1];
            for (Term t : wordFreqs) {
                if (t.title.equals(word)) {
                    t.count++;
                    added = true;
                    break;
                }
            }
            if (!added)
                wordFreqs.add(new Term(word));
        }

        private void output(String[] event){
            wordFreqs.sort(new Comparator<Term>(){
                @Override
                public int compare(Term t1, Term t2) {
                    // descending order (ascending order is the other way around)
                    return t2.count-t1.count;
                }
            });

            try {
                FileWriter myWriter = new FileWriter("./term_frequency16.txt");
                int nMost = 0;
                for (Term t : wordFreqs ){
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
    }

    // WordFrequencyApplication (handler -> run and stop)
    private static class WordFrequencyApplication {
        EventManager em;
        WordFrequencyApplication(EventManager outerEM) {
            em = outerEM;
            em.subscribe("run", (Consumer<String[]>) (event) -> run(event));
            em.subscribe("eof", (Consumer<String[]>) (event) -> stop(event));
        }

        private void run (String[] event) {
            String targetFile = event[1];
            em.publish(new String[]{"load", targetFile});
            em.publish(new String[]{"start", null});
        }

        private void stop (String[] event) {
            em.publish(new String[]{"output", null});
        }
    }

    // 16.2 class ZWordFilter and ZwordCounter
    // - non-stop words with z counter - 837
    // specialWordCounter (handler -> )
    private static class ZWordFilter {
        EventManager em;
        ArrayList<String> nonstopWordsWithZ;
        ZWordFilter(EventManager outerEM) {
            em = outerEM;
            nonstopWordsWithZ = new ArrayList<>();
            em.subscribe("word", (Consumer<String[]>) (event) -> containZ(event));
        }

        private void containZ(String[] event) {
            boolean foundZ = false;
            String word = event[1];
            for (int i = 0; i<word.length(); i++)
                if (word.charAt(i) == 'z') {
                    foundZ = true;
                    break;
                }
            if (foundZ)
                em.publish(new String[]{"foundZWord", null});
        }
    }

    private static class ZwordCounter {
        EventManager em;
        int zWordCount;

        ZwordCounter(EventManager outerEM) {
            em = outerEM;
            zWordCount = 0;
            em.subscribe("foundZWord", (Consumer<String[]>) (event) -> incrementCount(event));
            em.subscribe("output", (Consumer<String[]>) (event) -> output(event));
        }

        private void incrementCount(String[] event) {
            zWordCount++;
        }

        private void output(String[] event){
            try {
                FileWriter myWriter = new FileWriter("./term_frequency16.txt", true);
                myWriter.append("Total count of non-stopword with z: " + zWordCount);
                System.out.println("Total count of non-stopword with z: " + zWordCount);
                myWriter.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                System.out.println("Write to file failing.");
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
//        String targetFile = args[0];
        String targetFile = "../pride-and-prejudice.txt";

        EventManager em = new EventManager();
        new DataStorage(em);
        new StopWordFilter(em);
        new ZWordFilter(em);        // 16.2
        new WordFrequencyCounter(em);
        new ZwordCounter(em);       //16.2
        new WordFrequencyApplication(em);
        em.publish(new String[]{"run", targetFile});
    }
}


// Structure  (ZwordFilter and ZwordCounter are for 16.2)
// subscription { load: [DataStorage, StopWordFilter];
//                start: [DataStorage];
//                word: [StopWordFilter, ZwordFilter];
//                validWord: [WordFrequencyCounter];
//                output: [WordFrequencyCounter, ZwordCounter];
//                foundZWord: [ZwordCounter];
//                run: [WordFrequencyApp];
//                eof: [WordFrequencyApp]
//                }
//
// Procedure  (ZwordFilter and ZwordCounter are for 16.2)
// - run
//    1) WordFrequencyApp.run   (load, start)
// - - load
//        1) DataStorage.load
//        2) StopWordFilter.load
// - - start
//        1) DataStorage.produceWords   (word, eof)
// - - - word
//         1) StopWordFilter.isStopWord   (validWord)
// - - - - validWord
//              1) WordFrequencyCounter.incrementCount
//         2) ZwordFilter.containZ   (foundZWord)
// - - - - foundZWord
//              1) ZwordCounter.incrementCout
// - - - eof
//         1) WordFrequencyApp.stop   (output)
// - - - - output
//               1) WordFrequencyCounter.output
//               2) ZwordCounter.output


