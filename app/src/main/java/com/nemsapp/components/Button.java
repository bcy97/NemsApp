package com.nemsapp.components;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Region;

import com.nemsapp.R;

/**
 * Created by duoduogao on 2018/3/11.
 */

public class Button {

    private Context context;
    private Path path;
    private Paint paint;
    private boolean isOn;
    private String name;
    private String id;

    public boolean isOn() {
        return isOn;
    }

    public void setOn(boolean on) {
        isOn = on;
    }

    private Region region;

    public Button(Context context, float strokeWidth) {
        this.context = context;
        paint = new Paint();
        paint.setStrokeWidth(strokeWidth);
        paint.setAntiAlias(true);
    }

    public Path getPath() {
        return path;
    }

    public void setPath(Path path) {
        this.path = path;
    }

    public void draw(Canvas canvas) {
        if (isOn()) {
            int color = getRanColor();
            paint.setColor(this.context.getResources().getColor(R.color.coler0));
            paint.setStyle(Paint.Style.FILL_AND_STROKE);
//            paint.setShadowLayer(8, 3, 3, Color.DKGRAY);

        } else {
            paint.setColor(this.context.getResources().getColor(R.color.color1));
            paint.setStyle(Paint.Style.STROKE);
        }
        canvas.drawPath(path, paint);
    }

    private int getRanColor() {
        int[] colors = {this.context.getResources().getColor(R.color.coler0),
                this.context.getResources().getColor(R.color.color1)};
        return colors[(int) (Math.random() * 2)];
    }

    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
