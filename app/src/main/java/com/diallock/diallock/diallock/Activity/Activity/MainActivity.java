package com.diallock.diallock.diallock.Activity.Activity;

import android.app.Activity;
import android.os.Bundle;

import com.diallock.diallock.diallock.R;
import com.facebook.drawee.backends.pipeline.Fresco;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Fresco.initialize(MainActivity.this);
        setContentView(R.layout.activity_main);
    }


}
