package com.diallock.diallock.diallock.Activity.taskAction;

import android.app.KeyguardManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import com.diallock.diallock.diallock.Activity.Activity.LockScreenActivity;
import com.diallock.diallock.diallock.Activity.Activity.LockScreenViewActivity;
import com.diallock.diallock.diallock.Activity.Common.CommonJava;

/**
 * Created by park on 2016-08-22.
 * 출처 {link : http://ccdev.tistory.com/16}
 */
public class ScreenReceiver extends BroadcastReceiver {


    private KeyguardManager km = null;
    private KeyguardManager.KeyguardLock keyLock = null;
    private TelephonyManager telephonyManager = null;
    private boolean isPhoneIdle = true;

    @Override
    public void onReceive(Context context, Intent intent) {

        CommonJava.Loging.i(getClass().getName(), "onReceive() context : " + context);
        CommonJava.Loging.i(getClass().getName(), "onReceive() intent : " + intent);


        if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
            if (km == null)

                km = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);


            if (keyLock == null)


                if (telephonyManager == null) {
                    telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                    telephonyManager.listen(phoneListener, PhoneStateListener.LISTEN_CALL_STATE);
                }


            if (isPhoneIdle) {
                keyLock = km.newKeyguardLock(Context.KEYGUARD_SERVICE);
                disableKeyguard();

                Intent intentLockScreenView = new Intent(context, LockScreenViewActivity.class);
                intentLockScreenView.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intentLockScreenView.putExtra("strSwitch","ScreenReceiver");
                context.startActivity(intentLockScreenView);
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
                case TelephonyManager.CALL_STATE_IDLE:
                    isPhoneIdle = true;
                    break;

                case TelephonyManager.CALL_STATE_RINGING:
                    isPhoneIdle = false;
                    break;

                case TelephonyManager.CALL_STATE_OFFHOOK:
                    isPhoneIdle = false;
                    break;
            }

        }

    };


}
