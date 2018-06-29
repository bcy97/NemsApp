package com.nemsapp.components;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public class CommandButton {

    private Paint paint;

    private String name;
    private Rect rect;
    private Bitmap down;
    private Bitmap up;


    public CommandButton() {
        paint = new Paint();
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(down, null, rect, paint);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRect(Rect rect) {
        this.rect = rect;
    }

    public void setDown(Bitmap down) {
        this.down = down;
    }

    public void setUp(Bitmap up) {
        this.up = up;
    }
}
