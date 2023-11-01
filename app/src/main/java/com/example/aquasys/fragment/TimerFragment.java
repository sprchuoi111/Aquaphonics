package com.example.aquasys.fragment;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.NumberPicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.aquasys.MainActivity;
import com.example.aquasys.R;
import com.example.aquasys.adapter.TimerAdapter;
import com.example.aquasys.listener.SelectListenerTimer;
import com.example.aquasys.object.actuator;
import com.example.aquasys.object.timer;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class TimerFragment extends Fragment {

    private FloatingActionButton btn_timer;
    private View mView;
    private actuator temp_act;
    private ToggleButton btn_bulb1,btn_bulb2,btn_pump,btn_heater;
    private NumberPicker np_duration_hour_from , np_duration_minute_from ;
    private NumberPicker np_duration_hour_to , np_duration_minute_to ;
    private MainActivity mMainActivity;
    private FloatingActionButton btn_done;
    private RecyclerView recyclerview_timer;
    private TimerAdapter timerAdapter;
    String time;

    public TimerFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public void onResume() {
        super.onResume();
        timerAdapter.notifyDataSetChanged(); // re-change data base on resume method

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_timer, container, false);

        btn_timer = mView.findViewById(R.id.btn_timer);
        btn_timer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottomDiaLog();
            }
        });

        // Define a boolean variable to keep track of the currently selected button

        // Inflate the layout for this fragment.
        recyclerview_timer = mView.findViewById(R.id.recyclerview_timer); // Find the RecyclerView in the layout.
        // setting show sensor list in the recyclerview
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mMainActivity);
        recyclerview_timer.setLayoutManager(linearLayoutManager);
        timerAdapter = new TimerAdapter(timer.globalTimer, new SelectListenerTimer() {
            @Override
            public void onClickItemTimer(timer tim, int position) {

            }
        });
        recyclerview_timer.setAdapter(timerAdapter);

        return mView;
    }

    private void showBottomDiaLog(){
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mMainActivity = (MainActivity) getContext();
        View mViewDialog = mMainActivity.getLayoutInflater().inflate(R.layout.dialog_set_timer,null);
        dialog.setContentView(mViewDialog);
        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);

        temp_act = null;
        //pocessing imf in bottom sheet
        // Set a click listener for each button
        /// get View from dialog


        btn_bulb1 = mViewDialog.findViewById(R.id.btn_bulb1);
        btn_bulb2 = mViewDialog.findViewById(R.id.btn_bulb2);
        btn_pump = mViewDialog.findViewById(R.id.btn_pump);
        btn_heater = mViewDialog.findViewById(R.id.btn_heater);
        btn_done = mViewDialog.findViewById(R.id.btn_done);
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
        // set conditon for np value of minute and hour
        np_duration_hour_to.setValue(np_duration_hour_from.getValue());
        btn_bulb1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    btn_bulb2.setChecked(false);
                    btn_pump.setChecked(false);
                    btn_heater.setChecked(false);
                    temp_act = actuator.listActuator().get(0);
                }
                else temp_act = null;
            }
        });
        btn_bulb2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    btn_bulb1.setChecked(false);
                    btn_pump.setChecked(false);
                    btn_heater.setChecked(false);
                    temp_act = actuator.listActuator().get(1);
                }
                else temp_act = null;
            }
        });
        btn_pump.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    btn_bulb1.setChecked(false);
                    btn_bulb2.setChecked(false);
                    btn_heater.setChecked(false);
                    temp_act = actuator.listActuator().get(2);
                }
                else temp_act = null;
            }
        });

        btn_heater.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    btn_bulb1.setChecked(false);
                    btn_bulb2.setChecked(false);
                    btn_pump.setChecked(false);
                    temp_act = actuator.listActuator().get(3);
                }
                else temp_act = null;
            }
        });

        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Inflate the layout for this fragment.
                recyclerview_timer = mView.findViewById(R.id.recyclerview_timer); // Find the RecyclerView in the layout.
                // setting show sensor list in the recyclerview
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mMainActivity);
                recyclerview_timer.setLayoutManager(linearLayoutManager);
                timerAdapter = new TimerAdapter(timer.globalTimer, new SelectListenerTimer() {
                    @Override
                    public void onClickItemTimer(timer tim, int position) {

                    }
                });
                if (temp_act != null) {
                    int hourFrom = np_duration_hour_from.getValue();
                    int minuteFrom = np_duration_minute_from.getValue();
                    int hourTo = np_duration_hour_to.getValue();
                    int minuteTo = np_duration_minute_to.getValue();

                    if ((hourFrom < hourTo) || (hourFrom == hourTo && minuteFrom < minuteTo)) {
                        // Add the timer
                        timer.globalTimer.add(0, new timer(temp_act, hourFrom, minuteFrom, hourTo, minuteTo));
                        recyclerview_timer.setAdapter(timerAdapter);
                        dialog.cancel();
                    } else {
                        // Display a toast when the conditions are not met
                        Toast.makeText(getContext(), "Please choose again!!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Display a toast when temp_act is null
                    Toast.makeText(getContext(), "Actuator is null. Please choose again!!", Toast.LENGTH_SHORT).show();
                }


            }
        });

    }


}