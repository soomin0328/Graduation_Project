package com.neurosky.algo_sdk_sample;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MdayFrag extends Fragment {

    private TextView tvCalendarTitle;
    //private TextView tvSelectedDate;

    private ArrayList<DayInfo> arrayListDayInfo;
    Calendar mThisMonthCalendar;
    WeekCalendarAdapter mCalendarAdapter;

    View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        //inflater를 사용해 프래그먼트에 사용할 레이아웃 불러오고 리턴
        //해당 프래그먼트에 대한 기능적코드 여기에 넣으래
//..?
        view = inflater.inflate(R.layout.mp_dayfrag, container, false);

        Button btnPreviousCalendar = view.findViewById(R.id.md_previous_calendar);
        Button btnNextCalendar = view.findViewById(R.id.md_next_calendar);
        Button goToday = view.findViewById(R.id.mdptoday);
        tvCalendarTitle = view.findViewById(R.id.md_calendar_title);


        goToday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mThisMonthCalendar = Calendar.getInstance();
                getCalendar(mThisMonthCalendar.getTime());
            }
        });

        btnPreviousCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mThisMonthCalendar.add(Calendar.DAY_OF_MONTH, -1);

                getCalendar(mThisMonthCalendar.getTime());
            }
        });
        btnNextCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mThisMonthCalendar.add(Calendar.DAY_OF_MONTH, +1);

                getCalendar(mThisMonthCalendar.getTime());
            }
        });


        arrayListDayInfo = new ArrayList<>();
        return view;
    }


    public void onResume() {
        super.onResume();

        mThisMonthCalendar = Calendar.getInstance();
        getCalendar(mThisMonthCalendar.getTime());
    }

    private void getCalendar(Date dateForCurrentMonth) {
        int dayOfWeek;
        int thisWeekLastDay;

        arrayListDayInfo.clear();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateForCurrentMonth);
        dayOfWeek = calendar.get(Calendar.DAY_OF_MONTH);//오늘
        calendar.set(Calendar.DATE, dayOfWeek);//1일로 변경

        Log.d("CalendarTest", "dayOfWeek = " + dayOfWeek + "");

        /*if(dayOfWeek == Calendar.SUNDAY){//현재 달의 1일이 무슨 요일인지 검사
            Log.d("현재 달 1일 무슨 요일",dayOfWeek+"");
            dayOfWeek += 7;
        }*/

        thisWeekLastDay = calendar.getActualMaximum(Calendar.DAY_OF_WEEK);

        setCalendarTitle();

        DayInfo day;
        //여기 아래부터
        calendar.add(Calendar.DATE, -1 * (dayOfWeek - 1)); //현재 달력화면에서 보이는 지난달의 시작일
        for (int i = 0; i < dayOfWeek - 1; i++) {
            day = new DayInfo();
            day.setDate(calendar.getTime());
            day.setInMonth(true);
            // arrayListDayInfo.add(day);
            calendar.add(Calendar.DATE, +1);
        }
//여기까지 지우면 오늘기준날짜부터 일주일간격 날짜로 나옴.
        /*for(int i=1; i <= thisWeekLastDay; i++){
            day = new DayInfo();
            day.setDate(calendar.getTime());
            day.setInMonth(true);
            arrayListDayInfo.add(day);

            calendar.add(Calendar.DATE, +1);
        }*/

        /*for(int i=1; i<thisMonthLastDay+1; i++) {
            day = new DayInfo();
            day.setDate(calendar.getTime());
            day.setInMonth(false);
            arrayListDayInfo.add(day);

            calendar.add(Calendar.DATE, +1);
        }*/

        //    mCalendarAdapter = new WeekCalendarAdapter(arrayListDayInfo, selectedDate);

        // tvSelectedDate.setText(sdf.format(selectedDate));
    }

    private void setCalendarTitle() {
        StringBuilder sb = new StringBuilder();

        sb.append(mThisMonthCalendar.get(Calendar.YEAR))
                .append("년 ")
                .append((mThisMonthCalendar.get(Calendar.MONTH) + 1))
                .append("월")
                .append(mThisMonthCalendar.get(Calendar.DAY_OF_MONTH))
                .append("일");
        tvCalendarTitle.setText(sb.toString());
    }


}