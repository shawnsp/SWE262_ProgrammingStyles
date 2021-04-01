package test_262P;

import org.json.*;
import org.json.junit.Util;
import org.junit.Assert;
import org.junit.Test;
import java.io.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.function.Consumer;
import java.util.function.Function;

import static org.junit.Assert.*;


/**
 * Tests for JSON-Java XML.java methods:
 * 1) public static JSONObject toJSONObject(Reader reader, JSONPointer path)
 * 2) public static JSONObject toJSONObject(Reader reader, JSONPointer path, JSONObject replacement)
 */


public class XMLTest {
    /**
     * test on "JSONObject toJSONObject(Reader reader, JSONPointer path)"
     * expect return a valid JSONObject
     */
    @Test
    public void testExtractJSONObject() {
        try {
            FileReader reader = new FileReader("src/test/java/test_262P/books.xml");
            JSONPointer path = new JSONPointer("/catalog/book");

            JSONObject actual = XML.toJSONObject(reader, path);
//            System.out.println("here " + actual);
            reader.close();

            String expect = "{\"book\": [\n" +
                    "    {\n" +
                    "        \"author\": \"Gambardella, Matthew\",\n" +
                    "        \"price\": 44.95,\n" +
                    "        \"genre\": \"Computer\",\n" +
                    "        \"description\": \"An in-depth look at creating applications \\n      with XML.\",\n" +
                    "        \"id\": \"bk101\",\n" +
                    "        \"title\": \"XML Developer's Guide\",\n" +
                    "        \"publish_date\": \"2000-10-01\"\n" +
                    "    },\n" +
                    "    {\n" +
                    "        \"author\": \"Ralls, Kim\",\n" +
                    "        \"price\": 5.95,\n" +
                    "        \"genre\": \"Fantasy\",\n" +
                    "        \"description\": \"A former architect battles corporate zombies, \\n      an evil sorceress, and her own childhood to become queen \\n      of the world.\",\n" +
                    "        \"id\": \"bk102\",\n" +
                    "        \"title\": \"Midnight Rain\",\n" +
                    "        \"publish_date\": \"2000-12-16\"\n" +
                    "    },\n" +
                    "    {\n" +
                    "        \"author\": \"Corets, Eva\",\n" +
                    "        \"price\": 5.95,\n" +
                    "        \"genre\": \"Fantasy\",\n" +
                    "        \"description\": \"After the collapse of a nanotechnology \\n      society in England, the young survivors lay the \\n      foundation for a new society.\",\n" +
                    "        \"id\": \"bk103\",\n" +
                    "        \"title\": \"Maeve Ascendant\",\n" +
                    "        \"publish_date\": \"2000-11-17\"\n" +
                    "    },\n" +
                    "    {\n" +
                    "        \"author\": \"Corets, Eva\",\n" +
                    "        \"price\": 5.95,\n" +
                    "        \"genre\": \"Fantasy\",\n" +
                    "        \"description\": \"In post-apocalypse England, the mysterious \\n      agent known only as Oberon helps to create a new life \\n      for the inhabitants of London. Sequel to Maeve \\n      Ascendant.\",\n" +
                    "        \"id\": \"bk104\",\n" +
                    "        \"title\": \"Oberon's Legacy\",\n" +
                    "        \"publish_date\": \"2001-03-10\"\n" +
                    "    },\n" +
                    "    {\n" +
                    "        \"author\": \"Corets, Eva\",\n" +
                    "        \"price\": 5.95,\n" +
                    "        \"genre\": \"Fantasy\",\n" +
                    "        \"description\": \"The two daughters of Maeve, half-sisters, \\n      battle one another for control of England. Sequel to \\n      Oberon's Legacy.\",\n" +
                    "        \"id\": \"bk105\",\n" +
                    "        \"title\": \"The Sundered Grail\",\n" +
                    "        \"publish_date\": \"2001-09-10\"\n" +
                    "    },\n" +
                    "    {\n" +
                    "        \"author\": \"Randall, Cynthia\",\n" +
                    "        \"price\": 4.95,\n" +
                    "        \"genre\": \"Romance\",\n" +
                    "        \"description\": \"When Carla meets Paul at an ornithology \\n      conference, tempers fly as feathers get ruffled.\",\n" +
                    "        \"id\": \"bk106\",\n" +
                    "        \"title\": \"Lover Birds\",\n" +
                    "        \"publish_date\": \"2000-09-02\"\n" +
                    "    },\n" +
                    "    {\n" +
                    "        \"author\": \"Thurman, Paula\",\n" +
                    "        \"price\": 4.95,\n" +
                    "        \"genre\": \"Romance\",\n" +
                    "        \"description\": \"A deep sea diver finds true love twenty \\n      thousand leagues beneath the sea.\",\n" +
                    "        \"id\": \"bk107\",\n" +
                    "        \"title\": \"Splish Splash\",\n" +
                    "        \"publish_date\": \"2000-11-02\"\n" +
                    "    },\n" +
                    "    {\n" +
                    "        \"author\": \"Knorr, Stefan\",\n" +
                    "        \"price\": 4.95,\n" +
                    "        \"genre\": \"Horror\",\n" +
                    "        \"description\": \"An anthology of horror stories about roaches,\\n      centipedes, scorpions  and other insects.\",\n" +
                    "        \"id\": \"bk108\",\n" +
                    "        \"title\": \"Creepy Crawlies\",\n" +
                    "        \"publish_date\": \"2000-12-06\"\n" +
                    "    },\n" +
                    "    {\n" +
                    "        \"author\": \"Kress, Peter\",\n" +
                    "        \"price\": 6.95,\n" +
                    "        \"genre\": \"Science Fiction\",\n" +
                    "        \"description\": \"After an inadvertant trip through a Heisenberg\\n      Uncertainty Device, James Salway discovers the problems \\n      of being quantum.\",\n" +
                    "        \"id\": \"bk109\",\n" +
                    "        \"title\": \"Paradox Lost\",\n" +
                    "        \"publish_date\": \"2000-11-02\"\n" +
                    "    },\n" +
                    "    {\n" +
                    "        \"author\": \"O'Brien, Tim\",\n" +
                    "        \"price\": 36.95,\n" +
                    "        \"genre\": \"Computer\",\n" +
                    "        \"description\": \"Microsoft's .NET initiative is explored in \\n      detail in this deep programmer's reference.\",\n" +
                    "        \"id\": \"bk110\",\n" +
                    "        \"title\": \"Microsoft .NET: The Programming Bible\",\n" +
                    "        \"publish_date\": \"2000-12-09\"\n" +
                    "    },\n" +
                    "    {\n" +
                    "        \"author\": \"O'Brien, Tim\",\n" +
                    "        \"price\": 36.95,\n" +
                    "        \"genre\": \"Computer\",\n" +
                    "        \"description\": \"The Microsoft MSXML3 parser is covered in \\n      detail, with attention to XML DOM interfaces, XSLT processing, \\n      SAX and more.\",\n" +
                    "        \"id\": \"bk111\",\n" +
                    "        \"title\": \"MSXML3: A Comprehensive Guide\",\n" +
                    "        \"publish_date\": \"2000-12-01\"\n" +
                    "    },\n" +
                    "    {\n" +
                    "        \"author\": \"Galos, Mike\",\n" +
                    "        \"price\": 49.95,\n" +
                    "        \"genre\": \"Computer\",\n" +
                    "        \"description\": \"Microsoft Visual Studio 7 is explored in depth,\\n      looking at how Visual Basic, Visual C++, C#, and ASP+ are \\n      integrated into a comprehensive development \\n      environment.\",\n" +
                    "        \"id\": \"bk112\",\n" +
                    "        \"title\": \"Visual Studio 7: A Comprehensive Guide\",\n" +
                    "        \"publish_date\": \"2001-04-16\"\n" +
                    "    }\n" +
                    "]}";

            Util.compareActualVsExpectedJsonObjects(actual, new JSONObject(expect));

        } catch (FileNotFoundException e) {
            System.out.println("File not found.");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Fail to close file.");
            e.printStackTrace();
        }
    }


    /**
     * test on "JSONObject toJSONObject(Reader reader, JSONPointer path, JSONObject replacement)"
     * expect return a new JSONObject
     */
    @Test
    public void testReplaceJSONObject() {
        try {
            FileReader reader = new FileReader("src/test/java/test_262P/books.xml");
            JSONPointer path = new JSONPointer("/catalog/book");
            JSONObject newSubObject = new JSONObject();
            newSubObject.put("author", "NA");
            newSubObject.put("price", "NA");
            newSubObject.put("genre", "NA");
            newSubObject.put("description", "NA");
            newSubObject.put("id", "NA");
            newSubObject.put("title", "NA");
            newSubObject.put("publish_date", "NA");
            JSONObject actual = XML.toJSONObject(reader, path, newSubObject);

            String actualString = actual.toString();
            String expectString = "{\"catalog\":{\"book\":{\"author\":\"NA\",\"price\":\"NA\",\"genre\":\"NA\",\"description\":\"NA\",\"id\":[\"bk101\",\"NA\"],\"title\":\"NA\",\"publish_date\":\"NA\"}}}";
            assertEquals(actualString, expectString);
            reader.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found.");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Fail to close file.");
            e.printStackTrace();
        }
    }

    /**
     * test on "JSONObject toJSONObject(Reader reader, JSONPointer path, JSONObject replacement)"
     * expect actual and expect JSONObject is different
     */
    @Test
    public void testReplaceJSONObjectWithInvalidPath() {
        try {
            FileReader reader = new FileReader("src/test/java/test_262P/books.xml");
            JSONPointer path = new JSONPointer("/catalog/test");
            JSONObject newSubObject = new JSONObject();
            newSubObject.put("author", "NA");
            newSubObject.put("price", "NA");
            newSubObject.put("genre", "NA");
            newSubObject.put("description", "NA");
            newSubObject.put("id", "NA");
            newSubObject.put("title", "NA");
            newSubObject.put("publish_date", "NA");
            JSONObject actual = XML.toJSONObject(reader, path, newSubObject);

            String actualString = actual.toString();
            String expectString = "{\"catalog\":{\"book\":{\"author\":\"NA\",\"price\":\"NA\",\"genre\":\"NA\",\"description\":\"NA\",\"id\":[\"bk101\",\"NA\"],\"title\":\"NA\",\"publish_date\":\"NA\"}}}";
            assertNotEquals(actualString, expectString);
        } catch (FileNotFoundException e) {
            System.out.println("File not found.");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Fail to close file.");
            e.printStackTrace();
        }
    }

    //    ---------------------------- Milestone 3 ------------------------------
    /**
     * test on "JSONObject toJSONObject(Reader reader, Function<String, String> keyTransformer)"
     * add prefix to the key
     */
    @Test
    public void testPrefixTransformJSONObjectKey() {
        try {
            FileReader reader = new FileReader("src/test/java/test_262P/books.xml");
            Function<String, String> keyTransformer = (key) -> "swe262_" + key;
            JSONObject actual = XML.toJSONObject(reader, keyTransformer);
            reader.close();
            String expect = "{\"swe262_catalog\": {\"swe262_book\": [\n" +
                    "    {\n" +
                    "        \"swe262_author\": \"Gambardella, Matthew\",\n" +
                    "        \"swe262_price\": 44.95,\n" +
                    "        \"swe262_genre\": \"Computer\",\n" +
                    "        \"swe262_description\": \"An in-depth look at creating applications \\n      with XML.\",\n" +
                    "        \"swe262_id\": \"bk101\",\n" +
                    "        \"swe262_title\": \"XML Developer's Guide\",\n" +
                    "        \"swe262_publish_date\": \"2000-10-01\"\n" +
                    "    },\n" +
                    "    {\n" +
                    "        \"swe262_author\": \"Ralls, Kim\",\n" +
                    "        \"swe262_price\": 5.95,\n" +
                    "        \"swe262_genre\": \"Fantasy\",\n" +
                    "        \"swe262_description\": \"A former architect battles corporate zombies, \\n      an evil sorceress, and her own childhood to become queen \\n      of the world.\",\n" +
                    "        \"swe262_id\": \"bk102\",\n" +
                    "        \"swe262_title\": \"Midnight Rain\",\n" +
                    "        \"swe262_publish_date\": \"2000-12-16\"\n" +
                    "    },\n" +
                    "    {\n" +
                    "        \"swe262_author\": \"Corets, Eva\",\n" +
                    "        \"swe262_price\": 5.95,\n" +
                    "        \"swe262_genre\": \"Fantasy\",\n" +
                    "        \"swe262_description\": \"After the collapse of a nanotechnology \\n      society in England, the young survivors lay the \\n      foundation for a new society.\",\n" +
                    "        \"swe262_id\": \"bk103\",\n" +
                    "        \"swe262_title\": \"Maeve Ascendant\",\n" +
                    "        \"swe262_publish_date\": \"2000-11-17\"\n" +
                    "    },\n" +
                    "    {\n" +
                    "        \"swe262_author\": \"Corets, Eva\",\n" +
                    "        \"swe262_price\": 5.95,\n" +
                    "        \"swe262_genre\": \"Fantasy\",\n" +
                    "        \"swe262_description\": \"In post-apocalypse England, the mysterious \\n      agent known only as Oberon helps to create a new life \\n      for the inhabitants of London. Sequel to Maeve \\n      Ascendant.\",\n" +
                    "        \"swe262_id\": \"bk104\",\n" +
                    "        \"swe262_title\": \"Oberon's Legacy\",\n" +
                    "        \"swe262_publish_date\": \"2001-03-10\"\n" +
                    "    },\n" +
                    "    {\n" +
                    "        \"swe262_author\": \"Corets, Eva\",\n" +
                    "        \"swe262_price\": 5.95,\n" +
                    "        \"swe262_genre\": \"Fantasy\",\n" +
                    "        \"swe262_description\": \"The two daughters of Maeve, half-sisters, \\n      battle one another for control of England. Sequel to \\n      Oberon's Legacy.\",\n" +
                    "        \"swe262_id\": \"bk105\",\n" +
                    "        \"swe262_title\": \"The Sundered Grail\",\n" +
                    "        \"swe262_publish_date\": \"2001-09-10\"\n" +
                    "    },\n" +
                    "    {\n" +
                    "        \"swe262_author\": \"Randall, Cynthia\",\n" +
                    "        \"swe262_price\": 4.95,\n" +
                    "        \"swe262_genre\": \"Romance\",\n" +
                    "        \"swe262_description\": \"When Carla meets Paul at an ornithology \\n      conference, tempers fly as feathers get ruffled.\",\n" +
                    "        \"swe262_id\": \"bk106\",\n" +
                    "        \"swe262_title\": \"Lover Birds\",\n" +
                    "        \"swe262_publish_date\": \"2000-09-02\"\n" +
                    "    },\n" +
                    "    {\n" +
                    "        \"swe262_author\": \"Thurman, Paula\",\n" +
                    "        \"swe262_price\": 4.95,\n" +
                    "        \"swe262_genre\": \"Romance\",\n" +
                    "        \"swe262_description\": \"A deep sea diver finds true love twenty \\n      thousand leagues beneath the sea.\",\n" +
                    "        \"swe262_id\": \"bk107\",\n" +
                    "        \"swe262_title\": \"Splish Splash\",\n" +
                    "        \"swe262_publish_date\": \"2000-11-02\"\n" +
                    "    },\n" +
                    "    {\n" +
                    "        \"swe262_author\": \"Knorr, Stefan\",\n" +
                    "        \"swe262_price\": 4.95,\n" +
                    "        \"swe262_genre\": \"Horror\",\n" +
                    "        \"swe262_description\": \"An anthology of horror stories about roaches,\\n      centipedes, scorpions  and other insects.\",\n" +
                    "        \"swe262_id\": \"bk108\",\n" +
                    "        \"swe262_title\": \"Creepy Crawlies\",\n" +
                    "        \"swe262_publish_date\": \"2000-12-06\"\n" +
                    "    },\n" +
                    "    {\n" +
                    "        \"swe262_author\": \"Kress, Peter\",\n" +
                    "        \"swe262_price\": 6.95,\n" +
                    "        \"swe262_genre\": \"Science Fiction\",\n" +
                    "        \"swe262_description\": \"After an inadvertant trip through a Heisenberg\\n      Uncertainty Device, James Salway discovers the problems \\n      of being quantum.\",\n" +
                    "        \"swe262_id\": \"bk109\",\n" +
                    "        \"swe262_title\": \"Paradox Lost\",\n" +
                    "        \"swe262_publish_date\": \"2000-11-02\"\n" +
                    "    },\n" +
                    "    {\n" +
                    "        \"swe262_author\": \"O'Brien, Tim\",\n" +
                    "        \"swe262_price\": 36.95,\n" +
                    "        \"swe262_genre\": \"Computer\",\n" +
                    "        \"swe262_description\": \"Microsoft's .NET initiative is explored in \\n      detail in this deep programmer's reference.\",\n" +
                    "        \"swe262_id\": \"bk110\",\n" +
                    "        \"swe262_title\": \"Microsoft .NET: The Programming Bible\",\n" +
                    "        \"swe262_publish_date\": \"2000-12-09\"\n" +
                    "    },\n" +
                    "    {\n" +
                    "        \"swe262_author\": \"O'Brien, Tim\",\n" +
                    "        \"swe262_price\": 36.95,\n" +
                    "        \"swe262_genre\": \"Computer\",\n" +
                    "        \"swe262_description\": \"The Microsoft MSXML3 parser is covered in \\n      detail, with attention to XML DOM interfaces, XSLT processing, \\n      SAX and more.\",\n" +
                    "        \"swe262_id\": \"bk111\",\n" +
                    "        \"swe262_title\": \"MSXML3: A Comprehensive Guide\",\n" +
                    "        \"swe262_publish_date\": \"2000-12-01\"\n" +
                    "    },\n" +
                    "    {\n" +
                    "        \"swe262_author\": \"Galos, Mike\",\n" +
                    "        \"swe262_price\": 49.95,\n" +
                    "        \"swe262_genre\": \"Computer\",\n" +
                    "        \"swe262_description\": \"Microsoft Visual Studio 7 is explored in depth,\\n      looking at how Visual Basic, Visual C++, C#, and ASP+ are \\n      integrated into a comprehensive development \\n      environment.\",\n" +
                    "        \"swe262_id\": \"bk112\",\n" +
                    "        \"swe262_title\": \"Visual Studio 7: A Comprehensive Guide\",\n" +
                    "        \"swe262_publish_date\": \"2001-04-16\"\n" +
                    "    }\n" +
                    "]}}";

            Util.compareActualVsExpectedJsonObjects(actual, new JSONObject(expect));

        } catch (FileNotFoundException e) {
            System.out.println("File not found.");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Fail to close file.");
            e.printStackTrace();
        }
    }

    /**
     * test on "JSONObject toJSONObject(Reader reader, Function<String, String> keyTransformer)"
     * add postfix to the key
     */
    @Test
    public void testPostfixTransformJSONObjectKey() {
        try {
            FileReader reader = new FileReader("src/test/java/test_262P/books.xml");
            Function<String, String> keyTransformer = (key) -> key + "_swe262";
            JSONObject actual = XML.toJSONObject(reader, keyTransformer);
            reader.close();
            String expect = "{\"catalog_swe262\": {\"book_swe262\": [\n" +
                    "    {\n" +
                    "        \"author_swe262\": \"Gambardella, Matthew\",\n" +
                    "        \"price_swe262\": 44.95,\n" +
                    "        \"genre_swe262\": \"Computer\",\n" +
                    "        \"description_swe262\": \"An in-depth look at creating applications \\n      with XML.\",\n" +
                    "        \"id_swe262\": \"bk101\",\n" +
                    "        \"title_swe262\": \"XML Developer's Guide\",\n" +
                    "        \"publish_date_swe262\": \"2000-10-01\"\n" +
                    "    },\n" +
                    "    {\n" +
                    "        \"author_swe262\": \"Ralls, Kim\",\n" +
                    "        \"price_swe262\": 5.95,\n" +
                    "        \"genre_swe262\": \"Fantasy\",\n" +
                    "        \"description_swe262\": \"A former architect battles corporate zombies, \\n      an evil sorceress, and her own childhood to become queen \\n      of the world.\",\n" +
                    "        \"id_swe262\": \"bk102\",\n" +
                    "        \"title_swe262\": \"Midnight Rain\",\n" +
                    "        \"publish_date_swe262\": \"2000-12-16\"\n" +
                    "    },\n" +
                    "    {\n" +
                    "        \"author_swe262\": \"Corets, Eva\",\n" +
                    "        \"price_swe262\": 5.95,\n" +
                    "        \"genre_swe262\": \"Fantasy\",\n" +
                    "        \"description_swe262\": \"After the collapse of a nanotechnology \\n      society in England, the young survivors lay the \\n      foundation for a new society.\",\n" +
                    "        \"id_swe262\": \"bk103\",\n" +
                    "        \"title_swe262\": \"Maeve Ascendant\",\n" +
                    "        \"publish_date_swe262\": \"2000-11-17\"\n" +
                    "    },\n" +
                    "    {\n" +
                    "        \"author_swe262\": \"Corets, Eva\",\n" +
                    "        \"price_swe262\": 5.95,\n" +
                    "        \"genre_swe262\": \"Fantasy\",\n" +
                    "        \"description_swe262\": \"In post-apocalypse England, the mysterious \\n      agent known only as Oberon helps to create a new life \\n      for the inhabitants of London. Sequel to Maeve \\n      Ascendant.\",\n" +
                    "        \"id_swe262\": \"bk104\",\n" +
                    "        \"title_swe262\": \"Oberon's Legacy\",\n" +
                    "        \"publish_date_swe262\": \"2001-03-10\"\n" +
                    "    },\n" +
                    "    {\n" +
                    "        \"author_swe262\": \"Corets, Eva\",\n" +
                    "        \"price_swe262\": 5.95,\n" +
                    "        \"genre_swe262\": \"Fantasy\",\n" +
                    "        \"description_swe262\": \"The two daughters of Maeve, half-sisters, \\n      battle one another for control of England. Sequel to \\n      Oberon's Legacy.\",\n" +
                    "        \"id_swe262\": \"bk105\",\n" +
                    "        \"title_swe262\": \"The Sundered Grail\",\n" +
                    "        \"publish_date_swe262\": \"2001-09-10\"\n" +
                    "    },\n" +
                    "    {\n" +
                    "        \"author_swe262\": \"Randall, Cynthia\",\n" +
                    "        \"price_swe262\": 4.95,\n" +
                    "        \"genre_swe262\": \"Romance\",\n" +
                    "        \"description_swe262\": \"When Carla meets Paul at an ornithology \\n      conference, tempers fly as feathers get ruffled.\",\n" +
                    "        \"id_swe262\": \"bk106\",\n" +
                    "        \"title_swe262\": \"Lover Birds\",\n" +
                    "        \"publish_date_swe262\": \"2000-09-02\"\n" +
                    "    },\n" +
                    "    {\n" +
                    "        \"author_swe262\": \"Thurman, Paula\",\n" +
                    "        \"price_swe262\": 4.95,\n" +
                    "        \"genre_swe262\": \"Romance\",\n" +
                    "        \"description_swe262\": \"A deep sea diver finds true love twenty \\n      thousand leagues beneath the sea.\",\n" +
                    "        \"id_swe262\": \"bk107\",\n" +
                    "        \"title_swe262\": \"Splish Splash\",\n" +
                    "        \"publish_date_swe262\": \"2000-11-02\"\n" +
                    "    },\n" +
                    "    {\n" +
                    "        \"author_swe262\": \"Knorr, Stefan\",\n" +
                    "        \"price_swe262\": 4.95,\n" +
                    "        \"genre_swe262\": \"Horror\",\n" +
                    "        \"description_swe262\": \"An anthology of horror stories about roaches,\\n      centipedes, scorpions  and other insects.\",\n" +
                    "        \"id_swe262\": \"bk108\",\n" +
                    "        \"title_swe262\": \"Creepy Crawlies\",\n" +
                    "        \"publish_date_swe262\": \"2000-12-06\"\n" +
                    "    },\n" +
                    "    {\n" +
                    "        \"author_swe262\": \"Kress, Peter\",\n" +
                    "        \"price_swe262\": 6.95,\n" +
                    "        \"genre_swe262\": \"Science Fiction\",\n" +
                    "        \"description_swe262\": \"After an inadvertant trip through a Heisenberg\\n      Uncertainty Device, James Salway discovers the problems \\n      of being quantum.\",\n" +
                    "        \"id_swe262\": \"bk109\",\n" +
                    "        \"title_swe262\": \"Paradox Lost\",\n" +
                    "        \"publish_date_swe262\": \"2000-11-02\"\n" +
                    "    },\n" +
                    "    {\n" +
                    "        \"author_swe262\": \"O'Brien, Tim\",\n" +
                    "        \"price_swe262\": 36.95,\n" +
                    "        \"genre_swe262\": \"Computer\",\n" +
                    "        \"description_swe262\": \"Microsoft's .NET initiative is explored in \\n      detail in this deep programmer's reference.\",\n" +
                    "        \"id_swe262\": \"bk110\",\n" +
                    "        \"title_swe262\": \"Microsoft .NET: The Programming Bible\",\n" +
                    "        \"publish_date_swe262\": \"2000-12-09\"\n" +
                    "    },\n" +
                    "    {\n" +
                    "        \"author_swe262\": \"O'Brien, Tim\",\n" +
                    "        \"price_swe262\": 36.95,\n" +
                    "        \"genre_swe262\": \"Computer\",\n" +
                    "        \"description_swe262\": \"The Microsoft MSXML3 parser is covered in \\n      detail, with attention to XML DOM interfaces, XSLT processing, \\n      SAX and more.\",\n" +
                    "        \"id_swe262\": \"bk111\",\n" +
                    "        \"title_swe262\": \"MSXML3: A Comprehensive Guide\",\n" +
                    "        \"publish_date_swe262\": \"2000-12-01\"\n" +
                    "    },\n" +
                    "    {\n" +
                    "        \"author_swe262\": \"Galos, Mike\",\n" +
                    "        \"price_swe262\": 49.95,\n" +
                    "        \"genre_swe262\": \"Computer\",\n" +
                    "        \"description_swe262\": \"Microsoft Visual Studio 7 is explored in depth,\\n      looking at how Visual Basic, Visual C++, C#, and ASP+ are \\n      integrated into a comprehensive development \\n      environment.\",\n" +
                    "        \"id_swe262\": \"bk112\",\n" +
                    "        \"title_swe262\": \"Visual Studio 7: A Comprehensive Guide\",\n" +
                    "        \"publish_date_swe262\": \"2001-04-16\"\n" +
                    "    }\n" +
                    "]}}";

            Util.compareActualVsExpectedJsonObjects(actual, new JSONObject(expect));

        } catch (FileNotFoundException e) {
            System.out.println("File not found.");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Fail to close file.");
            e.printStackTrace();
        }
    }

    /**
     * test on "JSONObject toJSONObject(Reader reader, Function<String, String> keyTransformer)"
     * with a different input file (albums.xml)
     */
    @Test
    public void testTransformJSONObjectKeyDifferentFile() {
        try {
            FileReader reader = new FileReader("src/test/java/test_262P/albums.xml");
            Function<String, String> keyTransformer = (key) -> "swe262_" + key;
            JSONObject actual = XML.toJSONObject(reader, keyTransformer);
            reader.close();
            String expect = "{\"swe262_CATALOG\": {\"swe262_CD\": [\n" +
                    "    {\n" +
                    "        \"swe262_COMPANY\": \"tseries\",\n" +
                    "        \"swe262_PRICE\": 10.90,\n" +
                    "        \"swe262_ARTIST\": \"Arijit singh\",\n" +
                    "        \"swe262_COUNTRY\": \"India\",\n" +
                    "        \"swe262_YEAR\": 2018,\n" +
                    "        \"swe262_TITLE\": \"dill diya galla\"\n" +
                    "    },\n" +
                    "    {\n" +
                    "        \"swe262_COMPANY\": \"Records\",\n" +
                    "        \"swe262_PRICE\": 9.90,\n" +
                    "        \"swe262_ARTIST\": \"Atif Aslam\",\n" +
                    "        \"swe262_COUNTRY\": \"Uk\",\n" +
                    "        \"swe262_YEAR\": 2015,\n" +
                    "        \"swe262_TITLE\": \"Saiyara\"\n" +
                    "    },\n" +
                    "    {\n" +
                    "        \"swe262_COMPANY\": \"radio\",\n" +
                    "        \"swe262_PRICE\": 9.90,\n" +
                    "        \"swe262_ARTIST\": \"Sonu nigam\",\n" +
                    "        \"swe262_COUNTRY\": \"india\",\n" +
                    "        \"swe262_YEAR\": 2019,\n" +
                    "        \"swe262_TITLE\": \"Khairiyat\"\n" +
                    "    },\n" +
                    "    {\n" +
                    "        \"swe262_COMPANY\": \"Virgin records\",\n" +
                    "        \"swe262_PRICE\": 10.20,\n" +
                    "        \"swe262_ARTIST\": \"Amir Khan\",\n" +
                    "        \"swe262_COUNTRY\": \"pak\",\n" +
                    "        \"swe262_YEAR\": 2012,\n" +
                    "        \"swe262_TITLE\": \"all is well\"\n" +
                    "    },\n" +
                    "    {\n" +
                    "        \"swe262_COMPANY\": \"Eros\",\n" +
                    "        \"swe262_PRICE\": 9.90,\n" +
                    "        \"swe262_ARTIST\": \"kk\",\n" +
                    "        \"swe262_COUNTRY\": \"india\",\n" +
                    "        \"swe262_YEAR\": 2014,\n" +
                    "        \"swe262_TITLE\": \"rockstar\"\n" +
                    "    }\n" +
                    "]}}";

            Util.compareActualVsExpectedJsonObjects(actual, new JSONObject(expect));

        } catch (FileNotFoundException e) {
            System.out.println("File not found.");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Fail to close file.");
            e.printStackTrace();
        }
    }

    /**
     * test on "JSONObject toJSONObject(Reader reader, Function<String, String> keyTransformer)"
     * with a different input file (planes.xml)
     */
    @Test
    public void testTransformJSONObjectKeyDifferentFile2() {
        try {
            FileReader reader = new FileReader("src/test/java/test_262P/planes.xml");
            Function<String, String> keyTransformer = (key) -> "swe262_" + key;
            JSONObject actual = XML.toJSONObject(reader, keyTransformer);
            reader.close();
            String expect = "{\"swe262_planes_for_sale\": {\"swe262_ad\": [\n" +
                    "    {\n" +
                    "        \"swe262_location\": {\n" +
                    "            \"swe262_city\": \"Rapid City,\",\n" +
                    "            \"swe262_state\": \"South Dakota\"\n" +
                    "        },\n" +
                    "        \"swe262_price\": \"23,495\",\n" +
                    "        \"swe262_make\": \"&c;\",\n" +
                    "        \"swe262_seller\": {\n" +
                    "            \"phone\": \"555-222-3333\",\n" +
                    "            \"content\": \"Skyway Aircraft\"\n" +
                    "        },\n" +
                    "        \"swe262_model\": \"Skyhawk\",\n" +
                    "        \"swe262_description\": \"New paint, nearly new interior,\\n            685 hours SMOH, full IFR King avionics\",\n" +
                    "        \"swe262_year\": 1977,\n" +
                    "        \"swe262_color\": \"Light blue and white\"\n" +
                    "    },\n" +
                    "    {\n" +
                    "        \"swe262_location\": {\n" +
                    "            \"swe262_city\": \"St. Joseph,\",\n" +
                    "            \"swe262_state\": \"Missouri\"\n" +
                    "        },\n" +
                    "        \"swe262_make\": \"&p;\",\n" +
                    "        \"swe262_seller\": {\n" +
                    "            \"phone\": \"555-333-2222\",\n" +
                    "            \"email\": \"jseller@www.axl.com\",\n" +
                    "            \"content\": \"John Seller\"\n" +
                    "        },\n" +
                    "        \"swe262_model\": \"Cherokee\",\n" +
                    "        \"swe262_description\": \"240 hours SMOH, dual NAVCOMs, DME, \\n                new Cleveland brakes, great shape\",\n" +
                    "        \"swe262_year\": 1965,\n" +
                    "        \"swe262_color\": \"Gold\"\n" +
                    "    }\n" +
                    "]}}";

            Util.compareActualVsExpectedJsonObjects(actual, new JSONObject(expect));

        } catch (FileNotFoundException e) {
            System.out.println("File not found.");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Fail to close file.");
            e.printStackTrace();
        }
    }


    //    ---------------------------- Milestone 5 ------------------------------
    /**
     * test on "Future<JSONObject> toJSONObject(Reader reader, Function<String, String> keyTransformer, Consumer<Exception> exceptionHandler)"
     * expect to return correct output
     */
    @Test
    public void testTransformJSONObjectKeyAsyn(){
        try {
            FileReader reader = new FileReader("src/test/java/test_262P/planes.xml");
            Function<String, String> keyTransformer = (key) -> "swe262_" + key;
            Consumer<Exception> exceptionHandler = (e) -> e.printStackTrace();

            Future<JSONObject> futureActual = XML.toJSONObject(reader, keyTransformer, exceptionHandler);
            String expect = "{\"swe262_planes_for_sale\": {\"swe262_ad\": [\n" +
                    "    {\n" +
                    "        \"swe262_location\": {\n" +
                    "            \"swe262_city\": \"Rapid City,\",\n" +
                    "            \"swe262_state\": \"South Dakota\"\n" +
                    "        },\n" +
                    "        \"swe262_price\": \"23,495\",\n" +
                    "        \"swe262_make\": \"&c;\",\n" +
                    "        \"swe262_seller\": {\n" +
                    "            \"phone\": \"555-222-3333\",\n" +
                    "            \"content\": \"Skyway Aircraft\"\n" +
                    "        },\n" +
                    "        \"swe262_model\": \"Skyhawk\",\n" +
                    "        \"swe262_description\": \"New paint, nearly new interior,\\n            685 hours SMOH, full IFR King avionics\",\n" +
                    "        \"swe262_year\": 1977,\n" +
                    "        \"swe262_color\": \"Light blue and white\"\n" +
                    "    },\n" +
                    "    {\n" +
                    "        \"swe262_location\": {\n" +
                    "            \"swe262_city\": \"St. Joseph,\",\n" +
                    "            \"swe262_state\": \"Missouri\"\n" +
                    "        },\n" +
                    "        \"swe262_make\": \"&p;\",\n" +
                    "        \"swe262_seller\": {\n" +
                    "            \"phone\": \"555-333-2222\",\n" +
                    "            \"email\": \"jseller@www.axl.com\",\n" +
                    "            \"content\": \"John Seller\"\n" +
                    "        },\n" +
                    "        \"swe262_model\": \"Cherokee\",\n" +
                    "        \"swe262_description\": \"240 hours SMOH, dual NAVCOMs, DME, \\n                new Cleveland brakes, great shape\",\n" +
                    "        \"swe262_year\": 1965,\n" +
                    "        \"swe262_color\": \"Gold\"\n" +
                    "    }\n" +
                    "]}}";

            // block the execution until the task is complete
            while(!futureActual.isDone()) {
                System.out.println("Calculating...");
                Thread.sleep(300);
            }

            JSONObject actual = futureActual.get();

            Util.compareActualVsExpectedJsonObjects(actual, new JSONObject(expect));
            reader.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found.");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Fail to close file.");
            e.printStackTrace();
        } catch (InterruptedException e) {
            System.out.println("Something wrong while blockign futureActual.isDone().");
            e.printStackTrace();
        } catch (ExecutionException e) {
            System.out.println("error found when executing futureActual.get().");
            e.printStackTrace();
        }
    }

    /**
     * test on "Future<JSONObject> toJSONObject(Reader reader, Function<String, String> keyTransformer, Consumer<Exception> exceptionHandler)"
     * expect futureActual.get() will return a JSONObject eventually
     */
    @Test
    public void testTransformJSONObjectKeyAsynReturnType() {
        try {
            FileReader reader = new FileReader("src/test/java/test_262P/planes.xml");
            Function<String, String> keyTransformer = (key) -> "swe262_" + key;
            Consumer<Exception> exceptionHandler = (e) -> e.printStackTrace();

            Future<JSONObject> futureActual = XML.toJSONObject(reader, keyTransformer, exceptionHandler);

            // block the execution until the task is complete
            while (!futureActual.isDone()) {
                System.out.println("Calculating...");
                Thread.sleep(300);
            }

            JSONObject actual = futureActual.get();

            assertTrue(actual instanceof JSONObject);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * test on "Future<JSONObject> toJSONObject(Reader reader, Function<String, String> keyTransformer, Consumer<Exception> exceptionHandler)"
     * check the pass in exceptionHandler is working correctly
     * expect print "OMG ERROR!!" and futureActual return null
     */
    @Test
    public void testTransformJSONObjectKeyAsynExceptionHandler() {
        try {
            FileReader reader = new FileReader("src/test/java/test_262P/planes.xml");
            Consumer<Exception> exceptionHandler = (e) -> System.out.println("OMG ERROR!!");

            Future<JSONObject> futureActual = XML.toJSONObject(reader, null, exceptionHandler);
            assertNull(futureActual);
        } catch (FileNotFoundException e) {
                e.printStackTrace();
        }
    }

}