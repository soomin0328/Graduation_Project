package com.neurosky.algo_sdk_sample;

import android.app.Fragment;

import java.util.ArrayList;
import java.util.Calendar;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;

import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;

public class WeekFrag extends Fragment {

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference("USERS");
    private DatabaseReference database = firebaseDatabase.getReference("USERS");
    private DatabaseReference databasePercent = firebaseDatabase.getReference("USERS");

    String i, wconper, h = "";
    long wconTime, wconHour, conTime, cmigrate, day_allTime;
    int weekAim, z;

    DayInfo day;
    private TextView tvCalendarTitle;
    //private TextView tvSelectedDate;
    private GridView gvCalendar;
    private LineChart mChart;           //mChart 라는 LineChart를 선언해준다.

    private ArrayList<DayInfo> arrayListDayInfo;
    private ArrayList<String> hours = new ArrayList<>();

    Calendar mThisMonthCalendar;
    WeekCalendarAdapter mCalendarAdapter, mCalendarAdapter2;

    Date selectedDate;
    ProgressBar bar;
    TextView barPercent;
    TextView wAimPer; //날짜하나의 달성율
    View view;
    TextView cp_week; //주별에서 하루하루 집중ㅅㅣ간
    TextView cp_weekall;

    public void setSelectedDate(Date date) {
        selectedDate = date;

        if (mCalendarAdapter != null) {
            mCalendarAdapter.selectedDate = date;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        hours.clear();

        view = inflater.inflate(R.layout.cp_weekfrag, container, false);

        TextView ws_hour = view.findViewById(R.id.ws_hour);
        cp_week = view.findViewById(R.id.ws_hour);
        cp_weekall = view.findViewById(R.id.cweekTime);
        tvCalendarTitle = view.findViewById(R.id.w_calendar_title);
        wAimPer = view.findViewById(R.id.wconper);

        gvCalendar = view.findViewById(R.id.w_gv_calendar);

        Button btnPreviousCalendar = view.findViewById(R.id.w_previous_calendar);
        Button btnNextCalendar = view.findViewById(R.id.w_next_calendar);
        Button goToday = view.findViewById(R.id.wcptoday);

        bar = (ProgressBar) view.findViewById(R.id.wprogressBar);
        barPercent = view.findViewById(R.id.wbarPercent);

        mChart = view.findViewById(R.id.wChart);
        mChart.setDragEnabled(true);

        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setEnabled(false);

        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);   //x축 값을 밑에 표시.
        xAxis.setDrawGridLines(false);                  //x축의 그리드라인을 없앰.

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setDrawGridLines(false);               //y축의 그리드라인을 없앰.
        ArrayList<Entry> yValues = new ArrayList<>();
        // ArrayList<Entry> xValues = new ArrayList<>();
        //ArrayList<Entry> zValues = new ArrayList<>();     //각 그래프 별 데이터를 입력받을 배열을 만듦.

        //x축을 일월화수목금토 할거임 숫자를->한글로바꾸는거 알아보기
        yValues.add(new Entry(1, 60));
        yValues.add(new Entry(2, 50));
        yValues.add(new Entry(3, 70));
        yValues.add(new Entry(4, 30));
        yValues.add(new Entry(5, 50));
        yValues.add(new Entry(6, 60));
        yValues.add(new Entry(7, 20));

        LineDataSet set1 = new LineDataSet(yValues, "Alpha");
        set1.setColor(Color.RED);

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);
        LineData data = new LineData(dataSets);     //dataSet이란 배열을 data로 선언
        data.setDrawValues(true);                  //data의 수치를 그래프 위에 나타내지 않기.
        mChart.setData(data);                       //mChart를 이용하여 data 표시

        goToday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hours.clear();

                mThisMonthCalendar = Calendar.getInstance();

                database.addValueEventListener(weekListener);
                databasePercent.addValueEventListener(dataPercentListener);
                getCalendar(mThisMonthCalendar.getTime());
            }
        });

        btnPreviousCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hours.clear();

                mThisMonthCalendar.add(Calendar.WEEK_OF_MONTH, -1);

                database.addValueEventListener(weekListener);
                databasePercent.addValueEventListener(dataPercentListener);
                getCalendar(mThisMonthCalendar.getTime());
            }
        });
        btnNextCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hours.clear();

                mThisMonthCalendar.add(Calendar.WEEK_OF_MONTH, +1);

                database.addValueEventListener(weekListener);
                databasePercent.addValueEventListener(dataPercentListener);
                getCalendar(mThisMonthCalendar.getTime());
            }
        });

        gvCalendar.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            ValueEventListener pListener = new ValueEventListener() {

                @Override

                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    for (DataSnapshot snapshot : dataSnapshot.child("aa").child("EEG DATA").child(mThisMonthCalendar.get(Calendar.YEAR) + "년")
                            .child(String.valueOf(mThisMonthCalendar.get(Calendar.MONTH) + 1 + "월")).child(String.valueOf(i + "일"))
                            .child("집중시간").getChildren()) {
                        long test = Long.parseLong(snapshot.getValue().toString());
                        wconTime += test;
                    }

                    wconHour = wconTime / 1000 / 3600;
                    long wconMin = (wconTime / 1000) / 60;
                    long wconSec = ((wconTime) / 1000) % 60;

                    if (wconHour == 0) {
                        cp_week.setText(wconMin + "분 " + wconSec + "초");
                        wconTime = 0;
                    } else if (wconHour != 0 && wconMin == 0) {
                        cp_week.setText(wconHour + "시간" + wconMin + "분 " + wconSec + "초");
                        wconTime = 0;
                    } else
                        cp_week.setText(wconHour + "시간" + wconMin + "분 " + wconSec + "초");
                    wconTime = 0;
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            };

            ValueEventListener percentListener = new ValueEventListener() { //주별에서 날짜하나 클릭했을때 그날짜의 달성율

                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.child("aa").child("EEG DATA").child(mThisMonthCalendar.get(Calendar.YEAR) + "년")
                            .child(String.valueOf(mThisMonthCalendar.get(Calendar.MONTH) + 1 + "월")).child(String.valueOf(i + "일"))
                            .child("하루달성율").getChildren())

                    {
                        wconper = (snapshot.getValue().toString());
                    }

                    if (wconper == null) {
                        wconper = "0";
                        wAimPer.setText(wconper + "%");

                    } else {
                        wAimPer.setText(wconper + "%");
                    }
                    wconper = "0";
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            };

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) { //디비에서 누른날짜에 해당되는 값을 찾아야함'

                databaseReference.addValueEventListener(pListener);
                databaseReference.addValueEventListener(percentListener);
                setSelectedDate(((DayInfo) view.getTag()).getDate());
                day = arrayListDayInfo.get(position);
                i = day.getDay();

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

    int dayOfWeek;

    private void getCalendar(Date dateForCurrentMonth) {

        int thisWeekLastDay;

        arrayListDayInfo.clear();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateForCurrentMonth);
        dayOfWeek = calendar.get(Calendar.WEEK_OF_MONTH);
        calendar.set(Calendar.DATE, dayOfWeek);//1일로 변경--->오늘날짜로..

        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        thisWeekLastDay = calendar.getActualMaximum(Calendar.DAY_OF_WEEK);

        database.addValueEventListener(weekListener);
        databasePercent.addValueEventListener(dataPercentListener);
        setCalendarTitle();

        // DayInfo day;

        for (int i = 1; i <= thisWeekLastDay; i++) {
            day = new DayInfo();
            day.setDate(calendar.getTime());

            day.setInMonth(true);
            arrayListDayInfo.add(day);

            calendar.add(Calendar.DATE, +1);

        }

        mCalendarAdapter2 = new WeekCalendarAdapter(arrayListDayInfo, selectedDate);
        gvCalendar.setAdapter(mCalendarAdapter2);

        Handler m = new Handler(Looper.getMainLooper());
        m.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.e("week size", String.valueOf(hours.size()));
                mCalendarAdapter = new WeekCalendarAdapter(arrayListDayInfo, selectedDate);
                gvCalendar.setAdapter(mCalendarAdapter);
                mCalendarAdapter.setData(hours);
            }
        }, 4000);

    }

    ValueEventListener dataPercentListener = new ValueEventListener() { //한 주 의 전체 퍼센트
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) { //c_allTime이 식 위에
            int testValue;
            z = Integer.parseInt(day.getDay());
            int k = z - 6;
            int a = z;

            for (int j = k; j <= a; j++) {
                for (DataSnapshot snapshot : dataSnapshot.child("aa").child("EEG DATA").child(mThisMonthCalendar.get(Calendar.YEAR) + "년")
                        .child(String.valueOf(mThisMonthCalendar.get(Calendar.MONTH) + 1 + "월")).child(String.valueOf(j + "일"))
                        .child("목표시간").getChildren()) {
                    if (snapshot.getValue().toString() == null) {
                        testValue = 0;
                    } else {
                        testValue = Integer.parseInt(snapshot.getValue().toString());
                    }
                    weekAim += testValue;
                }
            }
            long migrate2 = cmigrate;
            long week_Aim2 = weekAim;
            long imValue;

            if (week_Aim2 == 0 || migrate2 == 0 || cmigrate == 0 || weekAim == 0) {
                bar.setProgress(0);
                barPercent.setText("0");
            } else {
                imValue = ((migrate2) / (week_Aim2 / 10L) * 10L);
                bar.setProgress((int) imValue);
                barPercent.setText(imValue + "");
                weekAim = 0;
                migrate2 = 0;

            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };


    ValueEventListener weekListener = new ValueEventListener() { //주별용 주총 시간 구할겨

        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            long test;
            int i = Integer.parseInt(day.getDay());
            int k = i - 6;

            if ((i == 1 || i == 2 || i == 3 || i == 4 || i == 5 || i == 6) && (dayOfWeek == 4 || dayOfWeek == 5)) {
                int month = mThisMonthCalendar.get(Calendar.MONTH) + 1;
                if (month == 1 || month == 3 || month == 5 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12) {
                    for (int j = 31 - (6 - i); j <= 31; j++) {
                        for (DataSnapshot snapshot : dataSnapshot.child("aa").child("EEG DATA").child(mThisMonthCalendar.get(Calendar.YEAR) + "년")
                                .child(String.valueOf(mThisMonthCalendar.get(Calendar.MONTH) + 1 + "월")).child(String.valueOf(j + "일"))
                                .child("집중시간").getChildren()) {
                            if (snapshot.getValue().toString() == null) {
                                test = 0;
                            } else {
                                test = Long.parseLong(snapshot.getValue().toString());
                            }
                            day_allTime += test;
                            conTime += test;
                        }
                        divide(day_allTime);
                        day_allTime = 0;
                        conTime += 0;
                    }
                } else if (month == 4 || month == 6 || month == 9 || month == 11) {
                    for (int j = 30 - (6 - i); j <= 30; j++) {
                        for (DataSnapshot snapshot : dataSnapshot.child("aa").child("EEG DATA").child(mThisMonthCalendar.get(Calendar.YEAR) + "년")
                                .child(String.valueOf(mThisMonthCalendar.get(Calendar.MONTH) + "월")).child(String.valueOf(j + "일"))
                                .child("집중시간").getChildren()) {
                            if (snapshot.getValue().toString() == null) {
                                test = 0;
                            } else {
                                test = Long.parseLong(snapshot.getValue().toString());
                            }
                            day_allTime += test;
                            conTime += test;
                        }
                        divide(day_allTime);
                        day_allTime = 0;
                        conTime += 0;
                    }
                } else {
                    for (int j = 28 - (6 - i); j <= 28; j++) {
                        for (DataSnapshot snapshot : dataSnapshot.child("aa").child("EEG DATA").child(mThisMonthCalendar.get(Calendar.YEAR) + "년")
                                .child(String.valueOf(mThisMonthCalendar.get(Calendar.MONTH) + "월")).child(String.valueOf(j + "일"))
                                .child("집중시간").getChildren()) {
                            if (snapshot.getValue().toString() == null) {
                                test = 0;
                            } else {
                                test = Long.parseLong(snapshot.getValue().toString());
                            }
                            day_allTime += test;
                            conTime += test;
                        }
                        divide(day_allTime);
                        day_allTime = 0;
                        conTime += 0;
                    }
                }
            }

            if ((i != 1 || i != 2 || i != 3 || i != 4 || i != 5 || i != 6) && (dayOfWeek != 4 || dayOfWeek != 5)) {
                for (int j = k; j <= i; j++) {
                    for (DataSnapshot snapshot : dataSnapshot.child("aa").child("EEG DATA").child(mThisMonthCalendar.get(Calendar.YEAR) + "년")
                            .child(String.valueOf(mThisMonthCalendar.get(Calendar.MONTH) + 1 + "월")).child(String.valueOf(j + "일"))
                            .child("집중시간").getChildren()) {
                        if (snapshot.getValue().toString() == null) {
                            test = 0;
                        } else {
                            test = Long.parseLong(snapshot.getValue().toString());
                        }
                        day_allTime += test;
                        conTime += test;
                    }
                    divide(day_allTime);
                    day_allTime = 0;
                    conTime += 0;
                }
            }

            cmigrate = conTime;

            long mediHour2 = conTime / 1000 / 3600;
            long mediMin2 = (conTime / 1000) % 3600 / 60;
            long mediSec2 = ((conTime) / 1000) % 60;

            if (mediHour2 != 0) {
                cp_weekall.setText(mediHour2 + "시간 " + mediMin2 + "분 " + mediSec2 + "초");
                conTime = 0;
            } else if (mediMin2 != 0) {
                cp_weekall.setText(mediMin2 + "분 " + mediSec2 + "초");
                conTime = 0;
            } else
                cp_weekall.setText(mediSec2 + "초");
            conTime = 0;

        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    private void setCalendarTitle() {
        StringBuilder sb = new StringBuilder();

        sb.append(mThisMonthCalendar.get(Calendar.YEAR))
                .append("년 ")
                .append((mThisMonthCalendar.get(Calendar.MONTH) + 1))
                .append("월")
                .append((mThisMonthCalendar.get(Calendar.WEEK_OF_MONTH)))
                .append("주차");
        tvCalendarTitle.setText(sb.toString());
    }

    private void divide(Long time) {

        if (time != 0) {
            long hour = time / 1000 / 3600;
            long min = (time / 1000) % 3600 / 60;
            long sec = ((time) / 1000) % 60;

            if (hour != 0) {
                h = hour + "시간" + min + "분 " + sec + "초";
            } else if (min != 0) {
                h = min + "분 " + sec + "초";
            } else
                h = sec + "초";

            hours.add(h);
        } else {
            hours.add("");
        }
    }
}