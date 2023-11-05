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
    private static List<sensor> globalSensor = new ArrayList<>();
    public static List<sensor> listSensor() {
        if (globalSensor.isEmpty()) {
            globalSensor.add(new sensor("Humidity","null", R.drawable.humidity_percentage));
            globalSensor.add(new sensor("Temperature", "null",R.drawable.device_thermostat));
            globalSensor.add(new sensor("Water level", "null",R.drawable.water_level));
            globalSensor.add(new sensor("PH", "null",R.drawable.ph));
            globalSensor.add(new sensor("Light Sensor", "null",R.drawable.light_mode));
            globalSensor.add(new sensor("Moisture Humi", "null",R.drawable.soil));
        }
        return globalSensor;
    }
}
