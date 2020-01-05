package com.example.bjtu_ins;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;

import com.example.bjtu_ins.utils.HttpUtil;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private IntentFilter intentFilter;
    private LocalReceiver localReceiver;
    private LocalBroadcastManager localBroadcastManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navView = findViewById(R.id.nav_view);
        /*AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_search, R.id.navigation_add,R.id.navigation_message,R.id.navigation_my)
                .build();
         */
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(navView, navController);

        localBroadcastManager = LocalBroadcastManager.getInstance(this);

        intentFilter = new IntentFilter();
        intentFilter.addAction("com.example.broadcast.LESSON_ADVERTISEMENT");
        localReceiver = new LocalReceiver();
        localBroadcastManager.registerReceiver(localReceiver, intentFilter);

        String channelId = "subscribe";
        String channelName = "动态消息";
        int importance = NotificationManager.IMPORTANCE_HIGH;
        createNotificationChannel(channelId, channelName, importance);

        new Thread(new Runnable() {
            @Override
            public void run() {
                String address = "http://123.207.6.140:8080/getCourseAdvertisement";
                while(true){
                    HttpUtil.sendOkHttpRequest(address, new okhttp3.Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {

                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            String responseData = response.body().string();
                            Intent intent = new Intent("com.example.broadcast.LESSON_ADVERTISEMENT");
                            intent.putExtra("data", responseData);
                            localBroadcastManager.sendBroadcast(intent);
                            //Log.d(TAG, "onResponse: 发送广播");
                        }
                    });

                    try {
                        Thread.sleep(10 * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createNotificationChannel(String channelId, String channelName, int importance) {
        NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
        NotificationManager notificationManager = (NotificationManager) getSystemService(
                NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(channel);
    }

    class LocalReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent){
            String data = intent.getStringExtra("data");
            //Log.d(TAG, "onReceive: 接收广播");
            try {
                JSONObject jsonObject = new JSONObject(data);

                String courseName = jsonObject.getString("name");
                String courseTeacher = jsonObject.getString("teacher");
                String courseSchool = jsonObject.getString("school");
                String text = courseSchool + "好友动态：" + courseName + ", 好友： " + courseTeacher;
                //Log.d(TAG, "onReceive: text:" + text);
                Notification notification = new NotificationCompat.Builder(context, "subscribe")
                        .setContentTitle("动态消息提醒")
                        .setContentText(text)
                        .setWhen(System.currentTimeMillis())
                        .setSmallIcon(R.drawable.audio)
                        .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.audio))
                        .build();
                NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                manager.notify(1, notification);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        localBroadcastManager.unregisterReceiver(localReceiver);
    }
}
