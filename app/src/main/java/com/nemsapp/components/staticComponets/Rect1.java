package com.nemsapp.components.staticComponets;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.nemsapp.components.Component;

/**
 * 对应pic中rect，和Android的rect区分
 */
public class Rect1 extends Component {

    private int x1;
    private int y1;
    private int x2;
    private int y2;

    //是否有边框
    private int border;
    //边框颜色
    private String borderColor;
    //边框宽度
    private int borderWidth;

    //是否填充
    private int fill;
    //填充颜色
    private String fillColor;

    //是否圆角矩形
    private int round;
    //圆角半径
    private int r;

    public Rect1() {
        paint = new Paint();
    }

    @Override
    public void draw(Canvas canvas) {

        if (round == 0) {
            //画边框
            if (border == 1) {
                paint.setStyle(Paint.Style.STROKE);
                paint.setColor(Color.parseColor(borderColor));
                paint.setStrokeWidth(borderWidth);
                canvas.drawRect(x1, y1, x2, y2, paint);
            }
            //画矩形颜色
            if (fill == 1) {
                paint.setStyle(Paint.Style.FILL);
                paint.setColor(Color.parseColor(fillColor));
                canvas.drawRect(x1, y1, x2, y2, paint);
            }
        } else {
            //画边框
            if (border == 1) {
                paint.setStyle(Paint.Style.STROKE);
                paint.setColor(Color.parseColor(borderColor));
                paint.setStrokeWidth(borderWidth);
                canvas.drawRoundRect(x1, y1, x2, y2, r, r, paint);
            }
            //画矩形颜色
            if (fill == 1) {
                paint.setStyle(Paint.Style.FILL);
                paint.setColor(Color.parseColor(fillColor));
                canvas.drawRoundRect(x1, y1, x2, y2, r, r, paint);
            }
        }
    }

    public int getX1() {
        return x1;
    }

    public void setX1(int x1) {
        this.x1 = x1;
    }

    public int getY1() {
        return y1;
    }

    public void setY1(int y1) {
        this.y1 = y1;
    }

    public int getX2() {
        return x2;
    }

    public void setX2(int x2) {
        this.x2 = x2;
    }

    public int getY2() {
        return y2;
    }

    public void setY2(int y2) {
        this.y2 = y2;
    }

    public int getBorder() {
        return border;
    }

    public void setBorder(int border) {
        this.border = border;
    }

    public String getBorderColor() {
        return borderColor;
    }

    public void setBorderColor(String borderColor) {
        this.borderColor = borderColor;
    }

    public int getBorderWidth() {
        return borderWidth;
    }

    public void setBorderWidth(int borderWidth) {
        this.borderWidth = borderWidth;
    }

    public int getFill() {
        return fill;
    }

    public void setFill(int fill) {
        this.fill = fill;
    }

    public String getFillColor() {
        return fillColor;
    }

    public void setFillColor(String fillColor) {
        this.fillColor = fillColor;
    }

    public int getRound() {
        return round;
    }

    public void setRound(int round) {
        this.round = round;
    }

    public int getR() {
        return r;
    }

    public void setR(int r) {
        this.r = r;
    }
}
