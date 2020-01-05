package com.example.bjtu_ins.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.bjtu_ins.R;
import com.example.bjtu_ins.utils.Moment;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecyclerviewAdapter extends RecyclerView.Adapter<RecyclerviewAdapter.ViewHolder> {
    private static final String TAG = "RecyclerviewAdapter";
    private Context context;
    private List<Moment> momentList;
    Moment moment;

    public RecyclerviewAdapter(Context context, List<Moment> momentList){
        super();
        this.context = context;
        this.momentList = momentList;
    }

    @Override
    public RecyclerviewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                            int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_crad, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerviewAdapter.ViewHolder holder,final int position) {
        Log.d(TAG, "onBindViewHolder: ");
        moment = momentList.get(position);

        holder.tv_Name.setText(moment.getName());
        holder.tv_likeNum.setText("共"+moment.getLikeNum()+"次赞");
        holder.tv_content.setText(moment.getContent());
        System.out.println(moment.getName()+"  "+moment.getPictureURL());
        Picasso.get()
                .load(moment.getPictureURL())
                .fit()
                .into(holder.imageView);


    }

    //获取列表长度
    @Override
    public int getItemCount() {
        return momentList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_Name;
        public ImageView imageView;
        public TextView tv_likeNum;
        public TextView tv_content;
        public Button button;

        public ViewHolder(View v) {
            super(v);
            tv_Name = (TextView)v.findViewById(R.id.tv_name);
            imageView = (ImageView)v.findViewById(R.id.iv_send);
            tv_likeNum = (TextView)v.findViewById(R.id.tv_like);
            tv_content = (TextView)v.findViewById(R.id.tv_content);
            button = (Button)v.findViewById(R.id.button_share);
        }
    }
}

