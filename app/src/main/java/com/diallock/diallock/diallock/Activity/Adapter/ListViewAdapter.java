package com.diallock.diallock.diallock.Activity.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.diallock.diallock.diallock.Activity.Common.CommonJava;
import com.diallock.diallock.diallock.Activity.Common.FestivalInfo;
import com.diallock.diallock.diallock.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by park on 2016-09-12.
 * 축제 일정 표시
 */
public class ListViewAdapter extends BaseAdapter {

    private ArrayList<FestivalInfo> festivalInfos = new ArrayList<FestivalInfo>();

    public ListViewAdapter(ArrayList<FestivalInfo> festivalInfos) {
        this.festivalInfos = festivalInfos;
    }


    @Override
    public int getCount() {
        return festivalInfos.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View customView, ViewGroup parent) {
        CommonJava.Loging.i(getClass().getName(), "getView()");
        final int pos = position;
        final Context context = parent.getContext();

        if (customView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            customView = inflater.inflate(R.layout.lock_screen_listview_item, parent, false);
        }

        LinearLayout list_item = (LinearLayout) customView.findViewById(R.id.list_item);
        TextView txt_item_si = (TextView) customView.findViewById(R.id.txt_item_si);
        TextView txt_item_gu = (TextView) customView.findViewById(R.id.txt_item_gu);
        TextView txt_item_title = (TextView) customView.findViewById(R.id.txt_item_title);
        TextView txt_item_day = (TextView) customView.findViewById(R.id.txt_item_day);
        TextView txt_item_local = (TextView) customView.findViewById(R.id.txt_item_local);
        Button btn_item = (Button) customView.findViewById(R.id.btn_item);

        if (pos % 2 != 0) {
            list_item.setBackgroundColor(Color.rgb(222, 232, 243));
        } else {
            list_item.setBackgroundColor(Color.WHITE);
        }

        txt_item_si.setText(festivalInfos.get(pos).si);

        if (festivalInfos.get(pos).gu == null || festivalInfos.get(pos).gu.isEmpty()) {
            txt_item_gu.setVisibility(View.GONE);
        } else {
            txt_item_gu.setVisibility(View.VISIBLE);
            txt_item_gu.setText(festivalInfos.get(pos).gu);
        }

        txt_item_title.setText(festivalInfos.get(pos).title);

        String strDayStart = festivalInfos.get(pos).day_start;
        String strDayEnd = festivalInfos.get(pos).day_end;
        String strDayStartDay = null;
        String strDayEndDay = null;

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault());
        try {
            Date dateStart = dateFormat.parse(strDayStart);
            Date dateEnd = dateFormat.parse(strDayEnd);

            strDayStartDay = strDayStart + "." + CommonJava.getDayOfWeek(dateStart).substring(0, 1);
            strDayEndDay = strDayEnd + "." + CommonJava.getDayOfWeek(dateEnd).substring(0, 1);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        strDayStart = strDayStart.substring(5);
        strDayEnd = strDayEnd.substring(5);
        strDayStartDay = strDayStartDay.substring(5).replace("-",".");
        strDayEndDay = strDayEndDay.substring(5).replace("-",".");

        if (strDayStart.equals(strDayEnd)) {
            txt_item_day.setText(strDayStartDay);
        } else {
            txt_item_day.setText(strDayStartDay + "~" + strDayEndDay);
        }
        txt_item_local.setText(festivalInfos.get(pos).local);

        Date currentDate = Calendar.getInstance().getTime();
        String strCurrentDate = dateFormat.format(currentDate);
        String strCurrentDateSub = strCurrentDate.substring(5);

        CommonJava.Loging.i(getClass().getName(), "strCurrentDate : " + strCurrentDate);
        CommonJava.Loging.i(getClass().getName(), "strCurrentDate.compareTo(festivalInfos.get(pos).day_end) : " + strCurrentDate.compareTo(festivalInfos.get(pos).day_end));
        CommonJava.Loging.i(getClass().getName(), "strCurrentDate.compareTo(festivalInfos.get(pos).day_start) : " + strCurrentDate.compareTo(festivalInfos.get(pos).day_start));
        CommonJava.Loging.i(getClass().getName(), "strCurrentDate strDayStart : " + strDayStart);
        CommonJava.Loging.i(getClass().getName(), "strCurrentDate strDayEnd : " + strDayEnd);


        if (strCurrentDate.compareTo(festivalInfos.get(pos).day_end) > 0) {
            CommonJava.Loging.i(getClass().getName(), "festivalInfos.get(pos).day_end : " + festivalInfos.get(pos).day_end);
            btn_item.setBackgroundResource(R.drawable.btn_lock_screen_end);
            btn_item.setText("끝");
        } else if (strCurrentDate.compareTo(festivalInfos.get(pos).day_start) < 0) {
            CommonJava.Loging.i(getClass().getName(), "festivalInfos.get(pos).day_start : " + festivalInfos.get(pos).day_start);
            btn_item.setBackgroundResource(R.drawable.btn_lock_screen_end);
            btn_item.setText("예정");
        } else if (strDayStart.equals(strDayEnd) && strDayStart.equals(strCurrentDateSub)) {
            btn_item.setBackgroundResource(R.drawable.btn_lock_screen_today);
            btn_item.setText("당일");
        } else if (strDayStart.equals(strCurrentDateSub)) {
            btn_item.setBackgroundResource(R.drawable.btn_lock_screen_start);
            btn_item.setText("시작");
        } else if (strDayEnd.equals(strCurrentDateSub)) {
            btn_item.setBackgroundResource(R.drawable.btn_lock_screen_end);
            btn_item.setText("종료");
        } else {
            btn_item.setBackgroundResource(R.drawable.btn_lock_screen_pro);
            btn_item.setText("진행");
        }

        return customView;
    }
}
