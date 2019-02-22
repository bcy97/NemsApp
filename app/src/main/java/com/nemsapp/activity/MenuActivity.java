package com.nemsapp.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.SimpleAdapter;

import com.nemsapp.R;
import com.nemsapp.util.FileHelper;
import com.nemsapp.util.GlideImageLoader;
import com.nemsapp.vo.FileMD5;
import com.youth.banner.Banner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MenuActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    //滚动栏
    Banner banner;
    //6格导航栏
    GridView menu;
    private List<Map<String, Object>> dataList;
    private int[] icon = {R.drawable.ic_monitor, R.drawable.ic_graph,
            R.drawable.ic_data, R.drawable.ic_statistics,
            R.drawable.ic_alert, R.drawable.ic_search};
    private String[] iconName = {"监控图", "曲线图", "实时数据", "累加量统计", "重要警报", "查询统计"};
    private SimpleAdapter adapter;

    private FileHelper fileHelper = new FileHelper();

    Map<String, List<FileMD5>> configFileMD5;

    //读写权限
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    //请求状态码
    private static int REQUEST_PERMISSION_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, REQUEST_PERMISSION_CODE);
            }
        }

        //初始化滚动栏
        banner = findViewById(R.id.home_banner);
        //资源文件
        Integer[] images = {R.drawable.p1, R.drawable.p2, R.drawable.p3, R.drawable.p4};
        //设置图片加载器
        banner.setImageLoader(new GlideImageLoader());
        //设置图片集合
        banner.setImages(Arrays.asList(images));
        //banner设置方法全部调用完毕时最后调用
        banner.start();

        //初始化导航栏
        menu = findViewById(R.id.menu);
        dataList = new ArrayList<>();
        adapter = new SimpleAdapter(this, gatData(), R.layout.item_menu,
                new String[]{"pic", "text"}, new int[]{R.id.pic, R.id.text});
        menu.setAdapter(adapter);

        menu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent;
                switch (position) {
                    case 0:
                        intent = new Intent(MenuActivity.this, MonitorPicActivity.class);
                        startActivity(intent);
                        break;
                    case 1:
                        System.out.println("点击");
                        intent = new Intent(MenuActivity.this, GraphActivity.class);
                        startActivity(intent);
                        break;
                    case 2:
                        intent = new Intent(MenuActivity.this, RealDataActivity.class);
                        startActivity(intent);
                        break;
                    case 3:
                        intent = new Intent(MenuActivity.this, CumulantDataActivity.class);
                        startActivity(intent);
                        break;
                    case 4:
                        intent = new Intent(MenuActivity.this, AlertActivity.class);
                        startActivity(intent);
                        break;
                    case 5:
                        intent = new Intent(MenuActivity.this, StatisActivity.class);
                        startActivity(intent);
                        break;
                }
            }
        });

        configFileMD5 = fileHelper.initConfig();

        fileHelper.initPictureLib();

    }

    private List<Map<String, Object>> gatData() {
        // TODO Auto-generated method stub
        for (int i = 0; i < icon.length; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("pic", icon[i]);
            map.put("text", iconName[i]);
            dataList.add(map);
        }
        return dataList;
    }

    @Override
    public void onBackPressed() {
        //回退到桌面
        Intent intent = new Intent();
        intent.setAction("android.intent.action.MAIN");
        intent.addCategory("android.intent.category.HOME");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addCategory("android.intent.category.MONKEY");
        startActivity(intent);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_CODE) {
            for (int i = 0; i < permissions.length; i++) {
                Log.i("MainActivity", "申请的权限为：" + permissions[i] + ",申请结果：" + grantResults[i]);
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
