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

    private int strokeWidth;  //线宽
    private String color;  //颜色
    private String on_path;  //开状态路径字符串
    private String off_path; //关状态路径字符串

    private int on_fill;  //开状态是否填充
    private int off_fill;  //闭状态是否填充

    private String str_path;  //加上坐标后的路径字符串


    public ImageStatue_0() {
        paint = new Paint();
        on = true;
    }

    @Override
    public void draw(Canvas canvas) {
        paint.setColor(Color.parseColor(color));
        if (on) {
            if (on_fill == 1) {
                paint.setStyle(Paint.Style.FILL_AND_STROKE);
            } else {
                paint.setStyle(Paint.Style.STROKE);
            }
            canvas.drawPath(path_on, paint);
        } else {
            if (off_fill == 1) {
                paint.setStyle(Paint.Style.FILL_AND_STROKE);
            } else {
                paint.setStyle(Paint.Style.STROKE);
            }
            canvas.drawPath(path_off, paint);
        }
    }

    public void init() {
        str_path = "m" + rect.left + "," + rect.top + on_path;
        path_on = PathParser.createPathFromPathData(str_path);
        str_path = "m" + rect.left + "," + rect.top + off_path;
        path_off = PathParser.createPathFromPathData(str_path);
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

    public void setOn_path(String on_path) {
        this.on_path = on_path;
    }

    public void setOff_path(String off_path) {
        this.off_path = off_path;
    }

    public void setOn_fill(int on_fill) {
        this.on_fill = on_fill;
    }

    public void setOff_fill(int off_fill) {
        this.off_fill = off_fill;
    }
}
