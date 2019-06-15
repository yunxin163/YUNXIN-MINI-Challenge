package com.tzutalin.dlibtest;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

public class MsgAdapter extends RecyclerView.Adapter<MsgAdapter.ViewHolder> {
    private List<Msg> msgList;
    public MsgAdapter(List<Msg> msgList){
        this.msgList=msgList;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.msgitem,viewGroup,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Msg msg = msgList.get(i);
        if(msg.getType()== Msg.SEND_MASSAGE){
            viewHolder.leftLayout.setVisibility(View.GONE);
            viewHolder.rightLayout.setVisibility(View.VISIBLE);
            viewHolder.imageView2.setImageResource(msg.getImageID());
            viewHolder.textView2.setText(msg.getMessage());
            viewHolder.textView1.setVisibility(View.GONE);
            viewHolder.imageView1.setVisibility(View.GONE);

        }else if(msg.getType()== Msg.RECEIVED_MASSAGE){
            viewHolder.leftLayout.setVisibility(View.VISIBLE);
            viewHolder.rightLayout.setVisibility(View.GONE);
            viewHolder.textView2.setVisibility(View.GONE);
            viewHolder.imageView2.setVisibility(View.GONE);
            viewHolder.imageView1.setImageResource(msg.getImageID());
            viewHolder.textView1.setText(msg.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return msgList.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView1;
        TextView textView1;
        ImageView imageView2;
        TextView textView2;
        RelativeLayout leftLayout;
        RelativeLayout rightLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView1=itemView.findViewById(R.id.left_icon);
            textView1=itemView.findViewById(R.id.left_msg);
            imageView2=itemView.findViewById(R.id.right_icon);
            textView2=itemView.findViewById(R.id.right_msg);
            leftLayout =  itemView.findViewById(R.id.left_layout);
            rightLayout =  itemView.findViewById(R.id.right_layout);
        }
    }
}
