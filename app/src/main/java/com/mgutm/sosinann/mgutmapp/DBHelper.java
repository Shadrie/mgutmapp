package com.mgutm.sosinann.mgutmapp;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {

    public DBHelper(Context context) {
        super(context, "pagesDb", null, 11);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table favourites (_id integer primary key autoincrement, title text, content text;");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
