package com.example.bjtu_ins.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.bjtu_ins.R;
import com.example.bjtu_ins.ui.search.SearchFragment;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

public class VideoPlayActivity extends AppCompatActivity implements View.OnClickListener{

    private SensorManager sensorManager;
    private JCVideoPlayer.JCAutoFullscreenListener sensorEventListener;
    private JCVideoPlayerStandard jcVideoPlayerStandard;
    private Button bt_return;

    String imageUrl="https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1578133533988&di=d815f628421ccc1f925c542b74ff6718&imgtype=0&src=http%3A%2F%2Fbpic.588ku.com%2Felement_origin_min_pic%2F16%2F08%2F21%2F1057b91163eff67.jpg";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_play);

        bt_return = (Button)findViewById(R.id.video_return) ;
        bt_return.setOnClickListener(this);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorEventListener = new JCVideoPlayer.JCAutoFullscreenListener();

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String url = bundle.getString("url");

        jcVideoPlayerStandard= (JCVideoPlayerStandard) findViewById(R.id.jc_video);
        //添加视频缩略图
        ImageView thumbImageView = jcVideoPlayerStandard.thumbImageView;
        //使用Glide添加
        Glide.with(this).load(imageUrl).into(thumbImageView );
        //配置jiecaovideoplayer
        jcVideoPlayerStandard.setUp("http://2449.vod.myqcloud.com/2449_22ca37a6ea9011e5acaaf51d105342e3.f20.mp4",jcVideoPlayerStandard.SCREEN_LAYOUT_NORMAL,"视频标题");
    }

    public void onClick(View v){
        switch (v.getId()){
            case R.id.video_return:
                Intent intent = new Intent(this, SearchFragment.class);
                startActivity(intent);
                finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Sensor accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(sensorEventListener, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(sensorEventListener);
        JCVideoPlayer.releaseAllVideos();
    }
    @Override
    public void onBackPressed() {
        if (JCVideoPlayer.backPress()){
            return;
        }
        super.onBackPressed();
    }
}
