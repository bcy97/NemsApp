package com.nemsapp.components.image;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;

import com.nemsapp.components.image.Image;
import com.nemsapp.util.PathParser;

/**
 * 对应xml文件内的Image，iconType为0，即为图原的图标
 */
public class Image_0 extends Image {

    private Path path;

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

    @Override
    public void draw(Canvas canvas) {
        paint.setStrokeWidth(strokeWidth);
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

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
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

    public void setCom_path(String com_path) {
        this.com_path = com_path;
    }

    public void setStyle(Paint.Style style) {
        this.style = style;
    }
}
