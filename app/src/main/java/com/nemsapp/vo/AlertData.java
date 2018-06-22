package com.nemsapp.vo;

import com.bin.david.form.annotation.SmartColumn;

public class AlertData {
    @SmartColumn(name = "id")
    private double id;
    @SmartColumn(name = "time")
    private double time;
    @SmartColumn(name = "unit")
    private double unit;
    @SmartColumn(name = "event")
    private double event;
    @SmartColumn(name = "content")
    private double content;
    @SmartColumn(name = "data")
    private double data;

    public AlertData() {
    }

    public AlertData(double id, double time, double unit, double event, double content, double data) {
        this.id = id;
        this.time = time;
        this.unit = unit;
        this.event = event;
        this.content = content;
        this.data = data;
    }

    public double getId() {
        return id;
    }

    public void setId(double id) {
        this.id = id;
    }

    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }

    public double getUnit() {
        return unit;
    }

    public void setUnit(double unit) {
        this.unit = unit;
    }

    public double getEvent() {
        return event;
    }

    public void setEvent(double event) {
        this.event = event;
    }

    public double getContent() {
        return content;
    }

    public void setContent(double content) {
        this.content = content;
    }

    public double getData() {
        return data;
    }

    public void setData(double data) {
        this.data = data;
    }
}
