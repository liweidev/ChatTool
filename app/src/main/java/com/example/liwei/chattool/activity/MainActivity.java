package com.example.liwei.chattool.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import com.hyphenate.EMContactListener;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.zhihu.matisse.Matisse;

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
    List<Fragment> fragments = new ArrayList<>();
    //标题
    @BindView(R.id.tv_title)
    TextView tvTitle;
    //添加
    @BindView(R.id.iv_add)
    ImageView ivAdd;
    @BindView(R.id.rl_message)
    RelativeLayout rlMessage;
    @BindView(R.id.rl_contacts)
    RelativeLayout rlContacts;
    @BindView(R.id.rl_find)
    RelativeLayout rlFind;
    @BindView(R.id.rl_me)
    RelativeLayout rlMe;
    //消息
    @BindView(R.id.tv_message)
    TextView tvMessage;
    //联系人
    @BindView(R.id.tv_contacts)
    TextView tvContacts;
    //发现
    @BindView(R.id.tv_find)
    TextView tvFind;
    //我
    @BindView(R.id.tv_me)
    TextView tvMe;
    //消息小红点
    @BindView(R.id.tv_message_tap)
    TextView tvMessageTap;
    //联系人小红点
    @BindView(R.id.tv_contacts_tap)
    TextView tvContactsTap;
    //发现小红点
    @BindView(R.id.tv_find_tap)
    TextView tvFindTap;
    //我小红点
    @BindView(R.id.tv_me_tap)
    TextView tvMeTap;
    private Context mContext;
    //是否显示联系人小红点
    private boolean isShowContactsTap=false;
    //消息监听回掉
    EMMessageListener msgListener = new EMMessageListener() {
        @Override
        public void onMessageReceived(List<EMMessage> messages) {
            //收到消息
            LogUtil.d(TAG, "收到消息");
        }

        @Override
        public void onCmdMessageReceived(List<EMMessage> messages) {
            //收到透传消息
            LogUtil.d(TAG, "收到透传消息");
        }

        @Override
        public void onMessageRead(List<EMMessage> messages) {
            //收到已读回执
            LogUtil.d(TAG, "收到已读回执");
        }

        @Override
        public void onMessageDelivered(List<EMMessage> message) {
            //收到已送达回执
            LogUtil.d(TAG, "收到已送达回执");
        }

        @Override
        public void onMessageChanged(EMMessage message, Object change) {
            //消息状态变动
            LogUtil.d(TAG, "消息状态变动");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        hideActionBar();
        init();
        initContactsTap();
        initViewPager();
        registerMessageLisenter();
        getUnReadMessageCount();
        registerFriendStateLisenter();
    }
    //是否显示联系人小红点
    private void initContactsTap() {
        isShowContactsTap=SPUtil.getInstance(mContext,Constant.SHOW_TAP_MAIN_CONTACTS).get(Constant.SHOW_TAP_MAIN_CONTACTS_KEY,false);
        if(isShowContactsTap){
            tvContactsTap.setVisibility(View.VISIBLE);
        }else{
            tvContactsTap.setVisibility(View.GONE);
        }
    }
    //监听好友状态
    private void registerFriendStateLisenter() {
        EMClient.getInstance().contactManager().setContactListener(new EMContactListener() {
            @Override
            public void onContactInvited(String username, String reason) {
                //收到好友邀请
                ToastUtils.showToast("收到好友邀请" + username + reason);
                if (fragments != null && fragments.size() > 0) {
                    ContactsFragment fragment = (ContactsFragment) fragments.get(1);
                    fragment.showTap();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tvContactsTap.setVisibility(View.VISIBLE);
                        SPUtil.getInstance(mContext,Constant.SHOW_TAP_MAIN_CONTACTS).put(Constant.SHOW_TAP_MAIN_CONTACTS_KEY,true);
                    }
                });
            }
            @Override
            public void onFriendRequestAccepted(String s) {
                ToastUtils.showToast("同意好友请求" + s);
            }
            @Override
            public void onFriendRequestDeclined(String s) {
                ToastUtils.showToast("拒绝好友邀请" + s);
            }
            @Override
            public void onContactDeleted(String username) {
                //被删除时回调此方法
                ToastUtils.showToast("被删除时回调此方法");
            }
            @Override
            public void onContactAdded(String username) {
                //增加了联系人时回调此方法
                ToastUtils.showToast("增加了联系人时回调此方法" + username);
            }
        });
    }

    //获取未读消息数量
    private void getUnReadMessageCount() {
        String username = SPUtil.getInstance(mContext, Constant.CURRENT_USER).get(Constant.CURRENT_USER_KEY, null);
        if (username != null || !TextUtils.isEmpty(username)) {
            EMConversation conversation = EMClient.getInstance().chatManager().getConversation(username);
            if (conversation != null) {
                int unreadMsgCount = conversation.getUnreadMsgCount();
                LogUtil.d(TAG, "未读消息数量:" + unreadMsgCount);
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
        for (int i = 0; i < 4; i++) {
            Fragment fragment = null;
            switch (i) {
                case 0:
                    fragment = new MessageFragment();
                    break;
                case 1:
                    fragment = new ContactsFragment();
                    break;
                case 2:
                    fragment = new FindFragment();
                    break;
                case 3:
                    fragment = new MeFragment();
                    break;
            }
            fragments.add(fragment);
        }
        mainViewPagerAdapter = new MainViewPagerAdapter(getSupportFragmentManager(), mContext, fragments);
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
        switch (position) {
            case 0:
                ivMessage.setImageResource(R.drawable.message_selector);
                ivContacts.setImageResource(R.drawable.contacts_normal);
                ivFind.setImageResource(R.drawable.find_normal);
                ivMe.setImageResource(R.drawable.me_normal);
                tvMessage.setTextColor(Color.parseColor("#1296db"));
                tvContacts.setTextColor(Color.parseColor("#FF000000"));
                tvFind.setTextColor(Color.parseColor("#FF000000"));
                tvMe.setTextColor(Color.parseColor("#FF000000"));
                tvTitle.setText(R.string.main_message);
                ivAdd.setVisibility(View.GONE);
                break;
            case 1:
                ivMessage.setImageResource(R.drawable.message_normal);
                ivContacts.setImageResource(R.drawable.contacts_selector);
                ivFind.setImageResource(R.drawable.find_normal);
                ivMe.setImageResource(R.drawable.me_normal);
                tvMessage.setTextColor(Color.parseColor("#FF000000"));
                tvContacts.setTextColor(Color.parseColor("#1296db"));
                tvFind.setTextColor(Color.parseColor("#FF000000"));
                tvMe.setTextColor(Color.parseColor("#FF000000"));
                tvTitle.setText(R.string.main_contacts);
                ivAdd.setVisibility(View.VISIBLE);
                break;
            case 2:
                ivMessage.setImageResource(R.drawable.message_normal);
                ivContacts.setImageResource(R.drawable.contacts_normal);
                ivFind.setImageResource(R.drawable.find_selector);
                ivMe.setImageResource(R.drawable.me_normal);
                tvMessage.setTextColor(Color.parseColor("#FF000000"));
                tvContacts.setTextColor(Color.parseColor("#FF000000"));
                tvFind.setTextColor(Color.parseColor("#1296db"));
                tvMe.setTextColor(Color.parseColor("#FF000000"));
                tvTitle.setText(R.string.main_find);
                ivAdd.setVisibility(View.GONE);
                break;
            case 3:
                ivMessage.setImageResource(R.drawable.message_normal);
                ivContacts.setImageResource(R.drawable.contacts_normal);
                ivFind.setImageResource(R.drawable.find_normal);
                ivMe.setImageResource(R.drawable.me_selector);
                tvMessage.setTextColor(Color.parseColor("#FF000000"));
                tvContacts.setTextColor(Color.parseColor("#FF000000"));
                tvFind.setTextColor(Color.parseColor("#FF000000"));
                tvMe.setTextColor(Color.parseColor("#1296db"));
                tvTitle.setText(R.string.main_me);
                ivAdd.setVisibility(View.GONE);
                break;
        }
        viewPager.setCurrentItem(position);
    }

    //初始化
    private void init() {
        mContext = this;
        rlMessage.setOnClickListener(this);
        rlContacts.setOnClickListener(this);
        rlFind.setOnClickListener(this);
        rlMe.setOnClickListener(this);
        ivAdd.setOnClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constant.REQUEST_IMAGE_CODE_CHOOSE && resultCode == RESULT_OK) {
            //被选中图片的URI集合
            List<Uri> mSelected;
            mSelected = Matisse.obtainResult(data);
            for (Uri uri : mSelected) {
                String realPathName = getRealFilePath(mContext, uri);
                LogUtil.d(TAG, "realPathName: " + realPathName);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_message://消息
                setViewPagerSelect(0);
                break;
            case R.id.rl_contacts://联系人
                setViewPagerSelect(1);
                break;
            case R.id.rl_find://发现
                setViewPagerSelect(2);
                break;
            case R.id.rl_me://我
                setViewPagerSelect(3);
                break;
            case R.id.iv_add://添加好友
//                requestPermission();
                Intent intent = new Intent(mContext, AddFriendActivity.class);
                startActivityWithAnimation(intent, false);
                break;
        }
    }
    //请求权限
//    private void requestPermission() {
//        MPermissions.requestPermissions(this, Constant.WRITE_STORGE_REQUEST_CODE, Manifest.permission.WRITE_EXTERNAL_STORAGE);
//    }

    //SD卡权限成功回掉
//    @PermissionGrant(Constant.WRITE_STORGE_REQUEST_CODE)
//    public void onSuccess() {
//        //ToastUtils.showToast(R.string.permission_sdcard_success);
//        selecteImage(this, Constant.REQUEST_IMAGE_CODE_CHOOSE);
//    }

    //SD卡权限失败回掉
//    @PermissionDenied(Constant.WRITE_STORGE_REQUEST_CODE)
//    public void onFailed() {
//        ToastUtils.showToast(R.string.permission_sdcard_failed);
//    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        MPermissions.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//    }

}
