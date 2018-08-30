package com.neurosky.algo_sdk_sample;

import android.app.Fragment;

import java.util.ArrayList;
import java.util.Calendar;

import android.graphics.Color;
import android.os.Bundle;
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
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;

public class MweekFrag extends Fragment { //명상 주별 과거

    private TextView tvCalendarTitle;
    private GridView gvCalendar;
    private LineChart mChart;           //mChart 라는 LineChart를 선언해준다.

    String i;
    long mediTime;
    private int preSelected = -1;

    DayInfo day;
    View view;
    TextView mp_week, mp_weekall;

    private ArrayList<DayInfo> arrayListDayInfo;
    Calendar mThisMonthCalendar;
    WeekCalendarAdapter mCalendarAdapter;

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference("USERS");
    private DatabaseReference database = firebaseDatabase.getReference("USERS"); //주 총시간

    Date selectedDate;
    ProgressBar bar;
    TextView barPercent;

    public void setSelectedDate(Date date) {
        selectedDate = date;

        if (mCalendarAdapter != null) {
            mCalendarAdapter.selectedDate = date;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.mp_weekfrag, container, false);

        mp_week = view.findViewById(R.id.mp_week);
        mp_weekall = view.findViewById(R.id.mweekT); //주 총시간

        Button btnPreviousCalendar = view.findViewById(R.id.mw_previous_calendar); //과거 명상 주별 이라 mv
        Button btnNextCalendar = view.findViewById(R.id.mw_next_calendar);
        Button goToday = view.findViewById(R.id.mwptoday);  //주별 명상 past

        tvCalendarTitle = view.findViewById(R.id.mw_calendar_title);
        gvCalendar = view.findViewById(R.id.mw_gv_calendar);

        mChart = view.findViewById(R.id.mwChart);
        mChart.setDragEnabled(true);

        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setEnabled(false);                //오른쪽에 y축의 데이터를 표시하지 않음.

        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);          //x축 값을 밑에 표시.
        xAxis.setDrawGridLines(false);                  //x축의 그리드라인을 없앰.

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setDrawGridLines(false);               //y축의 그리드라인을 없앰.
        ArrayList<Entry> yValues = new ArrayList<>();
        // ArrayList<Entry> xValues = new ArrayList<>();
        //ArrayList<Entry> zValues = new ArrayList<>();           //각 그래프 별 데이터를 입력받을 배열을 만듦.

        //x축을 일월화수목금토 할거임 숫자를->한글로바꾸는거 알아보기

        yValues.add(new Entry(1, 60));
        yValues.add(new Entry(2, 50));
        yValues.add(new Entry(3, 70));
        yValues.add(new Entry(4, 30));
        yValues.add(new Entry(5, 50));
        yValues.add(new Entry(6, 60));
        yValues.add(new Entry(7, 20));              //Alpha 값 입력

        LineDataSet set1 = new LineDataSet(yValues, "Alpha");

        set1.setColor(Color.RED);
        // set2.setColor(Color.BLUE);
        //set3.setColor(Color.GREEN);                 //그래프 별 색 지정


        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);
        //   dataSets.add(set2);
        //  dataSets.add(set3);                         //3개의 그래프를 하나의 dataSet이란 배열로 만듦

        LineData data = new LineData(dataSets);     //dataSet이란 배열을 data로 선언
        data.setDrawValues(true);                  //data의 수치를 그래프 위에 나타내지 않기.
        mChart.setData(data);                       //mChart를 이용하여 data 표시

        ///////////////////////database.addValueEventListener(weekListener);
        goToday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mThisMonthCalendar = Calendar.getInstance();
                database.addValueEventListener(weekListener);
                getCalendar(mThisMonthCalendar.getTime());
            }
        });

        btnPreviousCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mThisMonthCalendar.add(Calendar.WEEK_OF_MONTH, -1);
                database.addValueEventListener(weekListener);
                getCalendar(mThisMonthCalendar.getTime());
            }
        });
        btnNextCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mThisMonthCalendar.add(Calendar.WEEK_OF_MONTH, +1);
                database.addValueEventListener(weekListener);
                getCalendar(mThisMonthCalendar.getTime());
            }
        });
        database.addValueEventListener(weekListener);
        gvCalendar.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            ValueEventListener pListener = new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.child("aa").child("EEG DATA").child(mThisMonthCalendar.get(Calendar.YEAR) + "년")
                            .child(String.valueOf(mThisMonthCalendar.get(Calendar.MONTH) + 1 + "월")).child(String.valueOf(i + "일"))
                            .child("명상시간").getChildren()) {
                        long test = Long.parseLong(snapshot.getValue().toString());
                        mediTime += test;
                    }

                    long mediHour = mediTime / 1000 / 3600;
                    long mediMin = (mediTime / 1000) / 60;
                    long mediSec = ((mediTime) / 1000) % 60;

                    Log.d("아아테스트", mediTime / 1000 / 3600 + "시간" + (mediTime / 1000) / 60 + "분  " + mediTime + "는메디타임" + "medihour은" + mediHour);

                    if (mediHour == 0) {
                        mp_week.setText(mediMin + "분 " + mediSec + "초");
                        mediTime = 0;
                    } else if (mediHour != 0 && mediMin == 0) {
                        mp_week.setText(mediHour + "시간" + mediMin + "분 " + mediSec + "초");
                        mediTime = 0;
                    } else
                        mp_week.setText(mediHour + "시간" + mediMin + "분 " + mediSec + "초");
                    mediTime = 0;
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            };


            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) { //디비에서 누른날짜에 해당되는 값을 찾아야함'

                setSelectedDate(((DayInfo) view.getTag()).getDate());
                DayInfo day = arrayListDayInfo.get(position);
                i = day.getDay();

                databaseReference.addValueEventListener(pListener);

                view.setBackgroundColor(Color.YELLOW);
                View prevSelectedView = adapterView.getChildAt(preSelected);

                if (preSelected != -1) {
                    //prevSelectedView.setClickable(false);
                    prevSelectedView.setSelected(false);
                    prevSelectedView.setBackgroundResource(R.drawable.bg_rect_border);
                }

                preSelected = position;
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

    Calendar calendar = Calendar.getInstance();

    private void getCalendar(Date dateForCurrentMonth) {
        int dayOfWeek, thisWeekLastDay;

        arrayListDayInfo.clear();

        calendar.setTime(dateForCurrentMonth);
        dayOfWeek = calendar.get(Calendar.WEEK_OF_MONTH);
        calendar.set(Calendar.DATE, dayOfWeek);//1일로 변경--->오늘날짜로..

        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        thisWeekLastDay = calendar.getActualMaximum(Calendar.DAY_OF_WEEK);
        database.addValueEventListener(weekListener);
        setCalendarTitle();

        for (int i = 1; i <= thisWeekLastDay; i++) {
            day = new DayInfo();
            day.setDate(calendar.getTime());
            day.setInMonth(true);

            arrayListDayInfo.add(day);

            calendar.add(Calendar.DATE, +1);
        }

        mCalendarAdapter = new WeekCalendarAdapter(arrayListDayInfo, selectedDate);
        gvCalendar.setAdapter(mCalendarAdapter);

    }

    //주별용 주총 시간 구할겨
    ValueEventListener weekListener = new ValueEventListener() {

        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            long test;
            int i = Integer.parseInt(day.getDay());
            int k = i - 7;

            for (int j = k; j <= i; j++) {
                for (DataSnapshot snapshot : dataSnapshot.child("aa").child("EEG DATA").child(mThisMonthCalendar.get(Calendar.YEAR) + "년")
                        .child(String.valueOf(mThisMonthCalendar.get(Calendar.MONTH) + 1 + "월")).child(String.valueOf(j + "일"))
                        .child("명상시간").getChildren()) {
                    if (snapshot.getValue().toString() == null) {
                        test = 0;
                    } else {
                        test = Long.parseLong(snapshot.getValue().toString());
                    }
                    mediTime += test;
                }
                mediTime += 0;
            }


            long mediHour2 = mediTime / 1000 / 3600;
            long mediMin2 = (mediTime / 1000) / 60;
            long mediSec2 = ((mediTime) / 1000) % 60;

            if (mediHour2 != 0) {
                mp_weekall.setText(mediHour2 + "시간 " + mediMin2 + "분 " + mediSec2 + "초");

                mediTime = 0;
            } else if (mediMin2 != 0) {
                mp_weekall.setText(mediMin2 + "분 " + mediSec2 + "초");
                mediTime = 0;
            } else
                mp_weekall.setText(mediSec2 + "초");
            mediTime = 0;

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
}