package com.nemsapp.util;


import com.nemsapp.vo.AcO;
import com.nemsapp.vo.AnO;
import com.nemsapp.vo.StO;
import com.nemsapp.vo.UnitInfo;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Utils {

    public static SimpleDateFormat _DATE_FORMAT_ = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static int getId(byte type, short unitNo, short ptNo) {

        int id = type << 24 | (unitNo & 0xFF) << 16 | (ptNo & 0xFFFF);

        return id;
    }

    public static byte getTypeInId(int id) {

        byte type = (byte) (id >> 24);

        return type;
    }

    public static short getUnitNoInId(int id) {

        short unitNo = (byte) (id >> 16);

        return unitNo;
    }

    public static short getPtNoInId(int id) {

        short ptNo = (short) id;

        return ptNo;
    }

    public static byte[] idArrToBytes(int[] idArr) {
        byte[] idByteArr = new byte[idArr.length * 4];

        ByteBuffer bb = ByteBuffer.allocate(idByteArr.length);
        bb.order(ByteOrder.LITTLE_ENDIAN);
        for (int id : idArr)
            bb.putInt(id);

        System.arraycopy(bb.array(), 0, idByteArr, 0, bb.position());

        return idByteArr;
    }

    public static byte[] idArrToBytes(Integer[] idArr) {
        byte[] idByteArr = new byte[idArr.length * 4];

        ByteBuffer bb = ByteBuffer.allocate(idByteArr.length);
        bb.order(ByteOrder.LITTLE_ENDIAN);
        for (int id : idArr)
            bb.putInt(id);

        System.arraycopy(bb.array(), 0, idByteArr, 0, bb.position());

        return idByteArr;
    }

    public static Integer[] anPtNamesToIds(String[] ptNames) {
        Integer[] ids = new Integer[ptNames.length];
        for (int i = 0; i < ptNames.length; i++)
            ids[i] = CfgData.getInstance().getAnID(ptNames[i]);
        return ids;
    }

    public static Integer[] acPtNamesToIds(String[] ptNames) {
        Integer[] ids = new Integer[ptNames.length];
        for (int i = 0; i < ptNames.length; i++)
            ids[i] = CfgData.getInstance().getAcID(ptNames[i]);
        return ids;
    }

    public static Integer[] stPtNamesToIds(String[] ptNames) {
        Integer[] ids = new Integer[ptNames.length];
        for (int i = 0; i < ptNames.length; i++)
            ids[i] = CfgData.getInstance().getStID(ptNames[i]);
        return ids;
    }

    /***
     * 通过单元名获取该单元所有的遥测id
     * */
    public Integer[] getAnIdsByUnitName(String unitName) {
        Integer[] ids = new Integer[0];


        List<UnitInfo> unitList = CfgData.getInstance().getInstance().getUnitList();
        for (UnitInfo ui : unitList) {
            if (ui.getName().equals(unitName)) {
                List<AnO> anoList = CfgData.getInstance().getInstance().getAnOByUnitNo(ui.getUnitNo());
                ids = new Integer[anoList.size()];
                for (int i = 0; i < ids.length; i++)
                    ids[i] = anoList.get(i).getId();
                break;
            }
        }
        return ids;
    }

    /***
     * 通过单元名获取该单元所有的遥信id
     * */
    public Integer[] getStIdsByUnitName(String unitName) {
        Integer[] ids = new Integer[0];


        List<UnitInfo> unitList = CfgData.getInstance().getUnitList();
        for (UnitInfo ui : unitList) {
            if (ui.getName().equals(unitName)) {
                List<StO> stoList = CfgData.getInstance().getStOByUnitNo(ui.getUnitNo());
                ids = new Integer[stoList.size()];
                for (int i = 0; i < ids.length; i++)
                    ids[i] = stoList.get(i).getId();
                break;
            }
        }
        return ids;
    }

    /***
     * 通过单元名获取该单元所有的电度id
     * */
    public Integer[] getAcIdsByUnitName(String unitName) {
        Integer[] ids = new Integer[0];


        List<UnitInfo> unitList = CfgData.getInstance().getUnitList();
        for (UnitInfo ui : unitList) {
            if (ui.getName().equals(unitName)) {
                List<AcO> acoList = CfgData.getInstance().getAcOByUnitNo(ui.getUnitNo());
                ids = new Integer[acoList.size()];
                for (int i = 0; i < ids.length; i++)
                    ids[i] = acoList.get(i).getId();
                break;
            }
        }
        return ids;
    }

    /***
     * 获取某个时间段中的所有5分钟点，即00:05、00:10
     * */
    public static List<String> get5MinPoint(Calendar begTime, Calendar endTime) {
        List<String> list = new ArrayList<>();

        Calendar tmpCal = Calendar.getInstance();
        tmpCal.setTimeInMillis(begTime.getTimeInMillis());
        tmpCal.set(Calendar.SECOND, endTime.get(Calendar.SECOND));
        tmpCal.set(Calendar.MILLISECOND, endTime.get(Calendar.MILLISECOND));

        while (tmpCal.getTimeInMillis() <= endTime.getTimeInMillis()) {
            int minute = tmpCal.get(Calendar.MINUTE);
            if (0 == minute % 5) {
                list.add(Utils._DATE_FORMAT_.format(tmpCal.getTime()));
                tmpCal.add(Calendar.MINUTE, 5);
            } else {
                tmpCal.add(Calendar.MINUTE, 1);
            }
        }

        return list;
    }

    /***
     * 获取某个时间段中的所有整点，即00:00、01:00
     * */
    public static List<String> getHourPoint(Calendar begTime, Calendar endTime) {
        List<String> list = new ArrayList<>();

        Calendar tmpCal = Calendar.getInstance();
        tmpCal.setTimeInMillis(begTime.getTimeInMillis());
        tmpCal.set(Calendar.SECOND, endTime.get(Calendar.SECOND));
        tmpCal.set(Calendar.MILLISECOND, endTime.get(Calendar.MILLISECOND));

        while (tmpCal.getTimeInMillis() <= endTime.getTimeInMillis()) {
            int minute = tmpCal.get(Calendar.MINUTE);
            if (0 == minute) {
                list.add(Utils._DATE_FORMAT_.format(tmpCal.getTime()));
                tmpCal.add(Calendar.HOUR, 1);
            } else {
                tmpCal.add(Calendar.MINUTE, 1);
            }
        }

        return list;
    }
}
