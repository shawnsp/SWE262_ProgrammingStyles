package wordFreqs;

import framework.TfFreqs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class WordFreqs1 implements TfFreqs {
    @Override
    public ArrayList<Map.Entry<String, Integer>> top25(ArrayList<String> words) {
        // count freq
        HashMap<String, Integer> wordFreq = new HashMap<>();
        for (String w : words) {
           if (wordFreq.containsKey(w))
               wordFreq.replace(w, wordFreq.get(w)+1);
           else
               wordFreq.put(w, 1);
        }

        // sort top 25
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
        return top25WordFreqs;
    }
}
