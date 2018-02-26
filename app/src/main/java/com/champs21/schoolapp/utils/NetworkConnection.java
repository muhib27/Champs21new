package com.champs21.schoolapp.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by RR on 11-Feb-18.
 */

public class NetworkConnection {
    Context context;
    private static final NetworkConnection instance = new NetworkConnection();

    private NetworkConnection() {
    }

    public static NetworkConnection getInstance() {
        return instance;
    }
    public boolean isNetworkAvailable() {
        try {
            ConnectivityManager cm = (ConnectivityManager) MyApplication.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
            //NetworkInfo netInfo = cm.getActiveNetworkInfo();
            NetworkInfo networkInfo = cm.getActiveNetworkInfo();
            //return networkInfo.isConnected();
            if (networkInfo != null && networkInfo.isConnected()) {
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

}
