package com.tzutalin.dlibtest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

public class TalkViewActivity extends AppCompatActivity implements View.OnClickListener {
    private RecyclerView recyclerView;
    private List<Msg> msgList = new ArrayList<>();
    private EditText editText;
    private MsgAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_talk_view);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);


        Intent intent =getIntent();
        int ID =  intent.getIntExtra("imageID",0);
        recyclerView = (RecyclerView) findViewById(R.id.msg_recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        msgList.add(new Msg(ID,"好嗨呀", Msg.RECEIVED_MASSAGE));
        adapter = new MsgAdapter(msgList);
        recyclerView.setAdapter(adapter);
        editText = (EditText) findViewById(R.id.sendMsg_edit);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String content = s.toString();
                if(content.contains("\r")||content.contains("\n")){
                    content.replace("\r","").replace("\n","");
                    if(!"".equals(content)){
                        Msg msg = new Msg(0,content, Msg.SEND_MASSAGE);
                        msgList.add(msg);
                        adapter.notifyItemInserted(msgList.size()-1);
                        recyclerView.scrollToPosition(msgList.size()-1);
                        editText.setText("");
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }
    }
}
