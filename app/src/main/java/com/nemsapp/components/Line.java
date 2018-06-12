package com.nemsapp.components;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;

import com.nemsapp.R;

/**
 * Created by duoduogao on 2018/3/11.
 */

public class Line {

    private Context context;
    private Path path;
    private Paint paint;

    public Line(Context context, float strokeWidth) {
        this.context = context;
        paint = new Paint();
        paint.setStrokeWidth(strokeWidth);
        paint.setAntiAlias(true);
//        this.color = color;
    }

    public Path getPath() {
        return path;
    }

    public void setPath(Path path) {
        this.path = path;
    }

    public void draw(Canvas canvas) {
        paint.setColor(this.context.getResources().getColor(R.color.coler0));
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        canvas.drawPath(path, paint);
    }


}
