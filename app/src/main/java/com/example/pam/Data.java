package com.example.pam;

public class Data {

    private String name;
    private double lastValue;

    public Data() {
    }

    public Data(String name, double lastValue) {
        this.name = name;
        this.lastValue = lastValue;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLastValue() {
        return lastValue;
    }

    public void setLastValue(double lastValue) {
        this.lastValue = lastValue;
    }
}
