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
import android.view.View;
import android.widget.Button;

import com.nemsapp.R;
import com.nemsapp.util.Constants;
import com.nemsapp.util.FileHelper;
import com.nemsapp.util.MD5;
import com.nemsapp.vo.FileMD5;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.BufferedSink;
import okio.Okio;
import okio.Sink;

public class MenuActivity extends AppCompatActivity {

    final OkHttpClient client = new OkHttpClient();

    Map<String, List<FileMD5>> configFileMD5;

    OkHttpClient okHttpClient = new OkHttpClient();

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

        Button button1 = findViewById(R.id.button1);
        Button button2 = findViewById(R.id.button2);
        Button button3 = findViewById(R.id.button3);
        Button button4 = findViewById(R.id.button4);
        Button button5 = findViewById(R.id.button5);
        Button button6 = findViewById(R.id.button6);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, MonitorPicActivity.class);
                startActivity(intent);
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, GraphActivity.class);
                startActivity(intent);
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, RealDataActivity.class);
                startActivity(intent);
            }
        });

        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, CumulantDataActivity.class);
                startActivity(intent);
            }
        });


        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, AlertActivity.class);
                startActivity(intent);
            }
        });

        button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, StatisActivity.class);
                startActivity(intent);
            }
        });

        initConfig();
    }

    @Override
    public void onBackPressed() {
        //回退到桌面
   /* <action android:name="android.intent.action.MAIN" />
    <category android:name="android.intent.category.HOME" />
    <category android:name="android.intent.category.DEFAULT" />
    <category android:name="android.intent.category.MONKEY"/>*/

        Intent intent = new Intent();
        intent.setAction("android.intent.action.MAIN");
        intent.addCategory("android.intent.category.HOME");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addCategory("android.intent.category.MONKEY");
        startActivity(intent);
    }

    public void initConfig() {

        File folder = new File(Constants.folderPath);
        System.out.println(Constants.folderPath);

        if (!folder.exists()) {
            folder.mkdirs();
        }

        final File configList = new File(Constants.folderPath + "/configList.xml");

        //configList存在 一致
        if (configList.exists()) {
            //获取当前configList的MD5
            String data = MD5.getMD5(configList);

            //比较configList的MD5
            String url = "http://" + Constants.ip + ":8080/file/compare";

            MediaType mediaType = MediaType.parse("application/json; charset=utf-8");

            final Request request = new Request.Builder()
                    .url(url)
                    .post(RequestBody.create(mediaType, data))
                    .build();
            final Call call = okHttpClient.newCall(request);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Response response = call.execute();
                        boolean result = Boolean.parseBoolean(response.body().string());
                        //configList的MD5不一致
                        if (!result) {
                            //请求新的configList
                            downloadFile("configList.xml", Constants.folderPath);
                            //获取新的md5列表
                            configFileMD5 = FileHelper.getConfigFileMD5();
                            //获取config文件的种类
                            Set<String> configFileType = configFileMD5.keySet();
                            //按种类遍历
                            for (String type : configFileType) {
                                String path = Constants.folderPath + "/" + type;
                                List<FileMD5> md5List = configFileMD5.get(type);
                                //遍历当前种类的文件的md5
                                for (FileMD5 f : md5List) {
                                    File file = new File(path + "/" + f.getFileName());
                                    if (!f.getMd5().equals(MD5.getMD5(file))) {
                                        downloadFile(f.getFileName(), path);
                                    }
                                }
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();

            //不存在configList
        } else {
            downloadFile("configList.xml", Constants.folderPath);
            //获取md5列表
            configFileMD5 = FileHelper.getConfigFileMD5();
            //获取config文件的种类
            Set<String> configFileType = configFileMD5.keySet();
            //按种类遍历
            for (String type : configFileType) {
                String path = Constants.folderPath + "/" + type;
                File configFolder = new File(path);
                if (!configFolder.exists()) {
                    configFolder.mkdirs();
                }
                List<FileMD5> md5List = configFileMD5.get(type);
                //全部下载
                for (FileMD5 f : md5List) {
                    downloadFile(f.getFileName(), path);
                }
            }
        }

    }

    private void downloadFile(final String filename, final String path) {
        //下载路径，如果路径无效了，可换成你的下载路径
        final String url = "http://" + Constants.ip + ":8080/" + filename;
        final long startTime = System.currentTimeMillis();
        Log.i("DOWNLOAD", "startTime=" + startTime);

        Request request = new Request.Builder()
                .url(url)
                .build();

        final Call call = okHttpClient.newCall(request);

        new OkHttpClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // 下载失败
                e.printStackTrace();
                Log.i("DOWNLOAD", "download failed");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Sink sink = null;
                BufferedSink bufferedSink = null;
                try {
                    File dest = new File(path, url.substring(url.lastIndexOf("/") + 1));
                    sink = Okio.sink(dest);
                    bufferedSink = Okio.buffer(sink);
                    bufferedSink.writeAll(response.body().source());

                    bufferedSink.close();
                    Log.i("DOWNLOAD", "download success");
                    Log.i("DOWNLOAD", "totalTime=" + (System.currentTimeMillis() - startTime));
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.i("DOWNLOAD", "download failed");
                } finally {
                    if (bufferedSink != null) {
                        bufferedSink.close();
                    }

                }
            }
        });

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
}
