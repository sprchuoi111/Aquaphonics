package com.example.aquasys.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aquasys.MainActivity;
import com.example.aquasys.R;
import com.example.aquasys.listener.SelectListener;
import com.example.aquasys.object.sensor;

import java.util.List;

public class SensorAdapter extends RecyclerView.Adapter<SensorAdapter.SensorViewHolder> {

    private Context context;
    private SelectListener mOnClickItemListener;
    private List<sensor> mListSensor;
    // Constructor for the SensorAdapter class
    public SensorAdapter( List<sensor> mListSensor, SelectListener mOnClickItemListener) {
        this.mOnClickItemListener = mOnClickItemListener;
        this.mListSensor = mListSensor;
    }

    @NonNull
    @Override
    public SensorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_sensor, parent, false);

        return new SensorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SensorViewHolder holder, int position) {
        sensor sen =mListSensor.get(position);
        int itemPosition = holder.getAdapterPosition();
        if(sen ==null){
            return;
        }
        holder.tv_sensor.setText(sen.getName());
        holder.tv_sensor_val.setText(sen.getValue());
        holder.img_sensor.setImageResource(sen.getImg());

    }


    @Override
    public int getItemCount() {
        return mListSensor.size();
    }

    // Create sensor view holder
    public class SensorViewHolder extends RecyclerView.ViewHolder{
        private LinearLayout layoutSensor;
        private TextView tv_sensor;
        private TextView tv_sensor_val;
        private MainActivity mainActivity;
        private ImageView img_sensor ;

        public SensorViewHolder(@NonNull View itemView) {
            super(itemView);

            layoutSensor = itemView.findViewById(R.id.sensor_content);
            tv_sensor = itemView.findViewById(R.id.tv_sensor);
            tv_sensor_val = itemView.findViewById(R.id.tv_sensor_val);
            img_sensor  = itemView.findViewById(R.id.img_sensor);
            mainActivity = (MainActivity) itemView.getContext();

        }
    }
}
