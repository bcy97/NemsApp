package com.nemsapp.components;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;

import com.nemsapp.util.PathParser;

public class Line {

    private Paint paint;
    private Path path;

    private int x1;
    private int y1;
    private int x2;
    private int y2;

    private int strokeWidth;
    private String color;

    private String str_path;

    public Line() {
        paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
    }

    public void draw(Canvas canvas) {

        canvas.drawPath(path, paint);
    }

    public void init() {
        int c = Color.parseColor(color);
        paint.setColor(c);
        str_path = "m" + x1 + "," + y1 + " l" + (x2 - x1) + "," + (y2 - y1);
        path = PathParser.createPathFromPathData(str_path);
    }

    public int getX1() {
        return x1;
    }

    public void setX1(int x1) {
        this.x1 = x1;
    }

    public int getY1() {
        return y1;
    }

    public void setY1(int y1) {
        this.y1 = y1;
    }

    public int getX2() {
        return x2;
    }

    public void setX2(int x2) {
        this.x2 = x2;
    }

    public int getY2() {
        return y2;
    }

    public void setY2(int y2) {
        this.y2 = y2;
    }

    public int getStrokeWidth() {
        return strokeWidth;
    }

    public void setStrokeWidth(int strokeWidth) {
        this.strokeWidth = strokeWidth;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
