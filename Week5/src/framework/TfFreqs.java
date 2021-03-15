package framework;

import java.util.ArrayList;
import java.util.Map;

// an interface from framework to let 3rd party programmer creating app to get top25 words by implementing this
// 1. providing a guide line
// 2. indicate what the main program want
public interface TfFreqs {
    // list of Map entries
    public ArrayList<Map.Entry<String, Integer>> top25(ArrayList<String> words);
}
