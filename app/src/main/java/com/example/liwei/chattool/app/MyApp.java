package com.example.liwei.chattool.app;

import android.app.Application;
import android.content.Context;

import com.example.liwei.chattool.util.LogUtil;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;

/**
 * Created by liwei on 2017/8/1.
 */

public class MyApp extends Application {
    private static Context mContext;
    @Override
    public void onCreate() {
        super.onCreate();
        mContext=getApplicationContext();
        registPushServer();
    }

    //注册推送服务
    private void registPushServer() {
        PushAgent mPushAgent = PushAgent.getInstance(this);
        //注册推送服务，每次调用register方法都会回调该接口
        mPushAgent.register(new IUmengRegisterCallback() {
            @Override
            public void onSuccess(String deviceToken) {
                //device token是【友盟+】生成的用于标识设备的id，长度为44位，
                // 不能定制和修改。同一台设备上不同应用对应的device token不一样。
                //注册成功会返回device token
                LogUtil.d("onSuccess-deviceToken:",deviceToken);
            }
            @Override
            public void onFailure(String s, String s1) {
                LogUtil.d("onFailure:",s+" || "+s1);
            }
        });
    }

    //全局Context
    public static Context getmContext(){
        return mContext;
    }

}
