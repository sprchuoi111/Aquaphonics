package com.example.aquasys.adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aquasys.MainActivity;
import com.example.aquasys.R;
import com.example.aquasys.listener.SelectListenerTimer;
import com.example.aquasys.object.actuator;
import com.example.aquasys.object.timer;

import java.util.List;

public class TimerAdapter extends RecyclerView.Adapter<TimerAdapter.TimerViewHolder>{
    private Context context;
    private SelectListenerTimer listenerTimer;
    private List<timer> timerList;


    public TimerAdapter( List<timer> timerList,SelectListenerTimer listenerTimer) {
        this.listenerTimer = listenerTimer;
        this.timerList = timerList;
    }

    @NonNull
    @Override
    public TimerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_timer_schedule , parent,false);
        return new TimerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TimerViewHolder holder, int position) {
        timer tim = timerList.get(position);
        int itemPosition = holder.getAdapterPosition();
        if(tim == null){
            return;
        }
        holder.tv_name_timer_act.setText(tim.getAct().getName());
        String time = String.format("%02d:%02d-%02d:%02d",
                tim.getTime_start_hour(), tim.getTime_start_minute(),
                tim.getTime_stop_hour(), tim.getTime_stop_minute());
        holder.tv_timer_set.setText(time);
        holder.btn_clear_timer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get adapter position of ViewHolder in RecyclerView and assign it to 'currentPosition'.
                int currentPosition = holder.getAdapterPosition();

                // Create a builder for an alert dialog that uses default alert dialog theme.
                new AlertDialog.Builder(v.getContext())
                        .setTitle("Delete Schedule") // Set title text for dialog.
                        .setMessage("Are you sure you want to delete this Schedule?") // Set message text for dialog.
                        // Add positive button to dialog with text "OK" and click listener.
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            // This method is called when positive button is clicked.
                            public void onClick(DialogInterface dialog, int which) {
                                //If 'currentPosition' is a valid position

                                if (currentPosition != RecyclerView.NO_POSITION) {
                                    // Remove the room at 'currentPosition' from mListRoom.
                                    timerList.remove(currentPosition);
                                    notifyDataSetChanged();
                                    holder.mMainActivity.addScheduleToFireBase();
                                }
                            }
                        })
                        // Add negative button to dialog with text "Cancel" and null click listener.
                        .setNegativeButton(android.R.string.cancel, null)
                        // Set icon for dialog using a drawable resource.
                        .setIcon(android.R.drawable.ic_menu_delete)
                        // Show this dialog, adding it to the screen.
                        .show();
            }
        });
        holder.btn_edit_timer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get current position
                int currentPosition = holder.getAdapterPosition();
                AlertDialog.Builder builder = new AlertDialog.Builder(holder.mMainActivity);
                View  diaLogView = holder.mMainActivity.getLayoutInflater().inflate(R.layout.dialog_edit_timer, null);
                builder.setView(diaLogView);
                builder.setTitle("Edit Schedule");
                builder.setIcon(R.drawable.edit);
                // mapping for num picker in layout edit schedule
                NumberPicker np_edit_duration_hour_from = diaLogView.findViewById(R.id.np_edit_duration_hour_from);
                NumberPicker np_edit_duration_minute_from = diaLogView.findViewById(R.id.np_edit_duration_minute_from);
                NumberPicker np_edit_duration_hour_to = diaLogView.findViewById(R.id.np_edit_duration_hour_to);
                NumberPicker np_edit_duration_minute_to = diaLogView.findViewById(R.id.np_edit_duration_minute_to);
                // set threshold for num picker
                np_edit_duration_hour_from.setMaxValue(23);
                np_edit_duration_hour_from.setMinValue(0);
                np_edit_duration_minute_from.setMaxValue(63);
                np_edit_duration_minute_from.setMinValue(0);
                np_edit_duration_hour_to.setMaxValue(23);
                np_edit_duration_hour_to.setMinValue(0);
                np_edit_duration_minute_to.setMaxValue(63);
                np_edit_duration_minute_to.setMinValue(0);
                // set time initiate for num picker when
                np_edit_duration_hour_from.setValue(timerList.get(currentPosition).getTime_start_hour());
                np_edit_duration_minute_from.setValue(timerList.get(currentPosition).getTime_start_minute());
                np_edit_duration_hour_to.setValue(timerList.get(currentPosition).getTime_stop_hour());
                np_edit_duration_minute_to.setValue(timerList.get(currentPosition).getTime_stop_minute());
                builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int hourFrom = np_edit_duration_hour_from.getValue();
                        int minuteFrom = np_edit_duration_minute_from.getValue();
                        int hourTo = np_edit_duration_hour_to.getValue();
                        int minuteTo = np_edit_duration_minute_to.getValue();

                        if ((hourFrom < hourTo) || (hourFrom == hourTo && minuteFrom < minuteTo)) {
                            // change the schedule time
                            timerList.get(currentPosition).setTime_start_hour(hourFrom);
                            timerList.get(currentPosition).setTime_start_minute(minuteFrom);
                            timerList.get(currentPosition).setTime_stop_hour(hourTo);
                            timerList.get(currentPosition).setTime_stop_minute(minuteTo);
                            notifyDataSetChanged();
                            holder.mMainActivity.addScheduleToFireBase();
                        } else {
                            // Display a toast when the conditions are not met
                            Toast.makeText(holder.mMainActivity, "Please choose again!!", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
                builder.setNegativeButton("Cancel" ,null);
                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });
    }


    @Override
    public int getItemCount() {
        return timerList.size();
    }
    public class TimerViewHolder extends RecyclerView.ViewHolder{

        private Button btn_clear_timer , btn_edit_timer;
        private TextView tv_name_timer_act;
        private TextView tv_timer_set;
        private MainActivity mMainActivity;
        public TimerViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_name_timer_act = itemView.findViewById(R.id.tv_name_timer_act);
            tv_timer_set = itemView.findViewById(R.id.tv_timer_set);
            btn_clear_timer = itemView.findViewById(R.id.btn_clear_timer);
            btn_edit_timer = itemView.findViewById(R.id.btn_edit_timer);
            mMainActivity = (MainActivity) itemView.getContext();
        }
    }

}
