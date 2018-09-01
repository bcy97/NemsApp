package com.nemsapp.vo;

public class UnitInfo {

    public UnitInfo(String name, short unitNo, byte type) {
        this.name = name;
        this.type = type;
        this.unitNo = unitNo;
    }

    public UnitInfo() {

    }

    private String name;
    private short unitNo;
    private byte type;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public short getUnitNo() {
        return unitNo;
    }

    public void setUnitNo(short unitNo) {
        this.unitNo = unitNo;
    }

    public byte getType() {
        return type;
    }

    public void setType(byte type) {
        this.type = type;
    }
}

