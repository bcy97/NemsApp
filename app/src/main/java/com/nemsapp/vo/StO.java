package com.nemsapp.vo;

public class StO {
    private int id = -1;
    private short unitNo;
    private short ptNo;
    private String sname;
    private String cname;
    private byte mask;
    private byte swidef;
    private byte type;
    private byte epd;
    private byte soe;
    private byte alarm;
    private String standby;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public short getUnitNo() {
        return unitNo;
    }

    public void setUnitNo(short unitNo) {
        this.unitNo = unitNo;
    }

    public short getPtNo() {
        return ptNo;
    }

    public void setPtNo(short ptNo) {
        this.ptNo = ptNo;
    }

    public String getSname() {
        return sname;
    }

    public void setSname(String sname) {
        this.sname = sname;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public byte getMask() {
        return mask;
    }

    public void setMask(byte mask) {
        this.mask = mask;
    }

    public byte getSwidef() {
        return swidef;
    }

    public void setSwidef(byte swidef) {
        this.swidef = swidef;
    }

    public byte getType() {
        return type;
    }

    public void setType(byte type) {
        this.type = type;
    }

    public byte getEpd() {
        return epd;
    }

    public void setEpd(byte epd) {
        this.epd = epd;
    }

    public byte getSoe() {
        return soe;
    }

    public void setSoe(byte soe) {
        this.soe = soe;
    }

    public byte getAlarm() {
        return alarm;
    }

    public void setAlarm(byte alarm) {
        this.alarm = alarm;
    }

    public String getStandby() {
        return standby;
    }

    public void setStandby(String standby) {
        this.standby = standby;
    }

}
