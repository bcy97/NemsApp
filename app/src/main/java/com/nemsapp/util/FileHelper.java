package com.nemsapp.util;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nemsapp.vo.FileMD5;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

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

public class FileHelper {

    private OkHttpClient okHttpClient = new OkHttpClient();

    private Map<String, List<FileMD5>> configFileMD5;

    public Map<String, List<FileMD5>> initConfig() {

        File folder = new File(Constants.folderPath);

        if (!folder.exists()) {
            folder.mkdirs();
        }

        final File configList = new File(Constants.folderPath + "/baseConfigs/configList.xml");

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
                            downloadFile("configList.xml", Constants.folderPath + "/baseConfigs", "baseConfigs");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();

            //不存在configList
        } else {
            downloadFile("configList.xml", Constants.folderPath + "/baseConfigs", "baseConfigs");

        }
        return configFileMD5;
    }

    public void initPictureLib() {

        File publiclib = new File(Constants.folderPath + "/iconlibrary/public");
        File userlib = new File(Constants.folderPath + "/iconlibrary/user");

        if (!publiclib.exists()) {
            publiclib.mkdirs();
        }
        if (!userlib.exists()) {
            userlib.mkdirs();
        }

        final List<String> publicList = Arrays.asList(publiclib.list());
        final List<String> userList = Arrays.asList(userlib.list());

        //获取服务器的图片库列表
        String url = "http://" + Constants.ip + ":8080/file/getIconLibrary";
        Request request = new Request.Builder()
                .url(url)
                .build();

        //同步请求
        final Call call = okHttpClient.newCall(request);

        //发起请求
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final Response response = call.execute();
                    final String strdata = response.body().string();
                    Gson gson = new Gson();
                    Map<String, List<String>> iconLib = gson.fromJson(strdata, new TypeToken<Map<String, List<String>>>() {
                    }.getType());
                    Map<String, Integer> pubDiff = getDiffrent(publicList, iconLib.get("public"));
                    Map<String, Integer> userDiff = getDiffrent(userList, iconLib.get("user"));

                    for (String name : pubDiff.keySet()) {
                        if (pubDiff.get(name) == 0) {
                            File file = new File(Constants.folderPath + "/iconlibrary/public/" + name);
                            file.delete();
                        } else {
                            downloadFile(name, Constants.folderPath + "/iconlibrary/public", "iconlibrary/public");
                        }
                    }
                    for (String name : userDiff.keySet()) {
                        if (userDiff.get(name) == 0) {
                            File file = new File(Constants.folderPath + "/iconlibrary/user/" + name);
                            file.delete();
                        } else {
                            downloadFile(name, Constants.folderPath + "/iconlibrary/user", "iconlibrary/user");
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();


    }

    public boolean downloadFile(final String filename, final String path, final String kind) {

        //判断路径是否存在
        File configFolder = new File(path);
        if (!configFolder.exists()) {
            configFolder.mkdirs();
        }

        //下载路径
        final String url = "http://" + Constants.ip + ":8080/" + kind + "/" + filename;
        final long startTime = System.currentTimeMillis();
        Log.i("DOWNLOAD", "FileHelper: startTime=" + startTime);

        Request request = new Request.Builder()
                .url(url)
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // 下载失败
                e.printStackTrace();
                Log.i("DOWNLOAD", "FileHelper: download failed");
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
                    Log.i("DOWNLOAD", "FileHelper: download success");
                    if (filename.equals("configList.xml")) {
                        Message message = new Message();
                        message.what = 1;
                        handler.sendMessage(message);
                    }
                    Log.i("DOWNLOAD", "FileHelper: " + filename + " totalTime=" + (System.currentTimeMillis() - startTime));
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.i("DOWNLOAD", "FileHelper: download failed");
                } finally {
                    if (bufferedSink != null) {
                        bufferedSink.close();
                    }

                }
            }
        });
        return true;
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:

                    //获取md5列表
                    configFileMD5 = getConfigFileMD5();

                    //获取config文件的种类
                    Set<String> configFileType = configFileMD5.keySet();

                    //按种类遍历
                    for (String type : configFileType) {
                        String path = Constants.folderPath + "/" + type;
                        List<FileMD5> md5List = configFileMD5.get(type);
                        File typeFolder = new File(path);
                        //删除除了configList以外到所有文件
                        if (typeFolder.exists() && typeFolder.isDirectory()) {
                            for (File file : typeFolder.listFiles(new FileFilter() {
                                @Override
                                public boolean accept(File pathname) {
                                    if (pathname.getName().equals("configList.xml")) {
                                        return false;
                                    } else {
                                        return true;
                                    }
                                }
                            })) {
                                file.delete();
                            }
                        }

                        //全部下载
                        for (FileMD5 f : md5List) {
                            downloadFile(f.getFileName(), path, type);
                        }
                    }
                    break;
            }
            super.handleMessage(msg);
        }
    };

    public static Map<String, List<FileMD5>> getConfigFileMD5() {

        Map<String, List<FileMD5>> md5Map = new HashMap<>();

        File configList = new File(Constants.folderPath + "/baseConfigs/configList.xml");

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
            Document document = builder.parse(configList);
            NodeList typeList = document.getElementsByTagName("configType");
            for (int i = 0; i < typeList.getLength(); i++) {
                Element element = (Element) typeList.item(i);
                String path = element.getAttribute("path");
                String name = element.getAttribute("name");
                NodeList fileList = element.getElementsByTagName("configFile");
                List<FileMD5> fileMD5List = new ArrayList<>();
                for (int j = 0; j < fileList.getLength(); j++) {
                    FileMD5 md5 = new FileMD5();
                    md5.setFileName(((Element) fileList.item(j)).getAttribute("name"));
                    md5.setMd5(((Element) fileList.item(j)).getAttribute("md5"));
                    fileMD5List.add(md5);
                }
                md5Map.put(path, fileMD5List);
            }
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return md5Map;

    }

    /**
     * 获取两个List的不同元素
     *
     * @param list1
     * @param list2
     * @return
     */
    private static Map<String, Integer> getDiffrent(List<String> list1, List<String> list2) {
        Map<String, Integer> map = new HashMap<>(list1.size() + list2.size());
        Map<String, Integer> diff = new HashMap<>();
        for (String string : list1) {
            map.put(string, 0);
        }
        for (String string : list2) {
            Integer cc = map.get(string);
            if (cc != null) {
                map.put(string, ++cc);
                continue;
            }
            map.put(string, 2);
        }
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            if (entry.getValue() == 0 || entry.getValue() == 2) {
                diff.put(entry.getKey(), entry.getValue());
            }
        }
        return diff;
    }
}
