package com.shadercat.havvka;

import java.util.ArrayList;
import java.util.List;

public class WebAPI {
    public static boolean IsProductDataUpdated() {
        return true;
    }

    public static boolean IsFavouriteSetDataUpdated() {
        return true;
    }

    public static boolean CheckUserInfo(String email, String password) {
        if (email != null && password != null) {
            if (email.trim().equals("hola@nure.ua") && password.equals("12345")) {
                UserInfo.IsCheckedAccount = true;
                UserInfo.UserEmail = email.trim();
                UserInfo.UserID = 2;
                return true;
            }
        }
        return false;
        //use DataAdapter.SaveUserInfo(...);
    }

    public static ArrayList<Item> GetProductData() {
        ArrayList<Item> list = new ArrayList<>();
        for (int i = 0; i < 101; i++) {
            Item item = new Item("Name of Food " + i, "Small descrition", "Big Description", R.drawable.food_test);
            item.SetID(i);
            item.SetPrice(2.30);
            item.SetIngridients("Ingridients");
            list.add(item);
        }
        return list;
    }

    public static ArrayList<FavouriteSet> GetFavouriteSetData() {
        ArrayList<FavouriteSet> list = new ArrayList<>();
        return list;
    }
}
