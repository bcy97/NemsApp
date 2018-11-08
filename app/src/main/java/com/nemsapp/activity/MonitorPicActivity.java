package com.nemsapp.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nemsapp.R;
import com.nemsapp.components.CommandButton;
import com.nemsapp.components.DyanData;
import com.nemsapp.components.ImageStatue;
import com.nemsapp.components.ImageStatue_0;
import com.nemsapp.components.ImageStatue_1;
import com.nemsapp.components.Image_0;
import com.nemsapp.components.Image_1;
import com.nemsapp.components.Line;
import com.nemsapp.components.Text;
import com.nemsapp.ui.MainUI;
import com.nemsapp.util.Constants;
import com.nemsapp.vo.AnValue;
import com.nemsapp.vo.StValue;
import com.okhttplib.HttpInfo;
import com.okhttplib.OkHttpUtil;
import com.okhttplib.annotation.ContentType;
import com.okhttplib.annotation.Encoding;
import com.okhttplib.callback.Callback;

import org.dom4j.io.SAXReader;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;


public class MonitorPicActivity extends AppCompatActivity {

    //主pic图
    private MainUI mainUI;

    //pic列表
    private ListView sidebar;
    List<String> picList;

    private Map<String, Map<String, String>> piclib;

    private Map<String, ImageStatue> imageStatues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitor_pic);
        mainUI = findViewById(R.id.mainUI);

        sidebar = findViewById(R.id.pic_sidebar);

        imageStatues = new HashMap<>();

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        initSideBar();

        if (picList != null && picList.size() > 0) {
            initXml(picList.get(0));
        }


        //初始化刷新数据
        getAnData(mainUI.getDyanDatas());
        getStData(imageStatues);
        //刷新msg的内容
        mainUI.invalidate();


        timer.schedule(task, 0, 5000);

    }

    private void initSideBar() {
        picList = new ArrayList<>();

        //获取文件夹
        File folder = new File(Constants.folderPath + "/pictures");
        if (folder != null && !folder.exists()) return;
        if (!folder.isDirectory()) return;

        //获取文件夹下的pic列表
        picList = Arrays.asList(folder.list());

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, picList);
        sidebar.setAdapter(adapter);
        sidebar.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                initXml(picList.get(i));
                mainUI.invalidate();

            }
        });

    }

    private void initXml(String filename) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;

        try {
            builder = factory.newDocumentBuilder();

            File file = new File(Constants.folderPath + "/pictures/" + filename);
            InputStream is = new FileInputStream(file);
            Document document = builder.parse(is);
            InputStream is2 = getResources().openRawResource(R.raw.piclib);
            Document pic = builder.parse(is2);

            piclib = initPiclib16(pic);

            mainUI.setLines(parseLine(document));
            mainUI.setTexts(parseString(document));
            mainUI.setImage0s(parseImage0(document));
            mainUI.setImage1s(parseImage1(document));
            parseImageStatue(document);
//            mainUI.setImageStatue0s(parseImageStatue0(document));
//            mainUI.setImageStatue1s(parseImageStatue1(document));
            mainUI.setCommandButtons(parseCommandButton(document));
            mainUI.setDyanDatas(parseDyanData(document));


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Map<String, Map<String, String>> initPiclib16(Document document) {
        Map<String, Map<String, String>> piclib = new HashMap<>();
        NodeList lineList = document.getElementsByTagName("p16");
        Map<String, String> lib16 = new HashMap<>();
        for (int i = 0; i < lineList.getLength(); i++) {
            Element element = (Element) lineList.item(i);
            lib16.put(element.getAttribute("id"), element.getAttribute("path"));
        }
        lineList = document.getElementsByTagName("p32");
        Map<String, String> lib32 = new HashMap<>();
        for (int i = 0; i < lineList.getLength(); i++) {
            Element element = (Element) lineList.item(i);
            lib32.put(element.getAttribute("id"), element.getAttribute("path"));
        }
        lineList = document.getElementsByTagName("p48");
        Map<String, String> lib48 = new HashMap<>();
        for (int i = 0; i < lineList.getLength(); i++) {
            Element element = (Element) lineList.item(i);
            lib48.put(element.getAttribute("id"), element.getAttribute("path"));
        }
        piclib.put("16", lib16);
        piclib.put("32", lib32);
        piclib.put("48", lib48);
        return piclib;
    }

    private List<Line> parseLine(Document document) {
        NodeList lineList = document.getElementsByTagName("line");
        List<Line> lines = new ArrayList<>();
        for (int i = 0; i < lineList.getLength(); i++) {
            Element element = (Element) lineList.item(i);
            Line line = new Line();
            line.setColor(element.getAttribute("lineColor"));
            line.setStrokeWidth(Integer.parseInt(element.getAttribute("lineWidth")));
            String[] from = element.getAttribute("from").split(",");
            line.setX1(Integer.parseInt(from[0]));
            line.setY1(Integer.parseInt(from[1]));
            String[] to = element.getAttribute("to").split(",");
            line.setX2(Integer.parseInt(to[0]));
            line.setY2(Integer.parseInt(to[1]));
            line.init();
            lines.add(line);
        }
        return lines;
    }

    private List<Text> parseString(Document document) {
        NodeList lineList = document.getElementsByTagName("string");
        List<Text> texts = new ArrayList<>();
        for (int i = 0; i < lineList.getLength(); i++) {
            Element element = (Element) lineList.item(i);
            Text text = new Text();
            String[] from = element.getAttribute("from").split(",");
            text.setX(Integer.parseInt(from[0]));
            text.setY(Integer.parseInt(from[1]));
            text.setText(element.getAttribute("text"));
            text.setSize(Integer.parseInt(element.getAttribute("size")));
            text.setColor(element.getAttribute("fontColor"));
            text.init();
            texts.add(text);
        }

        return texts;
    }

    private List<Image_0> parseImage0(Document document) {
        NodeList lineList = document.getElementsByTagName("image");
        List<Image_0> image0s = new ArrayList<>();
        for (int i = 0; i < lineList.getLength(); i++) {
            Element element = (Element) lineList.item(i);
            if (element.getAttribute("iconType").equals("0")) {
                Image_0 image0 = new Image_0();
                String[] from = element.getAttribute("from").split(",");
                image0.setX(Integer.parseInt(from[0]));
                image0.setY(Integer.parseInt(from[1]));
                image0.setColor(element.getAttribute("borderColor"));
                image0.setStrokeWidth(Integer.parseInt(element.getAttribute("borderWidth")));
                image0.setCom_path(piclib.get(element.getAttribute("size")).get(element.getAttribute("index")));
                image0.init();
                image0s.add(image0);
            }
        }

        return image0s;
    }

    private List<Image_1> parseImage1(Document document) {
        NodeList lineList = document.getElementsByTagName("image");
        List<Image_1> image1s = new ArrayList<>();
        for (int i = 0; i < lineList.getLength(); i++) {
            Element element = (Element) lineList.item(i);
            if (element.getAttribute("iconType").equals("1")) {
                Image_1 image1 = new Image_1();
                String[] from = element.getAttribute("from").split(",");
                String[] to = element.getAttribute("to").split(",");
                image1.setRect(new Rect(Integer.parseInt(from[0]), Integer.parseInt(from[1]), Integer.parseInt(to[0]), Integer.parseInt(to[1])));
                Bitmap bitmap = null;
                try {
                    File file = new File(Constants.folderPath + "/" + element.getAttribute("filename"));
                    InputStream is = new FileInputStream(file);
                    bitmap = BitmapFactory.decodeStream(is);
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                image1.setBitmap(bitmap);
                image1s.add(image1);
            }
        }

        return image1s;
    }

    private void parseImageStatue(Document document) {
        NodeList lineList = document.getElementsByTagName("imageStatue");
        List<ImageStatue_0> imageStatue0s = new ArrayList<>();
        List<ImageStatue_1> imageStatue1s = new ArrayList<>();
        for (int i = 0; i < lineList.getLength(); i++) {
            Element element = (Element) lineList.item(i);
            if (element.getAttribute("iconType").equals("0")) {
                ImageStatue_0 imageStatue0 = new ImageStatue_0();
                String[] from = element.getAttribute("from").split(",");
                imageStatue0.setX(Integer.parseInt(from[0]));
                imageStatue0.setY(Integer.parseInt(from[1]));
                imageStatue0.setName(element.getAttribute("stname"));
                imageStatue0.setColor(element.getAttribute("borderColor"));
                imageStatue0.setStrokeWidth(Integer.parseInt(element.getAttribute("borderWidth")));
                try {
                    imageStatue0.setOn_path(piclib.get(element.getAttribute("size")).get(element.getAttribute("index_open")));
                } catch (Exception e) {
                    System.out.println(imageStatue0.getName());
                    continue;
                }

                try {
                    imageStatue0.setOff_path(piclib.get(element.getAttribute("size")).get(element.getAttribute("index_close")));
                } catch (Exception e) {
                    System.out.println(imageStatue0.getName());
                    continue;
                }

                imageStatue0.init();
                imageStatue0s.add(imageStatue0);

                if (!imageStatue0.getName().equals("")) {
                    imageStatues.put(imageStatue0.name, imageStatue0);
                }
            } else {
                ImageStatue_1 imageStatue1 = new ImageStatue_1();
                String[] from = element.getAttribute("from").split(",");
                String[] to = element.getAttribute("to").split(",");
                imageStatue1.setRect(new Rect(Integer.parseInt(from[0]), Integer.parseInt(from[1]), Integer.parseInt(to[0]), Integer.parseInt(to[1])));
                imageStatue1.setName(element.getAttribute("stname"));
                imageStatue1s.add(imageStatue1);
                Bitmap bitmap = null;
                try {
                    File file = new File(Constants.folderPath + "/" + element.getAttribute("filename_open"));
                    InputStream is = new FileInputStream(file);
                    bitmap = BitmapFactory.decodeStream(is);
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                imageStatue1.setOpen(bitmap);
                try {
                    File file = new File(Constants.folderPath + "/" + element.getAttribute("filename_close"));
                    InputStream is = new FileInputStream(file);
                    bitmap = BitmapFactory.decodeStream(is);
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                imageStatue1.setClose(bitmap);
                imageStatue1s.add(imageStatue1);

                if (!imageStatue1.name.equals("")) {
                    imageStatues.put(imageStatue1.name, imageStatue1);
                }
            }
        }
        mainUI.setImageStatue0s(imageStatue0s);
        mainUI.setImageStatue1s(imageStatue1s);
    }

    private List<ImageStatue_0> parseImageStatue0(Document document) {
        NodeList lineList = document.getElementsByTagName("imageStatue");
        List<ImageStatue_0> imageStatue0s = new ArrayList<>();
        for (int i = 0; i < lineList.getLength(); i++) {
            Element element = (Element) lineList.item(i);
            if (element.getAttribute("iconType").equals("0")) {
                ImageStatue_0 imageStatue0 = new ImageStatue_0();
                String[] from = element.getAttribute("from").split(",");
                imageStatue0.setX(Integer.parseInt(from[0]));
                imageStatue0.setY(Integer.parseInt(from[1]));
                imageStatue0.setName(element.getAttribute("stname"));
                imageStatue0.setColor(element.getAttribute("borderColor"));
                imageStatue0.setStrokeWidth(Integer.parseInt(element.getAttribute("borderWidth")));
                imageStatue0.setOn_path(piclib.get(element.getAttribute("size")).get(element.getAttribute("index_open")));
                imageStatue0.setOff_path(piclib.get(element.getAttribute("size")).get(element.getAttribute("index_close")));
                imageStatue0.init();
                imageStatue0s.add(imageStatue0);

                if (!imageStatue0.getName().equals("")) {
                    imageStatues.put(imageStatue0.name, imageStatue0);
                }
            }
        }

        return imageStatue0s;
    }

    private List<ImageStatue_1> parseImageStatue1(Document document) {
        NodeList lineList = document.getElementsByTagName("imageStatue");
        List<ImageStatue_1> imageStatue1s = new ArrayList<>();
        for (int i = 0; i < lineList.getLength(); i++) {
            Element element = (Element) lineList.item(i);
            if (element.getAttribute("iconType").equals("1")) {
                ImageStatue_1 imageStatue1 = new ImageStatue_1();
                String[] from = element.getAttribute("from").split(",");
                String[] to = element.getAttribute("to").split(",");
                imageStatue1.setRect(new Rect(Integer.parseInt(from[0]), Integer.parseInt(from[1]), Integer.parseInt(to[0]), Integer.parseInt(to[1])));
                imageStatue1.setName(element.getAttribute("stname"));
                imageStatue1s.add(imageStatue1);
                Bitmap bitmap = null;
                try {
                    File file = new File(Constants.folderPath + "/" + element.getAttribute("filename_open"));
                    InputStream is = new FileInputStream(file);
                    bitmap = BitmapFactory.decodeStream(is);
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                imageStatue1.setOpen(bitmap);
                try {
                    File file = new File(Constants.folderPath + "/" + element.getAttribute("filename_close"));
                    InputStream is = new FileInputStream(file);
                    bitmap = BitmapFactory.decodeStream(is);
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                imageStatue1.setClose(bitmap);
                imageStatue1s.add(imageStatue1);

                if (!imageStatue1.name.equals("")) {
                    imageStatues.put(imageStatue1.name, imageStatue1);
                }
            }
        }

        return imageStatue1s;
    }

    private Map<String, DyanData> parseDyanData(Document document) {
        NodeList lineList = document.getElementsByTagName("dyanData");
        Map<String, DyanData> dyanDatas = new HashMap<>();
        for (int i = 0; i < lineList.getLength(); i++) {
            Element element = (Element) lineList.item(i);
            DyanData dyanData = new DyanData();
            String[] from = element.getAttribute("from").split(",");
            dyanData.setX(Integer.parseInt(from[0]));
            dyanData.setY(Integer.parseInt(from[1]));
            dyanData.setName(element.getAttribute("name"));
            dyanData.setSize(Integer.parseInt(element.getAttribute("size")));
            dyanData.init();
            dyanDatas.put(element.getAttribute("name"), dyanData);
        }

        return dyanDatas;
    }

    private List<CommandButton> parseCommandButton(Document document) {
        NodeList lineList = document.getElementsByTagName("commandButton");
        List<CommandButton> commandButtons = new ArrayList<>();
        for (int i = 0; i < lineList.getLength(); i++) {
            Element element = (Element) lineList.item(i);
            CommandButton commandButton = new CommandButton();
            String[] from = element.getAttribute("from").split(",");
            String[] to = element.getAttribute("to").split(",");
            commandButton.setRect(new Rect(Integer.parseInt(from[0]), Integer.parseInt(from[1]), Integer.parseInt(to[0]), Integer.parseInt(to[1])));
            Bitmap bitmap = null;
            try {
                File file = new File(Constants.folderPath + "/" + element.getAttribute("filename_down"));
                InputStream is = new FileInputStream(file);
                bitmap = BitmapFactory.decodeStream(is);
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            commandButton.setDown(bitmap);
            try {
                if (element.getAttribute("filename_up").equals("")) {
                    continue;
                }
                File file = new File(Constants.folderPath + "/" + element.getAttribute("filename_up"));
                InputStream is = new FileInputStream(file);
                bitmap = BitmapFactory.decodeStream(is);
                is.close();
            } catch (IOException e) {
            }
            commandButton.setUp(bitmap);
            commandButtons.add(commandButton);
        }

        return commandButtons;
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

        void update() {
            getAnData(mainUI.getDyanDatas());
            getStData(imageStatues);
            //刷新msg的内容
            mainUI.invalidate();
        }
    };
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
                            if (data.get(name).getValid() == 1) {
                                andatas.get(name).setText(new DecimalFormat("0.00").format(data.get(name).getValue()));
                            }
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
