package testXML;


import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.jayway.jsonpath.JsonPath;

public class TestMain {


	   public static int PRETTY_PRINT_INDENT_FACTOR = 4;
	    public static String TEST_XML_STRING = "Your xml string here";
	    static JsonParser parser = new JsonParser();
	    
	public static void main(String[] args) throws FileNotFoundException {
	
		String data=readFile();

    try {
        JSONObject xmlJSONObj = XML.toJSONObject(data);
        String jsonPrettyPrintString = xmlJSONObj.toString(PRETTY_PRINT_INDENT_FACTOR);
        System.out.println(jsonPrettyPrintString);
      
        
        String[] keys = JSONObject.getNames(xmlJSONObj);

        for (String key : keys)
        {
            Object value = xmlJSONObj.get(key);
           System.out.println("location="+ value);
            // Determine type of value and do something with it...
        }
        HashMap<String, Object> oo=createHashMapFromJsonString(jsonPrettyPrintString);
     //   JSONObject location = xmlJSONObj.getJSONObject( "E3K" );
       // JSONObject sec = location.getJSONObject( "ProductFeature" );
        
        for (Entry<String, Object> entry : oo.entrySet()) {
    		System.out.println("Key : " + entry.getKey() + " Value : " + entry.getValue());
    	}
     //  System.out.println("->"+ JsonPath.read(jsonPrettyPrintString, "$.E3K.*"));
        
    } catch (JSONException je) {
        System.out.println(je.toString());
    }
		
		
		
	}

	
	
	public static HashMap<String, Object> createHashMapFromJsonString(String json) {

	    JsonObject object = (JsonObject) parser.parse(json);
	    Set<Map.Entry<String, JsonElement>> set = object.entrySet();
	    Iterator<Map.Entry<String, JsonElement>> iterator = set.iterator();
	    HashMap<String, Object> map = new HashMap<String, Object>();

	    while (iterator.hasNext()) {

	        Map.Entry<String, JsonElement> entry = iterator.next();
	        String key = entry.getKey();
	        JsonElement value = entry.getValue();

	        if (null != value) {
	            if (!value.isJsonPrimitive()) {
	                if (value.isJsonObject()) {

	                    map.put(key, createHashMapFromJsonString(value.toString()));
	                } else if (value.isJsonArray() && value.toString().contains(":")) {

	                    List<HashMap<String, Object>> list = new ArrayList<>();
	                    JsonArray array = value.getAsJsonArray();
	                    if (null != array) {
	                        for (JsonElement element : array) {
	                            list.add(createHashMapFromJsonString(element.toString()));
	                        }
	                        map.put(key, list);
	                    }
	                } else if (value.isJsonArray() && !value.toString().contains(":")) {
	                    map.put(key, value.getAsJsonArray());
	                }
	            } else {
	                map.put(key, value.getAsString());
	            }
	        }
	    }
	    return map;
	}
	

	
	public static String readFile() throws FileNotFoundException{
		
		Scanner in = new Scanner(new File("c:\\temp\\data.xml"));

		String data="";
		
		while (in.hasNext()) { // Iterates each line in the file
		    String line = in.nextLine();
		    data=data+line;
		}

		in.close(); // Don't forget to close resource leaks
		return data;
		
	}
	
}
