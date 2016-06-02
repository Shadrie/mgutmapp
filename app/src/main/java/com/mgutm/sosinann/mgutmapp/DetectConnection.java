package com.mgutm.sosinann.mgutmapp;

        import android.content.Context;
        import android.net.ConnectivityManager;

// Класс, определяющий состояния подключения к сети
public class DetectConnection {
    public static boolean checkInternetConnection(Context context) {

        ConnectivityManager con_manager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);

        return con_manager.getActiveNetworkInfo() != null
                && con_manager.getActiveNetworkInfo().isAvailable()
                && con_manager.getActiveNetworkInfo().isConnected();
    }
}
