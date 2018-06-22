package com.nemsapp.vo;

import com.bin.david.form.annotation.SmartColumn;

public class NowData {

    @SmartColumn(name = "data1")
    private double data1;
    @SmartColumn(name = "data2")
    private double data2;
    @SmartColumn(name = "data3")
    private double data3;

    public NowData() {
    }

    public NowData(double data1, double data2, double data3) {
        this.data1 = data1;
        this.data2 = data2;
        this.data3 = data3;
    }

    public double getData1() {
        return data1;
    }

    public void setData1(double data1) {
        this.data1 = data1;
    }

    public double getData2() {
        return data2;
    }

    public void setData2(double data2) {
        this.data2 = data2;
    }

    public double getData3() {
        return data3;
    }

    public void setData3(double data3) {
        this.data3 = data3;
    }

}
