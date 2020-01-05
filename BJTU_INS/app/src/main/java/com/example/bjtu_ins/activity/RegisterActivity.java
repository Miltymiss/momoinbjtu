package com.example.bjtu_ins.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.example.bjtu_ins.MainActivity;
import com.example.bjtu_ins.R;
import com.example.bjtu_ins.utils.HttpUtil;
import com.example.bjtu_ins.utils.RegexUtil;
import com.example.bjtu_ins.utils.SharedPreferencesUtil;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{
    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }
    private EditText et_username;
    private EditText et_mail;
    private EditText et_password;
    private EditText et_password1;

    private Button bt_sign;
    private SharedPreferencesUtil spu;
    private Button bt_return;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        et_username = (EditText)findViewById(R.id.name_input);
        et_mail = (EditText)findViewById(R.id.account_input);
        et_password = (EditText)findViewById(R.id.password_input) ;
        et_password1 = (EditText)findViewById(R.id.password_confirm_input);

        bt_sign = (Button)findViewById(R.id.btn_sign);
        bt_return = (Button)findViewById(R.id.btn_return) ;

        bt_sign.setOnClickListener(this);
        bt_return.setOnClickListener(this);

        spu=SharedPreferencesUtil.getInstance(getApplicationContext());
    }

    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.btn_sign:
                register();
                break;

            case R.id.btn_return:
                Intent intent = new Intent(this,LoginActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }

    public void register(){
        String userName = et_username.getText().toString().trim();
        String mail = et_mail.getText().toString().trim();
        String password = et_password.getText().toString().trim();
        String password1 = et_password1.getText().toString().trim();

        System.out.println(userName+"-"+password);

        if(TextUtils.isEmpty(userName)){
            Toast.makeText(this,"请输入昵称",Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(mail)){
            Toast.makeText(this,"请输入邮箱",Toast.LENGTH_SHORT).show();
            return;
        }
        //判断邮箱格式是否正确
        if(!RegexUtil.isEmail(mail)){
            Toast.makeText(this,R.string.error_password_format,Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(mail)){
            Toast.makeText(this,"请输入密码",Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(mail)){
            Toast.makeText(this,"请再次输入密码",Toast.LENGTH_SHORT).show();
            return;
        }

        if(!password.equals(password1)){
            Toast.makeText(this,"两次密码不一致",Toast.LENGTH_SHORT).show();
            return;
        }//当两次密码一致
        if(password.equals(password1)){
            String body = "username="+userName+"&userid="+mail+"&password="+password;
            //String Address = "http://192.168.0.112:5050/login?userid=123333@qq.com&password=123456";
            String Address = "http://192.168.0.112:5050/register?"+body;
            System.out.println(Address);
            HttpUtil.sendOkHttpRequest(Address,new okhttp3.Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String responseData = response.body().string();
                    System.out.println("收到的消息:"+responseData);
                    showResponse(responseData);
                }
            });
        }else {
            Toast.makeText(this,"两次密码不一致",Toast.LENGTH_SHORT).show();
            return;
        }
    }

    private void showResponse(final String response){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                JSONObject jsonObject = JSONObject.parseObject(response);
                boolean status = jsonObject.getBoolean("status");
                if(status){
                    spu.setLogin(true);
                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                    startActivity(intent);

                    finish();
                }else{
                    Toast.makeText(RegisterActivity.this,"用户已存在",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
