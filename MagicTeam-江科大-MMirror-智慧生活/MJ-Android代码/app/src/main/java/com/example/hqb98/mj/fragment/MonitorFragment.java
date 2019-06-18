package com.example.hqb98.mj.fragment;

import android.Manifest;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hqb98.mj.R;
import com.example.hqb98.mj.data.Picture;
import com.example.hqb98.mj.data.PictureAdapter;
import com.example.hqb98.mj.data.Sensor;
import com.example.hqb98.mj.data.SensorAdapter;
import com.example.hqb98.mj.service.AlarmService;
import com.example.hqb98.mj.util.GlobalUtil;
import com.example.hqb98.mj.util.HttpUtil;
import com.example.hqb98.mj.util.SharedUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.LitePal;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.zip.Inflater;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static org.litepal.LitePal.findAll;

public class MonitorFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "Ax123";
    private View view;
    private FloatingActionButton floatingActionButton;
    private RecyclerView imageRecycle;
    public static List<Picture> pictureList = new ArrayList<>();
    private PictureAdapter pictureAdapter;
    private TextView temperaturetv;
    private TextView humiditytv;
    private TextView smoketv;
    private List<Sensor> sensorList = new ArrayList<>();
    private SensorAdapter sensorAdapter;
    private Toolbar toolbar;
    private ServiceRecever serviceRecever;
    private LocalBroadcastManager localBroadcastManager;
    private IntentFilter intentFilter;
    public Intent intent1;
    private boolean isRefresh = true;
    private CircleImageView touxiang;
    private ImageView device_bind;
    private LinearLayout monitor_hide;
    private LinearLayout monitor_show;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.monitor_layout,container,false);
        initData();
        initView();
        initParams();
        initMTemperature();
        return view;
    }



    private void initData() {
        pictureList.clear();
        pictureList.addAll(LitePal.findAll(Picture.class));
        Collections.reverse(pictureList);
        intent1 = new Intent(getContext(),AlarmService.class);
        getData();
    }

    private void getData(){
        new Thread(new Runnable() {
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
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    temperaturetv.setText(temperature+"℃");
                                    humiditytv.setText(humidity+"%");
                                    smoketv.setText(smoke);
                                }
                            });
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
            }
        }).start();
    }

    private void initView() {
        monitor_show = (LinearLayout)view.findViewById(R.id.monitor_show);
        monitor_hide = (LinearLayout)view.findViewById(R.id.monitor_hide);
        if (!SharedUtil.read("device_bind",false)){
            monitor_hide.setVisibility(View.VISIBLE);
            monitor_show.setVisibility(View.GONE);
        }else {
            monitor_hide.setVisibility(View.GONE);
            monitor_show.setVisibility(View.VISIBLE);
        }
        floatingActionButton = (FloatingActionButton)view.findViewById(R.id.monitor_fab);
        imageRecycle = (RecyclerView)view.findViewById(R.id.monitor_recycle_image);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        imageRecycle.setLayoutManager(linearLayoutManager);
        pictureAdapter = new PictureAdapter(pictureList);
        imageRecycle.setAdapter(pictureAdapter);
        floatingActionButton.setOnClickListener(this);
        temperaturetv = (TextView)view.findViewById(R.id.monitor_temperature);
        humiditytv = (TextView)view.findViewById(R.id.monitor_humidity);
        smoketv = (TextView)view.findViewById(R.id.monitor_smoke);

        RecyclerView recyclerView = (RecyclerView)view.findViewById(R.id.monitor_recycler_temperature);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(manager);
        sensorAdapter = new SensorAdapter(sensorList);
        recyclerView.setAdapter(sensorAdapter);

        toolbar = (Toolbar)view.findViewById(R.id.toolbar3);
        AppCompatActivity appCompatActivity= (AppCompatActivity) getActivity();
        appCompatActivity.setSupportActionBar(toolbar);
        ActionBar actionBar = appCompatActivity.getSupportActionBar();

        intentFilter = new IntentFilter("com.example.hqb98.servicebroadcast");
        serviceRecever = new ServiceRecever();
        localBroadcastManager = LocalBroadcastManager.getInstance(getContext());
        localBroadcastManager.registerReceiver(serviceRecever,intentFilter);

        touxiang = (CircleImageView)view.findViewById(R.id.circle_image);

        device_bind = (ImageView)view.findViewById(R.id.monitor_bind);
        device_bind.setOnClickListener(this);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.monitor_menu,menu);
    }

    private void refreshData(){
        isRefresh = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (isRefresh){
                    try {
                        Thread.sleep(5*1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }finally {
                        getData();
                    }
                }
            }
        }).start();
    }

    private void noRefreshData(){
        isRefresh = false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.monitor_alarm:
                startAlarm();
                break;
            case R.id.monitor_noalarm:
                Toast.makeText(getContext(),"您关闭了报警装置",Toast.LENGTH_SHORT).show();
                getContext().stopService(intent1);
                break;
            case R.id.monitor_refresh:
                Toast.makeText(getContext(),"您开启了实时更新数据",Toast.LENGTH_SHORT).show();
                refreshData();
                break;
            case R.id.monitor_norefresh:
                Toast.makeText(getContext(),"您关闭了实时更新数据",Toast.LENGTH_SHORT).show();
                noRefreshData();
                break;
            case R.id.monitor_call:
                callPhone();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    private void startAlarm(){
        Toast.makeText(getContext(),"您开启了报警装置",Toast.LENGTH_SHORT).show();
        getContext().startService(intent1);
    }

    private void callPhone(){
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:119"));
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case 1:
                if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Log.d("jinlaileme","警报");
                    startAlarm();
                }else {
                    Toast.makeText(getContext(),"拒绝了权限可不能使用报警功能噢！",Toast.LENGTH_SHORT).show();
                }
                break;
            case 2:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    getPicture();
                }else {
                    Toast.makeText(getContext(),"拒绝了权限可不能使用监控功能哦！",Toast.LENGTH_SHORT).show();
                }
                break;


        }
    }

    private void initParams() {
        if (GlobalUtil.isExternalStorageWritable()){
            String path = Environment.getExternalStorageDirectory().getAbsolutePath();
            File file = new File(path,"MJ/TouXiang/touxiang.jpg");
            if (file.exists()){
                try {
                    InputStream inputStream = new FileInputStream(file);
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    touxiang.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

            }
        }

    }

    private void initMTemperature() {
        HttpUtil.getSensorAll(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(getContext(),"网络不好",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d("monitordd","wendu");
                String responseData = response.body().string();
                Gson gson = new Gson();
                List<Sensor> sensors = gson.fromJson(responseData,new TypeToken<List<Sensor>>(){}.getType());
                Collections.reverse(sensors);
                for (int i=0;i<15;i++){
                    if (sensors.size()>i&&sensors.get(i)!=null){
                        sensorList.add(sensors.get(i));
                    }
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        sensorAdapter.notifyDataSetChanged();
                    }
                });
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.monitor_fab:
                if (ContextCompat.checkSelfPermission(getContext(),Manifest.permission.WRITE_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},2);
                }else {
                    getPicture();
                }
                break;
            case R.id.monitor_bind:
                View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_kebiao_login,null);
                TextView textView = (TextView)view.findViewById(R.id.enter_title);
                textView.setText("绑定设备号");
                Button device_ok = (Button)view.findViewById(R.id.cgroup_ok) ;
                Button device_cancle = (Button)view.findViewById(R.id.cgroup_cancel);
                final EditText device_number = (EditText)view.findViewById(R.id.cgroup_name);
                final EditText device_password = (EditText)view.findViewById(R.id.cgroup_password);
                final AlertDialog alertDialog;
                AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                alertDialog = dialog.setView(view).create();
                alertDialog.show();
                device_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (device_number.getText().toString().equals("100000")&&device_password.getText().toString().equals("123456")){
                            SharedUtil.save("device_bind",true);
                            monitor_show.setVisibility(View.VISIBLE);
                            monitor_hide.setVisibility(View.GONE);
                            alertDialog.dismiss();

                        }else {
                            Toast.makeText(getContext(),"设备号或密码错误",Toast.LENGTH_SHORT).show();
                            SharedUtil.save("device_bind",false);
                        }

                    }
                });
                device_cancle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });

                break;

        }

    }


    private void getPicture(){
        Request request = new Request.Builder().url("http://101.132.169.177/testPic/upfiles_resize/1.jpg").build();
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG,"获取图片错误"+e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                byte[] bytes = response.body().bytes();
                Bitmap bitmap1 = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                String filepath = Environment.getExternalStorageDirectory().getAbsolutePath();
                File file = new File(filepath,"MJ/MonitorPitcture");
                if (!file.exists()){
                    file.mkdirs();
                }
                long systemTime = System.currentTimeMillis();
                String filename = "tx"+systemTime+".jpg";
                File file1 = new File(file,filename);
                file1.createNewFile();
                FileOutputStream outputStream = new FileOutputStream(file1);
                bitmap1.compress(Bitmap.CompressFormat.JPEG,50,outputStream);
                outputStream.flush();
                outputStream.close();
                bitmap1.recycle();
                if (file1.length()!=0){
                    Picture picture = new Picture();
                    picture.setUrl(file1.getPath());
                    picture.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(systemTime)));
                    picture.save();
                    pictureList.add(0,picture);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pictureAdapter.notifyItemInserted(0);
                            imageRecycle.scrollToPosition(0);
                        }
                    });
                }
            }
        });

    }



    class ServiceRecever extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, final Intent intent) {
            final Vibrator vibrator = (Vibrator)context.getSystemService(Service.VIBRATOR_SERVICE);
            vibrator.vibrate(new long[]{1000,1000,1000,1000},2);
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                    .setTitle("警告")
                    .setMessage("\n检测到家里烟雾情况出现异常！")
                    .setPositiveButton("关闭报警", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            vibrator.cancel();
                            getContext().stopService(intent1);
                        }
                    });
            builder.create().show();

        }
    }
}
