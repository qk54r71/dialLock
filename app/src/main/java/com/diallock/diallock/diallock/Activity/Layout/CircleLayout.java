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
import android.provider.MediaStore;
import android.util.AttributeSet;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.diallock.diallock.diallock.Activity.Activity.SettingActivity;
import com.diallock.diallock.diallock.Activity.Common.CommonJava;
import com.diallock.diallock.diallock.R;

import java.io.IOException;
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

    private Bitmap mCenterBitmapImg;

    /**
     * 현재 입력되는 패스워드를 기억하는 변수
     */
    private String mPassword;

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

        init();

    }

    private void init() {

        bitmapImageListInit();

        String centerStrImgUrl = CommonJava.loadSharedPreferences(mContext, "imgUrl");

        if (centerStrImgUrl != null && !centerStrImgUrl.isEmpty()) {
            Uri centerImgUrl = Uri.parse(centerStrImgUrl);
            CommonJava.Loging.i(mContext.getClass().getName(), "centerStrImgUrl : " + centerStrImgUrl);
            try {
                mCenterBitmapImg = MediaStore.Images.Media.getBitmap(mContext.getContentResolver(), centerImgUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            CommonJava.Loging.e(mContext.getClass().getName(), "centerStrImgUrl Null ");
            //TODO: 기본 이미지 로고 넣어야 함
            mCenterBitmapImg = BitmapFactory.decodeResource(getResources(), R.drawable.circle);
        }

        mPassword = new String();

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
            mInnerRadius = height / 2 - (width * 270 / 1440);
        }
        if (mOuterRadius == 0) {
            mOuterRadius = height / 2 - (width * 30 / 1440);
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
            float bitmapImgY = height / 2 + centerY + parentLinearHeight / 2;
            int bitmapRadius = bitmapClickImage.getWidth() / 2;


            mBitmapImages.get(i).setxLocation(bitmapImgX);
            mBitmapImages.get(i).setyLocation(bitmapImgY);
            mBitmapImages.get(i).setImgRadius((int) (bitmapRadius * 1.25));
        }

        mPaint.setColor(Color.WHITE);
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(width / 2, height / 2, mInnerRadius, mPaint);

        /**
         * 가운에 화면에 이미지 넣기
         */

        Bitmap centerImg = getCircleBitmap(mCenterBitmapImg, mInnerRadius);
        canvas.drawBitmap(centerImg, width / 2 - centerImg.getWidth() / 2, height / 2 - centerImg.getHeight() / 2, null);


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
        mStartDial = false;
        mDialImageInfo.initDialImageInfo();

        if (isDialInner) {
            int isImageInnerIndex = isImageInner(xLocation, yLocation);
            if (isImageInnerIndex != NUM_NULL) {

                CommonJava.Loging.i("CircleLayout", "screenTouchLocationStart Event start isImageInnerIndex : " + isImageInnerIndex);
                mStartDial = true;
                //mDialImageInfo.setCurrentClickBitmapImageIndex(isImageInnerIndex);

                mPassword = isImageInnerValue(xLocation, yLocation);
                CommonJava.Loging.i("CircleLayout", "screenTouchLocationStart isImageInnerValue : " + mPassword);

                bitmapImageListShuffle();
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

        CommonJava.Loging.i("CircleLayout", "screenTouchLocationDrag xLocation : " + xLocation);
        CommonJava.Loging.i("CircleLayout", "screenTouchLocationDrag yLocation : " + yLocation);
        if (mStartDial) {
            Boolean isDialInner = isDialInner(xLocation, yLocation);

            if (isDialInner) {
                int isImageInnerIndex = isImageInner(xLocation, yLocation);

                if (isImageInnerIndex != NUM_NULL && mDragIndex != isImageInnerIndex) {

                    CommonJava.Loging.i("CircleLayout", "screenTouchLocationDrag Event start isImageInnerIndex : " + isImageInnerIndex);

                    mDialImageInfo.setCurrentClickBitmapImageIndex(isImageInnerIndex);

                    mStartDial = true;
                    mDragIndex = isImageInnerIndex;
                    //bitmapImageListShuffle();
                    invalidate();

                    Boolean vactorBl = dragIndexVactor();
                    CommonJava.Loging.i("CircleLayout", "dragIndexVactor : " + vactorBl);
                    if (!vactorBl) {

                        if (mPassword == null) {
                            mPassword = mBitmapImages.get(mDialImageInfo.getPreClickBitmapImageIndex()).getBitmapValue();
                            CommonJava.Loging.i("CircleLayout", "screenTouchLocationDrag isImageInnerValue : " + mPassword);
                        } else {
                            CommonJava.Loging.i("CircleLayout", "screenTouchLocationDrag isImageInnerValue : " + mPassword);
                            mPassword += mBitmapImages.get(mDialImageInfo.getPreClickBitmapImageIndex()).getBitmapValue();
                        }

                        bitmapImageListShuffle();
                        invalidate();
                    }


                }
            } else {
                mStartDial = false;
                mDragIndex = NUM_NULL;
                mDialImageInfo.initDialImageInfo();
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

        CommonJava.Loging.i("CircleLayout", "xLocation : " + xLocation);
        CommonJava.Loging.i("CircleLayout", "yLocation : " + yLocation);
        if (mStartDial) {

            mPassword += isImageInnerValue(xLocation, yLocation);
            CommonJava.Loging.i("CircleLayout", "screenTouchLocationEnd isImageInnerValue : " + mPassword);

            mStartDial = false;
            mDialImageInfo.initDialImageInfo();
            bitmapImageListShuffle();
            invalidate();

            String loadPassword = CommonJava.loadSharedPreferences(mContext, "password");

            CommonJava.Loging.i(mContext.getClass().getName(), "screenTouchLocationEnd loadPassword : " + loadPassword);

            if (loadPassword.equals(mPassword)) {
                Toast.makeText(mContext, "맞는 비밀번호 입니다.", Toast.LENGTH_SHORT).show();
                Intent intentSetting = new Intent(mContext, SettingActivity.class);
                mContext.startActivity(intentSetting);
                ((Activity) mContext).finish();
            } else {
                Toast.makeText(mContext, "틀린 비밀번호 입니다.", Toast.LENGTH_SHORT).show();
            }

            mPassword = null;
        }
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
        mIcon.clear();
        mIconClick.clear();
        mBitmapImages.clear();

        int randomFirstIndex = (int) (Math.random() * 9);
        int randomSecondIndex = 0;

        do {
            randomSecondIndex = (int) (Math.random() * 9);
        } while (randomFirstIndex == randomSecondIndex);

        int randomImgFirst = mArcIconIdRandom[randomFirstIndex];
        int randomImgSecond = mArcIconIdRandom[randomSecondIndex];
        int randomImgClickFirst = mArcIconClickIdRandom[randomFirstIndex];
        int randomImgClickSecond = mArcIconClickIdRandom[randomSecondIndex];

        if (mIcon.size() == 0) {
            for (int arcIconId : mArcIconIdRandom) {
                mIcon.add(BitmapFactory.decodeResource(getResources(), arcIconId));
            }

            for (int arcIconId : mArcIconClickIdRandom) {
                mIconClick.add(BitmapFactory.decodeResource(getResources(), arcIconId));
            }

            mIcon.add(BitmapFactory.decodeResource(getResources(), randomImgFirst));
            mIcon.add(BitmapFactory.decodeResource(getResources(), randomImgSecond));
            mIconClick.add(BitmapFactory.decodeResource(getResources(), randomImgClickFirst));
            mIconClick.add(BitmapFactory.decodeResource(getResources(), randomImgClickSecond));

            mResizeIcon = resizeBitmap(mIcon);
            mResizeIconClick = resizeBitmap(mIconClick);

        }

        for (int i = 0; i < mResizeIcon.size(); i++) {

            BitmapImage bitmapImage = new BitmapImage();
            bitmapImage.setNum(mResizeIcon.get(i));
            bitmapImage.setNumClick(mResizeIconClick.get(i));
            bitmapImage.setBitmapID("" + i);

            if (i < 10) {
                bitmapImage.setBitmapValue("" + i);
            } else if (i == 10) {
                bitmapImage.setBitmapValue("" + randomFirstIndex);
            } else if (i == 11) {
                bitmapImage.setBitmapValue("" + randomSecondIndex);
            }

            mBitmapImages.add(bitmapImage);
        }

    }

    /**
     * 이미지 배열 섞기
     */
    private void bitmapImageListShuffle() {

        bitmapImageListInit();
        Collections.shuffle(mBitmapImages);

    }

    /**
     * 터치 진행방향이 진행하던 방향이면
     * true
     * 반대방향이면
     * false
     *
     * @return
     */
    private Boolean dragIndexVactor() {

        int currentIndex = mDialImageInfo.getCurrentClickBitmapImageIndex();
        int preIndex = mDialImageInfo.getPreClickBitmapImageIndex();
        int prePreIndex = mDialImageInfo.getPrePreClickBitmapImageIndex();

        if (currentIndex == NUM_NULL || preIndex == NUM_NULL || prePreIndex == NUM_NULL) {
            return true;
        }

        Boolean vactorBl = (currentIndex == (preIndex + 1) % 12) && (preIndex == (prePreIndex + 1) % 12) || (prePreIndex == (preIndex + 1) % 12) && (preIndex == (currentIndex + 1) % 12);

        return vactorBl;
    }

}