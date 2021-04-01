 -------------- Milestone 2 -----------------
added two methods at src/main/java/org/json/XML.java
public static JSONObject toJSONObject(Reader reader, JSONPointer path)
public static JSONObject toJSONObject(Reader reader, JSONPointer path, JSONObject replacement)

added Junit tests at src/test/java/test_262P/XMLTest.java
run XMLTest.java

 -------------- Milestone 3 -----------------
added method at src/main/java/org/json/XML.java
public static JSONObject toJSONObject(Reader reader, Function<String, String> keyTransformer)

added Junit tests at src/test/java/test_262P/XMLTest.java
run XMLTest.java

This method is similar to milestone task4 by transform every key inside an xml file and return
a Json object, except this method is done inside the library vs. milestone1 is doing it in client code.
Transforming keys in the library can be done while parsing the XML files instead of recursively
change the keys on an Json object.
