package com.example.liwei.chattool.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.liwei.chattool.R;

import butterknife.ButterKnife;

/**
 * Created by liwei on 2017/8/4.
 */
//消息Fragment
public class MessageFragment extends BaseFragment {
    private static final String TAG = "MessageFragment";
    private Context mContext;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        init();
        View messageView = LayoutInflater.from(mContext).inflate(R.layout.fragment_message, null, false);
        ButterKnife.bind(this,messageView);
        return messageView;
    }
    //初始化
    private void init(){
        mContext=getActivity();
    }
}
