package com.example.hqb98.mj.data;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.hqb98.mj.R;
import com.example.hqb98.mj.callback.ResultCallback;
import com.example.hqb98.mj.fragment.MonitorFragment;
import com.example.hqb98.mj.util.OkHttpEngine;

import org.litepal.LitePal;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class PictureAdapter extends RecyclerView.Adapter<PictureAdapter.ViewHolder> {
    private List<Picture> pictureList;

    public PictureAdapter(List<Picture> pictureList) {
        this.pictureList = pictureList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.monitor_item_layout,viewGroup,false);
        final ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = viewHolder.getAdapterPosition();
                LitePal.delete(Picture.class,pictureList.get(position).getId());
                MonitorFragment.pictureList.remove(position);
                notifyItemRemoved(position);

//                notifyItemChanged(position);
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Picture picture = pictureList.get(i);

        Glide.with(viewHolder.view.getContext()).load(picture.getUrl()).diskCacheStrategy(DiskCacheStrategy.ALL).centerCrop().into(viewHolder.imageView);
        viewHolder.textView.setText(picture.getTime());
    }

    @Override
    public int getItemCount() {
        return pictureList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        View view;
        ImageView imageView;
        TextView textView;
        Button deleteButton;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            imageView = (ImageView)itemView.findViewById(R.id.monitor_item_image);
            textView = (TextView)itemView.findViewById(R.id.monitor_item_text);
            deleteButton = (Button)itemView.findViewById(R.id.monitor_item_button);
        }
    }
}
