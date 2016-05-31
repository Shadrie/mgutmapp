package com.mgutm.sosinann.mgutmapp;

import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by 38.2 on 19.05.2016.
 */
public class Dialog extends DialogFragment implements View.OnClickListener {
    final String LOG_TAG = "myLogs";
    public String RESULT;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().setTitle("@string/app_name");
        View v = inflater.inflate(R.layout.dialog_msg, null);
        v.findViewById(R.id.buttonAgree).setOnClickListener(this);
        v.findViewById(R.id.buttonDisagree).setOnClickListener(this);
        return v;
    }

    @Override
    public void onClick(View v) {
        RESULT = (String) ((Button) v).getText();
        Log.d(LOG_TAG, "Результат: " + RESULT);
        if (RESULT.equals("Да")) {
            Log.d(LOG_TAG, "Ветка диалога: Да");
            HistoryActivity.onDelete();
            dismiss();
        }
        else {
            Log.d(LOG_TAG, "Ветка диалога: Нет");
            dismiss();
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        Log.d(LOG_TAG, "Выход из диалога");
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        Log.d(LOG_TAG, "Отмена диалога");
    }

}