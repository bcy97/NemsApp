package com.nemsapp.vo;

/***
 * 遥测
 * */
public class AnO {
    private int id = -1;// id
    private short unitNo;// 单元号
    private short ptNo;// 点号
    private String cname;// 中文描述
    private String sname;// 点名
    private String lgName;// 单位
    private byte librank; // 库级别 0：不存 1：5分钟存一次 2：1小时存一次
    private byte mask; // 使能
    private byte nominus; // 无负数
    private float refV;// 参考值
    private float fi; // 比例
    private float zeroV; // 零区
    private float offsetV; // 零偏
    private byte type;// 类型
    private float upV; // 上限
    private float uupV; // 上上限
    private float dwV; // 下限
    private float ddwV; // 下下限
    private byte alarm;// 报警
    private byte poinum;// 小数位

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

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public String getSname() {
        return sname;
    }

    public void setSname(String sname) {
        this.sname = sname;
    }

    public String getLgName() {
        return lgName;
    }

    public void setLgName(String lgName) {
        this.lgName = lgName;
    }

    public byte getLibrank() {
        return librank;
    }

    public void setLibrank(byte librank) {
        this.librank = librank;
    }

    public byte getMask() {
        return mask;
    }

    public void setMask(byte mask) {
        this.mask = mask;
    }

    public byte getNominus() {
        return nominus;
    }

    public void setNominus(byte nominus) {
        this.nominus = nominus;
    }

    public float getRefV() {
        return refV;
    }

    public void setRefV(float refV) {
        this.refV = refV;
    }

    public float getFi() {
        return fi;
    }

    public void setFi(float fi) {
        this.fi = fi;
    }

    public float getZeroV() {
        return zeroV;
    }

    public void setZeroV(float zeroV) {
        this.zeroV = zeroV;
    }

    public float getOffsetV() {
        return offsetV;
    }

    public void setOffsetV(float offsetV) {
        this.offsetV = offsetV;
    }

    public byte getType() {
        return type;
    }

    public void setType(byte type) {
        this.type = type;
    }

    public float getUpV() {
        return upV;
    }

    public void setUpV(float upV) {
        this.upV = upV;
    }

    public float getUupV() {
        return uupV;
    }

    public void setUupV(float uupV) {
        this.uupV = uupV;
    }

    public float getDwV() {
        return dwV;
    }

    public void setDwV(float dwV) {
        this.dwV = dwV;
    }

    public float getDdwV() {
        return ddwV;
    }

    public void setDdwV(float ddwV) {
        this.ddwV = ddwV;
    }

    public byte getAlarm() {
        return alarm;
    }

    public void setAlarm(byte alarm) {
        this.alarm = alarm;
    }

    public byte getPoinum() {
        return poinum;
    }

    public void setPoinum(byte poinum) {
        this.poinum = poinum;
    }

}
