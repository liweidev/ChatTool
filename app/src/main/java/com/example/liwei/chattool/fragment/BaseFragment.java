package com.example.liwei.chattool.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.liwei.chattool.util.LogUtil;

/**
 * Created by liwei on 2017/8/4.
 */
//所有Frgament基类
public class BaseFragment extends Fragment {
    private static final String TAG = "BaseFragment";
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        printlyClassName();
        return super.onCreateView(inflater, container, savedInstanceState);
    }
    //打印当前类名
    private void printlyClassName() {
        String simpleName = getClass().getSimpleName();
        LogUtil.d(TAG,simpleName);
    }
}
