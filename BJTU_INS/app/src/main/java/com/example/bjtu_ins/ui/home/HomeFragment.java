package com.example.bjtu_ins.ui.home;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.bjtu_ins.R;
import com.example.bjtu_ins.adapter.RecyclerviewAdapter;
import com.example.bjtu_ins.utils.HttpUtil;
import com.example.bjtu_ins.utils.Moment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

import static android.content.Context.MODE_PRIVATE;

public class HomeFragment extends Fragment {
    private HomeViewModel homeViewModel;
    private List<Moment> momentList = new ArrayList<Moment>();
    private RecyclerView recyclerView;
    private String userid;
    private Context context = getActivity();
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = (RecyclerView) root.findViewById(R.id.rv_home);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));            //*************************

        SharedPreferences sp1 = getContext().getSharedPreferences("userid", MODE_PRIVATE);
        userid = sp1.getString("id", null);


        init();

        RecyclerviewAdapter reAdapter = new RecyclerviewAdapter(context,momentList);
        recyclerView.setAdapter(reAdapter);

        return root;
    }

    public void init(){


        String name = "Moemo";
        String imageURL = "https://youimg1.c-ctrip.com/target/10061a000001974mvB0F5_R_671_10000_Q90.jpg?proc=autoorient";
        String content = "2020你好\n遇见成都";
        int likeNum = 230;
        Moment moment = new Moment(name,imageURL,likeNum,content);
        momentList.add(moment);

        String name1 = "春光炸裂";
        String imageURL1 = "https://youimg1.c-ctrip.com/target/100c1b000001c1qjs6883_R_1024_10000_Q90.jpg?proc=autoorient";
        String content1 = "不为谁而作的歌";
        int likeNum1 = 100;
        Moment moment1 = new Moment(name1,imageURL1,likeNum1,content1);
        momentList.add(moment1);

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
                    Moment moment = new Moment(name,imageURL,likeNum,content);

                    momentList.add(moment);
                }
                RecyclerviewAdapter reAdapter = new RecyclerviewAdapter(context,momentList);
                recyclerView.setAdapter(reAdapter);
            }
        });
    }
}