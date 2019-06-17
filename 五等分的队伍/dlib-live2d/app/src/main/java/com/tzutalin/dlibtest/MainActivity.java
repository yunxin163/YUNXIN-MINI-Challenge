package com.tzutalin.dlibtest;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

//import com.tzutalin.chat2d.R;
import com.google.gson.Gson;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.avchat.AVChatCallback;
import com.netease.nimlib.sdk.avchat.AVChatManager;
import com.netease.nimlib.sdk.avchat.constant.AVChatEventType;
import com.netease.nimlib.sdk.avchat.constant.AVChatType;
import com.netease.nimlib.sdk.avchat.model.AVChatAudioFrame;
import com.netease.nimlib.sdk.avchat.model.AVChatCalleeAckEvent;
import com.netease.nimlib.sdk.avchat.model.AVChatData;
import com.netease.nimlib.sdk.avchat.model.AVChatNotifyOption;
import com.netease.nimlib.sdk.avchat.model.AVChatParameters;
import com.netease.nimlib.sdk.avchat.model.AVChatVideoFrame;
import com.netease.nimlib.sdk.avchat.video.AVChatVideoCapturerFactory;
import com.netease.nimlib.sdk.avchat.model.AVChatCommonEvent;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.netease.nimlib.sdk.avchat.constant.AVChatChannelProfile.CHANNEL_PROFILE_DEFAULT;
import static com.netease.nimlib.sdk.avchat.model.AVChatParameters.KEY_AUDIO_FRAME_FILTER;
import static com.netease.nrtc.sdk.NRtcConstants.VideoScalingType.SCALE_ASPECT_BALANCED;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "tag";
    public static List<Role> roleList=new ArrayList<>();
    public static Handler handler = new myHandler();
    public static URL url =null;
    private static MainActivity thiz;
    private static Gson gson=new Gson();
    private static RecyclerView recyclerView;
    private static RoleAdapter adapter;
    private View view1,view2,view3,view4;
    private List<View> viewList;
    private ViewPager viewPager;
    private AlertDialog.Builder dialog;
    private CircleImageView headPic;
    private EditText usernameEdit;
    private EditText autoEdit;
    private Button preserveBtn;
    private BottomNavigationView mBottomNavigationView;
    private MenuItem mMenuItem;
    private Spinner spinner;
    private Handler interfaceHandler;
    public Mine mine =Mine.getInstance();
    public String imagePath = null;
    public static void activityStart(Context context, Mine mine,List<Role> roles){
        Intent intent = new Intent(context, MainActivity.class);
//        intent.putExtra("rolePhone",mine.getRolePhone().toArray());
        roleList=roles;
        context.startActivity(intent);
    }
    public static void activityStart(Context context,Role role){
        Intent intent = new Intent(context,MainActivity.class);
        roleList.add(role);
        context.startActivity(intent);
        adapter.notifyItemInserted(roleList.size()-1);
        recyclerView.scrollToPosition(roleList.size()-1);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(!GlobalVar.isIMLogin()){
            Toast.makeText(this,"云信认证失败",Toast.LENGTH_LONG).show();
        }
        thiz = this;
        interfaceHandler = new Handler(getMainLooper());
        LandmarkManager.getInstance().setContext(this);

        List<String> permissionList = new ArrayList<>();
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.CAMERA);
        }
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.RECORD_AUDIO);
        }
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAPTURE_AUDIO_OUTPUT)
                != PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.CAPTURE_AUDIO_OUTPUT);
        }
        if(!permissionList.isEmpty()){
            String[] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(MainActivity.this, permissions, 1);
        }

        dialog=new AlertDialog.Builder(MainActivity.this);
        dialog.setIcon(R.drawable.touxiang2);

        Observer<AVChatCalleeAckEvent> callAckObserver = (Observer<AVChatCalleeAckEvent>) ackInfo -> {
            if (ackInfo.getEvent() == AVChatEventType.CALLEE_ACK_BUSY) {
                // 对方正在忙
                Toast.makeText(MainActivity.this,"对方正在忙",Toast.LENGTH_SHORT).show();
            } else if (ackInfo.getEvent() == AVChatEventType.CALLEE_ACK_REJECT) {
                // 对方拒绝接听
                Toast.makeText(MainActivity.this,"对方拒绝接听",Toast.LENGTH_SHORT).show();
            } else if (ackInfo.getEvent() == AVChatEventType.CALLEE_ACK_AGREE) {
                // 对方同意接听
//                ChatActivity.activityStart(MainActivity.this);
            }
        };
        AVChatManager.getInstance().observeCalleeAckNotification(callAckObserver, true);
        enableAVChat();
        tryt();

        viewPager = (ViewPager) findViewById(R.id.view_pager);
        mBottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigationView);
        LayoutInflater layoutInflater =getLayoutInflater();
        view1=layoutInflater.inflate(R.layout.activity_message,null);
        view2=layoutInflater.inflate(R.layout.activity_friends,null);
        view3=layoutInflater.inflate(R.layout.activity_work,null);
        view4=layoutInflater.inflate(R.layout.activity_me,null);
        init();
        viewList=new ArrayList<>();
        viewList.add(view1);
        viewList.add(view2);
        viewList.add(view3);
        viewList.add(view4);
        viewPager.setAdapter(new ViewPagerAdapter(viewList));
        Toolbar toolbar = view1.findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        BottomNavigationViewHelper.disableShiftMode(mBottomNavigationView);
        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                mMenuItem = menuItem;
                switch (menuItem.getItemId()) {
                    case R.id.xiaoxi:
                        viewPager.setCurrentItem(0);
                        return true;
                    case R.id.lianxiren:
                        viewPager.setCurrentItem(1);
                        return true;
                    case R.id.gongzuotai:
                        viewPager.setCurrentItem(2);
                        return true;
                    case R.id.wo:
                        viewPager.setCurrentItem(3);
                        return true;
                }
                return false;
            }
        });
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (mMenuItem != null) {
                    mMenuItem.setChecked(false);
                } else {
                    mBottomNavigationView.getMenu().getItem(0).setChecked(false);
                }
                mMenuItem = mBottomNavigationView.getMenu().getItem(position);
                mMenuItem.setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.jiahao:
                        AddFriendActivity.activityStart(MainActivity.this);
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
//        Intent intent= getIntent();
//        String[] phones =intent.getStringArrayExtra("rolePhone");
//        Link.rolePhonePush(url, phones, new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                if(response.isSuccessful()){
//                    Role role = gson.fromJson(response.body().string(),Role.class);
//                    roleList.add(role);
//                }
//            }
//        });
        initRecyclerView();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.function_menu,menu);
        return true;
    }
    private  void initRecyclerView(){
//        initRole();
        recyclerView =  view1.findViewById(R.id.message_recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new RoleAdapter(MainActivity.this,roleList);
//        adapter.setHandler(handler);
        adapter.setHandler(handler);
        recyclerView.setAdapter(adapter);
    }
//    private static void initRole(){
//        for(int i = 0;i<20;i++){
//            Role role = new Role(R.drawable.begin,"次元信","好嗨呀","星期日");
//            roleList.add(role);
//        }
//    }



    public static class myHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case MyService.NEW_FRIEND:
                    roleList.add(MyService.role);
                    adapter.notifyItemInserted(roleList.size()-1);
                    recyclerView.scrollToPosition(roleList.size()-1);
                    break;
                case MyService.CALL2:
                    MainActivity.getInstance().outGoingCalling(msg.obj.toString());
                    break;
                default:
                    break;
            }
        }
    }


    public static MainActivity getInstance(){
        return thiz;
    }
    public CallStateEnum callingState;
    public AVChatData avChatData;
    public void outGoingCalling(String account) {
        AVChatNotifyOption notifyOption = new AVChatNotifyOption();

        //附加字段
        notifyOption.extendMessage = "extra_data";
        //默认forceKeepCalling为true，开发者如果不需要离线持续呼叫功能可以将forceKeepCalling设为false
        notifyOption.forceKeepCalling = false;
        //打开Rtc模块
        AVChatManager.getInstance().enableRtc();

        this.callingState = CallStateEnum.AUDIO;

        AVChatParameters avChatParameters = new AVChatParameters();
        //设置自己需要的可选参数
//        AVChatManager.getInstance().setParameters(avChatParameters);
        AVChatManager.getInstance().setChannelProfile(CHANNEL_PROFILE_DEFAULT);
        AVChatManager.getInstance().setParameter(KEY_AUDIO_FRAME_FILTER,true);
        //呼叫
        AVChatManager.getInstance().call2(account, AVChatType.AUDIO, notifyOption, new AVChatCallback<AVChatData>() {
            @Override
            public void onSuccess(AVChatData data) {
                avChatData = data;
                //发起会话成功
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this,data.getAccount(),Toast.LENGTH_SHORT).show();
                    }
                });

            }

            @Override
            public void onFailed(int code) {

                closeRtc();
                closeSessions(CloseType.ERROR);
            }

            @Override
            public void onException(Throwable exception) {
                closeRtc();
                closeSessions(CloseType.ERROR);
            }
        });
    }
    private void closeRtc(){
        AVChatManager.getInstance().disableRtc();
    }
    private void enableAVChat() {
        registerAVChatIncomingCallObserver(true);
    }

    private void registerAVChatIncomingCallObserver(boolean register) {
        AVChatManager.getInstance().observeIncomingCall((Observer<AVChatData>) data -> {
            String extra = data.getExtra();
            Log.e("Extra", "Extra Message->" + extra);
            //被叫收到通话请求回调
            avChatData=data;
            runOnUiThread(() -> {
                dialog.setTitle(data.getAccount() + "向你发出视频请求");
                dialog.setNegativeButton("拒绝", (dialog, which) -> {
                    reject();
                });
                dialog.setPositiveButton("接受", (dialog, which) -> {
                    receiveInComingCall();
                    LandmarkManager manager = LandmarkManager.getInstance();
                    if(manager.chatlock){
                        manager.activeChat(data.getAccount());
                    }
                });
                dialog.show();
            });

//                ChatActivity.activityStart(MainActivity.this);
        }, register);
    }


    private void reject(){
        AVChatManager.getInstance().hangUp2(avChatData.getChatId(), new AVChatCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(MainActivity.this,"成功拒绝通话",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailed(int i) {

            }

            @Override
            public void onException(Throwable throwable) {

            }
        });
    }
    private void receiveInComingCall() {
        //接听，告知服务器，以便通知其他端

        AVChatManager.getInstance().enableRtc();
        AVChatParameters avChatParameters = new AVChatParameters();
//        AVChatManager.getInstance().setParameters(avChatParameters);
        AVChatManager.getInstance().setParameter(KEY_AUDIO_FRAME_FILTER,true);
        AVChatManager.getInstance().accept2(avChatData.getChatId(), new AVChatCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
//                isCallEstablish.set(true);
            }

            @Override
            public void onFailed(int code) {
                if (code == -1) {
                    Toast.makeText(MainActivity.this, "本地音视频启动失败", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "建立连接失败", Toast.LENGTH_SHORT).show();
                }
                Log.e(TAG, "accept onFailed->" + code);
                handleAcceptFailed();
            }

            @Override
            public void onException(Throwable exception) {
                Log.d(TAG, "accept exception->" + exception);
                handleAcceptFailed();
            }
        });
    }

    private boolean destroyRTC = false;
    private void handleAcceptFailed() {
        if (destroyRTC) {
            return;
        }
        AVChatManager.getInstance().disableRtc();
        destroyRTC = true;
        closeSessions(CloseType.CANCEL);
    }
    private void closeSessions(CloseType type){
        switch (type){
            case ERROR:
                interfaceHandler.post(()->Toast.makeText(this,"出错",Toast.LENGTH_SHORT).show());
                break;
            case CANCEL:
                interfaceHandler.post(()->Toast.makeText(this,"对方已取消",Toast.LENGTH_SHORT).show());
                break;
        }
    }
    enum CallStateEnum{VIDEO,AUDIO,VIDEO_CONNECTING}
    enum CloseType{ERROR,CANCEL}
//    @Override
//    public void onCallEstablished() {
//
//    }

    public void tryt(){
        AVChatManager.getInstance().observeAVChatState(new AVChatStateAdapter() {

            @Override
            public void onDisconnectServer(int i) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this,"与服务器断开连接",Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onCallEstablished() {
                startActivity(new Intent(MainActivity.this,CameraActivity.class));
            }

            @Override
            public void onDeviceEvent(int i, String s) {
                runOnUiThread(() -> Log.i(TAG, "device event: "+s));

            }

            @Override
            public boolean onVideoFrameFilter(AVChatVideoFrame avChatVideoFrame, boolean b) {
                return false;
            }

            @Override
            public boolean onAudioFrameFilter(AVChatAudioFrame avChatAudioFrame) {

                Log.d("AudioFrame",String.valueOf(avChatAudioFrame.getData().array().length));
                return true;
            }
        },true);
    }
    private void init(){
        headPic = view4.findViewById(R.id.head_pic);
        Bitmap bitmap = BitmapFactory.decodeFile(getExternalCacheDir()+"/"+"avatar/"+mine.getImage());
//        headPic.setImageURI(Uri.fromFile(new File(getExternalCacheDir(),"avatar/"+mine.getImage())));
        headPic.setImageBitmap(bitmap);
        usernameEdit = view4.findViewById(R.id.me_username);
        usernameEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String content = s.toString();
                if(content.contains("\r")||content.contains("\n")) {
                    content = content.replace("\r", "").replace("\n", "");
                    autoEdit.setText(content);
                }
            }
        });
        usernameEdit.setText(mine.getName());
        autoEdit = view4.findViewById(R.id.me_auto);
        autoEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String content = s.toString();
                if(content.contains("\r")||content.contains("\n")) {
                    content = content.replace("\r", "").replace("\n", "");
                    autoEdit.setText(content);
                }
            }
        });
        autoEdit.setText(mine.getSignature());
        preserveBtn = view4.findViewById(R.id.preserve_btn);
        preserveBtn.setOnClickListener(this);
        spinner=view4.findViewById(R.id.sp);
        headPic.setOnClickListener(this);
        LinearLayout linearLayout = view4.findViewById(R.id.me_layout);
        linearLayout.setOnClickListener(this);
        String[] ratioDescArray = {
                "角色1",
                "角色2"


        };
        ArrayAdapter<String> ratioAdapter = new ArrayAdapter<String>(this,
                R.layout.item_select, ratioDescArray);
        ratioAdapter.setDropDownViewResource(R.layout.item_select);
        spinner.setPrompt("请选择角色");
        spinner.setAdapter(ratioAdapter);
        spinner.setSelection(0);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==1){
                    ModelParam.modelIndex=1;
                }else if(position==0){
                    ModelParam.modelIndex=0;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }


    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.head_pic:
                headPicOnClick();
                break;
            case R.id.preserve_btn:
                if(preserveBtn.getText().toString().equals("修改信息")) {
                    usernameEdit.setFocusable(true);
                    usernameEdit.setFocusableInTouchMode(true);
                    autoEdit.setFocusable(true);
                    autoEdit.setFocusableInTouchMode(true);
                    preserveBtn.setText("保存修改");

                }else {
                    String username = usernameEdit.getText().toString();
                    String signature = autoEdit.getText().toString();
                    try {
                        URL url = new URL("http://125.124.155.138:3000/user/editUser");
                        Link.editUser(url, mine.getToken(), username, signature, new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {

                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                try {
                                    JSONObject jsonObject = new JSONObject(response.body().string());
                                    if (jsonObject.getInt("code") == 200) {
                                        runOnUiThread(() -> {
                                            mine.setName(username);
                                            mine.setSignature(signature);
                                            try {
                                                Toast.makeText(MainActivity.this,jsonObject.getString("msg"),Toast.LENGTH_SHORT).show();
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        });
                                    }else {
                                        runOnUiThread(()->{
                                            usernameEdit.setText(mine.getName());
                                            autoEdit.setText(mine.getSignature());
                                            try {
                                                Toast.makeText(MainActivity.this,jsonObject.getString("msg"),Toast.LENGTH_SHORT).show();
                                            } catch (JSONException e) {
                                                e.printStackTrace();
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
                    usernameEdit.setFocusable(false);
                    autoEdit.setFocusable(false);
                    preserveBtn.setText("修改信息");
//                    Toast.makeText(MainActivity.this, "抱歉，功能尚未开通.", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.me_layout:
                hideSoftKeyboard(MainActivity.this,viewList);
                break;
            default:
                break;
        }
    }
    private void headPicOnClick(){
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this, new
                    String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }else{
            openAlbum();
        }
    }

    private void openAlbum(){
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, 1);
    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        switch(requestCode){
//            case 1:
//                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
//                    openAlbum();
//                }else{
//                    Toast.makeText(this, "You denied the permission",
//                            Toast.LENGTH_SHORT).show();
//                }
//                break;
//            default:
//                break;
//        }
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode){
            case 1:
                if(resultCode == RESULT_OK){
                    if(Build.VERSION.SDK_INT >= 19){
                        handleImageOnKitKat(data);
                    }else{
                        handleImageBeforeKitKat(data);
                    }
                }
                break;
            default:
                break;
        }
    }

    @TargetApi(19)
    private void handleImageOnKitKat(Intent data){

        Uri uri = data.getData();
        if(DocumentsContract.isDocumentUri(this, uri)){
            String docId = DocumentsContract.getDocumentId(uri);
            if("com.android.providers.media.documents".equals(uri.getAuthority())){
                String id = docId.split(":")[1];
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            }else if("com.android.provider.downloads.documents".equals(uri.getAuthority())){
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }
        }else if("content".equalsIgnoreCase(uri.getScheme())){
            imagePath = getImagePath(uri, null);
        }else if("file".equalsIgnoreCase(uri.getScheme())){
            imagePath = uri.getPath();
        }
        displayImage(imagePath);
    }

    private void handleImageBeforeKitKat(Intent data){
        Uri uri = data.getData();
        String imagePath = getImagePath(uri, null);
        displayImage(imagePath);
    }

    private String getImagePath(Uri uri, String selection){
        String path = null;
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if(cursor != null){
            if(cursor.moveToNext()){
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    private void displayImage(String imagePath){
        if(imagePath != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            headPic.setImageBitmap(bitmap);
            try {
                URL url = new URL("http://125.124.155.138:3000/user/uploadAvatar");
                Link.postImage(url, mine.getToken(), new File(imagePath), new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        try {
                            JSONObject jsonObject = new JSONObject(response.body().string());

                            if(jsonObject.getInt("code")==200){
                                JSONObject dataObject = jsonObject.getJSONObject("data");
                                String avatar = dataObject.getString("avatar");
                                mine.setImage(avatar);
                            }else {
                                runOnUiThread(()->{
                                    try {
                                        Toast.makeText(MainActivity.this,jsonObject.getString("msg"),Toast.LENGTH_SHORT).show();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
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

        }else{
            Toast.makeText(this, "failed to get image",
                    Toast.LENGTH_SHORT).show();
        }
    }
    public static void hideSoftKeyboard(Context context, List<View> viewList) {
        if (viewList == null) return;

        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);

        for (View v : viewList) {
            inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}
