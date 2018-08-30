package com.neurosky.algo_sdk_sample;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class WeekCalendarAdapter extends BaseAdapter {
    private ArrayList<DayInfo> arrayListDayInfo;
    private ArrayList<String> day_hours = new ArrayList<>();

    public Date selectedDate;
    TextView tvDay, wc_hour;

    public WeekCalendarAdapter(ArrayList<DayInfo> arrayLIstDayInfo, Date date) {
        this.arrayListDayInfo = arrayLIstDayInfo;
        this.selectedDate = date;
    }

    public void setData(ArrayList<String> arr) {
        this.day_hours = arr;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return arrayListDayInfo.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return arrayListDayInfo.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        DayInfo day = arrayListDayInfo.get(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.calendar_day, parent, false);
        }

        if (day != null) {
            tvDay = convertView.findViewById(R.id.week_cell_tv_day); //몇일인지 표시
            wc_hour = convertView.findViewById(R.id.wc_hour);

            tvDay.setText(day.getDay());

            ImageView ivSelected = convertView.findViewById(R.id.wiv_selected);
            if (day.isSameDay(selectedDate)) {
                ivSelected.setVisibility(View.VISIBLE); //오늘 날짜표시하기
            } else {
                ivSelected.setVisibility(View.INVISIBLE);
            }

            if (day_hours.size() == 0) {
                wc_hour.setText("");
            } else {
                if (day.isInMonth()) {
                    wc_hour.setText(day_hours.get(position));
                } else {
                    wc_hour.setText("");
                }
            }

            if (day.isInMonth()) {
                if ((position % 7 + 1) == Calendar.SUNDAY) {//캘런터 선데이값이 1임.

                    tvDay.setTextColor(Color.RED);
                } else if ((position % 7 + 1) == Calendar.SATURDAY) { //토요일은 7
                    tvDay.setTextColor(Color.BLUE);
                } else {
                    tvDay.setTextColor(Color.BLACK);
                }

            } else {
                tvDay.setTextColor(Color.GRAY); //이번달에 날짜가 아니면...
            }
        }
        convertView.setTag(day);

        return convertView;
    }

}