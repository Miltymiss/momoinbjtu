package com.example.bjtu_ins.ui.my;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.bjtu_ins.R;
import com.example.bjtu_ins.activity.FriendListActivity;
import com.example.bjtu_ins.activity.LoginActivity;
import com.example.bjtu_ins.utils.SharedPreferencesUtil;
import com.hb.dialog.myDialog.ActionSheetDialog;
import com.hb.dialog.myDialog.MyAlertDialog;
import com.hb.dialog.myDialog.MyAlertInputDialog;
import com.leon.lib.settingview.LSettingItem;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.app.Activity.RESULT_CANCELED;
import static android.content.Context.MODE_PRIVATE;

public class MyFragment extends Fragment {

    DateFormat format = DateFormat.getDateInstance();
    String strDateFormat = "yyyy-MM-dd";
    SimpleDateFormat sdf = new SimpleDateFormat(strDateFormat);
    //获取日期格式器对象
    private SharedPreferencesUtil spu;

    Calendar calendar = Calendar.getInstance(Locale.CHINA);

    //获取日期格式器对象
    private static String[] PERMISSIONS_STORAGE = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
    private Uri uri;
    private MyViewModel myViewModel;
    private Button logoutButton;
    private LSettingItem item_id;
    private LSettingItem item_name;
    private LSettingItem item_birthday;
    private LSettingItem item_sex;
    private LSettingItem item_description;
    private ImageView head;
    private String name="";
    private String id="";
    private String birthday="";
    private String sex="";
    private String description="";
    private LSettingItem friendList;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        myViewModel =
                ViewModelProviders.of(this).get(MyViewModel.class);
        View root = inflater.inflate(R.layout.fragment_my, container, false);

        spu = SharedPreferencesUtil.getInstance(this.getContext());
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        id = "milty111";
        logoutButton = (Button) getActivity().findViewById(R.id.logout);
        item_name = (LSettingItem) getActivity().findViewById(R.id.item_name);
        item_birthday = (LSettingItem) getActivity().findViewById(R.id.item_birthday);
        item_sex = (LSettingItem) getActivity().findViewById(R.id.item_sex);
        item_description = (LSettingItem) getActivity().findViewById(R.id.item_description);
        item_id = (LSettingItem) getActivity().findViewById(R.id.item_id);
        friendList = (LSettingItem) getActivity().findViewById(R.id.item_friend);
        head = (ImageView) getActivity().findViewById(R.id.head);
        SharedPreferences sp1 = getContext().getSharedPreferences("userid", MODE_PRIVATE);
        id = sp1.getString("id", null);
        item_id.setLeftText("id:   "+id);
        getMyInformation();
        head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActionSheetDialog dialog = new ActionSheetDialog(getActivity()).builder().setTitle("请选择")
                        .addSheetItem("相册", null, new ActionSheetDialog.OnSheetItemClickListener() {
                            @Override
                            public void onClick(int which) {
                                toPicture();
                                Toast.makeText(getActivity(), "相册", Toast.LENGTH_SHORT).show();
                            }
                        }).addSheetItem("拍照", null, new ActionSheetDialog.OnSheetItemClickListener() {
                            @Override
                            public void onClick(int which) {
                                if (verifyPermissions(getActivity(), PERMISSIONS_STORAGE[2]) == 0) {
                                    ActivityCompat.requestPermissions(getActivity(), PERMISSIONS_STORAGE, 3);
                                } else {
                                    //已经有权限
                                    toCamera();  //打开相机
                                }
                            }
                        });
                dialog.show();
            }
        });


        //登出

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyAlertDialog myAlertDialog = new MyAlertDialog(getActivity()).builder()
                        .setMsg("是否要退出登录")
                        .setPositiveButton("确认", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                spu.setLogin(false);
                                startActivity(new Intent(getActivity(), LoginActivity.class));
                            }
                        }).setNegativeButton("取消", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                return;
                            }
                        });
                myAlertDialog.show();
            }
        });

        item_name.setmOnLSettingItemClick(new LSettingItem.OnLSettingItemClick() {
            @Override
            public void click(boolean isChecked) {
                final MyAlertInputDialog myAlertInputDialog = new MyAlertInputDialog(getActivity()).builder()
                        .setTitle("请输入姓名")
                        .setEditText("");
                myAlertInputDialog.setPositiveButton("确认", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (myAlertInputDialog.getResult().equals("")) {
                            Toast.makeText(getActivity(), "名字不为空", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        name = myAlertInputDialog.getResult();
                        item_name.setLeftText("姓名:   " + name);
                        changeMyInformation();
                        myAlertInputDialog.dismiss();
                    }
                }).setNegativeButton("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        return;
                    }
                });
                myAlertInputDialog.show();
            }
        });


        item_birthday.setmOnLSettingItemClick(new LSettingItem.OnLSettingItemClick() {
            @Override
            public void click(boolean isChecked) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {

                    @Override

                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                        //修改日历控件的年，月，日

                        //这里的year,monthOfYear,dayOfMonth的值与DatePickerDialog控件设置的最新值一致

                        calendar.set(Calendar.YEAR, year);

                        calendar.set(Calendar.MONTH, month);

                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        birthday = sdf.format(calendar.getTime());
                        item_birthday.setLeftText("生日:   " + birthday);
                        changeMyInformation();
                    }

                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

                datePickerDialog.show();
            }
        });


        item_sex.setmOnLSettingItemClick(new LSettingItem.OnLSettingItemClick() {
            @Override
            public void click(boolean isChecked) {
                ActionSheetDialog dialog = new ActionSheetDialog(getActivity()).builder().setTitle("请选择性别")
                        .addSheetItem("男", null, new ActionSheetDialog.OnSheetItemClickListener() {
                            @Override
                            public void onClick(int which) {
                                sex = "男";
                                item_sex.setLeftText("性别:   男");
                            }
                        }).addSheetItem("女", null, new ActionSheetDialog.OnSheetItemClickListener() {
                            @Override
                            public void onClick(int which) {
                                sex = "女";
                                item_sex.setLeftText("性别:   女");
                            }
                        });
                changeMyInformation();
                dialog.show();
            }
        });


        item_description.setmOnLSettingItemClick(new LSettingItem.OnLSettingItemClick() {
            @Override
            public void click(boolean isChecked) {
                final MyAlertInputDialog myAlertInputDialog = new MyAlertInputDialog(getActivity()).builder()
                        .setTitle("请输入签名")
                        .setEditText("");
                myAlertInputDialog.setPositiveButton("确认", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        description = myAlertInputDialog.getResult();
                        item_description.setLeftText("描述:   " + description);
                        myAlertInputDialog.dismiss();
                        changeMyInformation();
                    }
                }).setNegativeButton("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        return;
                    }
                });
                myAlertInputDialog.show();
            }
        });

        friendList.setmOnLSettingItemClick(new LSettingItem.OnLSettingItemClick() {
            @Override
            public void click(boolean isChecked) {
                Intent intent = new Intent(getActivity(), FriendListActivity.class);
                startActivity(intent);
            }
        });
    }



    public int verifyPermissions(Activity activity, String permission) {
        int Permission = ActivityCompat.checkSelfPermission(activity, permission);
        if (Permission == PackageManager.PERMISSION_GRANTED) {
            return 1;
        } else {
            return 0;
        }
    }


    //获取 相机 或者 图库 返回的图片
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //判断返回码不等于0
        if (requestCode != RESULT_CANCELED) {    //RESULT_CANCELED = 0(也可以直接写“if (requestCode != 0 )”)
            //读取返回码
            switch (requestCode) {
                case 100:   //相册返回的数据（相册的返回码）

                    uri = data.getData();
                    try {
                        Bitmap bitmap = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(uri));
                        head.setImageBitmap(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    break;
                case 101:  //相机返回的数据（相机的返回码）
                    Bundle extras = data.getExtras();
                    if (extras != null) {
                        Bitmap bitmap = extras.getParcelable("data");
                        uri = Uri.parse(MediaStore.Images.Media.insertImage(getActivity().getContentResolver(), bitmap, null, null));
                        /* 将Bitmap设定到ImageView */
                        head.setImageBitmap(bitmap);
                    }
                    break;
            }
        }
    }

    //跳转相册
    private void toPicture() {
        Intent intent = new Intent(Intent.ACTION_PICK);  //跳转到 ACTION_IMAGE_CAPTURE
        intent.setType("image/*");
        startActivityForResult(intent, 100);

    }

    //跳转相机
    private void toCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);  //跳转到 ACTION_IMAGE_CAPTURE
        //判断内存卡是否可用，可用的话就进行存储
        //putExtra：取值，Uri.fromFile：传一个拍照所得到的文件，fileImg.jpg：文件名
        //intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStorageDirectory(),"fileImg.jpg")));
        startActivityForResult(intent, 101); // 101: 相机的返回码参数（随便一个值就行，只要不冲突就好）
    }


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    try {
                        //String转JSONObject
                        String s = msg.obj.toString();
                        JSONObject result = new JSONObject(s);
                        //取数据
                        String res = result.get("response").toString();
                        JSONArray jsonArray = result.getJSONArray("response");
                        JSONArray ja = jsonArray.getJSONArray(0);
                        String i = ja.get(0).toString();

                        name = ja.get(0).toString();
                        sex = ja.get(1).toString();
                        if (sex.equals("1")) {
                            sex = "男";
                        } else if (sex.equals("2")) {
                            sex = "女";
                        } else {
                            sex = "";
                        }
                        birthday = ja.get(2).toString();
                        description = ja.get(3).toString();
                        //LSettingItem item_name1 = (LSettingItem)getActivity().findViewById(R.id.item_name);
                        item_name.setLeftText("姓名:   " + name);
                        item_birthday.setLeftText("生日:   " + birthday);
                        item_sex.setLeftText("性别:   " + sex);
                        item_description.setLeftText("描述:   " + description);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        msg.what = 0;
                    }
                    break;
                case 0:
                    Toast.makeText(getActivity(), "上传成功", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    };


    private void getMyInformation() {
        String body = "userid="+id;
        String address = "http://192.168.0.112:5050/getuserifm?" + body;


        OkHttpClient client = new OkHttpClient();
        client.newCall(new Request.Builder()
                .header("Content-Type", "application/x-www-form-urlencoded")
                .url(address)
                .build())
                .enqueue(new Callback() {

                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        String str = response.body().string();
                        //Toast.makeText(getActivity(),str,Toast.LENGTH_SHORT).show();
                        Message msg = Message.obtain();
                        msg.obj = str;
                        msg.what = 1;   //标志消息的标志
                        handler.sendMessage(msg);
                    }

                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        e.getMessage();
                    }


                });

    }



    private void changeMyInformation() {
        String real_sex;
        if(sex.equals("男")){
            real_sex = "1";
        }else if(sex.equals("女")){
            real_sex = "2";
        }else {
            real_sex = "0";
        }
        String body = "userid="+id+"&username="+name+"&birthday="+birthday+"&sex="+real_sex+"&graph=not"+"&discription="+description;
        String address = "http://192.168.0.112:5050//updateuserifm?" + body;


        OkHttpClient client = new OkHttpClient();
        client.newCall(new Request.Builder()
                .header("Content-Type", "application/x-www-form-urlencoded")
                .url(address)
                .build())
                .enqueue(new Callback() {
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        String str = response.body().string();
                        try {
                            JSONObject result = new JSONObject(str);
                            //取数据
                            String res = result.get("response").toString();
                            if(res.equals("Update success")){
//
                                Message msg = Message.obtain();
                                msg.obj = str;
                                msg.what = 0;   //标志消息的标志
                                handler.sendMessage(msg);
                            }
                        }catch (JSONException e){
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        e.getMessage();
                    }


                });

    }
}