package com.nemsapp.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.RectF;

import com.nemsapp.components.DyanData;
import com.nemsapp.components.image.ImageNavi;
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

    //pic图源库
    private Map<String, Map<String, String>> piclib;

    //pic图源需要填充的列表
    private Map<String, List<String>> picFill;

    //imageNavi和侧边导航栏信息
    private List<String> naviList;

    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

    private PicParser() {
        initPiclib();
        initNaviTable();
    }

    public static PicParser getInstance() {
        if (instance == null) {
            instance = new PicParser();
        }
        return instance;
    }

    private void initPiclib() {
        File lib = new File(Constants.folderPath + "/baseConfigs/piclib.xml");
        DocumentBuilder builder;

        piclib = new HashMap<>();
        picFill = new HashMap<>();

        try {
            builder = factory.newDocumentBuilder();
            InputStream is = new FileInputStream(lib);
            Document document = builder.parse(is);

            //解析16位图源
            NodeList lineList = document.getElementsByTagName("p16");
            Map<String, String> lib16 = new HashMap<>();
            List<String> fill16 = new ArrayList<>();
            for (int i = 0; i < lineList.getLength(); i++) {
                Element element = (Element) lineList.item(i);
                lib16.put(element.getAttribute("id"), element.getAttribute("path"));
                if (element.getAttribute("fill").equals("1")) {
                    fill16.add(element.getAttribute("id"));
                }
            }

            //解析24位图源
            lineList = document.getElementsByTagName("p32");
            Map<String, String> lib24 = new HashMap<>();
            List<String> fill24 = new ArrayList<>();
            for (int i = 0; i < lineList.getLength(); i++) {
                Element element = (Element) lineList.item(i);
                lib24.put(element.getAttribute("id"), element.getAttribute("path"));
                if (element.getAttribute("fill").equals("1")) {
                    fill24.add(element.getAttribute("id"));
                }
            }

            //解析32位图源
            lineList = document.getElementsByTagName("p32");
            Map<String, String> lib32 = new HashMap<>();
            List<String> fill32 = new ArrayList<>();
            for (int i = 0; i < lineList.getLength(); i++) {
                Element element = (Element) lineList.item(i);
                lib32.put(element.getAttribute("id"), element.getAttribute("path"));
                if (element.getAttribute("fill").equals("1")) {
                    fill32.add(element.getAttribute("id"));
                }
            }

            //解析24位图源
            lineList = document.getElementsByTagName("p48");
            Map<String, String> lib48 = new HashMap<>();
            List<String> fill48 = new ArrayList<>();
            for (int i = 0; i < lineList.getLength(); i++) {
                Element element = (Element) lineList.item(i);
                lib48.put(element.getAttribute("id"), element.getAttribute("path"));
                if (element.getAttribute("fill").equals("1")) {
                    fill48.add(element.getAttribute("id"));
                }
            }

            //绘制路径放入piclib
            piclib.put("16", lib16);
            piclib.put("24", lib24);
            piclib.put("32", lib32);
            piclib.put("48", lib48);

            //填充模式列表
            picFill.put("16", fill16);
            picFill.put("24", fill24);
            picFill.put("32", fill32);
            picFill.put("48", fill48);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initNaviTable() {
        File lib = new File(Constants.folderPath + "/baseConfigs/Picture.xml");
        DocumentBuilder builder;

        naviList = new ArrayList<>();

        try {
            builder = factory.newDocumentBuilder();
            InputStream is = new FileInputStream(lib);
            Document document = builder.parse(is);

            NodeList picList = document.getElementsByTagName("pic");
            for (int i = 0; i < picList.getLength(); i++) {
                Element element = (Element) picList.item(i);
                naviList.add(element.getAttribute("name"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Map<String, ImageStatue> initXml(MainUI mainUI) {

        //清空mainUI的内容
        mainUI.clean();

        //读取pic文件
        File file = new File(Constants.folderPath + "/pictures/" + mainUI.getFilename());
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
            parseRectAndRoundRect(mainUI, document);
            parseString(mainUI, document);
            parseImageNavi(mainUI, document);


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
            line.setRect(getComponentRect(element));
            line.setColor(element.getAttribute("lineColor"));
            line.setStrokeWidth(Integer.parseInt(element.getAttribute("lineWidth")));
            line.init();
            mainUI.getComponents().add(line);
        }
    }

    public void parseString(MainUI mainUI, Document document) {
        NodeList lineList = document.getElementsByTagName("string");
        for (int i = 0; i < lineList.getLength(); i++) {
            Element element = (Element) lineList.item(i);
            Text text = new Text();
            text.setRect(getComponentRect(element));
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
            RectF rect = getComponentRect(element);
            if (element.getAttribute("iconType").equals("0")) {
                Image_0 image0 = new Image_0();
                image0.setRect(rect);
                image0.setColor(element.getAttribute("borderColor"));
                image0.setStrokeWidth(Integer.parseInt(element.getAttribute("borderWidth")));

                //设置路径
                try {
                    image0.setCom_path(piclib.get(element.getAttribute("size")).get(element.getAttribute("index")));
                } catch (Exception e) {
                    System.out.println("xml图源缺失：" + element.getAttribute("size") + "位，" + element.getAttribute("index") + "号");
                    continue;
                }

                //设置画笔是否为填充模式
                if (picFill.get(element.getAttribute("size")).contains(element.getAttribute("index"))) {
                    image0.setFill(1);
                }

                image0.init();
                mainUI.getComponents().add(image0);
            } else if (element.getAttribute("iconType").equals("1")) {
                Image_1 image1 = new Image_1();
                image1.setRect(rect);
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

            RectF rect = getComponentRect(element);
            if (element.getAttribute("iconType").equals("0")) {
                //处理type为0，使用图源
                ImageStatue_0 imageStatue0 = new ImageStatue_0();
                imageStatue0.setRect(rect);
                imageStatue0.setName(element.getAttribute("stname"));
                imageStatue0.setColor(element.getAttribute("borderColor"));
                imageStatue0.setStrokeWidth(Integer.parseInt(element.getAttribute("borderWidth")));
                try {
                    imageStatue0.setOn_path(piclib.get(element.getAttribute("size")).get(element.getAttribute("index_open")));
                } catch (Exception e) {
                    System.out.println("xml图源缺失：" + element.getAttribute("size") + "位，" + element.getAttribute("index_open") + "号");
                    continue;
                }

                //设置画笔是否填充模式
                if (picFill.get(element.getAttribute("size")).contains(element.getAttribute("index_open"))) {
                    imageStatue0.setOn_fill(1);
                }

                try {
                    imageStatue0.setOff_path(piclib.get(element.getAttribute("size")).get(element.getAttribute("index_close")));
                } catch (Exception e) {
                    System.out.println("xml图源缺失：" + element.getAttribute("size") + "位，" + element.getAttribute("index_close") + "号");
                    continue;
                }

                //设置画笔是否填充模式
                if (picFill.get(element.getAttribute("size")).contains(element.getAttribute("index_close"))) {
                    imageStatue0.setOff_fill(1);
                }

                imageStatue0.init();
                mainUI.getComponents().add(imageStatue0);

                if (!imageStatue0.getName().equals("")) {
                    statueMap.put(imageStatue0.name, imageStatue0);
                }
            } else {
                //处理type为1，使用bmp图库
                ImageStatue_1 imageStatue1 = new ImageStatue_1();
                imageStatue1.setRect(rect);
                imageStatue1.setName(element.getAttribute("stname"));
                Bitmap bitmap;
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
            commandButton.setRect(getComponentRect(element));
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

    public void parseRectAndRoundRect(MainUI mainUI, Document document) {
        NodeList rectList = document.getElementsByTagName("rect");
        for (int i = 0; i < rectList.getLength(); i++) {
            mainUI.getComponents().add(parseBasicRect((Element) rectList.item(i)));
        }
        NodeList roundRectList = document.getElementsByTagName("roundrect");
        for (int i = 0; i < roundRectList.getLength(); i++) {
            mainUI.getComponents().add(parseBasicRect((Element) roundRectList.item(i)));
        }
    }

    public Rect1 parseBasicRect(Element element) {
        Rect1 rect = new Rect1();
        rect.setRect(getComponentRect(element));
        rect.setBorder(Integer.parseInt(element.getAttribute("border")));
        if (rect.getBorder() == 1) {
            rect.setBorderColor(element.getAttribute("borderColor"));
            rect.setBorderWidth(Integer.parseInt(element.getAttribute("borderWidth")));
        }
        rect.setFill(Integer.parseInt(element.getAttribute("fill")));
        if (rect.getFill() == 1) {
            rect.setFillColor(element.getAttribute("fillColor"));
        }

        if (!element.getAttribute("round").equals("")) {
            rect.setRound(1);
            rect.setR(Integer.parseInt(element.getAttribute("round").split(",")[1]) / 2);
        }

        return rect;
    }

    public void parseImageNavi(MainUI mainUI, Document document) {

        //找到所有的image节点
        NodeList imageList = document.getElementsByTagName("imageNavi");

        for (int i = 0; i < imageList.getLength(); i++) {
            Element element = (Element) imageList.item(i);
            RectF rect = getComponentRect(element);
            ImageNavi imageNavi = new ImageNavi();
            imageNavi.setRect(rect);
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
            imageNavi.setBitmap(bitmap);
            imageNavi.setNo(Integer.parseInt(element.getAttribute("naviTo")));
            mainUI.getComponents().add(imageNavi);
            mainUI.getClickableComponents().add(imageNavi);
        }
    }

    /**
     * 根据xml文档里的from和to值确定组件的范围
     *
     * @param element xml文档中的组件
     * @return Rect 组件范围
     */
    private RectF getComponentRect(Element element) {
        RectF rect = new RectF();
        String[] from = element.getAttribute("from").split(",");
        String[] to = element.getAttribute("to").split(",");
        int x1 = Integer.parseInt(from[0]);
        int y1 = Integer.parseInt(from[1]);
        int x2 = Integer.parseInt(to[0]);
        int y2 = Integer.parseInt(to[1]);

        //如果是直线，直接返回点坐标
        if (element.getTagName().equals("line")) {
            rect.set(x1, y1, x2, y2);
            return rect;
        }

        //不是直线，根据from，to的坐标圈定rect
        if (x1 > x2) {
            rect.left = x2;
            rect.right = x1;
        } else {
            rect.left = x1;
            rect.right = x2;
        }
        if (y1 > y2) {
            rect.top = y2;
            rect.bottom = y1;
        } else {
            rect.top = y1;
            rect.bottom = y2;
        }
        return rect;
    }

    public Map<String, Map<String, String>> getPiclib() {
        return piclib;
    }

    public List<String> getNaviList() {
        return naviList;
    }
}
