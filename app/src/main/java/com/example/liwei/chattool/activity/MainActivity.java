package com.example.liwei.chattool.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.example.liwei.chattool.Constant.Constant;
import com.example.liwei.chattool.R;
import com.example.liwei.chattool.adapter.MainViewPagerAdapter;
import com.example.liwei.chattool.fragment.ContactsFragment;
import com.example.liwei.chattool.fragment.FindFragment;
import com.example.liwei.chattool.fragment.MeFragment;
import com.example.liwei.chattool.fragment.MessageFragment;
import com.example.liwei.chattool.util.LogUtil;
import com.example.liwei.chattool.util.SPUtil;
import com.example.liwei.chattool.util.ToastUtils;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.zhihu.matisse.Matisse;
import com.zhy.m.permission.MPermissions;
import com.zhy.m.permission.PermissionDenied;
import com.zhy.m.permission.PermissionGrant;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

//主界面
public class MainActivity extends ParentActivity implements View.OnClickListener {
    //消息
    @BindView(R.id.iv_message)
    ImageView ivMessage;
    //联系人
    @BindView(R.id.iv_contacts)
    ImageView ivContacts;
    //发现
    @BindView(R.id.iv_find)
    ImageView ivFind;
    //我
    @BindView(R.id.iv_me)
    ImageView ivMe;
    //ViewPager
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    //日志
    private static final String TAG = "MainActivity";
    //主界面ViewPager适配器
    MainViewPagerAdapter mainViewPagerAdapter;
    //Fragment集合
    List<Fragment>fragments=new ArrayList<>();
    private Context mContext;
    //消息监听回掉
    EMMessageListener msgListener = new EMMessageListener() {
        @Override
        public void onMessageReceived(List<EMMessage> messages) {
            //收到消息
            LogUtil.d(TAG,"收到消息");
        }
        @Override
        public void onCmdMessageReceived(List<EMMessage> messages) {
            //收到透传消息
            LogUtil.d(TAG,"收到透传消息");
        }
        @Override
        public void onMessageRead(List<EMMessage> messages) {
            //收到已读回执
            LogUtil.d(TAG,"收到已读回执");
        }
        @Override
        public void onMessageDelivered(List<EMMessage> message) {
            //收到已送达回执
            LogUtil.d(TAG,"收到已送达回执");
        }
        @Override
        public void onMessageChanged(EMMessage message, Object change) {
            //消息状态变动
            LogUtil.d(TAG,"消息状态变动");
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        init();
        initViewPager();
        registerMessageLisenter();
        getUnReadMessageCount();
    }
    //获取未读消息数量
    private void getUnReadMessageCount() {
        String username = SPUtil.getInstance(mContext, Constant.CURRENT_USER).get(Constant.CURRENT_USER_KEY, null);
        if(username!=null|| !TextUtils.isEmpty(username)){
            EMConversation conversation = EMClient.getInstance().chatManager().getConversation(username);
            if(conversation!=null){
                int unreadMsgCount = conversation.getUnreadMsgCount();
                LogUtil.d(TAG,"未读消息数量:"+unreadMsgCount);
            }
        }
    }
    //注册消息监听
    private void registerMessageLisenter() {
        EMClient.getInstance().chatManager().addMessageListener(msgListener);
    }
    @Override
    protected void onResume() {
        super.onResume();
        checkNetworkState();
    }
    //初始化ViewPager
    private void initViewPager() {
        fragments.clear();
        for(int i=0;i<4;i++){
            Fragment fragment=null;
            switch (i){
                case 0:
                    fragment=new MessageFragment();
                    break;
                case 1:
                     fragment=new ContactsFragment();
                    break;
                case 2:
                     fragment=new FindFragment();
                    break;
                case 3:
                     fragment=new MeFragment();
                    break;
            }
            fragments.add(fragment);
        }
        mainViewPagerAdapter=new MainViewPagerAdapter(getSupportFragmentManager(),mContext,fragments);
        viewPager.setAdapter(mainViewPagerAdapter);
        setViewPagerSelect(0);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }
            @Override
            public void onPageSelected(int position) {
                setViewPagerSelect(position);
            }
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
    //ViewPagerer被选中的TAB变化
    private void setViewPagerSelect(int position) {
        switch (position){
            case 0:
                ivMessage.setImageResource(R.drawable.message_selector);
                ivContacts.setImageResource(R.drawable.contacts_normal);
                ivFind.setImageResource(R.drawable.find_normal);
                ivMe.setImageResource(R.drawable.me_normal);
                break;
            case 1:
                ivMessage.setImageResource(R.drawable.message_normal);
                ivContacts.setImageResource(R.drawable.contacts_selector);
                ivFind.setImageResource(R.drawable.find_normal);
                ivMe.setImageResource(R.drawable.me_normal);
                break;
            case 2:
                ivMessage.setImageResource(R.drawable.message_normal);
                ivContacts.setImageResource(R.drawable.contacts_normal);
                ivFind.setImageResource(R.drawable.find_selector);
                ivMe.setImageResource(R.drawable.me_normal);
                break;
            case 3:
                ivMessage.setImageResource(R.drawable.message_normal);
                ivContacts.setImageResource(R.drawable.contacts_normal);
                ivFind.setImageResource(R.drawable.find_normal);
                ivMe.setImageResource(R.drawable.me_selector);
                break;
        }
        viewPager.setCurrentItem(position);
    }
    //初始化
    private void init(){
        mContext=this;
        ivMessage.setOnClickListener(this);
        ivContacts.setOnClickListener(this);
        ivFind.setOnClickListener(this);
        ivMe.setOnClickListener(this);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add:
                requestPermission();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constant.REQUEST_IMAGE_CODE_CHOOSE && resultCode == RESULT_OK) {
            //被选中图片的URI集合
            List<Uri> mSelected;
            mSelected = Matisse.obtainResult(data);
            for(Uri uri:mSelected){
                String realPathName = getRealFilePath(mContext, uri);
                LogUtil.d(TAG, "realPathName: " + realPathName);
            }
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_message://消息
                setViewPagerSelect(0);
                break;
            case R.id.iv_contacts://联系人
                setViewPagerSelect(1);
                break;
            case R.id.iv_find://发现
                setViewPagerSelect(2);
                break;
            case R.id.iv_me://我
                setViewPagerSelect(3);
                break;
        }
    }
    //请求权限
    private void requestPermission() {
        MPermissions.requestPermissions(this, Constant.WRITE_STORGE_REQUEST_CODE, Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }
    //SD卡权限成功回掉
    @PermissionGrant(Constant.WRITE_STORGE_REQUEST_CODE)
    public void onSuccess() {
        //ToastUtils.showToast(R.string.permission_sdcard_success);
        selecteImage(this, Constant.REQUEST_IMAGE_CODE_CHOOSE);
    }
    //SD卡权限失败回掉
    @PermissionDenied(Constant.WRITE_STORGE_REQUEST_CODE)
    public void onFailed() {
        ToastUtils.showToast(R.string.permission_sdcard_failed);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        MPermissions.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

}
