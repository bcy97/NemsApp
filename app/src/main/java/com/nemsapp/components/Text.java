package com.nemsapp.components;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;

public class Text {

    private Paint paint;

    private String text;
    private int x;
    private int y;
    private int size;

    //字体渲染位置
    private int loc_x;
    private int loc_y;

    private String color;
    //对齐方式，0为左对齐，1为居中，2为右对齐
    private int align;
    //最右端
    private int right;

    public Text() {
        paint = new Paint();
    }

    public void draw(Canvas canvas) {
        //全部显示，起点在(50,100)点
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
        //设置文字颜色
        int c = Color.parseColor(color);
        paint.setColor(c);
        //设置文字baseline向下偏移size
        loc_y = y + size;

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
        //设置对齐
//        paint.setTextAlign(Paint.Align.RIGHT);
        //设置字体
//        paint.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
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

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
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
}
