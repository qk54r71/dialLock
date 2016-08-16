package com.diallock.diallock.diallock.Activity.Activity;

import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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
                    break;
            }
        }
    };

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

}
