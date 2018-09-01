package com.nemsapp.util;

import com.nemsapp.vo.FileMD5;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class FileHelper {

    public static Map<String, List<FileMD5>> getConfigFileMD5() {

        Map<String, List<FileMD5>> md5Map = new HashMap<>();

        File configList = new File(Constants.folderPath + "/configList.xml");

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
}
