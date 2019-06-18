package com.example.hqb98.mj.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hqb98.mj.data.Corse;
import com.example.hqb98.mj.util.ActivityCollector;
import com.example.hqb98.mj.R;
import com.example.hqb98.mj.activity.LoginActivity;
import com.example.hqb98.mj.data.StudentInfo;
import com.example.hqb98.mj.util.GlobalUtil;
import com.example.hqb98.mj.util.HttpUtil;
import com.example.hqb98.mj.util.SharedUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.litepal.LitePal;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

public class MeFragment extends Fragment implements View.OnClickListener {
    private View view;
    private TextView me_name;
    private TextView me_number;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private String loginString;
    private StudentInfo studentInfo;
    private LinearLayout fankuigeikaifazhe;
    private LinearLayout qiehuanzhanghao;
    private LinearLayout tuichudenglu;
    private LinearLayout jianchagengxin;
    private LinearLayout wodexinxi;
    private LinearLayout shezhi;
    private CircleImageView touxiang;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.me_layout,container,false);
        preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        editor = preferences.edit();
        loginString = preferences.getString("loginString","");
        studentInfo = HttpUtil.handleLoginString(loginString);
        initView();
        initParams();

        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        String name = SharedUtil.read("username","");
        me_name.setText(name);
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


    private void initView() {
        me_name = (TextView)view.findViewById(R.id.me_name);
        me_number = (TextView)view.findViewById(R.id.me_number);
        fankuigeikaifazhe = (LinearLayout)view.findViewById(R.id.me_fankuigeikaifazhe);
        qiehuanzhanghao = (LinearLayout)view.findViewById(R.id.me_qiehuanzhanghao);
        tuichudenglu = (LinearLayout)view.findViewById(R.id.me_tuichu);
        jianchagengxin = (LinearLayout)view.findViewById(R.id.me_jianchagengxin);
        shezhi = (LinearLayout)view.findViewById(R.id.me_shezhi);
        fankuigeikaifazhe.setOnClickListener(this);
        qiehuanzhanghao.setOnClickListener(this);
        tuichudenglu.setOnClickListener(this);
        jianchagengxin.setOnClickListener(this);
        shezhi.setOnClickListener(this);
        String name = preferences.getString("username","");
        String number = preferences.getString("account","");
        me_name.setText(name);
        me_number.setText("账号："+number);
//        wodexinxi = (LinearLayout)view.findViewById(R.id.me_wodexinxi);
//        wodexinxi.setOnClickListener(this);
        touxiang = (CircleImageView)view.findViewById(R.id.me_touxiang);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.me_fankuigeikaifazhe:
                if (isQQAvailable(getContext())){
                    String url="mqqwpa://im/chat?chat_type=wpa&uin=996595179";
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                }else {
                    Toast.makeText(getContext(),"您未安装QQ",Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.me_qiehuanzhanghao:
                ActivityCollector.finishAll();
                Intent intent = new Intent(getContext(),LoginActivity.class);
                startActivity(intent);
                editor = preferences.edit();
                editor.putBoolean("autologin",false);
                SharedUtil.save("device_bind",false);
                editor.apply();
                LitePal.deleteAll(Corse.class);
                break;
            case R.id.me_tuichu:
                LinearLayout popup = (LinearLayout)getActivity().getLayoutInflater().inflate(R.layout.me_exit,null);
                final AlertDialog.Builder dialog= new AlertDialog.Builder(getContext());
                final AlertDialog alertDialog = dialog.setView(popup)
                        .create();
                        alertDialog.show();
                Button ok = (Button)popup.findViewById(R.id.me_ok);
                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ActivityCollector.finishAll();
                        LitePal.deleteAll(Corse.class);
                        SharedUtil.clearAll();
                        SharedUtil.save("device_bind",false);

                    }
                });
                Button cancel = (Button)popup.findViewById(R.id.me_cancel);
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
                break;
            case R.id.me_jianchagengxin:
                Log.d("mefragmentdd","dianji");
                Toast.makeText(view.getContext(),"现在已经是最新版本了哦",Toast.LENGTH_SHORT).show();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(view.getContext(),"现在已经是最新版本了哦",Toast.LENGTH_SHORT).show();
                    }
                });

                break;
//            case R.id.me_wodexinxi:
//                break;
            case R.id.me_shezhi:
                Intent intent1 = new Intent("com.example.hqb98.mj.SETTING");
                intent1.addCategory("com.example.hqb98.mj.SETTING");
                startActivity(intent1);
                break;
                default:

                    break;
        }
    }
    Uri imageUri;
    public static final int TAKE_PHOTO = 123;
    private void openCamera() {

        try {
            File outputImage = new File(getContext().getExternalCacheDir(),"attendence.jpg");
            if (outputImage.exists()){
                outputImage.delete();
            }
            outputImage.createNewFile();
            if (Build.VERSION.SDK_INT>=24){
                imageUri = FileProvider.getUriForFile(getContext(),"com.example.hqb98.aiattendance",outputImage);
            }else{
                imageUri = Uri.fromFile(outputImage);
            }
            Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
            intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
            startActivityForResult(intent,TAKE_PHOTO);
        }catch (IOException e){
            e.printStackTrace();
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case TAKE_PHOTO:
                if (resultCode==RESULT_OK){
                    Toast.makeText(getContext(),"paizhao",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    public static boolean isQQAvailable(Context context) {
        final PackageManager mPackageManager = context.getPackageManager();
        List<PackageInfo> pinfo = mPackageManager.getInstalledPackages(0);
        if(pinfo !=null) {
            for(int i =0;i < pinfo.size();i++) {
                String pn = pinfo.get(i).packageName;
                if(pn.equals("com.tencent.mobileqq")) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    openCamera();
                }else {
                    Toast.makeText(getContext(),"拒绝了权限可不能拍照哦！",Toast.LENGTH_SHORT).show();
                }
                break;
        }

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
