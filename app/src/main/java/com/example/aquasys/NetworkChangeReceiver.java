package com.example.aquasys;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;
public class NetworkChangeReceiver extends BroadcastReceiver {
    int check_Online = 0;
    @Override
    public void onReceive(Context context, Intent intent) {
        try{
            if(isOnline(context)){
                check_Online = 1 ;
                Toast.makeText(context, "Network Connected", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(context, "No Network Connection", Toast.LENGTH_SHORT).show();
                check_Online = 0;
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
