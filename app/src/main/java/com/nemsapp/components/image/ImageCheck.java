package com.nemsapp.components.image;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.nemsapp.components.Component;

public class ImageCheck extends Component {

    //点名，判断是否为开一组
    private String name;

    //组名
    private String groupName;

    private boolean on;
    private Bitmap open;
    private Bitmap close;

    public ImageCheck() {
        paint = new Paint();
    }

    public void draw(Canvas canvas) {
        if (on) {
            canvas.drawBitmap(open, null, rect, paint);
        } else {
            canvas.drawBitmap(close, null, rect, paint);
        }
    }

    public void setOn(boolean on) {
        this.on = on;
    }

    public boolean isOn() {
        return on;
    }

    public void setOpen(Bitmap open) {
        this.open = open;
    }

    public void setClose(Bitmap close) {
        this.close = close;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
}