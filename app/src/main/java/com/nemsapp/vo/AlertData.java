package com.nemsapp.vo;

import com.bin.david.form.annotation.SmartColumn;

public class AlertData {
    @SmartColumn(name = "time")
    private String time;
    @SmartColumn(name = "unit")
    private String unit;
    @SmartColumn(name = "event")
    private String event;
    @SmartColumn(name = "info")
    private String info;
    @SmartColumn(name = "more")
    private String more;

    public AlertData() {
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getMore() {
        return more;
    }

    public void setMore(String more) {
        this.more = more;
    }
}
