package com.nemsapp.components.image;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.nemsapp.components.Component;

public class ImageStatue extends Component {

    public String name;  //状态量名称

    public boolean on;  //开关状态

    private Bitmap open;  //开状态bmp
    private Bitmap close; //关状态bmp

    public void setOn() {
        this.on = true;
    }

    public void setOff() {
        this.on = false;
    }

    public ImageStatue() {
        paint = new Paint();
        on = false;
    }

    @Override
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

    public void setOpen(Bitmap open) {
        this.open = open;
    }

    public void setClose(Bitmap close) {
        this.close = close;
    }

}
