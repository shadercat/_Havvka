package com.shadercat.havvka;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

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
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.insertWithOnConflict("itemdb", null, cv, SQLiteDatabase.CONFLICT_IGNORE);
        dbHelper.close();
    }

    public void DeleteItemTable() {
        ItemDatabaseHelper dbHelper = new ItemDatabaseHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete("itemdb", null, null);
        dbHelper.close();
    }

    public ArrayList<Item> GetItems() {
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
