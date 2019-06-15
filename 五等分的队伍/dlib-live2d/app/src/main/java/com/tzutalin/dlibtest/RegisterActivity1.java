package com.tzutalin.dlibtest;

import android.os.Bundle;
//import android.support.design.button.MaterialButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.Response;

public class RegisterActivity1 extends AppCompatActivity {
    private Button getyanzhengmaButton,putyanzhengmaButton,setpasswordButton;
    private EditText putphoneEdit,putyanzhengmaEdit,usernameEdit,fristpasswordEdit,secondpasswordEdit;
    private TextView textView1,textView2,textView3,textView_phone;
    private TextView errorpassword,errorname;
    private String phonenumber;
    private String password;
    private String yanzhengma=null;

    private static Gson gson =new Gson();
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register1);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar3);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);



        errorname= (TextView) findViewById(R.id.error_name_register);
        errorpassword= (TextView) findViewById(R.id.error_password_register);
        textView1= (TextView) findViewById(R.id.left_register);
        textView2= (TextView) findViewById(R.id.middle_register);
        textView3= (TextView) findViewById(R.id.right_register);
        textView_phone= (TextView) findViewById(R.id.phone_number_text);
        getyanzhengmaButton= (Button) findViewById(R.id.get_yanzhengma_register_button);
        putyanzhengmaButton= (Button) findViewById(R.id.put_yanzhengma_register_button);
        setpasswordButton= (Button) findViewById(R.id.put_password_register_button);
        putphoneEdit= (EditText) findViewById(R.id.input_phone_number_register);
        putyanzhengmaEdit= (EditText) findViewById(R.id.input_yanzhengma_register);
        usernameEdit= (EditText) findViewById(R.id.username_register);
        fristpasswordEdit= (EditText) findViewById(R.id.frist_password_register);
        secondpasswordEdit= (EditText) findViewById(R.id.second_password_register);

        putphoneEdit.setVisibility(View.VISIBLE);
        getyanzhengmaButton.setVisibility(View.VISIBLE);
        errorname.setVisibility(View.GONE);
        errorpassword.setVisibility(View.GONE);
        usernameEdit.setVisibility(View.GONE);
        fristpasswordEdit.setVisibility(View.GONE);
        secondpasswordEdit.setVisibility(View.GONE);
        setpasswordButton.setVisibility(View.GONE);







//        getyanzhengmaButton.setVisibility(View.GONE);
//        putphoneEdit.setVisibility(View.GONE);
//        textView_phone.setVisibility(View.VISIBLE);
//        putyanzhengmaEdit.setVisibility(View.VISIBLE);
//        putyanzhengmaButton.setVisibility(View.VISIBLE);
//        textView_phone.setText(null);
//        textView1.setTextColor(getResources().getColor(R.color.colorPrimary));
//        textView2.setTextColor(getResources().getColor(R.color.colorAccent));

        getyanzhengmaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                {
                    try {
                        URL url = new URL("http://125.124.155.138:3000/user/getsms");
                        phonenumber=putphoneEdit.getText().toString();
                        String regex = "^1([38][0-9]|4[579]|5[0-3,5-9]|6[6]|7[0135678]|9[89])\\d{8}$";
                        Pattern pattern = Pattern.compile(regex);
                        Matcher matcher = pattern.matcher(phonenumber);
                        if(!matcher.matches()){
                            Toast.makeText(RegisterActivity1.this,"error phone number!",Toast.LENGTH_SHORT).show();
                        }else {
                            Link.phoneDataPush(url, phonenumber, new Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {

                                }

                                @Override
                                public void onResponse(Call call, Response response) throws IOException {
                                    if (response.isSuccessful()){
                                        try {
                                            JSONObject jsonObject =new JSONObject(response.body().string());
                                            int isphonePushSccessful = jsonObject.getInt("code");
                                            if(isphonePushSccessful==200){
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {

                                                        getyanzhengmaButton.setVisibility(View.GONE);
                                                        putphoneEdit.setVisibility(View.GONE);
                                                        textView_phone.setVisibility(View.VISIBLE);
                                                        putyanzhengmaEdit.setVisibility(View.VISIBLE);
                                                        putyanzhengmaButton.setVisibility(View.VISIBLE);
                                                        textView_phone.setText("验证码已发送至"+phonenumber.substring(0,3)+"****"+phonenumber.substring(8));
                                                        textView1.setTextColor(getResources().getColor(R.color.colorPrimary));
                                                        textView2.setTextColor(getResources().getColor(R.color.colorAccent));
                                                    }
                                                });
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                    }
                                }
                            });
                        }
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                }

            }
        });
        putyanzhengmaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                URL url;
                try {
                    url = new URL("http://125.124.155.138:3000/user/validateSms");
                    String yanzhengma_input = putyanzhengmaEdit.getText().toString();
                    Link.yanzhengmaDatePush(url, phonenumber, yanzhengma_input, new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {

                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            try {

                                JSONObject jsonObject =new JSONObject(response.body().string());

                                int isphonePushSccessful = jsonObject.getInt("code");
                                String message = jsonObject.getString("msg");
                                Log.d("yanzhengma", String.valueOf(isphonePushSccessful));
                                Log.d("message",message);
                                if(!(isphonePushSccessful==200)){
                                    runOnUiThread(
                                            new Runnable() {
                                                @Override
                                                public void run() {
                                                    Toast.makeText(RegisterActivity1.this,"error "+yanzhengma_input,Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                    );
                                }else {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            textView_phone.setVisibility(View.GONE);
                                            putyanzhengmaEdit.setVisibility(View.GONE);
                                            putyanzhengmaButton.setVisibility(View.GONE);
                                            errorname.setVisibility(View.VISIBLE);
                                            errorpassword.setVisibility(View.VISIBLE);
                                            usernameEdit.setVisibility(View.VISIBLE);
                                            fristpasswordEdit.setVisibility(View.VISIBLE);
                                            secondpasswordEdit.setVisibility(View.VISIBLE);
                                            setpasswordButton.setVisibility(View.VISIBLE);
                                            textView2.setTextColor(getResources().getColor(R.color.colorPrimary));
                                            textView3.setTextColor(getResources().getColor(R.color.colorAccent));
                                        }
                                    });

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    });
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }

            }
        });
        usernameEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (usernameEdit.getText().toString().length()>8){
                    errorname.setText("昵称长度不可超过8位");
                }else {
                    errorname.setText("");
                }
            }
        });

        setpasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameEdit.getText().toString();
                String password1=fristpasswordEdit.getText().toString();
                String password2=secondpasswordEdit.getText().toString();
                if(password1.length()>=8&&password1.length()<=16){
                    if(password1.equals(password2)){
                        password=password1;
                        URL url = null ;
                        try {
                            url = new URL("http://125.124.155.138:3000/user/register");
                            Link.passwordDataPush(url, phonenumber,password, new Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {

                                }

                                @Override
                                public void onResponse(Call call, Response response) throws IOException {
                                    if (response.isSuccessful()){
                                        try {

                                            JSONObject jsonObject =new JSONObject(response.body().string());
                                            int isphonePushSccessful = jsonObject.getInt("code");
                                            String message = jsonObject.getString("msg");
                                            if(isphonePushSccessful==200){
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Toast.makeText(RegisterActivity1.this,"Welcome wo 2D-Chat",Toast.LENGTH_SHORT).show();
                                                        LoginActivity.activityStart(RegisterActivity1.this);
                                                    }
                                                });
                                            }else {
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Toast.makeText(RegisterActivity1.this,message,Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            }

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }

                                    }
                                }
                            });
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        }


                    }else {
                        errorpassword.setText("两次密码不一致");
                        Toast.makeText(RegisterActivity1.this,"password mistake!",Toast.LENGTH_SHORT).show();
                    }
                }else {
                    errorpassword.setText("密码需在8-16位之间");
                    Toast.makeText(RegisterActivity1.this,"password is too short",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

}
