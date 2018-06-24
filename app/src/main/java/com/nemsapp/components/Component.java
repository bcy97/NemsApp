package com.nemsapp.components;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;

import com.nemsapp.util.PathParser;

public class Component {

    private Context context;
    private Paint paint;
    private Path path;

    private String name;
    private double x;
    private double y;
    private float strokeWidth;
    private String color;

    private String str_path;

    public Component(Context context) {
        this.context = context;
        paint = new Paint();
    }

    public void draw(Canvas canvas) {
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.parseColor(color));
        path = PathParser.createPathFromPathData(str_path);
        canvas.drawPath(path, paint);
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

    public float getStrokeWidth() {
        return strokeWidth;
    }

    public void setStrokeWidth(float strokeWidth) {
        this.strokeWidth = strokeWidth;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getStr_path() {
        return str_path;
    }

    public void setStr_path(String str_path) {
        this.str_path = str_path;
    }
}
