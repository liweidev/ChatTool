package com.example.liwei.chattool.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.liwei.chattool.R;

/**
 * Created by liwei on 2017/8/4.
 */
//我Fragment
public class MeFragment extends BaseFragment {
    private static final String TAG = "MeFragment";
    private Context mContext;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        init();
        View meView = LayoutInflater.from(mContext).inflate(R.layout.fragment_me, null, false);
        return meView;
    }
    //初始化
    private void init(){
        mContext=getActivity();
    }
}
