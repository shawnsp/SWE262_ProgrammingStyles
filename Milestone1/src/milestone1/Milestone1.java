package milestone1;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONPointer;
import org.json.XML;

public class Milestone1 {
	final static int PRETTY_PRINT_INDENT_FACTOR = 4;
	
	// Helper Functions:
	// 1. read an XML file thru command line into JSON object
	public static JSONObject readXmlFileToJsonObject(String xmlFile) {
		try {
			FileReader fr = new FileReader(new File(xmlFile));
			JSONObject rawJson = XML.toJSONObject(fr);
			fr.close();
			return rawJson;
		} catch (JSONException e) {
        	System.out.println("JSON converting failed.");
    	} catch (FileNotFoundException e) {
    		System.out.println("File not found.");
    		e.printStackTrace();
    	} catch (IOException e) {
            System.out.println("Write to file failing.");
            e.printStackTrace();
        } 
		return null;
	}
	
	// 2. write JSON text to .json file
	public static void writeJsonFile(String jsonFile, String jsonText) {
		try {
	        FileWriter myWriter = new FileWriter(jsonFile);
	        myWriter.write(jsonText);
	        myWriter.close();
        } catch (IOException e) {
            System.out.println("Write to file failing.");
            e.printStackTrace();
        } 
	}
	
	
	// Tasks:
	// 1. read an XML file thru command line into JSON object
	// write JSON object to a JSON file
	public static void xmlToJson(JSONObject rawJson, String jsonFile) {
		String prettyJson = rawJson.toString(PRETTY_PRINT_INDENT_FACTOR);
        System.out.println(prettyJson);
        writeJsonFile(jsonFile, prettyJson);
	}
	
	
	// 2. Read XML file into JSON object
	// extract some smaller sub-object inside, given a certain path
	// write that smaller object to a JSON file
	//
	// 3. Read XML file into JSON object
	// check if it has a certain key path (given in command line)
	// if so, write it to JSON file
	public static void xmlToSubJson(JSONObject rawJson, String jsonFile, String query) {
		JSONPointer pointer = new JSONPointer(query);
//        System.out.println(pointer);
        Object subRawJson = pointer.queryFrom(rawJson);
//        System.out.println(subRawJson);
        String jsonText = subRawJson.toString();
        System.out.println(jsonText);
        writeJsonFile(jsonFile, jsonText);
	}
	
	
	// 4. Read an XML file into JSON object
	// add prefix "swe262_" to all of its keys
	public static void xmlToJsonPrefix(JSONObject rawJson, String jsonFile) {
		Map<String, Object> originalMap = rawJson.toMap();
        Map<String, Object> newMap = addPrefix(originalMap);
        JSONObject newJson = new JSONObject(newMap);
		String jsonText = newJson.toString(PRETTY_PRINT_INDENT_FACTOR);
		System.out.println(jsonText);
		writeJsonFile(jsonFile, jsonText);
	}
	
	// recursively traverse each key
	public static Map<String, Object> addPrefix(Map<String, Object> map) {
		Set<Map.Entry<String,Object>> currentSet = map.entrySet();
		// base case 
		if (currentSet.size() == 1 && 
				!(currentSet.iterator().next().getValue() instanceof JSONObject) && 
				!(currentSet.iterator().next().getValue() instanceof JSONArray)){
			modifyKey(map);
		} else {
			// recursion
			for (String key : map.keySet()) {
				addPrefix(map);
				modifyKey(map);
			}
		}
		return map;
	}
	
	public static void modifyKey(Map<String, Object> map) {
        for (Map.Entry entry : map.entrySet()) {
            String newKey = "swe262_" + entry.getKey();
            Object value = entry.getValue();
            map.remove(entry.getKey());
            map.put(newKey, value);
        }
	}
	
	
	// 5. Read an XML file into JSON object
	// replace as sub-object on a certain key path with another JSON object
	// we construct, then write the result on a JSON file
	public static void xmlToJsonReplaceSub(JSONObject rawJson, String jsonFile, String keyPath) {
		JSONObject newSubObject = new JSONObject();
		newSubObject.put("author", "NA");
		newSubObject.put("price", "NA");
		newSubObject.put("genre", "NA");
		newSubObject.put("description", "NA");
		newSubObject.put("id", "NA");
		newSubObject.put("title", "NA");
		newSubObject.put("publish_date", "NA");
		Map<String, Object> newSubMap = newSubObject.toMap();

		if (keyPath.substring(0,1).equals("/"))
			keyPath = keyPath.substring(1);
		String[] keys = keyPath.split("/");
		
		Map<String, Object> m = rawJson.toMap();
		Map<String, Object> newM = recurse(m, keys, newSubMap);
		System.out.println(newM);
		
		JSONObject newJson = new JSONObject(newM);
		String jsonText = newJson.toString(PRETTY_PRINT_INDENT_FACTOR);
		writeJsonFile(jsonFile, jsonText);

	}
	
	// recursively get to end of the path and change the it 
	public static Map<String, Object> recurse(Map<String, Object> map, String[] keys, Map<String, Object> newSubMap) {
		//basecase - when there's only one key left in keys
		if (keys.length == 1)
			map.put(keys[0], newSubMap);
		else {
			// get rid off the top level key 
			String topLevelKey = keys[0];
			String[] newKeys = new String[keys.length-1];
			for (int i = 0; i < newKeys.length; i++) {
				newKeys[i] = keys[i+1];
			}
//			System.out.println(topLevelKey);
//			System.out.println(map.get(topLevelKey));
//			System.out.println(map.get(topLevelKey).getClass());
			recurse((Map<String, Object>)map.get(topLevelKey), newKeys, newSubMap);
		}
		return map;
	}
	
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		
//		String xmlFile = args[0];
		String xmlFile = "./books.xml";
		JSONObject rawJson = readXmlFileToJsonObject(xmlFile);
		
		// Task1
		System.out.println("Task1");
		String jsonFile1 = xmlFile.substring(0, xmlFile.lastIndexOf(".")) + "1.json";
        xmlToJson(rawJson, jsonFile1);
        
        // User provides 1 input - Task2 
        // User provides 2 inputs - Task3
        if (args.length < 2) {
        	System.out.println("Task2");
	        String jsonFile2 = xmlFile.substring(0, xmlFile.lastIndexOf(".")) + "2.json";
	        String keyPathQuery = "/catalog/book/1";
	        xmlToSubJson(rawJson, jsonFile2, keyPathQuery);
        } else if (args.length == 2) {
        	System.out.println("Task3");
        	String jsonFile3 = xmlFile.substring(0, xmlFile.lastIndexOf(".")) + "3.json";
        	String keyPathQuery = args[1];
        	xmlToSubJson(rawJson, jsonFile3, keyPathQuery);
        } else {
        	throw new IllegalArgumentException("This program can only accept 1 or 2 arguments.");
        }
        
        //Task4
        System.out.println("Task4");
    	String jsonFile4 = xmlFile.substring(0, xmlFile.lastIndexOf(".")) + "4.json";
        xmlToJsonPrefix(rawJson, jsonFile4);

        // Task5
        System.out.println("Task5");
        String jsonFile5 = xmlFile.substring(0, xmlFile.lastIndexOf(".")) + "5.json";
        String keyPath = "/catalog/book";
        xmlToJsonReplaceSub(rawJson, jsonFile5, keyPath);
	}
}




