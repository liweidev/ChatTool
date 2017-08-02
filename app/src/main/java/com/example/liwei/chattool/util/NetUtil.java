package com.example.liwei.chattool.util;

import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by liwei on 2017/8/2.
 * 网络工具
 */

public class NetUtil {

    //网络请求GET
    public static void sendHttpRequest(String url, final Callback callback){
        Observable.just(url)
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(Schedulers.io())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String url) {
                        OkHttpClient client=new OkHttpClient();
                        Request request=new Request.Builder().url(url).build();
                        client.newCall(request).enqueue(callback);
                    }
                });
    }

}
