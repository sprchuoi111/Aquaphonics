package com.example.aquasys.object;

import com.example.aquasys.R;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.List;

public class sensor {
    String name;
    String value;
    int img;
    int status;

    public sensor(String name,String value, int img) {
        this.name = name;
        this.value = value;
        this.img = img;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
    // sensor list for fish
    private static List<sensor> globalSensor_enviroment = new ArrayList<>();
    // sensor list for tree
    private static List<sensor> globalSensor_water = new ArrayList<>();


    // sensor list for aqua
    public static List<sensor> listSensor_environment() {
        if (globalSensor_enviroment.isEmpty()) {
            globalSensor_enviroment.add(new sensor("Humidity","null", R.drawable.humidity_percentage));
            globalSensor_enviroment.add(new sensor("Temperature", "null",R.drawable.device_thermostat));
            globalSensor_enviroment.add(new sensor("Light Sensor", "null",R.drawable.light_mode));
            globalSensor_enviroment.add(new sensor("Moisture Humi", "null",R.drawable.soil));
        }
        return globalSensor_enviroment;
    }
    // sensor list for hydro
    public static List<sensor> listSensor_water() {
        if (globalSensor_water.isEmpty()) {
            globalSensor_water.add(new sensor("PH","null", R.drawable.ph));
            globalSensor_water.add(new sensor("Water Level", "null",R.drawable.water_level));
        }
        return globalSensor_water;
    }
}
