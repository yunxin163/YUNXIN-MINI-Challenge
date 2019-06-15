package com.tzutalin.dlibtest;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
//import android.support.design.button.MaterialButton;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private Button loginButton;
    private TextView registerButton,forgetButton;
    private EditText phoneEdit,passwordEidt;
    public static Mine mine = Mine.getInstance();
    private static Gson gson=new Gson();
    private static final URL url = null;
    public String token;
    private Role role;
    private List<Role> roles = new ArrayList<>();
    public static void activityStart(Context context){
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        phoneEdit= (EditText) findViewById(R.id.edit_phone);
        passwordEidt= (EditText) findViewById(R.id.edit_password);
        loginButton= (Button) findViewById(R.id.login_button);
        loginButton.setOnClickListener(this);
        registerButton= (TextView) findViewById(R.id.phone_register);
        registerButton.setOnClickListener(this);
        forgetButton= (TextView) findViewById(R.id.forget_password);
        forgetButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.login_button:
                /*
                根据用户名在数据库在数据库中进行查找，判断是否有该用户，继而再判断密码是否正确
                */
                String phone =phoneEdit.getText().toString();
                String password =passwordEidt.getText().toString();
                String regex = "^1([38][0-9]|4[579]|5[0-3,5-9]|6[6]|7[0135678]|9[89])\\d{8}$";
                Pattern pattern = Pattern.compile(regex);
                final Matcher matcher =pattern.matcher(phone);
                if(matcher.matches()) {
                    URL url ;
                    try {
                        url = new URL("http://125.124.155.138:3000/user/login");
                        Link.login(url, phone, password, new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {

                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                if (response.isSuccessful()) {

                                    try {
                                        JSONObject jsonObject = new JSONObject(response.body().string());
                                        int code =jsonObject.getInt("code");
                                        if(code==200){
                                            JSONObject dataObject = jsonObject.getJSONObject("data");
                                            token = dataObject.getString("token");
                                            token = "bearer "+token;
                                            mine.setToken(token);
                                            Log.d("token",token);
                                            URL url1 = new URL("http://125.124.155.138:3000/user/getInfo");
                                            Link.getInfo(url1, token, new Callback() {
                                                @Override
                                                public void onFailure(Call call, IOException e) {

                                                }

                                                @Override
                                                public void onResponse(Call call, Response response) throws IOException {
                                                    if (response.isSuccessful()) {
                                                        try {
                                                            JSONObject jsonObject1 = new JSONObject(response.body().string());
                                                            int code = jsonObject1.getInt("code");
                                                            // Log.d("message",jsonObject1.getString("msg"));
                                                            if (code == 200){
                                                                JSONObject infoDate = jsonObject1.getJSONObject("data");
                                                                String nickname = infoDate.getString("nickname");
                                                                String phone_number = infoDate.getString("phone_number");
                                                                String signature = infoDate.getString("signature");
                                                                String avatar = infoDate.getString("avatar");
                                                                mine.setName(nickname);
                                                                mine.setPhone(phone_number);
                                                                mine.setSignature(signature);
                                                                mine.setImage(avatar);
                                                                URL getroleUrl = new URL("http://125.124.155.138:3000/user/getFriends");
                                                                Link.getRole(getroleUrl,token,new Callback(){

                                                                    @Override
                                                                    public void onFailure(Call call, IOException e) {

                                                                    }

                                                                    @Override
                                                                    public void onResponse(Call call, Response response) throws IOException {
                                                                        try {
                                                                            JSONObject jsonObject2 = new JSONObject(response.body().string());
                                                                            if(jsonObject2.getInt("code")==200){
                                                                                JSONArray jsonArray = jsonObject2.getJSONArray("data");
                                                                                for(int i =0;i<jsonArray.length();i++){
                                                                                    JSONObject roleData = jsonArray.getJSONObject(i);
                                                                                    String phone = roleData.getString("phone_number");
                                                                                    String nickname = roleData.getString("nickname");
                                                                                    String avatar = roleData.getString("avatar");
                                                                                    Log.d("message",phone);
                                                                                    role = new Role(phone,avatar,nickname);
                                                                                    roles.add(role);
                                                                                    URL imageUrl = new URL("http://125.124.155.138:3000/avatar/" + avatar);
                                                                                    Log.d("image",avatar);
                                                                                    Link.getImage(imageUrl, new Callback() {
                                                                                        @Override
                                                                                        public void onFailure(Call call, IOException e) {

                                                                                        }

                                                                                        @Override
                                                                                        public void onResponse(Call call, Response response) throws IOException {
                                                                                            Link.downloadImage(LoginActivity.this,response,avatar);
                                                                                        }
                                                                                    });
                                                                                }

                                                                            }else {
                                                                                runOnUiThread(new Runnable() {
                                                                                    @Override
                                                                                    public void run() {
                                                                                        try {
                                                                                            Toast.makeText(LoginActivity.this,jsonObject2.getString("msg"),Toast.LENGTH_SHORT).show();
                                                                                        } catch (JSONException e) {
                                                                                            e.printStackTrace();
                                                                                        }
                                                                                    }
                                                                                });
                                                                            }
                                                                        } catch (JSONException e) {
                                                                            e.printStackTrace();
                                                                        }

                                                                    }
                                                                });
                                                                URL imageUrl = new URL("http://125.124.155.138:3000/avatar/" + avatar);
                                                                Log.d("image",avatar);
                                                                Link.getImage(imageUrl, new Callback() {
                                                                    @Override
                                                                    public void onFailure(Call call, IOException e) {

                                                                    }

                                                                    @Override
                                                                    public void onResponse(Call call, Response response) throws IOException {
                                                                        Link.downloadImage(LoginActivity.this,response,avatar);
                                                                    }
                                                                });
                                                                runOnUiThread(new Runnable() {
                                                                    @Override
                                                                    public void run() {
//                                                                        Intent intent = new Intent(LoginActivity.this, Service.class);
//                                                                        startService(intent);
                                                                        MainActivity.activityStart(LoginActivity.this,mine,roles);
                                                                    }
                                                                });
                                                            }
                                                        } catch(JSONException e){
                                                            e.printStackTrace();
                                                        }


                                                    }

                                                }

                                            });
                                        }else {
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Toast.makeText(LoginActivity.this,"Error in account or password",Toast.LENGTH_SHORT).show();
                                                }
                                            });

                                        }

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }



                                }
                            }
                        });
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }


                }else {
                    Toast.makeText(LoginActivity.this,"Please check whether the cell phone number is correct or not.",Toast.LENGTH_SHORT).show();
                }
//                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                startActivity(intent);
                break;
            case R.id.phone_register:
                Intent intent1 = new Intent(LoginActivity.this, RegisterActivity1.class);
                startActivity(intent1);
                break;
            case R.id.forget_password:
                Intent intent2 = new Intent(LoginActivity.this, ForgetActivity.class);
                startActivity(intent2);
                break;
            default:
                break;
        }
    }

}
