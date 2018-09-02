package com.nemsapp.vo;

import com.bin.david.form.annotation.SmartColumn;

public class RealData {

    @SmartColumn(name = "name")
    private String name;
    @SmartColumn(name = "data")
    private double data;
    @SmartColumn(name = "unit")
    private String unit;

    public RealData(String name) {
        this.name = name;
    }

    public RealData(String name, double data, String unit) {
        this.name = name;
        this.data = data;
        this.unit = unit;
    }

    public double getData() {
        return data;
    }

    public void setData(double data) {
        this.data = data;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
