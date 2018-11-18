package com.nemsapp.components.image;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;

import com.nemsapp.components.image.ImageStatue;
import com.nemsapp.util.PathParser;

/**
 * 对应xml文件内的ImageStatue，iconType为0，即为图原的图标
 */
public class ImageStatue_0 extends ImageStatue {

    private Path path_on;  //开状态路径
    private Path path_off; //关状态路径

    private double x;  //坐标x
    private double y;  //坐标y
    private int strokeWidth;  //线宽
    private String color;  //颜色
    private String on_path;  //开状态路径字符串
    private String off_path; //关状态路径字符串


    private String str_path;  //加上坐标后的路径字符串

    private Paint.Style style = Paint.Style.STROKE;  //画笔样式

    public ImageStatue_0() {
        paint = new Paint();
        on = true;
    }

    @Override
    public void draw(Canvas canvas) {
        paint.setColor(Color.parseColor(color));
        if (on) {
            canvas.drawPath(path_on, paint);
        } else {
            canvas.drawPath(path_off, paint);
        }
    }

    public void init() {
        paint.setStyle(style);

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
