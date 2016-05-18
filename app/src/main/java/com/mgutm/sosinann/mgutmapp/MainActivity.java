package com.mgutm.sosinann.mgutmapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    DB db;
    ImageButton newsPanel;
    ImageButton categoriesPanel;
    ImageButton contactsPanel;
    ImageButton savedItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        newsPanel = (ImageButton) findViewById(R.id.newsPanel);
        categoriesPanel = (ImageButton) findViewById(R.id.categoriesPanel);
        contactsPanel = (ImageButton) findViewById(R.id.contactsPanel);
        savedItems = (ImageButton) findViewById(R.id.savedItems);

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
            Toast.makeText(getApplicationContext(), "No Internet!", Toast.LENGTH_SHORT).show();
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
}
