package com.nemsapp.components.image;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.nemsapp.components.Component;

/**
 * 对应xml文件内的ImageNavi，iconType为1，图源为图片。有操作属性
 */
public class ImageNavi extends Component {

    private String name;
    private Bitmap bitmap;

    public ImageNavi() {
        paint = new Paint();
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(bitmap, null, rect, paint);
    }
}
