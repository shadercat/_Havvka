package com.shadercat.havvka;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.ArrayList;

//use this class for getting data
public class DataAdapter {
    public static ArrayList<Item> GetProductList(Context context) {
        ArrayList<Item> list = new ArrayList<>();
        if (WebAPI.IsProductDataUpdated()) {
            list = WebAPI.GetProductData();
            //TODO function for save product data in locale storage; update info about data
        } else {
            //TODO function for get product data from locale storage;
            int id = R.drawable.food_test;
            DatabaseAdapter db = new DatabaseAdapter(context);
            Bitmap mb = BitmapFactory.decodeResource(context.getResources(), id);
            db.PutImage(mb, id);
            for (int i = 0; i < 101; i++) {
                Item item = new Item("Name of Food " + i, "Small descrition", "Big Description", id);
                item.SetID(i);
                item.SetPrice(2.30);
                item.SetIngridients("Ingridients");
                list.add(item);
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
            //TODO function for get favourite set data from locale storage;
        }
        return list;
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
        if (!preferences.contains("IsCheckedAccount")) {
            SharedPreferences.Editor ed = preferences.edit();
            ed.putBoolean("IsCheckedAccount", false);
            ed.putString("useremail", "guest");
            ed.putInt("userid", 0);
            ed.putString("password", "");
            ed.apply();
        }
    }

    public static void UserExit(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("Preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = preferences.edit();
        ed.clear();
        ed.commit();
        InitializeUserInfo(context);
    }
}
