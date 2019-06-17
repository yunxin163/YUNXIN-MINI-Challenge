package com.tzutalin.dlibtest;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class AddFriendActivity extends AppCompatActivity {
    private EditText editText ;
    public static URL url=null;
    private List<Role> roleList=new ArrayList<>();
    private RecyclerView recyclerView ;
    //    private SearchRoleAdapter searchRoleAdapter;
    public static Role role;
    private Mine mine =Mine.getInstance();
    private boolean isAdd;
    private String content;
    public static void activityStart(Context context){
        Intent intent = new Intent(context, AddFriendActivity.class);
        context.startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar4);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        editText = (EditText) findViewById(R.id.search_friend_edit);
        recyclerView= (RecyclerView) findViewById(R.id.search_friend_recycler);
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
//        recyclerView.setLayoutManager(linearLayoutManager);
//        searchRoleAdapter = new SearchRoleAdapter(this,roleList);
//        recyclerView.setAdapter(searchRoleAdapter);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                content = s.toString();
                if(content.contains("\r")||content.contains("\n")){
                    content = content.replace("\r","").replace("\n","");
                    if(!"".equals(content)){
                        try {
                            URL url = new URL("http://125.124.155.138:3000/user/addFriend");
                            Link.addRole(url, mine.getToken(), content, new Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {

                                }

                                @Override
                                public void onResponse(Call call, Response response) throws IOException {
                                    try {
                                        JSONObject jsonObject = new JSONObject(response.body().string());
                                        if(jsonObject.getInt("code")==200){
                                            JSONObject roleData= jsonObject.getJSONObject("data");
                                            String phone = roleData.getString("phone_number");
                                            String nickname = roleData.getString("nickname");
                                            String avatar = roleData.getString("avatar");
                                            role = new Role(phone,avatar,nickname);
                                            Link.downloadImage(AddFriendActivity.this,response,avatar);
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Toast.makeText(AddFriendActivity.this,"添加成功",Toast.LENGTH_SHORT).show();
                                                    editText.setText("");
                                                    MainActivity.activityStart(AddFriendActivity.this,role);
                                                }
                                            });

                                        }else {
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    try {
                                                        Toast.makeText(AddFriendActivity.this,jsonObject.getString("msg"),Toast.LENGTH_SHORT).show();
                                                        editText.setText("");
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
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        }


                    }
                }
            }
        });
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
            default:
                return true;
        }
    }

}
