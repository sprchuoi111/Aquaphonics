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
    String id;

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
    //no-argument constructor
    public sensor(){}

    public sensor(String name,String value, int img , typeofsensor type , String id) {
        this.name = name;
        this.value = value;
        this.img = img;
        this.type = type;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public static List<sensor> getGlobalSensor_enviroment() {
        return globalSensor_enviroment;
    }

    public static void setGlobalSensor_enviroment(List<sensor> globalSensor_enviroment) {
        sensor.globalSensor_enviroment = globalSensor_enviroment;
    }

    public static List<sensor> getGlobalSensor_water() {
        return globalSensor_water;
    }

    public static void setGlobalSensor_water(List<sensor> globalSensor_water) {
        sensor.globalSensor_water = globalSensor_water;
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
    public static  List<sensor> globalSensor_enviroment = new ArrayList<>();
    // sensor list for tree
    public static  List<sensor> globalSensor_water = new ArrayList<>();


    // sensor list for aqua
    public static List<sensor> listSensor_environment() {
        if (globalSensor_enviroment.isEmpty()) {
            globalSensor_enviroment.add(new sensor("Humidity", "-1", R.drawable.humidity_percentage, typeofsensor.humidity , "ENS00001"));
            globalSensor_enviroment.add(new sensor("Temperature", "-1", R.drawable.device_thermostat, typeofsensor.temp ,"ENS00002"));
            globalSensor_enviroment.add(new sensor("Light Sensor", "-1", R.drawable.light_mode, typeofsensor.light, "ENS00003"));
            globalSensor_enviroment.add(new sensor("Moisture Humi", "-1", R.drawable.soil, typeofsensor.soil_moisture , "ENS00004"));

        }
        return globalSensor_enviroment;
    }
    // sensor list for hydro
    public static List<sensor> listSensor_water() {
        if (globalSensor_water.isEmpty()) {
            globalSensor_water.add(new sensor("PH","-1", R.drawable.ph , typeofsensor.ph , "WTS00001"));
            globalSensor_water.add(new sensor("Water Level", "-1",R.drawable.water_level , typeofsensor.level_water , "WTS00002"));
        }
        return globalSensor_water;
    }

}

