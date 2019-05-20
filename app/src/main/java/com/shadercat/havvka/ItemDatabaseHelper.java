package com.shadercat.havvka;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class ItemDatabaseHelper extends SQLiteOpenHelper {
    public ItemDatabaseHelper(Context context) {
        super(context, "ItemDB", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table itemdb ("
                + "id integer primary key,"
                + "name text,"
                + "smallDescr text,"
                + "bigDescr text,"
                + "ingridients,"
                + "price double,"
                + "image integer,"
                + "rating integer" + ");");
        db.execSQL("create table favset (" +
                "id integer primary key, " +
                "name text, " +
                "count integer" + ");");
        db.execSQL("create table vafitem (" +
                "id integer," +
                "itemid integer," +
                "count integer," +
                "constraint fk_favset " +
                "foreign key (id) " +
                "references favset(id) " +
                "on update cascade " +
                "on delete cascade" + ");");
        db.execSQL("create table pictures(" +
                "id integer primary key," +
                "img blob," +
                "constraint fk_itemdb " +
                "foreign key (id) " +
                "references itemdb(id) " +
                "on update cascade " +
                "on delete cascade);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
