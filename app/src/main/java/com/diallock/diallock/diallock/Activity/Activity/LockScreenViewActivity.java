package com.diallock.diallock.diallock.Activity.Activity;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.diallock.diallock.diallock.Activity.Common.CommonJava;
import com.diallock.diallock.diallock.Activity.Common.GMailSender;
import com.diallock.diallock.diallock.Activity.Common.HomeKeyLocker;
import com.diallock.diallock.diallock.Activity.Common.LockScreenManager;
import com.diallock.diallock.diallock.Activity.Layout.CircleLayout;
import com.diallock.diallock.diallock.Activity.taskAction.NoLockStatusListenerException;
import com.diallock.diallock.diallock.Activity.taskAction.ScreenReceiver;
import com.diallock.diallock.diallock.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 이메일 보내기
 * 출처 : {Link :http://blog.naver.com/PostView.nhn?blogId=junhwen&logNo=130151732452 }
 */
public class LockScreenViewActivity extends BaseActivity implements LockScreenManager.LockStatusListener{

    private CircleLayout circleLayout;
    private Boolean backFlag;
    private TextView txt_lock_day;
    private TextView txt_lock_time;


    /**
     * 비밀번호 찾기 버튼
     */
    private Button btn_find_pass;
    private Timer mTimer;

    /**
     * test용 버튼
     */
    private Button btn_cancle;
    private LockScreenManager mLockScreeniManager;

    public static Activity mLockScreenViewActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().addFlags(
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                        WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                        WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);

        CommonJava.Loging.i("LockScreenActivity", "onCreate()");



        setupView(R.layout.activity_lock_screen_view);
        mLockScreeniManager = LockScreenManager.getInstance(LockScreenViewActivity.this); // 한개의 액티비티만 생성 되게 함 싱글톤 방식
        mLockScreeniManager.setLockStatusListener(this);
        mLockScreeniManager.setLockScreen(LayoutInflater.from(LockScreenViewActivity.this).inflate(R.layout.activity_lock_screen, null));
        mLockScreeniManager.updateActivity(LockScreenViewActivity.this);

       /* HomeKeyLocker homeKeyLoader = new HomeKeyLocker();
        homeKeyLoader.lock(this);*/

        init();

    }


    private void setFindView() {

    }

    private void init() {
        mLockScreenViewActivity = this;
    }

    public void onFinish() {
        CommonJava.Loging.i(getLocalClassName(), "onFinish()");
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        CommonJava.Loging.i(getLocalClassName(), "onStart()");

        try {
            mLockScreeniManager.Lock();
        } catch (NoLockStatusListenerException e) {
            e.printStackTrace();
        }
    }

    @Override

    protected void onDestroy() {
        mLockScreeniManager.unLock();
        mLockScreeniManager.updateActivity(LockScreenViewActivity.this);
        mLockScreeniManager.timeCancle();
        super.onDestroy();

    }


    @Override

    protected void onPause() {

        mLockScreeniManager.timeCancle();

        super.onPause();

    }


    @Override

    protected void onResume() {

        super.onResume();
        mLockScreeniManager.timeStart();

    }


    @Override
    public void onLocked() {

    }

    @Override
    public void onUnlock() {
        mLockScreeniManager.timeCancle();
        mLockScreeniManager.unLock();
    }
}
