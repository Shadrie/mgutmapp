package com.mgutm.sosinann.mgutmapp;

import android.app.DialogFragment;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.SimpleCursorTreeAdapter;
import android.widget.Toast;

public class HistoryActivity extends AppCompatActivity {

    ExpandableListView elvMain;
    static DB db;
    static Dialog delDialog;
    public static int stringID;

    /** Called when the activity is first created. */
    @SuppressWarnings("deprecation")
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history);

        // подключаемся к БД
        db = new DB(this);
        db.open();
        delDialog = new Dialog();

        // готовим данные по группам для адаптера
        Cursor cursor = db.getTitleData();
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
                    final String LOG_TAG = "myLogs";
                    Log.d(LOG_TAG, "Long " + position);
                    stringID = position + 1;
                    delDialog.show(getFragmentManager(), "delDialog");
                    return false;
                }
            });
        }
    }

    public static void onDelete() {
        final String LOG_TAG = "myLogs";
        Log.d(LOG_TAG, "Результат диалога: " + delDialog.RESULT);
        if (delDialog.RESULT.equals("Да")) {
            Log.d(LOG_TAG, "Удаляемая запись: " + stringID);
            db.mDB.delete(db.FAVES_TABLE, "_id = " + stringID, null);
//обновить вид экрана
        }
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
}