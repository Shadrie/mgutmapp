package com.mgutm.sosinann.mgutmapp;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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
import android.widget.TextView;
import android.widget.Toast;
import java.io.InputStream;

@SuppressWarnings("ALL")
public class ViewActivity extends AppCompatActivity {

    // Переменная, использующаяся для вывода сообщений в лог
    final String LOG_TAG = "Лог: ";

    // База данных приложения
    DB db;

    // Определенные сообщения должны быть проигнорированы
    @SuppressLint({"NewApi", "SetJavaScriptEnabled"})

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Задать отображаемый макет
        setContentView(R.layout.view);

        // Определить переменные, соответствующие элементам интерфейса макета
        final WebView webView = (WebView) findViewById(R.id.webView);
        final WebView simpleView = (WebView) findViewById(R.id.simpleView);
        final WebView menuView = (WebView) findViewById(R.id.menuView);
        final TextView nameView = (TextView) findViewById(R.id.nameView);
        final TextView urlView = (TextView) findViewById(R.id.urlView);
        final TextView contentView = (TextView) findViewById(R.id.contentView);

        // assert - утверждение
        // Утверждать, что переменные, соответсвующие веб-компонентам существуют
        // После чего, отключить их отображение
        assert webView != null;
        webView.setVisibility(View.GONE);
        assert simpleView != null;
        simpleView.setVisibility(View.GONE);
        assert menuView != null;
        menuView.setVisibility(View.GONE);

        // Определить для каждого веб-компонента один экземпляр класса настроек
        WebSettings webSettings = webView.getSettings();
        WebSettings simpleSettings = simpleView.getSettings();
        WebSettings menuSettings = menuView.getSettings();

        // В настройках для каждого веб-компонента включить javascript
        webSettings.setJavaScriptEnabled(true);
        simpleSettings.setJavaScriptEnabled(true);
        menuSettings.setJavaScriptEnabled(true);

        // Создать класс, ображение к которому происходит через javascript
        class DataSettingInterface
        {

            // Переменные, в которые будет записываться результат выполнения функций класса
            public TextView nameView;
            public TextView contentView;

            public DataSettingInterface(TextView aNameView, TextView aContentView)
            {
                nameView = aNameView;
                contentView = aContentView;
            }

            // Функция, которая записывает результат выполнения javascript-функции в текстовое
            // поле nameView
            @SuppressWarnings("unused")
            @JavascriptInterface
            public void processName(String aName)
            {
                final String name = aName;
                if (nameView != null) {
                    nameView.post(new Runnable() {
                        public void run() {
                            nameView.setText(name);
                        }
                    });
                }
            }

            // Функция, которая записывает результат выполнения javascript-функции в текстовое
            // поле contentView
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

        // Добавить интерфейсы javascript в веб-компонент; Т.к. в программе загружаются одновременно
        // три компонента, достаточно добавить интерфейс к одному
        webView.addJavascriptInterface(new DataSettingInterface(nameView, contentView), "INTERFACE");
        webView.addJavascriptInterface(new showWeb(), "CallToAnAndroidFunction");

        // Задать веб-компоненту загрузчик файлов. При нажатии она также будет открываться в трех
        // компонентах одновременно, поэтому достаточно добавить загрузчик одному
        webView.setDownloadListener(new DownloadListener() {
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        // Задать клиент для компонента webView
        webView.setWebViewClient(new WebViewClient() {

            // Действия, выполняемые по окончанию загрузки веб-страницы
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

                // Применить к веб-странице css-файл custom.css
                applyCSS(view, "custom.css");

                // Включить отображение веб-компонента
                view.loadUrl("javascript: window.CallToAnAndroidFunction.setVisible()");

                // Записать в переменные результаты javascript-функций, обратившись к интерфейсу
                view.loadUrl("javascript:window.INTERFACE.processContent(document.getElementsByTagName('body')[0].innerText);");
                view.loadUrl("javascript:window.INTERFACE.processName(document.getElementsByTagName('title')[0].innerText);");

                // Записать в переменную URL-адрес текущей страницы
                if (urlView != null) {
                    urlView.setText(url);
                }
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                // Когда загружается новая страница, также загружать ее и в другие компоненты
                view.loadUrl(url);
                menuView.loadUrl(url);
                simpleView.loadUrl(url);
                return true;
            }

        });

        // Задать клиент для компонента simpleView
        simpleView.setWebViewClient(new WebViewClient() {

            // Действия, выполняемые по окончанию загрузки веб-страницы
            @Override
            public void onPageFinished(WebView view, String url) {

                // Применить к веб-странице css-файл plain.css
                applyCSS(simpleView, "plain.css");
                super.onPageFinished(view, url);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                // Когда загружается новая страница, также загружать ее и в другие компоненты
                view.loadUrl(url);
                webView.loadUrl(url);
                menuView.loadUrl(url);
                return true;
            }
        });

        // Задать клиент для компонента menuView
        menuView.setWebViewClient(new WebViewClient() {

            // Действия, выполняемые по окончанию загрузки веб-страницы
            @Override
            public void onPageFinished(WebView view, String url) {

                // Применить к веб-странице css-файл menu.css
                applyCSS(menuView, "menu.css");
                super.onPageFinished(view, url);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                // Когда загружается новая страница, также загружать ее и в другие компоненты
                view.loadUrl(url);
                webView.loadUrl(url);
                simpleView.loadUrl(url);
                return true;
            }
        });

        // Задать стиль полосы прокрутки трем веб-компонентам
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        simpleView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        menuView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

        // Получить переданные с переходом в активность данные о url-адресе
        Intent intent = getIntent();
        String siteUrl = intent.getStringExtra("url");

        // Перейти по url-адресу
        webView.loadUrl(siteUrl);
        simpleView.loadUrl(siteUrl);
        menuView.loadUrl(siteUrl);
    }

    // Создать заголовочное меню, используя макет меню menu_view
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.view_menu, menu);
        return true;
    }

    // Выполнение различный действий при выборе различных пунктов меню
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Получение идентификатора выбранного пункта меню
        int id = item.getItemId();

        // Определить переменные, соответствующие элементам интерфейса макета
        final WebView webView = (WebView) findViewById(R.id.webView);
        final WebView simpleView = (WebView) findViewById(R.id.simpleView);
        final WebView menuView = (WebView) findViewById(R.id.menuView);

        // Поставить переключатель на нажатый пукт меню
        item.setChecked(true);

        // Убеждать, что веб-компоненты существуют
        assert simpleView != null;
        assert menuView != null;
        assert webView != null;

        // Выполнить операции для выбранного пункта меню
        switch (id) {
            case R.id.action_normal:{

                //Выбран пункт "Стандартный вид". Отобразить webView, скрыть остальные веб-компоненты
                simpleView.setVisibility(View.GONE);
                menuView.setVisibility(View.GONE);
                webView.setVisibility(View.VISIBLE);
                return true;
            }
            case R.id.action_simple:{

                //Выбран пункт "Только текст". Отобразить simpleView, скрыть остальные веб-компоненты
                webView.setVisibility(View.GONE);
                menuView.setVisibility(View.GONE);
                simpleView.setVisibility(View.VISIBLE);
                return true;
            }
            case R.id.action_menu:{

                //Выбран пункт "Меню". Отобразить menuView, скрыть остальные веб-компоненты
                webView.setVisibility(View.GONE);
                simpleView.setVisibility(View.GONE);
                menuView.setVisibility(View.VISIBLE);
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // Выбор пункта "Сохранить"
    public void onFavouriteMenuClick(MenuItem item) {

        // Открыть подключение к базе данных
        db = new DB(this);
        db.open();

        // Определить переменные, соответствующие элементам интерфейса макета
        final TextView nameView = (TextView) findViewById(R.id.nameView);
        final TextView urlView = (TextView) findViewById(R.id.urlView);
        final TextView contentView = (TextView) findViewById(R.id.contentView);

        // Создать объект, в который будут зановиться данные для сохранения
        ContentValues cv = new ContentValues();

        // Получить данные из элементов интерфейса, в которые они были занесены ранее через javascript
        String titleData = null;
        if (nameView != null) {
            titleData = nameView.getText().toString();

            //Вывод в лог сообщения со значением заголовка страницы
            Log.d(LOG_TAG, "Заголовок = " + titleData);
        }
        if (urlView != null) {
            String urlData = urlView.getText().toString();

            //Вывод в лог сообщения со значением URL-адреса страницы
            Log.d(LOG_TAG, "URL = " + urlData);
        }
        String contentData = null;
        if (contentView != null) {
            contentData = contentView.getText().toString();

            //Вывод в лог сообщения со содержимым страницы
            Log.d(LOG_TAG, "Контент = " + contentData);
        }

        // Занесение данных о заголовке и содержимом как пар "название-значение"
        cv.put("title", titleData);
        cv.put("content", contentData);

        // Занесение информации в базу данных и передача номера занесенной строки в переменную
        long rowID = db.mDB.insert("favourites", null, cv);

        // Вывод в лог сообщения с номером занесенной строки
        Log.d(LOG_TAG, "Внесено = " + rowID);

        // Закрытие базы данных
        db.close();

        // Вывод сообщения о сохранении пользователю
        Toast.makeText(getApplicationContext(), "Сохранено!", Toast.LENGTH_SHORT).show();
    }

    // Метод применения css-файла
    private void applyCSS(WebView view, String css) {
        try {

            // Передача содержимого css-файла из раздела assets поток входных данных
            InputStream inputStream = getAssets().open(css);

            // Занесение потока в буфер
            byte[] buffer = new byte[inputStream.available()];
            inputStream.read(buffer);

            // Закрытие потока
            inputStream.close();

            // Перевод данных из буфера в строку
            String encoded = Base64.encodeToString(buffer, Base64.NO_WRAP);

            // Выполнение javascript-функции, в результате которой создается style и в него
            //заносится содержимое css-файла
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

    // Функция, используемая ранее для отображения webView через javascript
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