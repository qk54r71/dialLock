package com.diallock.diallock.diallock.Activity.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;

import com.diallock.diallock.diallock.Activity.Common.CommonJava;
import com.diallock.diallock.diallock.R;

public class SettingActivity extends AppCompatActivity {

    private LinearLayout linear_lock;
    private LinearLayout linear_unlock;
    private LinearLayout linear_pass_change;
    private LinearLayout linear_img_change;
    private LinearLayout linear_email_change;
    private LinearLayout linear_ad;

    private Boolean lockCheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        setFindView();
        init();
        setOnClick();
    }

    /**
     * 레이아웃과 연결
     */
    private void setFindView() {
        linear_lock = (LinearLayout) findViewById(R.id.linear_lock);
        linear_unlock = (LinearLayout) findViewById(R.id.linear_unlock);
        linear_pass_change = (LinearLayout) findViewById(R.id.linear_pass_change);
        linear_img_change = (LinearLayout) findViewById(R.id.linear_img_change);
        linear_email_change = (LinearLayout) findViewById(R.id.linear_email_change);
        linear_ad = (LinearLayout) findViewById(R.id.linear_ad);
    }

    /**
     * 상태값 초기화
     */
    private void init() {

        String strLockCheck = CommonJava.loadSharedPreferences(SettingActivity.this, "lockCheck");

        switch (strLockCheck) {
            case "true":
                lockCheck = true;
                linear_lock.setBackgroundResource(R.drawable.btn_click);
                linear_unlock.setBackgroundResource(R.drawable.btn_bg);
                break;
            case "false":
                lockCheck = false;
                linear_lock.setBackgroundResource(R.drawable.btn_bg);
                linear_unlock.setBackgroundResource(R.drawable.btn_click);
                break;
            default:
                lockCheck = false;
                linear_lock.setBackgroundResource(R.drawable.btn_bg);
                linear_unlock.setBackgroundResource(R.drawable.btn_click);
        }


    }

    /**
     * 클릭 이벤트 연결
     */
    private void setOnClick() {
        linear_lock.setOnClickListener(onClickListener);
        linear_unlock.setOnClickListener(onClickListener);
        linear_pass_change.setOnClickListener(onClickListener);
        linear_img_change.setOnClickListener(onClickListener);
        linear_email_change.setOnClickListener(onClickListener);
        linear_ad.setOnClickListener(onClickListener);
    }

    /**
     * 클릭 이벤트 설정
     */
    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            switch (view.getId()) {
                case R.id.linear_lock:
                    if (lockCheck == false) {
                        CommonJava.saveSharedPreferences(SettingActivity.this, "lockCheck", "true");
                        linear_lock.setBackgroundResource(R.drawable.btn_click);
                        linear_unlock.setBackgroundResource(R.drawable.btn_bg);

                        Intent intentLockScreen = new Intent(SettingActivity.this, LockScreenActivity.class);
                        intentLockScreen.putExtra("strSwitch", "SettingActivity");
                        startActivity(intentLockScreen);
                        lockCheck = true;
                    }

                    break;
                case R.id.linear_unlock:

                    if (lockCheck == true) {
                        CommonJava.saveSharedPreferences(SettingActivity.this, "lockCheck", "false");
                        linear_lock.setBackgroundResource(R.drawable.btn_bg);
                        linear_unlock.setBackgroundResource(R.drawable.btn_click);
                        lockCheck = false;
                    }

                    break;
                case R.id.linear_pass_change:

                    Intent intentPassChange = new Intent(SettingActivity.this, PasswordChangeActivity.class);
                    startActivity(intentPassChange);

                    break;
                case R.id.linear_img_change:
                    break;
                case R.id.linear_email_change:
                    break;
                case R.id.linear_ad:
                    break;
            }

        }
    };

}
