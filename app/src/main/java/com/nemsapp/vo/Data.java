package com.nemsapp.vo;

import com.bin.david.form.annotation.SmartColumn;
import com.bin.david.form.annotation.SmartTable;

@SmartTable(name = "遥测量")
public class Data {

    @SmartColumn(name = "data1")
    private double data1;
    @SmartColumn(name = "data2")
    private double data2;
    @SmartColumn(name = "data3")
    private double data3;
    @SmartColumn(name = "data4")
    private double data4;
    @SmartColumn(name = "data5")
    private double data5;
    @SmartColumn(name = "data6")
    private double data6;
    @SmartColumn(name = "data7")
    private double data7;
    @SmartColumn(name = "data8")
    private double data8;
    @SmartColumn(name = "data9")
    private double data9;
    @SmartColumn(name = "data10")
    private double data10;
    @SmartColumn(name = "data11")
    private double data11;
    @SmartColumn(name = "data12")
    private double data12;

    public Data() {
    }

    public Data(double data1, double data2, double data3, double data4, double data5, double data6, double data7, double data8, double data9, double data10, double data11, double data12) {
        this.data1 = data1;
        this.data2 = data2;
        this.data3 = data3;
        this.data4 = data4;
        this.data5 = data5;
        this.data6 = data6;
        this.data7 = data7;
        this.data8 = data8;
        this.data9 = data9;
        this.data10 = data10;
        this.data11 = data11;
        this.data12 = data12;
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

    public double getData4() {
        return data4;
    }

    public void setData4(double data4) {
        this.data4 = data4;
    }

    public double getData5() {
        return data5;
    }

    public void setData5(double data5) {
        this.data5 = data5;
    }

    public double getData6() {
        return data6;
    }

    public void setData6(double data6) {
        this.data6 = data6;
    }

    public double getData7() {
        return data7;
    }

    public void setData7(double data7) {
        this.data7 = data7;
    }

    public double getData8() {
        return data8;
    }

    public void setData8(double data8) {
        this.data8 = data8;
    }

    public double getData9() {
        return data9;
    }

    public void setData9(double data9) {
        this.data9 = data9;
    }
}
