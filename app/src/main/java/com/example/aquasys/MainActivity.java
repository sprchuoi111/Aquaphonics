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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    //create variable for fragment



    private static final int FRAGMENT_SENSOR = 0;
    private static final int FRAGMENT_ACTUATOR = 1;
    private static final int FRAGMENT_TIMER = 2;
    private static final String CHANNEL_ID = "notify";
    // create for current fragment
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
        FirebaseAuth auth = FirebaseAuth.getInstance();
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

    // check internet ping to gg
    public boolean InternetIsConnected() {
        try {
            String command = "ping -c 1 google.com";
            return (Runtime.getRuntime().exec(command).waitFor() == 0);
        } catch (Exception e) {
            return false;
        }
    }

    // save sensor to firebase

    public void addSensorToFireBase() {
        // sensor environment
        mDatabaseSensor_environment.setValue(sensor.listSensor_environment()).addOnSuccessListener(aVoid -> {
                    // Data has been saved successfully
                    Toast.makeText(MainActivity.this, "Save complete", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    // Handle any errors
                    Toast.makeText(MainActivity.this, "Error saving data", Toast.LENGTH_SHORT).show();
                });
        // sensor water
        mDatabaseSensor_water.setValue(sensor.listSensor_water()).addOnSuccessListener(aVoid -> {
                    // Data has been saved successfully
                    Toast.makeText(MainActivity.this, "Save complete", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(MainActivity.this, "Save complete", Toast.LENGTH_SHORT).show();
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

    // notification builder
//    private void Notification() {
//            // Create an explicit intent for an Activity in your app
//            Intent intent = new Intent(this, MainActivity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);
//
//            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, String.valueOf(CHANNEL_ID))
//                    .setSmallIcon(R.drawable.aquaphonic)
//                    .setContentTitle("My notification")
//                    .setContentText("Hello World!")
//                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//                // Set the intent that will fire when the user taps the notification
//                .setContentIntent(pendingIntent)
//                .setAutoCancel(true);
//        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
//
//        // notificationId is a unique int for each notification that you must define
//        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return;
//        }
//        int notificationId = 0;
//        notificationManager.notify(notificationId, mBuilder.build());
//    }
//       public void Notification() {
//           // get Layout Inflater for Notification
//           @SuppressLint("InflateParams") View mViewNotification = MainActivity.this.getLayoutInflater().inflate(R.layout.notification_layout,null);
//           // mapping component in notification layout
//           TextView tv_humidity_val = mViewNotification.findViewById(R.id.tv_humidity_val);
//           TextView tv_ph_val = mViewNotification.findViewById(R.id.tv_ph_val);
//           TextView tv_light_val = mViewNotification.findViewById(R.id.tv_light_val);
//           TextView tv_soil_val = mViewNotification.findViewById(R.id.tv_soil_val);
//           TextView tv_temperature_val = mViewNotification.findViewById(R.id.tv_temperature_val);
//           TextView tv_waterlevel = mViewNotification.findViewById(R.id.tv_waterlevel);
//           // set value for sensor in notification
//           tv_humidity_val.setText(sensor.listSensor().get(0).getValue());
//           tv_temperature_val.setText(sensor.listSensor().get(1).getValue());
//           tv_waterlevel.setText(sensor.listSensor().get(2).getValue());
//           tv_ph_val.setText(sensor.listSensor().get(3).getValue());
//           tv_light_val.setText(sensor.listSensor().get(4).getValue());
//           tv_soil_val.setText(sensor.listSensor().get(5).getValue());
//           NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//           NotificationChannel notificationChannel= null;
//           if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//               notificationChannel = new NotificationChannel(CHANNEL_ID,"name", NotificationManager.IMPORTANCE_LOW);
//           }
//
//
//           // Get the layouts to use in the custom notification
//           RemoteViews notificationLayout = new RemoteViews(getPackageName(), R.layout.small_notification_layout);
//           RemoteViews notificationLayoutExpanded = new RemoteViews(getPackageName(), R.layout.notification_layout);
//
//           // Apply the layouts to the notification.
//           Notification customNotification = new NotificationCompat.Builder(MainActivity.this, CHANNEL_ID)
//                   .setSmallIcon(R.drawable.aquaphonic)
//                   .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
//                   .setCustomContentView(notificationLayout)
//                   .setCustomBigContentView(notificationLayoutExpanded)
//                   .build();
//           if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//               notificationManager.createNotificationChannel(notificationChannel);
//           }
//           notificationManager.notify(666, customNotification);
//       }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterNetwork();
    }


}