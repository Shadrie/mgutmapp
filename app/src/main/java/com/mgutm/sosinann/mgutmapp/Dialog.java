package com.mgutm.sosinann.mgutmapp;

import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

// Класс, служащий для отображения диалогового окна удаления элемента
public class Dialog extends DialogFragment implements View.OnClickListener {

    // Переменная, использующаяся для вывода сообщений в лог
    final String LOG_TAG = "myLogs";

    // Переменная, в которую будет заноситься результат диалога
    public String RESULT;

    // Создание диалогового окна по макету dialog_msg
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().setTitle(R.string.app_name);
        View v = inflater.inflate(R.layout.dialog_msg, null);
        v.findViewById(R.id.buttonAgree).setOnClickListener(this);
        v.findViewById(R.id.buttonDisagree).setOnClickListener(this);
        return v;
    }

    // Действия, выполняющиеся при нажатии на одну из кнопок диалогового окна
    @Override
    public void onClick(View v) {
        RESULT = (String) ((Button) v).getText();

        // Вывести результат диалога в консоль
        Log.d(LOG_TAG, "Результат: " + RESULT);
        if (RESULT.equals("Да")) {

            // Вывести сообщение о попадании в ветвь диалога "Да"
            Log.d(LOG_TAG, "Ветка диалога: Да");

            // Запуск функции удаления элемента
            HistoryActivity.onDelete();
            dismiss();
        }
        else {

            // Вывести сообщение о попадании в ветвь диалога "Нет"
            Log.d(LOG_TAG, "Ветка диалога: Нет");
            dismiss();
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);

        // Сообщение о выходе из диалога (т.е. завершении)
        Log.d(LOG_TAG, "Выход из диалога");
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);

        // Сообщение об отмене диалога кнопкой "Назад"
        Log.d(LOG_TAG, "Отмена диалога");
    }

}