

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


public class Parser {
	
	public ArrayList<dishes> getDishes(String path) throws ParseException, FileNotFoundException, IOException {
		JSONParser parser = new JSONParser();
		JSONArray array = (JSONArray) parser.parse(new FileReader(path));
		ArrayList<dishes> result = new  ArrayList<>();
        for (Object object : array){
         JSONObject dishes = (JSONObject) object;
		 long dish_id = (long) dishes.get("dish_id");
		 String dish_name = (String) dishes.get("dish_name");
		 String dish_img = (String) dishes.get("dish_img");
		 long dish_type = (long) dishes.get("dish_type");
		 double dish_price = (double) dishes.get("dish_price");
		 String dish_long_description = (String) dishes.get("dish_long_description");
		 String dish_short_description  = (String) dishes.get("dish_short_description");
		 long dish_popularity = (long) dishes.get("dish_popularity");

		 result.add(new dishes(dish_id, dish_name, dish_img, dish_type, dish_price, dish_long_description, dish_short_description, dish_popularity));
        }
        return result;
     }
}

