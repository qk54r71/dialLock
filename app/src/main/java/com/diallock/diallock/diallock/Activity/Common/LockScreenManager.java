package com.diallock.diallock.diallock.Activity.Common;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.diallock.diallock.diallock.Activity.Activity.LockScreenViewActivity;
import com.diallock.diallock.diallock.Activity.Layout.CircleLayout;
import com.diallock.diallock.diallock.Activity.taskAction.NoLockStatusListenerException;
import com.diallock.diallock.diallock.R;

import java.lang.ref.WeakReference;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by park on 2016-08-26.
 */
public class LockScreenManager {

    public static LockScreenManager mLockScreenManager;
    public static Activity mActivity;
    public static View mLockView;
    private WindowManager.LayoutParams layoutParams;
    private boolean mIsLock;
    private WeakReference<WindowManager> mWindowManagerRef;
    private CircleLayout circleLayout;
    private Button btn_cancle;
    private Timer mTimer;
    private Handler mHandler = new Handler();
    private LockStatusListener lockStatusListener;

    public static synchronized LockScreenManager getInstance(Activity activity) {
        CommonJava.Loging.i(activity.getLocalClassName(), "getInstance");
        if (mLockScreenManager == null)
            mLockScreenManager = new LockScreenManager(activity);
        return mLockScreenManager;
    }

    public LockScreenManager(Activity activity) {
        this.mActivity = activity;

        init();
    }

    private void setFindView() {
    }

    public void updateActivity(Activity ac) {
        this.mActivity = ac;
    }

    private void init() {
        mIsLock = false;
        mWindowManagerRef = new WeakReference<WindowManager>(mActivity.getWindowManager());
        layoutParams = new WindowManager.LayoutParams();
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ERROR; // 이 기능임
        layoutParams.flags = 1280;


        MainTimerTask timerTask = new MainTimerTask();

        mTimer = new Timer();

        mTimer.schedule(timerTask, 0, 1000);
    }


    class MainTimerTask extends TimerTask {

        public void run() {

            mHandler.post(mUpdateTimeTask);

        }

    }

    private Runnable mUpdateTimeTask = new Runnable() {

        public void run() {
            String strTxtLockDay =
                    CommonJava.getYear() + "년 " + CommonJava.getMonth() + "월 " + CommonJava.getDay() + "일 " + CommonJava.getDayOfWeek();
            String strTxtLockTime =
                    CommonJava.getAmPm() + " " + CommonJava.getHour() + "시 " + CommonJava.getMinute() + "분";

            ((TextView) mLockView.findViewById(R.id.txt_lock_day)).setText(strTxtLockDay);
            ((TextView) mLockView.findViewById(R.id.txt_lock_time)).setText(strTxtLockTime);
        }

    };

    public void timeCancle() {
        mTimer.cancel();
    }

    public void timeStart() {
        if (mTimer != null) {
            MainTimerTask timerTask = new MainTimerTask();
            mTimer = new Timer();
            mTimer.schedule(timerTask, 500, 3000);
        }
    }

    public void setLockScreen(View v) {
        mLockView = v;
        setLockBg();
        mLockView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                CommonJava.Loging.i(getClass().getName(), "onTouch : " + event);
                float xLocation = event.getX(0);
                float yLocation = event.getY(0);

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        ((CircleLayout) mLockView.findViewById(R.id.circle_screen)).screenTouchLocationStart(xLocation, yLocation);
                        break;
                    case MotionEvent.ACTION_MOVE:

                        ((CircleLayout) mLockView.findViewById(R.id.circle_screen)).screenTouchLocationDrag(xLocation, yLocation);
                        break;
                    case MotionEvent.ACTION_UP:

                        ((CircleLayout) mLockView.findViewById(R.id.circle_screen)).screenTouchLocationEnd(xLocation, yLocation);
                        break;
                }

                return false;
            }
        });

        mLockView.findViewById(R.id.btn_cancle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CommonJava.Loging.i(getClass().getName(), "mActivity : " + mActivity);

                //ActivityCompat.finishAffinity(mActivity);
                //System.exit(0);
                unLock();
            }
        });

        mLockView.findViewById(R.id.btn_find_pass).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startEmailSend();
                startTxtToast();
            }
        });


    }

    private void setLockBg() {
        Uri uri = Uri.parse("");
        String path = uri.getPath();
        Bitmap bitmap = null;
        Point point = new Point();
        ScreenHelper.getScreenPixel(mActivity, point);
        bitmap = ImageHelper.scaleImage(path, point.x, point.y);
    }

    private WindowManager getWindowManager() {
        WindowManager windowManager = mWindowManagerRef.get();
        if (windowManager == null) {
            windowManager = mActivity.getWindowManager();
            mWindowManagerRef = new WeakReference<WindowManager>(windowManager);
        }
        return windowManager;
    }

    public synchronized void Lock() throws NoLockStatusListenerException {
        CommonJava.Loging.i(getClass().getName(), "Lock()");


        if (lockStatusListener == null) {
            throw new NoLockStatusListenerException();
        }

        if (mLockView != null && !mIsLock) {
            if (mIsLock) {
                getWindowManager().updateViewLayout(mLockView, layoutParams);
                CommonJava.Loging.i(getClass().getName(), "updateViewLayout()");
            } else {
                getWindowManager().addView(mLockView, layoutParams);
                CommonJava.Loging.i(getClass().getName(), "addView()");
            }

            mIsLock = true;
            lockStatusListener.onLocked();

        }


        setFindView();

    }

    public synchronized void unLock() {
        Log.i(getClass().getName(), "unLock()");
        Log.i(getClass().getName(), "getWindowManager() : " + getWindowManager());
        Log.i(getClass().getName(), "mIsLock : " + mIsLock);
        if (getWindowManager() != null && mIsLock) {
            Log.i(getClass().getName(), "unLock() removeView");
            getWindowManager().removeView(mLockView);
            mIsLock = false;
            mActivity.finish();
        }
    }

    /**
     * 등록된 이메일로 비밀번호 보내는 함수
     */
    private void startEmailSend() {

        AsyncTask asyncTask = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {
                try {

                    GMailSender mail = new GMailSender("geogotae@gmail.com", "molppang202501");  //보내는 사람 메일 주소와 암호
                    String email = CommonJava.loadSharedPreferences(mActivity, "email");
                    String password = CommonJava.loadSharedPreferences(mActivity, "password");
                    //순서대로, 제목 - 본문 - 보내는 사람 메일 - 받는 사람 메일

                    mail.sendMail(
                            "다이얼락 비밀번호입니다.",
                            password,
                            "geogotae@gmail.com",
                            email
                    );


                } catch (Exception e) {
                }

                return null;
            }
        };
        asyncTask.execute();
    }

    /**
     * 이메일 발송시 토스트 메시지 2초 동안 보여줌
     */
    private void startTxtToast() {
        ((TextView) mLockView.findViewById(R.id.txt_toast)).setVisibility(View.VISIBLE);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ((TextView) mLockView.findViewById(R.id.txt_toast)).setVisibility(View.INVISIBLE);
            }
        }, 2000);


    }

    public void setLockStatusListener(LockStatusListener listener) {
        this.lockStatusListener = listener;
    }

    public interface LockStatusListener {
        void onLocked();

        void onUnlock();
    }


}
