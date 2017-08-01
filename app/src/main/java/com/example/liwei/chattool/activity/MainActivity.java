package com.example.liwei.chattool.activity;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.example.liwei.chattool.Constant.Constant;
import com.example.liwei.chattool.R;
import com.example.liwei.chattool.util.ToastUtils;
import com.zhy.m.permission.MPermissions;
import com.zhy.m.permission.PermissionDenied;
import com.zhy.m.permission.PermissionGrant;

/**
 * 主界面
 */
public class MainActivity extends ParentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestPermission();
    }
    //请求权限
    private void requestPermission() {
        MPermissions.requestPermissions(this, Constant.WRITE_STORGE_REQUEST_CODE, Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }
    //SD卡权限成功回掉
    @PermissionGrant(Constant.WRITE_STORGE_REQUEST_CODE)
    public void onSuccess(){
        ToastUtils.showToast(R.string.permission_sdcard_success);
    }
    //SD卡权限失败回掉
    @PermissionDenied(Constant.WRITE_STORGE_REQUEST_CODE)
    public void onFailed(){
        ToastUtils.showToast(R.string.permission_sdcard_failed);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        MPermissions.onRequestPermissionsResult(this,requestCode,permissions,grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

}
