package com.diallock.diallock.diallock.Activity.Activity;

import android.gesture.Gesture;
import android.gesture.GestureOverlayView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;

import com.diallock.diallock.diallock.Activity.Common.CommonJava;
import com.diallock.diallock.diallock.R;

public class LockScreenActivity extends AppCompatActivity {

    private GestureDetector mGestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock_screen);

        CommonJava.Loging.i("LockScreenActivity", "onCreate()");
        mGestureDetector = new GestureDetector(getBaseContext(), new GestureDetector.OnGestureListener() {
            @Override
            public boolean onDown(MotionEvent motionEvent) {
                CommonJava.Loging.i("LockScreenActivity", "onDown MotionEvent : " + motionEvent);

                return false;
            }

            @Override
            public void onShowPress(MotionEvent motionEvent) {
                CommonJava.Loging.i("LockScreenActivity", "onShowPress MotionEvent : " + motionEvent);

            }

            @Override
            public boolean onSingleTapUp(MotionEvent motionEvent) {
                CommonJava.Loging.i("LockScreenActivity", "onSingleTapUp MotionEvent : " + motionEvent);
                return false;
            }

            @Override
            public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
                CommonJava.Loging.i("LockScreenActivity", "onScroll MotionEvent : " + motionEvent);
                return false;
            }

            @Override
            public void onLongPress(MotionEvent motionEvent) {
                CommonJava.Loging.i("LockScreenActivity", "onLongPress MotionEvent : " + motionEvent);

            }

            @Override
            public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
                CommonJava.Loging.i("LockScreenActivity", "onFling MotionEvent : " + motionEvent);
                return false;
            }

        });


    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mGestureDetector.onTouchEvent(event);
    }
}
