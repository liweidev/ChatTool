package com.example.liwei.chattool.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.example.liwei.chattool.R;
import com.example.liwei.chattool.adapter.SortAdapter;
import com.example.liwei.chattool.entity.User;
import com.example.liwei.chattool.util.Cn2Spell;
import com.example.liwei.chattool.util.ToastUtils;
import com.example.liwei.chattool.view.SideBar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by liwei on 2017/8/4.
 */
//联系人Fragment
public class ContactsFragment extends BaseFragment {
    private static final String TAG = "ContactsFragment";
    //搜索
    @BindView(R.id.etSearch)
    EditText etSearch;
    //联系人列表
    @BindView(R.id.listView)
    ListView listView;
    //右侧导航条
    @BindView(R.id.side_bar)
    SideBar sideBar;
    Unbinder unbinder;
    private Context mContext;
    private List<User> list;
    private SortAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        init();
        View meView = LayoutInflater.from(mContext).inflate(R.layout.fragment_contacts, null, false);
        unbinder = ButterKnife.bind(this, meView);
        initSideBar();
        initListView();
        getConatcts();
        initEditSearch();
        return meView;
    }
    //从网络获取联系人列表
    private void getConatcts() {
        //TODO
    }

    //初始化EditSearch
    private void initEditSearch() {
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String chines=s.toString();
                if(chines!=null&&!chines.equals("")){
                    String pinyin = Cn2Spell.getPinYin(chines); // 根据姓名获取拼音
                    String firstLetter = pinyin.substring(0, 1).toUpperCase(); // 获取拼音首字母并转成大写
                    if (!firstLetter.matches("[A-Z]")) { // 如果不在A-Z中则默认为“#”
                        firstLetter = "#";
                    }
                    int position = adapter.getPositionForSection(firstLetter);
                    listView.setSelection(position+2);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }
    //初始化SideBar
    private void initSideBar() {
        sideBar.setOnStrSelectCallBack(new SideBar.ISideBarSelectCallBack() {
            @Override
            public void onSelectStr(int index, String selectStr) {
                for (int i = 0; i < list.size(); i++) {
                    if (selectStr.equalsIgnoreCase(list.get(i).getFirstLetter())) {
                        listView.setSelection(i+2); // 选择到首字母出现的位置
                        return;
                    }
                }
            }
        });
    }
    //初始化ListView数据，获取联系人列表
    private void initListView() {
        list = new ArrayList<>();
        list.add(new User("亳州")); // 亳[bó]属于不常见的二级汉字
        list.add(new User("大娃"));
        list.add(new User("二娃"));
        list.add(new User("三娃"));
        list.add(new User("四娃"));
        list.add(new User("五娃"));
        list.add(new User("六娃"));
        list.add(new User("七娃"));
        list.add(new User("喜羊羊"));
        list.add(new User("美羊羊"));
        list.add(new User("懒羊羊"));
        list.add(new User("沸羊羊"));
        list.add(new User("暖羊羊"));
        list.add(new User("慢羊羊"));
        list.add(new User("灰太狼"));
        list.add(new User("红太狼"));
        list.add(new User("孙悟空"));
        list.add(new User("黑猫警长"));
        list.add(new User("舒克"));
        list.add(new User("贝塔"));
        list.add(new User("海尔"));
        list.add(new User("阿凡提"));
        list.add(new User("邋遢大王"));
        list.add(new User("哪吒"));
        list.add(new User("没头脑"));
        list.add(new User("不高兴"));
        list.add(new User("蓝皮鼠"));
        list.add(new User("大脸猫"));
        list.add(new User("大头儿子"));
        list.add(new User("小头爸爸"));
        list.add(new User("蓝猫"));
        list.add(new User("淘气"));
        list.add(new User("叶峰"));
        list.add(new User("楚天歌"));
        list.add(new User("江流儿"));
        list.add(new User("Tom"));
        list.add(new User("Jerry"));
        list.add(new User("12345"));
        list.add(new User("54321"));
        list.add(new User("_(:з」∠)_"));
        list.add(new User("……%￥#￥%#"));
        Collections.sort(list); // 对list进行排序，需要让User实现Comparable接口重写compareTo方法
        adapter = new SortAdapter(mContext, list);
        listView.addHeaderView(LayoutInflater.from(mContext).inflate(R.layout.contacts_fragment_head_new_friend,null,false));
        listView.addHeaderView(LayoutInflater.from(mContext).inflate(R.layout.contacts_fragment_head_group_chat,null,false));
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        ToastUtils.showToast("新的朋友");
                        break;
                    case 1:
                        ToastUtils.showToast("群聊");
                        break;
                    default:
                        if(position>=2){
                            User user = list.get(position - 2);
                            ToastUtils.showToast(user.getName());
                        }
                        break;
                }
            }
        });
    }
    //初始化
    private void init() {
        mContext = getActivity();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
