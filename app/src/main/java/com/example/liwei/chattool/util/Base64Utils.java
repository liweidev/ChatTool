package com.example.liwei.chattool.util;

import android.util.Base64;

import java.io.UnsupportedEncodingException;

/**
 * Created by liwei on 2017/8/3.
 * Base64加密解密
 */

public class Base64Utils {
    //加密
    public static String encode(String str){
        String result="";
        if(str!=null){
            try {
                result=new String(Base64.encode(str.getBytes("utf-8"),Base64.NO_WRAP),"utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
    // 解密
    public static String decode(String str) {
        String result = "";
        if (str != null) {
            try {
                result = new String(Base64.decode(str, Base64.NO_WRAP), "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

}
