package com.example.aquasys.object;

import com.example.aquasys.R;

import java.util.ArrayList;
import java.util.List;


public class actuator {
    String name;
    int status;
    int img;
    int hour;
    int minute;


    //no-argument constructor
    public actuator(){}
    // constructor
    public actuator(String name, int img   , int status) {
        this.name = name;
        this.img = img;
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }
    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public static List<actuator> globalActuator = new ArrayList<>();
    public static List<actuator> listActuator(){
        if(globalActuator.isEmpty()){
            globalActuator.add(0, new actuator("BULB 1" , R.drawable.lightbulb , 0 ));
            globalActuator.add(1, new actuator("BULB 2" , R.drawable.lightbulb , 0 ));
            globalActuator.add(2, new actuator("PUMP 1" , R.drawable.water_pump , 0 ));
            globalActuator.add(3, new actuator("HEATER 1" , R.drawable.heater , 0 ));
        }
        return globalActuator;
    }

}
