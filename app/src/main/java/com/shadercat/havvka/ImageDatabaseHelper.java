package com.shadercat.havvka;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ImageDatabaseHelper extends SQLiteOpenHelper {
    public ImageDatabaseHelper(Context context) {
        super(context, "ItemDB", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table imagedb ("
                + "id integer primary key,"
                + "img blob" + ");");
        db.execSQL("create table itemdb ("
                + "id integer primary key,"
                + "name text,"
                + "smallDescr text,"
                + "bigDescr text,"
                + "ingridients,"
                + "price double,"
                + "image integer,"
                + "rating integer" + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
