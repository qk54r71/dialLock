package com.diallock.diallock.diallock.Activity.Activity;

import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.diallock.diallock.diallock.Activity.Common.CommonJava;
import com.diallock.diallock.diallock.R;
import com.facebook.drawee.backends.pipeline.Fresco;

public class EmailChangeActivity extends AppCompatActivity {

    private TextView txt_load_email;
    private EditText edt_new_email;
    private EditText edt_re_email;
    private Button btn_email_cancle;
    private Button btn_email_change;
    private Boolean backFlag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Fresco.initialize(EmailChangeActivity.this);
        setContentView(R.layout.activity_email_change);

        setFindView();
        init();
        setOnClick();
    }

    private void setFindView() {
        txt_load_email = (TextView) findViewById(R.id.txt_load_email);
        edt_new_email = (EditText) findViewById(R.id.edt_new_email);
        edt_re_email = (EditText) findViewById(R.id.edt_re_email);
        btn_email_cancle = (Button) findViewById(R.id.btn_email_cancle);
        btn_email_change = (Button) findViewById(R.id.btn_email_change);
    }

    private void init() {

        String email = CommonJava.loadSharedPreferences(EmailChangeActivity.this, "email");
        if (email == null || email.isEmpty()) {
            email = CommonJava.getGmail(EmailChangeActivity.this);
        }
        txt_load_email.setText(email);
        backFlag = false;

    }

    private void setOnClick() {
        btn_email_cancle.setOnClickListener(onClickListener);
        btn_email_change.setOnClickListener(onClickListener);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btn_email_cancle:
                    finish();
                    break;
                case R.id.btn_email_change:
                    if (isEmailSame()) {
                        String email = String.valueOf(edt_new_email.getText());
                        CommonJava.saveSharedPreferences(EmailChangeActivity.this, "email", email);
                        finish();
                    } else {
                        new Toast(EmailChangeActivity.this).makeText(EmailChangeActivity.this, "이메일 확인이 잘못되었습니다.", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    };

    /**
     * 백업 이메일과 새 백업 이메일이 같은지 확인
     *
     * @return
     */
    private Boolean isEmailSame() {

        String strEdtNew = String.valueOf(edt_new_email.getText());
        String strEdtRe = String.valueOf(edt_re_email.getText());

        if (strEdtNew.equals(strEdtRe)) {
            return true;
        }

        return false;
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

            Toast.makeText(EmailChangeActivity.this, "한번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show();
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
