package com.nemsapp.activity;

import android.content.res.AssetManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.nemsapp.R;
import com.nemsapp.components.Component;
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
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class MonitorPicActivity extends AppCompatActivity {

    private MainUI mainUI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitor_pic);
        mainUI = findViewById(R.id.mainUI);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

//        initXml();
        String path = "";
        Component component = new Component(this);
        component.setColor("#ff0000");
        component.setName("16-0");
        component.setCom_path(path);
        component.setStrokeWidth(1);
        component.setX(100);
        component.setY(100);
        component.init();

        String path2 = "";
        Component component2 = new Component(this);
        component2.setColor("#ff0000");
        component2.setName("16-0");
        component2.setCom_path(path2);
        component2.setStrokeWidth(1);
        component2.setX(100);
        component2.setY(150);
        component2.init();
        List<Component> components = new ArrayList<>();
        components.add(component);
        components.add(component2);
        mainUI.setComponents(components);
    }

    private void initXml() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
            InputStream is = getResources().openRawResource(R.raw.system2);
            Document document = builder.parse(is);


            mainUI.setLines(parseLine(document));
            mainUI.setTexts(parseString(document));
        } catch (Exception e) {
            e.printStackTrace();
        }
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
}
