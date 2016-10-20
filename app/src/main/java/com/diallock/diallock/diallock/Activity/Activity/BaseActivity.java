package com.diallock.diallock.diallock.Activity.Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.diallock.diallock.diallock.Activity.Common.CommonJava;
import com.diallock.diallock.diallock.R;
import com.facebook.drawee.backends.pipeline.Fresco;


/**
 *
 */
public class BaseActivity extends AppCompatActivity {
    FrameLayout container;
    View content;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        CommonJava.Loging.i(getLocalClassName(), "onCreate()");
        super.onCreate(savedInstanceState);

        Fresco.initialize(BaseActivity.this);
        setContentView(R.layout.activity_lock_screen_view);
        container = (FrameLayout) findViewById(R.id.frame_layout);
    }


    public void setupView(int layoutId) {
        CommonJava.Loging.i(getLocalClassName(), "setupView layoutId : " + layoutId);
        content = LayoutInflater.from(this).inflate(layoutId, null);
        container.addView(content);
    }


    public void Show(String str) {

    }

    public View findView(int id) {
        return content.findViewById(id);
    }
}
