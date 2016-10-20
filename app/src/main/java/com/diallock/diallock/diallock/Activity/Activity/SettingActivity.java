package com.diallock.diallock.diallock.Activity.Activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.diallock.diallock.diallock.Activity.Common.CommonJava;
import com.diallock.diallock.diallock.Activity.Common.DBManagement;
import com.diallock.diallock.diallock.Activity.taskAction.ScreenService;
import com.diallock.diallock.diallock.R;

import java.io.IOException;
import java.io.InputStream;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class SettingActivity extends AppCompatActivity {

    private LinearLayout linear_lock;
    private LinearLayout linear_unlock;
    private LinearLayout linear_pass_change;
    private LinearLayout linear_img_change;
    private LinearLayout linear_email_change;
    private LinearLayout linear_ad;

    private Boolean lockCheck;
    private Boolean backFlag;

    final int REQ_CODE_SELECT_IMAGE = 100;
    private final static int PERMISSIONS_REQ_NUM = 6242;

    private DBManagement dbManageMent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        Boolean isAuthorityCheck = isAuthorityCheck();
        if (isAuthorityCheck) {
            CommonJava.Loging.i("SettingActivity", "isAuthorityCheck : " + isAuthorityCheck);
            loadPassword();
        }

        setFindView();
        init();
        setOnClick();
        copyExcelDataToDatabase();
    }

    /**
     * 저장된 패스워드가 없으면 패드워드 설정 창으로 이동
     */
    private void loadPassword() {
        String password = CommonJava.loadSharedPreferences(SettingActivity.this, "password");
        CommonJava.Loging.i("SettingActivity", "password : " + password);
        if (password.isEmpty()) {

            Intent intentSetPassword = new Intent(SettingActivity.this, PasswordChangeActivity.class);
            intentSetPassword.putExtra("strSwitch", "veryfirst");
            startActivity(intentSetPassword);
            finish();

        }

    }

    /**
     * 마시멜로우 이상 버전에서 사용되는 권한 체크 하기
     */
    private Boolean isAuthorityCheck() {
        /**
         * 주소록 가져오기
         */
        //Boolean get_accounts_bl = ActivityCompat.shouldShowRequestPermissionRationale(SettingActivity.this, Manifest.permission.GET_ACCOUNTS);
        //CommonJava.Loging.i("SettingActivity", "get_accounts_bl : " + get_accounts_bl);
        int checkGetAccounts = 99;
        int checkReadExternalStorage = 99;
        int checkReadPhoneState = 99;
        Boolean checkOverlays = false;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            checkGetAccounts = checkSelfPermission(Manifest.permission.GET_ACCOUNTS);
            checkReadExternalStorage = checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
            checkReadPhoneState = checkSelfPermission(Manifest.permission.READ_PHONE_STATE);
            checkOverlays = Settings.canDrawOverlays(this);

        }
        if (checkGetAccounts == -1) {
            CommonJava.Loging.i("SettingActivity", "checkGetAccounts : " + checkGetAccounts);
            ActivityCompat.requestPermissions(SettingActivity.this, new String[]{Manifest.permission.GET_ACCOUNTS}, 0);
            return false;
        } else if (checkReadExternalStorage == -1) {
            CommonJava.Loging.i("SettingActivity", "checkReadExternalStorage : " + checkReadExternalStorage);
            ActivityCompat.requestPermissions(SettingActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
            return false;
        } else if (checkReadPhoneState == -1) {
            CommonJava.Loging.i("SettingActivity", "checkReadPhoneState : " + checkReadPhoneState);
            ActivityCompat.requestPermissions(SettingActivity.this, new String[]{Manifest.permission.READ_PHONE_STATE}, 0);

        } else if (!checkOverlays) {

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                CommonJava.Loging.i("SettingActivity", "checkOverlays : " + checkOverlays);
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, 6242);
            }
        }


        return true;

    }

    /**
     * 레이아웃과 연결
     */
    private void setFindView() {
        linear_lock = (LinearLayout) findViewById(R.id.linear_lock);
        linear_unlock = (LinearLayout) findViewById(R.id.linear_unlock);
        linear_pass_change = (LinearLayout) findViewById(R.id.linear_pass_change);
        linear_img_change = (LinearLayout) findViewById(R.id.linear_img_change);
        linear_email_change = (LinearLayout) findViewById(R.id.linear_email_change);
        linear_ad = (LinearLayout) findViewById(R.id.linear_ad);
    }

    /**
     * 상태값 초기화
     */
    private void init() {

        String strLockCheck = CommonJava.loadSharedPreferences(SettingActivity.this, "lockCheck");

        switch (strLockCheck) {
            case "true":
                lockCheck = true;
                linear_lock.setBackgroundResource(R.drawable.btn_click);
                linear_unlock.setBackgroundResource(R.drawable.btn_bg);
                break;
            case "false":
                lockCheck = false;
                linear_lock.setBackgroundResource(R.drawable.btn_bg);
                linear_unlock.setBackgroundResource(R.drawable.btn_click);
                break;
            default:
                lockCheck = false;
                linear_lock.setBackgroundResource(R.drawable.btn_bg);
                linear_unlock.setBackgroundResource(R.drawable.btn_click);
        }

        backFlag = false;


    }

    /**
     * 클릭 이벤트 연결
     */
    private void setOnClick() {
        linear_lock.setOnClickListener(onClickListener);
        linear_unlock.setOnClickListener(onClickListener);
        linear_pass_change.setOnClickListener(onClickListener);
        linear_img_change.setOnClickListener(onClickListener);
        linear_email_change.setOnClickListener(onClickListener);
        linear_ad.setOnClickListener(onClickListener);
    }

    /**
     * 클릭 이벤트 설정
     */
    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            switch (view.getId()) {
                case R.id.linear_lock:
                    String password = CommonJava.loadSharedPreferences(SettingActivity.this, "password");
                    if (password.isEmpty()) {
                        loadPassword();
                    } else if (lockCheck == false) {

                        linear_lock.setBackgroundResource(R.drawable.btn_click);
                        linear_unlock.setBackgroundResource(R.drawable.btn_bg);

                        //Intent intentLockScreen = new Intent(SettingActivity.this, LockScreenActivity.class);
                        Intent intentLockScreen = new Intent(SettingActivity.this, LockScreenViewActivity.class);
                        intentLockScreen.putExtra("strSwitch", "SettingActivity");
                        startActivity(intentLockScreen);
                        //lockCheck = true;
                    }

                    break;
                case R.id.linear_unlock:

                    if (lockCheck == true) {
                        CommonJava.saveSharedPreferences(SettingActivity.this, "lockCheck", "false");
                        linear_lock.setBackgroundResource(R.drawable.btn_bg);
                        linear_unlock.setBackgroundResource(R.drawable.btn_click);
                        lockCheck = false;

                        Intent intentStopService = new Intent(SettingActivity.this, ScreenService.class);
                        stopService(intentStopService);

                    }

                    break;
                case R.id.linear_pass_change:

                    Intent intentPassChange = new Intent(SettingActivity.this, PasswordChangeActivity.class);
                    intentPassChange.putExtra("strSwitch", "first");
                    startActivity(intentPassChange);

                    break;
                case R.id.linear_img_change:

                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
                    intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, REQ_CODE_SELECT_IMAGE);

                    break;
                case R.id.linear_email_change:

                    Intent intentEmailChange = new Intent(SettingActivity.this, EmailChangeActivity.class);
                    startActivity(intentEmailChange);

                    break;
                case R.id.linear_ad:

                    Intent intentAdRequest = new Intent(SettingActivity.this, AdRequestActivity.class);
                    startActivity(intentAdRequest);

                    break;
            }

        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        CommonJava.Loging.i("SettingActivity", "requestCode : " + requestCode);
        CommonJava.Loging.i("SettingActivity", "permissions : " + permissions[0]);
        CommonJava.Loging.i("SettingActivity", "grantResults : " + grantResults[0]);

        if (grantResults[0] != -1) {

            int checkReadExternalStorage = 99;
            int checkReadPhoneState = 99;
            Boolean checkOverlays = false;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                checkReadExternalStorage = checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
                checkReadPhoneState = checkSelfPermission(Manifest.permission.READ_PHONE_STATE);
                checkOverlays = Settings.canDrawOverlays(this);
            }
            if (checkReadExternalStorage == -1) {
                CommonJava.Loging.i("SettingActivity", "checkReadExternalStorage : " + checkReadExternalStorage);
                ActivityCompat.requestPermissions(SettingActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
            } else if (checkReadPhoneState == -1) {
                CommonJava.Loging.i("SettingActivity", "checkReadPhoneState : " + checkReadPhoneState);
                ActivityCompat.requestPermissions(SettingActivity.this, new String[]{Manifest.permission.READ_PHONE_STATE}, 0);
            } else if (!checkOverlays) {
                CommonJava.Loging.i("SettingActivity", "checkOverlays : " + checkOverlays);
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, PERMISSIONS_REQ_NUM);
            }

        } else {
            finish();
        }


    }

    /**
     * 뒤로가기 버튼 클릭 시 종료
     */
    @Override
    public void onBackPressed() {

        CommonJava.Loging.i("MainActivity", "onBackPressed()");


        if (backFlag) {

            CommonJava.Loging.i("MainActivity", "onBackPressed() : 종료");

            finish();
        } else {
            backFlag = true;

            Toast.makeText(SettingActivity.this, "한번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show();
            Handler han = new Handler();
            han.postDelayed(new Runnable() {

                @Override
                public void run() {
                    backFlag = false;
                }
            }, 2000);
        }

    }

    /**
     * 갤러리에서 선택된 이미지 가져오기
     * 출처 {Link :http://ankyu.entersoft.kr/Lecture/android/gallery_01.asp}
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        //Toast.makeText(getBaseContext(), "resultCode : " + resultCode, Toast.LENGTH_SHORT).show();
        CommonJava.Loging.i("SettingActivity", "onActivityResult resultCode :" + resultCode);

        switch (requestCode) {
            case REQ_CODE_SELECT_IMAGE:
                if (resultCode == Activity.RESULT_OK) {
                /* // 원본
                try {
                    //Uri에서 이미지 이름을 얻어온다.
                    //String name_Str = getImageNameToUri(data.getData());

                    //이미지 데이터를 비트맵으로 받아온다.
                    Bitmap image_bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                    ImageView image = (ImageView)findViewById(R.id.imageView1);

                    //배치해놓은 ImageView에 set
                    image.setImageBitmap(image_bitmap);


                    //Toast.makeText(getBaseContext(), "name_Str : "+name_Str , Toast.LENGTH_SHORT).show();



                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }*/

                    String strUrl = String.valueOf(data.getData());
                    CommonJava.saveSharedPreferences(SettingActivity.this, "imgUrl", strUrl);
                    CommonJava.Loging.i("SettingActivity", "onActivityResult strUrl : " + strUrl);


                } else if (resultCode == Activity.RESULT_CANCELED) {

                    CommonJava.saveSharedPreferences(SettingActivity.this, "imgUrl", null);
                    CommonJava.Loging.i("SettingActivity", "onActivityResult strUrl null ");
                }
                break;
            case PERMISSIONS_REQ_NUM:
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                    if (!Settings.canDrawOverlays(this)) {
                        finish();
                    } else {
                        loadPassword();
                    }
                }
                break;
        }

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        CommonJava.Loging.i(getLocalClassName(), "onRestart()");

        /*lockCheck = Boolean.valueOf(CommonJava.loadSharedPreferences(SettingActivity.this, "lockCheck"));
        CommonJava.Loging.i(getLocalClassName(), "lockCheck : " + lockCheck);

        if (lockCheck) {
            linear_lock.setBackgroundResource(R.drawable.btn_click);
            linear_unlock.setBackgroundResource(R.drawable.btn_bg);

            Intent intentStopService = new Intent(SettingActivity.this, ScreenService.class);
            stopService(intentStopService);
            System.exit(0);
        } else {
            linear_lock.setBackgroundResource(R.drawable.btn_bg);
            linear_unlock.setBackgroundResource(R.drawable.btn_click);
        }*/

    }

    @Override
    protected void onResume() {
        super.onResume();
        CommonJava.Loging.i(getLocalClassName(), "onResume()");

        lockCheck = Boolean.valueOf(CommonJava.loadSharedPreferences(SettingActivity.this, "lockCheck"));
        CommonJava.Loging.i(getLocalClassName(), "lockCheck : " + lockCheck);

        if (lockCheck) {
            linear_lock.setBackgroundResource(R.drawable.btn_click);
            linear_unlock.setBackgroundResource(R.drawable.btn_bg);
        } else {
            linear_lock.setBackgroundResource(R.drawable.btn_bg);
            linear_unlock.setBackgroundResource(R.drawable.btn_click);
        }


    }

    /**
     * assets 폴더에 존재하는 엑셀 파일을 db 에 넣음
     */
    private void copyExcelDataToDatabase() {

        dbManageMent = new DBManagement(SettingActivity.this);
        dbManageMent.delete();

        CommonJava.Loging.i("CustomKey", "copyExcelDataToDatabase()");

        Workbook workbook = null;
        Sheet sheet = null;

        try {
            InputStream is = getBaseContext().getResources().getAssets().open("festival2.xls");

            try {
                workbook = workbook.getWorkbook(is);

                if (workbook != null) {
                    sheet = workbook.getSheet(0);

                    if (sheet != null) {

                        int nMaxColumn = 7;
                        int nRowStartIndex = 1;
                        int nRowEndIndex = sheet.getColumn(nMaxColumn - 1).length - 1;
                        int nColumnStartIndex = 0;
                        int nColumnEndIndex = sheet.getRow(2).length - 1;
                        dbManageMent.open();

                        for (int nRow = nRowStartIndex; nRow <= nRowEndIndex; nRow++) {

                            //String no = sheet.getCell(nColumnStartIndex, nRow).getContents();
                            String si = null;
                            String gu = null;
                            String title = null;
                            String day_start = null;
                            String day_end = null;
                            String local = null;


                            for (int nColumn = nColumnStartIndex; nColumn <= nColumnEndIndex; nColumn++) {
                                si = sheet.getCell(1, nRow).getContents();
                                gu = sheet.getCell(2, nRow).getContents();
                                title = sheet.getCell(3, nRow).getContents();
                                String strDayStart = sheet.getCell(4, nRow).getContents();
                                day_start = strDayStart.replace(".", "-");
                                String strDayEnd = sheet.getCell(5, nRow).getContents();
                                day_end = strDayEnd.replace(".", "-");
                                local = sheet.getCell(6, nRow).getContents();

                            }

                            CommonJava.Loging.i(getLocalClassName(), "si : " + si + " gu : " + gu + " title : " + title + " day_start : " + day_start + " day_end : " + day_end + " local : " + local);

                            dbManageMent.createNote(si, gu, title, day_start, day_end, local);
                        }

                    } else {
                        CommonJava.Loging.e(getLocalClassName(), "sheet is null");
                    }
                } else {
                    CommonJava.Loging.e(getLocalClassName(), "workbook is null");
                }

            } catch (BiffException e) {
                e.printStackTrace();
                CommonJava.Loging.e(getLocalClassName(), "Error : " + e.toString());
            }

        } catch (IOException e) {
            e.printStackTrace();
            CommonJava.Loging.e(getLocalClassName(), "Error : " + e.toString());
        }

        if (workbook != null) {
            workbook.close();
        }

    }

    @Override
    protected void onDestroy() {
        dbManageMent.close();
        super.onDestroy();
    }
}
