package com.shadercat.havvka;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Converter {
    public static ArrayList<Item> parseItems(String json) throws JSONException {
        ArrayList<Item> items = new ArrayList<>();

        JSONArray array = new JSONArray(json);
        for (int i = 0; i < array.length(); i++){
            JSONObject object = array.getJSONObject(i);
            int id = object.getInt("dish_id");
            String name = object.getString("dish_name");
            String smallDescr = object.getString("dish_short_description");
            String bigDescr = object.getString("dish_long_description");
            double price = object.getDouble("dish_price");
            String img = object.getString("dish_img");
            //double rating = object.getDouble("dish_rating");
            items.add(new Item(id,name,smallDescr,bigDescr,"Наша любов та піклування... Ну ще інші інгрідієнти",price,5,img));
        }
        return items;
    }
    public static ArrayList<Proposition> parsePropositions(String json) throws JSONException {
        ArrayList<Proposition> propositions = new ArrayList<>();

        JSONArray array = new JSONArray(json);
        for (int i = 0; i < array.length(); i++){
            JSONObject object = array.getJSONObject(i);
            int id = object.getInt("organization_id");
            String name = object.getString("organization_name");
            propositions.add(new Proposition(name,id));
        }
        return propositions;
    }
    public static boolean parseResponse(String json) throws JSONException {
        JSONObject object = new JSONObject(json);
        return object.getBoolean("checked");
    }
}
