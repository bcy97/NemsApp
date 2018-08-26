package com.nemsapp.util;

import android.content.Context;

import org.w3c.dom.Document;

import java.io.InputStream;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class PointParser {
    private Pattern pattern = Pattern.compile("\\s*|\t|\r|\n");

    private Context context;

    public PointParser(Context context) {
        this.context = context;
    }


    public void initData() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
            InputStream is = context.getAssets().open("xml/a.xml");
            Document document = builder.parse(is);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
