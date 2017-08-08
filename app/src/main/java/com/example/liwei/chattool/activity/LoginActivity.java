package com.example.liwei.chattool.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.liwei.chattool.Constant.Constant;
import com.example.liwei.chattool.R;
import com.example.liwei.chattool.util.Base64Utils;
import com.example.liwei.chattool.util.LogUtil;
import com.example.liwei.chattool.util.NetUtil;
import com.example.liwei.chattool.util.SPUtil;
import com.example.liwei.chattool.util.ToastUtils;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;
import com.hyphenate.util.NetUtils;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.example.liwei.chattool.R.id.savePassword;

//主界面
public class LoginActivity extends ParentActivity implements View.OnClickListener {
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
    @BindView(savePassword)
    CheckBox cbRemeberMe;
    //必应每日一图
    @BindView(R.id.iv_biYing)
    ImageView ivBiYing;
    private Context mContext;
    //是否记住密码
    private boolean isRemeberMe=false;
    //用户名
    private String username="";
    //密码
    private String password="";
    //日志TAG
    private static final String TAG = "LoginActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        init();
        hideStateBar();
        checkNetworkState();
        uploadBiYingImage();
        isRememberMe();
    }
    //判断用户上次是否选择记住密码
    //记住密码：显示密码
    //未记住密码：清空
    private void isRememberMe() {
        SharedPreferences sp = getSharedPreferences(Constant.REMEBER_ME, Context.MODE_PRIVATE);
        String username = sp.getString(Constant.USERNAME_KEY, "");
        String password = sp.getString(Constant.PASSWORD_KEY, "");
        boolean isRemeberMe=sp.getBoolean(Constant.IS_REMEBER_ME,false);
        if(!TextUtils.isEmpty(username)){
                etUsername.setText(username);
        }else{
            etUsername.setText("");
        }
        if(!TextUtils.isEmpty(password)){
            etPassword.setText(Base64Utils.decode(password));
        }else {
            etPassword.setText("");
        }
        if(isRemeberMe){
            cbRemeberMe.setChecked(true);
        }else{
            cbRemeberMe.setChecked(false);
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
        cbRemeberMe.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked){
                        isRemeberMe=true;
                    }else{
                        isRemeberMe=false;
                    }
            }
        });
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.login://登录
                login();
                break;
            case R.id.regist://注册
                regist();
                break;
        }
    }
    //注册
    private void regist() {
        Intent intent=new Intent(this,RegistActivity.class);
        startActivityWithAnimation(intent,false);
    }
    //登录
    private void login() {
        boolean isVailable = checkNetworkState();
        if(isVailable){
            if(!isEmpty(etUsername,R.string.regist_username_is_empty)){
                username=etUsername.getText().toString();
            }else{
                return;
            }
            if(!isEmpty(etPassword,R.string.regist_password_is_empty)){
                password= Base64Utils.encode(etPassword.getText().toString());
            }else{
                return;
            }
            showDialog();
            EMClient.getInstance().login(username,password,new EMCallBack() {
                @Override
                public void onSuccess() {
                    LogUtil.d(TAG,String.valueOf(Thread.currentThread().getId()));
                    EMClient.getInstance().groupManager().loadAllGroups();
                    EMClient.getInstance().chatManager().loadAllConversations();
                    // update current user's display name for APNs
                    boolean updatenick = EMClient.getInstance().pushManager().updatePushNickname(username);
                    if (!updatenick) {
                        Log.e("LoginActivity", "update current user nick fail");
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            dismissDialog();
                            ToastUtils.showToast(R.string.login_success);
                            LogUtil.d(TAG, "登录聊天服务器成功！");
                            savePassword();
                            Intent intent=new Intent(mContext,MainActivity.class);
                            startActivityWithAnimation(intent,true);
                            regiestConnectLisenter();
                            SPUtil.getInstance(mContext,Constant.CURRENT_USER).put(Constant.CURRENT_USER_KEY,username);
                        }
                    });
                }
                @Override
                public void onProgress(int progress, String status) {
                    //TODO 该方法并未回掉
                    LogUtil.d(TAG, String.valueOf(progress));
                }
                @Override
                public void onError(int code, final String message) {
                    LogUtil.d(TAG,String.valueOf(Thread.currentThread().getId()));
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            dismissDialog();
                            ToastUtils.showToast(message);
                            LogUtil.d(TAG, "登录聊天服务器失败！"+"\n"+"message:"+message);
                            etUsername.setText("");
                            etPassword.setText("");
                            cbRemeberMe.setChecked(false);
                        }
                    });
                }
            });
        }
    }
    //注册连接状态监听
    private void regiestConnectLisenter() {
        //注册一个监听连接状态的listener
        EMClient.getInstance().addConnectionListener(new MyConnectionListener());
    }
    //保存密码并且Base64加密
    private void savePassword() {
        SharedPreferences sp = getSharedPreferences(Constant.REMEBER_ME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        String username=etUsername.getText().toString();
        String password=Base64Utils.encode(etPassword.getText().toString());
        if(isRemeberMe){
            editor.putString(Constant.USERNAME_KEY,username);
            editor.putString(Constant.PASSWORD_KEY,password);
        }else {
            editor.putString(Constant.USERNAME_KEY,"");
            editor.putString(Constant.PASSWORD_KEY,"");
        }
        editor.putBoolean(Constant.IS_REMEBER_ME,isRemeberMe);
        editor.apply();
    }
    //实现ConnectionListener接口(监听连接状态)
    private class MyConnectionListener implements EMConnectionListener {
        @Override
        public void onConnected() {
        }
        @Override
        public void onDisconnected(final int error) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(error == EMError.USER_REMOVED){
                        // 显示帐号已经被移除
                        ToastUtils.showToast(R.string.account_is_remove);
                    }else if (error == EMError.USER_LOGIN_ANOTHER_DEVICE) {
                        // 显示帐号在其他设备登录
                        ToastUtils.showToast(R.string.account_is_login_on_other_device);
                    } else {
                        if (NetUtils.hasNetwork(mContext)){
                            //连接不到聊天服务器
                            ToastUtils.showToast(R.string.connect_server_failed);
                        }
                        else{
                            //当前网络不可用，请检查网络设置
                            ToastUtils.showToast(R.string.network_is_not_avilable);
                        }
                    }
                }
            });
        }
    }

}