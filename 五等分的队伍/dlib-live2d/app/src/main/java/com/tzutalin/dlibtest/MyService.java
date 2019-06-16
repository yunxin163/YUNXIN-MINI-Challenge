package com.tzutalin.dlibtest;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class MyService extends Service {
    private Socket socket ;
    private BufferedReader in;
    private PrintWriter out;
    private static String HOST =null;
    private static int PORT =0;
    public static final int CALL2 = 0x233333;
    public static Role role=null;
    public static Handler handler= MainActivity.handler;
    public static final int NEW_FRIEND=1;
    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent){
        return null;
    }
    @Override
    public void onCreate() {
        super.onCreate();

        try {
            socket = new Socket(HOST, PORT);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
            out = new PrintWriter(new BufferedWriter(new OutputStreamWriter( socket.getOutputStream())), true);
            while (true){
                String content=null;
                if(socket.isConnected()){
                    if(!socket.isInputShutdown()){
                        if((content=in.readLine())!=null){
                            role = new Gson().fromJson(content, Role.class);
                            handler.sendEmptyMessage(NEW_FRIEND);
                        }
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


}
