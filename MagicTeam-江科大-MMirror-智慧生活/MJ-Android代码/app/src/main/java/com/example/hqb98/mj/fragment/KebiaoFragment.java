package com.example.hqb98.mj.fragment;

import android.app.Instrumentation;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hqb98.mj.R;
import com.example.hqb98.mj.data.Corse;
import com.example.hqb98.mj.data.InfoCourse;
import com.example.hqb98.mj.data.Information;
import com.example.hqb98.mj.util.GlobalUtil;
import com.example.hqb98.mj.util.HttpUtil;
import com.example.hqb98.mj.util.SharedUtil;
import com.example.hqb98.mj.util.SoftKeyBoardListener;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.LitePal;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class KebiaoFragment extends Fragment implements View.OnClickListener {
    private View view;
    private Toolbar toolbar;
    private RelativeLayout relativeLayouts[];
    private LinearLayout linearLayout0;
    private ArrayList<Corse> corses;
    private ArrayList<Integer> digital = new ArrayList<>();
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private ProgressBar progressBar;
    private List<Corse> corses1 = new ArrayList<>();
    private TextView kebiao_view;
    private Calendar calendar = Calendar.getInstance();
    private int windowWidth;
    private int windowHeight;
    private DisplayMetrics displayMetrics;
    private Display display;
    private WindowManager windowManager;
    //软键盘是否显示
    private boolean keyboard;
    private Instrumentation inst = new Instrumentation();
    Button[] buttons = new Button[9];

    public static final int SEND_REQUEST = 123;
    Handler handler1 = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case SEND_REQUEST:
                    sendReqest();
                    break;
            }
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        windowManager = getActivity().getWindowManager();
        display = windowManager.getDefaultDisplay();
        displayMetrics = new DisplayMetrics();
        display.getMetrics(displayMetrics);
        windowHeight = displayMetrics.heightPixels;
        windowWidth = displayMetrics.widthPixels;
    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.kebiao_layout,container,false);
        addKebiaoLayout();
        LitePal.getDatabase();
        preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        editor = preferences.edit();
        progressBar = (ProgressBar)view.findViewById(R.id.kebiao_progressbar);
        progressBar.setVisibility(View.GONE);
        initView();
        return view;
    }

    private void initView() {
        toolbar = (Toolbar)view.findViewById(R.id.toolbar1);
        AppCompatActivity appCompatActivity= (AppCompatActivity) getActivity();
        appCompatActivity.setSupportActionBar(toolbar);
        ActionBar actionBar = appCompatActivity.getSupportActionBar();
        corses = new ArrayList<Corse>();
        corses.addAll(LitePal.findAll(Corse.class));
        if (corses.size()!=0){
            choose_week(corses,6);

        }else {
            sendReqest();
        }
        kebiao_view = (TextView)view.findViewById(R.id.kebiao_view);
        kebiao_view.setText("第"+getWeeks()+"周");
        kebiao_view.setOnClickListener(this);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.toolbar_menu,menu);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {

        super.onPrepareOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_login:
                final View popup = (View)getLayoutInflater().inflate(R.layout.fragment_kebiao_login,null);
                final AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                final AlertDialog dialog1 = dialog.setView(popup)
                        .create();
                        dialog1.show();
                Button ok = (Button)popup.findViewById(R.id.cgroup_ok);
                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final TextInputEditText textInputEditText = (TextInputEditText)popup.findViewById(R.id.cgroup_name);
                        TextInputEditText textInputEditText1 = (TextInputEditText)popup.findViewById(R.id.cgroup_password);
                        String jiaowu_account = textInputEditText.getText().toString();
                        String jiaowu_password = textInputEditText1.getText().toString();
                        if (GlobalUtil.isNotEmpty(jiaowu_account)&&GlobalUtil.isNotEmpty(jiaowu_password)){
                            SharedUtil.save("jiaowu_account",jiaowu_account);
                            SharedUtil.save("jiaowu_password",jiaowu_password);
                            SharedUtil.save("semester","2018-2019-1");
                            sendReqest(dialog1);
                        }else {
                            Toast.makeText(getContext(),"内容不准为空",Toast.LENGTH_SHORT).show();
                        }

                    }
                });
                final Button cancel = (Button)popup.findViewById(R.id.cgroup_cancel);
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog1.dismiss();
                    }
                });
                break;
            case R.id.menu_importschedule:
                sendReqest();
                kebiao_view.setText("第"+getWeeks()+"周");
                break;
            case R.id.menu_changesemester:
                String str = preferences.getString("account","");
                String semester = str.substring(0,2);
                int j = calendar.get(Calendar.YEAR)-Integer.parseInt("20"+semester);
                if (calendar.get(Calendar.MONTH)>=8){
                    j = j+1;
                }
                final String[] semesters = new String[(j)*2];
                for (int i = 0;i < j;i++){
                    semesters[i*2] = (calendar.get(Calendar.YEAR)-i-1)+"-"+(calendar.get(Calendar.YEAR)-i)+"-2";
                    semesters[i*2+1] = (calendar.get(Calendar.YEAR)-i-1)+"-"+(calendar.get(Calendar.YEAR)-i)+"-1";
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("选择学期")
                        .setItems(semesters, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                editor.putString("semester",semesters[which]);
                                editor.apply();
                                Message message = new Message();
                                message.what = SEND_REQUEST;
                                handler1.sendMessage(message);
                                dialog.dismiss();

                            }
                        });
                builder.create().show();
                break;
            case R.id.menu_choeesekebiao:
                AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
                builder1.setTitle("请选择周次");
                String[] weekss = getResources().getStringArray(R.array.weeks);
                builder1.setItems(weekss, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        choose_week(corses,which+1);
                        kebiao_view.setText("第"+(which+1)+"周");
                        dialog.dismiss();
                    }
                });
                builder1.create().show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private String getWeeks() {
        String week = new String();
        int day = 0;
        day = Integer.parseInt(getTwoDay(calendar.get(Calendar.YEAR)+"-"+(calendar.get(Calendar.MONTH)+1)+"-"+calendar.get(Calendar.DAY_OF_MONTH),getResources().getString(R.string.semesteDate)))-1;
        if (day%7==0){
            week = (int)(day/7)+"";
        }else {
            week = (int)(day/7)+1+"";
        }
        if (Integer.parseInt(week)<0||Integer.parseInt(week)>20){
            week=1+"";
        }

        return week;
    }
    /**
     * 得到二个日期间的间隔天数
     */
    public static String getTwoDay(String sj1, String sj2) {
        SimpleDateFormat myFormatter = new SimpleDateFormat("yyyy-MM-dd");
        long day = 0;
        try {
            java.util.Date date = myFormatter.parse(sj1);
            java.util.Date mydate = myFormatter.parse(sj2);
            day = (date.getTime() - mydate.getTime()) / (24 * 60 * 60 * 1000);
        } catch (Exception e) {
            return "";
        }
        return day + "";
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.kebiao_view:
                AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
                builder1.setTitle("请选择周次");
                String[] weekss = getResources().getStringArray(R.array.weeks);
                builder1.setItems(weekss, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        choose_week(corses,which+1);
                        kebiao_view.setText("第"+(which+1)+"周");
                        dialog.dismiss();
                    }
                });
                builder1.create().show();
                break;
        }
    }

    private void setSelect(){
        for (int j=0;j<9;j++){
            buttons[j].setSelected(false);
        }
    }

    private void sendReqest(){
        /**
         * 这里需要手动设置一下
         */
        String account = preferences.getString("jiaowu_account","");
        String password = preferences.getString("jiaowu_password","");
        String semester = preferences.getString("semester","2018-2019-1");
        if (GlobalUtil.isNotEmpty(account)&&GlobalUtil.isNotEmpty(password)){
            final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setMessage("努力加载中，请稍后~");
            final AlertDialog alertDialog = builder.create();
            alertDialog.show();
            HttpUtil.getCourseRequest(account,password,semester ,new okhttp3.Callback(){
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.d("getCourse",e.toString());
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getContext(),"网络出现了一点小问题哦~",Toast.LENGTH_SHORT).show();
                        }
                    });
                    alertDialog.dismiss();
                    progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String responseData = response.body().string();
                    Log.d("getCourse",responseData);
                    if (response.code()==200){
                        try {
                            JSONObject jsonObject = new JSONObject(responseData);
                            if (jsonObject.getInt("code")==200){
                                Gson gson = new Gson();
                                Information information = gson.fromJson(responseData,Information.class);
                                int code = information.getCode();
                                if (code==200) {
                                    LitePal.deleteAll(Corse.class);
                                    final List<InfoCourse> courseList = information.getInfo();
                                    setData(courseList);
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            int count = preferences.getInt("weekNum",1);
                                            choose_week(corses,count);
                                            alertDialog.dismiss();
//                                            new Thread(new Runnable() {
//                                                @Override
//                                                public void run() {
//                                                    Instrumentation inst = new Instrumentation();
//                                                    inst.sendKeyDownUpSync(KeyEvent.KEYCODE_BACK);
//                                                }
//                                            }).start();
                                        }
                                    });

                                }
                            }else {
                                alertDialog.dismiss();
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
//                                        backKeyDown();
                                        Toast.makeText(getContext(),"密码输错了哦",Toast.LENGTH_SHORT).show();

                                    }
                                });
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }else {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getContext(),"网络出现了一点小问题",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                }
            });
        }else {
            Toast.makeText(getContext(),"请先登录教务系统才能查看课表哦！",Toast.LENGTH_SHORT).show();
        }

    }

    private void sendReqest(final AlertDialog dialog){
        /**
         * 这里需要手动设置一下
         */
        String account = preferences.getString("jiaowu_account","");
        String password = preferences.getString("jiaowu_password","");
        String semester = preferences.getString("semester","2018-2019-1");
        if (GlobalUtil.isNotEmpty(account)&&GlobalUtil.isNotEmpty(password)){
            final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setMessage("努力加载中，请稍后~");
            final AlertDialog alertDialog = builder.create();
            alertDialog.show();
            HttpUtil.getCourseRequest(account,password,semester ,new okhttp3.Callback(){
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.d("getCourse",e.toString());
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getContext(),"网络出现了一点小问题哦~",Toast.LENGTH_SHORT).show();
                        }
                    });
                    dialog.dismiss();
                    alertDialog.dismiss();
                    progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String responseData = response.body().string();
                    Log.d("getCourse",responseData);
                    if (response.code()==200){
                        try {
                            JSONObject jsonObject = new JSONObject(responseData);
                            if (jsonObject.getInt("code")==200){
                                Gson gson = new Gson();
                                Information information = gson.fromJson(responseData,Information.class);
                                int code = information.getCode();
                                if (code==200) {
                                    LitePal.deleteAll(Corse.class);
                                    final List<InfoCourse> courseList = information.getInfo();
                                    setData(courseList);
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            int count = preferences.getInt("weekNum",1);
                                            choose_week(corses,count);
                                            dialog.dismiss();
                                            alertDialog.dismiss();
//                                            new Thread(new Runnable() {
//                                                @Override
//                                                public void run() {
//                                                    Instrumentation inst = new Instrumentation();
//                                                    inst.sendKeyDownUpSync(KeyEvent.KEYCODE_BACK);
//                                                }
//                                            }).start();
                                        }
                                    });

                                }
                            }else {
                                alertDialog.dismiss();
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        SharedUtil.clear("jiaowu_account");
                                        SharedUtil.clear("jiaowu_password");
                                        Toast.makeText(getContext(),"密码输错了哦",Toast.LENGTH_SHORT).show();

                                    }
                                });
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }else {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getContext(),"网络出现了一点小问题",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                }
            });
        }else {
            Toast.makeText(getContext(),"请先登录教务系统才能查看课表哦！",Toast.LENGTH_SHORT).show();
        }

    }

    private void choose_week(List<Corse> corses,int count){
        corses1.clear();
        for (int j=0;j<corses.size();j++){
            String weeks = corses.get(j).getCorseWeek();
            if (weeks.length()>3){
                String week_info = weeks.substring(0,weeks.length()-3);
                String[] ws = week_info.split(",");
                List<Integer> week = new ArrayList<>();
                for (int i=0;i<ws.length;i++){
                    String[] strings = ws[i].split("-");
                    if(strings.length%2==0){
                        int fdigit = Integer.parseInt(strings[0]);
                        int ldigit = Integer.parseInt(strings[1]);
                        for (int x=fdigit;x<=ldigit;x++){
                            week.add(x);
                        }
                    }else {
                        week.add(Integer.parseInt(strings[0]));
                    }
                }
                for (int i=0;i<week.size();i++){
                    if (count == week.get(i)){
                        corses1.add(corses.get(j));
                    }
                }
            }
        }
        addView(corses1);
    }

    //给每个Relative添加显示课表的view
    private void addView(final List<Corse> corseList) {

        for (int j=0;j<relativeLayouts.length;j++){
            relativeLayouts[j].removeAllViews();
        }
        for (int i=0;i<corseList.size();i++){
            int section =  corseList.get(i).getSection();
            int weekday = corseList.get(i).getWeekDay();
            int d = section*7+weekday;
            digital.clear();
            if (!digital.contains(d)){
                digital.add(d);
                String str = corseList.get(i).getCorseName()+"@"+corseList.get(i).getClassRoom();
                String string=str.replace("@null","");
                final TextView textView = new TextView(relativeLayouts[d-1].getContext());
                textView.setId(i);
                textView.setText(string);
                textView.setTextSize(12);
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.MATCH_PARENT);
                params.setMargins(2,3,2,3);
                textView.setGravity(Gravity.CENTER);
                textView.setTextColor(Color.WHITE);
                textView.setBackground(getResources().getDrawable(R.drawable.shape));
                textView.setLayoutParams(params);
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int i = v.getId();
                        TableLayout kebiao_dialog = (TableLayout)getActivity().getLayoutInflater().inflate(R.layout.kebiao_detail,null);
                        TextView textView1 = (TextView)kebiao_dialog.findViewById(R.id.detail_kch);
                        TextView textView2 = (TextView)kebiao_dialog.findViewById(R.id.detail_kcjs);
                        TextView textView3 = (TextView)kebiao_dialog.findViewById(R.id.detail_js);
                        TextView textView4 = (TextView)kebiao_dialog.findViewById(R.id.detail_zc);
                        textView1.setText(corseList.get(i).getCorseNumber());
                        textView2.setText(corseList.get(i).getTeacherName());
                        textView3.setText(corseList.get(i).getClassRoom());
                        textView4.setText(corseList.get(i).getCorseWeek());
                        new AlertDialog.Builder(getActivity())
                                .setTitle(corseList.get(i).getCorseName())
                                .setView(kebiao_dialog)
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                })
                                .create()
                                .show();

                    }
                });
                relativeLayouts[d-1].addView(textView);
            }

        }

    }
    //获取获取课表信息
    private void setData(List<InfoCourse> courseList) {
        List<InfoCourse> info = courseList;
        corses.clear();
        for (int i = 0; i < info.size() - 1; i++) {
            InfoCourse info1 = info.get(i);
            for (int j = 0;j<7;j++){
                getdata(i,j,info1);
            }
        }
    }
    //精确课表信息
    private void getdata(int section,int day, InfoCourse info){
        int weekday;
        if (info.getMonday()!=null&&day==0) {
            String string = info.getMonday();
            weekday = 1;
            String s = string.replace("---------------------", "@");
            String[] strings = s.split("@");
            if_two(section,weekday,strings);
        }
        if (info.getTuesday()!=null&&day==1) {
            String string = info.getTuesday();
            weekday = 2;
            String s = string.replace("---------------------", "@");
            String[] strings = s.split("@");
            if_two(section,weekday,strings);
        }
        if (info.getWednesday()!=null&&day==2) {
            String string = info.getWednesday();
            weekday = 3;
            String s = string.replace("---------------------", "@");
            String[] strings = s.split("@");
            if_two(section,weekday,strings);
        }
        if (info.getThursday()!=null&&day==3) {
            String string = info.getThursday();
            weekday = 4;
            String s = string.replace("---------------------", "@");
            String[] strings = s.split("@");
            if_two(section,weekday,strings);
        }
        if (info.getFriday()!=null&&day==4) {
            String string = info.getFriday();
            weekday = 5;
            String s = string.replace("---------------------", "@");
            String[] strings = s.split("@");
            if_two(section,weekday,strings);
        }
        if (info.getSaturday()!=null&&day==5) {
            String string = info.getSaturday();
            weekday = 6;
            String s = string.replace("---------------------", "@");
            String[] strings = s.split("@");
            if_two(section,weekday,strings);
        }
        if (info.getSunday()!=null&&day==6) {
            String string = info.getSunday();
            weekday = 7;
            String s = string.replace("---------------------", "@");
            String[] strings = s.split("@");
            if_two(section,weekday,strings);
        }
    }

    private void if_two(int i,int weekday,String[] strings){
        if (strings.length == 10) {
            Corse corse = new Corse();
            corse.setCorseNumber(strings[0]);
            corse.setCorseName(strings[1]);
            corse.setTeacherName(strings[2]);
            corse.setCorseWeek(strings[3]);
            corse.setClassRoom(strings[4]);
            corse.setSection(i);
            corse.setWeekDay(weekday);
            Corse corse1 = new Corse();
            corse1.setCorseNumber(strings[5]);
            corse1.setCorseName(strings[6]);
            corse1.setTeacherName(strings[7]);
            corse1.setCorseWeek(strings[8]);
            corse1.setClassRoom(strings[9]);
            corse1.setSection(i);
            corse1.setWeekDay(weekday);
            corses.add(corse);
            corses.add(corse1);
            corse.save();
            corse1.save();
        } else if (strings.length==4){
            Corse corse = new Corse();
            corse.setCorseNumber(strings[0]);
            corse.setCorseName(strings[1]);
            corse.setTeacherName(strings[2]);
            corse.setCorseWeek(strings[3]);
            corse.setSection(i);
            corse.setWeekDay(weekday);
            corses.add(corse);
            corse.save();
        }else if (strings.length==5){
            Corse corse = new Corse();
            corse.setCorseNumber(strings[0]);
            corse.setCorseName(strings[1]);
            corse.setTeacherName(strings[2]);
            corse.setCorseWeek(strings[3]);
            corse.setClassRoom(strings[4]);
            corse.setSection(i);
            corse.setWeekDay(weekday);
            corses.add(corse);
            corse.save();
        }
        else if (strings.length==8){
            Corse corse = new Corse();
            corse.setCorseNumber(strings[0]);
            corse.setCorseName(strings[1]);
            corse.setTeacherName(strings[2]);
            corse.setCorseWeek(strings[3]);
            corse.setSection(i);
            corse.setWeekDay(weekday);
            Corse corse1 = new Corse();
            corse1.setCorseNumber(strings[4]);
            corse1.setCorseName(strings[5]);
            corse1.setTeacherName(strings[6]);
            corse1.setCorseWeek(strings[7]);
            corse1.setSection(i);
            corse1.setWeekDay(weekday);
            corses.add(corse);
            corses.add(corse1);
            corse.save();
            corse1.save();
        }
        else if (strings.length==15){
            Corse corse = new Corse();
            corse.setCorseNumber(strings[0]);
            corse.setCorseName(strings[1]);
            corse.setTeacherName(strings[2]);
            corse.setCorseWeek(strings[3]);
            corse.setClassRoom(strings[4]);
            corse.setSection(i);
            corse.setWeekDay(weekday);
            Corse corse1 = new Corse();
            corse1.setCorseNumber(strings[5]);
            corse1.setCorseName(strings[6]);
            corse1.setTeacherName(strings[7]);
            corse1.setCorseWeek(strings[8]);
            corse1.setClassRoom(strings[9]);
            corse1.setSection(i);
            corse1.setWeekDay(weekday);
            Corse corse2 = new Corse();
            corse2.setCorseNumber(strings[10]);
            corse2.setCorseName(strings[11]);
            corse2.setTeacherName(strings[12]);
            corse2.setCorseWeek(strings[13]);
            corse2.setClassRoom(strings[14]);
            corse2.setSection(i);
            corse2.setWeekDay(weekday);
            corses.add(corse);
            corses.add(corse1);
            corses.add(corse2);
            corse.save();
            corse1.save();
            corse2.save();
        }
    }

    private void addKebiaoSideBar(){
        int height = windowHeight/4;
        int width = 35;
        linearLayout0 = (LinearLayout)view.findViewById(R.id.line0);
        RelativeLayout relativeLayout1 = new RelativeLayout(linearLayout0.getContext());
        RelativeLayout relativeLayout2 = new RelativeLayout(getContext());
        RelativeLayout relativeLayout3 = new RelativeLayout(getContext());
        RelativeLayout relativeLayout4 = new RelativeLayout(getContext());
        RelativeLayout relativeLayout5 = new RelativeLayout(getContext());
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams)linearLayout0.getLayoutParams();
        layoutParams.width = width;
        layoutParams.height = height;
        relativeLayout1.setLayoutParams(layoutParams);
        relativeLayout1.setBackgroundColor(getResources().getColor(R.color.black_word));
        linearLayout0.addView(relativeLayout1);
    }

    private int dip2px(Context context,float dipValue){
        Resources resources = context.getResources();
        return (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,dipValue,resources.getDisplayMetrics());
    }
    private void addKebiaoLayout() {
        relativeLayouts = new RelativeLayout[35];
        relativeLayouts[0] = (RelativeLayout)view.findViewById(R.id.view1);
        relativeLayouts[1] = (RelativeLayout)view.findViewById(R.id.view2);
        relativeLayouts[2] = (RelativeLayout)view.findViewById(R.id.view3);
        relativeLayouts[3] = (RelativeLayout)view.findViewById(R.id.view4);
        relativeLayouts[4] = (RelativeLayout)view.findViewById(R.id.view5);
        relativeLayouts[5] = (RelativeLayout)view.findViewById(R.id.view6);
        relativeLayouts[6] = (RelativeLayout)view.findViewById(R.id.view7);
        relativeLayouts[7] = (RelativeLayout)view.findViewById(R.id.view8);
        relativeLayouts[8] = (RelativeLayout)view.findViewById(R.id.view9);
        relativeLayouts[9] = (RelativeLayout)view.findViewById(R.id.view10);
        relativeLayouts[10] = (RelativeLayout)view.findViewById(R.id.view11);
        relativeLayouts[11] = (RelativeLayout)view.findViewById(R.id.view12);
        relativeLayouts[12] = (RelativeLayout)view.findViewById(R.id.view13);
        relativeLayouts[13] = (RelativeLayout)view.findViewById(R.id.view14);
        relativeLayouts[14] = (RelativeLayout)view.findViewById(R.id.view15);
        relativeLayouts[15] = (RelativeLayout)view.findViewById(R.id.view16);
        relativeLayouts[16] = (RelativeLayout)view.findViewById(R.id.view17);
        relativeLayouts[17] = (RelativeLayout)view.findViewById(R.id.view18);
        relativeLayouts[18] = (RelativeLayout)view.findViewById(R.id.view19);
        relativeLayouts[19] = (RelativeLayout)view.findViewById(R.id.view20);
        relativeLayouts[20] = (RelativeLayout)view.findViewById(R.id.view21);
        relativeLayouts[21] = (RelativeLayout)view.findViewById(R.id.view22);
        relativeLayouts[22] = (RelativeLayout)view.findViewById(R.id.view23);
        relativeLayouts[23] = (RelativeLayout)view.findViewById(R.id.view24);
        relativeLayouts[24] = (RelativeLayout)view.findViewById(R.id.view25);
        relativeLayouts[25] = (RelativeLayout)view.findViewById(R.id.view26);
        relativeLayouts[26] = (RelativeLayout)view.findViewById(R.id.view27);
        relativeLayouts[27] = (RelativeLayout)view.findViewById(R.id.view28);
        relativeLayouts[28] = (RelativeLayout)view.findViewById(R.id.view29);
        relativeLayouts[29] = (RelativeLayout)view.findViewById(R.id.view30);
        relativeLayouts[30] = (RelativeLayout)view.findViewById(R.id.view31);
        relativeLayouts[31] = (RelativeLayout)view.findViewById(R.id.view32);
        relativeLayouts[32] = (RelativeLayout)view.findViewById(R.id.view33);
        relativeLayouts[33] = (RelativeLayout)view.findViewById(R.id.view34);
        relativeLayouts[34] = (RelativeLayout)view.findViewById(R.id.view35);
    }

    private void backKeyDown(){
        //监听软键盘
        SoftKeyBoardListener.setListener(getActivity(), new SoftKeyBoardListener.OnSoftKeyBoardChangeListener() {
            @Override
            public void keyBoardShow(int height) {
                keyboard = true;
            }

            @Override
            public void keyBoardHide(int height) {
                keyboard = false;
            }
        });
        if (keyboard){
            new Thread(){
                @Override
                public void run() {
                    inst.sendKeyDownUpSync(KeyEvent.KEYCODE_BACK);
                    inst.sendKeyDownUpSync(KeyEvent.KEYCODE_BACK);
                }
            }.start();
        }else {
            new Thread(){
                @Override
                public void run() {
                    inst.sendKeyDownUpSync(KeyEvent.KEYCODE_BACK);
                }
            }.start();
        }
    }




}
