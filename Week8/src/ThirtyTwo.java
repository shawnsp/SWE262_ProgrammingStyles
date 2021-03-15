//Style #32 - Double Map Reduce
//    - Input data is divided in chunks, similar to what an inverse multiplexer does to input signals
//    - A map function applies a given worker function to each chunk of data, potentially in parallel
//    - The results of the many worker functions are reshuffled in a way that allows for the reduce step to be also parallelized
//    - The reshuffled chunks of data are given as input to a second map function that takes a reducible function as input

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Stream;

public class ThirtyTwo {
    private static Scanner readFile(String targetFile) throws FileNotFoundException {
        Scanner sc = new Scanner(new File(targetFile));
        return sc;
    }

    // return a pack iterator - 200 lines per pack
    private static Iterable<String> partition(Scanner sc, int nlines){
        Stream.Builder<String> packBuilder = Stream.builder();
        int lineCount = 1;
        String pack = "";
        while(sc.hasNextLine()){
            if(lineCount == nlines){
                pack +=  " " + sc.nextLine();
                packBuilder.add(pack);
                pack = "";
                lineCount = 1;
            } else{
                pack += " "+ sc.nextLine();
                lineCount++;
            }
        }
        sc.close();
        Iterable<String> packIterator = () -> packBuilder.build().iterator();
        return packIterator;
    }

    /*
        Takes a string, returns a list of pairs (word, 1),
        one for each word in the input, so
        [(w1, 1), (w2, 1), ..., (wn, 1)]
     */
    private static ArrayList<ArrayList<Object>> splitWords(String pack) throws FileNotFoundException {
        // scan pack
        ArrayList<String> words = new ArrayList<>();
        String[] arrOfStr = pack.toLowerCase().replaceAll("[^a-z0-9]", " ").split(" ");
        for (String s : arrOfStr)
            if (s != "" && s.length() > 1)
                words.add(s);

        // remove stop words
        ArrayList<String> nonStopWords = new ArrayList<>();
        List<String> stopWords;
        Scanner sc = new Scanner(new File("../stop_words.txt"));
        stopWords = Arrays.asList(sc.nextLine().split(","));
        sc.close();
        for (String s : words)
            if (!stopWords.contains(s))
                nonStopWords.add(s);

        // mapper -> [(w1, 1), (w2, 1), ..., (wn, 1)]
        ArrayList<ArrayList<Object>> wordList = new ArrayList<>();
        for (String s: nonStopWords){
            ArrayList<Object> pair = new ArrayList<>();
            pair.add(s);
            pair.add(1);
            wordList.add(pair);
        }
        return wordList;
    }

    /*
        Takes a list of lists of pairs of the form
        [[(w1, 1), (w2, 1), ..., (wn, 1)],
         [(w1, 1), (w2, 1), ..., (wn, 1)],
         ...]

        and returns a dictionary mapping each unique word to the corresponding list of pairs, so
        { w1 : [(w1, 1), (w1, 1)...],
          w2 : [(w2, 1), (w2, 1)...],
          ...}
     */
    private static HashMap<String, ArrayList<ArrayList<Object>>> regroup(ArrayList<ArrayList<ArrayList<Object>>> splitWordsList){
        HashMap<String, ArrayList<ArrayList<Object>>> mapping = new HashMap<>();
        for(ArrayList<ArrayList<Object>> splitWords : splitWordsList){
            for(ArrayList<Object> pair : splitWords){
                String key = (String) pair.get(0);
                if(mapping.containsKey(key))
                    mapping.get(key).add(pair);
                else{
                    ArrayList<ArrayList<Object>> newSplitWords = new ArrayList<>();
                    newSplitWords.add(pair);
                    mapping.put(key, newSplitWords);
                }
            }
        }
        return mapping;
    }

    /*
         Takes a mapping of the form (word, [(word, 1), (word, 1)...)])
        and returns a pair (word, frequency), where frequency is the
        sum of all the reported occurrences
     */
    private static HashMap<String, Integer> countingWords(Map.Entry<String, ArrayList<ArrayList<Object>>> splitsPerWordEntry){
        HashMap wordFreq = new HashMap();
        int freq = 0;
        for(ArrayList<Object> pair : splitsPerWordEntry.getValue()){
            freq += (int)pair.get(1);
        }
        wordFreq.put(splitsPerWordEntry.getKey(), freq);
        return wordFreq;
    }

    // sort wordFreqs
    private static ArrayList<Map.Entry<String, Integer>> sorted(HashMap<String, Integer> wordFreqs){
        ArrayList<Map.Entry<String, Integer>> freqsSorted = new ArrayList<>(wordFreqs.entrySet());
        freqsSorted.sort((a, b) -> b.getValue().compareTo(a.getValue()));
        return freqsSorted;
    }

    public static void main(String[] args) throws FileNotFoundException {
        //        String targetFile = args[0];
        String targetFile = "../pride-and-prejudice.txt";

        // map splitWords with partition
        ArrayList<ArrayList<ArrayList<Object>>> splits = new ArrayList<>();
        for(String pack: partition(readFile(targetFile), 200)){
            splits.add(splitWords(pack));
        }

        // regroup splitWords list into a dictionary
        HashMap<String, ArrayList<ArrayList<Object>>> splitsPerWord = regroup(splits);

        // map each entry of splitsPerWord to countingWords
        // produce a new dictionary of word frequency count
        HashMap<String, Integer> wordFreqs = new HashMap<>();
        for(Map.Entry<String, ArrayList<ArrayList<Object>>> splitsPerWordEntry : splitsPerWord.entrySet()) {
            HashMap<String, Integer> wordFreq = countingWords(splitsPerWordEntry);
            for (Map.Entry<String, Integer> wordFreqEntry : wordFreq.entrySet())
                wordFreqs.put(wordFreqEntry.getKey(), wordFreqEntry.getValue());
        }
        ArrayList<Map.Entry<String, Integer>> freqsSorted = sorted(wordFreqs);

        // output top25
        int count = 0;
        for (Map.Entry<String, Integer> entry : freqsSorted) {
            count++;
            System.out.println(entry.getKey() + "  -  " + entry.getValue());
            if (count == 25)
                break;
        }
    }
}
