package com.shadercat.havvka;

import java.util.ArrayList;

//use this class for getting data
public class DataAdapter {
    public static ArrayList<Item> GetProductList() {
        ArrayList<Item> list = new ArrayList<>();
        if (WebAPI.IsProductDataUpdated()) {
            list = WebAPI.GetProductData();
            //TODO function for save product data in locale storage; update info about data
        } else {
            //TODO function for get product data from locale storage;
            for (int i = 0; i < 101; i++) {
                Item item = new Item("Name of Food " + i, "Small descrition", "Big Description", R.drawable.food_test);
                item.SetID(i);
                item.SetPrice(2.30);
                item.SetIngridients("Ingridients");
                list.add(item);
            }
        }
        return list;
    }

    public static ArrayList<FavouriteSet> GetFavouriteData() {
        ArrayList<FavouriteSet> list = new ArrayList<>();
        if (WebAPI.IsFavouriteSetDataUpdated()) {
            list = WebAPI.GetFavouriteSetData();
            //TODO function for save favourite set data in locale storage; update info about data
        } else {
            //TODO function for get favourite set data from locale storage;
        }
        return list;
    }
    public static void SaveUserInfo(String email, String password, int userId){
        //TODO function for save user data in locale storage;
    }
    public static void InitializeUserInfo() {
        //TODO get user info from locale storage; check info from server;
        UserInfo.GuestMode = false;
        UserInfo.IsCheckedAccount = false;
        UserInfo.UserEmail = "";
        UserInfo.UserID = 0;
    }
}
