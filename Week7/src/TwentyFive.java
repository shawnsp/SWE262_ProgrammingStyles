//Style #25 - Quarantine: a variation of style #09, The One, with the following additional constraints
// - Core program functions have no side effects of any kind, including IO
// - All IO actions must be contained in computation sequences that are clearly separated from the pure functions
// - All sequences that have IO must be called from the main program

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.function.Function;

public class TwentyFive {
    interface MyFunc {
        public Object doSomeThing();
    }

    public static class Quarantine {
        private Object value;
        private ArrayList<Object> funcList;

        public Quarantine(Object value) {
            this.value = value;
            this.funcList = new ArrayList<>();
        }

        public Quarantine bind(Object func) {
            this.funcList.add(func);
            return this;
        }

        public void execute() {
            for (Object func : this.funcList) {
                if(guardCallable(value)){
                    this.value = ((Function)func).apply(((MyFunc)this.value).doSomeThing());
                }
                else{
                    this.value = ((Function)func).apply(this.value);
                }
            }
            System.out.println(((MyFunc)this.value).doSomeThing());
        }

        private boolean guardCallable(Object value){
            if (value instanceof MyFunc)
                return true;
            else
                return false;
        }
    }


    // extract words from file
    public static final Function<Object, Object> extractWords = (targetFile) -> {
        MyFunc func = new MyFunc() {
            @Override
            public Object doSomeThing() {
                System.out.println("extractWords...");
                ArrayList<String> wordList = new ArrayList<>();
                Scanner tf = null;
                try {
//                    System.out.println((String)targetFile);
                    tf = new Scanner(new File((String)targetFile));
                    while (tf.hasNextLine()) {
                        String line = tf.nextLine();
                        String[] arrOfStr = line.split("[^A-Za-z0-9]+");
                        for (String s : arrOfStr) {
                            s = s.toLowerCase();
                            if (s != "" && s.length() > 1)
                                wordList.add(s);
                        }
                    }
                } catch (FileNotFoundException e) {
                    System.out.println("File not found");
                }
                tf.close();
                return wordList;
            }
        };
        return func;
    };

    // remove stop words from wordList
    public static final Function<Object, Object> removeStopWords = (wordList) -> {
        MyFunc func = new MyFunc() {
            @Override
            public Object doSomeThing() {
                System.out.println("removeStopWords...");
                ArrayList<String> filteredWordList = new ArrayList<>();
                Scanner sw = null;
                try {
                    sw = new Scanner(new File("../stop_words.txt"));
                    ArrayList<String> stopWordsArray = new ArrayList<>();
                    for (String s : sw.nextLine().split(","))
                        stopWordsArray.add(s);
                    for (String s : (ArrayList<String>)wordList) {
                        if (!stopWordsArray.contains(s))
                            filteredWordList.add(s);
                    }
                } catch (FileNotFoundException e) {
                    System.out.println("Stop words file not found");
                }
                sw.close();
                return filteredWordList;
            }
        };
        return func;
    };

    // count frequency
    public static final Function<Object, Object> frequencies = (filteredWordList) -> {
        MyFunc func = new MyFunc() {
            @Override
            public Object doSomeThing() {
                System.out.println("frequencies...");
                HashMap<String, Integer> wordFreq = new HashMap<>();
                for (String w : (ArrayList<String>)filteredWordList) {
                    if (wordFreq.containsKey(w))
                        wordFreq.replace(w, wordFreq.get(w) + 1);
                    else
                        wordFreq.put(w, 1);
                }
                ArrayList<Map.Entry<String, Integer>> wordFreqsList = new ArrayList<>(wordFreq.entrySet());
                return wordFreqsList;
            }
        };
        return func;
    };

    // sort the wordFreqsList
    public static final Function<Object, Object> sort = (wordFreqsList) -> {
        MyFunc func = new MyFunc() {
            @Override
            public Object doSomeThing() {
                System.out.println("sort...");
                ((ArrayList<Map.Entry<String, Integer>>)wordFreqsList).sort((a, b) -> b.getValue().compareTo(a.getValue()));
                return wordFreqsList;
            }
        };
        return func;
    };

    // print top25
    public static final Function<Object, Object> top25 = (wordFreqsList) -> {
        MyFunc func = new MyFunc() {
            @Override
            public Object doSomeThing() {
                System.out.println("\n----top25----");
                String output = "";
                int count = 0;
                for (Map.Entry<String, Integer> entry : (ArrayList<Map.Entry<String, Integer>>)wordFreqsList) {
                    count++;
                    output += entry.getKey() + "  -  " + entry.getValue() + "\n";
                    if (count == 25)
                        break;
                }
                return output;
            }
        };
        return func;
    };

    public static void main(String[] args) {
//        File targetFile = new File(args[0]);
        String targetFile = "../pride-and-prejudice.txt";
        new Quarantine(targetFile)
                .bind(extractWords)
                .bind(removeStopWords)
                .bind(frequencies)
                .bind(sort)
                .bind(top25)
                .execute();

    }
}
