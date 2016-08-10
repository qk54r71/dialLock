package com.diallock.diallock.diallock.Activity.Common;

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
}
