package com.nemsapp.components.image;

import android.graphics.Canvas;

import com.nemsapp.components.Component;

public class ImageStatue extends Component {

    public String name;  //状态量名称

    public boolean on;  //开关状态

    public void setOn() {
        this.on = true;
    }

    public void setOff() {
        this.on = false;
    }

    @Override
    public void draw(Canvas canvas) {
    }

}
