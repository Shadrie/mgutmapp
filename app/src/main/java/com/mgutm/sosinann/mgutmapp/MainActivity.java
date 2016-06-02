package com.mgutm.sosinann.mgutmapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    final static String LOG_TAG = "myLogs";
    DB db;
    ImageButton newsPanel;
    ImageButton categoriesPanel;
    ImageButton contactsPanel;
    ImageButton savedItems;
    private String[] extraItems;
    private ListView extraView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        newsPanel = (ImageButton) findViewById(R.id.newsPanel);
        categoriesPanel = (ImageButton) findViewById(R.id.categoriesPanel);
        contactsPanel = (ImageButton) findViewById(R.id.contactsPanel);
        savedItems = (ImageButton) findViewById(R.id.savedItems);

        // get list items from strings.xml
        extraItems = getResources().getStringArray(R.array.extra_array);

        // get ListView defined in activity_main.xml
        extraView = (ListView) findViewById(R.id.left_drawer);

        // Set the adapter for the list view
        extraView.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_item, extraItems));

        extraView.setOnItemClickListener(new DrawerItemClickListener());

        View.OnClickListener NewsPanelClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NewsActivity.class);
                startActivity(intent);
            }
        };
        newsPanel.setOnClickListener(NewsPanelClick);

        View.OnClickListener CategoriesPanelClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CategoriesActivity.class);
                startActivity(intent);
            }
        };
        categoriesPanel.setOnClickListener(CategoriesPanelClick);

        View.OnClickListener ContactsPanelClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ViewActivity.class);
                intent.putExtra("url", "http://mgutm.ru/contacts/");
                startActivity(intent);
            }
        };
        if (!DetectConnection.checkInternetConnection(this)) {
            Toast.makeText(getApplicationContext(), "Подключение отсутствует!", Toast.LENGTH_SHORT).show();
        }
        else {
            contactsPanel.setOnClickListener(ContactsPanelClick);
        }
        db = new DB(this);
        db.open();
        db.close();

        View.OnClickListener SavedPanelClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
                startActivity(intent);
            }
        };
        savedItems.setOnClickListener(SavedPanelClick);
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            String clickedItem = ((TextView)view).getText().toString();
            String url = null;
            Log.d(LOG_TAG, "Нажато = " + clickedItem);
            switch(clickedItem) {
                case "Оплата обучения":
                    url = "http://mgutm.ru/oplata-za-obuchenie.php";
                    break;
                case "Стратегия развития":
                    url = "http://mgutm.ru/kazachestvo/strategiya_razvitiya.php";
                    break;
                case "Значимые события":
                    url = "http://mgutm.ru/znachemie_sobitiya.php";
                    break;
                case "Журнал Университетская жизнь":
                    url = "http://mgutm.ru/jurnal/universitetskaya-zhizn.php?jur=http://j.mgutm.ru/index.php/01-yanvar-fevral-studencheskij-vypusk";
                    break;
                case "Видео Университета":
                    url = "http://mgutm.ru/video_universiteta/";
                    break;
                case "Печатные издания Университета":
                    url = "http://mgutm.ru/jurnal/";
                    break;
                case "Кластер непрерывного казачьего образования":
                    url = "http://mgutm.ru/lektsii-pensioneram-kazakam/";
                    break;
            }
            Log.d(LOG_TAG, "URL = " + url);
            Intent intent = new Intent(MainActivity.this, ViewActivity.class);
            intent.putExtra("url", url);
            startActivity(intent);
        }
    }
}
