package com.example.aquasys.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.aquasys.MainActivity;
import com.example.aquasys.R;
import com.example.aquasys.adapter.SensorAdapter;
import com.example.aquasys.listener.SelectListener;
import com.example.aquasys.object.sensor;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SensorFragment extends Fragment {

    private RecyclerView recyclerview_sensor_environment ; // recyclerView for sensor
    private RecyclerView recyclerview_sensor_water ; // recyclerView for sensor

    private MainActivity mMainActivity; // A reference to the main activity.
    private SensorAdapter sensorAdapter_environment , sensorAdapter_water ; // adapter for the sensor fragment


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
        sensorAdapter_environment =new SensorAdapter(sensor.listSensor_environment(), (sen, position) -> mMainActivity.goToSensorDevice(sen.getName(), position));
        sensorAdapter_water =new SensorAdapter(sensor.listSensor_water(), (sen, position) -> mMainActivity.goToSensorDevice(sen.getName(), position));
        //Set Sensor adapter
        recyclerview_sensor_environment.setAdapter(sensorAdapter_environment);
        recyclerview_sensor_water.setAdapter(sensorAdapter_water);
        // Realtime database reading sensor check environment
        for(int i = 0 ; i <= sensor.listSensor_environment().size() ; i++) {
            ReadSensorData_Environment(i);
        }
        // Realtime database reading sensor check water-qualify
        for(int j  =0 ; j < sensor.listSensor_water().size() ; j++){
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
                    sensorAdapter_environment =new SensorAdapter(sensor.listSensor_environment(), (sen, position) -> mMainActivity.goToSensorDevice(sen.getName(), position));
                    //Set Sensor adapter
                    recyclerview_sensor_environment.setAdapter(sensorAdapter_environment);
                    // checking data
                    //Toast.makeText(mMainActivity, sensorName + " Data Received : " +sensor.listSensor().get(sensorIndex).getValue(), Toast.LENGTH_SHORT).show();
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
                    sensorAdapter_water =new SensorAdapter(sensor.listSensor_water(), (sen, position) -> mMainActivity.goToSensorDevice(sen.getName(), position));
                    //Set Sensor adapter
                    recyclerview_sensor_water.setAdapter(sensorAdapter_water);
                    // checking data
                    //Toast.makeText(mMainActivity, sensorName + " Data Received : " +sensor.listSensor().get(sensorIndex).getValue(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(mMainActivity, "Error occurred while reading data", Toast.LENGTH_SHORT).show();
            }
        });
    }


}