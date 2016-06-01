package com.mgutm.sosinann.mgutmapp;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import java.io.InputStream;

public class ViewActivity extends AppCompatActivity {

    DB db;
    final String LOG_TAG = "myLogs";

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view);
        final WebView webView = (WebView) findViewById(R.id.webView);
        final WebView simpleView = (WebView) findViewById(R.id.simpleView);
        final WebView menuView = (WebView) findViewById(R.id.menuView);
        final LinearLayout tabContent = (LinearLayout) findViewById(R.id.tabContent);
        final TextView nameView = (TextView) findViewById(R.id.nameView);
        final TextView urlView = (TextView) findViewById(R.id.urlView);
        final TextView contentView = (TextView) findViewById(R.id.contentView);

//set loading foreground
        //webView.setForeground(ResourcesCompat.getDrawable(getResources(), R.drawable.loading, null));
        //simpleView.setForeground(ResourcesCompat.getDrawable(getResources(), R.drawable.loading, null));
        //menuView.setForeground(ResourcesCompat.getDrawable(getResources(), R.drawable.loading, null));
        //tabContent.setForeground(ResourcesCompat.getDrawable(getResources(), R.drawable.loading, null));
        webView.setVisibility(View.GONE);
        simpleView.setVisibility(View.GONE);
        menuView.setVisibility(View.GONE);

        WebSettings webSettings = webView.getSettings();
        WebSettings simpleSettings = simpleView.getSettings();
        WebSettings menuSettings = menuView.getSettings();

        webSettings.setJavaScriptEnabled(true);
        simpleSettings.setJavaScriptEnabled(true);
        menuSettings.setJavaScriptEnabled(true);

        class MyJavaScriptInterface
        {
            public TextView contentView;

            public MyJavaScriptInterface(TextView aContentView)
            {
                contentView = aContentView;
            }

            @SuppressWarnings("unused")
            @JavascriptInterface
            public void processContent(String aContent)
            {
                final String content = aContent;
                contentView.post(new Runnable() {
                    public void run() {
                        contentView.setText(content);
                    }
                });
            }
        }

        class NameGettingInterface
        {
            public TextView contentView;

            public NameGettingInterface(TextView aContentView)
            {
                contentView = aContentView;
            }

            @SuppressWarnings("unused")
            @JavascriptInterface
            public void processContent(String aContent)
            {
                final String content = aContent;
                nameView.post(new Runnable() {
                    public void run() {
                        nameView.setText(content);
                    }
                });
            }
        }
        //set interface for getting body content
        webView.addJavascriptInterface(new MyJavaScriptInterface(contentView), "INTERFACE");
        simpleView.addJavascriptInterface(new MyJavaScriptInterface(contentView), "INTERFACE");
        //set interface for getting header content
        webView.addJavascriptInterface(new NameGettingInterface(nameView), "INTERFACE2");
        simpleView.addJavascriptInterface(new NameGettingInterface(nameView), "INTERFACE2");

        webView.setDownloadListener(new DownloadListener() {
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                //download file using web browser
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                // Inject CSS when page is done loading
                applyCSS(webView, "custom.css");
                webView.loadUrl("javascript: window.CallToAnAndroidFunction.setVisible()");
                //use client interface to set variables Работает так: 1)Написать интерфейс 2)Приделать ему название и вставить в клиент 3)ЖС запрос
                view.loadUrl("javascript:window.INTERFACE.processContent(document.getElementsByTagName('body')[0].innerText);");
                view.loadUrl("javascript:window.INTERFACE2.processContent(document.getElementsByTagName('title')[0].innerText);");
                urlView.setText(url);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                view.loadUrl(url);
                menuView.loadUrl(url);
                simpleView.loadUrl(url);
                return true;
            }

        });
        simpleView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {

                // Inject CSS when page is done loading
                applyCSS(simpleView, "plain.css");
                super.onPageFinished(view, url);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                webView.loadUrl(url);
                menuView.loadUrl(url);
                return true;
            }
        });
        menuView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {

                // Inject CSS when page is done loading
                applyCSS(menuView, "menu.css");
                super.onPageFinished(view, url);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                webView.loadUrl(url);
                simpleView.loadUrl(url);
                return true;
            }
        });

        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        simpleView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        menuView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        contentView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

//
        webView.addJavascriptInterface(new showWeb(), "CallToAnAndroidFunction");
        //
        Intent intent = getIntent();
        String siteUrl = intent.getStringExtra("url");
        webView.loadUrl(siteUrl);
        simpleView.loadUrl(siteUrl);
        menuView.loadUrl(siteUrl);
//warning
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.view_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // получим идентификатор выбранного пункта меню
        int id = item.getItemId();
        final WebView webView = (WebView) findViewById(R.id.webView);
        final WebView simpleView = (WebView) findViewById(R.id.simpleView);
        final WebView menuView = (WebView) findViewById(R.id.menuView);
        item.setChecked(true);
        // Операции для выбранного пункта меню
        switch (id) {
            case R.id.action_normal:{
                simpleView.setVisibility(View.GONE);
                menuView.setVisibility(View.GONE);
                webView.setVisibility(View.VISIBLE);
                return true;}
            case R.id.action_simple:{
                webView.setVisibility(View.GONE);
                menuView.setVisibility(View.GONE);
                simpleView.setVisibility(View.VISIBLE);
                return true;}
            case R.id.action_menu:{
                webView.setVisibility(View.GONE);
                simpleView.setVisibility(View.GONE);
                menuView.setVisibility(View.VISIBLE);
                return true;}
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onFavouriteMenuClick(MenuItem item) {
        db = new DB(this);
        db.open();
        final TextView nameView = (TextView) findViewById(R.id.nameView);
        final TextView urlView = (TextView) findViewById(R.id.urlView);
        final TextView contentView = (TextView) findViewById(R.id.contentView);
        ContentValues cv = new ContentValues();
        // получаем данные из полей ввода
        String titleData = nameView.getText().toString();
        String urlData = urlView.getText().toString();
        String contentData = contentView.getText().toString();
        // подключаемся к БД
        //SQLiteDatabase favesDB = dbHelper.getWritableDatabase();

        cv.put("title", titleData);
        //cv.put("url", urlData);
        cv.put("content", contentData);
        // вставляем запись и получаем ее ID
        long rowID = db.mDB.insert("favourites", null, cv);
        Log.d(LOG_TAG, "row inserted, ID = " + rowID);
        db.close();
        Log.d(LOG_TAG, "title " + nameView.getText().toString());
        Toast.makeText(getApplicationContext(), "Сохранено!", Toast.LENGTH_SHORT).show();
    }
    // Inject CSS method: read style.css from assets folder
// Append stylesheet to document head

    private void applyCSS(WebView view, String css) {
        try {
            InputStream inputStream = getAssets().open(css);
            byte[] buffer = new byte[inputStream.available()];
            inputStream.read(buffer);
            inputStream.close();
            String encoded = Base64.encodeToString(buffer, Base64.NO_WRAP);
            view.loadUrl("javascript:(function() {" +
                    "var parent = document.getElementsByTagName('head').item(0);" +
                    "var style = document.createElement('style');" +
                    "style.type = 'text/css';" +
                    "style.innerHTML = window.atob('" + encoded + "');" +
                    "parent.appendChild(style)" +
                    "})()");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class showWeb {
        @JavascriptInterface
        public void setVisible() {
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    final WebView webView = (WebView) findViewById(R.id.webView);
                    webView.setVisibility(View.VISIBLE);
                }
            });
        }
    }
}