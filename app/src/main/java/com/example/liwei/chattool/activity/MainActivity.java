package com.example.liwei.chattool.activity;

import android.Manifest;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.liwei.chattool.Constant.Constant;
import com.example.liwei.chattool.R;
import com.example.liwei.chattool.util.LogUtil;
import com.example.liwei.chattool.util.NetUtil;
import com.example.liwei.chattool.util.ToastUtils;
import com.zhy.m.permission.MPermissions;
import com.zhy.m.permission.PermissionDenied;
import com.zhy.m.permission.PermissionGrant;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * 主界面
 */
public class MainActivity extends ParentActivity implements View.OnClickListener {
    //登录
    @BindView(R.id.login)
    Button btnLogin;
    //注册
    @BindView(R.id.regist)
    Button btnRegist;
    //用户名
    @BindView(R.id.username)
    EditText etUsername;
    //密码
    @BindView(R.id.password)
    EditText etPassword;
    //记住密码
    @BindView(R.id.savePassword)
    CheckBox cbSavePassword;
    //必应每日一图
    @BindView(R.id.iv_biYing)
    ImageView ivBiYing;
    private Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        requestPermission();
        init();
        hideStateBar();
        checkNetworkState();
        uploadBiYingImage();
    }

    //隐藏StateBar
    private void hideStateBar() {
        View decorView = getWindow().getDecorView();
        if(Build.VERSION.SDK_INT >= 21){
            int options=View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN  |  View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(options);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }

    //加载必应每日一图
    private void uploadBiYingImage() {
        boolean isAvalibale = checkNetworkState();
        if(!isAvalibale){
            ToastUtils.showToast(R.string.network_is_not_avilable);
            return;
        }
        showDialog();
        NetUtil.sendHttpRequest(Constant.BIYING_URL, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtils.showToast(R.string.request_server_is_error);
                        dismissDialog();
                    }
                });

            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                LogUtil.d("currentThreadID:",String.valueOf(Thread.currentThread().getId()));
                final String imageUrl = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(response.isSuccessful()){
                            try {
                                Glide.with(mContext).load(imageUrl).into(ivBiYing);
                                dismissDialog();
                                LogUtil.d("imageUrl",imageUrl);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }else{
                            dismissDialog();
                        }
                    }
                });
            }
        });
    }

    //组件初始化
    private void init(){
        mContext=this;
        btnLogin.setOnClickListener(this);
        btnRegist.setOnClickListener(this);
        cbSavePassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            }
        });
    }
    //请求权限
    private void requestPermission() {
        MPermissions.requestPermissions(this, Constant.WRITE_STORGE_REQUEST_CODE, Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }
    //SD卡权限成功回掉
    @PermissionGrant(Constant.WRITE_STORGE_REQUEST_CODE)
    public void onSuccess() {
//        ToastUtils.showToast(R.string.permission_sdcard_success);
    }

    //SD卡权限失败回掉
    @PermissionDenied(Constant.WRITE_STORGE_REQUEST_CODE)
    public void onFailed() {
        ToastUtils.showToast(R.string.permission_sdcard_failed);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        MPermissions.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.login://登录

                break;
            case R.id.regist://注册

                break;
        }
    }


}
