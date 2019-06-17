package com.tzutalin.dlibtest;

import android.os.Bundle;
//import android.support.design.button.MaterialButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ForgetActivity extends AppCompatActivity {
    private View getyanzhengmaButton;
    private View putyanzhengmaButton;
    private View setpasswordButton;
    private EditText forgetphoneEdit,putyanzhengmaEdit,fristpasswordEdit,secondpasswordEdit;
    private TextView textView1,textView2,textView3,textView_phone;
    private LinearLayout linearLayout;
    private String password=null;
    private String phonenumber=null;
    private String yanzhengma=null;
    private URL url =null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar3);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);



        linearLayout= (LinearLayout) findViewById(R.id.forget_password_linearlayout);
        textView1= (TextView) findViewById(R.id.left_register);
        textView2= (TextView) findViewById(R.id.middle_register);
        textView3= (TextView) findViewById(R.id.right_register);
        textView_phone= (TextView) findViewById(R.id.phone_number_text);
        getyanzhengmaButton=findViewById(R.id.get_yanzhengma_register_button);
        putyanzhengmaButton=findViewById(R.id.put_yanzhengma_register_button);
        setpasswordButton=findViewById(R.id.put_password_register_button);
        forgetphoneEdit= (EditText) findViewById(R.id.forget_phone_edit);
        putyanzhengmaEdit= (EditText) findViewById(R.id.input_yanzhengma_register);
        fristpasswordEdit= (EditText) findViewById(R.id.frist_password_register);
        secondpasswordEdit= (EditText) findViewById(R.id.second_password_register);
        getyanzhengmaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phonenumber=forgetphoneEdit.getText().toString();
                Link.phoneDataPush(url, phonenumber, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {

                        if (response.isSuccessful()){
                            JSONArray jsonArray = null;
                            try {
                                jsonArray = new JSONArray(response.body().string());
                                JSONObject jsonObject =jsonArray.getJSONObject(0);
                                yanzhengma = jsonObject.getString("yanzhengma");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    getyanzhengmaButton.setVisibility(View.GONE);
                                    linearLayout.setVisibility(View.GONE);
                                    textView_phone.setVisibility(View.VISIBLE);
                                    putyanzhengmaEdit.setVisibility(View.VISIBLE);
                                    putyanzhengmaButton.setVisibility(View.VISIBLE);
                                    textView_phone.setText("验证码已发送至");
                                    textView1.setTextColor(getResources().getColor(R.color.colorPrimary));
                                    textView2.setTextColor(getResources().getColor(R.color.colorAccent));
                                }
                            });
                        }
                    }
                });


            }
        });
        putyanzhengmaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String yanzhengma_input = putyanzhengmaEdit.getText().toString();
                if(!yanzhengma_input.equals(yanzhengma)){
                    Toast.makeText(ForgetActivity.this,"error "+yanzhengma_input,Toast.LENGTH_SHORT).show();
                }else {
                    textView_phone.setVisibility(View.GONE);
                    putyanzhengmaEdit.setVisibility(View.GONE);
                    putyanzhengmaButton.setVisibility(View.GONE);
                    fristpasswordEdit.setVisibility(View.VISIBLE);
                    secondpasswordEdit.setVisibility(View.VISIBLE);
                    setpasswordButton.setVisibility(View.VISIBLE);
                    textView2.setTextColor(getResources().getColor(R.color.colorPrimary));
                    textView3.setTextColor(getResources().getColor(R.color.colorAccent));
                }
            }
        });
        setpasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password1=fristpasswordEdit.getText().toString();
                String password2=secondpasswordEdit.getText().toString();
                if(password1.length()>=6&&password1.length()<=16){
                    if(password1.equals(password2)){
                        password=password1;
                        /*
                        将密码传入数据库里，修改用户个人信息
                         */
                        Link.passwordChange(url, password, new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {

                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                if(response.isSuccessful()){
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(ForgetActivity.this,"successful!",Toast.LENGTH_SHORT).show();
                                            LoginActivity.activityStart(ForgetActivity.this);
                                        }
                                    });

                                }
                            }
                        });
                    }else {
                        Toast.makeText(ForgetActivity.this,"password mistake!",Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(ForgetActivity.this,"password is too short",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
