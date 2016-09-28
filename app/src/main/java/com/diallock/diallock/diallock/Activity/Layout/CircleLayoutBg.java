package com.diallock.diallock.diallock.Activity.Layout;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.util.AttributeSet;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

import com.diallock.diallock.diallock.Activity.Activity.LockScreenViewActivity;
import com.diallock.diallock.diallock.Activity.Common.CommonJava;
import com.diallock.diallock.diallock.Activity.Common.LockScreenManager;
import com.diallock.diallock.diallock.Activity.taskAction.ScreenService;
import com.diallock.diallock.diallock.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by park on 2016-08-09.
 */
public class CircleLayoutBg extends View {

    private Context mContext;

    private final static int TOTAL_DEGREE = 360;
    private final static int START_DEGREE = -90;

    private Paint mPaint;
    private RectF mOvalRect = null;

    private int mItemCount = 12;
    private int mSweepAngle;

    private int mInnerRadius;
    private int mOuterRadius;

    private int mBgColors = Color.rgb(55, 96, 146);

    /**
     * 비어있다는 뜻의 상수
     */
    private final int NUM_NULL = 99;

    /**
     * 다이얼 락 잠금 해제 스타트를 구분하기 위한 변수
     */
    private Boolean mStartDial;

    /**
     * 현재 드래그 하고 있는 숫자의 인덱스
     */
    private int mDragIndex = NUM_NULL;

    private Bitmap mCenterBitmapImg;

    /**
     * 현재 입력되는 패스워드를 기억하는 변수
     */
    private String mPassword;

    /**
     * 비밀번호 입력 틀렸을 시 사용되는 변수
     */
    private Handler handlerError;
    private Boolean errorDrowBl;
    private LockScreenManager mLockScreenManager;


    public CircleLayoutBg(Context context) {
        this(context, null);
    }

    public CircleLayoutBg(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleLayoutBg(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        CommonJava.Loging.i("CircleLayout", "Context : " + context);
        CommonJava.Loging.i("CircleLayout", "AttributeSet : " + attrs);
        CommonJava.Loging.i("CircleLayout", "defStyleAttr : " + defStyleAttr);

        mContext = context;

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        mSweepAngle = TOTAL_DEGREE / mItemCount;

        init();

    }

    private void init() {
    }

    /**
     * 화면에 다이얼 모양을 그려줌
     *
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {

        int width = getWidth();
        int height = getHeight();

        WindowManager windowManager = (WindowManager) mContext.getSystemService(mContext.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);

        Integer parentLinearWidth = point.x;
        Integer parentLinearHeight = point.y;

        if (mInnerRadius == 0) {
            mInnerRadius = height / 2 - (width * 270 / 1440);
        }
        if (mOuterRadius == 0) {
            mOuterRadius = height / 2 - (width * 30 / 1440);
        }

        if (mOvalRect == null) {
            mOvalRect = new RectF(width / 2 - mOuterRadius, height / 2 - mOuterRadius, width / 2 + mOuterRadius, height / 2 + mOuterRadius);
        }

        mPaint.setColor(mBgColors);
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawArc(mOvalRect, 0, 360, true, mPaint);


        mPaint.setColor(Color.WHITE);
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(width / 2, height / 2, mInnerRadius, mPaint);

        super.onDraw(canvas);
    }


}