package com.diallock.diallock.diallock.Activity.Activity;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.KeyEvent;
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
import com.diallock.diallock.diallock.Activity.Layout.CircleLayout;
import com.diallock.diallock.diallock.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.locks.Lock;
import java.util.regex.Pattern;

/**
 * 이메일 보내기
 * 출처 : {Link :http://blog.naver.com/PostView.nhn?blogId=junhwen&logNo=130151732452 }
 */
public class LockScreenActivity extends AppCompatActivity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock_screen);
        CommonJava.Loging.i("LockScreenActivity", "onCreate()");

        getWindow().addFlags(
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                        WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        setFindView();
        init();
        setOnClick();

        //HomeKeyLocker homeKeyLoader = new HomeKeyLocker();

        //homeKeyLoader.lock(this);


    }


    private void setFindView() {
        circleLayout = (CircleLayout) findViewById(R.id.circle_screen);
        btn_find_pass = (Button) findViewById(R.id.btn_find_pass);
        txt_lock_day = (TextView) findViewById(R.id.txt_lock_day);
        txt_lock_time = (TextView) findViewById(R.id.txt_lock_time);

        btn_cancle = (Button) findViewById(R.id.btn_cancle); // test용
    }

    private void init() {
        backFlag = false;

        String strTxtLockDay =
                CommonJava.getYear() + "년 " + CommonJava.getMonth() + "월 " + CommonJava.getDay() + "일 " + (CommonJava.getDayOfWeek().contains("요일") ? CommonJava.getDayOfWeek():CommonJava.getDayOfWeek()+"일");
        String strTxtLockTime =
                CommonJava.getAmPm() + " " + CommonJava.getHour() + "시 " + CommonJava.getMinute() + "분";

        txt_lock_day.setText(strTxtLockDay);
        txt_lock_time.setText(strTxtLockTime);

        MainTimerTask timerTask = new MainTimerTask();

        mTimer = new Timer();

        mTimer.schedule(timerTask, 500, 1000);


    }

    private Handler mHandler = new Handler();

    private Runnable mUpdateTimeTask = new Runnable() {

        public void run() {
            String strTxtLockTime =
                    CommonJava.getAmPm() + " " + CommonJava.getHour() + "시 " + CommonJava.getMinute() + "분";
            txt_lock_time.setText(strTxtLockTime);
        }

    };


    class MainTimerTask extends TimerTask {

        public void run() {

            mHandler.post(mUpdateTimeTask);

        }

    }


    private void setOnClick() {
        btn_find_pass.setOnClickListener(onClickListener);

        btn_cancle.setOnClickListener(onClickListener); // test용
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btn_find_pass:
                    CommonJava.Loging.i("LockScreenActivity", "onClick()");
                    startEmailSend();

                    Toast.makeText(LockScreenActivity.this, "플레이스토어에 등록된 gmail 로 비밀번호가 전송되었습니다.", Toast.LENGTH_SHORT).show();

                    break;
                case R.id.btn_cancle: // test용
                    finish();
                    break;
            }
        }
    };

    /**
     * 등록된 이메일로 비밀번호 보내는 함수
     */
    private void startEmailSend() {

        AsyncTask asyncTask = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {
                try {

                    GMailSender mail = new GMailSender("geogotae@gmail.com", "molppang202501");  //보내는 사람 메일 주소와 암호
                    String email = CommonJava.loadSharedPreferences(LockScreenActivity.this, "email");
                    String password = CommonJava.loadSharedPreferences(LockScreenActivity.this, "password");
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


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        //CommonJava.Loging.i("LockScreenActivity", "onTouchEvent : " + event);

        float xLocation = event.getX(0);
        float yLocation = event.getY(0);

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                circleLayout.screenTouchLocationStart(xLocation, yLocation);
                break;
            case MotionEvent.ACTION_MOVE:

                circleLayout.screenTouchLocationDrag(xLocation, yLocation);
                break;
            case MotionEvent.ACTION_UP:

                circleLayout.screenTouchLocationEnd(xLocation, yLocation);
                break;
        }

        return super.onTouchEvent(event);
    }

    /**
     * 뒤로가기 버튼 클릭 시 종료
     */
    @Override
    public void onBackPressed() {

        CommonJava.Loging.i("MainActivity", "onBackPressed()");


        if (backFlag) {

            CommonJava.Loging.i("MainActivity", "onBackPressed() : 종료");

            finish();
        } else {
            backFlag = true;

            Toast.makeText(LockScreenActivity.this, "한번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show();
            Handler han = new Handler();
            han.postDelayed(new Runnable() {

                @Override
                public void run() {
                    backFlag = false;
                }
            }, 2000);
        }

    }

    @Override

    protected void onDestroy() {

        mTimer.cancel();

        super.onDestroy();

    }


    @Override

    protected void onPause() {

        mTimer.cancel();

        super.onPause();

    }


    @Override

    protected void onResume() {

        super.onResume();
        if (mTimer != null) {
            MainTimerTask timerTask = new MainTimerTask();
            mTimer = new Timer();
            mTimer.schedule(timerTask, 500, 3000);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        return false;

    }

    public void isToast(String strMsg) {
        Toast.makeText(LockScreenActivity.this, strMsg, Toast.LENGTH_SHORT).show();
    }


}
