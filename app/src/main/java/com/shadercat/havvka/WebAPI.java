package com.shadercat.havvka;

import android.os.SystemClock;

import java.util.ArrayList;

public class WebAPI {
    public static boolean IsProductDataUpdated() {
        return false;
    }

    public static boolean IsFavouriteSetDataUpdated() {
        return false;
    }

    public static boolean CheckUserInfo(String email, String password) {
        if (email != null && password != null) {
            if (email.trim().equals("hola@nure.ua") && password.equals("12345")) {
                UserInfo.IsCheckedAccount = true;
                UserInfo.UserEmail = email.trim();
                UserInfo.UserID = 2;
                return true;
            }
            SystemClock.sleep(3000);
        }
        return false;
        //use DataAdapter.SaveUserInfo(...);
    }

    public static void CreateAccount(String email, String password) {

    }

    public static ArrayList<Item> GetProductData() {
        ArrayList<Item> list = new ArrayList<>();
        return list;
    }

    public static ArrayList<FavouriteSet> GetFavouriteSetData() {
        ArrayList<FavouriteSet> list = new ArrayList<>();
        return list;
    }
}
