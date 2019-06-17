package com.tzutalin.dlibtest;

import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.StatusCode;

public class GlobalVar {
    public static String token = null;
    public static String nimToken = null;
    public static String account = null;
    public static String appkey = "45c6af3c98409b18a84451215d0bdd6e";
    public static String passwdMD5 = null;
    public static boolean isIMLogin(){
        return (NIMClient.getStatus() == StatusCode.LOGINED);
    }
}
