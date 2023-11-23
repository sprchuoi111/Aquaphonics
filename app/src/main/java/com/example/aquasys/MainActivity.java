package com.example.aquasys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aquasys.network.NetworkChangeReceiver;
import com.example.aquasys.login.Login;
import com.example.aquasys.object.actuator;
import com.example.aquasys.object.sensor;
import com.example.aquasys.object.timer;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final int FRAGMENT_SENSOR = 0;
    private static final int FRAGMENT_ACTUATOR = 1;
    private static final int FRAGMENT_TIMER = 2;
    private static final String CHANNEL_ID = "notify";
    private int mCurrentFragment = FRAGMENT_SENSOR;
    private ChipNavigationBar bottom_navi;
    private ViewPager2 mViewPager; // Declare a ViewPager2 variable.
    private DrawerLayout drawer_layout; // Declare a DrawerLayout variable.
    public DatabaseReference mDatabaseSensor_environment;
    public DatabaseReference mDatabaseSensor_water;
    public DatabaseReference mDatabaseActuator_environment;
    public DatabaseReference mDatabaseActuator_water;
    public DatabaseReference mDatabaseSchedule;
    public int pos_edit_actuator;
    public actuator tmp_actuator;
    BroadcastReceiver broadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        // Check Internet Connection
        // create broadcast receiver
        broadcastReceiver = new NetworkChangeReceiver();
        registerNetworkBroadcastReceiver();
        // Check if the user is authenticated
        getUserInformation();
        bottom_navi = findViewById(R.id.bottom_navi); // Find the BottomNavigationView in the layout.
        mViewPager = findViewById(R.id.view_pager); // Find the ViewPager2 in the layout.
        drawer_layout = findViewById(R.id.drawer_layout); // Find the DrawerLayout in the layout
        // Set up the ViewPager for handling swipeable screens.
        setUpViewPager();
        // Set up the Toolbar and connect it to the ActionBar.
        // Declare a Toolbar variable.
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Right side button toolbar
        Button btn_update = new Button(this);
        btn_update.setBackgroundResource(R.drawable.update);
        Toolbar.LayoutParams l3 = new Toolbar.LayoutParams(Toolbar.LayoutParams.WRAP_CONTENT, Toolbar.LayoutParams.WRAP_CONTENT);
        btn_update.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.black)));
        l3.gravity = Gravity.END;
        l3.width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, getResources().getDisplayMetrics()); // Set width to 60dp
        l3.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, getResources().getDisplayMetrics()); // Set height to 60dp
        btn_update.setLayoutParams(l3);


        toolbar.addView(btn_update);

        btn_update.setOnClickListener(v -> {
            addSensorToFireBase();
            addActuatorToFireBase();
            addScheduleToFireBase();

        });

        // Create an ActionBarDrawerToggle for syncing the ActionBar with the DrawerLayout.
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer_layout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer_layout.addDrawerListener(toggle); // Add a DrawerListener to the DrawerLayout.
        toggle.syncState(); // Synchronize the ActionBarDrawerToggle state with the DrawerLayout.

        // Set up click events for the NavigationView.
        NavigationView navigation_view = findViewById(R.id.navigation_view);
        navigation_view.setNavigationItemSelectedListener(this);

        // Set up click events for the items in the BottomNavigationView.
        bottom_navi.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
              @Override
              public void onItemSelected(int id) {
                  if (id == R.id.active_sensor) {
                      openSensorFragment();
                  } else if (id == R.id.active_actuator) {
                      openActuatorFragment();
                  } else if (id == R.id.active_timer) {
                      openTimerFragment();
                  }
              }
        });



        // Set up a callback for ViewPager page changes.
        mViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                // Update the selected item in both the NavigationView and BottomNavigationView.
                switch (position) {
                    case 0:
                        mCurrentFragment = FRAGMENT_SENSOR; //sửa trạng thái của biến mCurrentFragment thành FRAGMENT_SENSOR
                        navigation_view.getMenu().findItem(R.id.active_sensor).setChecked(true);
                        bottom_navi.setItemSelected(navigation_view.getMenu().findItem(R.id.active_sensor).getItemId(),true);
                        break;
                    case 1:
                        mCurrentFragment = FRAGMENT_ACTUATOR; //sửa trạng thái của biến mCurrentFragment thành FRAGMENT_ACTUATOR
                        navigation_view.getMenu().findItem(R.id.active_actuator).setChecked(true);
                        bottom_navi.setItemSelected(navigation_view.getMenu().findItem(R.id.active_actuator).getItemId(),true);
                        break;
                    case 2:
                        mCurrentFragment = FRAGMENT_TIMER; //sửa trạng thái của biến mCurrentFragment thành FRAGMENT_TIMER
                        navigation_view.getMenu().findItem(R.id.active_timer).setChecked(true);
                        bottom_navi.setItemSelected(navigation_view.getMenu().findItem(R.id.active_timer).getItemId(),true);
                        break;
                }
            }
        });
        // database Sensor
        mDatabaseSensor_environment = FirebaseDatabase.getInstance().getReference().child("Sensors_environment");
        mDatabaseSensor_water = FirebaseDatabase.getInstance().getReference().child("Sensors_water");

        // database Actuator
        mDatabaseActuator_environment = FirebaseDatabase.getInstance().getReference().child("Actuators_environment");
        mDatabaseActuator_water = FirebaseDatabase.getInstance().getReference().child("Actuators_water");

        // database Schedule
        mDatabaseSchedule = FirebaseDatabase.getInstance().getReference().child("Schedules");
        // test notification




        // Assuming you have a reference to the Firebase Realtime Database
    }

    // Manage user information
    private void getUserInformation() {

        // Method to check user authentication and redirect to the login page if necessary
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        DatabaseReference usersRef = database.getReference("users");
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            String uid = currentUser.getUid();

            // Check the user's login state in the database
            usersRef.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        // User is already logged in on another device
                        // Log out from the original device
                        auth.signOut();
                    }
                    else {
                        // Mark the user as logged in on the new device
                        usersRef.child(uid).setValue(true);
                        // Proceed with the login on the new device
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        // get user
        //Firebase
        FirebaseUser user = auth.getCurrentUser();

        if (user == null) {
            // User is not logged in, so redirect to the login page
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
        }

    }

    // Set up the ViewPager with an adapter.
    private void setUpViewPager() {
        viewPagerAdapter mViewPagerAdapter = new viewPagerAdapter(this);
        mViewPager.setAdapter(mViewPagerAdapter);
    }


    // Handle item clicks in the NavigationView and synchronize with the BottomNavigationView.
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.active_sensor) {
            openSensorFragment();
            bottom_navi.setItemSelected(id,true);
        } else if (id == R.id.active_actuator) {
            openActuatorFragment();
            bottom_navi.setItemSelected(id,true);
        } else if (id == R.id.active_timer) {
            openTimerFragment();
            bottom_navi.setItemSelected(id,true);
        } else if (id == R.id.logout) {
            // Sign out the user and redirect to the login page
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            Toast.makeText(MainActivity.this, "Logout success", Toast.LENGTH_SHORT).show();
            finish();
        }else if (id == R.id.change_password) {
            Intent intent = new Intent(getApplicationContext(), ChangePassword.class);
            startActivity(intent);
        }

        drawer_layout.closeDrawer(GravityCompat.START); // Close the navigation drawer.
        return true;
    }

    public void goToSensorDevice(String nameSensor, int index , String typeSensor) { //tạo phương thức chuyển hướng Fragment từ Room sang Device

        //tạo Intent để chuyển từ MainActivity sang Device Activity đồng thời bundle dữ liệu sang
        Intent i = new Intent(this, SensorDevice.class);
        Bundle bundle = new Bundle();
        bundle.putString("nameSensor", nameSensor);
        bundle.putString("typeSensor", typeSensor);
        bundle.putInt("index", index);
        i.putExtras(bundle);
        //chuyển dữ liệu gồm tên phòng (key:NameRoom) và vị trí của phòng (key:index)
        startActivity(i);
    }

    //Create method open Fragment
    //Fragment Sensor
    private void openSensorFragment() {
        if (mCurrentFragment != FRAGMENT_SENSOR) {
            mViewPager.setCurrentItem(0);
            mCurrentFragment = FRAGMENT_SENSOR;
            //nếu màn hình hiện tại không ở HomeFragment thì nó sẽ chuyển sang HomeFragment đồng thời lưu giá trị tương ứng vào mCurrentFragment để kiểm tra cho các lần chọn sau
        }
    }

    //Fragment Actuator
    private void openActuatorFragment() {
        if (mCurrentFragment != FRAGMENT_ACTUATOR) {
            mViewPager.setCurrentItem(1);
            mCurrentFragment = FRAGMENT_ACTUATOR;
        }
    }

    //Fragment Timer
    private void openTimerFragment() {
        if (mCurrentFragment != FRAGMENT_TIMER) {
            mViewPager.setCurrentItem(2);
            mCurrentFragment = FRAGMENT_TIMER;
        }
    }

    // Handle the back button press to close the navigation drawer if it's open.
    @Override
    public void onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) MainActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }


    // save sensor to firebase

    public void addSensorToFireBase() {
        // sensor environment
        mDatabaseSensor_environment.setValue(sensor.listSensor_environment()).addOnSuccessListener(aVoid -> {
                })
                .addOnFailureListener(e -> {
                    // Handle any errors
                    Toast.makeText(MainActivity.this, "Error saving data", Toast.LENGTH_SHORT).show();
                });
        // sensor water
        mDatabaseSensor_water.setValue(sensor.listSensor_water()).addOnSuccessListener(aVoid -> {
                    // Data has been saved successfully
                })
                .addOnFailureListener(e -> {
                    // Handle any errors
                    Toast.makeText(MainActivity.this, "Error saving data", Toast.LENGTH_SHORT).show();
                });
    }

    // save actuator to firebase
    public void addActuatorToFireBase() {
        //actuator for tree
        mDatabaseActuator_environment.setValue(actuator.listActuator_environment()).addOnSuccessListener(aVoid -> {
                    // Data has been saved successfully
                })
                .addOnFailureListener(e -> {
                    // Handle any errors
                    Toast.makeText(MainActivity.this, "Error saving data", Toast.LENGTH_SHORT).show();
                });
        //actuator for fish
        mDatabaseActuator_water.setValue(actuator.listActuator_water()).addOnSuccessListener(aVoid -> {
                    // Data has been saved successfully
                })
                .addOnFailureListener(e -> {
                    // Handle any errors
                    Toast.makeText(MainActivity.this, "Error saving data", Toast.LENGTH_SHORT).show();
                });
    }

    // save Schedule to firebase
    public void addScheduleToFireBase() {
        mDatabaseSchedule.setValue(timer.globalTimer).addOnSuccessListener(aVoid -> {
                    // Data has been saved successfully
                })
                .addOnFailureListener(e -> {
                    // Handle any errors
                    Toast.makeText(MainActivity.this, "Error saving data", Toast.LENGTH_SHORT).show();
                });
    }

    // MetWork processing when occur error connection
    // register Network
    protected void registerNetworkBroadcastReceiver() {
        registerReceiver(broadcastReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    protected void unregisterNetwork() {
        try {
            unregisterReceiver(broadcastReceiver);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterNetwork();
    }


}