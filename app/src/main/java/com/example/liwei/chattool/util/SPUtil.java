package com.example.liwei.chattool.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by liwei on 2017/8/7.
 */
//SharedPrefrence工具类
public class SPUtil {
    private static SharedPreferences sp=null;
    private static SharedPreferences.Editor editor=null;
    private static SPUtil spUtil;
    //获取实例
    public static SPUtil getInstance(Context mContext, String spName){
        sp=mContext.getSharedPreferences(spName,Context.MODE_PRIVATE);
        editor=sp.edit();
        if(spUtil==null){
            spUtil=new SPUtil();
        }
        return spUtil;
    }
    //存入
    public void put(String key,String value){
        editor.putString(key,value);
        editor.apply();
    }
    //获取
    public String get(String key,String defaultValue){
        String value=sp.getString(key,defaultValue);
        return value;
    }
    //存入
    public void put(String key,boolean value){
        editor.putBoolean(key,value);
        editor.apply();
    }
    //获取
    public boolean get(String key,boolean defaultValue){
        boolean value=sp.getBoolean(key,defaultValue);
        return value;
    }


}
