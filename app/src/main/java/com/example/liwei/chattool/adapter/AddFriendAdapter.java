package com.example.liwei.chattool.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.liwei.chattool.Constant.Constant;
import com.example.liwei.chattool.R;
import com.example.liwei.chattool.activity.AddFriendActivity;
import com.example.liwei.chattool.util.ToastUtils;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liwei on 2017/8/8.
 */
//添加好友适配器
public class AddFriendAdapter extends BaseAdapter {
    private List<String> stringList=new ArrayList<>();
    private Context mContext;
    public AddFriendAdapter(List<String> stringList, Context mContext) {
        this.stringList = stringList;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return stringList.size();
    }

    @Override
    public Object getItem(int position) {
        return stringList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh=null;
        if(convertView==null){
            vh=new ViewHolder();
            convertView= LayoutInflater.from(mContext).inflate(R.layout.add_friend_item,parent,false);
            vh.tvFriendName= (TextView) convertView.findViewById(R.id.tv_friend_name);
            vh.btnAddFriend= (Button) convertView.findViewById(R.id.btn_add);
            convertView.setTag(vh);
        }else{
            vh= (ViewHolder) convertView.getTag();
        }
        final String toAddUsername = stringList.get(position);
        vh.tvFriendName.setText(toAddUsername);
        vh.btnAddFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((AddFriendActivity)mContext).showDialog();
                try {
                    EMClient.getInstance().contactManager().addContact(toAddUsername, Constant.ADD_FRIEND_REASON);
                    ((AddFriendActivity)mContext).dismissDialog();
                    ToastUtils.showToast(R.string.add_friend_success);
                } catch (HyphenateException e) {
                    e.printStackTrace();
                    ((AddFriendActivity)mContext).dismissDialog();
                    ToastUtils.showToast(R.string.add_friend_error);
                }
//                ((AddFriendActivity)mContext).showDialog();
//                Observable.create(new Observable.OnSubscribe<Boolean>() {
//                    @Override
//                    public void call(Subscriber<? super Boolean> subscriber) {
//                        //参数为要添加的好友的username和添加理由
//                        try {
//                            EMClient.getInstance().contactManager().addContact(toAddUsername, Constant.ADD_FRIEND_REASON);
//                            subscriber.onNext(true);
//                        } catch (HyphenateException e) {
//                            e.printStackTrace();
//                            subscriber.onNext(false);
//                        }
//                    }
//                }).subscribeOn(Schedulers.io()).subscribeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<Boolean>() {
//                    @Override
//                    public void call(Boolean isSuccess) {
//                        ((AddFriendActivity)mContext).showDialog();
//                        if(isSuccess){
//                            ToastUtils.showToast(R.string.add_friend_success);
//                        }else{
//                            ToastUtils.showToast(R.string.add_friend_error);
//                        }
//                    }
//                });
            }
        });
        return convertView;
    }
    class ViewHolder{
        TextView tvFriendName;
        Button btnAddFriend;
    }
}
