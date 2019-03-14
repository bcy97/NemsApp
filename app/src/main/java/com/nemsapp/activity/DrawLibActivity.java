package com.nemsapp.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import com.nemsapp.R;
import com.nemsapp.components.image.ImageStatue;
import com.nemsapp.ui.MainUI;
import com.nemsapp.util.DrawParser;
import com.nemsapp.util.FileHelper;

import java.util.HashMap;
import java.util.Map;

public class DrawLibActivity extends AppCompatActivity {

    //主pic图
    private MainUI mainUI;

    private Map<String, ImageStatue> imageStatues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitor_pic);

        //初始化MainUI
        mainUI = findViewById(R.id.mainUI);

        imageStatues = new HashMap<>();

        //隐藏顶部标题栏
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        FileHelper helper = new FileHelper();
        helper.initConfig();
        helper.initPictureLib();
        initXml();

    }

    private void initXml() {

        mainUI.setFilename("drawlib.xml");
        //初始化pic图
        imageStatues = DrawParser.getInstance(mainUI).initXml(mainUI);
    }

}
