package com.nemsapp.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.nemsapp.R;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class MonitorPicActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitor_pic);
    }

    public void initXml(String xml_name) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        try {
            builder = factory.newDocumentBuilder();
            InputStream is = getResources().openRawResource(R.raw.swch);
            Document document = builder.parse(is);
            NodeList lineList = document.getElementsByTagName("path");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
