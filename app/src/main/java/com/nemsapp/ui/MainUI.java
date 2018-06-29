package com.nemsapp.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceView;

import com.nemsapp.components.CommandButton;
import com.nemsapp.components.DyanData;
import com.nemsapp.components.ImageStatue_0;
import com.nemsapp.components.ImageStatue_1;
import com.nemsapp.components.Image_0;
import com.nemsapp.components.Image_1;
import com.nemsapp.components.Line;
import com.nemsapp.components.Text;

import java.util.List;

/**
 * Created by duoduogao on 2018/3/11.
 */

public class MainUI extends SurfaceView {

    private List<Line> lines;
    private List<Text> texts;
    private List<Image_0> image0s;
    private List<Image_1> image1s;
    private List<ImageStatue_0> imageStatue0s;
    private List<ImageStatue_1> imageStatue1s;
    private List<DyanData> dyanDatas;
    private List<CommandButton> commandButtons;


    private float scaleTime = 0.7f;

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
        if (null != lines && lines.size() > 0) {
            for (Line line : lines) {
                line.draw(canvas);
            }
        }
        if (null != texts && texts.size() > 0) {
            for (Text text : texts) {
                text.draw(canvas);
            }
        }
        if (null != image0s && image0s.size() > 0) {
            for (Image_0 image0 : image0s) {
                image0.draw(canvas);
            }
        }
        if (null != image1s && image1s.size() > 0) {
            for (Image_1 image1 : image1s) {
                image1.draw(canvas);
            }
        }
        if (null != imageStatue0s && imageStatue0s.size() > 0) {
            for (ImageStatue_0 imageStatue0 : imageStatue0s) {
                imageStatue0.draw(canvas);
            }
        }
        if (null != imageStatue1s && imageStatue1s.size() > 0) {
            for (ImageStatue_1 imageStatue1 : imageStatue1s) {
                imageStatue1.draw(canvas);
            }
        }
        if (null != dyanDatas && dyanDatas.size() > 0) {
            for (DyanData dyanData : dyanDatas) {
                dyanData.draw(canvas);
            }
        }
        if (null != commandButtons && commandButtons.size() > 0) {
            for (CommandButton commandButton : commandButtons) {
                commandButton.draw(canvas);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
//        long secClick = 0;
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                float x = event.getX();
                float y = event.getY();
                String kind = "";
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

    public List<Line> getLines() {
        return lines;
    }

    public void setLines(List<Line> lines) {
        this.lines = lines;
    }

    public List<Text> getTexts() {
        return texts;
    }

    public void setTexts(List<Text> texts) {
        this.texts = texts;
    }

    public List<Image_0> getImage0s() {
        return image0s;
    }

    public void setImage0s(List<Image_0> image0s) {
        this.image0s = image0s;
    }

    public List<Image_1> getImage1s() {
        return image1s;
    }

    public void setImage1s(List<Image_1> image1s) {
        this.image1s = image1s;
    }

    public List<ImageStatue_0> getImageStatue0s() {
        return imageStatue0s;
    }

    public void setImageStatue0s(List<ImageStatue_0> imageStatue0s) {
        this.imageStatue0s = imageStatue0s;
    }

    public void setImageStatue1s(List<ImageStatue_1> imageStatue1s) {
        this.imageStatue1s = imageStatue1s;
    }

    public void setDyanDatas(List<DyanData> dyanData) {
        this.dyanDatas = dyanData;
    }

    public void setCommandButtons(List<CommandButton> commandButtons) {
        this.commandButtons = commandButtons;
    }
}

