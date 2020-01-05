package com.example.bjtu_ins.ui.search;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.bjtu_ins.R;
import com.example.bjtu_ins.adapter.BannerAdapter;
import com.example.bjtu_ins.adapter.VideoListAdapter;
import com.example.bjtu_ins.utils.BannerIndicator;
import com.example.bjtu_ins.utils.HttpUtil;
import com.example.bjtu_ins.utils.Moment;
import com.example.bjtu_ins.utils.SmoothLinearLayoutManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Response;

import static android.content.Context.MODE_PRIVATE;

public class SearchFragment extends Fragment {

    private List<Integer> list = new ArrayList<>(4);
    private SearchViewModel searchViewModel;

    private RecyclerView video_recyclerView;
    private String userid;
    private Context context = getActivity();
    private List<Moment> momentList = new ArrayList<Moment>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        searchViewModel =
                ViewModelProviders.of(this).get(SearchViewModel.class);
        View root = inflater.inflate(R.layout.fragment_search, container, false);

        list.add(R.drawable.b1);
        list.add(R.drawable.b2);
        list.add(R.drawable.b3);
        list.add(R.drawable.b4);

        SharedPreferences sp1 = getContext().getSharedPreferences("userid", MODE_PRIVATE);
        userid = sp1.getString("id", null);

        BannerAdapter adapter = new BannerAdapter(this.getContext(), list);
        final RecyclerView recyclerView = root.findViewById(R.id.recycler);
        final SmoothLinearLayoutManager layoutManager = new SmoothLinearLayoutManager(this.getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        recyclerView.scrollToPosition(list.size() * 10);

        PagerSnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);


        final BannerIndicator bannerIndicator = root.findViewById(R.id.indicator);
        bannerIndicator.setNumber(list.size());

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    int i = layoutManager.findFirstVisibleItemPosition() % list.size();
                    bannerIndicator.setPosition(i);
                }
            }
        });

        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
        scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                recyclerView.smoothScrollToPosition(layoutManager.findFirstVisibleItemPosition() + 1);
            }
        }, 2000, 2000, TimeUnit.MILLISECONDS);

        init();

        video_recyclerView = (RecyclerView) root.findViewById(R.id.rv_video);

        //video_recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this.getContext(), 2, GridLayoutManager.VERTICAL, false);
        gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        video_recyclerView.setLayoutManager(gridLayoutManager);

        return root;
    }

    public void init(){

        /*
        String name = "Jelly";
        String imageURL = "https://youimg1.c-ctrip.com/target/10061a000001974mvB0F5_R_671_10000_Q90.jpg?proc=autoorient";
        String content = "欢迎来我的空间踩踩";
        int likeNum = 230;
        Moment moment = new Moment(name,imageURL,likeNum,content);
        momentList.add(moment);

        String name1 = "裂畜";
        String imageURL1 = "https://dimg04.c-ctrip.com/images/10041b000001at9r5DFA5_R_800_10000_Q90.jpg?proc=autoorient";
        String content1 = "overwater";
        int likeNum1 = 100;
        Moment moment1 = new Moment(name1,imageURL1,likeNum1,content1);
        momentList.add(moment1);

        String name2 = "disse";
        String imageURL2 = "https://youimg1.c-ctrip.com/target/10061a000001974mvB0F5_R_671_10000_Q90.jpg?proc=autoorient";
        String content2 = "2020你好\n遇见成都";
        int likeNum2 = 230;
        Moment moment2 = new Moment(name2,imageURL2,likeNum2,content2);
        momentList.add(moment2);


        String name3 = "周杰伦";
        String imageURL3 = "https://youimg1.c-ctrip.com/target/10061a000001974mvB0F5_R_671_10000_Q90.jpg?proc=autoorient";
        String content3 = "告白气球";
        int likeNum3 = 230;
        Moment moment3 = new Moment(name3,imageURL3,likeNum3,content3);
        momentList.add(moment3);

        String name4 = "林俊杰";
        String imageURL4 = "https://youimg1.c-ctrip.com/target/10061a000001974mvB0F5_R_671_10000_Q90.jpg?proc=autoorient";
        String content4 = "不为谁而作的歌";
        int likeNum4 = 230;
        Moment moment4 = new Moment(name4,imageURL4,likeNum4,content4);
        momentList.add(moment4);

         */


        String body = "userid="+userid;
        String Address = "http://192.168.0.112:5050//getfriendmessage?"+body;
        HttpUtil.sendOkHttpRequest(Address,new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                System.out.println(response);
                String responseData = response.body().string();
                showResponse(responseData);
            }
        });
    }

    private void showResponse(final String response){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                System.out.println(response);

                JSONObject jsonObject = JSONObject.parseObject(response);
                JSONArray jsonArray = jsonObject.getJSONArray("response");
                System.out.println(jsonArray.size());
                for(int i=0;i<jsonArray.size();i++){
                    JSONArray jsonArray1 = jsonArray.getJSONArray(i);

                    String name = jsonArray1.getString(0);
                    String imageURL = jsonArray1.getString(4);
                    String content = jsonArray1.getString(3);
                    int likeNum = jsonArray1.getIntValue(6);
                    System.out.println(jsonArray1.toJSONString());
                    Moment moment = new Moment(name,imageURL,likeNum,content);

                    if(moment!=null){
                        momentList.add(moment);
                    }
                }
                VideoListAdapter vlAdapter = new VideoListAdapter(context,momentList);
                video_recyclerView.setAdapter(vlAdapter);
            }
        });
    }
}