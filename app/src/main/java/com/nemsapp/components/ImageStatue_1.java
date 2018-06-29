package com.nemsapp.components;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public class ImageStatue_1 {
    private Paint paint;

    private String name;
    private Rect rect;
    private boolean on;
    private Bitmap open;
    private Bitmap close;

    public ImageStatue_1() {
        paint = new Paint();
        on = false;
    }

    public void draw(Canvas canvas) {
        if (on) {
            canvas.drawBitmap(open, null, rect, paint);
        } else {
            canvas.drawBitmap(close, null, rect, paint);
        }
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRect(Rect rect) {
        this.rect = rect;
    }

    public void setOn() {
        this.on = on;
    }

    private void setOff() {
        this.on = false;
    }

    public void setOpen(Bitmap open) {
        this.open = open;
    }

    public void setClose(Bitmap close) {
        this.close = close;
    }
}
