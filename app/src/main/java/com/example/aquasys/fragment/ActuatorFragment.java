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
import com.example.aquasys.adapter.ActuatorAdapter;
import com.example.aquasys.adapter.SensorAdapter;
import com.example.aquasys.listener.SelectListener;
import com.example.aquasys.listener.SelectListenerActuator;
import com.example.aquasys.object.actuator;
import com.example.aquasys.object.sensor;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ActuatorFragment extends Fragment {
    private RecyclerView recyclerview_adapter_environment; // RecyclerView for actuator for tree

    private RecyclerView recyclerview_adapter_water; // RecyclerView for actuator for fish
    private ActuatorAdapter actuatorAdapter ; // adapter for the actuator
    private MainActivity mMainActivity ;

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
        //actuatorAdapter.notifyDataSetChanged(); // re-change data base on resume method

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // inflate layout for this fragment
        View mView = inflater.inflate(R.layout.fragment_actuator, container, false);
        mMainActivity = (MainActivity) getContext();
        recyclerview_adapter_environment = mView.findViewById(R.id.recyclerview_adapter_environment);

        recyclerview_adapter_water = mView.findViewById(R.id.recyclerview_adapter_water);
        // setting show the managerList manager recyclerView for actuator
        GridLayoutManager gridLayoutManager_environment = new GridLayoutManager(mMainActivity , 2);
        GridLayoutManager gridLayoutManager_water = new GridLayoutManager(mMainActivity , 2);
        // set Gridlayout for adapter
        // for environment
        recyclerview_adapter_environment.setLayoutManager(gridLayoutManager_environment);
        // for water
        recyclerview_adapter_water.setLayoutManager(gridLayoutManager_water);
        // read from firebase when the first time open app
        Read_Data_fromFireBase_Actuator_Tree();
        Read_Data_fromFireBase_Actuator_Fish();
        return mView;
    }

    // read from firebase when the first time open app
    // Read data of actuator tree
    private void Read_Data_fromFireBase_Actuator_Tree(){
        mMainActivity.mDatabaseActuator_environment.addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            List<actuator> actuatorList = new ArrayList<>();
            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                actuator act = dataSnapshot.getValue(actuator.class);
                // check inform in actuator list not return null
                if (act != null) {
                    actuatorList.add(act);
                }
            }

            // test for reading
            //Toast.makeText(mMainActivity, "Read success", Toast.LENGTH_SHORT).show();

            actuator.globalActuator_environment = actuatorList;

            // set list for sensor adapter
            actuatorAdapter = new ActuatorAdapter(actuator.listActuator_environment(), (act, position) -> {
            });
            recyclerview_adapter_environment.setAdapter(actuatorAdapter);
            mMainActivity.addActuatorToFireBase();
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
            Toast.makeText(mMainActivity, "Error when reading data", Toast.LENGTH_SHORT).show();
        }
        });
    }

    // Read data of actuator fish
    private void Read_Data_fromFireBase_Actuator_Fish(){
        mMainActivity.mDatabaseActuator_water.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<actuator> actuatorList = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    actuator act = dataSnapshot.getValue(actuator.class);
                    // check inform in actuator list not return null
                    if (act != null) {
                        actuatorList.add(act);
                    }
                }

                // test for reading
                //Toast.makeText(mMainActivity, "Read success", Toast.LENGTH_SHORT).show();

                actuator.globalActuator_water = actuatorList;

                // set list for sensor adapter
                actuatorAdapter = new ActuatorAdapter(actuator.listActuator_water(), (act, position) -> {
                });
                recyclerview_adapter_water.setAdapter(actuatorAdapter);
                mMainActivity.addActuatorToFireBase();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(mMainActivity, "Error when reading data", Toast.LENGTH_SHORT).show();
            }
        });
    }


}