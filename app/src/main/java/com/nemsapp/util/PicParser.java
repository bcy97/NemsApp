package com.nemsapp.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Rect;

import com.nemsapp.components.DyanData;
import com.nemsapp.components.image.ImageStatue;
import com.nemsapp.components.image.ImageStatue_0;
import com.nemsapp.components.image.ImageStatue_1;
import com.nemsapp.components.image.Image_0;
import com.nemsapp.components.image.Image_1;
import com.nemsapp.components.staticComponets.CommandButton;
import com.nemsapp.components.staticComponets.Line;
import com.nemsapp.components.staticComponets.Rect1;
import com.nemsapp.components.staticComponets.Text;
import com.nemsapp.ui.MainUI;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class PicParser {

    private static PicParser instance;

    private Map<String, Map<String, String>> piclib;

    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

    private PicParser() {
        initPiclib();
    }

    public static PicParser getInstance() {
        if (instance == null) {
            instance = new PicParser();
        }
        return instance;
    }

    private void initPiclib() {
        File lib = new File(Constants.folderPath + "/iconlibrary/public/piclib.xml");
        DocumentBuilder builder;

        piclib = new HashMap<>();

        try {
            builder = factory.newDocumentBuilder();
            InputStream is = new FileInputStream(lib);
            Document document = builder.parse(is);

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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Map<String, ImageStatue> initXml(MainUI mainUI, String filename) {

        //清空mainUI的内容
        mainUI.clean();

        //读取pic文件
        File file = new File(Constants.folderPath + "/pictures/" + filename);
        InputStream is;
        DocumentBuilder builder;
        try {
            is = new FileInputStream(file);
            builder = factory.newDocumentBuilder();
            Document document = builder.parse(is);

            //设置pic背景颜色
            mainUI.setBackgroundColor(Color.parseColor(document.getDocumentElement().getAttribute("color")));

            //初始化pic的宽高属性
            mainUI.setPicWidth(Integer.parseInt(document.getDocumentElement().getAttribute("width")));
            mainUI.setPicHeight(Integer.parseInt(document.getDocumentElement().getAttribute("height")));

            //解析pic内容
            parseLine(mainUI, document);
            parseImage(mainUI, document);
            parseCommandButton(mainUI, document);
            parseDyanData(mainUI, document);
            parseRect(mainUI, document);
            parseString(mainUI, document);


            return parseImageStatue(mainUI, document);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void parseLine(MainUI mainUI, Document document) {
        NodeList lineList = document.getElementsByTagName("line");
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
            mainUI.getComponents().add(line);
        }
    }

    public void parseString(MainUI mainUI, Document document) {
        NodeList lineList = document.getElementsByTagName("string");
        for (int i = 0; i < lineList.getLength(); i++) {
            Element element = (Element) lineList.item(i);
            Text text = new Text();
            String[] from = element.getAttribute("from").split(",");
            text.setX(Integer.parseInt(from[0]));
            text.setY(Integer.parseInt(from[1]));
            String[] to = element.getAttribute("to").split(",");
            text.setRight(Integer.parseInt(to[0]));
            text.setText(element.getAttribute("text"));
            text.setSize(Integer.parseInt(element.getAttribute("size")));
            text.setColor(element.getAttribute("fontColor"));
            text.setAlign(Integer.parseInt(element.getAttribute("align")));
            text.init();
            mainUI.getComponents().add(text);
        }

    }

    public void parseImage(MainUI mainUI, Document document) {

        //找到所有的image节点
        NodeList imageList = document.getElementsByTagName("image");

        for (int i = 0; i < imageList.getLength(); i++) {
            Element element = (Element) imageList.item(i);
            String[] from = element.getAttribute("from").split(",");
            if (element.getAttribute("iconType").equals("0")) {
                Image_0 image0 = new Image_0();
                image0.setX(Integer.parseInt(from[0]));
                image0.setY(Integer.parseInt(from[1]));
                image0.setColor(element.getAttribute("borderColor"));
                image0.setStrokeWidth(Integer.parseInt(element.getAttribute("borderWidth")));
                try {

                    image0.setCom_path(piclib.get(element.getAttribute("size")).get(element.getAttribute("index")));
                } catch (Exception e) {
                    System.out.println("xml图源缺失：" + element.getAttribute("size") + "位，" + element.getAttribute("index") + "号");
                    continue;
                }
                image0.init();
                mainUI.getComponents().add(image0);
            } else if (element.getAttribute("iconType").equals("1")) {
                Image_1 image1 = new Image_1();
                String[] to = element.getAttribute("to").split(",");
                image1.setRect(new Rect(Integer.parseInt(from[0]), Integer.parseInt(from[1]), Integer.parseInt(to[0]), Integer.parseInt(to[1])));
                Bitmap bitmap;
                try {
                    File file = new File(Constants.folderPath + "/" + element.getAttribute("filename"));
                    InputStream is = new FileInputStream(file);
                    bitmap = BitmapFactory.decodeStream(is);
                    is.close();
                } catch (IOException e) {
                    System.out.println("bmp图源缺失：" + element.getAttribute("filename"));
                    e.printStackTrace();
                    continue;
                }
                image1.setBitmap(bitmap);
                mainUI.getComponents().add(image1);
            }
        }

    }

    /**
     * 解析pic中的imageStatue
     *
     * @param mainUI
     * @param document
     * @return 返回有stname的statue
     */
    public Map<String, ImageStatue> parseImageStatue(MainUI mainUI, Document document) {

        //返回有name属性的imageStatue
        Map<String, ImageStatue> statueMap = new HashMap<>();

        //找到所有imageStatue节点
        NodeList lineList = document.getElementsByTagName("imageStatue");

        //保存所有的imageStatue信息
        List<ImageStatue> imageStatues = new ArrayList<>();

        //遍历所有imageStatue节点
        for (int i = 0; i < lineList.getLength(); i++) {
            Element element = (Element) lineList.item(i);

            String[] from = element.getAttribute("from").split(",");
            if (element.getAttribute("iconType").equals("0")) {
                //处理type为0，使用图源
                ImageStatue_0 imageStatue0 = new ImageStatue_0();
                imageStatue0.setX(Integer.parseInt(from[0]));
                imageStatue0.setY(Integer.parseInt(from[1]));
                imageStatue0.setName(element.getAttribute("stname"));
                imageStatue0.setColor(element.getAttribute("borderColor"));
                imageStatue0.setStrokeWidth(Integer.parseInt(element.getAttribute("borderWidth")));
                try {
                    imageStatue0.setOn_path(piclib.get(element.getAttribute("size")).get(element.getAttribute("index_open")));
                } catch (Exception e) {
                    System.out.println("xml图源缺失：" + element.getAttribute("size") + "位，" + element.getAttribute("index_open") + "号");
                    continue;
                }

                try {
                    imageStatue0.setOff_path(piclib.get(element.getAttribute("size")).get(element.getAttribute("index_close")));
                } catch (Exception e) {
                    System.out.println("xml图源缺失：" + element.getAttribute("size") + "位，" + element.getAttribute("index_close") + "号");
                    continue;
                }

                imageStatue0.init();
                mainUI.getComponents().add(imageStatue0);

                if (!imageStatue0.getName().equals("")) {
                    statueMap.put(imageStatue0.name, imageStatue0);
                }
            } else {
                //处理type为1，使用bmp图库
                ImageStatue_1 imageStatue1 = new ImageStatue_1();
                String[] to = element.getAttribute("to").split(",");
                imageStatue1.setRect(new Rect(Integer.parseInt(from[0]), Integer.parseInt(from[1]), Integer.parseInt(to[0]), Integer.parseInt(to[1])));
                imageStatue1.setName(element.getAttribute("stname"));
                Bitmap bitmap = null;
                try {
                    File file = new File(Constants.folderPath + "/" + element.getAttribute("filename_open"));
                    InputStream is = new FileInputStream(file);
                    bitmap = BitmapFactory.decodeStream(is);
                    is.close();
                } catch (IOException e) {
                    System.out.println("bmp图库缺失：" + element.getAttribute("filename_open"));
                    e.printStackTrace();
                    continue;
                }
                imageStatue1.setOpen(bitmap);
                try {
                    File file = new File(Constants.folderPath + "/" + element.getAttribute("filename_close"));
                    InputStream is = new FileInputStream(file);
                    bitmap = BitmapFactory.decodeStream(is);
                    is.close();
                } catch (IOException e) {
                    System.out.println("bmp图库缺失：" + element.getAttribute("filename_open"));
                    e.printStackTrace();
                    continue;
                }
                imageStatue1.setClose(bitmap);

                mainUI.getComponents().add(imageStatue1);

                if (!imageStatue1.name.equals("")) {
                    statueMap.put(imageStatue1.name, imageStatue1);
                }
            }
        }

        return statueMap;
    }

    public void parseDyanData(MainUI mainUI, Document document) {
        NodeList lineList = document.getElementsByTagName("dyanData");
        Map<String, DyanData> dyanDatas = new HashMap<>();
        for (int i = 0; i < lineList.getLength(); i++) {
            Element element = (Element) lineList.item(i);
            DyanData dyanData = new DyanData();
            String[] from = element.getAttribute("from").split(",");
            dyanData.setX(Integer.parseInt(from[0]));
            dyanData.setY(Integer.parseInt(from[1]));
            String[] to = element.getAttribute("to").split(",");
            dyanData.setRight(Integer.parseInt(to[0]));
            dyanData.setName(element.getAttribute("name"));
            dyanData.setSize(Integer.parseInt(element.getAttribute("size")));
            dyanData.setAlign(Integer.parseInt(element.getAttribute("align")));
            dyanData.init();
            dyanDatas.put(element.getAttribute("name"), dyanData);
        }
        mainUI.setDyanDatas(dyanDatas);

    }

    public void parseCommandButton(MainUI mainUI, Document document) {
        NodeList lineList = document.getElementsByTagName("commandButton");
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
            mainUI.getComponents().add(commandButton);
        }

    }

    public void parseRect(MainUI mainUI, Document document) {
        NodeList rectList = document.getElementsByTagName("rect");
        parseRectList(mainUI, rectList);
    }

    public void parseRectList(MainUI mainUI, NodeList nodeList) {
        for (int i = 0; i < nodeList.getLength(); i++) {
            Element element = (Element) nodeList.item(i);
            Rect1 rect = new Rect1();
            String[] from = element.getAttribute("from").split(",");
            String[] to = element.getAttribute("to").split(",");
            int x1 = Integer.parseInt(from[0]);
            int y1 = Integer.parseInt(from[1]);
            int x2 = Integer.parseInt(to[0]);
            int y2 = Integer.parseInt(to[1]);
            if (x1 > x2) {
                rect.setX1(x2);
                rect.setX2(x1);
            } else {
                rect.setX1(x1);
                rect.setX2(x2);
            }
            if (y1 > y2) {
                rect.setY1(y2);
                rect.setY2(y1);
            } else {
                rect.setY1(y1);
                rect.setY2(y2);
            }
            rect.setBorder(Integer.parseInt(element.getAttribute("border")));
            if (rect.getBorder() == 1) {
                rect.setBorderColor(element.getAttribute("borderColor"));
                rect.setBorderWidth(Integer.parseInt(element.getAttribute("borderWidth")));
            }
            rect.setFill(Integer.parseInt(element.getAttribute("fill")));
            if (rect.getFill() == 1) {
                rect.setFillColor(element.getAttribute("fillColor"));
            }


            mainUI.getComponents().add(rect);
        }
    }


    public Map<String, Map<String, String>> getPiclib() {
        return piclib;
    }

}
