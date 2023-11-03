package com.example.aquasys;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

public class LoadingPage extends AppCompatActivity  {
    // create hander for loadding page
    private Handler handler;
    private MainActivity mMainactivity;

    private NetworkChangeReceiver networkChangeReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_page);

        // create loading page
        handler = new Handler();
        while(!InternetIsConnected()) ;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent intent = new Intent(LoadingPage.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, 3000);
    }



    //Check connect internet
    // check internet ping to gg
    public boolean InternetIsConnected() {
        try {
            String command = "ping -c 1 google.com";
            return (Runtime.getRuntime().exec(command).waitFor() == 0);
        } catch (Exception e) {
            return false;
        }
    }




}