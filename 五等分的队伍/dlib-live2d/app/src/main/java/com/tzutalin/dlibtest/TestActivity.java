package com.tzutalin.dlibtest;

import android.annotation.SuppressLint;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TestActivity extends AppCompatActivity {
    private static final String TAG = "TestTest";
    EditText meText,targetText;
    @SuppressLint("StaticFieldLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        Button loginbtn = findViewById(R.id.button);
        Button talkbtn = findViewById(R.id.button2);
        Button endbtn = findViewById(R.id.button3);

        meText = findViewById(R.id.editText);
        targetText = findViewById(R.id.editText2);

        LandmarkManager manager =  LandmarkManager.getInstance();
        LandmarkProcessor processor = new LandmarkProcessor();
        processor.start(null);
        loginbtn.setOnClickListener((a)->{
            manager.circle(meText.getText().toString(),processor);
        });
        talkbtn.setOnClickListener(a->{
            manager.activeChat(targetText.getText().toString());
        });
        endbtn.setOnClickListener(a->{
            manager.endChat();
        });
        try {
            //Log.i(TAG, "onCreate: "+JSONWrapper.getInstance().endChatJSON());
            //Log.i(TAG, "onCreate: "+JSONWrapper.getInstance().loginJSON("shaoyy"));
            //Log.i(TAG, "onCreate: "+JSONWrapper.getInstance().packVertJSON(new Live2DFrameData(1,2,3,4,5,6,7)));
            //Log.i(TAG, "onCreate: "+JSONWrapper.getInstance().startChatJSON("shaoyy"));



            //manager.pipeline("123456789","234567891",processor);

            ArrayList<Point> testVert = new ArrayList<>();
            for(int i=0;i<70;i++){
                testVert.add(new Point(0,0));
            }
            new AsyncTask<Void,Void,Void>(){

                @Override
                protected Void doInBackground(Void... voids) {
                    while(true){
                        try{
                            Thread.sleep(3000);
                            processor.updateLandmark(testVert);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }
            }.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
