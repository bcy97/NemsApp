package com.nemsapp.components.image;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;

import com.nemsapp.components.image.Image;
import com.nemsapp.util.PathParser;

/**
 * 对应xml文件内的Image，iconType为0，即为piclib的图标
 */
public class Image_0 extends Image {

    private Path path;

    private int strokeWidth;

    //颜色
    private String color;

    //图源绘制路线
    private String com_path;

    //pic图中绘制路线（加上了x和y坐标）
    private String str_path;

    //封闭曲线填充内部
    private int fill;


    public Image_0() {
        paint = new Paint();
    }

    @Override
    public void draw(Canvas canvas) {
        paint.setStrokeWidth(strokeWidth);
        canvas.drawPath(path, paint);
    }

    public void init() {

        if (fill == 1) {
            paint.setStyle(Paint.Style.FILL_AND_STROKE);
        } else {
            paint.setStyle(Paint.Style.STROKE);
        }

        paint.setColor(Color.parseColor(color));

        str_path = "m" + rect.left + "," + rect.top + com_path;

        path = PathParser.createPathFromPathData(str_path);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public void setFill(int fill) {
        this.fill = fill;
    }
}
