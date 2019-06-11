package com.shadercat.havvka;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.SystemClock;
import android.util.Log;

import org.json.JSONException;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class DataAdapter {
    public static final int SORT_MODE_DEFAULT = 0;
    public static final int SORT_MODE_DESERTS = 3;
    public static final int SORT_MODE_FIRST = 1;
    public static final int SORT_MODE_SECOND = 2;
    public static final int SORT_MODE_DRINKS = 4;

    public static final int SET_MODE_CHANGE = 989;
    public static final int SET_MODE_DELETE = 694;
    public static final int ORDER_CASH_MODE = 951;
    public static final int ORDER_CASHLESS_MODE = 794;
    //image cache
    public static Map<Integer, Bitmap> imgCache = new HashMap<>();
    //item cache
    public static Map<Integer, Item> itemCache = new HashMap<>();

    //get list of products
    public static ArrayList<Item> GetProductList(Context context, int sortMode) {
        String json;
        switch (sortMode) {
            case SORT_MODE_DEFAULT:
                json = WebAPI.performGetCall(WebAPI.API + "/api/dishes/all-dishes");
                break;
            case SORT_MODE_DRINKS:
                json = WebAPI.performGetCall(WebAPI.API + "/api/dishes/category-menu?dish_type=4");
                break;
            case SORT_MODE_FIRST:
                json = WebAPI.performGetCall(WebAPI.API + "/api/dishes/category-menu?dish_type=1");
                break;
            case SORT_MODE_DESERTS:
                json = WebAPI.performGetCall(WebAPI.API + "/api/dishes/category-menu?dish_type=3");
                break;
            case SORT_MODE_SECOND:
                json = WebAPI.performGetCall(WebAPI.API + "/api/dishes/category-menu?dish_type=2");
                break;
            default:
                json = WebAPI.performGetCall(WebAPI.API + "/api/dishes/all-dishes");
        }
        ArrayList<Item> items = new ArrayList<>();
        try {
            items = Converter.parseItems(json);
        } catch (JSONException e) {
            Log.e("error", e.getMessage());
        }
        for (Item it : items) {
            itemCache.put(it.getID(), it);
        }
        return items;
    }

    public static ArrayList<Proposition> GetPropositionsForItem(int itemDd) {
        ArrayList<Proposition> propositions = new ArrayList<>();
        try {
            propositions = Converter.parsePropositions(WebAPI.performGetCall("https://havvka-server-vlad-f96.c9users.io/api/organizations/dish-av-org" + itemDd));
        } catch (JSONException e) {
            Log.e("error", e.getMessage());
        }
        return propositions;
    }

    //get item by id
    public static Item GetItemById(int id) {
        Item item = new Item(id, "Sample name " + id, "Sample small description", "Sample big description",
                "Sample ingridients", 0.00D, 5, "http://placehold.it/250/" + 123456 + "?text=" + 123465);
        try {
            item = Converter.parseItem(WebAPI.performGetCall(WebAPI.API + "/api/dishes/gdish-by-id?dish_id=" + id));
            itemCache.put(item.getID(), item);
        } catch (JSONException e) {
            Log.e("error", e.getMessage());
        }
        return item;
    }

    public static boolean SetRating(int rating, int itemId) {
        boolean rate = false;
        if (UserInfo.IsCheckedAccount) {
            try {
                rate = Converter.parseResponseStatus(WebAPI.performPostCall(WebAPI.API + "/api/dishes/estimate-dish?" +
                        "dish_id=" + itemId + "&mark_value=" + rating + "&user_email=" + UserInfo.UserEmail, null));
            } catch (JSONException e) {
                Log.e("JSONException", e.getMessage());
            }
        }
        return rate;
    }

    //get fav sets
    public static ArrayList<FavouriteSet> GetFavouriteData(Context context) {

        ArrayList<FavouriteSet> sets = new ArrayList<>();
        if (UserInfo.IsCheckedAccount) {
            try {
                sets = Converter.parseSets(WebAPI.performGetCall(WebAPI.API + "/api/sets/sets-get?user_email=" + UserInfo.UserEmail));
            } catch (JSONException e) {
                Log.e("JSONException", e.getMessage());
            }
        }
        return sets;
    }

    //get fav items from set
    public static ArrayList<CartItem> GetFavItems(Context context, int id) {
        ArrayList<CartItem> cartItems = new ArrayList<>();
        try {
            cartItems = Converter.parseCartItems(WebAPI.performGetCall(WebAPI.API + "/api/sets/get-set-items-by-set?" +
                    "set_id=" + id + "&user_email=" + UserInfo.UserEmail));
        } catch (JSONException e) {
            Log.e("JSONException", e.getMessage());
        }
        return cartItems;
    }

    //save fav set
    public static boolean SaveFavSet(Context context, FavouriteSet set, boolean isNew) {
        Log.d("add SET", "sssssssssssssssssssssssssssssssssssssssssss");
        String name = set.getName();
        boolean isAdded = false;
        if (UserInfo.IsCheckedAccount) {
            try {
                isAdded = Converter.parseResponseStatus(WebAPI.performPostCall(WebAPI.API + "/api/sets/add-set-by-login?" +
                        "user_email=" + UserInfo.UserEmail + "&set_name=" + URLEncoder.encode(name, "UTF-8"), null));
            } catch (JSONException e) {
                Log.e("JSONException", e.getMessage());
            } catch (UnsupportedEncodingException e) {
                Log.e("UnsupportedEncoding", e.getMessage());
            }
        }
        return isAdded;
    }

    public static boolean DeleteSet(Context context, int id) {
        boolean isDeleted = false;
        if (UserInfo.IsCheckedAccount) {
            try {
                isDeleted = Converter.parseResponseDeleted(WebAPI.performDeleteCall(WebAPI.API + "/api/sets/set_delete?set_id=" + id, null));
            } catch (JSONException e) {
                Log.e("JSONException", e.getMessage());
            }
        }
        return isDeleted;
    }


    //add item to set
    public static boolean AddItemToFavSet(Context context, int setId, int itemId, int count) {
        boolean isAdded = false;
        if (UserInfo.IsCheckedAccount) {
            try {
                isAdded = Converter.parseResponseStatus(WebAPI.performPostCall(WebAPI.API + "/api/sets/sets-add-elem?set_id="
                        + setId + "&user_email=" + UserInfo.UserEmail + "&dish_id=" + itemId + "&set_item_amount=" + count, null));
            } catch (JSONException e) {
                Log.e("JSONException", e.getMessage());
            }
        }
        return isAdded;
    }

    public static boolean SetFavItemData(Context context, int setId, int itemId, int setMode, int quantity) {
        boolean isSet = false;
        if (UserInfo.IsCheckedAccount) {
            try {
                switch (setMode) {
                    case SET_MODE_DELETE:
                        isSet = Converter.parseResponseDeleted(WebAPI.performDeleteCall(WebAPI.API + "/api/sets/sets-delete-elem?" +
                                "set_id=" + setId + "&user_email=" + UserInfo.UserEmail + "&dish_id=" + itemId, null));
                        break;
                    case SET_MODE_CHANGE:
                        isSet = Converter.parseResponseStatus((WebAPI.performPostCall(WebAPI.API + "/api/sets/update-set-items-quentity?set_id=" +
                                setId + "&itemId=" + itemId + "&amount=" + quantity, null)));
                        break;
                    default:
                }
            } catch (JSONException e) {
                Log.e("JSONException", e.getMessage());
            }
        }
        return isSet;
    }

    //get list of orders
    public static ArrayList<Order> GetOrderList(Context context) {
        ArrayList<Order> list = new ArrayList<>();
        list.add(new Order(1, "ok", "26.26.26", "12:50", 23.90));
        list.add(new Order(2, "ok", "26.26.26", "12:50", 23.90));
        list.add(new Order(3, "ok", "26.26.26", "12:50", 23.90));
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
        return new Order(id, "ok", "22.06.19", "22:30", 240.90);
    }

    public static ArrayList<Proposition> GetProposition(Context context) {
        HashMap<Integer, Proposition> prop = new HashMap<>();
        for (CartItem it : CartHelper.list) {
            ArrayList<Proposition> pr = DataAdapter.GetPropositionsForItem(it.getItem().getID());
            for (Proposition p : pr) {
                prop.put(p.getId(), p);
            }
        }
        return new ArrayList<Proposition>(prop.values());
    }

    public static void SetOrder(Context context, Calendar calendar, int proposition_id, int order_mode) {
        SystemClock.sleep(3000);
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
        UserInfo.GuestMode = false;
        UserInfo.IsCheckedAccount = preferences.getBoolean("IsCheckedAccount", false);
        UserInfo.UserEmail = preferences.getString("useremail", "guest");
        UserInfo.UserID = preferences.getInt("userid", 0);
        UserInfo.DataVersion = preferences.getInt("dataver", 0);
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
