package com.example.hqb98.mj.util;

import android.os.Environment;

public class GlobalUtil {

    /**
     * 判断字符串不为空与不为""
     */
    public static boolean isNotEmpty(String str){
        //先判断是不是对象再判断对象是否为""
        if (str==null||str.equals("")){
            return false;
        }else {
            return true;
        }
    }

    /* Checks if external storage is available for read and write */
    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /* Checks if external storage is available to at least read */
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }
}
