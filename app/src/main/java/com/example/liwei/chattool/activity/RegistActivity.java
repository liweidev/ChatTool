package com.example.liwei.chattool.activity;

import android.os.Bundle;

import com.example.liwei.chattool.R;
//注册界面
public class RegistActivity extends ParentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);
        hideStateBar();
    }
}
