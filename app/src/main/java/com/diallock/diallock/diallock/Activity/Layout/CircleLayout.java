package com.diallock.diallock.diallock.Activity.Layout;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

import com.diallock.diallock.diallock.Activity.Common.CommonJava;
import com.diallock.diallock.diallock.R;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by park on 2016-08-09.
 */
public class CircleLayout extends View {

    private Context mContext;

    private final static int TOTAL_DEGREE = 360;
    private final static int START_DEGREE = -90;

    private Paint mPaint;
    private RectF mOvalRect = null;

    private int mItemCount = 12;
    private int mSweepAngle;

    private int mInnerRadius;
    private int mOuterRadius;

    private ArrayList<Bitmap> mIcon = new ArrayList<Bitmap>();
    private ArrayList<Bitmap> mIconClick = new ArrayList<Bitmap>();
    private int mRandomImgFrist;
    private int mRandomImgSecond;
    private int[] mArcIconIdRandom = {
            R.drawable.num_0, R.drawable.num_1, R.drawable.num_2,
            R.drawable.num_3, R.drawable.num_4, R.drawable.num_5,
            R.drawable.num_6, R.drawable.num_7, R.drawable.num_8,
            R.drawable.num_9};
    private int[] mArcIconClickIdRandom = {
            R.drawable.num_0_click, R.drawable.num_1_click, R.drawable.num_2_click,
            R.drawable.num_3_click, R.drawable.num_4_click, R.drawable.num_5_click,
            R.drawable.num_6_click, R.drawable.num_7_click, R.drawable.num_8_click,
            R.drawable.num_9_click};
    private int mBgColors = Color.rgb(55, 96, 146);
    private ArrayList<Bitmap> mResizeIcon = new ArrayList<Bitmap>();
    private ArrayList<Bitmap> mResizeIconClick = new ArrayList<Bitmap>();

    private ArrayList<BitmapImage> mBitmapImages = new ArrayList<BitmapImage>();

    BitmapImage mBigArcLocation = new BitmapImage();
    BitmapImage mSmallArcLocation = new BitmapImage();

    private final int BIG_CIRCLE = 12;
    private final int SMALL_CIRCLE = 13;

    public CircleLayout(Context context) {
        this(context, null);
    }

    public CircleLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        CommonJava.Loging.i("CircleLayout", "Context : " + context);
        CommonJava.Loging.i("CircleLayout", "AttributeSet : " + attrs);
        CommonJava.Loging.i("CircleLayout", "defStyleAttr : " + defStyleAttr);

        mContext = context;

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        mSweepAngle = TOTAL_DEGREE / mItemCount;

        bitmapImageListInit();

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

        CommonJava.Loging.i("CircleLayout", "Display parentLinearWidth : " + parentLinearWidth);
        CommonJava.Loging.i("CircleLayout", "Display parentLinearHeight : " + parentLinearHeight);

        if (mInnerRadius == 0) {
            mInnerRadius = height / 2 - 250;
        }
        if (mOuterRadius == 0) {
            mOuterRadius = height / 2 - 30;
        }

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
            canvas.drawBitmap(mBitmapImages.get(i).getApplyImage(), width / 2 + centerX - mBitmapImages.get(i).getApplyImage().getWidth() / 2, height / 2 + centerY - mBitmapImages.get(i).getApplyImage().getHeight() / 2, null);

            float bitmapImgX = width / 2 + centerX;
            float bitmapImgY = height / 2 + centerY + parentLinearHeight / 2;
            int bitmapRadius = mBitmapImages.get(i).getApplyImage().getWidth() / 2;


            mBitmapImages.get(i).setxLocation(bitmapImgX);
            mBitmapImages.get(i).setyLocation(bitmapImgY);
            mBitmapImages.get(i).setImgRadius(bitmapRadius);
        }

        mPaint.setColor(Color.WHITE);
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(width / 2, height / 2, mInnerRadius, mPaint);
        //canvas.drawBitmap(mCenterIcon, width / 2 - mCenterIcon.getWidth() / 2, height / 2 - mCenterIcon.getHeight() / 2, null);


        float bitmapImgX = width / 2;
        float bitmapImgY = height / 2 + parentLinearHeight / 2;
        int bitmapRadius = mOuterRadius;

        mBigArcLocation.setxLocation(bitmapImgX);
        mBigArcLocation.setyLocation(bitmapImgY);
        mBigArcLocation.setImgRadius(bitmapRadius);

        bitmapRadius = mInnerRadius;

        mSmallArcLocation.setxLocation(bitmapImgX);
        mSmallArcLocation.setyLocation(bitmapImgY);
        mSmallArcLocation.setImgRadius(bitmapRadius);

        super.onDraw(canvas);
    }

    /**
     * 최초 버튼 눌린 좌표값
     *
     * @param xLocation : x 좌표
     * @param yLocation : y 좌표
     */
    public void screenTouchLocationStart(float xLocation, float yLocation) {

        CommonJava.Loging.i("CircleLayout", "xLocation : " + xLocation);
        CommonJava.Loging.i("CircleLayout", "yLocation : " + yLocation);

        Boolean isDialInner = isDialInner(xLocation, yLocation);

        if (isDialInner) {
            Boolean isImageInner = isImageInner(xLocation, yLocation);
            if (isImageInner) {
                // Collections.shuffle(mBitmapImages);
                invalidate();
            }
        }
    }

    /**
     * 진행중 좌표 눌린값
     *
     * @param xLocation : x 좌표
     * @param yLocation : y 좌표
     */
    public void screenTouchLocationDrag(float xLocation, float yLocation) {

        CommonJava.Loging.i("CircleLayout", "xLocation : " + xLocation);
        CommonJava.Loging.i("CircleLayout", "yLocation : " + yLocation);
    }

    /**
     * 마지막 눌린 좌표값
     *
     * @param xLocation : x 좌표
     * @param yLocation : y 좌표
     */
    public void screenTouchLocationEnd(float xLocation, float yLocation) {

        CommonJava.Loging.i("CircleLayout", "xLocation : " + xLocation);
        CommonJava.Loging.i("CircleLayout", "yLocation : " + yLocation);

        Boolean isDialInner = isDialInner(xLocation, yLocation);

        if (isDialInner) {
            bitmapImageListShuffle();
            invalidate();
        }
    }

    /**
     * 현재 화면에 적용될 이미지 정보를 갖는 클래스
     */
    private class BitmapImage {
        private Bitmap num;
        private Bitmap numClick;
        private Bitmap applyImage;
        private String bitmapValue;
        private float xLocation;
        private float yLocation;
        private int imgRadius;

        public Bitmap getNum() {
            return num;
        }

        public void setNum(Bitmap num) {
            this.num = num;
            this.applyImage = num;
        }

        public Bitmap getNumClick() {
            return numClick;
        }

        public void setNumClick(Bitmap numClick) {
            this.numClick = numClick;
        }

        public Bitmap getApplyImage() {
            return applyImage;
        }

        public void isClickState(Boolean clickBoolean) {
            if (clickBoolean) {
                this.applyImage = numClick;
            } else {
                this.applyImage = num;
            }
        }

        public String getBitmapValue() {
            return bitmapValue;
        }

        public void setBitmapValue(String bitmapValue) {
            this.bitmapValue = bitmapValue;
        }

        public float getyLocation() {
            return yLocation;
        }

        public void setyLocation(float yLocation) {
            this.yLocation = yLocation;
        }

        public float getxLocation() {
            return xLocation;
        }

        public void setxLocation(float xLocation) {
            this.xLocation = xLocation;
        }

        public int getImgRadius() {
            return imgRadius;
        }

        public void setImgRadius(int imgRadius) {
            this.imgRadius = imgRadius;
        }
    }


    /**
     * 현재 터치된 좌표값이 이미지(원)안에 눌린건지 판별하는 함수
     *
     * @param xLocation : 이미지의 중앙 x 값
     * @param yLocation : 이미지의 중앙 y 값
     * @param xTouch    : 터치된 x 값
     * @param yTouch    : 터치된 y 값
     * @param radius    : 이미지 원의 반지름
     * @return
     */
    private Boolean isInnerLocation(float xLocation, float yLocation, float xTouch, float yTouch, int radius) {

        if (Math.pow((xLocation - xTouch), 2) + Math.pow((yLocation - yTouch), 2) < Math.pow(radius, 2)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 다이얼 모양 안에 터치됬는지 확인
     *
     * @param xTouch : 터치된 x값
     * @param yTouch : 터치된 y값
     * @return
     */
    private Boolean isDialInner(float xTouch, float yTouch) {

        CommonJava.Loging.i("CircleLayout", "isDialInner xTouch :" + xTouch);
        CommonJava.Loging.i("CircleLayout", "isDialInner yTouch :" + yTouch);

        Boolean isBigDialInner = isInnerLocation(
                mBigArcLocation.getxLocation(), mBigArcLocation.getyLocation(),
                xTouch, yTouch,
                mBigArcLocation.getImgRadius()
        );

        CommonJava.Loging.i("CircleLayout", "isDialInner isBigDialInner :" + isBigDialInner);

        if (isBigDialInner) {
            return !isInnerLocation(
                    mSmallArcLocation.getxLocation(), mSmallArcLocation.getyLocation(),
                    xTouch, yTouch,
                    mSmallArcLocation.getImgRadius()
            );
        } else {
            return false;
        }

    }

    /**
     * 이미지 버튼 안에 터치됬는지 확인
     *
     * @param xTouch
     * @param yTouch
     * @return
     */
    private Boolean isImageInner(float xTouch, float yTouch) {

        for (int i = 0; i < mBitmapImages.size(); i++) {
            Boolean isClick = isInnerLocation(
                    mBitmapImages.get(i).getxLocation(), mBitmapImages.get(i).getyLocation(),
                    xTouch, yTouch,
                    mBitmapImages.get(i).getImgRadius()
            );
            if (isClick) {
                mBitmapImages.get(i).isClickState(true);
                return true;
            } else {
            }
        }
        return false;
    }

    /**
     * 최초 이미지 배열 설정
     */
    private void bitmapImageListInit() {


        int randomFirstIndex = (int) (Math.random() * 9);
        int randomSecondIndex = 0;

        do {
            randomSecondIndex = (int) (Math.random() * 9);
        } while (randomFirstIndex == randomSecondIndex);

        mRandomImgFrist = mArcIconIdRandom[randomFirstIndex];
        mRandomImgSecond = mArcIconIdRandom[randomSecondIndex];

        if (mIcon.size() == 0) {
            for (int arcIconId : mArcIconIdRandom) {
                mIcon.add(BitmapFactory.decodeResource(getResources(), arcIconId));
            }

            for (int arcIconId : mArcIconClickIdRandom) {
                mIconClick.add(BitmapFactory.decodeResource(getResources(), arcIconId));
            }

            mIcon.add(BitmapFactory.decodeResource(getResources(), mRandomImgFrist));
            mIcon.add(BitmapFactory.decodeResource(getResources(), mRandomImgSecond));
            mIconClick.add(BitmapFactory.decodeResource(getResources(), mRandomImgFrist));
            mIconClick.add(BitmapFactory.decodeResource(getResources(), mRandomImgSecond));

            mResizeIcon = resizeBitmap(mIcon);
            mResizeIconClick = resizeBitmap(mIconClick);

        }

        for (int i = 0; i < mResizeIcon.size(); i++) {

            BitmapImage bitmapImage = new BitmapImage();
            bitmapImage.setNum(mResizeIcon.get(i));
            bitmapImage.setNumClick(mResizeIconClick.get(i));
            bitmapImage.setBitmapValue("" + i);

            mBitmapImages.add(bitmapImage);
        }

    }

    /**
     * 이미지 배열 섞기
     */
    private void bitmapImageListShuffle() {

        int randomFirstIndex = (int) (Math.random() * 9);
        int randomSecondIndex = 0;

        do {
            randomSecondIndex = (int) (Math.random() * 9);
        } while (randomFirstIndex == randomSecondIndex);

        mRandomImgFrist = mArcIconIdRandom[randomFirstIndex];
        mRandomImgSecond = mArcIconIdRandom[randomSecondIndex];

        for (BitmapImage bitmapImage : mBitmapImages) {
            String bitmapImageValue = bitmapImage.getBitmapValue();
            if (bitmapImageValue.equals("10")) {

            }
        }

        for(int bitmapIndex = 0; bitmapIndex <mBitmapImages.size() ; bitmapIndex++  ){
            String bitmapImageValue = mBitmapImages.get(bitmapIndex).getBitmapValue();
            if (bitmapImageValue.equals("10")) {
                //mBitmapImages.get(bitmapIndex).set
            }

        }

    }

}