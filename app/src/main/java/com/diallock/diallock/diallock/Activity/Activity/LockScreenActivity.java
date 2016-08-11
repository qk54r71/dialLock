package com.diallock.diallock.diallock.Activity.Activity;

import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.WindowManager;

import com.diallock.diallock.diallock.Activity.Common.CommonJava;
import com.diallock.diallock.diallock.Activity.Layout.CircleLayout;
import com.diallock.diallock.diallock.R;

public class LockScreenActivity extends AppCompatActivity {

    private CircleLayout circleLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock_screen);

        setFindView();

        CommonJava.Loging.i("LockScreenActivity", "onCreate()");

    }

    private void setFindView() {
        circleLayout = (CircleLayout) findViewById(R.id.circle_screen);
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
}
