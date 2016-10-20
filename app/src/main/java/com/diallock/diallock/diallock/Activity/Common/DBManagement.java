package com.diallock.diallock.diallock.Activity.Common;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by park on 2016-09-12.
 */
public class DBManagement {

    private static final String TAG = "DBManageMent";
    public static final String SI = "si";
    public static final String GU = "gu";
    public static final String TITLE = "title";
    public static final String DAY_START = "day_start";
    public static final String DAY_END = "day_end";
    public static final String LOCAL = "local";


    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    /**
     * Database creation sql statement
     */

    private static final String DATABASE_CREATE = "create table notes (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
            + "si TEXT NOT NULL, gu TEXT, title TEXT NOT NULL, day_start TEXT, day_end TEXT, local TEXT NOT NULL);";

    private static final String DATABASE_NAME = "data";
    private static final String DATABASE_TABLE = "notes";
    private static final int DATABASE_VERSION = 2;
    private final Context mContext;

    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DATABASE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            CommonJava.Loging.w(TAG, "Upgrading database from version " + oldVersion + " to " + newVersion
                    + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS notes");
            onCreate(db);
        }
    }

    public DBManagement(Context context) {
        this.mContext = context;
    }

    public DBManagement open() throws SQLException {
        mDbHelper = new DatabaseHelper(mContext);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        mDbHelper.close();
    }

    public void delete() {
        DatabaseHelper databaseHelper = new DatabaseHelper(mContext);
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();
        sqLiteDatabase.execSQL("DELETE FROM notes");
    }

    public long createNote(String si, String gu, String title, String day_start, String day_end, String local) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(SI, si);
        initialValues.put(GU, gu);
        initialValues.put(TITLE, title);
        initialValues.put(DAY_START, day_start);
        initialValues.put(DAY_END, day_end);
        initialValues.put(LOCAL, local);
        return mDb.insert(DATABASE_TABLE, null, initialValues);
    }

    public ArrayList<FestivalInfo> serchDay(String currenDay) {
        CommonJava.Loging.i(getClass().getName(), "serchDay() currenDay : " + currenDay);
        String sqlSelete = "SELECT si, gu, title, day_start, day_end, local  FROM " + DATABASE_TABLE + " WHERE day_start <='" + currenDay + "' and day_end >='" + currenDay + "';";

        ArrayList<FestivalInfo> festivalInfos = new ArrayList<FestivalInfo>();
        Cursor cursorSelect = mDb.rawQuery(sqlSelete, null);

        CommonJava.Loging.i(getClass().getName(), "cursorSelect : " + cursorSelect.getCount());
        while (cursorSelect.moveToNext()) {
            FestivalInfo festivalInfo = new FestivalInfo();
            festivalInfo.si = cursorSelect.getString(0);
            festivalInfo.gu = cursorSelect.getString(1);
            festivalInfo.title = cursorSelect.getString(2);
            festivalInfo.day_start = cursorSelect.getString(3);
            festivalInfo.day_end = cursorSelect.getString(4);
            festivalInfo.local = cursorSelect.getString(5);

            festivalInfos.add(festivalInfo);
            CommonJava.Loging.i(getClass().getName(), "serchDay()");
            CommonJava.Loging.i(getClass().getName(), "si : " + festivalInfo.si);
            CommonJava.Loging.i(getClass().getName(), "gu : " + festivalInfo.gu);
            CommonJava.Loging.i(getClass().getName(), "title : " + festivalInfo.title);
            CommonJava.Loging.i(getClass().getName(), "day_start : " + festivalInfo.day_start);
            CommonJava.Loging.i(getClass().getName(), "day_end : " + festivalInfo.day_end);
            CommonJava.Loging.i(getClass().getName(), "local : " + festivalInfo.local);
        }

        cursorSelect.close();

        return festivalInfos;
    }

}