package com.nemsapp.util;

import android.graphics.Bitmap;
import android.graphics.Color;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class LibParser {

    private static LibParser instance;

    private int[][][] picLib;

    public LibParser() {
        initPiclib();
    }

    public static LibParser getInstance() {
        if (instance == null) {
            instance = new LibParser();
        }
        return instance;
    }

    public void initPiclib() {
//        readLib(16);
        picLib = new int[5][][];

        readLib(16);
        readLib(24);
        readLib(32);
        readLib(48);
        readLib(64);
    }

    public void readLib(int size) {
        File lib = new File(Constants.folderPath + "/iconlibrary/public/Bit" + size + ".lib");

        //判断图源位数
        int len;
        //不同位数图源对应标号（16->0,24->1,...,64->5)
        int index = getIndex(size);
        switch (size) {
            case 16:
            case 32:
            case 64:
                len = size * size / 8;
                break;
            case 24:
                len = 128;
                break;
            case 48:
                len = 512;
                break;
            default:
                len = 0;
                break;
        }

        //图源个数
        int num = (int) (lib.length() / len);

        //当前位数存储的图源列表
        picLib[index] = new int[num][len];

        try {
            FileInputStream is = new FileInputStream(lib);
            for (int i = 0; i < lib.length() / len; i++) {
                byte[] pic = new byte[len];
                is.read(pic, 0, len);

                //判断是否为丢弃位
                int drop = 0;

                for (int j = 0; j < pic.length; j++) {
                    if ((size == 24 && j % 4 == 3) || (size == 48 && (j % 8 == 6 || j % 8 == 7))) {
                        continue;
                    }
                    picLib[index][i][drop] = pic[j] & 0xff;
                    drop++;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public Bitmap getIcon(int size, int index, String color, String background) {
        int[] rgbArray = new int[size * size + size];// 偏移量+纵向开始坐标*扫描间距+横向开始坐标
        for (int i = 0; i < size; i++) {//遍历每一行
            for (int j = 0; j < size; j++) {//遍历每一列
                int num = (i * size + j) / 8;
                if (((picLib[getIndex(size)][index][num] >> (7 - i * size - j + num * 8)) & 1) == 1) {
                    rgbArray[i * size + j] = Color.parseColor(color);
                } else {
                    rgbArray[i * size + j] = Color.parseColor(background);
                }
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(rgbArray, size, size, Bitmap.Config.ARGB_8888);
        return bitmap;
    }

    private int getIndex(int size) {
        switch (size) {
            case 16:
                return 0;
            case 24:
                return 1;
            case 32:
                return 2;
            case 48:
                return 3;
            case 64:
                return 4;
            default:
                return 0;
        }
    }
}
