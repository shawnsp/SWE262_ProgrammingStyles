package framework;

import java.util.ArrayList;

// an interface from framework to let 3rd party programmer creating app to extract words by implementing this
// 1. providing a guide line
// 2. indicate what the main program want
public interface TfWords {
    public ArrayList<String> extract_words(String targetFile);
}
