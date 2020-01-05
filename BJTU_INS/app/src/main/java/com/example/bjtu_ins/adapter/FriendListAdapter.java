package com.example.bjtu_ins.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.bjtu_ins.R;
import com.example.bjtu_ins.utils.Freind;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FriendListAdapter extends RecyclerView.Adapter<FriendListAdapter.ViewHolder> {
    private Context context;
    private List<Freind> friendList;
    Freind freind;

    public FriendListAdapter(Context context, List<Freind> friendList){
        super();
        this.context = context;
        this.friendList = friendList;
    }

    @Override
    public FriendListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                             int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_friend, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(FriendListAdapter.ViewHolder holder,final int position) {
        freind = friendList.get(position);

        holder.friend_id.setText(freind.getId());
        holder.friend_name.setText(freind.getName());

        Picasso.get()
                .load("https://youimg1.c-ctrip.com/target/10041700000136agj5FAA_R_671_10000_Q90.jpg?proc=autoorient")
                .fit()
                .into(holder.friend_head);

    }

    //获取列表长度
    @Override
    public int getItemCount() {
        return friendList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView friend_head;
        public TextView friend_id;
        public TextView friend_name;

        public ViewHolder(View v) {
            super(v);
            friend_head = (ImageView)v.findViewById(R.id.friend_head);
            friend_id = (TextView)v.findViewById(R.id.friend_id);
            friend_name = (TextView)v.findViewById(R.id.friend_name);
        }
    }
}
