package com.example.aquasys.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.aquasys.MainActivity;
import com.example.aquasys.R;
import com.example.aquasys.SensorDevice;
import com.example.aquasys.adapter.SensorAdapter;
import com.example.aquasys.listener.SelectListener;
import com.example.aquasys.sensor.sensor;

import java.util.ArrayList;

public class SensorFragment extends Fragment {

    private RecyclerView recyclerview_sensor ; // recyclerView for sensor
    private MainActivity mMainActivity; // A reference to the main activity.
    private SensorAdapter sensorAdapter ; // adapter for the sensor fragment

    private View mView; // A reference to the main view of the fragment.

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    //overide Onresume method when changing the fragment


    @Override
    public void onResume() {
        super.onResume();
        sensorAdapter.notifyDataSetChanged(); // re-change data base on resume method

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
        sensorAdapter =new SensorAdapter(sensor.listSensor(), new SelectListener() {
            @Override
            public void onClickItemSensor(sensor sen, int position) {
                mMainActivity.goToSensorDevice(sen.getName().toString() , position);
            }
        });
        recyclerview_sensor.setAdapter(sensorAdapter);
        return mView;
    }
}