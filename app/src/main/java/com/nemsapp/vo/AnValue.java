package com.nemsapp.vo;

public class AnValue {
    private float value = -1;
    private byte valid = 0;//是否有效位，1是有效，0是无效

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public byte getValid() {
        return valid;
    }

    public void setValid(byte valid) {
        this.valid = valid;
    }

    public AnValue() {

    }

    public AnValue(byte valid, float value) {
        this.valid = valid;
        this.value = value;
    }
}

