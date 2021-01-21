package week1;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileNotFoundException;


class Term {
    public String title;
    public int count;

    public Term(String title){
        this.title = title;
        this.count = 1;
    }
}


class Tf_01 {
	public static void readFile(File targetFile, File stopWordsFile){
		try {
            Scanner tf = new Scanner(targetFile);
            Scanner sw = new Scanner(stopWordsFile);
            FileWriter myWriter = new FileWriter("./term_frequency.txt");
            
            String[] stopWordsArray = sw.nextLine().split(",");
            ArrayList<Term> set = new ArrayList<>();
            System.out.println("Start filtering...");
            while (tf.hasNextLine()) {
                String line = tf.nextLine();
                // split with token other than alphanumeric 
                String[] arrOfStr = line.split("[^A-Za-z0-9]+");
                for (String s: arrOfStr) {
                	s = s.toLowerCase();
                    if (s == "" || s.length() < 2)
                        // jump to next iteration
                        continue;

                    boolean added = false;
                    for (Term t: set){
                        if (t.title.equals(s)){
                            t.count++;
                            added = true;
                            continue;
                        }
                    }
                    
                    // check if the word is not a stopword, then it is wanted
                    boolean wanted = true;
                    for (String stopWords: stopWordsArray) {
                    	if (s.equals(stopWords)) {
                    		wanted = false;
                    	}
                    }
                    
                    if (added == false && wanted == true)
                        set.add(new Term(s));
                }
            }
            
            // sort the set
            set.sort(new Comparator<Term>(){
            		@Override
            		public int compare(Term t1, Term t2) {
            			// descending order (ascending order is the other way around)
            			return t2.count-t1.count;
            		}
            });
            
            // write the 25 most frequent words to a file
            System.out.println("25 most frequent terms:");
            int nMost = 0;
            for (Term t : set){
            	nMost++;
                myWriter.write(t.title + "  -  " + t.count + "\n");
                System.out.println(t.title + "  -  " + t.count);
                if (nMost == 25)
                	break;
            }
            
            tf.close();
            sw.close();
            myWriter.close();

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            System.out.println("File not found.");
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            System.out.println("Write to file failing.");
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
//        File targetFile = new File(args[0]);
    	File targetFile = new File("../pride-and-prejudice.txt");
        File stopWordsFile = new File("../stop_words.txt");
        readFile(targetFile, stopWordsFile);
    }
}

// if the program is organized with seperated bin and src folders
// compile it using: javac -d bin src/week1/Tf_01.java
// in bin folder, run it: java Tf_01 ../pride-and-prejudice.txt