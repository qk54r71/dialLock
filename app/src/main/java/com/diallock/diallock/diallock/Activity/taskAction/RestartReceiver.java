package com.diallock.diallock.diallock.Activity.taskAction;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.diallock.diallock.diallock.Activity.Common.CommonJava;

/**
 * Created by park on 2016-08-29.
 */
public class RestartReceiver extends BroadcastReceiver {

    static public final String ACTION_RESTART_SERVICE = "RestartReceiver.restart";    // 값은 맘대로


    @Override

    public void onReceive(Context context, Intent intent) {

        CommonJava.Loging.i(getClass().getName(), "onReceive() context : " + context);
        CommonJava.Loging.i(getClass().getName(), "onReceive() intent : " + intent);

        if (intent.getAction().equals(ACTION_RESTART_SERVICE)) {

            Intent intentScreenReStart = new Intent(context, ScreenService.class);

            context.startService(intentScreenReStart);

        }

        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {

            CommonJava.Loging.i(getClass().getName(), "ACTION_BOOT_COMPLETED ");

            Intent intentScreenBoot = new Intent(context, ScreenService.class);

            context.startService(intentScreenBoot);

        }

    }

}

