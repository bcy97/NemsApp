package com.nemsapp.ui;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceView;

import com.nemsapp.components.Component;
import com.nemsapp.components.DyanData;
import com.nemsapp.components.image.ImageStatue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by duoduogao on 2018/3/11.
 */

public class MainUI extends SurfaceView {

    private Map<String, ImageStatue> statueMap;
    private Map<String, DyanData> dyanDatas;
    private List<Component> components;


    //记录是拖拉界面模式还是放大缩小界面模式
    private int mode = 0;// 初始状态
    //拖拉界面模式
    private static final int MODE_DRAG = 1;
    //放大缩小界面模式
    private static final int MODE_ZOOM = 2;

    //用于记录触摸事件开始时候的坐标位置
    private PointF startPoint = new PointF();
    //两个手指的开始距离
    private float startDis;

    //当前的位置
    private float nowX = 0f;
    private float nowY = 0f;

    //当前缩放倍数
    private float scaleTime;
    //最小缩放倍数
    private float minScale;
    //初始屏幕xy轴缩放比例
    private float timeX;
    private float timeY;
    //触控中间点位置
    PointF mid;

    //pic图像尺寸
    private int picWidth;
    private int picHeight;
    //应用范围尺寸
    private int appWidth;
    private int appHeight;

    public MainUI(Context context) {
        super(context);
    }

    public MainUI(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void clean() {
        statueMap = new HashMap<>();
        dyanDatas = new HashMap<>();
        components = new ArrayList<>();

        mode = 0;

    }

    @Override
    protected void onDraw(Canvas canvas) {
        //如果是初始状态，初始化缩放尺寸
        if (mode == 0) {
            //获取显示区域大小
            Rect appRegion = new Rect();
            ((Activity) getContext()).getWindow().getDecorView().getWindowVisibleDisplayFrame(appRegion);

            appWidth = appRegion.width();
            appHeight = appRegion.height();

            timeX = (float) appWidth / (float) picWidth;
            timeY = (float) appHeight / (float) picHeight;

            //设置缩放倍数为x和y缩放小者
            scaleTime = timeX > timeY ? timeY : timeX;

            //设置图像居中
            nowX = (appWidth - picWidth * scaleTime) / 2;
            nowY = (appHeight - picHeight * scaleTime) / 2;

            System.out.println(nowX + "," + nowY);
            minScale = scaleTime;

        }

        //平移到nowX，nowY位置
        canvas.translate(nowX, nowY);
        //缩放scaleTime倍
        canvas.scale(scaleTime, scaleTime);

        if (null != dyanDatas && dyanDatas.size() > 0) {
            for (String name : dyanDatas.keySet()) {
                dyanDatas.get(name).draw(canvas);
            }
        }

        if (null != components && components.size() > 0) {
            for (Component component : components) {
                component.draw(canvas);
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
                    float left = nowX + event.getX() - startPoint.x;
                    float top = nowY + event.getY() - startPoint.y;
                    float right = left + picWidth * scaleTime;
                    float bottom = top + picHeight * scaleTime;

                    if (picWidth * scaleTime > appWidth && left < 0 && right > appWidth) {
                        nowX = left;
                    }

                    if (picHeight * scaleTime > appHeight && top < 0 && bottom > appHeight) {
                        nowY = top;
                    }

                    startPoint.x = event.getX();
                    startPoint.y = event.getY();
                    invalidate();
                }
                // 放大缩小
                else if (mode == MODE_ZOOM) {
                    float endDis = distance(event);// 结束距离
                    if (endDis > 10f) { // 两个手指并拢在一起的时候像素大于10
                        scaleTime = scaleTime * (endDis / startDis);// 得到缩放倍数
                        //如果缩放倍数小于最小缩放倍数，设置为最小倍数
                        if (scaleTime < minScale) {
                            scaleTime = minScale;
                        }

                        adapt();
                        mid = mid(event);
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

    /**
     * 调整边界
     */
    private void adapt() {

        nowX = (appWidth - picWidth * scaleTime) / 2;
        nowY = (appHeight - picHeight * scaleTime) / 2;

    }

    public Map<String, DyanData> getDyanDatas() {
        return dyanDatas;
    }

    public void setDyanDatas(Map<String, DyanData> dyanDatas) {
        this.dyanDatas = dyanDatas;
    }

    public int getPicWidth() {
        return picWidth;
    }

    public void setPicWidth(int picWidth) {
        this.picWidth = picWidth;
    }

    public int getPicHeight() {
        return picHeight;
    }

    public void setPicHeight(int picHeight) {
        this.picHeight = picHeight;
    }


    public Map<String, ImageStatue> getStatueMap() {
        return statueMap;
    }

    public void setStatueMap(Map<String, ImageStatue> statueMap) {
        this.statueMap = statueMap;
    }

    public List<Component> getComponents() {
        if (components == null) {
            components = new ArrayList<>();
        }
        return components;
    }

    public void setComponents(List<Component> components) {
        this.components = components;
    }
}

