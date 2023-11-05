package com.example.aquasys.network;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

public class NetworkChangeReceiver extends BroadcastReceiver {
    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    @Override
    public void onReceive(Context context, Intent intent) {
        try{
            if(isOnline(context)){
                Toast.makeText(context, "Network Connected", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(context, "No Network Connection", Toast.LENGTH_SHORT).show();
                Intent suspendedScreenIntent = new Intent(context, NoInternetActivity.class);
                suspendedScreenIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(suspendedScreenIntent);
            }
        }
        catch (NullPointerException e ){
            e.printStackTrace();
        }
    }

    public boolean isOnline(Context context){
        try {
            ConnectivityManager cm  = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = cm.getActiveNetworkInfo();
            return (networkInfo !=null && networkInfo.isConnected());
         }
        catch (NullPointerException e ){
            e.printStackTrace();
            return false;
        }

    }
}
