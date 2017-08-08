package com.example.liwei.chattool.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.liwei.chattool.R;
import com.example.liwei.chattool.util.LogUtil;
import com.example.liwei.chattool.util.ToastUtils;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.umeng.message.PushAgent;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;

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
        ActionBar actionBar = getSupportActionBar();
        if(networkInfo==null){
            networkIsAvailable=false;
            ToastUtils.showToast(R.string.network_is_not_avilable);
            dismissDialog();
            if(actionBar!=null){
                actionBar.setTitle(R.string.network_is_not_avilable);
            }
            return networkIsAvailable;
        }
        if(networkInfo.isAvailable()){
            networkIsAvailable=true;
            if(actionBar!=null){
                actionBar.setTitle(R.string.app_name);
            }
        }else{
            networkIsAvailable=false;
            ToastUtils.showToast(R.string.network_is_not_avilable);
            if(actionBar!=null){
                actionBar.setTitle(R.string.network_is_not_avilable);
            }
        }
        dismissDialog();
        return networkIsAvailable;
    }
    //伴随动画跳转Activity
    public void startActivityWithAnimation(Intent intent,boolean isFinish){
        startActivity(intent);
        overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
        if(isFinish){
            finish();
        }
    }
    //隐藏StateBar
    public void hideStateBar() {
        View decorView = getWindow().getDecorView();
        if(Build.VERSION.SDK_INT >= 21){
            int options=View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN  |  View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(options);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }
    //判断EditText是否为空
    //return:
    //true:为空
    //false:不为空
    public boolean isEmpty(EditText editText,int resId){
        if(editText.getText()!=null&& !TextUtils.isEmpty(editText.getText().toString())){
            return false;
        }else{
            editText.requestFocus();
            ToastUtils.showToast(resId);
            return true;
        }
    }
    //开源图片选择库选择图片
    public void selecteImage(Activity activity,int requestCode){
        Matisse.from(activity)
                .choose(MimeType.allOf()) // 选择 mime 的类型
                .countable(true)
                .maxSelectable(9) // 图片选择的最多数量
                .gridExpectedSize(getResources().getDimensionPixelSize(R.dimen.grid_expected_size))//图片尺寸
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                .thumbnailScale(0.85f) // 缩略图的比例
                .imageEngine(new GlideEngine()) // 使用的图片加载引擎
                .forResult(requestCode); // 设置作为标记的请求码
    }
    //通过URI获取图片真实路径
    public static String getRealFilePath( final Context context, final Uri uri ) {
        if ( null == uri ) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if ( scheme == null )
            data = uri.getPath();
        else if ( ContentResolver.SCHEME_FILE.equals( scheme ) ) {
            data = uri.getPath();
        } else if ( ContentResolver.SCHEME_CONTENT.equals( scheme ) ) {
            Cursor cursor = context.getContentResolver().query( uri, new String[] { MediaStore.Images.ImageColumns.DATA }, null, null, null );
            if ( null != cursor ) {
                if ( cursor.moveToFirst() ) {
                    int index = cursor.getColumnIndex( MediaStore.Images.ImageColumns.DATA );
                    if ( index > -1 ) {
                        data = cursor.getString( index );
                    }
                }
                cursor.close();
            }
        }
        return data;
    }
    //退出登录(同步方法)
    public void logOut(){
        EMClient.getInstance().logout(true);
    }
    //退出登录(异步方法)
    public void logOutAsyn(EMCallBack emCallBack){
        EMClient.getInstance().logout(true,emCallBack);
    }

}
