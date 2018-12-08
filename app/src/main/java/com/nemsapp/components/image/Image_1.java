package com.nemsapp.components.image;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import com.nemsapp.components.image.Image;

/**
 * 对应xml文件内的Image，iconType为1，即为png的图标
 */
public class Image_1 extends Image {

    private Bitmap bitmap;


    public Image_1() {
        paint = new Paint();
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawBitmap(bitmap, null, rect, paint);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
