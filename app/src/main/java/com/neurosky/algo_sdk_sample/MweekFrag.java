package com.neurosky.algo_sdk_sample;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.mikephil.charting.charts.ScatterChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.ScatterData;
import com.github.mikephil.charting.data.ScatterDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MweekFrag extends Fragment { //명상 주별 과거

    private TextView tvCalendarTitle;
    private GridView gvCalendar;
    private ScatterChart scChart;

    String i, h = "", name = "", newmonth = "", newday = "", newmonth2 = "";
    long mediTime, Mday_allTime;
    private int preSelected = -1;

    int[] countArray = new int[24];
    int realCount = 0;

    DayInfo day;
    View view;
    TextView mp_week, mp_weekall;

    private ArrayList<DayInfo> arrayListDayInfo;
    private ArrayList<String> Mhours = new ArrayList<>();
    private ArrayList<Entry> entries = new ArrayList<>(); //주별 명상도 그래프
    final ArrayList<String> labels = new ArrayList<String>();

    Calendar mThisMonthCalendar;
    WeekCalendarAdapter mCalendarAdapter, mCalendarAdapter2;

    FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference("USERS");
    private DatabaseReference database = firebaseDatabase.getReference("USERS"); //주 총시간
    private DatabaseReference databaseGraph = firebaseDatabase.getReference("USERS"); //그래프

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

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        String email = user.getEmail();

        int idx = email.indexOf("@");
        name = email.substring(0, idx);

        Mhours.clear();

        view = inflater.inflate(R.layout.mp_weekfrag, container, false);

        mp_week = view.findViewById(R.id.mp_week);
        mp_weekall = view.findViewById(R.id.mweekT); //주 총시간

        Button btnPreviousCalendar = view.findViewById(R.id.mw_previous_calendar); //과거 명상 주별 이라 mv
        Button btnNextCalendar = view.findViewById(R.id.mw_next_calendar);
        Button goToday = view.findViewById(R.id.mwptoday);  //주별 명상 past

        tvCalendarTitle = view.findViewById(R.id.mw_calendar_title);
        gvCalendar = view.findViewById(R.id.mw_gv_calendar);

        scChart = view.findViewById(R.id.mwChart);
        scChart.setDragEnabled(false);


        XAxis xAxis = scChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        YAxis leftAxis = scChart.getAxisLeft();

        leftAxis.setAxisMinimum(0);
        leftAxis.setAxisMaximum(24);
        labels.add("일");
        labels.add("월");
        labels.add("화");
        labels.add("수");
        labels.add("목");
        labels.add("금");
        labels.add("토");

        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {

                return labels.get((int) value);

            }

            public int getDecimalDigits() {
                return 0;
            }
        });

        YAxis rightAxis = scChart.getAxisRight();
        rightAxis.setEnabled(false);

        entries.add(new Entry(0, -1));    //x축에서 0은 일요일 1시에 점을찍어라
        entries.add(new Entry(1, -1));   //x축에서 1은 월요일    2시에점을찍어라
        entries.add(new Entry(2, -1));   ////x축에서 2은 화요일  3시에 점을찍어라
        entries.add(new Entry(3, -1));   ////x축에서 3은 수요일  20시에점을찍어
        entries.add(new Entry(4, -1));    //x축에서 4은 목요일

        entries.add(new Entry(5, -1));   //x축에서 5은 금요일
        entries.add(new Entry(6, -1));

        //6은 토요일*/
        /// /y축에서 23이 최대임..


        databaseGraph.addValueEventListener(graphPoint);
        ScatterDataSet dataset = new ScatterDataSet(entries, "집중 시간대"); //이 데이타셋
        dataset.setScatterShape(ScatterChart.ScatterShape.CIRCLE);
        dataset.setColors(Color.TRANSPARENT);

        ScatterData data = new ScatterData(dataset);
        scChart.setData(data);

        goToday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Mhours.clear();
                databaseGraph.addValueEventListener(graphPoint);
                mThisMonthCalendar = Calendar.getInstance();
                database.addValueEventListener(weekListener);
                getCalendar(mThisMonthCalendar.getTime());
            }
        });

        btnPreviousCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Mhours.clear();
                entries.clear();
                graphChange();
                databaseGraph.addValueEventListener(graphPoint);
                mThisMonthCalendar.add(Calendar.WEEK_OF_MONTH, -1);
                database.addValueEventListener(weekListener);
                getCalendar(mThisMonthCalendar.getTime());
            }
        });
        btnNextCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Mhours.clear();
                entries.clear();
                graphChange();
                databaseGraph.addValueEventListener(graphPoint);
                mThisMonthCalendar.add(Calendar.WEEK_OF_MONTH, +1);
                database.addValueEventListener(weekListener);
                getCalendar(mThisMonthCalendar.getTime());
            }
        });

        gvCalendar.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            ValueEventListener pListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    newmonth = newCal(mThisMonthCalendar.get(Calendar.MONTH) + 1);
                    newday = newCal(Integer.parseInt(i));

                    for (DataSnapshot snapshot : dataSnapshot.child(name).child("EEG DATA").child(mThisMonthCalendar.get(Calendar.YEAR) + "년")
                            .child(newmonth + "월").child(String.valueOf(newday + "일"))
                            .child("명상시간").getChildren()) {
                        long test = Long.parseLong(snapshot.getValue().toString());
                        mediTime += test;
                    }

                    long mediHour = mediTime / 1000 / 3600;
                    long mediMin = (mediTime / 1000) % 3600 / 60;
                    long mediSec = ((mediTime) / 1000) % 60;

                    if (mediHour != 0) {
                        mp_week.setText(mediHour + "시간" + mediMin + "분 " + mediSec + "초");
                        mediTime = 0;
                    } else if (mediMin != 0) {
                        mp_week.setText(mediMin + "분 " + mediSec + "초");
                        mediTime = 0;
                    } else
                        mp_week.setText(mediSec + "초");
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
    int dayOfWeek, thisWeekLastDay;

    private void getCalendar(Date dateForCurrentMonth) {

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

        mCalendarAdapter2 = new WeekCalendarAdapter(arrayListDayInfo, selectedDate);
        gvCalendar.setAdapter(mCalendarAdapter2);

        Handler m = new Handler(Looper.getMainLooper());
        m.postDelayed(new Runnable() {
            @Override
            public void run() {
                mCalendarAdapter = new WeekCalendarAdapter(arrayListDayInfo, selectedDate);
                gvCalendar.setAdapter(mCalendarAdapter);
                mCalendarAdapter.setData(Mhours);
            }
        }, 4000);

    }

    //명상도 그래프를 위한 디비
    ValueEventListener graphPoint = new ValueEventListener() { //그래프에 점 띄울거.
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) { //c_allTime이 식 위에

            realCount = 0;
            int i = Integer.parseInt(day.getDay());
            for (int t = 0; t < 24; t++) {
                // Log.d("test what1",""+t); //작동됨
                countArray[t] = 0;
            }
            //24개의 배열방 0시~23시
            int t_Value = 0;
            String s1 = String.valueOf(mThisMonthCalendar.get(Calendar.MONTH) + 1);

            if (s1.length() == 1) {
                s1 = String.valueOf("0" + (mThisMonthCalendar.get(Calendar.MONTH) + 1) + "월");
            }


            int k = i - 6;
            {
                String changValue;
                //Log.d("test what 2",i+""+(String.valueOf("0"+(mThisMonthCalendar.get(Calendar.MONTH)+ 1) + "월"))+String.valueOf(0+6+ "일"));
                for (int j = k; j <= i; j++) {
                    if (String.valueOf(j).length() == 1) {
                        changValue = "0" + j;
                    } else {
                        changValue = String.valueOf(j);
                    }
                    ++realCount;
                    for (DataSnapshot snapshot : dataSnapshot.child(name).child("EEG DATA").child(String.valueOf(mThisMonthCalendar.get(Calendar.YEAR) + "년"))
                            .child(s1).child(String.valueOf(changValue + "일")).getChildren()) {

                        String db_Value = snapshot.getKey().toString(); //시
                        if (db_Value != null) {
                            for (int e = 0; e <= 59; e++) {
                                //for(int t=0 ; t <= 59 ; t++) {
                                String db_focus = String.valueOf(snapshot.child(e + "분").getValue());
                                if (db_focus != null) { //분이 존재하면 초단위로 내려감
                                    for (int y = 0; y <= 59; y++) {
                                        String db_focus2 = String.valueOf(snapshot.child(e + "분").child(y + "초").child("명상도").getValue());
                                        if (db_focus2.equals("null")) {
                                            db_focus2 = "0";
                                        } else {
                                            int changeValue = Integer.parseInt(db_focus2);
                                            if (changeValue >= 50) {
                                                String[] token = db_Value.split("시");

                                                for (String t : token) {
                                                    t_Value = Integer.parseInt(t);

                                                }
                                                countArray[t_Value] += 1;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    for (int r = 0; r < 24; r++) {
                        if (countArray[r] >= 30) { //각 시간대 배열에 집중도 높았던 순간이15번 이상이면
                            entries.add(new Entry(realCount - 1, r)); //realCount의 값은 최소 0부터 6까지로 0은 일요일 자리 1은 월요일 2는 화요일자리 3은 수요일자리 이렇게 나간다.

                            // r은 시간대. 카운트배열의 자리값은 시간과같아서 그 시간대의 값이 15(번) 이상이면 점찍기.
                        } else {

                        }
                    }

                    for (int t = 0; t < 24; t++) {
                        // Log.d("test what1",""+t); //작동됨
                        countArray[t] = 0;
                    }

                    ScatterDataSet dataset2 = new ScatterDataSet(entries, "집중 시간대");
                    dataset2.setScatterShape(ScatterChart.ScatterShape.CIRCLE);
                    // dataset2.setValueTextSize(10);
                    dataset2.setValueTextSize(0);
                    dataset2.setScatterShapeSize(24);
                    ScatterData data2 = new ScatterData(dataset2);
                    dataset2.setColors(Color.RED);
                    scChart.setData(data2);
                    scChart.animateY(3000);

                }


            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    //주별용 주총 시간 구할겨
    ValueEventListener weekListener = new ValueEventListener() {

        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            long test;
            int i = Integer.parseInt(day.getDay());
            int k = i - 6;
            newmonth = newCal(mThisMonthCalendar.get(Calendar.MONTH) + 1);
            newmonth2 = newCal(mThisMonthCalendar.get(Calendar.MONTH));
            if ((i == 1 || i == 2 || i == 3 || i == 4 || i == 5 || i == 6) && (dayOfWeek == 4 || dayOfWeek == 5)) {
                int month = mThisMonthCalendar.get(Calendar.MONTH) + 1;
                if (month == 1 || month == 3 || month == 5 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12) {
                    for (int j = 31 - (6 - i); j <= 31; j++) {
                        newday = newCal(j);
                        for (DataSnapshot snapshot : dataSnapshot.child(name).child("EEG DATA").child(mThisMonthCalendar.get(Calendar.YEAR) + "년")
                                .child(newmonth + "월").child(newday + "일")
                                .child("명상시간").getChildren()) {
                            if (snapshot.getValue().toString() == null) {
                                test = 0;
                            } else {
                                test = Long.parseLong(snapshot.getValue().toString());
                            }
                            Mday_allTime += test;
                            mediTime += test;
                        }
                        divide(Mday_allTime);
                        Mday_allTime = 0;
                        mediTime += 0;
                    }
                } else if (month == 4 || month == 6 || month == 9 || month == 11) {
                    for (int j = 30 - (6 - i); j <= 30; j++) {
                        newday = newCal(j);
                        for (DataSnapshot snapshot : dataSnapshot.child(name).child("EEG DATA").child(mThisMonthCalendar.get(Calendar.YEAR) + "년")
                                .child(newmonth2 + "월").child(newday + "일")
                                .child("명상시간").getChildren()) {
                            if (snapshot.getValue().toString() == null) {
                                test = 0;
                            } else {
                                test = Long.parseLong(snapshot.getValue().toString());
                            }
                            Mday_allTime += test;
                            mediTime += test;
                        }
                        divide(Mday_allTime);
                        Mday_allTime = 0;
                        mediTime += 0;
                    }
                } else {
                    for (int j = 28 - (6 - i); j <= 28; j++) {
                        newday = newCal(j);
                        for (DataSnapshot snapshot : dataSnapshot.child(name).child("EEG DATA").child(mThisMonthCalendar.get(Calendar.YEAR) + "년")
                                .child(newmonth2 + "월").child(newday + "일")
                                .child("명상시간").getChildren()) {
                            if (snapshot.getValue().toString() == null) {
                                test = 0;
                            } else {
                                test = Long.parseLong(snapshot.getValue().toString());
                            }
                            Mday_allTime += test;
                            mediTime += test;
                        }
                        divide(Mday_allTime);
                        Mday_allTime = 0;
                        mediTime += 0;
                    }
                }
            }

            if ((i != 1 || i != 2 || i != 3 || i != 4 || i != 5 || i != 6) && (dayOfWeek != 4 || dayOfWeek != 5)) {
                for (int j = k; j <= i; j++) {
                    for (DataSnapshot snapshot : dataSnapshot.child(name).child("EEG DATA").child(mThisMonthCalendar.get(Calendar.YEAR) + "년")
                            .child(newmonth + "월").child(newday + "일")
                            .child("명상시간").getChildren()) {
                        if (snapshot.getValue().toString() == null) {
                            test = 0;
                        } else {
                            test = Long.parseLong(snapshot.getValue().toString());
                        }
                        Mday_allTime += test;
                        mediTime += test;
                    }
                    divide(Mday_allTime);
                    Mday_allTime = 0;
                    mediTime += 0;
                }
            }

            long mediHour2 = mediTime / 1000 / 3600;
            long mediMin2 = (mediTime / 1000) % 3600 / 60;
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

            Mhours.add(h);
        } else {
            Mhours.add("");
        }
    }

    private void graphChange() {

        XAxis xAxis = scChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        labels.add("일");
        labels.add("월");
        labels.add("화");
        labels.add("수");
        labels.add("목");
        labels.add("금");
        labels.add("토");

        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {

                return labels.get((int) value);

            }

            public int getDecimalDigits() {
                return 0;
            }
        });

        YAxis rightAxis = scChart.getAxisRight();
        rightAxis.setEnabled(false);

        scChart.setDragEnabled(false);
        entries.add(new Entry(0, -1));    //x축에서 0은 일요일 1시에 점을찍어라
        entries.add(new Entry(1, -1));   //x축에서 1은 월요일    2시에점을찍어라
        entries.add(new Entry(2, -1));   ////x축에서 2은 화요일  3시에 점을찍어라
        entries.add(new Entry(3, -1));   ////x축에서 3은 수요일  20시에점을찍어
        entries.add(new Entry(4, -1));    //x축에서 4은 목요일

        entries.add(new Entry(5, -1));   //x축에서 5은 금요일
        entries.add(new Entry(6, -1));

    }

    private String newCal(int cal) {

        String str = String.valueOf(cal);

        if (str.length() == 1)
            return "0" + str;
        else
            return str;
    }

}