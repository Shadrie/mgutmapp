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

public class HistoryActivity extends AppCompatActivity {

    // Переменная, использующаяся для вывода сообщений в лог
    final static String LOG_TAG = "myLogs";

    // Список, использующийся для отображения сохраненных страниц
    ExpandableListView elvMain;

    // База данных приложения
    static DB db;

    // Диалог удаления
    static Dialog delDialog;

    // Переменная, использующаяся для удаления конкретной строки
    public static String stringID;

    // Контекст приложения
    private static Context mContext;

    // Текущая активность
    static Activity activity;


    @SuppressWarnings("deprecation")
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Задать отображаемый макет
        setContentView(R.layout.history);

        // Передать в переменные данные о контексте приложения и текущей активности
        mContext = this;
        activity = this;

        // Подключение к базе данных
        db = new DB(this);
        db.open();

        // Инициализация диалога
        delDialog = new Dialog();

        // Курсор считывает данные по группам для помещения в адаптер
        Cursor cursor = db.getTitleData();

        // Вывод количества строк, попавших в курсор, в консоль
        int row = cursor.getCount();
        Log.d(LOG_TAG, "Row " + row);
        startManagingCursor(cursor);

        // Сопоставление данных и View для групп (родительских элементов)
        String[] groupFrom = { DB.TITLE_COLUMN };
        int[] groupTo = { android.R.id.text1 };

        // Сопоставление данных и View для элементов (дочерних элементов)
        String[] childFrom = { DB.CONTENT_COLUMN };
        int[] childTo = { android.R.id.text1 };

        // Создание адаптер и настройка списка
        SimpleCursorTreeAdapter sctAdapter = new MyAdapter(this, cursor,
                android.R.layout.simple_expandable_list_item_1, groupFrom,
                groupTo, android.R.layout.simple_list_item_1, childFrom,
                childTo);
        elvMain = (ExpandableListView) findViewById(R.id.elvMain);
        if (elvMain != null) {
            elvMain.setAdapter(sctAdapter);

            // Задание функции, выполняющейся при долгом нажатии на родительский элемент
            elvMain.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    TextView v = (TextView)view.findViewById(android.R.id.text1);
                    String itemId = v.getText().toString();

                    // Вывод id, позиции в списке и содержания родительского элемента в консоль
                    Log.d(LOG_TAG, "ID = " + Long.toString(id));
                    Log.d(LOG_TAG, "Значение = " + itemId);
                    Log.d(LOG_TAG, "Позиция = " + position);
                    stringID = itemId;

                    // Вызов диалога удаления
                    delDialog.show(getFragmentManager(), "delDialog");
                    return false;
                }
            });
        }
    }

    // Функция удаления элемента
    public static void onDelete() {

        // Вывод результата диалога в консоль
        Log.d(LOG_TAG, "Результат диалога: " + delDialog.RESULT);
        if (delDialog.RESULT.equals("Да")) {

            // Вывод заголовка удаляемой записи в консоль
            Log.d(LOG_TAG, "Удаляемая запись: " + stringID);

            // Удаление записи путем выполнения скрипта SQL
            db.mDB.execSQL("delete from " + DB.FAVES_TABLE +
                    " where title = '" +stringID+ "' and " + DB.FAVES_COLUMN_ID +" in (select "+
                    DB.FAVES_COLUMN_ID +" from "+ DB.FAVES_TABLE +" where title = '" +stringID+ "' order by _id LIMIT 1);");

            // Перезагрузка активности для обновления вида экрана
            Log.d(LOG_TAG, "Перезагрузка активности");
            goToHistoryActivity(mContext);
        }
    }

    // Перезагрузка активности для обновления вида экрана
    public static void goToHistoryActivity(Context mContext) {
        activity.recreate();
    }

    // Закрытие подключения к базе при выходе из активности
    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }

    // Адаптер для отображения данных в списке
    class MyAdapter extends SimpleCursorTreeAdapter {

        public MyAdapter(Context context, Cursor cursor, int groupLayout,
                         String[] groupFrom, int[] groupTo, int childLayout,
                         String[] childFrom, int[] childTo) {
            super(context, cursor, groupLayout, groupFrom, groupTo,
                    childLayout, childFrom, childTo);
        }

        // Курсор, получающий данные родительского элемента по дочернему
        protected Cursor getChildrenCursor(Cursor groupCursor) {
            // получаем курсор по элементам для конкретной группы
            int idColumn = groupCursor.getColumnIndex(DB.FAVES_COLUMN_ID);
            return db.getContentData(groupCursor.getInt(idColumn));
        }
    }

    // Создание меню по макету меню history_menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.history_menu, menu);
        return true;
    }

    // Действия, совершаемые по клику пункта меню "Очистить"
    public void onClearMenuClick(MenuItem item) {

        // Подключение к базе данных
        db = new DB(this);
        db.open();

        // Вывод в консоль количества удаленных строк
        int clearCount = db.mDB.delete(DB.FAVES_TABLE, null, null);
        Log.d(LOG_TAG, "Всего удалено = " + clearCount);

        // Закрытие подключения
        db.close();

        // Перезагрузка активности для обновления вида экрана
        Log.d(LOG_TAG, "Перезагрузка активности");
        goToHistoryActivity(mContext);
    }
}