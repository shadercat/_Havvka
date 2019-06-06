package com.shadercat.havvka;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

//use this class for getting data
public class DataAdapter {
    public static Map<Integer, Bitmap> imgCache = new HashMap<>();
    public static ArrayList<Item> GetProductList(Context context) {
        ArrayList<Item> list = new ArrayList<>();
        if (WebAPI.IsProductDataUpdated()) {
            list = WebAPI.GetProductData();
            //TODO function for save product data in locale storage; update info about data
        } else {
            //TODO function for get product data from locale storage;
            DatabaseAdapter db = new DatabaseAdapter(context);
            int min = 100000;
            int max = 999999;
            int diff = max - min;
            Random random = new Random();
            int i = random.nextInt(diff + 1) + min;
            URL url;

            int id = R.drawable.food_test;
            for (int j = 1; j < 21; j++) {
                Item item = new Item("Name of Food " + i, "Small descrition", "Big Description", id);
                item.SetID(j);
                item.SetPrice(2.30);
                item.SetIngridients("Ingridients");
                item.setImg(BitmapFactory.decodeResource(context.getResources(), id));
                try{
                    url = new URL("http://placehold.it/250/" + i + "?text=" + i);
                } catch (MalformedURLException e){
                    url = null;
                }
                item.setUrl(url);
                i = random.nextInt(diff + 1) + min;
                list.add(item);
                db.PutItem(item);
            }
        }
        return list;
    }

    public static ArrayList<FavouriteSet> GetFavouriteData(Context context) {
        ArrayList<FavouriteSet> list = new ArrayList<>();
        if (WebAPI.IsFavouriteSetDataUpdated()) {
            list = WebAPI.GetFavouriteSetData();
            //TODO function for save favourite set data in locale storage; update info about data
        } else {
            DatabaseAdapter db = new DatabaseAdapter(context);
            list = db.GetFavourites();
        }
        return list;
    }

    public static void SaveFavSet(Context context, FavouriteSet set, boolean isNew) {
        DatabaseAdapter db = new DatabaseAdapter(context);
        if (isNew) {
            db.PutNewFavouriteSet(set);
        } else {
            db.PutFavouriteSet(set);
        }
    }

    public static void AddItemToFavSet(Context context, int setId, int itemId, int count) {
        if (setId >= 0 && itemId >= 0) {
            DatabaseAdapter db = new DatabaseAdapter(context);
            db.PutFavouriteItem(setId, itemId, count);
        }
    }
    public static ArrayList<CartItem> GetFavItems(Context context, int id){
        ArrayList<CartItem> arrayList;
        DatabaseAdapter db = new DatabaseAdapter(context);
        arrayList = db.GetFavouritesItems(id);
        return arrayList;
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

    public static void SaveDataVersion(int ver, Context context) {
        SharedPreferences preferences = context.getSharedPreferences("Preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = preferences.edit();
        if (!preferences.contains("IsCheckedAccount")) {
            ed.putBoolean("IsCheckedAccount", false);
            ed.putString("useremail", "guest");
            ed.putInt("userid", 0);
            ed.putString("password", "");
            ed.putInt("dataver", 0);
            ed.apply();
        } else {
            ed.putInt("dataver", ver);
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
