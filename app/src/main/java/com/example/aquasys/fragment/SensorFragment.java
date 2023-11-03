package com.example.aquasys.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
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

    private RecyclerView recyclerview_sensor ; // recyclerView for sensor
    private MainActivity mMainActivity; // A reference to the main activity.
    private SensorAdapter sensorAdapter ; // adapter for the sensor fragment

    private View mView; // A reference to the main view of the fragment.



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    //override Onresume method when changing the fragment
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
        mView = inflater.inflate(R.layout.fragment_sensor, container, false); // Inflate the fragment_home layout into mView.
        mMainActivity = (MainActivity) getActivity(); // Get a reference to the hosting Activity (assumed to be MainActivity).
        recyclerview_sensor = mView.findViewById(R.id.recyclerview_sensor); // Find the RecyclerView in the layout.
        // setting show sensor list in the recyclerview
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mMainActivity, 2);
        recyclerview_sensor.setLayoutManager(gridLayoutManager);
        ReadSensorData("Humidity", 0);
        ReadSensorData("Temperature", 1);
        ReadSensorData("Water level", 2);
        ReadSensorData("PH", 3);
        ReadSensorData("Light Sensor", 4);
        ReadSensorData("Moisture Soil", 5);

        return mView;
    }




    // read data form firebase
    // Read data from Firebase for a specific sensor and update the corresponding sensor object
    private void ReadSensorData(final String sensorName, final int sensorIndex) {
        mMainActivity.mDatabaseSensor.child(String.valueOf(sensorIndex)).child("value").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String value = snapshot.getValue(String.class);
                if (value != null) {
                    sensor.listSensor().get(sensorIndex).setValue(value);
                    // set list for sensor adapter
                    sensorAdapter =new SensorAdapter(sensor.listSensor(), new SelectListener() {
                        @Override
                        public void onClickItemSensor(sensor sen, int position) {
                            mMainActivity.goToSensorDevice(sen.getName().toString() , position);
                        }
                    });
                    //Set Sensor adapter
                    recyclerview_sensor.setAdapter(sensorAdapter);
                    Toast.makeText(mMainActivity, sensorName + " Data Received : " +sensor.listSensor().get(sensorIndex).getValue(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mMainActivity, sensorName + " Data is null", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(mMainActivity, "Error occurred while reading " + sensorName + " data", Toast.LENGTH_SHORT).show();
            }
        });
    }


}