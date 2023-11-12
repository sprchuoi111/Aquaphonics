package com.example.aquasys.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.aquasys.MainActivity;
import com.example.aquasys.R;
import com.example.aquasys.adapter.ActuatorAdapter_add;
import com.example.aquasys.adapter.ActuatorAdapter_environment;
import com.example.aquasys.adapter.ActuatorAdapter_water;
import com.example.aquasys.adapter.TimerActuatorAdapter;
import com.example.aquasys.object.actuator;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ActuatorFragment extends Fragment {
    private RecyclerView recyclerview_adapter_environment; // RecyclerView for actuator for tree

    private RecyclerView recyclerview_adapter_water; // RecyclerView for actuator for fish
    private ActuatorAdapter_environment actuatorAdapter_environment ; // adapter for the actuator

    private ActuatorAdapter_water actuatorAdapter_water ; // adapter for the actuator
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

        // Add actuator button
        FloatingActionButton btn_add_actuator = mView.findViewById(R.id.btn_add_actuator);

        btn_add_actuator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Set actuator list want to add to null
                actuator.globalActuator_add = null;
                AlertDialog.Builder builder = new AlertDialog.Builder(mMainActivity);
                @SuppressLint("InflateParams") View mViewDialog = mMainActivity.getLayoutInflater().inflate(R.layout.dialog_add_actuator, null);
                builder.setView(mViewDialog);
                builder.setTitle("Add actuator");
                builder.setIcon(R.drawable.add);

                // Mapping for components in dialog layout
                RecyclerView recyclerview_add_adapter_environment = mViewDialog.findViewById(R.id.recyclerview_add_adapter_environment);
                RecyclerView recyclerview_add_adapter_water = mViewDialog.findViewById(R.id.recyclerview_add_adapter_water);
                EditText edt_add_description = mViewDialog.findViewById(R.id.edt_add_description);

                // Add data to RecyclerView
                // Set GridLayoutManager
                GridLayoutManager gridLayoutManager_water = new GridLayoutManager(mMainActivity, 2);
                GridLayoutManager gridLayoutManager_environment = new GridLayoutManager(mMainActivity, 2);

                // Set GridLayoutManager for RecyclerView
                recyclerview_add_adapter_water.setLayoutManager(gridLayoutManager_water);
                recyclerview_add_adapter_environment.setLayoutManager(gridLayoutManager_environment);

                // Set data for actuator in timer
                // Actuator adapter for fish
                ActuatorAdapter_add actuatorAdapter_environment_add = new ActuatorAdapter_add(actuator.listActuator_environment_add());
                // Actuator adapter for tree
                ActuatorAdapter_add actuatorAdapter_water_add = new ActuatorAdapter_add(actuator.listActuator_water_add());

                // Set RecyclerView with adapter
                recyclerview_add_adapter_environment.setAdapter(actuatorAdapter_environment_add);
                recyclerview_add_adapter_water.setAdapter(actuatorAdapter_water_add);

                builder.setPositiveButton("Add", (dialog, which) -> {
                    if (actuator.globalActuator_add != null && actuator.globalActuator_add.size() == 1) {
                        if (!edt_add_description.getText().toString().isEmpty()) {
                            actuator.globalActuator_add.get(0).setName(edt_add_description.getText().toString());
                            if (actuator.globalActuator_add.get(0).getType() == actuator.typeof_actuator.bulb) {
                                actuator.globalActuator_environment.add(actuator.globalActuator_add.get(0));
                            } else {
                                actuator.globalActuator_water.add(actuator.globalActuator_add.get(0));
                            }
                            mMainActivity.addActuatorToFireBase();
                        } else {
                            // Display a toast when the conditions are not met
                            Toast.makeText(mMainActivity, "Please fill in the name field!", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(mMainActivity, "Please choose an adapter again, select only 1 adapter!", Toast.LENGTH_SHORT).show();
                    }
                });

                builder.setNegativeButton("Cancel", null);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

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
            actuatorAdapter_environment = new ActuatorAdapter_environment(actuator.listActuator_environment(), (act, position) -> {
            });
            recyclerview_adapter_environment.setAdapter(actuatorAdapter_environment);
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
                actuatorAdapter_water = new ActuatorAdapter_water(actuator.listActuator_water(), (act, position) -> {
                });
                recyclerview_adapter_water.setAdapter(actuatorAdapter_water);
                mMainActivity.addActuatorToFireBase();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(mMainActivity, "Error when reading data", Toast.LENGTH_SHORT).show();
            }
        });
    }


}