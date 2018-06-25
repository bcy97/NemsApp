package com.nemsapp.activity;

import android.content.res.AssetManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.nemsapp.R;
import com.nemsapp.components.Component;
import com.nemsapp.components.Image;
import com.nemsapp.components.Line;
import com.nemsapp.components.Text;
import com.nemsapp.ui.MainUI;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

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
            InputStream is = getResources().openRawResource(R.raw.system2);
            Document document = builder.parse(is);
            InputStream is2 = getResources().openRawResource(R.raw.piclib);
            Document pic = builder.parse(is2);

            piclib = initPiclib16(pic);

            mainUI.setLines(parseLine(document));
            mainUI.setTexts(parseString(document));
            mainUI.setImages(parseImage(document));
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
            Line line = new Line(this);
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
            Text text = new Text(this);
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

    private List<Image> parseImage(Document document) {
        NodeList lineList = document.getElementsByTagName("image");
        List<Image> images = new ArrayList<>();
        for (int i = 0; i < lineList.getLength(); i++) {
            Element element = (Element) lineList.item(i);
            if (element.getAttribute("iconType").equals("0")) {
                Image image = new Image(this);
                String[] from = element.getAttribute("from").split(",");
                image.setX(Integer.parseInt(from[0]));
                image.setY(Integer.parseInt(from[1]));
                image.setColor(element.getAttribute("borderColor"));
                image.setStrokeWidth(Integer.parseInt(element.getAttribute("borderWidth")));
                image.setCom_path(piclib.get(element.getAttribute("size")).get(element.getAttribute("index")));
                image.init();
                images.add(image);
            }
        }

        return images;
    }
}
