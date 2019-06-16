package com.xuhong.smarthome.bean;

public class UserBean {
    private static String isLogin = "-";

    private static String user_name = "-";        //用户名
    private static String user_nickname = "-";    //昵称

    public static String getIsLogin() {
        return isLogin;
    }

    public static void setIsLogin(String isLogin) {
        UserBean.isLogin = isLogin;
    }

    public static String getUser_name() {
        return user_name;
    }

    public static void setUser_name(String user_name) {
        UserBean.user_name = user_name;
    }

    public static String getUser_nickname() {
        return user_nickname;
    }

    public static void setUser_nickname(String user_nickname) {
        UserBean.user_nickname = user_nickname;
    }
}
