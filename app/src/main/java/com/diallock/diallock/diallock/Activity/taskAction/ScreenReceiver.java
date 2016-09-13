package com.diallock.diallock.diallock.Activity.taskAction;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.KeyguardManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import com.diallock.diallock.diallock.Activity.Activity.LockScreenActivity;
import com.diallock.diallock.diallock.Activity.Activity.LockScreenViewActivity;
import com.diallock.diallock.diallock.Activity.Common.CommonJava;
import com.diallock.diallock.diallock.Activity.Common.LockScreenManager;

/**
 * Created by park on 2016-08-22.
 * 출처 {link : http://ccdev.tistory.com/16}
 */
public class ScreenReceiver extends BroadcastReceiver {


    private KeyguardManager km = null;
    private KeyguardManager.KeyguardLock keyLock = null;
    private TelephonyManager telephonyManager = null;
    private boolean isPhoneIdle = true;
    private Boolean mLockCheck = false;
    private Context mContext;
    private Boolean mPhoneState = false;
    private int mTimeCheck = 0;
    private Handler handler = new Handler();

    @Override
    public void onReceive(Context context, Intent intent) {
        CommonJava.Loging.i(getClass().getName(), "onReceive()");
        CommonJava.Loging.i(getClass().getName(), "context : " + context);
        CommonJava.Loging.i(getClass().getName(), "intent : " + intent);

        isLockCheck(context);
        mContext = context;
        if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
            CommonJava.Loging.i(getClass().getName(), "ACTION_SCREEN_ON");
            //handler.removeCallbacks(startActivity);
        }

        if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
            CommonJava.Loging.i(getClass().getName(), "ACTION_SCREEN_OFF");
            if (km == null)

                km = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);


            if (keyLock == null)


                if (telephonyManager == null) {
                    telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                    telephonyManager.listen(phoneListener, PhoneStateListener.LISTEN_CALL_STATE);
                }


            if (isPhoneIdle && mLockCheck) {
                CommonJava.Loging.i(getClass().getName(), "isPhoneIdle && mLockCheck Lock start");

                //handler.postDelayed(startActivity, 2000);
                handler.post(startActivity);
            }

        }

    }

    public void reenableKeyguard() {
        if (keyLock != null) {
            keyLock.reenableKeyguard();
        }

    }


    public void disableKeyguard() {

        keyLock.disableKeyguard();


    }

    private PhoneStateListener phoneListener = new PhoneStateListener() {

        @Override

        public void onCallStateChanged(int state, String incomingNumber) {

            CommonJava.Loging.i(getClass().getName(), "onCallStateChanged() state : " + state);
            CommonJava.Loging.i(getClass().getName(), "onCallStateChanged() incomingNumber : " + incomingNumber);

            switch (state) {
                case TelephonyManager.CALL_STATE_IDLE: // 통화가 종료된 상태
                    isPhoneIdle = true;
                    if (mPhoneState && ScreenService.mPhoneProgressLock) {
                        CommonJava.Loging.i(getClass().getName(), "mPhoneState Lock start");
                        keyLock = km.newKeyguardLock(Context.KEYGUARD_SERVICE);
                        disableKeyguard();

                        Intent intentLockScreenView = new Intent(mContext, LockScreenViewActivity.class);
                        intentLockScreenView.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intentLockScreenView.putExtra("strSwitch", "ScreenReceiver");
                        mContext.startActivity(intentLockScreenView);
                        mPhoneState = false;
                    }

                    break;

                case TelephonyManager.CALL_STATE_RINGING: // 전화가 걸려오는 상태
                    isPhoneIdle = false;

                    if (mContext != null) {
                        isLockCheck(mContext);
                        if (mLockCheck && LockScreenViewActivity.mLockScreenViewActivity != null) {
                            CommonJava.Loging.i(getClass().getName(), "mLockScreenViewActivity finish()");
                            mPhoneState = true;
                            LockScreenViewActivity.mLockScreenViewActivity.finish();
                        }
                    }

                    break;

                case TelephonyManager.CALL_STATE_OFFHOOK: // 전화를 받은 상태
                    isPhoneIdle = false;

                    if (mPhoneState && ScreenService.mPhoneProgressLock) {
                        CommonJava.Loging.i(getClass().getName(), "mPhoneState Lock start");
                        keyLock = km.newKeyguardLock(Context.KEYGUARD_SERVICE);
                        disableKeyguard();

                        Intent intentLockScreenView = new Intent(mContext, LockScreenViewActivity.class);
                        intentLockScreenView.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intentLockScreenView.putExtra("strSwitch", "ScreenReceiver");
                        mContext.startActivity(intentLockScreenView);
                        mPhoneState = false;
                    }

                    break;
            }

        }

    };

    private void isLockCheck(Context context) {
        mLockCheck = Boolean.valueOf(CommonJava.loadSharedPreferences(context, "lockCheck"));
    }

    private Runnable startActivity = new Runnable() {
        @Override
        public void run() {
            CommonJava.Loging.i(getClass().getName(), "isPhoneIdle && mLockCheck Lock start 2초");
            keyLock = km.newKeyguardLock(Context.KEYGUARD_SERVICE);
            disableKeyguard();

            Intent intentLockScreenView = new Intent(mContext, LockScreenViewActivity.class);
            intentLockScreenView.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intentLockScreenView.putExtra("strSwitch", "ScreenReceiver");
            mContext.startActivity(intentLockScreenView);

            ScreenService.mPhoneProgressLock = true;

        }
    };
}
