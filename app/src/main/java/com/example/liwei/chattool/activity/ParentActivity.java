package com.example.liwei.chattool.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.liwei.chattool.util.LogUtil;
import com.umeng.message.PushAgent;

/**
 * Created by liwei on 2017/8/1.
 * 父类
 */

public class ParentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        printlPackageName();
        pushSDKStart();
    }

    //统计应用启动数据
    private void pushSDKStart() {
        PushAgent.getInstance(this).onAppStart();
    }

    //打印类名
    private void printlPackageName() {
        String className = getClass().getSimpleName();
        LogUtil.d("classname",className);
    }

}
