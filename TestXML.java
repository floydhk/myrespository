package com.hsbc.testxml;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;

import com.jayway.restassured.path.json.JsonPath;

public class TestXML {

	private static final int PRETTY_PRINT_INDENT_FACTOR = 0;

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub

		  File file = new File("teste.xml");
	        FileInputStream fin = new FileInputStream(file);
	        byte[] xmlData = new byte[(int) file.length()];
	        fin.read(xmlData);
	        fin.close();
	        String TEST_XML_STRING = new String(xmlData, "UTF-8");

	        try {
	     String     TEST_J_STRING="{\"Product\":{\"Section\":{\"field\":[\"fff\",\"ee\"],\"name\":\"ddd\"}}}";
	        	JSONObject xmlJSONObj = XML.toJSONObject(TEST_XML_STRING);
	             //String result=recurseKeys(xmlJSONObj, "Section");
	         System.out.println(" js"+ xmlJSONObj.toString());
	        // JSONArray reslt= xmlJSONObj.getJSONArray("Product.Section");
	      //   JSONObject reslt=(JSONObject)xmlJSONObj.get("Product.Section");
	         HashMap x=JsonPath.from( TEST_J_STRING).get();
	         Iterator it = x.entrySet().iterator();
	         while (it.hasNext()) {
	             Map.Entry pair = (Map.Entry)it.next();
	             System.out.println(pair.getKey() + " = " + pair.getValue());
	            
	         }
	         
	         String category= JsonPath.from(xmlJSONObj.toString()).get("Product");
	        	System.out.println("category="+ category);	
	  //       JSONObject reslt2=(JSONObject)reslt.get("Section");
	  //       JSONArray reslt3=(JSONArray)reslt2.get("field");
	       //  String xx=(String) reslt.get("name");
	        //     for(int i = 0 ; i < reslt3.length() ; i++){
	      //      	 String p = (String)reslt3.get(i); 
	     //       	 System.out.println("p="+ p);
	     //     }
	     //      String jsonPrettyPrintString = xmlJSONObj
	     //               .toString(PRETTY_PRINT_INDENT_FACTOR);
	     //       System.out.println(jsonPrettyPrintString);

	        } catch (JSONException e) {
	            System.out.println(e.toString());
	        }
		
	}

	
	public static String recurseKeys(JSONObject jObj, String findKey) throws JSONException {
	    String finalValue = "";
	    if (jObj == null) {
	        return "";
	    }

	    Iterator<String> keyItr = jObj.keys();
	    HashMap<String, String> map = new HashMap<>();

	    while(keyItr.hasNext()) {
	        String key = keyItr.next();
	        map.put(key, jObj.getString(key));
	    }

	    for (Map.Entry<String, String> e : (map).entrySet()) {
	        String key = e.getKey();
	        if (key.equalsIgnoreCase(findKey)) {
	            return jObj.getString(key);
	        }

	        // read value
	        Object value = jObj.get(key);

	        if (value instanceof JSONObject) {
	            finalValue = recurseKeys((JSONObject)value, findKey);
	        }
	    }

	    // key is not found
	    return finalValue;
	}
	
	
	
	
}



