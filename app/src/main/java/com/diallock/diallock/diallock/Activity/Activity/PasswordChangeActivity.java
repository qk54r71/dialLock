package com.diallock.diallock.diallock.Activity.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.diallock.diallock.diallock.Activity.Common.CommonJava;
import com.diallock.diallock.diallock.Activity.Layout.CircleLayout;
import com.diallock.diallock.diallock.Activity.Layout.CircleLayoutPassword;
import com.diallock.diallock.diallock.R;

public class PasswordChangeActivity extends AppCompatActivity {

    private CircleLayoutPassword circleLayoutPassword;

    private String passNumber;
    private Button pass_btn_cancle;
    private Button pass_btn_ok;
    private TextView pass_txt_lock;
    private Boolean passProgress;
    private String strSwitch;
    private Boolean backFlag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_change);

        setFindView();
        init();
        setOnClick();

    }

    private void setFindView() {
        circleLayoutPassword = (CircleLayoutPassword) findViewById(R.id.circle_screen_password);
        pass_btn_cancle = (Button) findViewById(R.id.pass_btn_cancle);
        pass_btn_ok = (Button) findViewById(R.id.pass_btn_ok);
        pass_txt_lock = (TextView) findViewById(R.id.pass_txt_lock);
    }

    private void init() {
        passNumber = null;
        passProgress = false;
        backFlag = false;
        strSwitch = getIntent().getStringExtra("strSwitch");

        switch (strSwitch) {
            case "veryfirst":
                pass_btn_cancle.setEnabled(false);
                break;
            case "first":
                strSwitch = "first";
                break;
            case "second":
                strSwitch = "second";
                break;
        }
    }

    private void setOnClick() {
        pass_btn_cancle.setOnClickListener(onClickListener);
        pass_btn_ok.setOnClickListener(onClickListener);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.pass_btn_cancle:

                    if (passProgress) {

                        passProgress = false;

                        passNumber = null;
                        pass_txt_lock.setText(passNumber);

                        pass_btn_cancle.setText("취소");
                    } else {
                        finish();
                    }

                    break;
                case R.id.pass_btn_ok:

                    if (passProgress && isPasswordLangth()) {

                        switch (strSwitch) {
                            case "first":
                                Intent intentSettingFirst = new Intent(PasswordChangeActivity.this, PasswordChangeActivity.class);
                                intentSettingFirst.putExtra("strSwitch", "second");

                                String strPasswordFirst = (String) pass_txt_lock.getText();
                                intentSettingFirst.putExtra("password", strPasswordFirst);

                                startActivity(intentSettingFirst);

                                Toast.makeText(getApplicationContext(), "한번 더 패스워드를 입력하세요.", Toast.LENGTH_SHORT).show();

                                finish();
                                break;
                            case "second":
                                if (isPasswordSame()) {
                                    String strPasswordSecond = (String) pass_txt_lock.getText();
                                    CommonJava.saveSharedPreferences(PasswordChangeActivity.this, "password", strPasswordSecond);

                                    String email = CommonJava.getGmail(PasswordChangeActivity.this);

                                    CommonJava.saveSharedPreferences(PasswordChangeActivity.this, "email", email);

                                    Intent intentSettingSecond = new Intent(PasswordChangeActivity.this, SettingActivity.class);
                                    startActivity(intentSettingSecond);
                                    finish();
                                } else {
                                    Toast.makeText(getApplicationContext(), "전 단계의 패스워드와 맞지 않습니다.", Toast.LENGTH_SHORT).show();
                                }
                                break;
                        }
                    }

                    break;
            }
        }
    };

    /**
     * 패스워드 길이 조절
     *
     * @return
     */
    private Boolean isPasswordLangth() {

        String strPassword = (String) pass_txt_lock.getText();
        int passwordLangth = strPassword.length();

        int minimumPass = 2;
        int maximumPass = 6;

        if (passwordLangth >= minimumPass && passwordLangth <= maximumPass) {
            return true;
        } else {
            Toast.makeText(getApplicationContext(), "비밀번호는 2~4자리로 만들어주세요.", Toast.LENGTH_SHORT).show();
            return false;
        }

    }

    /**
     * 전 단계의 패스워드와 현재 패스워드가 맞는지 확인하는 함수
     */
    private Boolean isPasswordSame() {

        String strPasswordFirst = getIntent().getStringExtra("password");
        String strPasswordSecond = (String) pass_txt_lock.getText();

        if (strPasswordFirst.equals(strPasswordSecond)) {
            return true;
        } else {
            return false;
        }

    }

    /**
     * 눌린 값을 받는 함수
     *
     * @param btnValue : 현재 눌린 값
     */
    public void onBtnClick(String btnValue) {
        CommonJava.Loging.i("PasswordChange", "btnValue : " + btnValue);
        passProgress = true;

        if (passNumber != null) {
            passNumber += btnValue;
        } else {
            passNumber = btnValue;
        }
        pass_txt_lock.setText(passNumber);

        pass_btn_cancle.setText("다시하기");

    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        CommonJava.Loging.i("PasswordChange", "event : " + event);
        float xLocation = event.getX(0);
        float yLocation = event.getY(0);

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                circleLayoutPassword.screenTouchLocationStart(xLocation, yLocation);

                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:

                circleLayoutPassword.scrennTouchLocationUp(xLocation, yLocation);

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

            Toast.makeText(PasswordChangeActivity.this, "한번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show();
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
