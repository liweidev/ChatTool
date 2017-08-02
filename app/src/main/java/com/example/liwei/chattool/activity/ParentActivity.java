package com.example.liwei.chattool.activity;

import android.app.Dialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import com.example.liwei.chattool.R;
import com.example.liwei.chattool.util.LogUtil;
import com.example.liwei.chattool.util.ToastUtils;
import com.umeng.message.PushAgent;

/**
 * Created by liwei on 2017/8/1.
 * 父类
 */

public class ParentActivity extends AppCompatActivity {
    //加载
    private Dialog loadDialog=null;
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
    //显示加载
    public void showDialog(){
        View view = LayoutInflater.from(this).inflate(R.layout.load, null, false);
        ImageView ivLoad= (ImageView) view.findViewById(R.id.iv_load);
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.anim_load);
        LinearInterpolator lir = new LinearInterpolator();
        anim.setInterpolator(lir);
        ivLoad.startAnimation(anim);
        ViewGroup viewGroup= (ViewGroup) view.getParent();
        if(viewGroup!=null){
            viewGroup.removeAllViews();
        }
        if(loadDialog==null){
            loadDialog=new Dialog(this);
            loadDialog.setCancelable(false);
            loadDialog.setContentView(view);
            loadDialog.show();
        }else{
            loadDialog.setCancelable(false);
            loadDialog.setContentView(view);
            loadDialog.show();
        }
    }
    //隐藏加载
    public void dismissDialog(){
        if(loadDialog!=null){
            loadDialog.dismiss();
        }
    }
    //检查网络状态
    public boolean checkNetworkState(){
        boolean networkIsAvailable=false;
        showDialog();
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if(networkInfo==null){
            networkIsAvailable=false;
            ToastUtils.showToast(R.string.network_is_not_avilable);
            dismissDialog();
            return networkIsAvailable;
        }
        if(networkInfo.isAvailable()){
            networkIsAvailable=true;
        }else{
            networkIsAvailable=false;
            ToastUtils.showToast(R.string.network_is_not_avilable);
        }
        dismissDialog();
        return networkIsAvailable;
    }

}
