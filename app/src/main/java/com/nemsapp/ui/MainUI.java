package com.nemsapp.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Region;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.nemsapp.R;
import com.nemsapp.components.Button;
import com.nemsapp.components.Line;
import com.nemsapp.components.Switch;

import java.util.List;

/**
 * Created by duoduogao on 2018/3/11.
 */

public class MainUI extends View {

    private List<Button> buttons;
    private List<Line> lines;
    private List<Switch> switches;

    private Paint paint;

    private float scaleTime = 1f;

    /**
     * 记录是拖拉界面模式还是放大缩小界面模式
     */
    private int mode = 0;// 初始状态
    /**
     * 拖拉界面模式
     */
    private static final int MODE_DRAG = 1;
    /**
     * 放大缩小界面模式
     */
    private static final int MODE_ZOOM = 2;

    /**
     * 用于记录开始时候的坐标位置
     */
    private PointF startPoint = new PointF();
    /**
     * 两个手指的开始距离
     */
    private float startDis;

    private float nowX = 0f;
    private float nowY = 0f;

    public MainUI(Context context) {
        super(context);
    }

    public MainUI(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (nowX < canvas.getWidth() && nowY < canvas.getHeight()) {
            canvas.translate(nowX, nowY);
        } else {
            nowX = 0;
            nowY = 0;
        }
        if (scaleTime <= 0) {
            scaleTime = 1f;
        }
        canvas.scale(scaleTime, scaleTime);
        if (null != buttons && buttons.size() > 0) {
            for (Button button : buttons) {
                button.draw(canvas);
            }
        }
        if (null != lines && lines.size() > 0) {
            for (Line line : lines) {
                line.draw(canvas);
            }
        }
        if (null != switches && switches.size() > 0) {
            for (Switch swtch : switches) {
                swtch.draw(canvas);
            }
        }
        paint = new Paint();
        //设置抗锯齿
        paint.setAntiAlias(true);
        //设置画笔颜色
        paint.setColor(getResources().getColor(R.color.colorPrimary));
        //设置画笔大小
        paint.setStrokeWidth(3);
        //设置字体大小
        paint.setTextSize(30);
        //设置文字样式
        paint.setTypeface(Typeface.DEFAULT_BOLD);
        String str = "电压";
        //全部显示，起点在(50,100)点
        canvas.drawText(str, 50, 100, paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
//        long secClick = 0;
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                float x = event.getX();
                float y = event.getY();
                String kind = "";
                for (Button button : buttons) {
                    Region region = button.getRegion();
                    boolean isContain = region.contains((int) ((x - nowX) / scaleTime), (int) ((y - nowY) / scaleTime));
                    if (isContain) {
                        button.setOn(button.isOn() ? false : true);
                        kind = "button";
                        break;
                    }
                }
                for (Switch swtch : switches) {
                    Region region = swtch.getRegion();
                    boolean isContain = region.contains((int) ((x - nowX) / scaleTime), (int) ((y - nowY) / scaleTime));
                    if (isContain) {
                        swtch.setOn(swtch.isOn() ? false : true);
                        kind = "switch";
                        break;
                    }
                }
                if (kind.equals("")) {
                    mode = MODE_DRAG;
                    startPoint.set(x, y);
                }
                invalidate();
                break;
            // 手指在屏幕上移动，改事件会被不断触发
            case MotionEvent.ACTION_MOVE:
                // 拖拉图片
                if (mode == MODE_DRAG) {
                    nowX += event.getX() - startPoint.x; // 得到x轴的移动距离
                    nowY += event.getY() - startPoint.y; // 得到y轴的移动距离
                    startPoint.x = event.getX();
                    startPoint.y = event.getY();
                    invalidate();
                }
                // 放大缩小
                else if (mode == MODE_ZOOM) {
                    float endDis = distance(event);// 结束距离
                    if (endDis > 10f) { // 两个手指并拢在一起的时候像素大于10
                        scaleTime = scaleTime * (endDis / startDis);// 得到缩放倍数
                    }
                    startDis = endDis;
                    invalidate();
                }
                break;
            // 手指离开屏幕
            case MotionEvent.ACTION_UP:
                break;
            // 当触点离开屏幕，但是屏幕上还有触点(手指)
            case MotionEvent.ACTION_POINTER_UP:
                mode = 0;
                break;
            // 当屏幕上已经有触点(手指)，再有一个触点压下屏幕
            case MotionEvent.ACTION_POINTER_DOWN:
                mode = MODE_ZOOM;
                /** 计算两个手指间的距离 */
                startDis = distance(event);
                break;
        }
        return true;
    }


    /**
     * 计算两个手指间的距离
     */
    private float distance(MotionEvent event) {
        float dx = event.getX(1) - event.getX(0);
        float dy = event.getY(1) - event.getY(0);
        /** 使用勾股定理返回两点之间的距离 */
        return (float) Math.sqrt(dx * dx + dy * dy);
    }

    /**
     * 计算两个手指间的中间点
     */
    private PointF mid(MotionEvent event) {
        float midX = (event.getX(1) + event.getX(0)) / 2;
        float midY = (event.getY(1) + event.getY(0)) / 2;
        return new PointF(midX, midY);
    }

    public List<Button> getButtons() {
        return buttons;
    }

    public void setButtons(List<Button> buttons) {
        this.buttons = buttons;
    }

    public List<Line> getLines() {
        return lines;
    }

    public void setLines(List<Line> lines) {
        this.lines = lines;
    }

    public List<Switch> getSwitches() {
        return switches;
    }

    public void setSwitches(List<Switch> switches) {
        this.switches = switches;
    }
}

