package com.nemsapp.components;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Typeface;

import com.nemsapp.R;

public class Text {

    private Context context;
    private Paint paint;

    private String text;
    private int x;
    private int y;
    private int size;

    public Text(Context context) {
        this.context = context;
        paint = new Paint();
    }

    public void draw(Canvas canvas) {
        //设置抗锯齿
        paint.setAntiAlias(true);
        //设置画笔大小
        paint.setStrokeWidth(1);
        //设置字体大小
        paint.setTextSize(size);
        //设置文字样式
        paint.setTypeface(Typeface.DEFAULT);
        //全部显示，起点在(50,100)点
        canvas.drawText(text, x, y + size, paint);
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
}
