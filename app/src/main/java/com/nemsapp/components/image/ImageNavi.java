package com.nemsapp.components.image;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import com.nemsapp.components.Component;

public class ImageNavi extends Component {

    private String name;
    private Rect rect;
    private Bitmap bitmap;

    public ImageNavi() {
        paint = new Paint();
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(bitmap, null, rect, paint);
    }
}
