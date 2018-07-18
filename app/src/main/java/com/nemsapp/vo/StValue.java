package com.nemsapp.vo;

public class StValue {

    public StValue() {

    }

    public StValue(byte valid, byte value) {
        this.valid = valid;
        this.value = value;
    }

    private byte value = -1;
    private byte valid = 0;

    public byte getValue() {
        return value;
    }

    public void setValue(byte value) {
        this.value = value;
    }

    public byte getValid() {
        return valid;
    }

    public void setValid(byte valid) {
        this.valid = valid;
    }

}
