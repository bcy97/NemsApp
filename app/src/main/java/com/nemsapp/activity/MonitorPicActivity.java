package com.nemsapp.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import com.nemsapp.R;
import com.nemsapp.components.CommandButton;
import com.nemsapp.components.DyanData;
import com.nemsapp.components.ImageStatue_0;
import com.nemsapp.components.ImageStatue_1;
import com.nemsapp.components.Image_0;
import com.nemsapp.components.Image_1;
import com.nemsapp.components.Line;
import com.nemsapp.components.Text;
import com.nemsapp.ui.MainUI;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class MonitorPicActivity extends AppCompatActivity {

    private MainUI mainUI;

    private Map<String, Map<String, String>> piclib;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitor_pic);
        mainUI = findViewById(R.id.mainUI);


        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        initXml();
    }

    private void initXml() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
//            InputStream is = getResources().openRawResource(R.raw.system2);
            InputStream is = getAssets().open("xml/2#开闭所系统图.xml");
            Document document = builder.parse(is);
            InputStream is2 = getResources().openRawResource(R.raw.piclib);
            Document pic = builder.parse(is2);

            piclib = initPiclib16(pic);

            mainUI.setLines(parseLine(document));
            mainUI.setTexts(parseString(document));
            mainUI.setImage0s(parseImage0(document));
            mainUI.setImage1s(parseImage1(document));
            mainUI.setImageStatue0s(parseImageStatue0(document));
            mainUI.setImageStatue1s(parseImageStatue1(document));
            mainUI.setCommandButtons(parseCommandButton(document));
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
                    InputStream is = getAssets().open(element.getAttribute("filename"));
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
                    InputStream is = getAssets().open(element.getAttribute("filename_open"));
                    bitmap = BitmapFactory.decodeStream(is);
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                imageStatue1.setOpen(bitmap);
                try {
                    InputStream is = getAssets().open(element.getAttribute("filename_close"));
                    bitmap = BitmapFactory.decodeStream(is);
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                imageStatue1.setClose(bitmap);
                imageStatue1s.add(imageStatue1);
            }
        }

        return imageStatue1s;
    }

    private List<DyanData> parseDyanData(Document document) {
        NodeList lineList = document.getElementsByTagName("dyanData");
        List<DyanData> dyanDatas = new ArrayList<>();
        for (int i = 0; i < lineList.getLength(); i++) {
            Element element = (Element) lineList.item(i);
            DyanData dyanData = new DyanData();
            String[] from = element.getAttribute("from").split(",");
            dyanData.setX(Integer.parseInt(from[0]));
            dyanData.setY(Integer.parseInt(from[1]));
            dyanData.setName(element.getAttribute("name"));
            dyanData.setSize(Integer.parseInt(element.getAttribute("size")));
            dyanData.init();
            System.out.println(dyanData.getName());
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
                InputStream is = getAssets().open(element.getAttribute("filename_down"));
                bitmap = BitmapFactory.decodeStream(is);
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            commandButton.setDown(bitmap);
            try {
                InputStream is = getAssets().open(element.getAttribute("filename_up"));
                bitmap = BitmapFactory.decodeStream(is);
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            commandButton.setUp(bitmap);
            commandButtons.add(commandButton);
        }

        return commandButtons;
    }
}
