package com.example.liwei.chattool.activity;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.liwei.chattool.R;
import com.example.liwei.chattool.adapter.AddFriendAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

//添加好友界面
public class AddFriendActivity extends ParentActivity implements View.OnClickListener {
    //返回
    @BindView(R.id.iv_back)
    ImageView ivBack;
    //标题
    @BindView(R.id.tv_title)
    TextView tvTitle;
    //搜索
    @BindView(R.id.iv_search)
    ImageView ivSearch;
    //搜索文本
    @BindView(R.id.etSearch)
    EditText etSearch;
    //清除EditText文本
    @BindView(R.id.iv_delete)
    ImageView ivDelete;
    //查询结果
    @BindView(R.id.lv_search_result)
    ListView lvSearchResult;
    //适配器
    private AddFriendAdapter adapter;
    //数据源
    private List<String>stringList=new ArrayList<>();
    private Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);
        ButterKnife.bind(this);
        mContext=this;
        hideActionBar();
        initViews();
        initLisenter();
        initListView();
    }
    //初始化ListView
    private void initListView() {
        adapter=new AddFriendAdapter(stringList,mContext);
        lvSearchResult.setAdapter(adapter);
    }
    //初始化Views
    private void initViews() {
        tvTitle.setText(R.string.add_friend);
    }
    //初始化监听器
    private void initLisenter() {
        ivBack.setOnClickListener(this);
        ivSearch.setOnClickListener(this);
        ivDelete.setOnClickListener(this);
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String str = s.toString();
                if(str!=null&& !TextUtils.isEmpty(str)){
                    ivDelete.setVisibility(View.VISIBLE);
                }else{
                    ivDelete.setVisibility(View.GONE);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH){
                    searchFriend();
                    return true;
                }
                return false;
            }
        });
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_search:
                searchFriend();
                break;
            case R.id.iv_delete:
                etSearch.setText("");
                break;
        }
    }
    //从服务器查找好友
    private void searchFriend() {
        if(isEmpty(etSearch,R.string.search_content_is_not_empty)){
            return;
        }
        stringList.clear();
        String searchName = etSearch.getText().toString();
        stringList.add(searchName);
        adapter.notifyDataSetChanged();
    }
}
