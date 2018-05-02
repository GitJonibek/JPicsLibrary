package com.example.jonib.jpicslibrary.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.jonib.jpicslibrary.R;
import com.example.jonib.jpicslibrary.model.JsonObject;
import com.jpics.jpics.JPics;

import java.util.List;


public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder>{

    private List<JsonObject> list;
    private Context context;
    private boolean isLoadingAdded = false;
    private static final int ITEM = 0;
    private static final int LOADING = 1;

    public RecyclerViewAdapter(Context context, List<JsonObject> list){
        this.context=context;
        this.list=list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_single_list_layout, null);
        MyViewHolder holder = new MyViewHolder(layoutView);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        JsonObject jsonObject = list.get(position);

        // - Loading Images using JPics custom library which is the package: com.example.jonib.jpics.jpics;
        JPics.with(context).setUrl(jsonObject.getUrls().getRegular()).into(holder.full_item_image);
        JPics.with(context).setUrl(jsonObject.getUser().getProfileImage().getMedium()).into(holder.profile_image);

        // TextView components
        holder.full_name.setText(jsonObject.getUser().getName());
        holder.username.setText("@" + jsonObject.getUser().getUsername());
        holder.likes.setText(jsonObject.getLikes() + " likes");

    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public void add(JsonObject newItem){
        list.add(newItem);
        notifyDataSetChanged();
    }

    public void clear(){
        list.clear();
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        private ImageView profile_image, full_item_image;
        private TextView full_name, username, likes;
        private RelativeLayout mCardView;

        public MyViewHolder(View itemView) {
            super(itemView);

            full_item_image = itemView.findViewById(R.id.full_image_id);
            profile_image = itemView.findViewById(R.id.profile_image);
            full_name = itemView.findViewById(R.id.full_name_id);
            username = itemView.findViewById(R.id.user_name_id);
            likes = itemView.findViewById(R.id.likes_id);

            mCardView = itemView.findViewById(R.id.my_card_view_id);

        }
    }

}
