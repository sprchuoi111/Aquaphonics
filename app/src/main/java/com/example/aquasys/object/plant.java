package com.example.aquasys.object;

public class plant {
    String Description;
    String name;
    type_of_plant typeOfPlant;
    float low_temp,
            high_temp,
            low_humi ,
            high_humi ,
            low_ph ,
            high_ph ,
            low_light ,
            high_light,
            low_moisture ,
            high_moisture ;
    enum  type_of_plant{
        // for vegetable
        Vegetative,
        // for flower
        Flowering,

    }

    public plant(String description, String name, type_of_plant typeOfPlant) {
        Description = description;
        this.name = name;
        this.typeOfPlant = typeOfPlant;
    }
    // non init
    public plant() {
    }

    public type_of_plant getTypeOfPlant() {
        return typeOfPlant;
    }

    public void setTypeOfPlant(type_of_plant typeOfPlant) {
        this.typeOfPlant = typeOfPlant;
    }

    public float getLow_temp() {
        return low_temp;
    }

    public void setLow_temp(float low_temp) {
        this.low_temp = low_temp;
    }

    public float getHigh_temp() {
        return high_temp;
    }

    public void setHigh_temp(float high_temp) {
        this.high_temp = high_temp;
    }

    public float getLow_humi() {
        return low_humi;
    }

    public void setLow_humi(float low_humi) {
        this.low_humi = low_humi;
    }

    public float getHigh_humi() {
        return high_humi;
    }

    public void setHigh_humi(float high_humi) {
        this.high_humi = high_humi;
    }

    public float getLow_ph() {
        return low_ph;
    }

    public void setLow_ph(float low_ph) {
        this.low_ph = low_ph;
    }

    public float getHigh_ph() {
        return high_ph;
    }

    public void setHigh_ph(float high_ph) {
        this.high_ph = high_ph;
    }

    public float getLow_light() {
        return low_light;
    }

    public void setLow_light(float low_light) {
        this.low_light = low_light;
    }

    public float getHigh_light() {
        return high_light;
    }

    public void setHigh_light(float high_light) {
        this.high_light = high_light;
    }

    public float getLow_moisture() {
        return low_moisture;
    }

    public void setLow_moisture(float low_moisture) {
        this.low_moisture = low_moisture;
    }

    public float getHigh_moisture() {
        return high_moisture;
    }

    public void setHigh_moisture(float high_moisture) {
        this.high_moisture = high_moisture;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
