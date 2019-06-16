/*
 * Copyright 2016-present Tzutalin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.tzutalin.dlibtest;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.view.View;

import android.util.Log;

import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.support.v4.content.ContextCompat;

import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.avchat.AVChatCallback;
import com.netease.nimlib.sdk.avchat.AVChatManager;
import com.netease.nimlib.sdk.avchat.model.AVChatCommonEvent;
import com.tzutalin.dlib.Constants;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by darrenl on 2016/5/20.
 */
public class CameraActivity extends Activity {
    private static final String TAG = "CameraActivity";
    private static int OVERLAY_PERMISSION_REQ_CODE = 1;
    public static String PASSIVE = "yoyoko saiko!";
    private String targetNumber;
    private LandmarkManager manager;
    private ImageView imageView;
    @SuppressLint("StaticFieldLeak")
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        manager = LandmarkManager.getInstance();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_camera);

        imageView=findViewById(R.id.back_image);
        imageView.setOnClickListener(v -> endChat());
        List<String> permissionList = new ArrayList<>();
        if(ContextCompat.checkSelfPermission(CameraActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if(ContextCompat.checkSelfPermission(CameraActivity.this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.CAMERA);
        }
        if(!permissionList.isEmpty()){
            String[] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(CameraActivity.this, permissions, 1);
        }else{
            startCamera();
        }
        if (null == savedInstanceState) {
            getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, CameraConnectionFragment.newInstance())
                    .commit();
        }
        if(!new File(Constants.getFaceShapeModelPath()).exists()){
            FileUtils.copyFileFromAssetsToOthers(getApplicationContext(),"shape_predictor_68_face_landmarks.dat", Constants.getFaceShapeModelPath());
            /*new AsyncTask<Void,Void,Void>(){

                @Override
                protected Void doInBackground(Void... voids) {
                    FileUtils.copyFileFromAssetsToOthers(getApplicationContext(),"shape_predictor_68_face_landmarks.dat", Constants.getFaceShapeModelPath());
                    return null;
                }
            }.execute();*/
        }
        registerAVChatHandUpObserver(true);
    }

    private void startCamera(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this.getApplicationContext())) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, OVERLAY_PERMISSION_REQ_CODE);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == OVERLAY_PERMISSION_REQ_CODE) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!Settings.canDrawOverlays(this.getApplicationContext())) {
                    Toast.makeText(CameraActivity.this, "CameraActivity\", \"SYSTEM_ALERT_WINDOW, permission not granted...", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = getIntent();
                    finish();
                    startActivity(intent);
                }
            }
        }
    }
    private void registerAVChatHandUpObserver(boolean register){

        Observer<AVChatCommonEvent> callHangupObserver = (Observer<AVChatCommonEvent>) hangUpInfo -> {
            // 结束通话
            AVChatManager.getInstance().disableRtc();
            CameraActivity.this.finish();
        };
        AVChatManager.getInstance().observeHangUpNotification(callHangupObserver, register);
    }
    private void endChat(){
        AVChatManager avChatManager = AVChatManager.getInstance();
        long chatId = avChatManager.getCurrentChatId();
        avChatManager.hangUp2(chatId, new AVChatCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                avChatManager.disableRtc();
                Log.i(TAG, "onSuccess: endChat");
            }

            @Override
            public void onFailed(int i) {
                avChatManager.disableRtc();
                Log.i(TAG, "onFailed: endChat");
            }

            @Override
            public void onException(Throwable throwable) {
                avChatManager.disableRtc();
                Log.i(TAG, "onException: endChat");
            }
        });
        this.finish();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        manager.reset();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch(requestCode){
            case 1:
                if(grantResults.length > 0){
                    for(int result : grantResults){
                        if(result != PackageManager.PERMISSION_GRANTED){
                            Toast.makeText(this, "必须同意所有权限才能使用本程序", Toast.LENGTH_LONG).show();
                            finish();
                            return ;
                        }
                    }
                    startCamera();
                }else{
                    Toast.makeText(this, "发生未知错误", Toast.LENGTH_LONG).show();
                    finish();
                }
                break;
            default:
                break;
        }
    }
}