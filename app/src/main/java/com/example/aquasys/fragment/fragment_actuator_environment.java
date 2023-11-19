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
import com.example.aquasys.adapter.ActuatorAdapter_environment;
import com.example.aquasys.object.actuator;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class fragment_actuator_environment extends Fragment {

    private MainActivity mMainActivity;
    private RecyclerView recyclerview_adapter_environment ; // recyclerView for sensor
    private ActuatorAdapter_environment actuatorAdapter_environment;
    private  TextView tv_number_actuator_environment;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // Inflate the layout for this fragment.
        // A reference to the main view of the fragment.
        View mView = inflater.inflate(R.layout.fragment_actuator_environment, container, false); // Inflate the fragment_home layout into mView.
        mMainActivity = (MainActivity) getActivity(); // Get a reference to the hosting Activity (assumed to be MainActivity).

        recyclerview_adapter_environment = mView.findViewById(R.id.recyclerview_adapter_environment); // Find the RecyclerView in the layout.
        tv_number_actuator_environment =  mView.findViewById(R.id.tv_number_actuator_environment);
        // Setting GridLayoutManager with 2 columns for environment
        recyclerview_adapter_environment.setLayoutManager(new GridLayoutManager(mMainActivity, 2));
        // Assuming there is a similar list for environment actuators
        int environmentActuatorSize = actuator.listActuator_environment() != null ? actuator.listActuator_environment().size() : 0;
        tv_number_actuator_environment.setText(environmentActuatorSize + " Devices");

        Read_Data_fromFireBase_Actuator_Tree();
        return mView;
    }
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
                actuatorAdapter_environment = new ActuatorAdapter_environment(actuator.listActuator_environment(), (act, position) -> {
                });
                recyclerview_adapter_environment.setAdapter(actuatorAdapter_environment);
                int environmentActuatorSize = actuator.globalActuator_environment != null ? actuator.globalActuator_environment.size() : 0;
                tv_number_actuator_environment.setText(environmentActuatorSize + " Devices");
                mMainActivity.addActuatorToFireBase();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(mMainActivity, "Error when reading data", Toast.LENGTH_SHORT).show();
            }
        });
    }
}