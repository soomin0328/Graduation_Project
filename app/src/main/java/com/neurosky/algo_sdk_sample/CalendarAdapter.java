package com.neurosky.algo_sdk_sample;

import android.graphics.Color;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class CalendarAdapter extends BaseAdapter {

    private ArrayList<DayInfo> arrayListDayInfo;
    private ArrayList<String> day_hours = new ArrayList<>();

    public Date selectedDate;
    DayInfo day;

    TextView tvDay, c_hour;

    public CalendarAdapter(ArrayList<DayInfo> arrayLIstDayInfo, Date date) {
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
        day = arrayListDayInfo.get(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.day, parent, false);
        }

        if (day != null) {
            tvDay = convertView.findViewById(R.id.day_cell_tv_day); //몇일인지 표시
            c_hour = convertView.findViewById(R.id.c_hour);

            tvDay.setText(day.getDay());

            ImageView ivSelected = convertView.findViewById(R.id.iv_selected);
            if (day.isSameDay(selectedDate)) {
                ivSelected.setVisibility(View.VISIBLE); //오늘 날짜표시하기
            } else {
                ivSelected.setVisibility(View.INVISIBLE);
            }

            if (day_hours.size() == 0) {
                c_hour.setText("");
            } else {
                if (day.isInMonth()) {
                    c_hour.setText(day_hours.get(Integer.parseInt(tvDay.getText().toString()) - 1));
                } else {
                    c_hour.setText("");
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