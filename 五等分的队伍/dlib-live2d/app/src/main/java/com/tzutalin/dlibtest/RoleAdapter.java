package com.tzutalin.dlibtest;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.support.v4.app.ActivityCompat.startActivityForResult;
import static android.support.v4.content.ContextCompat.startActivity;

public class RoleAdapter extends RecyclerView.Adapter<RoleAdapter.ViewHolder> {
    private List<Role> roleList;
    private Handler handler;
    private Context context;
    private static final int RESULT_LOAD_IMG = 1;
    private static final int REQUEST_CODE_PERMISSION = 2;
    public void setHandler(Handler handler) {
        this.handler = handler;
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.roleitem,viewGroup,false);
        final ViewHolder holder = new ViewHolder(view);
        holder.roleView.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                /*
                发送视频通话请求
                 */
                Intent intent = new Intent(context, CameraActivity.class);
                context.startActivity(intent);
            }
        });
        return holder;
    }
    public RoleAdapter(Context context,List<Role> roleList){
        this.context=context;
        this.roleList=roleList;
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Role role = roleList.get(i);
        Bitmap bitmap = BitmapFactory.decodeFile(context.getExternalCacheDir()+"/"+"avatar/"+role.getImage());
        Log.d("imageRole",role.getImage());
        viewHolder.roleImage.setImageBitmap(bitmap);
        viewHolder.rolename.setText(role.getName());
        viewHolder.lastTalk.setText(role.getLastTalk());
        viewHolder.lastTalkDate.setText(role.getLastTalkDate());
    }

    @Override
    public int getItemCount() {
        if(roleList==null){
            return 0;
        }else {
            return roleList.size();
        }

    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        View roleView;
        CircleImageView roleImage;
        TextView rolename;
        TextView lastTalk;
        TextView lastTalkDate;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            roleView = itemView;
            roleImage = itemView.findViewById(R.id.role_image);
            rolename = itemView.findViewById(R.id.role_name);
            lastTalk = itemView.findViewById(R.id.last_talk);
            lastTalkDate = itemView.findViewById(R.id.last_talk_date);
        }
    }
}
