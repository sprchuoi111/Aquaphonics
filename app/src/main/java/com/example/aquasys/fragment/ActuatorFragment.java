package com.example.aquasys.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aquasys.MainActivity;
import com.example.aquasys.R;
import com.example.aquasys.adapter.ActuatorAdapter_add;
import com.example.aquasys.adapter.ActuatorAdapter_environment;
import com.example.aquasys.adapter.ActuatorAdapter_water;
import com.example.aquasys.adapter.TimerActuatorAdapter;
import com.example.aquasys.object.actuator;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
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

    ExtendedFloatingActionButton btn_menu_actuator;
    FloatingActionButton btn_edit_actuator;
    FloatingActionButton btn_add_actuator;

    // to check whether sub FABs are visible or not
    Boolean isAllFabsVisible;

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

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate layout for this fragment
        View mView = inflater.inflate(R.layout.fragment_actuator, container, false);
        mMainActivity = (MainActivity) getContext();

        // Add actuator button
        btn_add_actuator = mView.findViewById(R.id.btn_add_actuator);
        btn_edit_actuator = mView.findViewById(R.id.btn_edit_actuator);
        btn_menu_actuator = mView.findViewById(R.id.btn_menu_actuator);
        // Now set all the FABs and all the action name
        // texts as GONE
        btn_add_actuator.setVisibility(View.GONE);
        btn_edit_actuator.setVisibility(View.GONE);

        // make the boolean variable as false, as all the
        // action name texts and all the sub FABs are
        // invisible
        isAllFabsVisible = false;

        // Set the Extended floating action button to
        // shrinked state initially
        btn_menu_actuator.shrink();
        btn_menu_actuator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isAllFabsVisible) {
                    // when isAllFabsVisible becomes
                    // true make all the action name
                    // texts and FABs VISIBLE.
                    btn_add_actuator.show();
                    btn_edit_actuator.show();
                    // Now extend the parent FAB, as
                    // user clicks on the shrinked
                    // parent FAB
                    btn_menu_actuator.extend();
                    // make the boolean variable true as
                    // we have set the sub FABs
                    // visibility to GONE
                    isAllFabsVisible = true;


                }
                else {
                    // when isAllFabsVisible becomes
                    // true make all the action name
                    // texts and FABs GONE.
                    btn_add_actuator.hide();
                    btn_edit_actuator.hide();
                    // Set the FAB to shrink after user
                    btn_menu_actuator.shrink();
                    // make the boolean variable false
                    // as we have set the sub FABs
                    // visibility to GONE
                    isAllFabsVisible = false;
                }
            }
        });
        // Read from Firebase when the first time the app is opened
        Read_Data_fromFireBase_Actuator_Tree();
        Read_Data_fromFireBase_Actuator_Fish();
        TextView tv_number_actuator_environment = mView.findViewById(R.id.tv_number_actuator_environment);
        TextView tv_number_actuator_water = mView.findViewById(R.id.tv_number_actuator_water);
        // button delete actuator
        btn_edit_actuator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Set actuator list to edit to null
                actuator.globalActuator_edit = null;

                AlertDialog.Builder builder = new AlertDialog.Builder(mMainActivity);
                @SuppressLint("InflateParams") View mViewDialog = mMainActivity.getLayoutInflater().inflate(R.layout.dialog_edit_actuator, null);
                builder.setView(mViewDialog);
                builder.setTitle("Edit Actuator");
                builder.setIcon(R.drawable.edit);

                // Mapping for components in the dialog layout
                RecyclerView recyclerview_edit_adapter_environment = mViewDialog.findViewById(R.id.recyclerview_edit_adapter_environment);
                RecyclerView recyclerview_edit_adapter_water = mViewDialog.findViewById(R.id.recyclerview_edit_adapter_water);
                EditText edt_edit_description = mViewDialog.findViewById(R.id.edt_edit_description);

                // Set GridLayoutManager for RecyclerView
                recyclerview_edit_adapter_water.setLayoutManager(new GridLayoutManager(mMainActivity, 2));
                recyclerview_edit_adapter_environment.setLayoutManager(new GridLayoutManager(mMainActivity, 2));

                // Set data for actuator in the timer
                ActuatorAdapter_add actuatorAdapter_environment_add = new ActuatorAdapter_add(actuator.listActuator_environment());
                ActuatorAdapter_add actuatorAdapter_water_add = new ActuatorAdapter_add(actuator.listActuator_water());

                // Set RecyclerView with adapter
                recyclerview_edit_adapter_environment.setAdapter(actuatorAdapter_environment_add);
                recyclerview_edit_adapter_water.setAdapter(actuatorAdapter_water_add);

                builder.setPositiveButton("Edit", (dialog, which) -> {
                    if (actuator.globalActuator_edit != null && actuator.globalActuator_edit.size() == 1) {
                        actuator editActuator = actuator.globalActuator_edit.get(0);

                        // Check if the description is empty
                        if (edt_edit_description.getText().toString().isEmpty()) {
                            Toast.makeText(mMainActivity, "Please fill in the description field!", Toast.LENGTH_SHORT).show();
                            return; // Exit the method if the description is empty
                        }

                        // Check the type of the actuator
                        switch (editActuator.getType()) {
                            case bulb:
                                // Update the name in the environment actuator list
                                actuator.globalActuator_environment.get(mMainActivity.pos_edit_actuator).setName(edt_edit_description.getText().toString());
                                break;
                            case pump:
                            case heater:
                            case feeder:
                                // Update the name in the water actuator list
                                actuator.globalActuator_water.get(mMainActivity.pos_edit_actuator).setName(edt_edit_description.getText().toString());
                                break;
                            // Add more cases if there are other types
                            default:
                                break;
                        }

                        // set list for sensor adapter
                        actuatorAdapter_environment = new ActuatorAdapter_environment(actuator.listActuator_environment(), (act, position) -> {
                        });
                        recyclerview_adapter_environment.setAdapter(actuatorAdapter_environment);
                        // set list for sensor adapter
                        actuatorAdapter_water = new ActuatorAdapter_water(actuator.listActuator_water(), (act, position) -> {
                        });
                        recyclerview_adapter_water.setAdapter(actuatorAdapter_water);
                        // Notify Firebase about the changes
                        mMainActivity.addActuatorToFireBase();
                    } else {
                        Toast.makeText(mMainActivity, "Please choose an adapter again, select only 1 adapter!", Toast.LENGTH_SHORT).show();
                    }
                });

                mMainActivity.pos_edit_actuator = 0;
                builder.setNegativeButton("Cancel", null);

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });


        // button add actuator
        btn_add_actuator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Set actuator list to add to null
                actuator.globalActuator_add = null;

                AlertDialog.Builder builder = new AlertDialog.Builder(mMainActivity);
                @SuppressLint("InflateParams") View mViewDialog = mMainActivity.getLayoutInflater().inflate(R.layout.dialog_add_actuator, null);
                builder.setView(mViewDialog);
                builder.setTitle("Add actuator");
                builder.setIcon(R.drawable.add);

                // Mapping for components in the dialog layout
                RecyclerView recyclerview_add_adapter_environment = mViewDialog.findViewById(R.id.recyclerview_add_adapter_environment);
                RecyclerView recyclerview_add_adapter_water = mViewDialog.findViewById(R.id.recyclerview_add_adapter_water);
                EditText edt_add_description = mViewDialog.findViewById(R.id.edt_add_description);

                // Set GridLayoutManager for RecyclerView
                recyclerview_add_adapter_water.setLayoutManager(new GridLayoutManager(mMainActivity, 2));
                recyclerview_add_adapter_environment.setLayoutManager(new GridLayoutManager(mMainActivity, 2));

                // Set data for actuator in the timer
                ActuatorAdapter_add actuatorAdapter_environment_add = new ActuatorAdapter_add(actuator.listActuator_environment_add());
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

                            int environmentActuatorSize = actuator.globalActuator_environment != null ? actuator.globalActuator_environment.size() : 0;
                            tv_number_actuator_environment.setText(environmentActuatorSize + " Devices");

                            int waterActuatorSize = actuator.globalActuator_water != null ? actuator.globalActuator_water.size() : 0;
                            tv_number_actuator_water.setText(waterActuatorSize + " Devices");

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

// Setting GridLayoutManager with 2 columns for environment
        recyclerview_adapter_environment.setLayoutManager(new GridLayoutManager(mMainActivity, 2));

// Setting GridLayoutManager with 2 columns for water
        recyclerview_adapter_water.setLayoutManager(new GridLayoutManager(mMainActivity, 2));
        // Display the number of environment actuators in the list


        // Assuming there is a similar list for water actuators
        int environmentActuatorSize = actuator.listActuator_environment() != null ? actuator.listActuator_environment().size() : 0;
        tv_number_actuator_environment.setText(environmentActuatorSize + " Devices");

        int waterActuatorSize = actuator.listActuator_water() != null ? actuator.listActuator_water().size() : 0;
        tv_number_actuator_water.setText(waterActuatorSize + " Devices");
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