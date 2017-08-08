package com.example.liwei.chattool.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.liwei.chattool.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by liwei on 2017/8/4.
 */
//消息Fragment
public class MessageFragment extends BaseFragment implements View.OnClickListener {
    private static final String TAG = "MessageFragment";
    //搜索
    @BindView(R.id.etSearch)
    EditText etSearch;
    //会话列表
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    //删除
    @BindView(R.id.iv_delete)
    ImageView ivDelete;
    private Context mContext;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        init();
        View messageView = LayoutInflater.from(mContext).inflate(R.layout.fragment_message, null, false);
        ButterKnife.bind(this, messageView);
        initLisenter();
        return messageView;
    }
    //初始化监听器
    private void initLisenter() {
        ivDelete.setOnClickListener(this);
    }
    //初始化
    private void init() {
        mContext = getActivity();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_delete:
                etSearch.setText("");
                break;
        }
    }
}
