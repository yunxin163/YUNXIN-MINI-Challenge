package com.example.hqb98.mj.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.hqb98.mj.R;
import com.example.hqb98.mj.data.Date;
import com.example.hqb98.mj.data.DateOneAdapter;
import com.example.hqb98.mj.data.EventMesage;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.shizhefei.fragment.LazyFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

public class Date_item_fragment_three extends LazyFragment {
    private View view;
    public RecyclerView recyclerView;
    public List<Date> dateList = new ArrayList<>();
    private LinearLayoutManager linearLayoutManager;
    public DateOneAdapter adapter = new DateOneAdapter(dateList);;
    public ImageView kong_image;
    private RefreshLayout refreshLayout;

    @Override
    protected void onCreateViewLazy(Bundle savedInstanceState) {
        super.onCreateViewLazy(savedInstanceState);
        setContentView(R.layout.fragment_date_item);
        EventBus.getDefault().register(this);
        initView();
        initData();
    }

    private void initView() {
        recyclerView = (RecyclerView)findViewById(R.id.date_item1_recycle);
        linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
        kong_image = (ImageView)findViewById(R.id.date_kongkongruye);
        refreshLayout = (RefreshLayout)findViewById(R.id.date_refresh);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                EventBus.getDefault().post(new EventMesage(2));
                refreshLayout.finishRefresh();//传入false表示刷新失败
            }
        });

    }



    private void initData(){
        refreshData();
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void initData(EventMesage mesage){
        switch (mesage.getNum()){
            case 1:
                refreshData();
                break;
        }

    }

    public void refreshData(){
        dateList.clear();
        for (int i = 0; i<DateFragment.dateList.size(); i++){
            if (DateFragment.dateList.get(i).getDate_type().equals("课堂笔记")){
                dateList.add(DateFragment.dateList.get(i));
            }
        }
        checkLength();
        adapter.notifyDataSetChanged();
    }

    public void checkLength(){
        if (dateList.size()==0){
            kong_image.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }else {
            kong_image.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onDestroyViewLazy() {
        super.onDestroyViewLazy();
        EventBus.getDefault().unregister(this);
    }
}
