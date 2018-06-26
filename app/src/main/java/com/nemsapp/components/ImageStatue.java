package com.nemsapp.components;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;

import com.nemsapp.util.PathParser;

public class ImageStatue {
    private Paint paint;
    private Path path_on;
    private Path path_off;

    private String name;
    private double x;
    private double y;
    private int strokeWidth;
    private String color;
    private String on_path;
    private String off_path;
    private boolean on;

    private String str_path;

    private Paint.Style style = Paint.Style.STROKE;

    public ImageStatue() {
        paint = new Paint();
        on = true;
    }

    public void draw(Canvas canvas) {
        if (on) {
            canvas.drawPath(path_on, paint);
        } else {
            canvas.drawPath(path_off, paint);
        }
    }

    public void init() {
        paint.setStyle(style);
        paint.setColor(Color.parseColor(color));

        str_path = "m" + x + "," + y + on_path;
        path_on = PathParser.createPathFromPathData(str_path);
        str_path = "m" + x + "," + y + off_path;
        path_off = PathParser.createPathFromPathData(str_path);
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

    public void setOn() {
        this.on = true;
    }

    public void setOff() {
        this.on = false;
    }

    public String getOn_path() {
        return on_path;
    }

    public void setOn_path(String on_path) {
        this.on_path = on_path;
    }

    public String getOff_path() {
        return off_path;
    }

    public void setOff_path(String off_path) {
        this.off_path = off_path;
    }

    public void setStyle(Paint.Style style) {
        this.style = style;
    }
}
