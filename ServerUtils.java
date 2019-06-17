package com.xuhong.smarthome.utils;

import com.xuhong.smarthome.bean.UserBean;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ServerUtils {
    private static String path;
    private static OutputStream out;
    private static HttpURLConnection http;
    private static String ip = "183.134.75.16";
    private static String server_path = "http://" + ip+"/yf";

    private static ExecutorService THREAD_POOL = new ThreadPoolExecutor(10, 20, 60, TimeUnit.MINUTES,
            new LinkedBlockingQueue<Runnable>(), Executors.defaultThreadFactory(),
            new ThreadPoolExecutor.AbortPolicy());

    public static String getIp() {
        return ip;
    }

    public static String getServer_path() {
        return server_path;
    }

    public static void setServer_path(String server_path) {
        ServerUtils.server_path = server_path;
    }

    public static void setIp(String ip) {
        server_path = "http://" + ip+"/yf";
        ServerUtils.ip = ip;
    }

    //注册账号 返回结果码 1 成功  0失败
    public static String isRegistered(String user_name, String user_pwd, String user_nickname) {
        path = server_path + "/xq_registered.php";
        final String params = "user_name=" + user_name + '&' + "user_pwd=" + user_pwd + '&' + "user_nickname=" + user_nickname;
        Future<String> future = THREAD_POOL.submit(new Callable<String>() {
            @Override
            public String call() {
                try {
                    con(path, params);
                    String result = readStream(http.getInputStream());//返回结果
                    JSONObject resultJSON = new JSONObject(result);
                    String r = resultJSON.getString("status");
                    return r;
                } catch (Exception e) {
                    e.printStackTrace();
                    return "0";
                }
            }
        });
        try {
            String result = future.get(); // 获取返回结果
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "0";
    }

    //登陆账号
    public static String isLogin(String user_name, String user_pwd) {
        path = server_path + "/xq_login.php";
        final String params = "user_name=" + user_name + '&' + "user_pwd=" + user_pwd;
        Future<String> future = THREAD_POOL.submit(new Callable<String>() {
            @Override
            public String call() {
                try {
                    con(path, params);
                    String result = readStream(http.getInputStream());//返回结果
                    /*获取服务器返回的JSON数据*/
                    JSONObject resultJSON = new JSONObject(result);
                    if (resultJSON.getString("status").equals("1")) {
                        if (resultJSON.getString("user_name") != null)
                            UserBean.setUser_name(resultJSON.getString("user_name"));
                        else
                            UserBean.setUser_name("");
                        if (resultJSON.getString("user_nickname") != null)
                            UserBean.setUser_nickname(resultJSON.getString("user_nickname"));
                        else
                            UserBean.setUser_nickname("");
                        UserBean.setIsLogin("1");
                    }
                    String r = resultJSON.getString("status");
                    return r;
                } catch (Exception e) {
                    e.printStackTrace();
                    return "0";
                }
            }
        });
        try {
            String result = future.get(); // 获取返回结果

            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "0";
    }

    //建立网络连接
    public static void con(String path, String params) throws Exception {
        URL url = new URL(path);
        http = (HttpURLConnection) url.openConnection();
        //往网页写入POST数据，和网页POST方法类似，参数间用‘&’连接
        http.setDoOutput(true);
        http.setDoInput(true);
        http.setRequestMethod("POST");
        out = http.getOutputStream();
        out.write(params.getBytes());//post提交参数
        out.flush();
        out.close();
    }

    //读取流
    public static String readStream(InputStream is) {
        InputStreamReader isr;
        String result = "";
        try {
            String line;
            isr = new InputStreamReader(is, "utf-8");
            BufferedReader br = new BufferedReader(isr);
            while ((line = br.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

}
