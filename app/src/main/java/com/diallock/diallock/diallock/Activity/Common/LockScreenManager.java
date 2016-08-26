package com.diallock.diallock.diallock.Activity.Common;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.diallock.diallock.diallock.Activity.Activity.LockScreenViewActivity;
import com.diallock.diallock.diallock.Activity.Layout.CircleLayout;
import com.diallock.diallock.diallock.R;

import java.lang.ref.WeakReference;

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

                ActivityCompat.finishAffinity(mActivity);
                System.exit(0);
                mActivity.finish();
            }
        });

        mLockView.findViewById(R.id.btn_find_pass).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startEmailSend();

                ((LockScreenViewActivity) mActivity).toastFunc();
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

    public synchronized void Lock() {
        CommonJava.Loging.i(getClass().getName(), "Lock()");
        if (mLockView != null && !mIsLock) {
            if (mIsLock) {
                getWindowManager().updateViewLayout(mLockView, layoutParams);
                CommonJava.Loging.i(getClass().getName(), "updateViewLayout()");
            } else {
                getWindowManager().addView(mLockView, layoutParams);
                CommonJava.Loging.i(getClass().getName(), "addView()");
            }
        }


        setFindView();

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
}
