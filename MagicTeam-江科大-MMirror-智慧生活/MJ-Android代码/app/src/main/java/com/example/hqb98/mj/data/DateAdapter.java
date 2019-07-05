package com.example.hqb98.mj.data;

import android.content.Intent;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hqb98.mj.activity.DetailDate;
import com.example.hqb98.mj.R;

import java.util.List;
import java.util.Random;

public class DateAdapter extends RecyclerView.Adapter<DateAdapter.ViewHolder> {
    private List<Date> mDateList;

    public DateAdapter(List<Date> dateList){
        mDateList = dateList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.date_item,viewGroup,false);
        final ViewHolder holder = new ViewHolder(view);
        holder.dateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                Date date = mDateList.get(position);
                int i = date.getId();
                int[] extra = new int[]{position,i};
                Intent intent = new Intent(v.getContext(), DetailDate.class);
                intent.putExtra("Detail_Date",extra);
                v.getContext().startActivity(intent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Date date = mDateList.get(i);
        viewHolder.date_image.setImageResource(date.getDate_image());
        int j = (int) (Math.random()*5);

        Log.d("dateddddd",j+"");
        Random random = new Random();
        int n = random.nextInt(5);
        Log.d("dateddddd",n+"");
//        if (n==0){
//            viewHolder.cardView.setBackgroundColor(viewHolder.cardView.getContext().getResources().getColor(R.color.card1));
//        }else if (n==1){
//            viewHolder.cardView.setBackgroundColor(viewHolder.cardView.getContext().getResources().getColor(R.color.card2));
//        }else if (n==2){
//            viewHolder.cardView.setBackgroundColor(viewHolder.cardView.getContext().getResources().getColor(R.color.card3));
//        }else if (n==3){
//            viewHolder.cardView.setBackgroundColor(viewHolder.cardView.getContext().getResources().getColor(R.color.card4));
//        }else{
//            viewHolder.cardView.setBackgroundColor(viewHolder.cardView.getContext().getResources().getColor(R.color.card5));
//        }
        viewHolder.date_type.setText(date.getDate_type());
        viewHolder.date_title.setText(date.getDate_title());
        viewHolder.date_time.setText(date.getDate_time());
    }

    @Override
    public int getItemCount() {
        return mDateList.size();
    }

    static public class ViewHolder extends RecyclerView.ViewHolder {
        View dateView;
        ImageView date_image;
        TextView date_type;
        TextView date_title;
        TextView date_time;
        CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            dateView = itemView;
            cardView = (CardView)itemView.findViewById(R.id.card);
            date_image = (ImageView)itemView.findViewById(R.id.date_image);
            date_type = (TextView)itemView.findViewById(R.id.date_type);
            date_title = (TextView)itemView.findViewById(R.id.date_title);
            date_time = (TextView)itemView.findViewById(R.id.date_time);
        }
    }

    public void addData(int position,Date date){
        mDateList.add(date);
        notifyItemInserted(position);
    }

    public void updateData(int position,Date date){
        mDateList.set(position,date);
        notifyItemChanged(position);
        notifyDataSetChanged();
    }
}
