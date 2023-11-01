package com.example.aquasys.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.aquasys.MainActivity;
import com.example.aquasys.R;
import com.example.aquasys.adapter.ActuatorAdapter;
import com.example.aquasys.adapter.SensorAdapter;
import com.example.aquasys.listener.SelectListenerActuator;
import com.example.aquasys.object.actuator;

public class ActuatorFragment extends Fragment {
    private RecyclerView recyclerViewActuator; // RecyclerView for actuator
    private ActuatorAdapter actuatorAdapter ; // adapter for the actuator
    private MainActivity mMainActivity ;
    private View mView;
    public ActuatorFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        actuatorAdapter.notifyDataSetChanged(); // re-change data base on resume method

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // inflate layout for this fragment
        mView = inflater.inflate(R.layout.fragment_actuator, container, false);
        mMainActivity = (MainActivity) getContext();
        recyclerViewActuator = mView.findViewById(R.id.recyclerview_adapter);
        // setting show the managerList manager recyclerView for actuator
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mMainActivity , 2);
        recyclerViewActuator.setLayoutManager(gridLayoutManager);
        actuatorAdapter = new ActuatorAdapter(actuator.listActuator(), new SelectListenerActuator() {
            @Override
            public void onClickItemActuator(actuator act, int position) {
                Toast.makeText(mMainActivity, act.getName(), Toast.LENGTH_SHORT).show();
            }
        });
        recyclerViewActuator.setAdapter(actuatorAdapter);
        return mView;
    }
}