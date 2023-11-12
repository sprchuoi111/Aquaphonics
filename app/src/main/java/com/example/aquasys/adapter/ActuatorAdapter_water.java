package com.example.aquasys.adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aquasys.MainActivity;
import com.example.aquasys.R;
import com.example.aquasys.listener.SelectListenerActuator;
import com.example.aquasys.object.actuator;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class ActuatorAdapter_water extends RecyclerView.Adapter<ActuatorAdapter_water.ActuatorViewHolder> {


    private final List<actuator> actuatorList;

    public ActuatorAdapter_water(List<actuator> actuatorList , SelectListenerActuator selectListenerActuator) {
        this.actuatorList = actuatorList;
    }

    @NonNull
    @Override
    public ActuatorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_actuator, parent, false);
        return new ActuatorViewHolder(view);
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(@NonNull ActuatorViewHolder holder, int position) {
        actuator act = actuatorList.get(position);
        int itemPosition = holder.getAdapterPosition();
        if(act == null){
            return;
        }
        holder.img_actuator.setImageResource(act.getImg());
        holder.tv_name_actuator.setText(act.getName());


        holder.btn_actuator.setOnCheckedChangeListener((buttonView, isChecked) -> {
            int purpleColor = ContextCompat.getColor(holder.mMainActivity, R.color.purple);
            int whiteColor = ContextCompat.getColor(holder.mMainActivity, R.color.white);
            if(isChecked) {
                if(act.getHour() == 0 && act.getMinute() == 0 ){
                    holder.btn_actuator.setChecked(false);
                    act.setStatus(0);
                    Toast.makeText(holder.mMainActivity, "Please choose time for activate" , Toast.LENGTH_SHORT).show();
                    holder.card_actuator.setCardBackgroundColor(whiteColor);
                }
                else {
                    holder.card_actuator.setCardBackgroundColor(purpleColor);
                    act.setStatus(1);
                    Toast.makeText(holder.mMainActivity, act.getName() + String.format(",Duration : %02d:%02d ", act.getHour(), act.getMinute()), Toast.LENGTH_SHORT).show();
                    //save actuator to firebase
                    holder.mMainActivity.addActuatorToFireBase();
                }
            }
            else {
                holder.card_actuator.setCardBackgroundColor(whiteColor);
                act.setStatus(0);
                //save actuator to firebase
                holder.mMainActivity.addActuatorToFireBase();
            }
        });
        holder.btn_set_duration.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(holder.mMainActivity);
            View diaLogView = holder.mMainActivity.getLayoutInflater().inflate(R.layout.dialog_set_duration_actuator, null);
            builder.setView(diaLogView);
            builder.setTitle("Setting Duration");
            builder.setIcon(R.drawable.timer);
            final NumberPicker np_duration_hour = diaLogView.findViewById(R.id.np_duration_hour);
            final NumberPicker np_duration_minute = diaLogView.findViewById(R.id.np_duration_minute);
            // set hold for hour val
            np_duration_hour.setMinValue(0);
            np_duration_hour.setMaxValue(12);
            // set hold for minute val
            np_duration_minute.setMinValue(0);
            np_duration_minute.setMaxValue(59);
            // set save position of value of previous
            np_duration_hour.setValue(act.getHour());
            np_duration_minute.setValue(act.getMinute());
            builder.setPositiveButton("OK", (dialog, which) -> {
                // Set time for actuator
                act.setHour(np_duration_hour.getValue());
                act.setMinute(np_duration_minute.getValue());
                //save actuator to firebase
                holder.mMainActivity.addActuatorToFireBase();
            });
            builder.setNegativeButton("Cancel", (dialog, which) -> holder.btn_actuator.setChecked(false));
            AlertDialog dialog = builder.create();
            dialog.show();
        });

        // update realtime actuator for fish in firebase
        holder.mMainActivity.mDatabaseActuator_water.child(String.valueOf(itemPosition)).child("status").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int status = snapshot.getValue(int.class);
                actuator.listActuator_water().get(itemPosition).setStatus(status);
                // set list for sensor adapter
                // Check realtime state
                //Toast.makeText(mMainActivity , "state button : " + actuator.listActuator().get(actuatorIndex).getStatus(),Toast.LENGTH_SHORT).show();
                // change state of button
                int purpleColor = ContextCompat.getColor(holder.mMainActivity, R.color.purple);
                int whiteColor = ContextCompat.getColor(holder.mMainActivity, R.color.white);
                if(status == 1 ) {
                    if(actuator.listActuator_water().get(itemPosition).getHour() == 0 && actuator.listActuator_water().get(itemPosition).getMinute() == 0 ){
                        holder.btn_actuator.setChecked(false);
                        act.setStatus(0);
                        holder.card_actuator.setCardBackgroundColor(whiteColor);
                        //Toast.makeText(holder.mMainActivity, "Please choose time for activate" , Toast.LENGTH_SHORT).show();
                        // save status back to actuator
                        holder.mMainActivity.addActuatorToFireBase();
                    }
                    else {
                        holder.card_actuator.setCardBackgroundColor(purpleColor);
                        holder.btn_actuator.setChecked(true);
                        act.setStatus(1);
                    }
                }
                if(status == 0){
                    holder.btn_actuator.setChecked(false);
                    act.setStatus(0);
                    holder.card_actuator.setCardBackgroundColor(whiteColor);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(holder.mMainActivity, "Error when read data" , Toast.LENGTH_SHORT).show();
            }

        });

    }

    @Override
    public int getItemCount() {
        return actuatorList.size();
    }

    public static class ActuatorViewHolder extends RecyclerView.ViewHolder{
        private final ImageView img_actuator;
        private final TextView tv_name_actuator;
        @SuppressLint("UseSwitchCompatOrMaterialCode")
        private final Switch btn_actuator;
        private final CardView card_actuator;

        private final MainActivity mMainActivity;

        private final Button btn_set_duration;
        public ActuatorViewHolder(@NonNull View itemView) {
            super(itemView);
            img_actuator = itemView.findViewById(R.id.img_actuator);
            tv_name_actuator = itemView.findViewById(R.id.tv_name_actuator);
            btn_actuator = itemView.findViewById(R.id.btn_actuator);
            card_actuator = itemView.findViewById(R.id.card_actuator);
            mMainActivity = (MainActivity) itemView.getContext();
            btn_set_duration = itemView.findViewById(R.id.btn_set_duration);
        }
    }
}
