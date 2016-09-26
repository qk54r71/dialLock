package com.diallock.diallock.diallock.Activity.Common;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.util.Log;
import android.util.Patterns;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Pattern;

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

        public static void i(Context context,String strContent) {
            if (logingCheck) {
                Log.i(((Activity)context).getLocalClassName(), strContent);
            }
        }

        public static void d(String className, String strContent) {
            if (logingCheck) {
                Log.d(className, strContent);
            }
        }

        public static void d(Context context,String strContent) {
            if (logingCheck) {
                Log.d(((Activity)context).getLocalClassName(), strContent);
            }
        }

        public static void e(String className, String strContent) {
            if (logingCheck) {
                Log.e(className, strContent);
            }
        }

        public static void e(Context context,String strContent) {
            if (logingCheck) {
                Log.e(((Activity)context).getLocalClassName(), strContent);
            }
        }

        public static void w(String className, String strContent) {
            if (logingCheck) {
                Log.w(className, strContent);
            }
        }

        public static void w(Context context,String strContent) {
            if (logingCheck) {
                Log.w(((Activity)context).getLocalClassName(), strContent);
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
        SharedPreferences prefs = context.getSharedPreferences("dialUser", context.MODE_PRIVATE);

        CommonJava.Loging.i(context.getClass().getName(), strRequest + " : " + prefs.getString(strRequest, ""));

        return prefs.getString(strRequest, "");
    }

    /**
     * 요청하는 String 값을 저장
     *
     * @param context         : 현재 화면
     * @param strRequestName  : 저장될 이름
     * @param strRequestValue : 저장될 값
     * @return
     */
    public static Boolean saveSharedPreferences(Context context, String strRequestName, String strRequestValue) {

        SharedPreferences sharedPreferences = context.getApplicationContext().getSharedPreferences("dialUser", context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(strRequestName);

        editor.putString(strRequestName, strRequestValue);

        editor.commit();

        CommonJava.Loging.i(context.getClass().getName(), "strRequestName : " + strRequestName + " // strRequestValue : " + strRequestValue);

        return true;
    }

    /**
     * Gmail 가져오기
     */
    public static String getGmail(Context context) {
        Pattern emailPattern = Patterns.EMAIL_ADDRESS;
        Account[] accounts = AccountManager.get(context).getAccounts();

        String email = null;

        for (Account account : accounts) {
            if (emailPattern.matcher(account.name).matches()) {
                email = account.name;
                CommonJava.Loging.i("LockScreenActivity", "email : " + email);
            }
        }
        return email;
    }

    /**
     * 현재 년도 가져오기
     *
     * @return
     */
    public static String getYear(Date date) {

        SimpleDateFormat df = new SimpleDateFormat("yyyy", Locale.KOREA);
        String strYear = df.format(date);

        return strYear;
    }

    /**
     * 현재 월 가져오기
     *
     * @return
     */
    public static String getMonth(Date date) {

        SimpleDateFormat df = new SimpleDateFormat("MM", Locale.KOREA);
        String strMonth = df.format(date);

        return strMonth;
    }

    /**
     * 현재 일 가져오기
     *
     * @return
     */
    public static String getDay(Date date) {

        SimpleDateFormat df = new SimpleDateFormat("dd", Locale.KOREA);
        String strDay = df.format(date);

        return strDay;
    }

    /**
     * 현재 오전,오후 가져오기
     *
     * @return
     */
    public static String getAmPm(Date date) {

        SimpleDateFormat df = new SimpleDateFormat("HH", Locale.KOREA);
        String strHour = df.format(date);
        int intHour = Integer.parseInt(strHour);
        String strAmPm = null;

        if (intHour >= 13) {
            strAmPm = "오후";
        } else {
            strAmPm = "오전";
        }


        return strAmPm;
    }

    /**
     * 현재 시 가져오기
     *
     * @return
     */
    public static String getHour(Date date) {

        SimpleDateFormat df = new SimpleDateFormat("HH", Locale.KOREA);
        String strHour = df.format(date);
        int intHour = Integer.parseInt(strHour);

        strHour = String.valueOf(intHour % 12 == 0 ? 12 : intHour % 12);

        return strHour;
    }

    /**
     * 현재 분 가져오기
     *
     * @return
     */
    public static String getMinute(Date date) {

        SimpleDateFormat df = new SimpleDateFormat("mm", Locale.KOREA);
        String strMinute = df.format(date);

        return strMinute;
    }

    /**
     * 현재 요일 가져오기
     *
     * @return
     */
    public static String getDayOfWeek(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        String strWeek = null;

        int nWeek = cal.get(Calendar.DAY_OF_WEEK);
        if (nWeek == 1) {
            strWeek = "일";
        } else if (nWeek == 2) {
            strWeek = "월";
        } else if (nWeek == 3) {
            strWeek = "화";
        } else if (nWeek == 4) {
            strWeek = "수";
        } else if (nWeek == 5) {
            strWeek = "목";
        } else if (nWeek == 6) {
            strWeek = "금";
        } else if (nWeek == 7) {
            strWeek = "토";
        }

        return strWeek;
    }

    /**
     * 현재 날짜 및 시간 가져오기
     *
     * @return
     */
    public static Date getNowDate() {

        long now = System.currentTimeMillis();
        Date date = new Date(now);

        return date;
    }

}
