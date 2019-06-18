package com.example.hqb98.mj.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import java.util.ArrayList;
import java.util.List;

public class Date_item_fragment_one extends LazyFragment {
    public static final String TAG = "X123";
    public RecyclerView recyclerView;
    public List<Date> dateList = new ArrayList<>();
    private LinearLayoutManager linearLayoutManager;
    public  DateOneAdapter adapter = new DateOneAdapter(dateList);;
    public ImageView kong_image;
    private RefreshLayout refreshLayout;

    @Override
    protected View getPreviewLayout(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.layout_preview,container,false);
    }

    @Override
    protected void onCreateViewLazy(Bundle savedInstanceState) {
        super.onCreateViewLazy(savedInstanceState);
        setContentView(R.layout.fragment_date_item);
        EventBus.getDefault().register(this);
        initView();
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

    @Subscribe(threadMode =  ThreadMode.MAIN)
    public void initData(EventMesage mesage){
        switch (mesage.getNum()){
            case 1:
                refreshData();
                break;
            case 2:
                refreshData();
                break;
        }

    }

    private void refreshData(){
        dateList.clear();
        for (int i = 0; i<DateFragment.dateList.size(); i++){
            if (DateFragment.dateList.get(i).getDate_type().equals("记录心情")){
                dateList.add(DateFragment.dateList.get(i));
            }
        }
        checkLength();
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onResumeLazy() {
        super.onResumeLazy();
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
