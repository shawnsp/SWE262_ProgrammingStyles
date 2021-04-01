package test_262P;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class JSONObjectStreamTest {
    /**
     * test on "public Stream<Object> toStream()"
     * expect return a list of object in the JSONArray
     */
    @Test
    public void testGettingObjectInJSONArray() throws IOException {
        FileReader fileReader = new FileReader("src/test/java/test_262P/books.xml");
        JSONObject actualObject= XML.toJSONObject(fileReader);
        fileReader.close();

        List<Object> expectedPrice = new ArrayList<>();
        JSONArray bookArray = (JSONArray) (actualObject.query("/catalog/book"));
        for(Object book : bookArray.toList()){
            expectedPrice.add(((HashMap)book).get("price"));
        }

        List<Object> actualPrice = actualObject.toStream()
                .filter(node -> ((HashMap) node).keySet().iterator().next().toString().contains("/catalog/book"))
                .filter(node -> ((HashMap) node).keySet().iterator().next().toString().contains("/price"))
                .map(node -> new ArrayList<Object>(((HashMap) node).values()).get(0))
                .collect(Collectors.toList());

        assertEquals(expectedPrice, actualPrice);
    }

    /**
     * test on "public Stream<Object> toStream()"
     * expect return size of the JSONObject
     */
    @Test
    public void testGettingJSONObjectSize() throws IOException {
        FileReader fileReader = new FileReader("src/test/java/test_262P/books.xml");
        JSONObject actualObject= XML.toJSONObject(fileReader);
        fileReader.close();

        int expectedSize = 84;

        long actualSize = actualObject.toStream()
                .count();

        assertEquals(expectedSize, actualSize);
    }

    /**
     * test on "public Stream<Object> toStream()"
     * expect return size of JSONArray
     */
    @Test
    public void testGettingJSONArraySize() throws IOException {
        FileReader fileReader = new FileReader("src/test/java/test_262P/books.xml");
        JSONObject actualObject= XML.toJSONObject(fileReader);
        fileReader.close();

        JSONArray bookArray = (JSONArray) (actualObject.query("/catalog/book"));
        int expectedSize = bookArray.length();


        List<Object> listOfObjectKeys = actualObject.toStream()
                .map(node -> new ArrayList<Object>(((HashMap) node).keySet()).get(0))
                .collect(Collectors.toList());

        String[] lastObjectKeys = listOfObjectKeys.get(listOfObjectKeys.size()-1).toString().substring(1).split("/");
        // this supposed be the array index
        int actualSize = Integer.valueOf(lastObjectKeys[2]);

        assertEquals(expectedSize, actualSize);
    }


}
