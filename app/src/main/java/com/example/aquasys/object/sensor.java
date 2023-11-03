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

    public sensor(String name, int img) {
        this.name = name;
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
    public static List<sensor> listSensor(){
        List<sensor> globalSensor =new ArrayList<>();
        globalSensor.add(0 , new sensor("Humidity", R.drawable.humidity_percentage) );
        globalSensor.add(1 , new sensor("Temperature", R.drawable.device_thermostat) );
        globalSensor.add(2 , new sensor("Water level",R.drawable.water_level) );
        globalSensor.add(3 , new sensor("PH", R.drawable.ph) );
        globalSensor.add(4 , new sensor("Light Sensor", R.drawable.light_mode) );
        globalSensor.add(5 , new sensor("Moisture Humi", R.drawable.soil) );
        return globalSensor;
    }
}
