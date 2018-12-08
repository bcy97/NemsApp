package com.nemsapp.components.staticComponets;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;

import com.nemsapp.components.Component;
import com.nemsapp.util.PathParser;

public class Line extends Component {

    private Path path;

    private int strokeWidth;
    private String color;

    private String str_path;

    public Line() {
        paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
    }

    public void draw(Canvas canvas) {

        paint.setStrokeWidth(strokeWidth);
        canvas.drawPath(path, paint);
    }

    public void init() {
        paint.setColor(Color.parseColor(color));
        str_path = "m" + rect.left + "," + rect.top + " l" + (rect.right - rect.left) + "," + (rect.bottom - rect.top);
        path = PathParser.createPathFromPathData(str_path);
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
