package com.mgutm.sosinann.mgutmapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Toast;

public class NewsActivity extends AppCompatActivity{

    // Переменные, соответсвующие элементам интерфейса определенного типа
    FrameLayout imgLayout;
    ImageButton newsButton;
    ImageButton advButton;
    ImageButton eventsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Задать отображаемый макет
        setContentView(R.layout.news);

        // Определить элементы интерфейса из макетов, соответствующие заданным переменным
        imgLayout = (FrameLayout) findViewById(R.id.imgLayout);
        newsButton = (ImageButton) findViewById(R.id.newsButton);
        advButton = (ImageButton) findViewById(R.id.advButton);
        eventsButton = (ImageButton) findViewById(R.id.eventsButton);

        // При нажатии на свободную область, производится возврат к главной странице
        View.OnClickListener GoHome = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(NewsActivity.this, MainActivity.class);
                startActivity(intent);
            }
        };
        imgLayout.setOnClickListener(GoHome);

        // Функция, совершающая переход в новостной раздел
        // по заданному url-адресу
        View.OnClickListener NewsClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewsActivity.this, ViewActivity.class);
                intent.putExtra("url", "http://mgutm.ru/content/news/");
                startActivity(intent);
            }
        };

        // Функция, совершающая переход в раздел объявлений
        // по заданному url-адресу
        View.OnClickListener AdvClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewsActivity.this, ViewActivity.class);
                intent.putExtra("url", "http://mgutm.ru/content/advertisement/");
                startActivity(intent);
            }
        };

        // Функция, совершающая переход в раздел событий
        // по заданному url-адресу
        View.OnClickListener EventsClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewsActivity.this, ViewActivity.class);
                intent.putExtra("url", "http://mgutm.ru/content/events/");
                startActivity(intent);
            }
        };

        // При наличии подключения, разработанные функции задаются определенным кнопкам,
        // иначе выводится сообщение об ошибке подключения
        if (!DetectConnection.checkInternetConnection(this)) {
            Toast.makeText(getApplicationContext(), "Подключение отсутствует!", Toast.LENGTH_SHORT).show();
        }
        else {
            newsButton.setOnClickListener(NewsClick);
            advButton.setOnClickListener(AdvClick);
            eventsButton.setOnClickListener(EventsClick);
        }
    }
}
