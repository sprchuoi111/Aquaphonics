package com.example.aquasys.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.MenuItem;
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
import com.example.aquasys.viewPagerAdapter;
import com.example.aquasys.viewPagerAdapter_actuator;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

import java.util.ArrayList;
import java.util.List;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import me.ibrahimsn.lib.OnItemSelectedListener;
import me.ibrahimsn.lib.SmoothBottomBar;

public class ActuatorFragment extends Fragment {

    private ActuatorAdapter_environment actuatorAdapter_environment ; // adapter for the actuator

    private ActuatorAdapter_water actuatorAdapter_water ; // adapter for the actuator
    private MainActivity mMainActivity ;
    private ViewPager2 mViewPager_actuator;

    private SmoothBottomBar navi_menu_actuator;

    ExtendedFloatingActionButton btn_menu_actuator;
    FloatingActionButton btn_edit_actuator;
    FloatingActionButton btn_add_actuator;

    // to check whether sub FABs are visible or not
    Boolean isAllFabsVisible;
    //create variable for fragment



    private static final int FRAGMENT_ACTUATOR_ENVIRONMENT = 0;
    private static final int FRAGMENT_ACTUATOR_WATER = 1;

    private int mCurrentFragment = FRAGMENT_ACTUATOR_ENVIRONMENT;
    ;

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

        // View pager for actuator fragment
        mViewPager_actuator = mView.findViewById(R.id.view_pager_actuator);
        navi_menu_actuator = mView.findViewById(R.id.navi_menu_actuator);
        // Read from Firebase when the first time the app is opened
//        Read_Data_fromFireBase_Actuator_Tree();
//        Read_Data_fromFireBase_Actuator_Fish();
        //initialize view pager
        setUpViewPager_adapter();

        // Set up click events for the items in the BottomNavigationView.
        navi_menu_actuator.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public boolean onItemSelect(int i) {
                if (i == 0) {
                    openActuatorFragment_environment();
                    Toast.makeText(mMainActivity , "0" , Toast.LENGTH_SHORT).show();
                } else if (i == 1) {
                    Toast.makeText(mMainActivity , "1" , Toast.LENGTH_SHORT).show();
                    openActuatorFragment_water();
                }
                return true;
            }

        });


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






        // button edit actuator
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
                EditText edt_edit_ID = mViewDialog.findViewById(R.id.edt_edit_ID);

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
                        if (edt_edit_description.getText().toString().isEmpty() || edt_edit_ID.getText().toString().isEmpty()) {
                            Toast.makeText(mMainActivity, "Please fill in the description and or ID field !", Toast.LENGTH_SHORT).show();
                            return; // Exit the method if the description is empty
                        }
                        // Check the type of the actuator
                        switch (editActuator.getType()) {
                            case bulb:
                                // Update the name and the id in the environment actuator list
                                actuator.globalActuator_environment.get(mMainActivity.pos_edit_actuator).setName(edt_edit_description.getText().toString());
                                actuator.globalActuator_environment.get(mMainActivity.pos_edit_actuator).setId(edt_edit_ID.getText().toString());

                                break;
                            case pump:
                            case heater:
                            case feeder:
                                // Update the name and the id in the environment actuator list
                                actuator.globalActuator_water.get(mMainActivity.pos_edit_actuator).setName(edt_edit_description.getText().toString());
                                actuator.globalActuator_water.get(mMainActivity.pos_edit_actuator).setId(edt_edit_ID.getText().toString());
                                break;
                            // Add more cases if there are other types
                            default:
                                break;
                        }
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
                EditText edt_add_ID  = mViewDialog.findViewById(R.id.edt_add_ID);

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
                        if (!edt_add_description.getText().toString().isEmpty() || edt_add_ID.getText().toString().isEmpty()) {
                            actuator.globalActuator_add.get(0).setName(edt_add_description.getText().toString());
                            actuator.globalActuator_add.get(0).setId(edt_add_ID.getText().toString());
                            if (actuator.globalActuator_add.get(0).getType() == actuator.typeof_actuator.bulb) {
                                actuator.globalActuator_environment.add(actuator.globalActuator_add.get(0));
                            } else {
                                actuator.globalActuator_water.add(actuator.globalActuator_add.get(0));
                            }
                            //save actuator to firebase
                            mMainActivity.addActuatorToFireBase();
                        } else {
                            // Display a toast when the conditions are not met
                            Toast.makeText(mMainActivity, "Please fill in the name  and the ID field!", Toast.LENGTH_SHORT).show();
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
        // Display the number of environment actuators in the list
        return mView;
    }

    // read from firebase when the first time open app
//    private boolean  is_read_tree = false;
//    // Read data of actuator tree
//    private void Read_Data_fromFireBase_Actuator_Tree(){
//        if(!is_read_tree) {
//            mMainActivity.mDatabaseActuator_environment.addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                    List<actuator> actuatorList = new ArrayList<>();
//                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
//                        actuator act = dataSnapshot.getValue(actuator.class);
//                        // check inform in actuator list not return null
//                        if (act != null) {
//                            actuatorList.add(act);
//                        }
//                    }
//                    // test for reading
//                    //Toast.makeText(mMainActivity, "Read success", Toast.LENGTH_SHORT).show();
//                    // update the list actuator environment
//                    actuator.globalActuator_environment = actuatorList;
//                    is_read_tree = true;
//                }
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//                    Toast.makeText(mMainActivity, "Error when reading data", Toast.LENGTH_SHORT).show();
//                }
//            });
//
//        }
//    }
//    private boolean is_read_fish=  false;
//    // Read data of actuator fish
//    private void Read_Data_fromFireBase_Actuator_Fish(){
//        if( ! is_read_fish) {
//            mMainActivity.mDatabaseActuator_water.addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                    List<actuator> actuatorList = new ArrayList<>();
//                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
//                        actuator act = dataSnapshot.getValue(actuator.class);
//                        // check inform in actuator list not return null
//                        if (act != null) {
//                            actuatorList.add(act);
//                        }
//                    }
//                    // test for reading
//                    //Toast.makeText(mMainActivity, "Read success", Toast.LENGTH_SHORT).show();
//                    actuator.globalActuator_water = actuatorList;
//                    is_read_fish = true;
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//                    Toast.makeText(mMainActivity, "Error when reading data", Toast.LENGTH_SHORT).show();
//                }
//            });
//        }
//    }
    // Set up the ViewPager with an adapter.
    private void setUpViewPager_adapter() {
        viewPagerAdapter_actuator mViewPagerAdapter = new viewPagerAdapter_actuator(this);
        mViewPager_actuator.setAdapter(mViewPagerAdapter);
    }
    //Fragment actuator environment
    private void openActuatorFragment_environment() {
        if (mCurrentFragment != FRAGMENT_ACTUATOR_ENVIRONMENT) {
            mViewPager_actuator.setCurrentItem(0);
            mCurrentFragment = FRAGMENT_ACTUATOR_ENVIRONMENT;
            //nếu màn hình hiện tại không ở HomeFragment thì nó sẽ chuyển sang HomeFragment đồng thời lưu giá trị tương ứng vào mCurrentFragment để kiểm tra cho các lần chọn sau
        }
    }


    //Fragment actuator water
    private void openActuatorFragment_water() {
        if (mCurrentFragment != FRAGMENT_ACTUATOR_WATER) {
            mViewPager_actuator.setCurrentItem(1);
            mCurrentFragment = FRAGMENT_ACTUATOR_WATER;
            //nếu màn hình hiện tại không ở HomeFragment thì nó sẽ chuyển sang HomeFragment đồng thời lưu giá trị tương ứng vào mCurrentFragment để kiểm tra cho các lần chọn sau
        }
    }


}