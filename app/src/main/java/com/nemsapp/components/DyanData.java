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

    //字体渲染位置
    private int loc_x;
    private int loc_y;

    //对齐方式，0为左对齐，1为居中，2为右对齐
    private int align;
    //最右端
    private int right;

    //是否有效
    private int valid;

    public DyanData() {
        paint = new Paint();
    }

    public void draw(Canvas canvas) {

        //设置文字颜色
        switch (valid) {
            case 0:
                paint.setColor(Color.YELLOW);
                break;
            case 1:
                paint.setColor(Color.WHITE);
                break;
            case 2:
                paint.setColor(Color.RED);
                break;
            case 3:
                paint.setColor(Color.GREEN);
                break;
        }

        canvas.drawText(text, loc_x, loc_y, paint);
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
        //设置文字baseline向下偏移size
        loc_y = y + size;
        //初始化text为空
        text = "";

        //设置对齐
        switch (align) {
            case 0:
                loc_x = x;
                break;
            case 1:
                loc_x = (x + right) / 2;
                paint.setTextAlign(Paint.Align.CENTER);
                break;
            case 2:
                loc_x = right;
                paint.setTextAlign(Paint.Align.RIGHT);
                break;
        }


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

    public int getAlign() {
        return align;
    }

    public void setAlign(int align) {
        this.align = align;
    }

    public int getRight() {
        return right;
    }

    public void setRight(int right) {
        this.right = right;
    }

    public int getValid() {
        return valid;
    }

    public void setValid(int valid) {
        this.valid = valid;
    }
}
