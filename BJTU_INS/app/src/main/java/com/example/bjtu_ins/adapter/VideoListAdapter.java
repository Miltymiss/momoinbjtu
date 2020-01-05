package com.example.bjtu_ins.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.bjtu_ins.R;
import com.example.bjtu_ins.activity.LoginActivity;
import com.example.bjtu_ins.activity.VideoPlayActivity;
import com.example.bjtu_ins.utils.Moment;
import com.squareup.picasso.Picasso;

import java.util.List;

public class VideoListAdapter extends RecyclerView.Adapter<VideoListAdapter.ViewHolder> {
    private static final String TAG = "RecyclerviewAdapter";
    private Context context;
    private List<Moment> videoList;
    Moment moment;

    public VideoListAdapter(Context context, List<Moment> momentList){
        super();
        this.context = context;
        this.videoList = momentList;
    }

    @Override
    public VideoListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                             int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.video_card, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(VideoListAdapter.ViewHolder holder,final int position) {
        Log.d(TAG, "onBindViewHolder: ");
        moment = videoList.get(position);

        holder.tv_username.setText(moment.getName());
        holder.tv_text.setText(moment.getContent());
        System.out.println(moment.getName()+"  "+moment.getPictureURL());
        Picasso.get()
                .load(moment.getPictureURL())
                .fit()
                .into(holder.iv_video_image);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                Intent intent = new Intent(context, VideoPlayActivity.class);
                String idContent = videoList.get(position).getPictureURL();
                intent.putExtra("idContent",idContent);
                context.startActivity(intent);
                 */
                Intent intent = new Intent(context, LoginActivity.class);
                context.startActivity(intent);
            }
        });
    }

    //获取列表长度
    @Override
    public int getItemCount() {
        System.out.println("长度"+videoList.size());
        return videoList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView iv_video_image;
        public TextView tv_text;
        public TextView tv_username;

        public ViewHolder(View v) {
            super(v);
            iv_video_image = (ImageView)v.findViewById(R.id.iv_video_image);
            tv_text = (TextView)v.findViewById(R.id.tv_text);
            tv_username = (TextView)v.findViewById(R.id.tv_username);
        }
    }
}
