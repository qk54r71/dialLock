package com.diallock.diallock.diallock.Activity.Common;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.diallock.diallock.diallock.Activity.Adapter.ListViewAdapter;
import com.diallock.diallock.diallock.Activity.Layout.CircleLayout;
import com.diallock.diallock.diallock.Activity.taskAction.NoLockStatusListenerException;
import com.diallock.diallock.diallock.R;
import com.facebook.drawee.view.SimpleDraweeView;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by park on 2016-08-26.
 */
public class LockScreenManager {

    public static LockScreenManager mLockScreenManager;
    public static Activity mActivity;
    public static View mLockView;
    private WindowManager.LayoutParams layoutParams;
    private boolean mIsLock;
    private WeakReference<WindowManager> mWindowManagerRef;
    private CircleLayout circleLayout;
    private SimpleDraweeView btn_cancle;
    private Timer mTimer;
    private Handler mHandler = new Handler();
    private LockStatusListener lockStatusListener;
    private Date nowDate;

    private TextView txt_lock_day;
    private TextView txt_lock_time;
    private TextView txt_lock_title;
    private ListView lock_screen_listview;
    private SimpleDraweeView lock_screen_pre;
    private SimpleDraweeView lock_screen_nex;
    private Calendar mCalendar;
    private SimpleDraweeView btn_find_pass;
    private Date mNowDate;

    private final String LOG_NAME = "LockScreenManager";

    private String strSwitch;

    public static synchronized LockScreenManager getInstance(Activity activity) {
        CommonJava.Loging.i(activity.getLocalClassName(), "getInstance");
        if (mLockScreenManager == null) {
            String getStrSwitch = activity.getIntent().getStringExtra("strSwitch");
            mLockScreenManager = new LockScreenManager(activity, getStrSwitch);
        }
        return mLockScreenManager;
    }

    public LockScreenManager(Activity activity, String strSwitch) {
        this.mActivity = activity;
        this.strSwitch = strSwitch;
        initLock();
    }

    private void setFindView() {
    }

    public void updateActivity(Activity ac) {
        this.mActivity = ac;
    }

    private void initLock() {
        mIsLock = false;
        mWindowManagerRef = new WeakReference<WindowManager>(mActivity.getWindowManager());
        layoutParams = new WindowManager.LayoutParams();
        //layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
        //layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        if (strSwitch.equals("ScreenReceiver")) {
            layoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ERROR;
        }
        CommonJava.Loging.i(LOG_NAME, "initLock layoutParams.height : " + layoutParams.height);
        Point point = new Point();
        getWindowManager().getDefaultDisplay().getSize(point);
        CommonJava.Loging.i(LOG_NAME, "initLock point.y : " + point.y);
        int disHeight = point.y;
        layoutParams.height = point.y - 100 * (disHeight / 2560);
        layoutParams.verticalMargin = 200 * (disHeight / 2560);
        layoutParams.flags = 1280;


        MainTimerTask timerTask = new MainTimerTask();

        mTimer = new Timer();

        mTimer.schedule(timerTask, 0, 1000);

        nowDate = CommonJava.getNowDate();
    }


    class MainTimerTask extends TimerTask {

        public void run() {

            mHandler.post(mUpdateTimeTask);

        }

    }

    private Runnable mUpdateTimeTask = new Runnable() {

        public void run() {
            Date runDate = new Date();
            CommonJava.Loging.i(LOG_NAME, "mUpdateTimeTask() run()");
            String strTxtLockTime =
                    CommonJava.getAmPm(runDate) + " " + CommonJava.getHour(runDate) + "시 " + CommonJava.getMinute(runDate) + "분";
            CommonJava.Loging.i(LOG_NAME, "mUpdateTimeTask() strTxtLockTime : " + strTxtLockTime);
            txt_lock_time.setText(strTxtLockTime);
        }

    };

    public void timeCancle() {
        mTimer.cancel();
    }

    public void timeStart() {
        if (mTimer != null) {
            MainTimerTask timerTask = new MainTimerTask();
            mTimer = new Timer();
            mTimer.schedule(timerTask, 0, 1000);
        }
    }

    public void setLockScreen(View v) {
        mLockView = v;
        setLockBg();
        setFIndView();
        init();
        setOnTouch();
        setOnClick();
        setListView();

    }

    private void setFIndView() {

        btn_find_pass = (SimpleDraweeView) mLockView.findViewById(R.id.btn_find_pass);
        txt_lock_day = (TextView) mLockView.findViewById(R.id.txt_lock_day);
        txt_lock_time = (TextView) mLockView.findViewById(R.id.txt_lock_time);
        lock_screen_pre = (SimpleDraweeView) mLockView.findViewById(R.id.lock_screen_pre);
        lock_screen_nex = (SimpleDraweeView) mLockView.findViewById(R.id.lock_screen_nex);
        lock_screen_listview = (ListView) mLockView.findViewById(R.id.lock_screen_listview);
        txt_lock_title = (TextView) mLockView.findViewById(R.id.txt_lock_title);
    }

    private void init() {

        mNowDate = CommonJava.getNowDate();
        mCalendar = Calendar.getInstance();

        txt_lock_title.setText("다이얼락");

    }

    private void setOnClick() {
        btn_find_pass.setOnLongClickListener(onLongClickListener);
        lock_screen_pre.setOnClickListener(onClickListener);
        lock_screen_nex.setOnClickListener(onClickListener);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.lock_screen_pre:

                    mCalendar.add(Calendar.DAY_OF_MONTH, -1);
                    mNowDate = mCalendar.getTime();

                    setTextDay(mNowDate);

                    changeListView(mNowDate);
                    break;
                case R.id.lock_screen_nex:

                    mCalendar.add(Calendar.DAY_OF_MONTH, +1);
                    mNowDate = mCalendar.getTime();

                    setTextDay(mNowDate);

                    changeListView(mNowDate);
                    break;
            }
        }
    };

    private View.OnLongClickListener onLongClickListener = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View view) {
            switch (view.getId()) {
                case R.id.btn_find_pass:
                    CommonJava.Loging.i(mActivity.getLocalClassName(), "onClick()");

                    startEmailSend();
                    startTxtToast("플레이스토어에 등록된 이메일로 패스워드가 발송됩니다.");
                    break;
            }
            return false;
        }
    };

    private void setOnTouch() {
        mLockView.setOnTouchListener(onTouchListener);
    }

    private View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {

            CommonJava.Loging.i(LOG_NAME, "onTouch : " + event);
            float xLocation = event.getX(0);
            float yLocation = event.getY(0);

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    ((CircleLayout) mLockView.findViewById(R.id.circle_screen)).screenTouchLocationStart(xLocation, yLocation);
                    break;
                case MotionEvent.ACTION_MOVE:

                    ((CircleLayout) mLockView.findViewById(R.id.circle_screen)).screenTouchLocationDrag(xLocation, yLocation);
                    break;
                case MotionEvent.ACTION_UP:

                    ((CircleLayout) mLockView.findViewById(R.id.circle_screen)).screenTouchLocationEnd(xLocation, yLocation);
                    break;
            }
/*
                CircleDial.newInstance().setOnTouchCircleDial(event);*/

            return false;
        }
    };

    private void setLockBg() {
        Uri uri = Uri.parse("");
        String path = uri.getPath();
        Bitmap bitmap = null;
        Point point = new Point();
        ScreenHelper.getScreenPixel(mActivity, point);
        bitmap = ImageHelper.scaleImage(path, point.x, point.y);
    }

    private WindowManager getWindowManager() {
        WindowManager windowManager = mWindowManagerRef.get();
        if (windowManager == null) {
            windowManager = mActivity.getWindowManager();
            mWindowManagerRef = new WeakReference<WindowManager>(windowManager);
        }
        return windowManager;
    }

    public synchronized void Lock() throws NoLockStatusListenerException {
        CommonJava.Loging.i(LOG_NAME, "Lock()");


        if (lockStatusListener == null) {
            throw new NoLockStatusListenerException();
        }

        if (mLockView != null && !mIsLock) {
            if (mIsLock) {
                getWindowManager().updateViewLayout(mLockView, layoutParams);
                CommonJava.Loging.i(LOG_NAME, "updateViewLayout()");
            } else {
                getWindowManager().addView(mLockView, layoutParams);
                CommonJava.Loging.i(LOG_NAME, "addView()");
            }

            mIsLock = true;
            lockStatusListener.onLocked();

        }


        setFindView();

    }

    public synchronized void unLock() {
        Log.i(LOG_NAME, "unLock()");
        Log.i(LOG_NAME, "getWindowManager() : " + getWindowManager());
        Log.i(LOG_NAME, "mIsLock : " + mIsLock);
        if (getWindowManager() != null && mIsLock) {
            Log.i(LOG_NAME, "unLock() removeView");
/*
            layoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;
            getWindowManager().removeView(mLockView);
            getWindowManager().addView(mLockView, layoutParams);*/
            getWindowManager().removeView(mLockView);
            mIsLock = false;
            mActivity.finish();
        }
    }

    /**
     * 등록된 이메일로 비밀번호 보내는 함수
     */
    private void startEmailSend() {

        AsyncTask asyncTask = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {
                try {

                    GMailSender mail = new GMailSender("geogotae@gmail.com", "molppang202501");  //보내는 사람 메일 주소와 암호
                    String email = CommonJava.loadSharedPreferences(mActivity, "email");
                    String password = CommonJava.loadSharedPreferences(mActivity, "password");
                    //순서대로, 제목 - 본문 - 보내는 사람 메일 - 받는 사람 메일

                    mail.sendMail(
                            "다이얼락 비밀번호입니다.",
                            password,
                            "geogotae@gmail.com",
                            email
                    );


                } catch (Exception e) {
                }

                return null;
            }
        };
        asyncTask.execute();
    }

    /**
     * 이메일 발송시 토스트 메시지 3초 동안 보여줌
     */
    public void startTxtToast(String strMsg) {
        ((TextView) mLockView.findViewById(R.id.txt_toast)).setText(strMsg);
        ((TextView) mLockView.findViewById(R.id.txt_toast)).setVisibility(View.VISIBLE);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ((TextView) mLockView.findViewById(R.id.txt_toast)).setVisibility(View.INVISIBLE);
            }
        }, 2000);
    }

    public void setLockStatusListener(LockStatusListener listener) {
        this.lockStatusListener = listener;
    }

    public interface LockStatusListener {
        void onLocked();

        void onUnlock();
    }

    private void setListView() {
        mNowDate = mCalendar.getTime();

        setTextDay(mNowDate);

        changeListView(mNowDate);
    }

    /**
     * DB 에 저장된 축제 정보를 가져와서 리스트뷰에 setting
     *
     * @param date
     */
    private void changeListView(Date date) {
        CommonJava.Loging.i(LOG_NAME, "changeListView()");

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault());
        String strDate = dateFormat.format(date);

        CommonJava.Loging.i(LOG_NAME, "strDate : " + strDate);

        ArrayList<FestivalInfo> festivalInfos = null;

        DBManagement dbManageMent = new DBManagement(mActivity);
        dbManageMent.open();

        festivalInfos = dbManageMent.serchDay(strDate);
        Collections.shuffle(festivalInfos);

        ListViewAdapter listViewAdapter = new ListViewAdapter(festivalInfos);
        lock_screen_listview.setAdapter(listViewAdapter);

        dbManageMent.close();
    }

    private void setTextDay(Date nowDate) {
        String strTxtLockDay =
                CommonJava.getYear(nowDate) + ". " + CommonJava.getMonth(nowDate) + ". " + CommonJava.getDay(nowDate) + ". " + CommonJava.getDayOfWeek(nowDate);

        txt_lock_day.setText(strTxtLockDay);
    }



}
