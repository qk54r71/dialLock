package com.diallock.diallock.diallock.Activity.Common;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by Administrator on 2016-08-10
 */
public class CommonJava {
    /**
     * Log문
     */
    public static class Loging {
        public static Boolean logingCheck = true;

        public static void i(String className, String strContent) {
            if (logingCheck) {
                Log.i(className, strContent);
            }
        }

        public static void d(String className, String strContent) {
            if (logingCheck) {
                Log.d(className, strContent);
            }
        }

        public static void e(String className, String strContent) {
            if (logingCheck) {
                Log.e(className, strContent);
            }
        }
    }

    /**
     * 저장된 값을 요청해서 불러옴
     *
     * @param context    : 현재 화면
     * @param strRequest : 불러올 값
     * @return
     */
    public static String loadSharedPreferences(Context context, String strRequest) {
        SharedPreferences prefs = context.getSharedPreferences("saveUser", context.MODE_PRIVATE);

        CommonJava.Loging.i(context.getClass().getName(), strRequest + " : " + prefs.getString(strRequest, ""));

        return prefs.getString(strRequest, "");
    }

    /**
     * 요청하는 값을 저장
     *
     * @param context         : 현재 화면
     * @param strRequestName  : 저장될 이름
     * @param strRequestValue : 저장될 값
     * @return
     */
    public static Boolean saveSharedPreferences(Context context, String strRequestName, String strRequestValue) {

        SharedPreferences sharedPreferences = context.getApplicationContext().getSharedPreferences("saveUser", context.getApplicationContext().MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();

        editor.putString(strRequestName, strRequestValue);

        editor.commit();
        return true;
    }
}
