package com.example.bjtu_ins.ui.add;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.bjtu_ins.MainActivity;
import com.example.bjtu_ins.R;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.app.Activity.RESULT_CANCELED;
import static android.content.Context.MODE_PRIVATE;
import static pub.devrel.easypermissions.RationaleDialogFragmentCompat.TAG;


public class AddFragment extends Fragment {

    private AddViewModel addViewModel;
    //需要的权限数组 读/写/相机
    private static String[] PERMISSIONS_STORAGE = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
    private File tempFile = null;   //新建一个 File 文件（用于相机拿数据）
    private Button album;
    private Button camera;
    private ImageView ImageView01;
    private TextView senddiary;
    private EditText editText;
    private Uri uri;
    private Bitmap bitmap;
    private String id;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add, container, false);
    }

    private void initView() {

        album = getActivity().findViewById(R.id.album);
        camera = getActivity().findViewById(R.id.camera);
        ImageView01 = getActivity().findViewById(R.id.ImageView);
        senddiary = getActivity().findViewById(R.id.send_diary);
        editText = getActivity().findViewById(R.id.edit_diary_text);
        SharedPreferences sp1 = getContext().getSharedPreferences("userid", MODE_PRIVATE);
        id = sp1.getString("id", null);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
        initView();
        // TODO Auto-generated method stub
        album.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                toPicture();
                Toast.makeText(getActivity(), "相册", Toast.LENGTH_SHORT).show();


            }
        });

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //检查是否已经获得相机的权限
                if (verifyPermissions(getActivity(), PERMISSIONS_STORAGE[2]) == 0) {

                    ActivityCompat.requestPermissions(getActivity(), PERMISSIONS_STORAGE, 3);
                } else {
                    //已经有权限
                    toCamera();  //打开相机
                }

            }
        });

        senddiary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editText.getText().toString().equals("")) {
                    Toast.makeText(getActivity(), "请写点什么吧", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (uri == null) {
                    Toast.makeText(getActivity(), "图片不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                File file;
                try {
                    file = new File(getActivity().getCacheDir(), "JiaHua");
                    file.createNewFile();
                    // convert bitmap to byte array
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
                    byte[] bitmapdata = bos.toByteArray();

                    // write the bytes in file
                    FileOutputStream fos = new FileOutputStream(file);
                    fos.write(bitmapdata);
                    fos.flush();
                    fos.close();

                    String body = "userid="+id + "&text=" + editText.getText().toString() + "&media=graph";
                    String address = "http://192.168.0.112:5050/addmessage?" + body;

                    RequestBody requestBody = RequestBody.create(file, MediaType.parse("image/*"));
                    RequestBody multipartBody = new MultipartBody.Builder()
                            .setType(MultipartBody.FORM)
                            .addFormDataPart("img", file.getName(), requestBody)
                            .build();
                    OkHttpClient client = new OkHttpClient();
                    client.newCall(new Request.Builder()
                            .header("Content-Type", "application/x-www-form-urlencoded")
                            .url(address)
                            .post(multipartBody)
                            .build())
                            .enqueue(new Callback() {
                                @Override
                                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                                    String str = response.body().string();
                                    //Toast.makeText(getActivity(),str,Toast.LENGTH_SHORT).show();

                                    try {
                                        //String转JSONObject
                                        JSONObject result = new JSONObject(str);
                                        //取数据
                                        String res = result.get("response").toString();
                                        Log.d(TAG,res);
                                        if(res.equals("Add success")){
                                            startActivity(new Intent(getActivity(), MainActivity.class));
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                                @Override
                                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                                    e.getMessage();
                                }
                            });
                } catch (Exception e) {
                    e.printStackTrace();
                }
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
                        bitmap = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(uri));
                        ImageView01.setImageBitmap(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    break;
                case 101:  //相机返回的数据（相机的返回码）
                    Bundle extras = data.getExtras();
                    if (extras != null) {
                        bitmap = extras.getParcelable("data");
                        uri = Uri.parse(MediaStore.Images.Media.insertImage(getActivity().getContentResolver(), bitmap, null, null));
                        /* 将Bitmap设定到ImageView */
                        ImageView01.setImageBitmap(bitmap);
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

}