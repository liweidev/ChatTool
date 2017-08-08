package com.example.liwei.chattool.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.liwei.chattool.R;
import com.example.liwei.chattool.util.Base64Utils;
import com.example.liwei.chattool.util.ToastUtils;
import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

//注册界面
public class RegistActivity extends ParentActivity implements View.OnClickListener {
    //用户名
    @BindView(R.id.username)
    EditText etUsername;
    //密码
    @BindView(R.id.password)
    EditText etPassword;
    //注册
    @BindView(R.id.regist)
    Button btnRegist;
    //用户名
    String username="";
    //密码
    String passwrod="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);
        ButterKnife.bind(this);
        hideStateBar();
        init();
    }
    //初始化
    private void init(){
        btnRegist.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.regist://注册
                regist();
                break;
        }
    }
    //注册
    private void regist() {
        boolean isAvilable = checkNetworkState();
        if(isAvilable){
            if(!isEmpty(etUsername,R.string.regist_username_is_empty)){
                username=etUsername.getText().toString();
            }else{
                return;
            }
            if(!isEmpty(etPassword,R.string.regist_password_is_empty)){
                passwrod=etPassword.getText().toString();
            }else{
                return;
            }
            passwrod= Base64Utils.encode(passwrod);
            showDialog();
            //注册失败会抛出HyphenateException
            //同步方法
            Observable.create(new Observable.OnSubscribe<Boolean>() {
                @Override
                public void call(Subscriber<? super Boolean> subscriber) {
                    try {
                        EMClient.getInstance().createAccount(username, passwrod);
                        subscriber.onNext(true);
                    } catch (final HyphenateException e) {
                        e.printStackTrace();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                int errorCode=e.getErrorCode();
                                if(errorCode== EMError.NETWORK_ERROR){
                                    ToastUtils.showToast(R.string.network_anomalies);
                                }else if(errorCode == EMError.USER_ALREADY_EXIST){
                                    ToastUtils.showToast(R.string.User_already_exists);
                                }else if(errorCode == EMError.USER_AUTHENTICATION_FAILED){
                                    ToastUtils.showToast(R.string.registration_failed_without_permission);
                                }else if(errorCode == EMError.USER_ILLEGAL_ARGUMENT){
                                    ToastUtils.showToast(R.string.illegal_user_name);
                                }else{
                                    ToastUtils.showToast(R.string.Registration_failed);
                                }
                            }
                        });
                        subscriber.onNext(false);
                    }
                }
            }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<Boolean>() {
                @Override
                public void call(Boolean isSuccess) {
                    if(isSuccess){
                        dismissDialog();
                        ToastUtils.showToast(R.string.regist_success);
                        finish();
                    }else{
                        dismissDialog();
                        etUsername.setText("");
                        etPassword.setText("");
                    }
                }
            });
        }
    }

}
