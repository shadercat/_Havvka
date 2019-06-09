package com.shadercat.havvka;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

//use this class for getting data
public class DataAdapter {
    public static final int SORT_MODE_NONE = 98;
    public static final int SORT_MODE_RATING = 575;
    public static final int SORT_MODE_POPULARITY = 4;
    public static final int SORT_MODE_FIRST = 180;
    public static final int SORT_MODE_SECOND = 586;
    public static final int SORT_MODE_DRINKS = 816;
    public static final int SET_MODE_CHANGE = 989;
    public static final int SET_MODE_DELETE = 694;
    //image cache
    public static Map<Integer, Bitmap> imgCache = new HashMap<>();
    //item cache
    public static Map<Integer, Item> itemCache = new HashMap<>();

    //get list of products
    public static ArrayList<Item> GetProductList(Context context, int sortMode) {
        ArrayList<Item> list = new ArrayList<>();
        int min = 100000;
        int max = 999999;
        int diff = max - min;
        Random random = new Random();
        for (int j = 1; j < 21; j++) {
            int i = random.nextInt(diff + 1) + min;
            Item item = new Item(j, "Sample name " + j, "Sample small description", "Sample big description",
                    "Sample ingridients", 2.30D, 4.6, "http://placehold.it/250/" + i + "?text=" + i);
            itemCache.put(item.getID(), item);
            list.add(item);
        }
        return list;
    }

    public static ArrayList<Proposition> GetPropositionsForItem(int itemDd) {
        ArrayList<Proposition> prop = new ArrayList<>();
        prop.add(new Proposition("canteen1", 1));
        prop.add(new Proposition("canteen2", 2));
        prop.add(new Proposition("canteen3", 3));
        return prop;
    }

    //get item by id
    public static Item GetItemById(int id) {
        Item item = new Item(id, "Sample name " + id, "Sample small description", "Sample big description",
                "Sample ingridients", 2.30D, 5, "http://placehold.it/250/" + 123456 + "?text=" + 123465);
        itemCache.put(item.getID(), item);
        return item;
    }

    public static void SetRating(int rating, int itemId) {

    }

    //get fav sets
    public static ArrayList<FavouriteSet> GetFavouriteData(Context context) {
        ArrayList<FavouriteSet> list = new ArrayList<>();
        list.add(new FavouriteSet(1, "first set", 39.5D));
        list.add(new FavouriteSet(2, "second set", 39.5D));
        list.add(new FavouriteSet(3, "third set", 39.5D));
        return list;
    }

    //get fav items from set
    public static ArrayList<CartItem> GetFavItems(Context context, int id) {
        ArrayList<CartItem> arrayList = new ArrayList<>();
        arrayList.add(new CartItem(new Item(1, "Sample name " + 1, "Sample small description", "Sample big description",
                "Sample ingridients", 2.30D, 4.4, "http://placehold.it/250/" + 123456 + "?text=" + 123465), 5));
        arrayList.add(new CartItem(new Item(2, "Sample name " + 2, "Sample small description", "Sample big description",
                "Sample ingridients", 2.30D, 5, "http://placehold.it/250/" + 123456 + "?text=" + 123465), 5));
        arrayList.add(new CartItem(new Item(3, "Sample name " + 3, "Sample small description", "Sample big description",
                "Sample ingridients", 2.30D, 5, "http://placehold.it/250/" + 123456 + "?text=" + 123465), 5));
        return arrayList;
    }

    //save fav set
    public static void SaveFavSet(Context context, FavouriteSet set, boolean isNew) {

    }


    //add item to set
    public static void AddItemToFavSet(Context context, int setId, int itemId, int count) {
        if (setId >= 0 && itemId >= 0) {

        }
    }

    public static void SetFavItemData(Context context, int setId, int itemId, int setMode, int quantity) {

    }

    //get list of orders
    public static ArrayList<Order> GetOrderList(Context context) {
        ArrayList<Order> list = new ArrayList<>();
        list.add(new Order(1, "ok", "26.26.26", 23.90));
        list.add(new Order(2, "ok", "26.26.26", 23.90));
        list.add(new Order(3, "ok", "26.26.26", 23.90));
        return list;
    }

    public static ArrayList<CartItem> GetOrderItems(Context context, int id) {
        ArrayList<CartItem> list = new ArrayList<>();
        list.add(new CartItem(new Item(1, "Sample name " + 1, "Sample small description", "Sample big description",
                "Sample ingridients", 2.30D, 4.4, "http://placehold.it/250/" + 123456 + "?text=" + 123465), 5));
        list.add(new CartItem(new Item(2, "Sample name " + 1, "Sample small description", "Sample big description",
                "Sample ingridients", 2.30D, 4.4, "http://placehold.it/250/" + 123456 + "?text=" + 123465), 5));
        list.add(new CartItem(new Item(3, "Sample name " + 1, "Sample small description", "Sample big description",
                "Sample ingridients", 2.30D, 4.4, "http://placehold.it/250/" + 123456 + "?text=" + 123465), 5));
        return list;
    }

    public static Order GetOrderById(Context context, int id) {
        return new Order(id, "ok", "22:30", 240.90);
    }

    public static void GetProposition(Context context, List<Proposition> propositions) {

    }

    public static void SetOrder(Context context) {

    }

    public static void SaveUserInfo(String email, String password, int userId, Context context) {
        //TODO function for save user data in locale storage;
        SharedPreferences preferences = context.getSharedPreferences("Preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = preferences.edit();
        if (!preferences.contains("IsCheckedAccount")) {
            ed.putBoolean("IsCheckedAccount", false);
            ed.putString("useremail", "guest");
            ed.putInt("userid", 0);
            ed.putString("password", "");
            ed.putBoolean("IsCheckedAccount", true);
            ed.putInt("dataver", 0);
            ed.apply();
        }
        ed.putString("useremail", email);
        ed.putString("password", password);
        ed.putInt("userid", userId);
        ed.putBoolean("IsCheckedAccount", true);
        ed.apply();
    }

    public static void InitializeUserInfo(Context context) {
        //TODO get user info from locale storage; check info from server;
        SharedPreferences preferences = context.getSharedPreferences("Preferences", Context.MODE_PRIVATE);
        UserInfo.GuestMode = false;
        UserInfo.IsCheckedAccount = preferences.getBoolean("IsCheckedAccount", false);
        UserInfo.UserEmail = preferences.getString("useremail", "guest");
        UserInfo.UserID = preferences.getInt("userid", 0);
        UserInfo.DataVersion = preferences.getInt("dataver", 0);
        if (!preferences.contains("IsCheckedAccount")) {
            SharedPreferences.Editor ed = preferences.edit();
            ed.putBoolean("IsCheckedAccount", false);
            ed.putString("useremail", "guest");
            ed.putInt("userid", 0);
            ed.putString("password", "");
            ed.putInt("dataver", 0);
            ed.apply();
        }
    }


    public static void UserExit(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("Preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = preferences.edit();
        ed.clear();
        ed.commit();  //use commit `cause it`s an synchronized method
        InitializeUserInfo(context);
    }
}
