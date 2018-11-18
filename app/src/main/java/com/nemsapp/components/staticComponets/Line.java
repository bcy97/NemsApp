package com.nemsapp.components.staticComponets;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;

import com.nemsapp.components.Component;
import com.nemsapp.util.PathParser;

public class Line extends Component {

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

//        paint.setStrokeWidth(strokeWidth);
        canvas.drawPath(path, paint);
    }

    public void init() {
        paint.setColor(Color.parseColor(color));
        str_path = "m" + x1 + "," + y1 + " l" + (x2 - x1) + "," + (y2 - y1);
        path = PathParser.createPathFromPathData(str_path);
    }

    public int getX1() {
        return x1;
    }

    public void setX1(int x1) {
        this.x1 = x1;
    }

    public void setY1(int y1) {
        this.y1 = y1;
    }

    public void setX2(int x2) {
        this.x2 = x2;
    }

    public void setY2(int y2) {
        this.y2 = y2;
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
