package com.nemsapp.util;

import com.nemsapp.vo.UnitInfo;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CfgData {


    public static List<UnitInfo> getAllUnitInfo() {
        List<UnitInfo> unitList = new ArrayList<>();
        File file = new File(Constants.folderPath + "/unitConfigs");
        if (file == null || file.exists()) {
            return unitList;
        }
        if (!file.isDirectory()) {
            return unitList;
        }
        SAXReader reader = new SAXReader();
        File[] fileArr = file.listFiles();
        for (File item : fileArr) {
            Document doc = null;
            try {
                doc = reader.read(item);
            } catch (DocumentException e) {
                e.printStackTrace();
            }
            Element root = doc.getRootElement();

            String temp = root.attributeValue("unitNo");
            Short unitNo = new Short(temp);

            temp = root.attributeValue("type");
            float type = new Float(temp);

            String name = root.attributeValue("name");

            UnitInfo ui = new UnitInfo(name, unitNo, (byte) type);

            unitList.add(ui);
        }
        return unitList;
    }

    public static List<UnitInfo> getUnitInfoByUnitName(String unitname) {
        List<UnitInfo> unitList = new ArrayList<>();
        File file = new File(Constants.folderPath + "/unitConfigs/"+unitname);
        if (file == null || file.exists()) {
            return unitList;
        }
        SAXReader reader = new SAXReader();
        Document doc = null;
        try {
            doc = reader.read(file);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        Element root = doc.getRootElement();

        String temp = root.attributeValue("unitNo");
        Short unitNo = new Short(temp);

        temp = root.attributeValue("type");
        float type = new Float(temp);

        String name = root.attributeValue("name");

        UnitInfo ui = new UnitInfo(name, unitNo, (byte) type);

        unitList.add(ui);
        return unitList;
    }

}
