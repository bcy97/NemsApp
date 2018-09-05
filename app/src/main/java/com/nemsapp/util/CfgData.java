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

    private static CfgData instance;

    private List<UnitInfo> unitList;

    private CfgData() {
        setUnitInfo();
    }

    public static CfgData getInstance() {
        if (instance == null) {
            instance = new CfgData();
        }
        return instance;
    }

    public void setUnitInfo() {
        unitList = new ArrayList<>();
        File file = new File(Constants.folderPath + "/unitConfigs");
        if (file == null || !file.exists()) {
            return;
        }
        if (!file.isDirectory()) {
            return;
        }
        SAXReader reader = new SAXReader();
        File[] fileArr = file.listFiles();
        for (File item : fileArr) {
            Document doc = null;
            try {
                doc = reader.read(item);
                Element root = doc.getRootElement();

                String temp = root.attributeValue("unitNo");
                Short unitNo = new Short(temp);

                temp = root.attributeValue("type");
                float type = new Float(temp);

                String name = root.attributeValue("name");

                UnitInfo ui = new UnitInfo(name, unitNo, (byte) type);

                unitList.add(ui);
            } catch (DocumentException e) {
                System.out.println(item.getName());
            }
        }
    }

    public List<UnitInfo> getUnitList() {
        return unitList;
    }

}
