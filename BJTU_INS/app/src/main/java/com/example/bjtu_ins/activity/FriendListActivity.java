package com.example.bjtu_ins.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.bjtu_ins.R;
import com.example.bjtu_ins.adapter.FriendListAdapter;
import com.example.bjtu_ins.utils.Freind;
import com.example.bjtu_ins.utils.HttpUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

public class FriendListActivity extends AppCompatActivity implements View.OnClickListener{

    private List<Freind> friendList = new ArrayList<Freind>();
    private String userid;
    private RecyclerView recyclerView;
    private Context mContext = this;
    private Button return_button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_list);

        recyclerView = (RecyclerView) findViewById(R.id._rv_friend_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        return_button = (Button)findViewById(R.id.btn_return);
        return_button.setOnClickListener(this);

        SharedPreferences sp1 = getSharedPreferences("userid", MODE_PRIVATE);
        userid = sp1.getString("id", null);

        init();

        FriendListAdapter reAdapter = new FriendListAdapter(this,friendList);
        recyclerView.setAdapter(reAdapter);
    }

    public void onClick(View v){
        switch (v.getId()){
            case R.id.btn_return:
                finish();
        }
    }

    public void init(){

        /*
        String name = "周杰伦";
        String imageURL = "https://youimg1.c-ctrip.com/target/10061a000001974mvB0F5_R_671_10000_Q90.jpg?proc=autoorient";
        String content = "2020你好\n遇见成都";
        int likeNum = 230;
        Moment moment = new Moment(name,imageURL,likeNum,content);
        momentList.add(moment);

        String name1 = "林俊杰";
        String imageURL1 = "https://dimg04.c-ctrip.com/images/10041b000001at9r5DFA5_R_800_10000_Q90.jpg?proc=autoorient";
        String content1 = "不为谁而作的歌";
        int likeNum1 = 100;
        Moment moment1 = new Moment(name1,imageURL1,likeNum1,content1);
        momentList.add(moment1);

        String name2 = "周杰伦";
        String imageURL2 = "https://youimg1.c-ctrip.com/target/10061a000001974mvB0F5_R_671_10000_Q90.jpg?proc=autoorient";
        String content2 = "2020你好\n遇见成都";
        int likeNum2 = 230;
        Moment moment2 = new Moment(name2,imageURL2,likeNum2,content2);
        momentList.add(moment2);

        String name3 = "周杰伦";
        String imageURL3 = "https://youimg1.c-ctrip.com/target/10061a000001974mvB0F5_R_671_10000_Q90.jpg?proc=autoorient";
        String content3 = "2020你好\n遇见成都";
        int likeNum3 = 230;
        Moment moment3 = new Moment(name3,imageURL3,likeNum3,content3);
        momentList.add(moment3);

        String name4 = "周杰伦";
        String imageURL4 = "https://youimg1.c-ctrip.com/target/10061a000001974mvB0F5_R_671_10000_Q90.jpg?proc=autoorient";
        String content4 = "2020你好\n遇见成都";
        int likeNum4 = 230;
        Moment moment4 = new Moment(name4,imageURL4,likeNum4,content4);
        momentList.add(moment4);
         */

        String body = "userid="+userid;
        String Address = "http://192.168.0.112:5050//getfriendlist?"+body;
        System.out.println(Address);
        HttpUtil.sendOkHttpRequest(Address,new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                showResponse(responseData);
            }
        });
    }

    private void showResponse(final String response){
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                System.out.println(response);

                JSONObject jsonObject = JSONObject.parseObject(response);
                JSONArray jsonArray = jsonObject.getJSONArray("response");
                System.out.println(jsonArray.size());
                for(int i=0;i<jsonArray.size();i++){
                    JSONArray jsonArray1 = jsonArray.getJSONArray(i);

                    String id = jsonArray1.getString(0);
                    String name = jsonArray1.getString(1);
                    Freind freind = new Freind(id,name);

                    friendList.add(freind);
                }
                FriendListAdapter reAdapter = new FriendListAdapter(mContext,friendList);
                recyclerView.setAdapter(reAdapter);
            }
        });
    }
}
