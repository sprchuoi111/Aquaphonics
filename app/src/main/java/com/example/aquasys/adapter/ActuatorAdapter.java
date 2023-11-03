package com.example.aquasys.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aquasys.MainActivity;
import com.example.aquasys.R;
import com.example.aquasys.object.actuator;
import com.example.aquasys.listener.SelectListenerActuator;

import java.util.List;

public class ActuatorAdapter extends RecyclerView.Adapter<ActuatorAdapter.ActuatorViewHolder>{

    private Context context;
    private SelectListenerActuator selectListenerActuator;
    private List<actuator>  actuatorList;
    //create constructor for actuatorList

    public ActuatorAdapter( List<actuator> actuatorList ,SelectListenerActuator selectListenerActuator) {
        this.selectListenerActuator = selectListenerActuator;
        this.actuatorList = actuatorList;
    }

    @NonNull
    @Override
    public ActuatorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_actuator, parent, false);
        return new ActuatorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ActuatorViewHolder holder, int position) {
        actuator act = actuatorList.get(position);
        int itemPosition = holder.getAdapterPosition();
        if(act == null){
            return;
        }
        holder.img_actuator.setImageResource(act.getImg());
        holder.tv_name_actuator.setText(act.getName());
        holder.btn_actuator.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int purpleColor = ContextCompat.getColor(holder.mMainActivity, R.color.purple);
                int whiteColor = ContextCompat.getColor(holder.mMainActivity, R.color.white);
                if(isChecked) {
                    if(act.getHour() == 0 && act.getMinute() == 0 ){
                        holder.btn_actuator.setChecked(false);
                        act.setStatus(0);
                        Toast.makeText(holder.mMainActivity, "Please choose time for activate" , Toast.LENGTH_SHORT).show();
                    }
                    else {
                        holder.card_actuator.setCardBackgroundColor(purpleColor);
                        act.setStatus(1);
                        Toast.makeText(holder.mMainActivity, "Duration: " + String.format("%02d:%02d", act.getHour(), act.getMinute()), Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    holder.card_actuator.setCardBackgroundColor(whiteColor);
                    act.setStatus(0);
                }
            }
        });
        holder.btn_set_duration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Set time for actuator
                        act.setHour(np_duration_hour.getValue());
                        act.setMinute(np_duration_minute.getValue());
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        holder.btn_actuator.setChecked(false);
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return actuatorList.size();
    }
    public class ActuatorViewHolder extends RecyclerView.ViewHolder{
        private ImageView img_actuator;
        private TextView tv_name_actuator;
        private Switch btn_actuator;
        private CardView card_actuator;

        private MainActivity mMainActivity;

        private Button btn_set_duration;




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
