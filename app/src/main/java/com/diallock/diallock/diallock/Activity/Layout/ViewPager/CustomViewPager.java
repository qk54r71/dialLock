package com.diallock.diallock.diallock.Activity.Layout.ViewPager;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.diallock.diallock.diallock.Activity.Common.CommonJava;

/**
 * Created by park on 2016-09-28.
 */
public class CustomViewPager extends ViewPager {
    private boolean enabled;
    private final String LOG_NAME = "CustomViwPager";

    public CustomViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.enabled = true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (this.enabled) {
            return super.onTouchEvent(event);
        }

        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (this.enabled) {
            return super.onInterceptTouchEvent(event);
        }

        return false;
    }

    public void setPagingEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public void draw(Canvas canvas) {/*
        Path clip = new Path();

        float width = canvas.getWidth();
        float height = canvas.getHeight();

        CommonJava.Loging.i(LOG_NAME,"draw width : "+width);
        CommonJava.Loging.i(LOG_NAME,"draw height : "+height);

        clip.addCircle(1500, 500, 500, Path.Direction.CCW);

        canvas.clipPath(clip);*/

        super.draw(canvas);
    }
}
