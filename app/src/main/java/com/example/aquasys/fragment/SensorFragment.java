package com.example.aquasys.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.example.aquasys.MainActivity;
import com.example.aquasys.MyApplication;
import com.example.aquasys.R;
import com.example.aquasys.adapter.SensorAdapter;
import com.example.aquasys.listener.SelectListener;
import com.example.aquasys.object.sensor;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.SimpleTimeZone;

public class SensorFragment extends Fragment {

    private static final String CHANEL_1_ID = "666";
    private static final String CHANEL_MAIN = "111";
    private static final String CHANEL_2_ID = "2";
    private RecyclerView recyclerview_sensor_environment; // recyclerView for sensor
    private RecyclerView recyclerview_sensor_water; // recyclerView for sensor

    private MainActivity mMainActivity; // A reference to the main activity.
    private SensorAdapter sensorAdapter_environment, sensorAdapter_water; // adapter for the sensor fragment


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    //override On resume method when changing the fragment
    @Override
    public void onResume() {
        super.onResume();
        //Load sensor to firebase
        //sensorAdapter.notifyDataSetChanged(); // re-change data base on resume method

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment.
        // A reference to the main view of the fragment.
        View mView = inflater.inflate(R.layout.fragment_sensor, container, false); // Inflate the fragment_home layout into mView.
        mMainActivity = (MainActivity) getActivity(); // Get a reference to the hosting Activity (assumed to be MainActivity).
        recyclerview_sensor_environment = mView.findViewById(R.id.recyclerview_sensor_environment); // Find the RecyclerView in the layout.
        recyclerview_sensor_water = mView.findViewById(R.id.recyclerview_sensor_water); // Find the RecyclerView in the layout.
        // setting show sensor list in the recyclerview
        GridLayoutManager gridLayoutManager_environment = new GridLayoutManager(mMainActivity, 2);
        GridLayoutManager gridLayoutManager_water = new GridLayoutManager(mMainActivity, 2);
        // setting show environment sensor
        recyclerview_sensor_environment.setLayoutManager(gridLayoutManager_environment);
        // setting show water sensor
        recyclerview_sensor_water.setLayoutManager(gridLayoutManager_water);
        sensorAdapter_environment = new SensorAdapter(sensor.listSensor_environment(), new SelectListener() {
            @Override
            public void onClickItemSensor(sensor sen, int position) {
                mMainActivity.goToSensorDevice(sen.getName(), position, sen.getType());

            }
        });
        sensorAdapter_water = new SensorAdapter(sensor.listSensor_water(), new SelectListener() {
            @Override
            public void onClickItemSensor(sensor sen, int position) {
                mMainActivity.goToSensorDevice(sen.getName(), position, sen.getType());
            }
        });        //Set Sensor adapter
        recyclerview_sensor_environment.setAdapter(sensorAdapter_environment);
        recyclerview_sensor_water.setAdapter(sensorAdapter_water);
        // Realtime database reading sensor check environment
        for (int i = 0; i <= sensor.listSensor_environment().size(); i++) {
            ReadSensorData_Environment(i);
        }
        // Realtime database reading sensor check water-qualify
        for (int j = 0; j < sensor.listSensor_water().size(); j++) {
            ReadSensorData_Water(j);
        }
        // notification of sensor value
        //mMainActivity.Notification();
        return mView;
    }
    // read data form firebase
    // Read data from Firebase for a specific sensor and update the corresponding sensor object

    // Read data sensor from environment
    private void ReadSensorData_Environment(final int sensorIndex) {
        mMainActivity.mDatabaseSensor_environment.child(String.valueOf(sensorIndex)).child("value").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String value = snapshot.getValue(String.class);
                if (value != null) {
                    sensor.listSensor_environment().get(sensorIndex).setValue(value);
                    // set list for sensor adapter
                    sensorAdapter_environment = new SensorAdapter(sensor.listSensor_environment(),
                            (sen, position) -> mMainActivity.goToSensorDevice(sen.getName(), position, sen.getType()));
                    //Set Sensor adapter
                    recyclerview_sensor_environment.setAdapter(sensorAdapter_environment);
                    // checking data
                    //Toast.makeText(mMainActivity, sensorName + " Data Received : " +sensor.listSensor().gset(sensorIndex).getValue(), Toast.LENGTH_SHORT).show();
                    sendCustomNotification();
                    if (Integer.parseInt(sensor.listSensor_environment().get(0).getValue()) < 50 || Integer.parseInt(sensor.listSensor_environment().get(1).getValue()) > 35)
                        sendNotification_environment();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(mMainActivity, "Error occurred while reading data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Read data sensor from environment
    private void ReadSensorData_Water(final int sensorIndex) {
        mMainActivity.mDatabaseSensor_water.child(String.valueOf(sensorIndex)).child("value").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String value = snapshot.getValue(String.class);
                if (value != null) {
                    sensor.listSensor_water().get(sensorIndex).setValue(value);
                    // set list for sensor adapter
                    sensorAdapter_water = new SensorAdapter(sensor.listSensor_water(), new SelectListener() {
                        @Override
                        public void onClickItemSensor(sensor sen, int position) {
                            mMainActivity.goToSensorDevice(sen.getName(), position, sen.getType());
                        }
                    });
                    //Set Sensor adapter
                    recyclerview_sensor_water.setAdapter(sensorAdapter_water);
                    // checking data
                    //Toast.makeText(mMainActivity, sensorName + " Data Received : " +sensor.listSensor().get(sensorIndex).getValue(), Toast.LENGTH_SHORT).show();
                    sendCustomNotification();
                    if (Integer.parseInt(sensor.listSensor_water().get(1).getValue()) < 10)
                        sendNotification_water();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(mMainActivity, "Error occurred while reading data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // notification for sensor environment
    private void sendNotification_environment() {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_aquaphonics);
        Notification notification = new NotificationCompat.Builder(mMainActivity, MyApplication.CHANNEL_ID_ENVIRONMENT)
                .setContentTitle("Alarm for activate")
                .setContentText("Please water your plant")
                .setSmallIcon(R.drawable.aquaphonic)
                .setLargeIcon(bitmap)
                .setColor(getResources().getColor(R.color.blue))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .build();
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(mMainActivity);
        if (ActivityCompat.checkSelfPermission(mMainActivity, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        notificationManager.notify(Integer.parseInt(CHANEL_1_ID), notification);

   }
    // notification for sensor water
    private void sendNotification_water(){
        Bitmap bitmap = BitmapFactory.decodeResource(getResources() , R.mipmap.ic_aquaphonics);
        Notification notification = new NotificationCompat.Builder(mMainActivity, MyApplication.CHANNEL_ID_WATER)
                .setContentTitle("Alarm for activate")
                .setContentText("Please fill your pool")
                .setSmallIcon(R.drawable.aquaphonic)
                .setLargeIcon(bitmap)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setColor(getResources().getColor(R.color.blue))
                .build();
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(mMainActivity);
        if (ActivityCompat.checkSelfPermission(mMainActivity, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        notificationManager.notify(Integer.parseInt(CHANEL_2_ID), notification);

    }
    private void sendCustomNotification(){
        RemoteViews notificationLayout = new RemoteViews(mMainActivity.getPackageName() , R.layout.small_notification_layout);
        notificationLayout.setTextViewText(R.id.notification_title , "Monitor ");
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String strDate = sdf.format(new Date());
        notificationLayout.setTextViewText(R.id.time_notification , strDate);

        // Notification Expand
        RemoteViews notificationLayoutExpanded = new RemoteViews(mMainActivity.getPackageName() , R.layout.notification_layout);
        // mapping information in layout
        notificationLayoutExpanded.setTextViewText(R.id.tv_humidity_val , sensor.listSensor_environment().get(0).getValue());
        notificationLayoutExpanded.setTextViewText(R.id.tv_temperature_val , sensor.listSensor_environment().get(1).getValue());
        notificationLayoutExpanded.setTextViewText(R.id.tv_light_val , sensor.listSensor_environment().get(2).getValue());
        notificationLayoutExpanded.setTextViewText(R.id.tv_soil_val , sensor.listSensor_environment().get(3).getValue());
        notificationLayoutExpanded.setTextViewText(R.id.tv_ph_val, sensor.listSensor_water().get(0).getValue());
        notificationLayoutExpanded.setTextViewText(R.id.tv_waterlevel_val, sensor.listSensor_water().get(1).getValue());
        Notification notification = new NotificationCompat.Builder(mMainActivity, MyApplication.CHANNEL_ID)
                .setSmallIcon(R.drawable.aquaphonic)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setColor(getResources().getColor(R.color.blue))
                .setCustomContentView(notificationLayout)
                .setCustomBigContentView(notificationLayoutExpanded)
                .build();
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(mMainActivity);
        if (ActivityCompat.checkSelfPermission(mMainActivity, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        notificationManager.notify(Integer.parseInt(CHANEL_MAIN), notification);

    }


}