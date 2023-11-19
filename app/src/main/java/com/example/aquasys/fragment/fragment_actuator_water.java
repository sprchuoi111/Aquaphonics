package com.example.aquasys.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aquasys.MainActivity;
import com.example.aquasys.R;
import com.example.aquasys.adapter.ActuatorAdapter_water;
import com.example.aquasys.object.actuator;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class fragment_actuator_water extends Fragment {

    private RecyclerView recyclerview_adapter_water; // RecyclerView for actuator for fish
    private MainActivity mMainActivity ;
    private TextView tv_number_actuator_water;
    private ActuatorAdapter_water actuatorAdapter_water ; // adapter for the actuator
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mView = inflater.inflate(R.layout.fragment_actuator_water, container, false);
        mMainActivity = (MainActivity) getContext();
        Read_Data_fromFireBase_Actuator_Fish();
        tv_number_actuator_water = mView.findViewById(R.id.tv_number_actuator_water);
        recyclerview_adapter_water = mView.findViewById(R.id.recyclerview_adapter_water);

        // Setting GridLayoutManager with 2 columns for water
        recyclerview_adapter_water.setLayoutManager(new GridLayoutManager(mMainActivity, 2));
        int waterActuatorSize = actuator.listActuator_water() != null ? actuator.listActuator_water().size() : 0;
        tv_number_actuator_water.setText(waterActuatorSize + " Devices");
        return mView;
    }

    private void Read_Data_fromFireBase_Actuator_Fish() {
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
                actuatorAdapter_water = new ActuatorAdapter_water(actuator.listActuator_water(), (act, position) -> {
                });
                recyclerview_adapter_water.setAdapter(actuatorAdapter_water);
                int waterActuatorSize = actuator.globalActuator_water != null ? actuator.globalActuator_water.size() : 0;
                tv_number_actuator_water.setText(waterActuatorSize + " Devices");
                mMainActivity.addActuatorToFireBase();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(mMainActivity, "Error when reading data", Toast.LENGTH_SHORT).show();
            }
        });
    }
}