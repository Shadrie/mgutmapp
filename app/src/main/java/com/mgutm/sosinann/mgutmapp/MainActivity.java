package com.mgutm.sosinann.mgutmapp;

import android.content.Intent;
import android.os.Bundle;
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

    // Переменная, использующаяся для вывода сообщений в лог
    final static String LOG_TAG = "Лог: ";

    // База данных приложения
    DB db;

    // Переменные, соответсвующие элементам интерфейса определенного типа
    ImageButton newsPanel;
    ImageButton categoriesPanel;
    ImageButton contactsPanel;
    ImageButton savedItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Задать отображаемый макет
        setContentView(R.layout.main);

        // Определить элементы интерфейса из макетов, соответствующие заданным переменным
        newsPanel = (ImageButton) findViewById(R.id.newsPanel);
        categoriesPanel = (ImageButton) findViewById(R.id.categoriesPanel);
        contactsPanel = (ImageButton) findViewById(R.id.contactsPanel);
        savedItems = (ImageButton) findViewById(R.id.savedItems);

        // Получить значения для меню из файла strings.xml
        String[] extraItems = getResources().getStringArray(R.array.extra_array);

        // Определить список, в котором будут храниться значения
        ListView extraView = (ListView) findViewById(R.id.left_drawer);

        // Наполнить список
        if (extraView != null) {
            extraView.setAdapter(new ArrayAdapter<>(this,
                    R.layout.drawer_item, extraItems));
        }

        // При наличии подключения, задать функции на клик по меню, иначе выдать сообщение об ошибке
        if (extraView != null) {
            if (!DetectConnection.checkInternetConnection(this)) {
                Toast.makeText(getApplicationContext(), "Подключение отсутствует!", Toast.LENGTH_SHORT).show();
            }
            else {
                extraView.setOnItemClickListener(new DrawerItemClickListener());
            }
        }

        // При нажатии на кнопку "Новостная лента", перейти к новостной активности
        View.OnClickListener NewsPanelClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NewsActivity.class);
                startActivity(intent);
            }
        };
        newsPanel.setOnClickListener(NewsPanelClick);

        // При нажатии на кнопку "Информация об университете", перейти к информационной активности
        View.OnClickListener CategoriesPanelClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CategoriesActivity.class);
                startActivity(intent);
            }
        };
        categoriesPanel.setOnClickListener(CategoriesPanelClick);

        // При наличии подключения, перейти к списку контактов по клику по кнопке "Контакты"
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

        //Открыть подключение к базе данных; При отсутсвии базы, создает ее; Закрыть подключение
        db = new DB(this);
        db.open();
        db.close();

        // При нажатии кнопки "Избранное", перейти к активности избранного
        View.OnClickListener SavedPanelClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
                startActivity(intent);
            }
        };
        savedItems.setOnClickListener(SavedPanelClick);
    }

    // Класс, задающий действия, выполняющиеся при выборе элементов меню
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {

            // Получить строковое значение нажатого элемента меню
            String clickedItem = ((TextView)view).getText().toString();
            String url = null;

            //Вывод значения нажатого элемента
            Log.d(LOG_TAG, "Нажато = " + clickedItem);

            // В зависимости от значения переменной, записать в переменную ссылку
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
            // Вывод полученного URL в лог
            Log.d(LOG_TAG, "URL = " + url);

            // Перейти по ссылке в другую активность
            Intent intent = new Intent(MainActivity.this, ViewActivity.class);
            intent.putExtra("url", url);
            startActivity(intent);
        }
    }
}
