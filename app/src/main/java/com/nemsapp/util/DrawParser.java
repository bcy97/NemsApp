package com.nemsapp.util;

import android.graphics.Color;
import android.graphics.RectF;

import com.nemsapp.components.image.ImageStatue;
import com.nemsapp.ui.MainUI;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class DrawParser {

    private static DrawParser instance;

    //pic图源库
    private Map<String, Map<String, String>> piclib;

    //pic图源需要填充的列表
    private Map<String, List<String>> picFill;

    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

    private DrawParser(MainUI mainUI) {
        initPiclib(mainUI);
    }

    public static DrawParser getInstance(MainUI mainUI) {
        if (instance == null) {
            instance = new DrawParser(mainUI);
        }
        return instance;
    }

    private void initPiclib(MainUI mainUI) {
//        File lib = new File(Constants.folderPath + "/baseConfigs/piclib.xml");
        DocumentBuilder builder;

        piclib = new HashMap<>();
        picFill = new HashMap<>();

        try {
            builder = factory.newDocumentBuilder();
//            InputStream is = new FileInputStream(lib);
            InputStream is = mainUI.getContext().getAssets().open("piclib.xml");
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
            lineList = document.getElementsByTagName("p24");
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


    public Map<String, ImageStatue> initXml(MainUI mainUI) {

        //清空mainUI的内容
        mainUI.clean();

        //读取pic文件
//        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + mainUI.getFilename());
        InputStream is;
        DocumentBuilder builder;
        try {
            is = mainUI.getContext().getAssets().open(mainUI.getFilename());
//            is = new FileInputStream(file);
            builder = factory.newDocumentBuilder();
            Document document = builder.parse(is);

            //设置pic背景颜色
            mainUI.setBackgroundColor(Color.parseColor(document.getDocumentElement().getAttribute("color")));

            //初始化pic的宽高属性
            mainUI.setPicWidth(Integer.parseInt(document.getDocumentElement().getAttribute("width")));
            mainUI.setPicHeight(Integer.parseInt(document.getDocumentElement().getAttribute("height")));

            return parseImageStatue(mainUI, document);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
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

        //遍历所有imageStatue节点
        for (int i = 0; i < lineList.getLength(); i++) {
            Element element = (Element) lineList.item(i);

            RectF rect = getComponentRect(element);
            if (element.getAttribute("iconType").equals("0")) {
                //处理type为1，使用bmp图库
                ImageStatue imageStatue1 = new ImageStatue();
                imageStatue1.setRect(rect);
                imageStatue1.setName(element.getAttribute("stname"));

                imageStatue1.setOpen(LibParser.getInstance().getIcon(Integer.parseInt(element.getAttribute("size")), Integer.parseInt(element.getAttribute("index_open")), element.getAttribute("borderColor"), "#303030"));
                imageStatue1.setClose(LibParser.getInstance().getIcon(Integer.parseInt(element.getAttribute("size")), Integer.parseInt(element.getAttribute("index_close")), element.getAttribute("borderColor"), "#303030"));

                mainUI.getComponents().add(imageStatue1);
            }
        }

        return statueMap;
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

}
