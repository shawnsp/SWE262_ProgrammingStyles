import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class TwentyEight {
    public static class AllWords implements Iterator<String> {
        Scanner sc;
        public AllWords(String targetFile) throws FileNotFoundException {
            sc = new Scanner(new File(targetFile));
        }

        @Override
        public boolean hasNext() {
            return sc.hasNextLine();
        }

        @Override
        public String next() {
            return sc.nextLine();
        }
    }

    public static class NonStopWord implements Iterator<ArrayList<String>> {
        AllWords allWords;
        ArrayList<String> stopWords;
        ArrayList<String> wordList;

        public NonStopWord(AllWords allWords) throws FileNotFoundException {
            this.allWords = allWords;
            Scanner sc = new Scanner(new File("../stop_words.txt"));
            stopWords = new ArrayList<>();
            String[] stopWordsArray = sc.nextLine().split(",");
            for (String s : stopWordsArray)
                stopWords.add(s);
            wordList = new ArrayList<>();
        }
        @Override
        public boolean hasNext() {
            return allWords.hasNext();
        }

        @Override
        public ArrayList<String> next() {
            if (hasNext()){
                String[] arrOfStr = allWords.next().split("[^A-Za-z0-9]+");
                for (String s : arrOfStr) {
                    s = s.toLowerCase();
                    if (s != "" && s.length() > 1 && !stopWords.contains(s))
                        wordList.add(s);
                }
            }
            return wordList;
        }
    }

    public static class CountAndSort implements Iterator<String> {
        NonStopWord nonStopWord;
        int counter;
        HashMap<String, Integer> wordFreq;


        public CountAndSort (NonStopWord nonStopWord) {
            this.nonStopWord = nonStopWord;
            counter = 0;
        }

        @Override
        public boolean hasNext() {
            return nonStopWord.hasNext();
        }

        @Override
        public String next() {
            while (hasNext()) {
                ArrayList<String> wordList = nonStopWord.next();
                counter++;
                wordFreq = new HashMap<>();
                for (String w : wordList) {
                    if (wordFreq.containsKey(w))
                        wordFreq.replace(w, wordFreq.get(w)+1);
                    else {
                        wordFreq.put(w, 1);
//                        counter++;
                    }
                }

                if (counter >= 5000) {
                    ArrayList<Map.Entry<String, Integer>> wordFreqsList = new ArrayList<>(wordFreq.entrySet());
                    wordFreqsList.sort((a, b) -> b.getValue().compareTo(a.getValue()));
                    ArrayList<Map.Entry<String, Integer>> top25WordFreqs = new ArrayList<>();
                    int count = 0;
                    for (Map.Entry<String, Integer> entry : wordFreqsList) {
                        count++;
                        top25WordFreqs.add(entry);
                        if (count == 25)
                            break;
                    }
                    for (Map.Entry<String, Integer> entry : top25WordFreqs)
                        System.out.println(entry.getKey() + "  -  " + entry.getValue());

                    System.out.println("----------- system running -------------");
                    counter = 0;
                }

            }

            ArrayList<Map.Entry<String, Integer>> wordFreqsList = new ArrayList<>(wordFreq.entrySet());
            wordFreqsList.sort((a, b) -> b.getValue().compareTo(a.getValue()));
            ArrayList<Map.Entry<String, Integer>> top25WordFreqs = new ArrayList<>();
            int count = 0;
            for (Map.Entry<String, Integer> entry : wordFreqsList) {
                count++;
                top25WordFreqs.add(entry);
                if (count == 25)
                    break;
            }
            for (Map.Entry<String, Integer> entry : top25WordFreqs)
                System.out.println(entry.getKey() + "  -  " + entry.getValue());
            return "----------- system end -------------";
        }
    }


    public static void main(String[] args) throws FileNotFoundException {
//        String targetFile = args[0];
        String targetFile = "../pride-and-prejudice.txt";

        AllWords allWords = new AllWords(targetFile);
        NonStopWord nonStopWord = new NonStopWord(allWords);
        CountAndSort countAndSort = new CountAndSort(nonStopWord);
        String end = countAndSort.next();
        System.out.println(end);


    }
}
