package com.diallock.diallock.diallock.Activity.Activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.diallock.diallock.diallock.Activity.Common.CommonJava;
import com.diallock.diallock.diallock.R;

import java.util.concurrent.ExecutionException;

public class SettingActivity extends AppCompatActivity {

    private LinearLayout linear_lock;
    private LinearLayout linear_unlock;
    private LinearLayout linear_pass_change;
    private LinearLayout linear_img_change;
    private LinearLayout linear_email_change;
    private LinearLayout linear_ad;

    private Boolean lockCheck;
    private Boolean backFlag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        Boolean isAuthorityCheck = isAuthorityCheck();
        if (isAuthorityCheck) {
            CommonJava.Loging.i("SettingActivity", "isAuthorityCheck : " + isAuthorityCheck);
            loadPassword();
        }

        setFindView();
        init();
        setOnClick();
    }

    private void loadPassword() {
        String password = CommonJava.loadSharedPreferences(SettingActivity.this, "password");
        CommonJava.Loging.i("SettingActivity", "password : " + password);
        if (password.isEmpty()) {

            Intent intentSetPassword = new Intent(SettingActivity.this, PasswordChangeActivity.class);
            intentSetPassword.putExtra("strSwitch", "first");
            startActivity(intentSetPassword);

        }

    }

    /**
     * 마시멜로우 이상 버전에서 사용되는 권한 체크 하기
     */
    private Boolean isAuthorityCheck() {
        /**
         * 주소록 가져오기
         */
        //Boolean get_accounts_bl = ActivityCompat.shouldShowRequestPermissionRationale(SettingActivity.this, Manifest.permission.GET_ACCOUNTS);
        //CommonJava.Loging.i("SettingActivity", "get_accounts_bl : " + get_accounts_bl);
        int checkInt = 99;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            checkInt = checkSelfPermission(Manifest.permission.GET_ACCOUNTS);
        }
        CommonJava.Loging.i("SettingActivity", "checkInt : " + checkInt);
        if (checkInt == 0) {
            return true;
        } else {
            ActivityCompat.requestPermissions(SettingActivity.this, new String[]{Manifest.permission.GET_ACCOUNTS}, 0);
            return false;
        }

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

        backFlag = false;


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

                    Intent intentEmailChange = new Intent(SettingActivity.this, EmailChangeActivity.class);
                    startActivity(intentEmailChange);

                    break;
                case R.id.linear_ad:
                    break;
            }

        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        CommonJava.Loging.i("SettingActivity", "requestCode : " + requestCode);
        CommonJava.Loging.i("SettingActivity", "permissions : " + permissions[0]);
        CommonJava.Loging.i("SettingActivity", "grantResults : " + grantResults[0]);
        if (grantResults[0] != -1) {
            CommonJava.Loging.i("SettingActivity", "퍼미션 됬다 : " + grantResults);
            loadPassword();
        } else {
            finish();
        }


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

            Toast.makeText(SettingActivity.this, "한번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show();
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
