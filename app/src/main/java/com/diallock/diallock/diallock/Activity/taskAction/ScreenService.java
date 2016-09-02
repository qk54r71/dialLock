package com.diallock.diallock.diallock.Activity.taskAction;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.IBinder;
import android.os.SystemClock;

import com.diallock.diallock.diallock.Activity.Activity.LockScreenViewActivity;
import com.diallock.diallock.diallock.Activity.Common.CommonJava;
import com.diallock.diallock.diallock.R;

/**
 * Created by park on 2016-08-22.
 */
public class ScreenService extends Service {


    private ScreenReceiver mReceiver = null;
    private PackageReceiver pReceiver;
    private RestartReceiver rReceiver;
    private Boolean mLockCheck = false;
    private NotificationManager mNotificationManager;
    private int mStartId;


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
        filter.addAction(Intent.ACTION_SCREEN_ON);
        registerReceiver(mReceiver, filter);


        pReceiver = new PackageReceiver();
        IntentFilter pFilter = new IntentFilter(Intent.ACTION_PACKAGE_ADDED);
        pFilter.addAction(Intent.ACTION_PACKAGE_REMOVED);
        pFilter.addAction(Intent.ACTION_PACKAGE_REPLACED);
        pFilter.addDataScheme("package");
        registerReceiver(pReceiver, pFilter);

        rReceiver = new RestartReceiver();
        IntentFilter rFilter = new IntentFilter(RestartReceiver.ACTION_RESTART_SERVICE);
        rFilter.addAction(Intent.ACTION_BOOT_COMPLETED);
        registerReceiver(rReceiver, rFilter);


        mLockCheck = Boolean.valueOf(CommonJava.loadSharedPreferences(getApplicationContext(), "lockCheck"));

    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        super.onStartCommand(intent, flags, startId);

        CommonJava.Loging.i(getClass().getName(), "onStartCommand()");
        mStartId = startId;
        CommonJava.Loging.i(getClass().getName(), "mStartId : " + mStartId);


        if (intent != null) {

            if (intent.getAction() == null) {
                CommonJava.Loging.i(getClass().getName(), "intent.getAction() :" + intent.getAction());
                CommonJava.Loging.i(getClass().getName(), "mLockCheck :" + mLockCheck);

                if (mLockCheck && mNotificationManager == null) {
                    mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

                    startForeground(1, new Notification());

                    Notification notification;

                    CommonJava.Loging.i(getClass().getName(), "Build.VERSION.SDK_INT : " + Build.VERSION.SDK_INT);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        CommonJava.Loging.i(getClass().getName(), "JELLY_BEAN  Custom Noti");

                        notification = new Notification.Builder(getApplicationContext())
                                .setSmallIcon(R.drawable.dial_icon)
                                .setContentTitle("DialLock")
                                .setContentText("잠금화면이 실행중입니다.")
                                .build();

                        mNotificationManager.notify(startId, notification);
                        mNotificationManager.cancel(startId);

                    }

                }

                if (mReceiver == null) {

                    mReceiver = new ScreenReceiver();

                    IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_OFF);

                    registerReceiver(mReceiver, filter);
                    CommonJava.Loging.i(getClass().getName(), "registerReceiver()");

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
