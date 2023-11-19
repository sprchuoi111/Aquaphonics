package com.example.aquasys.object;

public class plant {
    String Description;
    String name;
    actuator act;
    timer tim;
    sensor sen;
    type_of_plant typeOfPlant;
    enum  type_of_plant{
        // for vegetable
        Vegetative,
        // for flower
        Flowering,

    }

    public plant(String description, String name, actuator act, timer tim, sensor sen, type_of_plant typeOfPlant) {
        Description = description;
        this.name = name;
        this.act = act;
        this.tim = tim;
        this.sen = sen;
        this.typeOfPlant = typeOfPlant;
    }
    // non init
    public plant() {
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

    public actuator getAct() {
        return act;
    }

    public void setAct(actuator act) {
        this.act = act;
    }

    public timer getTim() {
        return tim;
    }

    public void setTim(timer tim) {
        this.tim = tim;
    }

    public sensor getSen() {
        return sen;
    }

    public void setSen(sensor sen) {
        this.sen = sen;
    }
}
