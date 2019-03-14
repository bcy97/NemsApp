package com.nemsapp.components.image;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.nemsapp.components.Component;

public class Image extends Component {

    public String name;

    private Bitmap bitmap;


    public Image() {
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
