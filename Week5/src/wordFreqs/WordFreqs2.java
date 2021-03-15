package wordFreqs;

import framework.TfFreqs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class WordFreqs2 implements TfFreqs {
    @Override
    public ArrayList<Map.Entry<String, Integer>> top25(ArrayList<String> words) {
        // count freq
        ArrayList<Map.Entry<String, Integer>> wordFreqsList = new ArrayList<>();
        for (String w : words) {
            for (Map.Entry<String, Integer> entry : wordFreqsList)
                if (entry.getKey() == w)
                    entry.setValue(entry.getValue()+1);
                else {
                    HashMap<String, Integer> temp = new HashMap<>();
                    temp.put(w, 1);
                    wordFreqsList.add(temp.entrySet().iterator().next());
                }
            }

        // sort top 25
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
