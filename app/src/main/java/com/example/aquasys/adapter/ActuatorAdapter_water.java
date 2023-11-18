package com.example.aquasys.adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;

import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
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
        int img_actuator_on = R.drawable.ic_launcher_background;
        int img_actuator_off = R.drawable.ic_launcher_background;
        //set drawable effect for btn
        switch (act.getType()) {
            case bulb:
                img_actuator_on = R.drawable.light_bulb_on;
                img_actuator_off = R.drawable.lightbulb;
                break;
            case pump:
                img_actuator_on = R.drawable.water_pump_on;
                img_actuator_off = R.drawable.water_pump;

                break;
            case heater:
                img_actuator_on = R.drawable.fish_feeder_on;
                img_actuator_off = R.drawable.fish_feeder;
                break;
            case feeder:
                img_actuator_on = R.drawable.heater_on;
                img_actuator_off = R.drawable.heater;
                break;
            default:
                break;
        }
        int finalImg_actuator_on = img_actuator_on;
        int finalImg_actuator_off = img_actuator_off;
        holder.btn_actuator.setOnCheckedChangeListener((buttonView, isChecked) -> {
            int off_actuator = ContextCompat.getColor(holder.mMainActivity, R.color.off_actuator);
            int on_actuator = ContextCompat.getColor(holder.mMainActivity, R.color.on_actuator);
            if(isChecked) {
                if(act.getHour() == 0 && act.getMinute() == 0 ){
                    holder.btn_actuator.setChecked(false);
                    act.setStatus(0);
                    Toast.makeText(holder.mMainActivity, "Please choose time for activate" , Toast.LENGTH_SHORT).show();
                    holder.card_actuator.setCardBackgroundColor(off_actuator);
                    holder.img_actuator.setImageResource(finalImg_actuator_off);
                    holder.tv_name_actuator.setTextColor(ColorStateList.valueOf(holder.mMainActivity.getResources().getColor(R.color.tv_actuator_off)));

                }
                else {
                    holder.card_actuator.setCardBackgroundColor(on_actuator);
                    act.setStatus(1);
                    Toast.makeText(holder.mMainActivity, act.getName() + String.format(",Duration : %02d:%02d ", act.getHour(), act.getMinute()), Toast.LENGTH_SHORT).show();
                    holder.img_actuator.setImageResource(finalImg_actuator_on);
                    //save actuator to firebase
                    holder.mMainActivity.addActuatorToFireBase();
                    holder.tv_name_actuator.setTextColor(ColorStateList.valueOf(holder.mMainActivity.getResources().getColor(R.color.tv_actuator_on)));


                }
            }
            else {
                holder.card_actuator.setCardBackgroundColor(off_actuator);
                act.setStatus(0);
                holder.img_actuator.setImageResource(finalImg_actuator_off);
                holder.tv_name_actuator.setTextColor(ColorStateList.valueOf(holder.mMainActivity.getResources().getColor(R.color.tv_actuator_off)));

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
                if(snapshot.exists()) {
                    int status = snapshot.getValue(int.class);
                    actuator.listActuator_water().get(itemPosition).setStatus(status);
                    // set list for sensor adapter
                    // Check realtime state
                    //Toast.makeText(mMainActivity , "state button : " + actuator.listActuator().get(actuatorIndex).getStatus(),Toast.LENGTH_SHORT).show();
                    // change state of button
                    int off_actuator = ContextCompat.getColor(holder.mMainActivity, R.color.off_actuator);
                    int on_actuator = ContextCompat.getColor(holder.mMainActivity, R.color.on_actuator);
                    if (status == 1) {
                        if (actuator.listActuator_water().get(itemPosition).getHour() == 0 && actuator.listActuator_water().get(itemPosition).getMinute() == 0) {
                            holder.btn_actuator.setChecked(false);
                            act.setStatus(0);
                            holder.img_actuator.setImageResource(finalImg_actuator_off);
                            holder.card_actuator.setCardBackgroundColor(off_actuator);
                            holder.tv_name_actuator.setTextColor(ColorStateList.valueOf(holder.mMainActivity.getResources().getColor(R.color.tv_actuator_off)));

                            //Toast.makeText(holder.mMainActivity, "Please choose time for activate" , Toast.LENGTH_SHORT).show();
                            // save status back to actuator
                            holder.mMainActivity.addActuatorToFireBase();
                        } else {
                            holder.card_actuator.setCardBackgroundColor(on_actuator);
                            holder.btn_actuator.setChecked(true);
                            holder.img_actuator.setImageResource(finalImg_actuator_on);
                            holder.tv_name_actuator.setTextColor(ColorStateList.valueOf(holder.mMainActivity.getResources().getColor(R.color.tv_actuator_on)));

                            act.setStatus(1);
                        }
                    }
                    if (status == 0) {
                        holder.btn_actuator.setChecked(false);
                        act.setStatus(0);
                        holder.img_actuator.setImageResource(finalImg_actuator_off);
                        holder.card_actuator.setCardBackgroundColor(off_actuator);
                        holder.tv_name_actuator.setTextColor(ColorStateList.valueOf(holder.mMainActivity.getResources().getColor(R.color.tv_actuator_off)));

                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(holder.mMainActivity, "Error when read data" , Toast.LENGTH_SHORT).show();
            }

        });

        // Remove Actuator
        holder.card_actuator.setOnLongClickListener(new View.OnLongClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public boolean onLongClick(View v) {
                // This method is called when positive button is clicked.
                new AlertDialog.Builder(v.getContext())
                        .setTitle("Delete Actuator") // Set title text for dialog.
                        .setMessage("Are you sure you want to delete this Schedule?") // Set message text for dialog.
                        // Add positive button to dialog with text "OK" and click listener.
                        .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                            //If 'currentPosition' is a valid position
                            int itemPosition = holder.getAdapterPosition();
                            if (itemPosition > 2) {
                                // Remove the room at 'currentPosition' from mListRoom.
                                actuatorList.remove(itemPosition);
                                notifyDataSetChanged();
                                holder.mMainActivity.addActuatorToFireBase();
                            }
                            else {Toast.makeText(holder.mMainActivity, "Can not remove this Actuator", Toast.LENGTH_SHORT).show();}

                            })
                        // Add negative button to dialog with text "Cancel" and null click listener.
                        .setNegativeButton(android.R.string.cancel, null)
                        // Set icon for dialog using a drawable resource.
                        .setIcon(android.R.drawable.ic_menu_delete)
                        // Show this dialog, adding it to the screen.
                        .show();
                return true;
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

        @SuppressLint("UseSwitchCompatOrMaterialCode")
        private final ToggleButton btn_set_duration;
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
