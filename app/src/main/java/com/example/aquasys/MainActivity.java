package com.example.aquasys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    //create variable for fragment
    private static final int FRAGMENT_SENSOR= 0;
    private static final int FRAGMENT_ACTUATOR = 1;
    private static final int FRAGMENT_TIMER  = 2;
    // create for current fragment
    private int mCurrentFragment = FRAGMENT_SENSOR;
    private BottomNavigationView bottom_navi; // Declare a BottomNavigationView variable.
    private ViewPager2 mViewPager; // Declare a ViewPager2 variable.

    private DrawerLayout drawer_layout; // Declare a DrawerLayout variable.
    private Toolbar toolbar; // Declare a Toolbar variable.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottom_navi = findViewById(R.id.bottom_navi); // Find the BottomNavigationView in the layout.
        mViewPager = findViewById(R.id.view_pager); // Find the ViewPager2 in the layout.
        drawer_layout = findViewById(R.id.drawer_layout); // Find the DrawerLayout in the layout
        // Set up the ViewPager for handling swipeable screens.
        setUpViewPager();
        // Set up the Toolbar and connect it to the ActionBar.
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Create an ActionBarDrawerToggle for syncing the ActionBar with the DrawerLayout.
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer_layout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer_layout.addDrawerListener(toggle); // Add a DrawerListener to the DrawerLayout.
        toggle.syncState(); // Synchronize the ActionBarDrawerToggle state with the DrawerLayout.

        // Set up click events for the NavigationView.
        NavigationView navigation_view = findViewById(R.id.navigation_view);
        navigation_view.setNavigationItemSelectedListener(this);

        // Set up click events for the items in the BottomNavigationView.
        bottom_navi.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // Handle item clicks in the BottomNavigationView.
                int id = item.getItemId();
                if (id == R.id.active_sensor) {
                    openSensorFragment();
                } else if (id == R.id.active_actuator) {
                    openActuatorFragment();
                } else if (id == R.id.active_timer) {
                    openTimerFragment();
                }
                return true;
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
                        bottom_navi.getMenu().findItem(R.id.active_sensor).setChecked(true);
                        break;
                    case 1:
                        mCurrentFragment = FRAGMENT_ACTUATOR; //sửa trạng thái của biến mCurrentFragment thành FRAGMENT_ACTUATOR
                        navigation_view.getMenu().findItem(R.id.active_actuator).setChecked(true);
                        bottom_navi.getMenu().findItem(R.id.active_actuator).setChecked(true);
                        break;
                    case 2:
                        mCurrentFragment = FRAGMENT_TIMER; //sửa trạng thái của biến mCurrentFragment thành FRAGMENT_TIMER
                        navigation_view.getMenu().findItem(R.id.active_timer).setChecked(true);
                        bottom_navi.getMenu().findItem(R.id.active_timer).setChecked(true);
                        break;
                }
            }
        });
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
            bottom_navi.getMenu().findItem(R.id.active_sensor).setChecked(true);
        } else if (id == R.id.active_actuator) {
            openActuatorFragment();
            bottom_navi.getMenu().findItem(R.id.active_actuator).setChecked(true);
        } else if (id == R.id.active_timer) {
            openTimerFragment();
            bottom_navi.getMenu().findItem(R.id.active_timer).setChecked(true);
        }

        drawer_layout.closeDrawer(GravityCompat.START); // Close the navigation drawer.
        return true;
    }
    public void goToSensorDevice(String nameSensor, int index) { //tạo phương thức chuyển hướng Fragment từ Room sang Device

        //tạo Intent để chuyển từ MainActivity sang Device Activity đồng thời bundle dữ liệu sang
        Intent i = new Intent(this, SensorDevice.class);
        Bundle bundle = new Bundle();
        bundle.putString("nameSensor", nameSensor);
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
}