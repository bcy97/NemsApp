package com.nemsapp.components;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;

import com.nemsapp.util.PathParser;

public class Image_0 {
    private Paint paint;
    private Path path;

    private String name;
    private double x;
    private double y;
    private int strokeWidth;
    private String color;
    private String com_path;

    private String str_path;

    private Paint.Style style = Paint.Style.STROKE;

    public Image_0() {
        paint = new Paint();
    }

    public void draw(Canvas canvas) {

        canvas.drawPath(path, paint);
    }

    public void init() {
        paint.setStyle(style);
        paint.setColor(Color.parseColor(color));

        str_path = "m" + x + "," + y + com_path;

        path = PathParser.createPathFromPathData(str_path);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
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

    public String getCom_path() {
        return com_path;
    }

    public void setCom_path(String com_path) {
        this.com_path = com_path;
    }

    public void setStyle(Paint.Style style) {
        this.style = style;
    }
}
