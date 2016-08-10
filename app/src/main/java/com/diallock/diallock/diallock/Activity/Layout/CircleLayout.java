package com.diallock.diallock.diallock.Activity.Layout;

import android.content.Context;
import android.gesture.Gesture;
import android.gesture.GestureOverlayView;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.v4.view.GestureDetectorCompat;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.diallock.diallock.diallock.Activity.Common.CommonJava;
import com.diallock.diallock.diallock.R;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by park on 2016-08-09.
 */
public class CircleLayout extends View {

    private final static int TOTAL_DEGREE = 360;
    private final static int START_DEGREE = -90;

    private Paint mPaint;
    private RectF mOvalRect = null;

    private int mItemCount = 12;
    private int mSweepAngle;

    private int mInnerRadius;
    private int mOuterRadius;

    private ArrayList<Bitmap> mIcon = new ArrayList<Bitmap>();
    private int mRandomImgFrist;
    private int mRandomImgSecond;
    private int[] mArcIconIdRandom = {
            R.drawable.num_0, R.drawable.num_1, R.drawable.num_2,
            R.drawable.num_3, R.drawable.num_4, R.drawable.num_5,
            R.drawable.num_6, R.drawable.num_7, R.drawable.num_8,
            R.drawable.num_9};
    private int mBgColors = Color.rgb(55, 96, 146);
    private ArrayList<Bitmap> mResizeIcon = new ArrayList<Bitmap>();

    private GestureDetector mGestureDetector;

    public CircleLayout(Context context) {
        this(context, null);
    }

    public CircleLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        //mPaint.setStrokeWidth(2);

        mSweepAngle = TOTAL_DEGREE / mItemCount;

        //mInnerRadius = 250;
        //mOuterRadius = 400;

        int randomFirstIndex = (int) (Math.random() * 9);
        int randomSecondIndex = 0;

        do {
            randomSecondIndex = (int) (Math.random() * 9);
        } while (randomFirstIndex == randomSecondIndex);

        mRandomImgFrist = mArcIconIdRandom[randomFirstIndex];
        mRandomImgSecond = mArcIconIdRandom[randomSecondIndex];

        for (int arcIconId : mArcIconIdRandom) {
            mIcon.add(BitmapFactory.decodeResource(getResources(), arcIconId));
        }
        mIcon.add(BitmapFactory.decodeResource(getResources(), mRandomImgFrist));
        mIcon.add(BitmapFactory.decodeResource(getResources(), mRandomImgSecond));

        Collections.shuffle(mIcon);

        mResizeIcon = resizeBitmap(mIcon);

        mGestureDetector = new GestureDetector(getContext(), new GestureDetector.OnGestureListener() {
            @Override
            public boolean onDown(MotionEvent motionEvent) {
                CommonJava.Loging.i("CircleLayout", "onDown MotionEvent : " + motionEvent);

                return false;
            }

            @Override
            public void onShowPress(MotionEvent motionEvent) {
                CommonJava.Loging.i("CircleLayout", "onShowPress MotionEvent : " + motionEvent);

            }

            @Override
            public boolean onSingleTapUp(MotionEvent motionEvent) {
                CommonJava.Loging.i("CircleLayout", "onSingleTapUp MotionEvent : " + motionEvent);
                return false;
            }

            @Override
            public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
                CommonJava.Loging.i("CircleLayout", "onScroll MotionEvent : " + motionEvent);
                return false;
            }

            @Override
            public void onLongPress(MotionEvent motionEvent) {
                CommonJava.Loging.i("CircleLayout", "onLongPress MotionEvent : " + motionEvent);

            }

            @Override
            public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
                CommonJava.Loging.i("CircleLayout", "onFling MotionEvent : " + motionEvent);
                return false;
            }

        });
    }

    /**
     * 이미지 리사이즈
     *
     * @param bitmaps : 기존 이미지 리스트
     * @return
     */

    private ArrayList<Bitmap> resizeBitmap(ArrayList<Bitmap> bitmaps) {

        ArrayList<Bitmap> resizeBitmap = new ArrayList<Bitmap>();

        CommonJava.Loging.i("CircleLayout", "원래 아이콘 가로 getWidth : " + bitmaps.get(0).getWidth());
        CommonJava.Loging.i("CircleLayout", "원래 아이콘 세로 getHeight : " + bitmaps.get(0).getHeight());

        for (Bitmap bitmap : bitmaps) {

            int bitmapWidth = (int) (bitmap.getWidth() * 0.75);
            int bitmapHeight = (int) (bitmap.getHeight() * 0.75);

            resizeBitmap.add(Bitmap.createScaledBitmap(bitmap, bitmapWidth, bitmapHeight, true));
        }

        return resizeBitmap;
    }

    @Override
    protected void onDraw(Canvas canvas) {

        int width = getWidth();
        int height = getHeight();

        mInnerRadius = height / 2 - 250;
        mOuterRadius = height / 2 - 30;

        CommonJava.Loging.i("CircleLayout", "현재 화면 가로 getWidth : " + getWidth());
        CommonJava.Loging.i("CircleLayout", "현재 화면 세로 getHeight: " + getHeight());

        if (mOvalRect == null) {
            mOvalRect = new RectF(width / 2 - mOuterRadius, height / 2 - mOuterRadius, width / 2 + mOuterRadius, height / 2 + mOuterRadius);
        }

        mPaint.setColor(mBgColors);
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawArc(mOvalRect, 0, 360, true, mPaint);

        for (int i = 0; i < mItemCount; i++) {
            int startAngle = START_DEGREE + i * mSweepAngle;

            int centerX = (int) ((mOuterRadius + mInnerRadius) / 2 * Math.cos(Math.toRadians(startAngle + mSweepAngle / 2)));
            int centerY = (int) ((mOuterRadius + mInnerRadius) / 2 * Math.sin(Math.toRadians(startAngle + mSweepAngle / 2)));
            canvas.drawBitmap(mResizeIcon.get(i), width / 2 + centerX - mResizeIcon.get(i).getWidth() / 2, height / 2 + centerY - mResizeIcon.get(i).getHeight() / 2, null);

        }

        mPaint.setColor(Color.WHITE);
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(width / 2, height / 2, mInnerRadius, mPaint);
        //canvas.drawBitmap(mCenterIcon, width / 2 - mCenterIcon.getWidth() / 2, height / 2 - mCenterIcon.getHeight() / 2, null);

        super.onDraw(canvas);
    }
}