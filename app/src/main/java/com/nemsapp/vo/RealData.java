package com.nemsapp.vo;

import com.bin.david.form.annotation.SmartColumn;

public class RealData {

    @SmartColumn(name = "ptNo")
    private Short ptNo;
    @SmartColumn(name = "cname")
    private String cname;
    @SmartColumn(name = "value")
    private double value;
    @SmartColumn(name = "lgName")
    private String lgName;

    public RealData(short ptNo, String name, double value, String lgName) {
        this.ptNo = ptNo;
        this.cname = name;
        this.value = value;
        this.lgName = lgName;
    }

}
