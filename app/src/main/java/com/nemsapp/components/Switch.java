package com.nemsapp.components;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Region;

import com.nemsapp.util.PathParser;
import com.nemsapp.R;

/**
 * Created by bcy on 2018/3/11.
 */

public class Switch {

    private Context context;
    private Paint paint;
    private boolean isOn;
    private String name;

    private String location;
    private double x;
    private double y;
    private Path path;

    private final static String on = "m16,0 a3,3 0 0,1 0,6 a3,3 0 0,1 0,-6 m0,6 l0,20 a3,3 0 0,1 0,6 a3,3 0 0,1 0,-6 ";
    private final static String off = "m16,0 a3,3 0 0,1 0,6 a3,3 0 0,1 0,-6 m0,3 l10,24 m-10,-1 a3,3 0 0,1 0,6 a3,3 0 0,1 0,-6 ";

    private String truePath;

    private Region region;

    public Switch(Context context, float strokeWidth) {
        this.context = context;
        paint = new Paint();
        paint.setStrokeWidth(strokeWidth);
        paint.setAntiAlias(true);
        isOn = false;
    }

    private void calcuteXY(String location) {
        location = location.substring(1);
        location.trim();
        String[] xy = location.split(",");
        x = Integer.parseInt(xy[0]);
        y = Integer.parseInt(xy[1]);
    }

    public void draw(Canvas canvas) {
        paint.setStyle(Paint.Style.STROKE);
        if (isOn()) {
            truePath = location + on;
            paint.setColor(this.context.getResources().getColor(R.color.coler0));

        } else {
            truePath = location + off;
            paint.setColor(this.context.getResources().getColor(R.color.color1));
        }
        path = PathParser.createPathFromPathData(truePath);
        canvas.drawPath(path, paint);
    }

    public Path getPath() {
        return path;
    }

    public void setPath(Path path) {
        this.path = path;
    }

    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
        calcuteXY(location);
        region = new Region((int) x + 8, (int) y, (int) x + 32, (int) y + 32);
    }

    public boolean isOn() {
        return isOn;
    }

    public void setOn(boolean on) {
        isOn = on;
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
}
