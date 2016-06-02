package com.mgutm.sosinann.mgutmapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.SimpleCursorTreeAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class HistoryActivity extends AppCompatActivity {

    final static String LOG_TAG = "myLogs";
    ExpandableListView elvMain;
    static DB db;
    static Dialog delDialog;
    public static String stringID;
    private static Context mContext;
    static Activity activity;


    /** Called when the activity is first created. */
    @SuppressWarnings("deprecation")
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history);
        mContext = this;
        activity = this;

        // подключаемся к БД
        db = new DB(this);
        db.open();
        delDialog = new Dialog();

        // готовим данные по группам для адаптера
        Cursor cursor = db.getTitleData();
        int row = cursor.getCount();
        Log.d(LOG_TAG, "Row " + row);
        startManagingCursor(cursor);
        // сопоставление данных и View для групп
        String[] groupFrom = { DB.TITLE_COLUMN };
        int[] groupTo = { android.R.id.text1 };
        // сопоставление данных и View для элементов
        String[] childFrom = { DB.CONTENT_COLUMN };
        int[] childTo = { android.R.id.text1 };

        // создаем адаптер и настраиваем список
        SimpleCursorTreeAdapter sctAdapter = new MyAdapter(this, cursor,
                android.R.layout.simple_expandable_list_item_1, groupFrom,
                groupTo, android.R.layout.simple_list_item_1, childFrom,
                childTo);
        elvMain = (ExpandableListView) findViewById(R.id.elvMain);
        if (elvMain != null) {
            elvMain.setAdapter(sctAdapter);
            elvMain.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    Toast.makeText(getApplicationContext(), "Long Click!", Toast.LENGTH_SHORT).show();
                    TextView v = (TextView)view.findViewById(android.R.id.text1);
                    String itemId = v.getText().toString();
                    Log.d(LOG_TAG, "Long test " + itemId);
                    Log.d(LOG_TAG, "Long tes2 " + position);
                    Log.d(LOG_TAG, "Long test3 " + Long.toString(id));
                    stringID = itemId;
                    delDialog.show(getFragmentManager(), "delDialog");
                    return false;
                }
            });
        }
    }

    public static void onDelete() {
        Log.d(LOG_TAG, "Результат диалога: " + delDialog.RESULT);
        if (delDialog.RESULT.equals("Да")) {
            Log.d(LOG_TAG, "Удаляемая запись: " + stringID);
            //db.mDB.delete(db.FAVES_TABLE, "title = '" + stringID + "'", null);
            db.mDB.execSQL("delete from " + DB.FAVES_TABLE +
                    " where title = '" +stringID+ "' and " + DB.FAVES_COLUMN_ID +" in (select "+
                    DB.FAVES_COLUMN_ID +" from "+ DB.FAVES_TABLE +" where title = '" +stringID+ "' order by _id LIMIT 1);");
//обновить вид экрана
            Log.d(LOG_TAG, "Reload");
            goToHistoryActivity(mContext);
        }
    }

    public static void goToHistoryActivity(Context mContext) {
        Intent login = new Intent(mContext, HistoryActivity.class);
        mContext.startActivity(login);
        activity.finish();
    }

    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }

    class MyAdapter extends SimpleCursorTreeAdapter {

        public MyAdapter(Context context, Cursor cursor, int groupLayout,
                         String[] groupFrom, int[] groupTo, int childLayout,
                         String[] childFrom, int[] childTo) {
            super(context, cursor, groupLayout, groupFrom, groupTo,
                    childLayout, childFrom, childTo);
        }

        protected Cursor getChildrenCursor(Cursor groupCursor) {
            // получаем курсор по элементам для конкретной группы
            int idColumn = groupCursor.getColumnIndex(DB.FAVES_COLUMN_ID);
            return db.getContentData(groupCursor.getInt(idColumn));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.history_menu, menu);
        return true;
    }

    public void onClearMenuClick(MenuItem item) {
        db = new DB(this);
        db.open();
        int clearCount = db.mDB.delete(DB.FAVES_TABLE, null, null);
        Log.d(LOG_TAG, "deleted rows count = " + clearCount);
        db.close();
        goToHistoryActivity(mContext);
    }
}