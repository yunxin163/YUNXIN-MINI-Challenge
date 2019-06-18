package com.example.hqb98.mj.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.v4.content.LocalBroadcastManager;

import com.example.hqb98.mj.util.HttpUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class AlarmService extends Service {
    public AlarmManager manager;
    public PendingIntent pi;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        final Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                HttpUtil.getSensorRequest(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String responseData = response.body().string();
                        try {
                            JSONObject jsonObject = new JSONObject(responseData);
                            final String temperature = jsonObject.getString("temperature");
                            final String humidity = jsonObject.getString("humidity");
                            final String smoke = jsonObject.getString("smoke");
                            if (smoke.equals("dangerous")){
                                Intent intent1 = new Intent("com.example.hqb98.servicebroadcast");
                                LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(getApplicationContext());
                                localBroadcastManager.sendBroadcast(intent1);
                                stopSelf();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
            }
        });
        thread.start();
        manager = (AlarmManager)getSystemService(ALARM_SERVICE);
        int gap = 5*1000;
        long triggerAtTime = SystemClock.elapsedRealtime()+gap;
        Intent i = new Intent(this ,AlarmService.class);
        pi = PendingIntent.getService(this,0,i,0);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,triggerAtTime,pi);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        manager.cancel(pi);
        stopSelf();

    }
}
