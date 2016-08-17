package com.diallock.diallock.diallock.Activity.Activity;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.diallock.diallock.diallock.Activity.Common.CommonJava;
import com.diallock.diallock.diallock.Activity.Common.GMailSender;
import com.diallock.diallock.diallock.Activity.Layout.CircleLayout;
import com.diallock.diallock.diallock.R;

import java.util.regex.Pattern;

/**
 * 이메일 보내기
 * 출처 : {Link :http://blog.naver.com/PostView.nhn?blogId=junhwen&logNo=130151732452 }
 */
public class LockScreenActivity extends AppCompatActivity {

    private CircleLayout circleLayout;
    private Boolean backFlag;

    /**
     * 비밀번호 찾기 버튼
     */
    private Button btn_find_pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock_screen);
        CommonJava.Loging.i("LockScreenActivity", "onCreate()");

        setFindView();
        init();
        setOnClick();


    }


    private void setFindView() {
        circleLayout = (CircleLayout) findViewById(R.id.circle_screen);
        btn_find_pass = (Button) findViewById(R.id.btn_find_pass);
    }

    private void init() {
        backFlag = false;
    }

    private void setOnClick() {
        btn_find_pass.setOnClickListener(onClickListener);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btn_find_pass:
                    CommonJava.Loging.i("LockScreenActivity", "onClick()");
                    startEmailSend();

                    break;
            }
        }
    };

    /**
     * Gmail 가져오기
     */
    private String getGmail() {
        Pattern emailPattern = Patterns.EMAIL_ADDRESS;
        Account[] accounts = AccountManager.get(this).getAccounts();

        String email = null;

        for (Account account : accounts) {
            if (emailPattern.matcher(account.name).matches()) {
                email = account.name;
                CommonJava.Loging.i("LockScreenActivity", "email : " + email);
            }
        }
        return email;
    }

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

            ActivityCompat.finishAffinity(this);
            System.exit(0);
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

}
