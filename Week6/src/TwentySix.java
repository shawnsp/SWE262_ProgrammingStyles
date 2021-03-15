import java.io.File;
import java.io.FileNotFoundException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

public class TwentySix {
    // create database
    public static void create_db_schema(Connection connection) throws SQLException {
        Statement statement = connection.createStatement();

        statement.executeUpdate("DROP TABLE IF EXISTS documents;");
        statement.executeUpdate("CREATE TABLE documents (id INTEGER PRIMARY KEY AUTOINCREMENT, name);");

        statement.executeUpdate("DROP TABLE IF EXISTS words;");
        statement.executeUpdate("CREATE TABLE words (id, doc_id, value);");

        statement.executeUpdate("DROP TABLE IF EXISTS characters;");
        statement.executeUpdate("CREATE TABLE characters (id, word_id, value);");

        statement.close();
    }

    // load non-stopwords database
    public static void load_file_into_db(String targetFile, Connection connection) throws FileNotFoundException, SQLException {
        // read stopwords
        ArrayList<String> stopWords = new ArrayList<>();
        Scanner scStopwords = new Scanner(new File("../stop_words.txt"));
        String[] stopWordsArray = scStopwords.nextLine().split(",");
        for (String s: stopWordsArray)
            stopWords.add(s);

        // get non-stopwords
        ArrayList<String> words = new ArrayList<>();
        Scanner scWords = new Scanner(new File(targetFile));
        while (scWords.hasNextLine()) {
            String line = scWords.nextLine();
            String[] arrOfStr= line.split("[^A-Za-z0-9]+");
            for (String s: arrOfStr){
                s = s.toLowerCase();
                if (s != "" && s.length() >1  && !stopWords.contains(s))
                    words.add(s);
            }
        }

        scStopwords.close();
        scWords.close();

        // load database
        Statement statement = connection.createStatement();
        statement.execute("INSERT INTO documents (name) VALUES (\'" + targetFile + "\')");
        ResultSet resultSet = statement.executeQuery("SELECT id from documents WHERE name=\'" + targetFile + "\'");
        int doc_id = resultSet.getInt(1);

        int word_id;
        try{
            resultSet = statement.executeQuery("SELECT MAX(id) FROM words");
            word_id = resultSet.getInt(1);
        } catch(SQLException e){
            word_id = 0;
        }

        for (String word : words) {
            statement.executeUpdate("INSERT INTO words (id,doc_id,value) VALUES (\'" +
                    word_id + "\',\'" + doc_id + "\',\'" + word + "\');");

            int char_id = 0;
            for (char c : word.toCharArray()) {
                statement.executeUpdate("INSERT INTO characters (id,word_id,value) VALUES (\'" + char_id + "\',\'"
                        + word_id + "\', \'" + c + "\');");
                char_id++;
            }
            word_id++;
        }
        statement.close();
        connection.commit();
    }

    public static void main(String[] args) throws SQLException, FileNotFoundException {
//        String targetFile = args[0];
        String targetFile = "../pride-and-prejudice.txt";

        Connection connection = DriverManager.getConnection("jdbc:sqlite:26.db");
        connection.setAutoCommit(false);

        create_db_schema(connection);
        load_file_into_db(targetFile, connection);

        Statement statement = connection.createStatement();

        // print top 25
        ResultSet rsTop25 = statement.executeQuery("SELECT value, COUNT(*) as C FROM words GROUP BY value ORDER BY C DESC;");
        int count = 0;
        while (count < 25) {
            rsTop25.next();
            System.out.println(rsTop25.getString(1) + "  -  " + rsTop25.getInt(2));
            count++;

        }

        // count Unique words contain "z"
        String countWordContainsZ = "SELECT value, COUNT(DISTINCT value) as C FROM words WHERE value LIKE '%z%';";
        ResultSet rsWordsWithZ = statement.executeQuery(countWordContainsZ);
        System.out.println("\nThe count of unique words with z: " + rsWordsWithZ.getInt(2));

        connection.close();
    }
}
