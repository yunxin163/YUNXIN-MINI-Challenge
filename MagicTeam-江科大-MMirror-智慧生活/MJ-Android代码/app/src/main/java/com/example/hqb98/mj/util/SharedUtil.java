package com.example.hqb98.mj.util;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SharedUtil {

    public static void save(String key,boolean value){
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(MyApplication.getContext()).edit();
        editor.putBoolean(key,value);
        editor.apply();
    }

    public static void save(String key,float value){
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(MyApplication.getContext()).edit();
        editor.putFloat(key,value);
        editor.apply();
    }

    public static void save(String key,int value){
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(MyApplication.getContext()).edit();
        editor.putInt(key,value);
        editor.apply();
    }

    public static void save(String key,String value){
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(MyApplication.getContext()).edit();
        editor.putString(key,value);
        editor.apply();
    }

    public static void save(String key,long value){
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(MyApplication.getContext()).edit();
        editor.putLong(key,value);
        editor.apply();
    }

    public static boolean read(String key,boolean defValue){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MyApplication.getContext());
        return preferences.getBoolean(key,defValue);
    }

    public static int read(String key,int defValue){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MyApplication.getContext());
        return preferences.getInt(key,defValue);
    }

    public static float read(String key,float defValue){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MyApplication.getContext());
        return preferences.getFloat(key,defValue);
    }

    public static String read(String key,String defValue){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MyApplication.getContext());
        return preferences.getString(key,defValue);
    }

    public static long read(String key,long defValue){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MyApplication.getContext());
        return preferences.getLong(key,defValue);
    }

    //判断preferences文件中是否含有指定的键值
    public static boolean contains(String key){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MyApplication.getContext());
        return preferences.contains(key);
    }

    //删除指定的key所对应的值
    public static void clear(String key){
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(MyApplication.getContext()).edit();
        editor.remove(key);
        editor.apply();
    }

    //清除preference文件中所有的值
    public static void clearAll(){
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(MyApplication.getContext()).edit();
        editor.clear();
        editor.apply();
    }

}
