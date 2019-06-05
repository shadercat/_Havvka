package com.shadercat.havvka;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatabaseAdapter {
    private Context context;

    public DatabaseAdapter(Context context) {
        this.context = context;
    }

    public void PutItem(Item item) {
        ItemDatabaseHelper dbHelper = new ItemDatabaseHelper(context);
        ContentValues cv = new ContentValues();
        cv.put("id", item.GetID());
        cv.put("name", item.GetName());
        cv.put("smallDescr", item.GetSmallDescr());
        cv.put("bigDescr", item.GetBigDescr());
        cv.put("ingridients", item.GetIngridients());
        cv.put("price", item.GetPrice());
        cv.put("image", item.GetImage());
        cv.put("rating", item.GetRating());
        ContentValues cv2 = new ContentValues();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        item.getImg().compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        cv2.put("id", item.GetID());
        cv2.put("img", byteArray);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.insertWithOnConflict("itemdb", null, cv, SQLiteDatabase.CONFLICT_REPLACE);
        db.insertWithOnConflict("pictures", null, cv2, SQLiteDatabase.CONFLICT_REPLACE);
        dbHelper.close();
    }

    public void DeleteItemTable() {
        ItemDatabaseHelper dbHelper = new ItemDatabaseHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete("itemdb", null, null);
        dbHelper.close();
    }

    public Item GetItem(int id) {
        ItemDatabaseHelper dbHelper = new ItemDatabaseHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String[] arg = {Integer.toString(id)};
        Item item = null;
        Cursor c = db.query("itemdb", null, "id = ?", arg, null, null, null);
        if (c.moveToFirst()) {
            int idColIndex = c.getColumnIndex("id");
            int nameColIndex = c.getColumnIndex("name");
            int smallDescrColIndex = c.getColumnIndex("smallDescr");
            int bigDescrColIndex = c.getColumnIndex("bigDescr");
            int ingridientsColIndex = c.getColumnIndex("ingridients");
            int priceColIndex = c.getColumnIndex("price");
            int imageColIndex = c.getColumnIndex("image");
            int ratingColIndex = c.getColumnIndex("rating");
            item = new Item(c.getString(nameColIndex),
                    c.getString(smallDescrColIndex),
                    c.getString(bigDescrColIndex),
                    c.getInt(imageColIndex),
                    c.getInt(idColIndex),
                    c.getString(ingridientsColIndex),
                    c.getDouble(priceColIndex),
                    c.getInt(ratingColIndex));
        }
        c.close();
        if (item == null) {
            return null;
        }
        Cursor c2 = db.query("pictures", null, "id = ?", arg, null, null, null);
        if (c2.moveToFirst()) {
            int imgColIndex = c2.getColumnIndex("img");
            byte[] bt = c2.getBlob(imgColIndex);
            item.setImg(BitmapFactory.decodeByteArray(bt, 0, bt.length));
        }
        c2.close();
        dbHelper.close();
        return item;
    }

    public void PutNewFavouriteSet(FavouriteSet set) {
        ItemDatabaseHelper dbHelper = new ItemDatabaseHelper(context);
        ContentValues cv = new ContentValues();
        cv.put("name", set.getName());
        cv.put("count", set.getCount());
        cv.put("serverid", set.getServerId());
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.insertWithOnConflict("favset", null, cv, SQLiteDatabase.CONFLICT_REPLACE);
        dbHelper.close();
    }

    public void PutFavouriteSet(FavouriteSet set) {
        ItemDatabaseHelper dbHelper = new ItemDatabaseHelper(context);
        ContentValues cv = new ContentValues();
        cv.put("id", set.getId());
        cv.put("name", set.getName());
        cv.put("count", set.getCount());
        cv.put("serverid", set.getServerId());
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.insertWithOnConflict("favset", null, cv, SQLiteDatabase.CONFLICT_REPLACE);
        dbHelper.close();
    }

    public void DeleteFavouriteSets() {
        ItemDatabaseHelper dbHelper = new ItemDatabaseHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete("favset", null, null);
        dbHelper.close();
    }

    public void DeleteFavouriteSetById(int id) {
        ItemDatabaseHelper dbHelper = new ItemDatabaseHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String[] arg = {Integer.toString(id)};
        db.delete("favset", "id = ?", arg);
        dbHelper.close();
    }

    public void UpdateFavouriteSetServerId(int setId, int serverId) {
        ItemDatabaseHelper dbHelper = new ItemDatabaseHelper(context);
        ContentValues cv = new ContentValues();
        cv.put("serverid", serverId);
        String[] arg = {Integer.toString(setId)};
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.update("favset", cv, "id = ?", arg);
    }


    public void PutFavouriteItem(int setId, int itemId, int countItem) {
        ItemDatabaseHelper dbHelper = new ItemDatabaseHelper(context);
        ContentValues cv = new ContentValues();
        cv.put("id", setId);
        cv.put("itemid", itemId);
        cv.put("count", countItem);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.insertWithOnConflict("favitem", null, cv, SQLiteDatabase.CONFLICT_REPLACE);
        dbHelper.close();
    }

    public ArrayList<FavouriteSet> GetFavourites() {
        ItemDatabaseHelper dbHelper = new ItemDatabaseHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor c = db.query("favset", null, null, null, null, null, null);
        ArrayList<FavouriteSet> res = new ArrayList<>();
        if (c.moveToFirst()) {
            int idColIndex = c.getColumnIndex("id");
            int nameColIndex = c.getColumnIndex("name");
            int serverIdColIndex = c.getColumnIndex("serverid");
            int countColIndex = c.getColumnIndex("count");
            do {
                res.add(new FavouriteSet(c.getInt(idColIndex),
                        c.getString(nameColIndex),
                        c.getInt(serverIdColIndex),
                        c.getInt(countColIndex)));
            } while (c.moveToNext());
        }
        c.close();
        dbHelper.close();
        return res;
    }
    public ArrayList<CartItem> GetFavouritesItems(int setId){
        ItemDatabaseHelper dbHelper = new ItemDatabaseHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String[] args = {Integer.toString(setId)};
        Cursor c = db.query("favitem", null, "id = ?", args, null, null, null);
        HashMap<Integer,Integer> map = new HashMap<>();
        if(c.moveToFirst()){
            int countColIndex = c.getColumnIndex("count");
            int itemIdColIndex = c.getColumnIndex("itemid");
            do{
                map.put(c.getInt(itemIdColIndex),c.getInt(countColIndex));
            } while (c.moveToNext());
        }
        c.close();
        ArrayList<CartItem> items = new ArrayList<>();
        for (Map.Entry<Integer,Integer> item : map.entrySet()){
            items.add(new CartItem(GetItem(item.getKey()),item.getValue()));
        }
        dbHelper.close();
        return items;
    }

    public ArrayList<Item> GetItems() {
        // need upgrading
        ItemDatabaseHelper dbHelper = new ItemDatabaseHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor c = db.query("itemdb", null, null, null, null, null, null);
        ArrayList<Item> res = new ArrayList<>();
        if (c.moveToFirst()) {
            int idColIndex = c.getColumnIndex("id");
            int nameColIndex = c.getColumnIndex("name");
            int smallDescrColIndex = c.getColumnIndex("smallDescr");
            int bigDescrColIndex = c.getColumnIndex("bigDescr");
            int ingridientsColIndex = c.getColumnIndex("ingridients");
            int priceColIndex = c.getColumnIndex("price");
            int imageColIndex = c.getColumnIndex("image");
            int ratingColIndex = c.getColumnIndex("rating");
            do {
                res.add(new Item(c.getString(nameColIndex),
                        c.getString(smallDescrColIndex),
                        c.getString(bigDescrColIndex),
                        c.getInt(imageColIndex),
                        c.getInt(idColIndex),
                        c.getString(ingridientsColIndex),
                        c.getDouble(priceColIndex),
                        c.getInt(ratingColIndex)));
            } while (c.moveToNext());
        }
        c.close();
        dbHelper.close();
        return res;
    }
}
