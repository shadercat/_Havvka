package com.shadercat.havvka;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Converter {
    public static ArrayList<Item> parseItems(String json) throws JSONException {
        ArrayList<Item> items = new ArrayList<>();

        JSONArray array = new JSONArray(json);
        for (int i = 0; i < array.length(); i++) {
            JSONObject object = array.getJSONObject(i);
            int id = object.getInt("dish_id");
            String name = object.getString("dish_name");
            String smallDescr = object.getString("dish_short_description");
            String bigDescr = object.getString("dish_long_description");
            double price = object.getDouble("dish_price");
            String img = object.getString("dish_img");
            double rating = object.getDouble("dish_rating");
            items.add(new Item(id, name, smallDescr, bigDescr, "Наша любов та піклування... Ну ще інші інгрідієнти", price, rating, img));
        }
        return items;
    }

    public static Item parseItem(String json) throws JSONException {
        JSONObject object = new JSONObject(json);
        int id = object.getInt("dish_id");
        String name = object.getString("dish_name");
        String smallDescr = object.getString("dish_short_description");
        String bigDescr = object.getString("dish_long_description");
        double price = object.getDouble("dish_price");
        String img = object.getString("dish_img");
        double rating = object.getDouble("dish_rating");
        return new Item(id, name, smallDescr, bigDescr, "Наша любов та піклування... Ну ще інші інгрідієнти", price, rating, img);
    }

    public static ArrayList<Proposition> parsePropositions(String json) throws JSONException {
        ArrayList<Proposition> propositions = new ArrayList<>();

        JSONArray array = new JSONArray(json);
        for (int i = 0; i < array.length(); i++) {
            JSONObject object = array.getJSONObject(i);
            int id = object.getInt("organization_id");
            String name = object.getString("organization_name");
            propositions.add(new Proposition(name, id));
        }
        return propositions;
    }

    public static boolean parseResponse(String json) throws JSONException {
        JSONObject object = new JSONObject(json);
        return object.getBoolean("checked");
    }

    public static boolean parseResponseStatus(String json) throws JSONException {
        JSONObject object = new JSONObject(json);
        return object.getBoolean("status");
    }

    public static boolean parseResponseDeleted(String json) throws JSONException {
        JSONObject object = new JSONObject(json);
        return object.getString("status").equals("deleted");
    }

    public static ArrayList<FavouriteSet> parseSets(String json) throws JSONException {
        ArrayList<FavouriteSet> favouriteSets = new ArrayList<>();
        JSONArray array = new JSONArray(json);
        for (int i = 0; i < array.length(); i++) {
            JSONObject object = array.getJSONObject(i);
            int id = object.getInt("set_id");
            String name = object.getString("set_name");
            double price = object.getDouble("set_total_price");
            favouriteSets.add(new FavouriteSet(id, name, price));
        }
        return favouriteSets;
    }

    public static ArrayList<CartItem> parseCartItems(String json) throws JSONException {
        ArrayList<CartItem> cartItems = new ArrayList<>();
        JSONArray array = new JSONArray(json);
        for (int i = 0; i < array.length(); i++) {
            JSONObject object = array.getJSONObject(i);
            int id_item = object.getInt("dish_id");
            String name_item = object.getString("dish_name");
            double price_item = object.getDouble("dish_price");
            String img = object.getString("dish_img");
            String longDescr = object.getString("dish_long_description");
            String shortDescr = object.getString("dish_short_description");
            int amount = object.getInt("set_item_amount");
            double rating = object.getDouble("dish_rating");
            cartItems.add(new CartItem(new Item(id_item, name_item, shortDescr, longDescr, "", price_item, 5, img), amount));
        }
        return cartItems;
    }
}
