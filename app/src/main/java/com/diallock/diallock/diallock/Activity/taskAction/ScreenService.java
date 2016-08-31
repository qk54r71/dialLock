package com.diallock.diallock.diallock.Activity.taskAction;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.IBinder;
import android.os.SystemClock;

import com.diallock.diallock.diallock.Activity.Common.CommonJava;
import com.diallock.diallock.diallock.R;

/**
 * Created by park on 2016-08-22.
 */
public class ScreenService extends Service {


    private ScreenReceiver mReceiver = null;
    private PackageReceiver pReceiver;


    @Override

    public IBinder onBind(Intent intent) {

        return null;

    }


    @Override

    public void onCreate() {

        super.onCreate();
        CommonJava.Loging.i(getClass().getName(), "onCreate()");

        mReceiver = new ScreenReceiver();
        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
        registerReceiver(mReceiver, filter);


        pReceiver = new PackageReceiver();
        IntentFilter pFilter = new IntentFilter(Intent.ACTION_PACKAGE_ADDED);
        pFilter.addAction(Intent.ACTION_PACKAGE_REMOVED);
        pFilter.addAction(Intent.ACTION_PACKAGE_REPLACED);
        pFilter.addDataScheme("package");
        registerReceiver(pReceiver, pFilter);

    }


    @Override

    public int onStartCommand(Intent intent, int flags, int startId) {

        super.onStartCommand(intent, flags, startId);


        CommonJava.Loging.i(getClass().getName(), "onStartCommand()");

        if (intent != null) {

            if (intent.getAction() == null) {

                if (mReceiver == null) {

                    mReceiver = new ScreenReceiver();

                    IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_OFF);

                    registerReceiver(mReceiver, filter);
                    CommonJava.Loging.i(getClass().getName(), "registerReceiver()");

                    /*
                    NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

                    Notification notification = new Notification(R.drawable.dial_icon, "서비스 실행됨", System.currentTimeMillis());

                    startForeground(1, notification);*/

                }

            }

        }

        return START_REDELIVER_INTENT;

    }


    @Override

    public void onDestroy() {

        super.onDestroy();

        CommonJava.Loging.i(getClass().getName(), "onDestroy()");

        if (mReceiver != null) {
            mReceiver.reenableKeyguard();
            unregisterReceiver(mReceiver);
        }

        if (pReceiver != null) {
            unregisterReceiver(pReceiver);
        }


    }

    public void registerRestartAlarm(boolean isOn) {

        Intent intent = new Intent(ScreenService.this, RestartReceiver.class);

        intent.setAction(RestartReceiver.ACTION_RESTART_SERVICE);

        PendingIntent sender = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, 0);


        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);

        if (isOn) {
            am.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + 1000, 10000, sender);
        } else {
            am.cancel(sender);
        }

    }


}
