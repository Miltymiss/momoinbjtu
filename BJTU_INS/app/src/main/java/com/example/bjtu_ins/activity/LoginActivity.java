package com.example.bjtu_ins.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.example.bjtu_ins.MainActivity;
import com.example.bjtu_ins.R;
import com.example.bjtu_ins.utils.HttpUtil;
import com.example.bjtu_ins.utils.JellyInterpolator;
import com.example.bjtu_ins.utils.RegexUtil;
import com.example.bjtu_ins.utils.SharedPreferencesUtil;

import java.io.IOException;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    private TextView mBtnLogin;
    private View progress;
    private View mInputLayout;
    private float mWidth, mHeight;
    private LinearLayout mName, mPsw;
    private RelativeLayout mBtnSign;
    private SharedPreferencesUtil spu;
    private EditText tv_name;
    private EditText tv_pass;
    private Button qq_login;
    private Button wechat_login;

    private String user;

    private static final int ANIMATION_DURATION = 3000;
    private static final float SCALE_END = 1.15F;


    private static final int[] SPLASH_ARRAY = {
            R.drawable.splash0,
            R.drawable.splash1,
            R.drawable.splash2,
            R.drawable.splash3,
    };

    private ImageView mSplashImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);

        initView();
        spu= SharedPreferencesUtil.getInstance(getApplicationContext());
    }

    private void initView() {
        mSplashImage = (ImageView) findViewById(R.id.backgroud_png);///////////
        Random r = new Random(SystemClock.elapsedRealtime());
        //设置随机的图片
        mSplashImage.setImageResource(SPLASH_ARRAY[r.nextInt(SPLASH_ARRAY.length)]);
        animateImage();

        mBtnLogin = (TextView) findViewById(R.id.main_btn_login);
        progress = findViewById(R.id.layout_progress);
        mInputLayout = findViewById(R.id.input_layout);
        mName = (LinearLayout) findViewById(R.id.input_layout_name);
        mPsw = (LinearLayout) findViewById(R.id.input_layout_psw);
        mBtnSign = (RelativeLayout) findViewById(R.id.main_title);

        tv_name = (EditText)findViewById(R.id.input_name);
        tv_pass = (EditText)findViewById(R.id.input_pass);

        qq_login = (Button)findViewById(R.id.qq_login);
        wechat_login = (Button)findViewById(R.id.wechat_login);

        mBtnLogin.setOnClickListener(this);
        mBtnSign.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        // 计算出控件的高与宽

        switch (v.getId()){
            case R.id.main_btn_login :
                mWidth = mBtnLogin.getMeasuredWidth();
                mHeight = mBtnLogin.getMeasuredHeight();
                // 隐藏输入框
                mName.setVisibility(View.INVISIBLE);
                mPsw.setVisibility(View.INVISIBLE);
                inputAnimator(mInputLayout, mWidth, mHeight);

                Timer timer = new Timer();
                TimerTask task = new TimerTask() {
                    @Override
                    public void run() {
                        login();
                    }
                };
                timer.schedule(task, 2000);

                /*
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();

                 */
                break;

            case R.id.main_title:
                Intent intent1 = new Intent(this,RegisterActivity.class);
                startActivity(intent1);
                finish();
                break;
        }
    }

    private void login(){
        user=tv_name.getText().toString().trim();
        //判断是否输入了邮箱
        if(TextUtils.isEmpty(user)){
            Toast.makeText(this,R.string.email_hint,Toast.LENGTH_SHORT).show();
            return;
        }
        //判断邮箱格式是否正确
        System.out.println(user);
        if(!RegexUtil.isEmail(user)){
            //Looper.prepare();
            Toast.makeText(this,R.string.error_password_format,Toast.LENGTH_SHORT).show();
            return;
        }

        //判断是否输入了密码
        String pass=tv_pass.getText().toString().trim();
        if(TextUtils.isEmpty(pass)){
            Toast.makeText(this,R.string.password_hint,Toast.LENGTH_SHORT).show();
            return;
        }
        //判断密码长度
        if(pass.length()<6||pass.length()>15){
            Toast.makeText(this,R.string.error_password_length,Toast.LENGTH_SHORT).show();
            return;
        }

        //String Address = "http://192.168.0.112:5050/login?userid=123333@qq.com&password=123456";
        String body = "userid="+user+"&password="+pass;
        String Address = "http://192.168.0.112:5050/login?"+body;
        System.out.println(Address);
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
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                System.out.println(response);
                JSONObject jsonObject = JSONObject.parseObject(response);
                boolean status = jsonObject.getBoolean("status");
                if(status){
                    spu.setLogin(true);
                    SharedPreferences sp = getSharedPreferences("userid", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("id", user);
                    editor.commit();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);

                    intent.putExtra("userid",user);
                    startActivity(intent);
                    finish();
                }else{
                    Toast.makeText(LoginActivity.this,R.string.error_email_password,Toast.LENGTH_SHORT).show();
                    recovery();
                }
            }
        });
    }

    /*
     * 输入框的动画效果
     * @param view  控件
     * @param w  宽
     * @param h  高
     */
    private void inputAnimator(final View view, float w, float h) {
        AnimatorSet set = new AnimatorSet();
        ValueAnimator animator = ValueAnimator.ofFloat(0, w);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (Float) animation.getAnimatedValue();
                ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) view
                        .getLayoutParams();
                params.leftMargin = (int) value;
                params.rightMargin = (int) value;
                view.setLayoutParams(params);
            }
        });

        ObjectAnimator animator2 = ObjectAnimator.ofFloat(mInputLayout,
                "scaleX", 1f, 0.5f);
        set.setDuration(1000);
        set.setInterpolator(new AccelerateDecelerateInterpolator());
        set.playTogether(animator, animator2);
        set.start();
        set.addListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                /**
                 * 动画结束后，先显示加载的动画，然后再隐藏输入框
                 */
                progress.setVisibility(View.VISIBLE);
                progressAnimator(progress);
                mInputLayout.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }
        });
    }

    /**
     * 出现进度动
     * @param view
     */
    private void progressAnimator(final View view) {
        PropertyValuesHolder animator = PropertyValuesHolder.ofFloat("scaleX",
                0.5f, 1f);
        PropertyValuesHolder animator2 = PropertyValuesHolder.ofFloat("scaleY",
                0.5f, 1f);
        ObjectAnimator animator3 = ObjectAnimator.ofPropertyValuesHolder(view,
                animator, animator2);
        animator3.setDuration(10000);
        animator3.setInterpolator(new JellyInterpolator());
        animator3.start();
    }
    /**
     * 恢复初始状态
     */
    private void recovery() {
        progress.setVisibility(View.GONE);
        mInputLayout.setVisibility(View.VISIBLE);
        mName.setVisibility(View.VISIBLE);
        mPsw.setVisibility(View.VISIBLE);

        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) mInputLayout.getLayoutParams();
        params.leftMargin = 0;
        params.rightMargin = 0;
        mInputLayout.setLayoutParams(params);

        ObjectAnimator animator2 = ObjectAnimator.ofFloat(mInputLayout, "scaleX", 0.5f,1f );
        animator2.setDuration(500);
        animator2.setInterpolator(new AccelerateDecelerateInterpolator());
        animator2.start();
    }

    private void animateImage() {
        //表示从1f --> 1.13f 的变化过程
        ObjectAnimator animatorX = ObjectAnimator.ofFloat(mSplashImage, "scaleX", 1f, SCALE_END);
        ObjectAnimator animatorY = ObjectAnimator.ofFloat(mSplashImage, "scaleY", 1f, SCALE_END);
        animatorX.setRepeatCount(ValueAnimator.INFINITE);
        animatorY.setRepeatCount(ValueAnimator.INFINITE);
        //表示多个动画的协同工作
        final AnimatorSet set = new AnimatorSet();
        set.setDuration(ANIMATION_DURATION).play(animatorX).with(animatorY);

        set.start();
        //对动画的监听,动画结束后立马跳转到主页面上
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationRepeat(Animator animation) {
                set.start();
            }
        });
    }
}
