package com.mgutm.sosinann.mgutmapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

public class NewsActivity extends AppCompatActivity{

    FrameLayout imgLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news);
        imgLayout = (FrameLayout) findViewById(R.id.imgLayout);

        View.OnClickListener GoHome = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(NewsActivity.this, MainActivity.class);
                startActivity(intent);
            }
        };
        imgLayout.setOnClickListener(GoHome);
    }
}
