package com.example.aquasys.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.aquasys.MainActivity;
import com.example.aquasys.R;
import com.example.aquasys.adapter.ActuatorAdapter;
import com.example.aquasys.adapter.SensorAdapter;
import com.example.aquasys.listener.SelectListener;
import com.example.aquasys.listener.SelectListenerActuator;
import com.example.aquasys.object.actuator;
import com.example.aquasys.object.sensor;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ActuatorFragment extends Fragment {
    private RecyclerView recyclerViewActuator; // RecyclerView for actuator
    private ActuatorAdapter actuatorAdapter ; // adapter for the actuator
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
        recyclerViewActuator = mView.findViewById(R.id.recyclerview_adapter);
        // setting show the managerList manager recyclerView for actuator
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mMainActivity , 2);
        recyclerViewActuator.setLayoutManager(gridLayoutManager);
        // read from firebase when the first time open app
        ReadDatafromFireBase();

        return mView;
    }

    // read from firebase when the first time open app
    private void ReadDatafromFireBase(){
        mMainActivity.mDatabaseActuator.addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            List<actuator> actuatorList = new ArrayList<>();
            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                actuator act = dataSnapshot.getValue(actuator.class);
                if (act != null) {
                    actuatorList.add(act);

                }
            }

            // test for reading
            //Toast.makeText(mMainActivity, "Read success", Toast.LENGTH_SHORT).show();

            actuator.globalActuator = actuatorList;

            // set list for sensor adapter
            actuatorAdapter = new ActuatorAdapter(actuator.listActuator(), (act, position) -> {
            });
            recyclerViewActuator.setAdapter(actuatorAdapter);
            mMainActivity.addActuatorToFireBase();
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
            Toast.makeText(mMainActivity, "Error when reading data", Toast.LENGTH_SHORT).show();
        }
        });
    }


}