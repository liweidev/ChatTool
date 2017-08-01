package com.example.liwei.chattool.util;

import android.util.Log;

/**
 * Created by liwei on 2017/8/1.
 * 日志工具
 */

public class LogUtil {
    public static final int LEVEL_VERBOSE=0;
    public static final int LEVEL_DEBUG=1;
    public static final int LEVEL_INFO=2;
    public static final int LEVEL_WARM=3;
    public static final int LEVEL_ERROR=4;
    //TODO 正式发布时候，修改 CURRENT_LEVEL的值
    public static final int CURRENT_LEVEL=1;

    public static void v(String tag,String message){
        if(CURRENT_LEVEL>=LEVEL_VERBOSE){
            Log.v(tag,message);
        }
    }

    public static void d(String tag,String message){
        if(CURRENT_LEVEL>=LEVEL_DEBUG){
            Log.d(tag,message);
        }
    }

    public static void i(String tag,String message){
        if(CURRENT_LEVEL>=LEVEL_INFO){
            Log.i(tag,message);
        }
    }

    public static void w(String tag,String message){
        if(CURRENT_LEVEL>=LEVEL_WARM){
            Log.w(tag,message);
        }
    }

    public static void e(String tag,String message){
        if(CURRENT_LEVEL>=LEVEL_ERROR){
            Log.e(tag,message);
        }
    }

}
