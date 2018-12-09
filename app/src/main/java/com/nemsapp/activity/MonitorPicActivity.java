package com.nemsapp.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nemsapp.R;
import com.nemsapp.components.Component;
import com.nemsapp.components.DyanData;
import com.nemsapp.components.image.ImageNavi;
import com.nemsapp.components.image.ImageStatue;
import com.nemsapp.ui.MainUI;
import com.nemsapp.util.Constants;
import com.nemsapp.util.PicParser;
import com.nemsapp.vo.AnValue;
import com.nemsapp.vo.StValue;
import com.okhttplib.HttpInfo;
import com.okhttplib.OkHttpUtil;
import com.okhttplib.annotation.ContentType;
import com.okhttplib.annotation.Encoding;
import com.okhttplib.callback.Callback;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;


public class MonitorPicActivity extends AppCompatActivity {

    //主pic图
    private MainUI mainUI;

    //pic列表
    private ListView sidebar;
    List<String> picList;

    private Map<String, ImageStatue> imageStatues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitor_pic);

        //初始化MainUI
        mainUI = findViewById(R.id.mainUI);

        //初始化侧边栏
        sidebar = findViewById(R.id.pic_sidebar);

        imageStatues = new HashMap<>();

        //隐藏顶部标题栏
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        //初始化侧边导航栏
        initSideBar();


        //初始化默认pic图（默认第一张）
        if (picList != null && picList.size() > 0) {
            initXml(picList.get(0));
        }

        //初始化刷新数据
        update();

        //添加定时任务，定时刷新pic图中数据
        timer.schedule(task, 0, 5000);

    }

    private void initSideBar() {
        picList = PicParser.getInstance().getNaviList();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, picList);
        sidebar.setAdapter(adapter);
        sidebar.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                initXml(picList.get(i));
                update();
            }
        });

    }

    private void initXml(String filename) {

        mainUI.setFilename(filename);
        //初始化pic图
        imageStatues = PicParser.getInstance().initXml(mainUI);

//        mainUI.setOnClickListener(new MainUI.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                System.out.println(v.getX() + "," + v.getY());
//                for (Component c : mainUI.getClickableComponents()) {
//
//                    //如果没有点击该按钮，继续循环
//                    if (!c.rect.contains(v.getX(), v.getY())) {
//                        continue;
//                    }
//
//                    //如果是navi按钮
//                    if (c instanceof ImageNavi) {
//                        String newFilename = PicParser.getInstance().getNaviList().get(((ImageNavi) c).getNo());
//                        if (!mainUI.getFilename().equals(newFilename)) {
//                            mainUI.setFilename(newFilename);
//                            mainUI.invalidate();
//                        }
//                        break;
//                    }
//                }
//            }
//        });

    }

    final Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    update();
                    break;
            }
            super.handleMessage(msg);
        }

    };

    void update() {
//        getAnData(mainUI.getDyanDatas());
//        getStData(imageStatues);
        //刷新msg的内容
        mainUI.invalidate();
    }

    Timer timer = new Timer();
    TimerTask task = new TimerTask() {
        public void run() {
            Message message = new Message();
            message.what = 1;
            handler.sendMessage(message);
        }
    };

    /**
     * 异步请求：请求监控图中的AnData,回调方法可以直接操作UI
     */
    private void getAnData(final Map<String, DyanData> andatas) {

        String data = "[";
        for (String name : andatas.keySet()) {
            data += "\"" + name + "\",";
        }

        data = data.substring(0, data.length() - 1) + "]";


        OkHttpUtil.getDefault(this).doPostAsync(
                HttpInfo.Builder()
                        .setUrl("http://" + Constants.ip + ":8080/monitor/getAnData")
                        .setContentType(ContentType.JSON)
                        .setResponseEncoding(Encoding.UTF_8)
                        .addParamJson(data)
                        .build(),
                new Callback() {
                    @Override
                    public void onSuccess(HttpInfo info) throws IOException {
                        String response = info.getRetDetail();
                        Gson gson = new Gson();
                        Map<String, AnValue> data = gson.fromJson(response, new TypeToken<HashMap<String, AnValue>>() {
                        }.getType());
                        for (String name : data.keySet()) {
                            andatas.get(name).setText(new DecimalFormat("0.00").format(data.get(name).getValue()));
                            andatas.get(name).setValid(data.get(name).getValid());
                        }
                    }

                    @Override
                    public void onFailure(HttpInfo info) throws IOException {
                        System.out.println("网络连接不可用");
                    }
                }
        );
    }

    /**
     * 异步请求：请求监控图中的StData,回调方法可以直接操作UI
     */
    private void getStData(final Map<String, ImageStatue> imageStatues) {

        String data = "[";
        for (String name : imageStatues.keySet()) {
            data += "\"" + name + "\",";
        }

        data = data.substring(0, data.length() - 1) + "]";


        OkHttpUtil.getDefault(this).doPostAsync(
                HttpInfo.Builder()
                        .setUrl("http://" + Constants.ip + ":8080/monitor/getStData")
                        .setContentType(ContentType.JSON)
                        .setResponseEncoding(Encoding.UTF_8)
                        .addParamJson(data)
                        .build(),
                new Callback() {
                    @Override
                    public void onSuccess(HttpInfo info) {
                        String response = info.getRetDetail();
                        Gson gson = new Gson();
                        Map<String, StValue> data = gson.fromJson(response, new TypeToken<HashMap<String, StValue>>() {
                        }.getType());
                        for (String name : data.keySet()) {
                            if (data.get(name).getValid() == 1) {
                                if (data.get(name).getValue() == 1) {
                                    imageStatues.get(name).setOn();
                                } else {
                                    imageStatues.get(name).setOff();
                                }
//                                andatas.get(name).setText(new DecimalFormat("0.00").format(data.get(name).getValue()));
                            }
                        }
                    }

                    @Override
                    public void onFailure(HttpInfo info) {
                        System.out.println("网络连接不可用");
                    }
                }
        );
    }

    @Override
    protected void onDestroy() {
        if (timer != null) {// 停止timer
            timer.cancel();
            timer = null;
        }
        super.onDestroy();
    }
}
