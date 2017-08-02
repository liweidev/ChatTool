package com.example.liwei.chattool.util;

import android.widget.Toast;

import com.example.liwei.chattool.app.MyApp;

/**
 * Created by liwei on 2017/8/1.
 * 土司工具
 */

public class ToastUtils {
    private static Toast toast=null;
    public static void showToast(String message){
        if(toast==null){
            synchronized (ToastUtils.class){
                if(toast==null){
                    toast=Toast.makeText(MyApp.getmContext(),message,Toast.LENGTH_SHORT);
                }else{
                    toast.setText(message);
                }
            }
        }else{
            toast.setText(message);
        }
        toast.show();
    }
    public static void showToast(int messageId){
        if(toast==null){
            synchronized (ToastUtils.class){
                if(toast==null){
                    toast=Toast.makeText(MyApp.getmContext(),MyApp.getmContext().getString(messageId),Toast.LENGTH_SHORT);
                }else{
                    toast.setText(MyApp.getmContext().getString(messageId));
                }
            }
        }else{
            toast.setText(MyApp.getmContext().getString(messageId));
        }
        toast.show();
    }



}
