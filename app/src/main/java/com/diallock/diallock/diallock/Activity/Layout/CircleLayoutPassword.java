package com.diallock.diallock.diallock.Activity.Layout;

import android.content.Context;
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
import android.os.Vibrator;
import android.util.AttributeSet;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

import com.diallock.diallock.diallock.Activity.Activity.PasswordChangeActivity;
import com.diallock.diallock.diallock.Activity.Common.CommonJava;
import com.diallock.diallock.diallock.R;

import java.util.ArrayList;

/**
 * Created by park on 2016-08-09.
 */
public class CircleLayoutPassword extends View {

    private Context mContext;

    private final static int TOTAL_DEGREE = 360;
    private final static int START_DEGREE = -90;

    private Paint mPaint;
    private RectF mOvalRect = null;

    private int mItemCount = 10;
    private int mSweepAngle;

    private int mInnerRadius;
    private int mOuterRadius;

    private ArrayList<Bitmap> mIcon = new ArrayList<Bitmap>();
    private ArrayList<Bitmap> mIconClick = new ArrayList<Bitmap>();

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

    /**
     * 다이얼락의 눌린 정보를 기록 하는 클래스
     */
    DialImageInfo mDialImageInfo = new DialImageInfo();

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

    /**
     * 현재 화면의 액티비티
     */
    private PasswordChangeActivity passwordChangeActivity;
    private Bitmap mCenterBitmapImg;

    public CircleLayoutPassword(Context context) {
        this(context, null);
    }

    public CircleLayoutPassword(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleLayoutPassword(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        CommonJava.Loging.i("CircleLayoutPassword", "Context : " + context);
        CommonJava.Loging.i("CircleLayoutPassword", "AttributeSet : " + attrs);
        CommonJava.Loging.i("CircleLayoutPassword", "defStyleAttr : " + defStyleAttr);

        mContext = context;
        if (context instanceof PasswordChangeActivity) {
            passwordChangeActivity = (PasswordChangeActivity) context;
        }

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        mSweepAngle = TOTAL_DEGREE / mItemCount;
        init();
    }

    private void init() {

        bitmapImageListInit();

        mCenterBitmapImg = BitmapFactory.decodeResource(getResources(), R.drawable.dialcenter);
    }

    /**
     * 이미지 리사이즈
     *
     * @param bitmap : 기존 이미지
     * @return
     */
    private Bitmap resizeBitmap(Bitmap bitmap) {
        int bitmapWidth = (int) (bitmap.getWidth() * 0.9);
        int bitmapHeight = (int) (bitmap.getHeight() * 0.9);

        Bitmap resizeBitmap = Bitmap.createScaledBitmap(bitmap, bitmapWidth, bitmapHeight, true);

        return resizeBitmap;
    }

    /**
     * 이미지 배열 리사이즈
     *
     * @param bitmaps : 기존 이미지 리스트
     * @return
     */
    private ArrayList<Bitmap> resizeBitmap(ArrayList<Bitmap> bitmaps) {

        ArrayList<Bitmap> resizeBitmap = new ArrayList<Bitmap>();

        CommonJava.Loging.i("CircleLayoutPassword", "원래 아이콘 가로 getWidth : " + bitmaps.get(0).getWidth());
        CommonJava.Loging.i("CircleLayoutPassword", "원래 아이콘 세로 getHeight : " + bitmaps.get(0).getHeight());

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

        CommonJava.Loging.i("CircleLayoutPassword", "Display parentLinearWidth : " + parentLinearWidth);
        CommonJava.Loging.i("CircleLayoutPassword", "Display parentLinearHeight : " + parentLinearHeight);

        if (mInnerRadius == 0) {
            mInnerRadius = height / 2 - (width * 270 / 1440);
        }
        if (mOuterRadius == 0) {
            mOuterRadius = height / 2 - (width * 30 / 1440);
        }

        CommonJava.Loging.i("CircleLayoutPassword", "현재 화면 가로 getWidth : " + getWidth());
        CommonJava.Loging.i("CircleLayoutPassword", "현재 화면 세로 getHeight: " + getHeight());

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

            /**
             * 현재 눌려진 위치 찾아서 클릭 이미지로 변경
             */
            Bitmap bitmapClickImage = null;
            if (mDialImageInfo.getCurrentClickBitmapImageIndex() == i) {
                bitmapClickImage = mBitmapImages.get(i).getNumClick();
            } else if (mDialImageInfo.getPreClickBitmapImageIndex() == i) {
                bitmapClickImage = mBitmapImages.get(i).getNumClick();
            } else if (mDialImageInfo.getPrePreClickBitmapImageIndex() == i) {
                bitmapClickImage = mBitmapImages.get(i).getNumClick();
            } else {
                bitmapClickImage = mBitmapImages.get(i).getNum();
            }

            canvas.drawBitmap(bitmapClickImage, width / 2 + centerX - bitmapClickImage.getWidth() / 2, height / 2 + centerY - bitmapClickImage.getHeight() / 2, null);

            float bitmapImgX = width / 2 + centerX;
            float bitmapImgY = height / 2 + centerY + parentLinearHeight / 4;
            int bitmapRadius = bitmapClickImage.getWidth();


            mBitmapImages.get(i).setxLocation(bitmapImgX);
            mBitmapImages.get(i).setyLocation(bitmapImgY);
            mBitmapImages.get(i).setImgRadius(bitmapRadius);
        }

        mPaint.setColor(Color.WHITE);
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(width / 2, height / 2, mInnerRadius, mPaint);

        /**
         * 가운에 화면에 이미지 넣기
         */
        Bitmap centerImg = getCircleBitmap(mCenterBitmapImg, mInnerRadius / 2);
        canvas.drawBitmap(centerImg, width / 2 - centerImg.getWidth() / 2, height / 2 - centerImg.getHeight() / 2, null);


        float bitmapImgX = width / 2;
        float bitmapImgY = height / 2 + parentLinearHeight / 4;
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
     * 비트맵 이미지 리사이징 하기
     * 비트맵 이미지 원으로 만들기
     *
     * @param bitmap
     * @return
     */
    private Bitmap getCircleBitmap(Bitmap bitmap, int innerRadius) {
        Bitmap reSize = Bitmap.createScaledBitmap(bitmap, 2 * innerRadius, 2 * innerRadius, true);
        Bitmap output = Bitmap.createBitmap(reSize.getWidth(), reSize.getHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;

        final Paint paint = new Paint();

        final Rect rect = new Rect(0, 0, reSize.getWidth(), reSize.getHeight());

        paint.setAntiAlias(true);

        canvas.drawARGB(0, 0, 0, 0);

        paint.setColor(color);

        int size = (reSize.getWidth() / 2);

        canvas.drawCircle(size, size, size, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

        canvas.drawBitmap(reSize, rect, rect, paint);

        return output;

    }


    /**
     * 최초 버튼 눌린 좌표값
     *
     * @param xLocation : x 좌표
     * @param yLocation : y 좌표
     */
    public void screenTouchLocationStart(float xLocation, float yLocation) {

        CommonJava.Loging.i("CircleLayoutPassword", "xLocation : " + xLocation);
        CommonJava.Loging.i("CircleLayoutPassword", "yLocation : " + yLocation);

        int isImageInnerIndex = isImageInner(xLocation, yLocation);
        if (isImageInnerIndex != NUM_NULL) {

            CommonJava.Loging.i("CircleLayoutPassword", "screenTouchLocationStart Event start isImageInnerIndex : " + isImageInnerIndex);

            String btnValue = isImageInnerValue(xLocation, yLocation);

            passwordChangeActivity.onBtnClick(btnValue);
            mDialImageInfo.setCurrentClickBitmapImageIndex(isImageInnerIndex);
            //bitmapImageListShuffle();
            invalidate();

            isVibrator();

        }

    }

    /**
     * 진행중 좌표 눌린값
     *
     * @param xLocation : x 좌표
     * @param yLocation : y 좌표
     */
    public void screenTouchLocationDrag(float xLocation, float yLocation) {

        CommonJava.Loging.i("CircleLayoutPassword", "screenTouchLocationDrag xLocation : " + xLocation);
        CommonJava.Loging.i("CircleLayoutPassword", "screenTouchLocationDrag yLocation : " + yLocation);
        if (mStartDial) {
            Boolean isDialInner = isDialInner(xLocation, yLocation);

            if (isDialInner) {
                int isImageInnerIndex = isImageInner(xLocation, yLocation);

                if (isImageInnerIndex != NUM_NULL && mDragIndex != isImageInnerIndex) {

                    CommonJava.Loging.i("CircleLayoutPassword", "screenTouchLocationDrag Event start isImageInnerIndex : " + isImageInnerIndex);

                    mDialImageInfo.setCurrentClickBitmapImageIndex(isImageInnerIndex);

                    mStartDial = true;
                    mDragIndex = isImageInnerIndex;
                    //bitmapImageListShuffle();
                    invalidate();
                }
            } else {
                mStartDial = false;
                mDragIndex = NUM_NULL;
            }
        }
    }

    /**
     * 마지막 눌린 좌표값
     *
     * @param xLocation : x 좌표
     * @param yLocation : y 좌표
     */
    public void screenTouchLocationEnd(float xLocation, float yLocation) {

        CommonJava.Loging.i("CircleLayoutPassword", "xLocation : " + xLocation);
        CommonJava.Loging.i("CircleLayoutPassword", "yLocation : " + yLocation);
    }

    /**
     * 화면에서 손이 떨어질 때
     *
     * @param xLocation
     * @param yLocation
     */
    public void scrennTouchLocationUp(float xLocation, float yLocation) {
        CommonJava.Loging.i("CircleLayoutPassword", "xLocation : " + xLocation);
        CommonJava.Loging.i("CircleLayoutPassword", "yLocation : " + yLocation);
        mDialImageInfo.setCurrentClickBitmapImageIndex(NUM_NULL);
        mDialImageInfo.setPreClickBitmapImageIndex(NUM_NULL);
        mDialImageInfo.setPrePreClickBitmapImageIndex(NUM_NULL);
        invalidate();
    }

    /**
     * 현재 화면에 적용될 이미지 정보를 갖는 클래스
     */
    private class BitmapImage {
        private Bitmap num;
        private Bitmap numClick;
        private String bitmapValue;
        private float xLocation;
        private float yLocation;
        private int imgRadius;
        private String bitmapID;

        public Bitmap getNum() {
            return num;
        }

        public void setNum(Bitmap num) {
            this.num = num;
        }

        public Bitmap getNumClick() {
            return numClick;
        }

        public void setNumClick(Bitmap numClick) {
            this.numClick = numClick;
        }

        public String getBitmapID() {
            return bitmapID;
        }

        public void setBitmapID(String bitmapID) {
            this.bitmapID = bitmapID;
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
     * 현재 눌려진 이미지의 정보를 갖음
     * 총 3개의 눌려진 정보를 갖음
     */
    private class DialImageInfo {
        private int currentClickBitmapImageIndex = NUM_NULL;
        private int preClickBitmapImageIndex = NUM_NULL;
        private int prePreClickBitmapImageIndex = NUM_NULL;

        public void initDialImageInfo() {
            this.currentClickBitmapImageIndex = NUM_NULL;
            this.preClickBitmapImageIndex = NUM_NULL;
            this.prePreClickBitmapImageIndex = NUM_NULL;
        }

        public int getCurrentClickBitmapImageIndex() {

            return currentClickBitmapImageIndex;
        }

        public void setCurrentClickBitmapImageIndex(int currentClickBitmapImageIndex) {

            if (this.currentClickBitmapImageIndex != NUM_NULL) {

                if (this.preClickBitmapImageIndex != NUM_NULL) {
                    this.prePreClickBitmapImageIndex = this.preClickBitmapImageIndex;
                }

                this.preClickBitmapImageIndex = this.currentClickBitmapImageIndex;
            }

            this.currentClickBitmapImageIndex = currentClickBitmapImageIndex;

        }

        public int getPreClickBitmapImageIndex() {
            return preClickBitmapImageIndex;
        }

        public void setPreClickBitmapImageIndex(int preClickBitmapImageIndex) {
            this.preClickBitmapImageIndex = preClickBitmapImageIndex;
        }

        public int getPrePreClickBitmapImageIndex() {
            return prePreClickBitmapImageIndex;
        }

        public void setPrePreClickBitmapImageIndex(int prePreClickBitmapImageIndex) {
            this.prePreClickBitmapImageIndex = prePreClickBitmapImageIndex;
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

        CommonJava.Loging.i("CircleLayoutPassword", "isDialInner xTouch :" + xTouch);
        CommonJava.Loging.i("CircleLayoutPassword", "isDialInner yTouch :" + yTouch);

        Boolean isBigDialInner = isInnerLocation(
                mBigArcLocation.getxLocation(), mBigArcLocation.getyLocation(),
                xTouch, yTouch,
                mBigArcLocation.getImgRadius()
        );

        CommonJava.Loging.i("CircleLayoutPassword", "isDialInner isBigDialInner :" + isBigDialInner);

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
    private int isImageInner(float xTouch, float yTouch) {

        for (int i = 0; i < mBitmapImages.size(); i++) {
            Boolean isClick = isInnerLocation(
                    mBitmapImages.get(i).getxLocation(), mBitmapImages.get(i).getyLocation(),
                    xTouch, yTouch,
                    mBitmapImages.get(i).getImgRadius()
            );
            if (isClick) {
                return i;
            }
        }
        return NUM_NULL;
    }

    /**
     * 터치된 이미지의 값 리턴
     *
     * @param xTouch
     * @param yTouch
     * @return
     */
    private String isImageInnerValue(float xTouch, float yTouch) {
        for (int i = 0; i < mBitmapImages.size(); i++) {
            Boolean isClick = isInnerLocation(
                    mBitmapImages.get(i).getxLocation(), mBitmapImages.get(i).getyLocation(),
                    xTouch, yTouch,
                    mBitmapImages.get(i).getImgRadius()
            );
            if (isClick) {
                return mBitmapImages.get(i).getBitmapValue();
            }
        }
        return null;
    }


    /**
     * 최초 이미지 배열 설정
     */
    private void bitmapImageListInit() {

        if (mIcon.size() == 0) {
            for (int arcIconId : mArcIconIdRandom) {
                mIcon.add(BitmapFactory.decodeResource(getResources(), arcIconId));
            }

            for (int arcIconId : mArcIconClickIdRandom) {
                mIconClick.add(BitmapFactory.decodeResource(getResources(), arcIconId));
            }

            mResizeIcon = resizeBitmap(mIcon);
            mResizeIconClick = resizeBitmap(mIconClick);

        }

        for (int i = 0; i < mResizeIcon.size(); i++) {

            BitmapImage bitmapImage = new BitmapImage();
            bitmapImage.setNum(mResizeIcon.get(i));
            bitmapImage.setNumClick(mResizeIconClick.get(i));
            bitmapImage.setBitmapID("" + i);
            bitmapImage.setBitmapValue("" + i);

            mBitmapImages.add(bitmapImage);
        }

    }

    /**
     * 진동을 주는 함수
     */
    private void isVibrator() {
        Vibrator vibrator = (Vibrator) mContext.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(100);
    }

}