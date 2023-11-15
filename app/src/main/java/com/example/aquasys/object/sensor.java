package com.example.aquasys.object;

import com.example.aquasys.R;

import java.util.ArrayList;
import java.util.List;

public class sensor {
    String name;
    String value;
    int img;
    int status;
    typeofsensor type;

    public String getType() {
        return type.toString();
    }

    public void setType(typeofsensor type) {
        this.type = type;
    }

    enum typeofsensor{
        humidity,
        temp,
        soil_moisture,
        light,
        ph ,
        level_water
    }

    public sensor(String name,String value, int img , typeofsensor type) {
        this.name = name;
        this.value = value;
        this.img = img;
        this.type = type;
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
    private static final List<sensor> globalSensor_enviroment = new ArrayList<>();
    // sensor list for tree
    private static final List<sensor> globalSensor_water = new ArrayList<>();


    // sensor list for aqua
    public static List<sensor> listSensor_environment() {
        if (globalSensor_enviroment.isEmpty()) {
            globalSensor_enviroment.add(new sensor("Humidity", "null", R.drawable.humidity_percentage, typeofsensor.humidity));
            globalSensor_enviroment.add(new sensor("Temperature", "null", R.drawable.device_thermostat, typeofsensor.temp));
            globalSensor_enviroment.add(new sensor("Light Sensor", "null", R.drawable.light_mode, typeofsensor.light));
            globalSensor_enviroment.add(new sensor("Moisture Humi", "null", R.drawable.soil, typeofsensor.soil_moisture));

        }
        return globalSensor_enviroment;
    }
    // sensor list for hydro
    public static List<sensor> listSensor_water() {
        if (globalSensor_water.isEmpty()) {
            globalSensor_water.add(new sensor("PH","null", R.drawable.ph , typeofsensor.ph));
            globalSensor_water.add(new sensor("Water Level", "null",R.drawable.water_level , typeofsensor.level_water));
        }
        return globalSensor_water;
    }
}

