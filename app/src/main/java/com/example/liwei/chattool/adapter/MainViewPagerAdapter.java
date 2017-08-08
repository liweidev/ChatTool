package com.example.liwei.chattool.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liwei on 2017/8/4.
 */
//主界面ViewPager适配器
public class MainViewPagerAdapter extends FragmentPagerAdapter{
    private Context mContext;
    private List<Fragment> fragments=new ArrayList<>();
    public MainViewPagerAdapter(FragmentManager fm, Context mContext, List<Fragment> fragments) {
        super(fm);
        this.mContext = mContext;
        this.fragments = fragments;
    }
    public MainViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }
    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }
    @Override
    public int getCount() {
        return fragments.size();
    }
}
