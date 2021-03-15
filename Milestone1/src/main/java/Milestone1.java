import java.io.FileNotFoundException;
import java.io.FileReader;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import org.json.*;

public class Milestone1 {
	final static int PRETTY_PRINT_INDENT_FACTOR = 4;
	
	// Helper Functions:
	// 1. read an XML file thru command line into JSON object
	public static JSONObject readXmlFileToJsonObject(String xmlFile) {
		try {
			FileReader fr = new FileReader(xmlFile);
			JSONObject rawJson = XML.toJSONObject(fr);
			fr.close();
			return rawJson;
		} catch (JSONException e) {
        	System.out.println("JSON converting failed.");
    	} catch (FileNotFoundException e) {
    		System.out.println("File not found.");
    		e.printStackTrace();
    	} catch (IOException e) {
            System.out.println("Fail to read xml file.");
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
            System.out.println("Fail to write json file.");
            e.printStackTrace();
        } 
	}
	
	
	// Tasks:
	// 1. read an XML file thru command line into JSON object
	// write JSON object to a JSON file
	public static void xmlToJson(String xmlFile, String jsonFile) {
		JSONObject rawJson = readXmlFileToJsonObject(xmlFile);
		String prettyJson = rawJson.toString(PRETTY_PRINT_INDENT_FACTOR);
//        System.out.println("--- [xmlToJson] ---\n" + prettyJson);
        writeJsonFile(jsonFile, prettyJson);
	}
	
	
	// 2. Read XML file into JSON object
	// extract some smaller sub-object inside, given a certain path
	// write that smaller object to a JSON file
	//
	// 3. Read XML file into JSON object
	// check if it has a certain key path (given in command line)
	// if so, write it to JSON file
	public static void xmlToSubJson(String xmlFile, String jsonFile, String query) {
		JSONObject rawJson = readXmlFileToJsonObject(xmlFile);
		JSONPointer pointer;
		Object subRawJson = null;
		String jsonText;

		try {
			pointer = new JSONPointer(query);
			// declared as Object but in fact the class type is decided at runtime
			subRawJson = pointer.queryFrom(rawJson).getClass().cast(pointer.queryFrom(rawJson));
		} catch (JSONPointerException e) {
			e.printStackTrace();
		}

		if (subRawJson instanceof JSONObject)
			jsonText = ((JSONObject) subRawJson).toString(PRETTY_PRINT_INDENT_FACTOR);
		else if (subRawJson instanceof JSONArray)
			jsonText = ((JSONArray) subRawJson).toString(PRETTY_PRINT_INDENT_FACTOR);
		else
			jsonText = subRawJson.toString();
//        System.out.println(jsonText);
        writeJsonFile(jsonFile, jsonText);
	}
	
	
	// 4. Read an XML file into JSON object
	// add prefix "swe262_" to all of its keys
	public static void xmlToJsonPrefix(String xmlFile, String jsonFile) {
		JSONObject rawJson = readXmlFileToJsonObject(xmlFile);
		Map<String, Object> originalMap = rawJson.toMap();
        Map<String, Object> newMap = addPrefix(originalMap);
        JSONObject newJson = new JSONObject(newMap);
		String jsonText = newJson.toString(PRETTY_PRINT_INDENT_FACTOR);
//		System.out.println(jsonText);
		writeJsonFile(jsonFile, jsonText);
	}
	
	// recursively traverse each key and addPrefix
	public static Map<String, Object> addPrefix(Map<String, Object> map) {
		Set<Map.Entry<String,Object>> currentSet = map.entrySet();
//		System.out.println(currentSet);
//		System.out.println(currentSet.iterator().next());
//		System.out.println(currentSet.iterator().next().getValue());
//		System.out.println(currentSet.iterator().next().getValue().getClass());

		// base case - value is not an HashMap or ArrayList
		if (!(currentSet.iterator().next().getValue() instanceof HashMap)) {
			if (currentSet.iterator().next().getValue() instanceof ArrayList) {
				// base case - if the ArrayList doesn't contain HashMap
				// recursion - if the ArrayList contains HashMap
				Map<String, Object> newMap = new HashMap<>();
				for (int i = 0; i < ((ArrayList)currentSet.iterator().next().getValue()).size(); i++) {
					if (((ArrayList)currentSet.iterator().next().getValue()).get(i) instanceof HashMap)
						addPrefix((HashMap)((ArrayList<?>) currentSet.iterator().next().getValue()).get(i));
				}
			}
			modifyKey(map);
		}
		else {
			// recursion - value is an HashMap
			addPrefix((HashMap)currentSet.iterator().next().getValue());
			modifyKey(map);
		}
		return map;
	}

	// modify each key in given map
	public static void modifyKey(Map<String, Object> map) {
		Map<String, Object> newMap = new HashMap<>();
		Iterator<Map.Entry<String, Object>> iterator = map.entrySet().iterator();
		// iterate through the map to construct new map and remove entry at the same time
		while (iterator.hasNext()) {
			Map.Entry<String, Object> entry = iterator.next();
			String newKey = "swe262_" + entry.getKey();
			Object value = entry.getValue();
			iterator.remove();
			newMap.put(newKey, value);
		}
		// mapping newMap into map
		map.putAll(newMap);
	}

	
	// 5. Read an XML file into JSON object
	// replace as sub-object on a certain key path with another JSON object
	// we construct, then write the result on a JSON file
	public static void xmlToJsonReplaceSub(String xmlFile, String jsonFile, String keyPath) {
		JSONObject rawJson = readXmlFileToJsonObject(xmlFile);
		JSONObject newSubObject = new JSONObject();
		newSubObject.put("author", "NA");
		newSubObject.put("price", "NA");
		newSubObject.put("genre", "NA");
		newSubObject.put("description", "NA");
		newSubObject.put("id", "NA");
		newSubObject.put("title", "NA");
		newSubObject.put("publish_date", "NA");
		Map<String, Object> newSubMap = newSubObject.toMap();

		// Sample keyPath -> /catalog/book/1
		if (keyPath.substring(0,1).equals("/"))
			keyPath = keyPath.substring(1);
		String[] keys = keyPath.split("/");
		
		Map<String, Object> m = rawJson.toMap();
		Map<String, Object> newM = recurseTask5(m, keys, newSubMap);
		
		JSONObject newJson = new JSONObject(newM);
		String prettyJson = newJson.toString(PRETTY_PRINT_INDENT_FACTOR);
//		System.out.println(prettyJson);
		writeJsonFile(jsonFile, prettyJson);
	}
	
	// recursively get to end of the path and change the it 
	public static Map<String, Object> recurseTask5(Map<String, Object> map, String[] keys, Map<String, Object> newSubMap) {
		//basecase - when there's only one key left in keys
		if (keys.length == 1) {
			map.put(keys[0], newSubMap);
		}
		else {
			String topLevelKey = keys[0];

//			System.out.println(topLevelKey);
//			System.out.println(map.get(topLevelKey));
//			System.out.println(map.get(topLevelKey).getClass());
			if (map.get(topLevelKey) instanceof ArrayList) {
				int index = Integer.parseInt(keys[1]);
				// if the path ends at one of the index position, we change the map here
				if (keys.length == 2)
					((ArrayList<Object>) map.get(topLevelKey)).set(index, newSubMap);
				else {
					// get rid of the topLevelKey (represents the array) and the next key (array index)
					String[] newKeys = new String[keys.length - 2];
					for (int i = 0; i < newKeys.length; i++)
						newKeys[i] = keys[i + 2];
					recurseTask5((Map<String, Object>) ((ArrayList<Object>) map.get(topLevelKey)).get(index), newKeys, newSubMap);
				}
			} else {
				// get rid of the topLevelKey
				String[] newKeys = new String[keys.length-1];
				for (int i = 0; i < newKeys.length; i++)
					newKeys[i] = keys[i+1];
				recurseTask5((Map<String, Object>) map.get(topLevelKey), newKeys, newSubMap);
			}
		}
		return map;
	}


	public static void main(String[] args) {
		args = new String[2];
		args[0] = "./books.xml";
		args[1] = "/catalog/book/3";
		String xmlFile = args[0];
//		String xmlFile = "./books.xml";

		// Task1
		System.out.println("Task1");
		String jsonFile1 = xmlFile.substring(0, xmlFile.lastIndexOf(".")) + "1.json";
        xmlToJson(xmlFile, jsonFile1);

		if (args.length < 2) {
			// Task2 - user provides 1 input
			System.out.println("Task2");
			String jsonFile2 = xmlFile.substring(0, xmlFile.lastIndexOf(".")) + "2.json";
			String keyPathQuery = "/catalog/book/1";
			xmlToSubJson(xmlFile, jsonFile2, keyPathQuery);
		} else if (args.length == 2) {
			// Task3 - user provides 2 inputs
			System.out.println("Task3");
			String jsonFile3 = xmlFile.substring(0, xmlFile.lastIndexOf(".")) + "3.json";
			String keyPathQuery = args[1];
			xmlToSubJson(xmlFile, jsonFile3, keyPathQuery);
		} else
			throw new IllegalArgumentException("This program can only accept exactly 1 or 2 arguments.");

        //Task4
        System.out.println("Task4");
    	String jsonFile4 = xmlFile.substring(0, xmlFile.lastIndexOf(".")) + "4.json";
        xmlToJsonPrefix(xmlFile, jsonFile4);

        //Task5
        System.out.println("Task5");
        String jsonFile5 = xmlFile.substring(0, xmlFile.lastIndexOf(".")) + "5.json";
        String keyPath = "/catalog/book/1";
        xmlToJsonReplaceSub(xmlFile, jsonFile5, keyPath);
	}
}




