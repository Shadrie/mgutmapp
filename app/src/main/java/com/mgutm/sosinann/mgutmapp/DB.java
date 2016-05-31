package com.mgutm.sosinann.mgutmapp;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DB {

    private static final String DB_NAME = "pagesDb";
    private static final int DB_VERSION = 11;
    final String LOG_TAG = "myLogs";
    String[] column = null;

    // имя таблицы компаний, поля и запрос создания
    public static final String FAVES_TABLE = "favourites";
    public static final String FAVES_COLUMN_ID = "_id";
    public static final String TITLE_COLUMN = "title";
    public static final String CONTENT_COLUMN = "content";
    private static final String FAVES_TABLE_CREATE = "create table "
            + FAVES_TABLE + "(" + FAVES_COLUMN_ID
            + " integer primary key autoincrement, " + TITLE_COLUMN + " text, " + CONTENT_COLUMN + " text" + ");";

    // имя таблицы телефонов, поля и запрос создания

    public final Context myContext;

    public DBHelper mDBHelper;
    public SQLiteDatabase mDB;

    public DB(Context context) {
        myContext = context;
    }



    // открываем подключение
    public void open() {
        mDBHelper = new DBHelper(myContext, DB_NAME, null, DB_VERSION);
        mDB = mDBHelper.getWritableDatabase();
    }

    // закрываем подключение
    public void close() {
        if (mDBHelper != null)
            mDBHelper.close();
    }

    // данные по компаниям
    public Cursor getTitleData() {
        column = new String[] { FAVES_COLUMN_ID, TITLE_COLUMN, CONTENT_COLUMN };
        return mDB.query(FAVES_TABLE, column, null, null, null, null, null);
    }

    // данные по телефонам конкретной группы
    public Cursor getContentData(long companyID) {
        column = new String[] { FAVES_COLUMN_ID, TITLE_COLUMN, CONTENT_COLUMN };
        return mDB.query(FAVES_TABLE, column, FAVES_COLUMN_ID + " = "
                + companyID, null, null, null, null);
    }

    public class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,
                        int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            db.execSQL(FAVES_TABLE_CREATE);

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.d(LOG_TAG, " --- onUpgrade database from " + oldVersion
                    + " to " + newVersion + " version --- ");
            db.execSQL("drop table favourites;");

            db.execSQL(FAVES_TABLE_CREATE);
            }
        }
    }
