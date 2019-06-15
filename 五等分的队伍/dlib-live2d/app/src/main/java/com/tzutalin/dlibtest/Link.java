package com.tzutalin.dlibtest;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Link  {
    private static Gson gson = new Gson();
    private static String yanzhengma=null;
    private static final OkHttpClient okHttpClient = new OkHttpClient();
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    public static void pushRequest(final URL url,HashMap<String,String> keymap,final Callback callback){
        new Thread(() -> {
            String json = gson.toJson(keymap);
            RequestBody requestBody = RequestBody.create(JSON, json);
            Request request = new Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .build();

            okHttpClient.newCall(request).enqueue(callback);

        }).start();
    }
    public static void phoneDataPush(final URL url, final String phone,final Callback phoneCallback){

        HashMap<String,String> map = new HashMap<>();
        map.put("phone_number",phone);
        pushRequest(url,map,phoneCallback);

    }
    public static void yanzhengmaDatePush(final URL url,final String phone,final String yanzhengma,final Callback yanzhengmaCallback){
        HashMap<String,String> map = new HashMap<>();
        map.put("phone_number",phone);
        map.put("sms_code",yanzhengma);
        pushRequest(url,map,yanzhengmaCallback);
    }
    public static void passwordDataPush(final URL url,final String phone,final String password,final Callback passwordCallback){


        HashMap<String,String> map = new HashMap<>();
        map.put("phone_number",phone);
        map.put("password",password);
        pushRequest(url,map,passwordCallback);

    }
    public static void passwordChange(final URL url,final String password,final Callback passwordCallback){
        HashMap<String,String> map = new HashMap<>();
        map.put("password",password);
        pushRequest(url,map,passwordCallback);
    }
    public static void login(final URL url,final String phone ,final String password,final Callback loginCallback){
        HashMap<String,String> map = new HashMap<>();
        map.put("username",phone);
        map.put("password",password);
        pushRequest(url,map,loginCallback);
    }
    public static void getInfo(final URL url,final String token,final Callback getInfoCallback){
        new Thread(()->{
            Request request = new Request.Builder()
                    .url(url)
                    .addHeader("Authorization",token)
                    .get()
                    .build();
            okHttpClient.newCall(request).enqueue(getInfoCallback);

        }).start();
    }
    public static void getImage(final URL url,final Callback getImageCallback){
        new Thread(()->{
            Request request = new Request.Builder()
                    .url(url)
                    .build();
            okHttpClient.newCall(request).enqueue(getImageCallback);

        }).start();
    }
    public static void postImage(final URL url,final String token,final File file ,final Callback postImageCallback){

        new Thread(()->{
            RequestBody flieBody = RequestBody.create(MediaType.parse("image/*"),file);
            RequestBody requestBody =new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("avatar",file.getName(),flieBody)
                    .build();
            Log.d("filename",file.getName());
            Request request = new Request.Builder()
                    .url(url)
                    .addHeader("Authorization",token)
                    .post(requestBody)
                    .build();
            okHttpClient.newCall(request).enqueue(postImageCallback);



        }).start();

    }
    public static void getRole(final URL url ,final String token ,final Callback getRoleCallback){
        new Thread(()->{
            Request request = new Request.Builder()
                    .url(url)
                    .addHeader("Authorization",token)
                    .get()
                    .build();
            okHttpClient.newCall(request).enqueue(getRoleCallback);
        }).start();
    }
    public static void addRole(final URL url,final String token,final String phone ,final Callback addRoleCallback){
        new Thread(()->{
            HashMap<String,String> map = new HashMap<>();
            map.put("phone_number",phone);
            String json = gson.toJson(map);
            RequestBody requestBody = RequestBody.create(JSON,json);
            Request request = new Request.Builder()
                    .url(url)
                    .addHeader("Authorization",token)
                    .post(requestBody)
                    .build();
            okHttpClient.newCall(request).enqueue(addRoleCallback);
        }).start();
    }
    public static void editUser(final URL url,final String token,final String nickname,final String signature,final Callback editUserCallback){
        new Thread(()->{
            HashMap<String,String> map = new HashMap<>();
            map.put("nickname",nickname);
            map.put("signature",signature);
            String json = gson.toJson(map);
            RequestBody requestBody = RequestBody.create(JSON,json);
            Request request = new Request.Builder()
                    .url(url)
                    .addHeader("Authorization",token)
                    .post(requestBody)
                    .build();
            okHttpClient.newCall(request).enqueue(editUserCallback);
        }).start();
    }
    public static void rolePhonePush(final URL url,final String[] phones,final Callback rolePhoneCallback ){

        for(int i = 0;i<phones.length;i++){
            HashMap<String,String> map = new HashMap<>();
            map.put("phone",phones[i]);
            pushRequest(url,map,rolePhoneCallback);
        }
    }
    public static void searchRole(final URL url,final String content,final Callback searchCallback){
        HashMap<String,String> map = new HashMap<>();
        map.put("search",content);
        pushRequest(url,map,searchCallback);
    }
    //    public static void addRole(final URL url,final String rolephone,final Callback addRoleCallback){
//        HashMap<String,String > map = new HashMap<>();
//        map.put("rolephone",rolephone);
//        pushRequest(url,map,addRoleCallback);
//    }
    public static void downloadImage(Context context,Response response,String avatar) {
        new Thread(()-> {
            InputStream is = null;
            byte[] bytes = new byte[2048];
            int len = 0;
            FileOutputStream fos = null;
            is = response.body().byteStream();
            Log.d("image", is.toString());
            long total = response.body().contentLength();
            File downloadFile = new File(context.getExternalCacheDir(), "avatar");
            if (!downloadFile.exists()) {
                downloadFile.mkdir();
            }
            String savePath = downloadFile.getAbsolutePath();
            Log.d("path", savePath);
            File file = new File(savePath, avatar);
            if (!file.exists()) {
                try {
                    file.createNewFile();
                    try {
                        fos = new FileOutputStream(file);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    long sum = 0;
                    while ((len = is.read(bytes)) != -1) {
                        fos.write(bytes, 0, len);
                        sum += len;
                        int progress = (int) (sum * 1.0f / total * 100);
                    }
                    fos.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

        }).start();

    }
}