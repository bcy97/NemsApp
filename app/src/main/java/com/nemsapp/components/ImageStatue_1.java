package com.nemsapp.components;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * 对应xml文件内的ImageStatue，iconType为1，即为bmp的图标
 */
public class ImageStatue_1 extends ImageStatue {

    private Rect rect;  //图标范围
    private boolean on;  //开关状态
    private Bitmap open;  //开状态bmp
    private Bitmap close; //关状态bmp

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

    public String getName() {
        return name;
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
