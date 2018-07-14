package com.nemsapp.components;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;

public class DyanData {

    private Paint paint;

    private String text;
    private String name;
    private int x;
    private int y;
    private int size;
    private int loc_y;

    public DyanData() {
        paint = new Paint();
    }

    public void draw(Canvas canvas) {
        canvas.drawText(text, x, loc_y, paint);
    }

    public void init() {
        //设置抗锯齿
        paint.setAntiAlias(true);
        //设置画笔大小
        paint.setStrokeWidth(1);
        //设置字体大小
        paint.setTextSize(size);
        //设置文字样式
        paint.setTypeface(Typeface.DEFAULT);
        //设置文字颜色
        paint.setColor(Color.RED);
        //设置文字baseline向下偏移size
        loc_y = y + size;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
