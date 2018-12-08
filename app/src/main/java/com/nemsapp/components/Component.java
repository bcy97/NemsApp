package com.nemsapp.components;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public class Component {

    public Paint paint;
    public Rect rect;

    public void draw(Canvas canvas) {
    }

    public Rect getRect() {
        return rect;
    }

    public void setRect(Rect rect) {
        this.rect = rect;
    }
}
