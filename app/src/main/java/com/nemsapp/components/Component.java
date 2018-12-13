package com.nemsapp.components;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

public class Component {

    public Paint paint;
    public RectF rect;

    public void draw(Canvas canvas) {
    }

    public RectF getRect() {
        return rect;
    }

    public void setRect(RectF rect) {
        this.rect = rect;
    }
}
