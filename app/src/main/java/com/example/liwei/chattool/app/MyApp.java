package com.example.liwei.chattool.app;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.multidex.MultiDex;

import com.example.liwei.chattool.util.LogUtil;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMOptions;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;

import java.util.Iterator;
import java.util.List;

/**
 * Created by liwei on 2017/8/1.
 */

public class MyApp extends Application {
    private static Context mContext;
    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);
        mContext=getApplicationContext();
        registPushServer();
        initChatService();
    }

    //初始化环信服务
    private void initChatService() {
        EMOptions options = new EMOptions();
        //取消自动登录
        //options.setAutoLogin(false);
        // 默认添加好友时，是不需要验证的，改成需要验证
        options.setAcceptInvitationAlways(false);
        int pid = android.os.Process.myPid();
        String processAppName = getAppName(pid);
        // 如果APP启用了远程的service，此application:onCreate会被调用2次
        // 为了防止环信SDK被初始化2次，加此判断会保证SDK被初始化1次
        // 默认的APP会在以包名为默认的process name下运行，如果查到的process name不是APP的process name就立即返回
        if (processAppName == null ||!processAppName.equalsIgnoreCase(mContext.getPackageName())) {
            LogUtil.d("TAG","enter the service process!");
            // 则此application::onCreate 是被service 调用的，直接返回
            return;
        }
        //初始化
        EMClient.getInstance().init(mContext, options);
        //在做打包混淆时，关闭debug模式，避免消耗不必要的资源
        EMClient.getInstance().setDebugMode(true);
    }
    //获取app名字
    private String getAppName(int pID) {
        String processName = null;
        ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        List l = am.getRunningAppProcesses();
        Iterator i = l.iterator();
        PackageManager pm = this.getPackageManager();
        while (i.hasNext()) {
            ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i.next());
            try {
                if (info.pid == pID) {
                    processName = info.processName;
                    return processName;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return processName;
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
