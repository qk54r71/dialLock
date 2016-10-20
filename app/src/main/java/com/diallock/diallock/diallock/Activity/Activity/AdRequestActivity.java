package com.diallock.diallock.diallock.Activity.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.diallock.diallock.diallock.R;
import com.facebook.drawee.backends.pipeline.Fresco;

public class AdRequestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(AdRequestActivity.this);
        setContentView(R.layout.activity_ad_request);
    }
}
