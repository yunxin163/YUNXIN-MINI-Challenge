//package com.tzutalin.dlibtest;
//
//import android.content.Context;
//import android.support.annotation.NonNull;
////import android.support.design.button.MaterialButton;
//import android.support.v7.widget.RecyclerView;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.TextView;
//
//import java.io.IOException;
//import java.net.URL;
//import java.util.List;
//
//import de.hdodenhof.circleimageview.CircleImageView;
//import okhttp3.Call;
//import okhttp3.Callback;
//import okhttp3.Response;
//
//public class SearchRoleAdapter extends RecyclerView.Adapter<SearchRoleAdapter.ViewHolder> {
//    private List<Role> roleList=null;
//    public static URL url=null;
//    private Context context;
//    public static final int HAVE_BEEN_FRIEND = 1;
//
//    @NonNull
//    @Override
//    public SearchRoleAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
//        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.searchitem,viewGroup,false);
//        final ViewHolder holder = new ViewHolder(view);
//        return holder;
//    }
//    public SearchRoleAdapter(Context context,List<Role> roleList){
//        this.context=context;
//        this.roleList=roleList;
//    }
//    @Override
//    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
//        Role role = roleList.get(i);
//        viewHolder.roleImage.setImageResource(role.getImageId());
//        viewHolder.rolename.setText(role.getName());
//        viewHolder.lastTalk.setText(role.getLastTalk());
//        if(LoginActivity.mine.getRolePhone().contains(role.getPhone())){
//            viewHolder.materialButton.setClickable(false);
//            viewHolder.materialButton.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
//        }else {
//            viewHolder.materialButton.setClickable(true);
//            viewHolder.materialButton.setBackgroundColor(context.getResources().getColor(R.color.colorAccent));
//            viewHolder.materialButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
////                    Link.addRole(url, role.getPhone(), new Callback() {
////                        @Override
////                        public void onFailure(Call call, IOException e) {
////
////                        }
////
////                        @Override
////                        public void onResponse(Call call, Response response) throws IOException {
////                            if(response.isSuccessful()){
////                                viewHolder.materialButton.setClickable(false);
////                                viewHolder.materialButton.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
////                            }
////
////                        }
////                    });
////                }
////            });
//        }
//
//
//    }
//
//    @Override
//    public int getItemCount() {
//        if(roleList==null){
//            return 0;
//        }else {
//            return roleList.size();
//        }
//
//    }
//
//    static class ViewHolder extends RecyclerView.ViewHolder{
//
//        CircleImageView roleImage;
//        TextView rolename;
//        TextView lastTalk;
//        Button materialButton;
//        public ViewHolder(@NonNull View itemView) {
//            super(itemView);
//            roleImage = itemView.findViewById(R.id.role_image);
//            rolename = itemView.findViewById(R.id.role_name);
//            lastTalk = itemView.findViewById(R.id.last_talk);
//            materialButton = itemView.findViewById(R.id.add_friend_button);
//
//        }
//    }
//}
