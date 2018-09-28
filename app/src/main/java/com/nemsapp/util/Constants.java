package com.nemsapp.util;

import android.os.Environment;

public class Constants {

    //后端ip
    final static public String ip = "10.0.2.2";

    //文件存储路径
    final static public String folderPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/nemsapp";

    // id
    public static final byte IDAN = 1;
    public static final byte IDACC = 2;
    public static final byte IDST = 3;
    public static final byte IDVIDEO = 4;
    public static final byte IDCTRL = 5;
    public static final byte IDOTHER = 16;
}
