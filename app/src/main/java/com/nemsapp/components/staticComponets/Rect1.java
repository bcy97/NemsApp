package com.nemsapp.components.staticComponets;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

import com.nemsapp.components.Component;

/**
 * 对应pic中rect，和Android的rect区分
 */
public class Rect1 extends Component {

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
                canvas.drawRect(rect, paint);
            }
            //画矩形颜色
            if (fill == 1) {
                paint.setStyle(Paint.Style.FILL);
                paint.setColor(Color.parseColor(fillColor));
                canvas.drawRect(rect, paint);
            }
        } else {
            System.out.println(rect);
            //画边框
            if (border == 1) {
                paint.setStyle(Paint.Style.STROKE);
                paint.setColor(Color.parseColor(borderColor));
                paint.setStrokeWidth(borderWidth);
                canvas.drawRoundRect(new RectF(rect), r, r, paint);
            }
            //画矩形颜色
            if (fill == 1) {
                paint.setStyle(Paint.Style.FILL);
                paint.setColor(Color.parseColor(fillColor));
                canvas.drawRoundRect(new RectF(rect), r, r, paint);
            }
        }
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
