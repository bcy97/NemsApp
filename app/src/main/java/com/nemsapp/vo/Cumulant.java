package com.nemsapp.vo;

import com.bin.david.form.annotation.SmartColumn;

public class Cumulant {

    private int id; //序号
    @SmartColumn(name = "name")
    private String name; //名称
    @SmartColumn(name = "today")
    private double today; //今日累加量
    @SmartColumn(name = "lastday")
    private double lastday; //昨天累加量
    @SmartColumn(name = "thisMonth")
    private double thisMonth; //本月累加量
    @SmartColumn(name = "lastMonth")
    private double lastMonth; //上月累加量
    @SmartColumn(name = "statis")
    private double statis; //统计量

    public Cumulant(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getToday() {
        return today;
    }

    public void setToday(double today) {
        this.today = today;
    }

    public double getLastday() {
        return lastday;
    }

    public void setLastday(double lastday) {
        this.lastday = lastday;
    }

    public double getThisMonth() {
        return thisMonth;
    }

    public void setThisMonth(double thisMonth) {
        this.thisMonth = thisMonth;
    }

    public double getLastMonth() {
        return lastMonth;
    }

    public void setLastMonth(double lastMonth) {
        this.lastMonth = lastMonth;
    }

    public double getStatis() {
        return statis;
    }

    public void setStatis(double statis) {
        this.statis = statis;
    }

    @Override
    public String toString() {
        return "Cumulant{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", today=" + today +
                ", lastday=" + lastday +
                ", thisMonth=" + thisMonth +
                ", lastMonth=" + lastMonth +
                ", statis=" + statis +
                '}';
    }
}
