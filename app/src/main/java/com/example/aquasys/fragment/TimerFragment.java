package com.example.aquasys.fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.NumberPicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.aquasys.MainActivity;
import com.example.aquasys.R;
import com.example.aquasys.adapter.TimerActuatorAdapter;
import com.example.aquasys.adapter.TimerAdapter;
import com.example.aquasys.object.actuator;
import com.example.aquasys.object.timer;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import java.util.Objects;

public class TimerFragment extends Fragment {

    private ToggleButton btn_bulb1,btn_bulb2,btn_pump,btn_heater;
    private NumberPicker np_duration_hour_from , np_duration_minute_from ;
    private NumberPicker np_duration_hour_to , np_duration_minute_to ;
    private MainActivity mMainActivity;
    private RecyclerView recyclerview_timer;
    private TimerAdapter timerAdapter;


    private TimerFragment timerFragment;


    public TimerFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onResume() {
        super.onResume();
        timerAdapter.notifyDataSetChanged(); // re-change data base on resume method

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View mView = inflater.inflate(R.layout.fragment_timer, container, false);
        //GetContext for main Activity
        mMainActivity = (MainActivity) getContext();
        FloatingActionButton btn_timer = mView.findViewById(R.id.btn_timer);
        btn_timer.setOnClickListener(v -> showBottomDiaLog());

        // Define a boolean variable to keep track of the currently selected button

        // Inflate the layout for this fragment.
        recyclerview_timer = mView.findViewById(R.id.recyclerview_timer); // Find the RecyclerView in the layout.
        // setting show sensor list in the recyclerview
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mMainActivity);
        recyclerview_timer.setLayoutManager(linearLayoutManager);

        // set list for sensor adapter
        timerAdapter = new TimerAdapter(timer.globalTimer, (tim, position) -> {

        });
        recyclerview_timer.setAdapter(timerAdapter);
        //read data from schedule firebase
        if(timer.globalTimer.isEmpty())
            ReadScheduleData();
        return mView;
    }

    @SuppressLint("NotifyDataSetChanged")
    private void showBottomDiaLog(){
        final Dialog dialog = new Dialog(requireContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        /// get View from layout timer
        @SuppressLint("InflateParams") View mViewDialog = mMainActivity.getLayoutInflater().inflate(R.layout.dialog_set_timer,null);
        dialog.setContentView(mViewDialog);
        dialog.show();
        Objects.requireNonNull(dialog.getWindow()).setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);

        mMainActivity.tmp_actuator = null;
        //processing imf in bottom sheet
        // Set a recyclerview
        RecyclerView recyclerview_adapter_timer_water = mViewDialog.findViewById(R.id.recyclerview_adapter_timer_water); // Find the RecyclerView in the layout.
        RecyclerView recyclerview_adapter_timer_environment = mViewDialog.findViewById(R.id.recyclerview_adapter_timer_environment); // Find the RecyclerView in the layout.
        // set GridlayoutManager
        GridLayoutManager gridLayoutManager_water = new GridLayoutManager(mMainActivity , 2);
        GridLayoutManager gridLayoutManager_environment = new GridLayoutManager(mMainActivity , 2);
        // set gridLayoutManager for recyclerview
        recyclerview_adapter_timer_water.setLayoutManager(gridLayoutManager_water);
        recyclerview_adapter_timer_environment.setLayoutManager(gridLayoutManager_environment);
        // set data for actuator in timer
        // Actuator adapter for fish
        TimerActuatorAdapter timerActuatorAdapter_water = new TimerActuatorAdapter(actuator.globalActuator_water);
        // Actuator adapter for tree
        TimerActuatorAdapter timerActuatorAdapter_environment = new TimerActuatorAdapter(actuator.globalActuator_environment);

        // set recyclerview with adapter
        recyclerview_adapter_timer_environment.setAdapter(timerActuatorAdapter_environment);
        recyclerview_adapter_timer_water.setAdapter(timerActuatorAdapter_water);

        // setting mapping components in Dialog set timer
        FloatingActionButton btn_done = mViewDialog.findViewById(R.id.btn_done);
        FloatingActionButton btn_close = mViewDialog.findViewById(R.id.btn_close);
        np_duration_hour_from = mViewDialog.findViewById(R.id.np_duration_hour_from);
        np_duration_minute_from = mViewDialog.findViewById(R.id.np_duration_minute_from);
        np_duration_hour_to = mViewDialog.findViewById(R.id.np_duration_hour_to);
        np_duration_minute_to = mViewDialog.findViewById(R.id.np_duration_minute_to);
        // set max && min for np
        np_duration_hour_from.setMaxValue(23);
        np_duration_hour_from.setMinValue(0);
        np_duration_minute_from.setMaxValue(59);
        np_duration_minute_from.setMinValue(0);
        np_duration_hour_to.setMaxValue(23);
        np_duration_hour_to.setMinValue(0);
        np_duration_minute_to.setMaxValue(59);
        np_duration_minute_to.setMinValue(0);
        // set condition for np value of minute and hour
        np_duration_hour_to.setValue(np_duration_hour_from.getValue());

        //Button select when choosing device for schedule timer
        // get list actuator for tree


        btn_done.setOnClickListener(v -> {
            timerAdapter.notifyDataSetChanged();
            if (actuator.globalActuator_timer != null) {
                int hourFrom = np_duration_hour_from.getValue();
                int minuteFrom = np_duration_minute_from.getValue();
                int hourTo = np_duration_hour_to.getValue();
                int minuteTo = np_duration_minute_to.getValue();

                if ((hourFrom < hourTo) || (hourFrom == hourTo && minuteFrom < minuteTo)) {
                    for(int i = 0 ; i <actuator.globalActuator_timer.size() ; i++ )
                        // Add the timer
                        timer.globalTimer.add(new timer(actuator.globalActuator_timer.get(i), hourFrom, minuteFrom, hourTo, minuteTo));
                    recyclerview_timer.setAdapter(timerAdapter);
                    mMainActivity.addScheduleToFireBase();
                    dialog.cancel();
                    actuator.globalActuator_timer = null;

                } else {
                    // Display a toast when the conditions are not met
                    Toast.makeText(getContext(), "Please choose again!!", Toast.LENGTH_SHORT).show();
                }
            } else {
                // Display a toast when temp_act is null
                Toast.makeText(getContext(), "Please Select Actuator !!", Toast.LENGTH_SHORT).show();
            }

        });
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });

    }
    // Read Scheduler data from firebase
    private void ReadScheduleData() {
        mMainActivity.mDatabaseSchedule.addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    timer schedule  = dataSnapshot.getValue(timer.class);
                    timer.globalTimer.add(schedule);
                    Toast.makeText(mMainActivity, "Reading success" , Toast.LENGTH_SHORT).show();
                    }
                timerAdapter.notifyDataSetChanged();
            }
                // set list for sensor adapter
//                timerAdapter = new TimerAdapter(timer.globalTimer, new SelectListenerTimer() {
//                    @Override
//                    public void onClickItemTimer(timer tim, int position) {
//
//                    }
//                });
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(mMainActivity, "Error occurred while reading" , Toast.LENGTH_SHORT).show();
            }
        });

    }


}