package com.example.hqb98.mj.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hqb98.mj.R;
import com.example.hqb98.mj.callback.DatePickerListenner;
import com.example.hqb98.mj.fragment.DatePickerFragment;
import com.example.hqb98.mj.util.GlobalUtil;
import com.example.hqb98.mj.util.SharedUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class SettingActivity extends BaseActivity implements View.OnClickListener {
    ImageView set_cancel;
    ImageView set_update;
    EditText set_name;
    LinearLayout set_touxiang;
    EditText set_student_number;
    EditText set_phone_number;
    EditText set_school;
    LinearLayout set_sex;
    LinearLayout set_birthday;
    LinearLayout set_password;
    TextView birthdayTv;
    View choose_sex;
    Button sex_boy;
    Button sex_girl;
    Button sex_cancel;
    TextView sex_tv;
    private Dialog dialog;
    TextView set_birthday_tv;
    private static final int CHOOSE_PHOTO = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initView();
        initData();
        initEvent();
    }

    private void initData() {
        String name = SharedUtil.read("username","");
        if (!name.equals("")){
            set_name.setText(name.split("：")[1]);
        }
        String studentNumber = SharedUtil.read("jiaowu_account","");
        set_student_number.setText(studentNumber);
        String phoneNumber = SharedUtil.read("phoneNumber","");
        set_phone_number.setText(phoneNumber);
        String school = SharedUtil.read("school","");
        set_school.setText(school);
        String sex = SharedUtil.read("sex","");
        sex_tv.setText(sex);
        String birthday = SharedUtil.read("birthday","");
        set_birthday_tv.setText(birthday);
    }

    private void initEvent() {
        set_cancel.setOnClickListener(this);
        set_update.setOnClickListener(this);
        set_touxiang.setOnClickListener(this);
        set_sex.setOnClickListener(this);
        set_birthday.setOnClickListener(this);
        set_password.setOnClickListener(this);
    }

    private void initView() {
        set_cancel = (ImageView)findViewById(R.id.set_cancel);
        set_update = (ImageView)findViewById(R.id.set_update);
        set_name = (EditText)findViewById(R.id.set_name);
        set_touxiang = (LinearLayout)findViewById(R.id.set_touxiang);
        set_student_number = (EditText)findViewById(R.id.set_student_number);
        set_phone_number = (EditText)findViewById(R.id.set_phone_number);
        set_school = (EditText)findViewById(R.id.set_school);
        set_sex = (LinearLayout)findViewById(R.id.set_sex);
        set_birthday = (LinearLayout)findViewById(R.id.set_birthday);
        set_password = (LinearLayout)findViewById(R.id.set_password);
        birthdayTv = (TextView)findViewById(R.id.set_birthday_tv);
        sex_tv = (TextView)findViewById(R.id.sex_tv);
        set_birthday_tv = (TextView)findViewById(R.id.set_birthday_tv);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.set_cancel:
                this.finish();
                break;
            case R.id.set_update:
                if (!set_name.getText().toString().equals("")){
                    Log.d("settingadad",set_name.getText().toString());
                    SharedUtil.save("username","昵称："+set_name.getText().toString());
                }
                if (!set_student_number.getText().toString().equals("")){
                    String jiaowu_account = set_student_number.getText().toString();
                    SharedUtil.save("jiaowu_account",jiaowu_account);
                }
                if (!set_phone_number.getText().toString().equals(""))
                {
                    SharedUtil.save("phoneNumber",set_phone_number.getText().toString());
                }
                if (!set_school.getText().toString().equals("")){
                    SharedUtil.save("school",set_school.getText().toString());
                }
                if (!sex_tv.getText().toString().equals("")){
                    SharedUtil.save("sex",sex_tv.getText().toString());
                }
                if (!set_birthday_tv.getText().toString().equals("")){
                    SharedUtil.save("birthday",set_birthday_tv.getText().toString());
                }

                this.finish();
                break;
            case R.id.set_touxiang:
                if (ContextCompat.checkSelfPermission(SettingActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(SettingActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
                }else {
                    openAlbum();
                }
                break;
            case R.id.set_sex:
                dialog = new Dialog(SettingActivity.this,R.style.Dialog);
                choose_sex = LayoutInflater.from(SettingActivity.this).inflate(R.layout.choose_sex,null);
                sex_boy = (Button)choose_sex.findViewById(R.id.sex_boy);
                sex_girl = (Button)choose_sex.findViewById(R.id.sex_girl);
                sex_cancel = (Button)choose_sex.findViewById(R.id.sex_cancel);
                sex_boy.setOnClickListener(this);
                sex_girl.setOnClickListener(this);
                sex_cancel.setOnClickListener(this);
                dialog.setContentView(choose_sex);
                Window window = dialog.getWindow();
                window.setGravity(Gravity.BOTTOM);
                WindowManager.LayoutParams layoutParams = window.getAttributes();
                layoutParams.y = 20;
                layoutParams.width = -1;
                window.setAttributes(layoutParams);
                dialog.show();
                break;
            case R.id.set_birthday:
                DatePickerFragment datePickerFragment = new DatePickerFragment(new DatePickerListenner() {
                    @Override
                    public void getDate(int year, int month, int day) {
                        birthdayTv.setText(year+"-"+month+"-"+day);
                    }
                });
                datePickerFragment.show(getSupportFragmentManager(),"datePicker");
                break;
            case R.id.sex_boy:
                sex_tv.setText("男");
                dialog.dismiss();
                break;
            case R.id.sex_girl:
                sex_tv.setText("女");
                dialog.dismiss();
                break;
            case R.id.sex_cancel:
                dialog.dismiss();
                break;
            case R.id.set_password:
                AlertDialog.Builder dialog = new AlertDialog.Builder(SettingActivity.this);
                EditText editText = new EditText(SettingActivity.this);
                dialog.setTitle("重置登陆密码").setView(editText).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).
                setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create().show();
                break;

        }
    }




    private void openAlbum() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent,CHOOSE_PHOTO);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                if (grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    openAlbum();
                }else {
                    Toast.makeText(SettingActivity.this,"拒绝了权限可换不了头像哦！",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode){
            case CHOOSE_PHOTO:
                if (resultCode==RESULT_OK){
                    //判断手机型号
                    if (Build.VERSION.SDK_INT>=19){
                        //4.4及以上的手机使用这个方法处理图片
                        handleImageOnKitKat(data);
                    }else {
                        //4.4以下的手机使用这个方法处理图片
                        handleImageBeforeKitKat(data);
                    }
                }
                break;
        }
    }

    private void handleImageBeforeKitKat(Intent data) {
        Uri uri = data.getData();
        String imagePath = getImagePath(uri,null);
        savePhoto(imagePath);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void handleImageOnKitKat(Intent data) {
        String imagePath = null;
        Uri uri = data.getData();
        if (DocumentsContract.isDocumentUri(this,uri)){
            //如果是document类型的uri，则通过document id处理
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())){
                String id = docId.split(":")[1];//解析出数字格式的id
                String selection = MediaStore.Images.Media._ID+"="+id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,selection);
            }
        }else if ("content".equalsIgnoreCase(uri.getScheme())){
            //如果是content类型的Uri，则使用普通方式处理
            imagePath = getImagePath(uri,null);
        }else if ("file".equalsIgnoreCase(uri.getScheme())){
            //如果是file类型的uri，直接获取图片路径即可
            imagePath = uri.getPath();
        }
        //imagePath已经得到
        savePhoto(imagePath);
    }

    private String getImagePath(Uri uri, String selection) {
        String path = null;
        //通过uri和selection来获取图片真实路径
        Cursor cursor = getContentResolver().query(uri,null,selection,null,null);
        if (cursor!=null){
            if (cursor.moveToFirst()){
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    private static volatile boolean isChange = false;
    private void savePhoto(String imagePath){

        AlertDialog.Builder builder = new AlertDialog.Builder(SettingActivity.this);
        builder.setTitle("提示").setMessage("\n您是否要更换成此头像?")
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        isChange = true;
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("no", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        isChange = false;
                        dialog.dismiss();
                    }
                }).create().show();
        if (!isChange){
            return;
        }
        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
        if (!GlobalUtil.isExternalStorageWritable()){
            return;
        }
        String path = Environment.getExternalStorageDirectory().getAbsolutePath();
        File file = new File(path,"MJ/TouXiang");
        if (!file.exists()){
            file.mkdirs();
        }
        String imageName = "touxiang.jpg";
        File file1 = new File(file,imageName);
        if (file1.exists()){
            file1.delete();
        }
        try {
            file1.createNewFile();
            OutputStream outputStream = new FileOutputStream(file1);
            bitmap.compress(Bitmap.CompressFormat.JPEG,60,outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
