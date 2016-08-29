package com.diallock.diallock.diallock.Activity.taskAction;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.diallock.diallock.diallock.Activity.Common.CommonJava;

/**
 * Created by park on 2016-08-29.
 */
public class PackageReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        CommonJava.Loging.i(getClass().getName(), "onReceive() context : " + context);
        CommonJava.Loging.i(getClass().getName(), "onReceive() intent : " + intent);

        String action = intent.getAction();

        if (action.equals(Intent.ACTION_PACKAGE_ADDED)) {

            // 앱이 설치되었을 때

        } else if (action.equals(Intent.ACTION_PACKAGE_REMOVED)) {

            // 앱이 삭제되었을 때

        } else if (action.equals(Intent.ACTION_PACKAGE_REPLACED)) {
// 앱이 업데이트 되었을 때

            Intent i = new Intent(context, ScreenService.class);

            context.startService(i);
        }

    }

}

