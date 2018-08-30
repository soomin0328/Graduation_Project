package com.neurosky.algo_sdk_sample;

import android.app.Fragment;
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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MonthFrag extends Fragment {

    private TextView tvCalendarTitle;
    private GridView gvCalendar;

    private ArrayList<DayInfo> arrayListDayInfo;
    private ArrayList<String> hours = new ArrayList<>();

    Calendar mThisMonthCalendar;
    CalendarAdapter mCalendarAdapter, mCalendarAdapter2;

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference("USERS");//월 전체 계산용
    private DatabaseReference databaseReferences = firebaseDatabase.getReference("USERS"); //하루계산용
    private DatabaseReference databaseRefer = firebaseDatabase.getReference("USERS");//달성율
    private DatabaseReference databaseReferences2 = firebaseDatabase.getReference("USERS");

    private int preSelected = -1;
    String dayAim_per, i, h = "";
    long conTime, conHour, c_allTime, day_allTime, migrate;
    int thisMonthLastDay, month_Aim; //한달 전체 달성울

    View view;
    TextView cpm_all, barPercent, aimPer, c_hour, cp_day; //달력의 하루하루
    Date selectedDate;
    ProgressBar bar;

    public void setSelectedDate(Date date) {
        selectedDate = date;

        if (mCalendarAdapter != null) {
            mCalendarAdapter.selectedDate = date;
        }
    }

    //달력에서 특정 날짜 눌렀을 때 하루 집중시간 구하기
    ValueEventListener pListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            for (DataSnapshot snapshot : dataSnapshot.child("aa").child("EEG DATA").child(mThisMonthCalendar.get(Calendar.YEAR) + "년")
                    .child(String.valueOf(mThisMonthCalendar.get(Calendar.MONTH) + 1 + "월")).child(String.valueOf(i + "일"))
                    .child("집중시간").getChildren()) {
                long test = Long.parseLong(snapshot.getValue().toString());
                conTime += test;
            }

            conHour = conTime / 1000 / 3600;
            long conMin = (conTime / 1000) % 3600 / 60;
            long conSec = ((conTime) / 1000) % 60;

            if (conHour != 0) {
                cp_day.setText(conHour + "시간 " + conMin + "분 " + conSec + "초");
                conTime = 0;
            } else if (conMin != 0) {
                cp_day.setText(conMin + "분 " + conSec + "초");
                conTime = 0;
            } else
                cp_day.setText(conSec + "초");
            conTime = 0;

        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    //한 달 총 집중시간(c_allTime) & 하루 총 집중시간(day_allTime) 구하기
    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            long test2;

            for (int z = 1; z < thisMonthLastDay + 1; z++) {
                for (DataSnapshot snapshot : dataSnapshot.child("aa").child("EEG DATA").child(mThisMonthCalendar.get(Calendar.YEAR) + "년")
                        .child(String.valueOf(mThisMonthCalendar.get(Calendar.MONTH) + 1 + "월")).child(String.valueOf(z + "일"))
                        .child("집중시간").getChildren()) {
                    if (snapshot.getValue().toString() == null) {
                        test2 = 0;
                    } else {
                        test2 = Long.parseLong(snapshot.getValue().toString());
                    }
                    day_allTime += test2;
                    c_allTime += test2;
                }
                divide(day_allTime);
                day_allTime = 0;
            }

            migrate = c_allTime; //다른데서 쓰려고 옮겨줌 아래에서 0으로해버리거든.

            long mediHour2 = c_allTime / 1000 / 3600;
            long mediMin2 = (c_allTime / 1000) % 3600 / 60;
            long mediSec2 = ((c_allTime) / 1000) % 60;

            if (mediHour2 != 0) {
                cpm_all.setText(mediHour2 + "시간 " + mediMin2 + "분 " + mediSec2 + "초");
                c_allTime = 0;
            } else if (mediMin2 != 0) {
                cpm_all.setText(mediMin2 + "분 " + mediSec2 + "초");
                c_allTime = 0;
            } else
                cpm_all.setText(mediSec2 + "초");
            c_allTime = 0;

        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    //하루 달성율 구하기
    ValueEventListener dayAimListener = new ValueEventListener() {

        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            for (DataSnapshot snapshot : dataSnapshot.child("aa").child("EEG DATA").child(mThisMonthCalendar.get(Calendar.YEAR) + "년")
                    .child(String.valueOf(mThisMonthCalendar.get(Calendar.MONTH) + 1 + "월")).child(String.valueOf(i + "일"))
                    .child("하루달성율").getChildren()) {
                dayAim_per = (snapshot.getValue().toString());
            }
            // barPercent.setText(String.valueOf(day_aim)); //숫자표현

            if (dayAim_per == null) {
                dayAim_per = "0";
                aimPer.setText(dayAim_per + "%");

            } else {
                aimPer.setText(dayAim_per + "%");
                // bar.setProgress(day_aim);
            }
            dayAim_per = "0";
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    //한달 퍼센트 구하기
    ValueEventListener percentListener = databaseReference.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            int testValue;

            for (int k = 1; k < thisMonthLastDay + 1; k++) {
                for (DataSnapshot snapshot : dataSnapshot.child("aa").child("EEG DATA").child(mThisMonthCalendar.get(Calendar.YEAR) + "년")
                        .child(String.valueOf(mThisMonthCalendar.get(Calendar.MONTH) + 1 + "월")).child(String.valueOf(k + "일"))
                        .child("목표시간").getChildren()) {
                    if (snapshot.getValue().toString() == null) {
                        testValue = 0;
                    } else {
                        testValue = Integer.parseInt(snapshot.getValue().toString());

                    }
                    month_Aim += testValue;
                }
            }

            long migrate2 = migrate;
            long month_Aim2 = month_Aim;
            long monthPer;

            // monthPer=migrate2 / month_Aim2;

            if (month_Aim2 == 0 || migrate2 == 0 || migrate == 0 || month_Aim == 0) {
                bar.setProgress(0);
                barPercent.setText("0");
            } else {
                long imValue = ((migrate2) / (month_Aim2 / 10L) * 10L);
                bar.setProgress((int) imValue);
                barPercent.setText(imValue + "");
                month_Aim2 = 0;
                month_Aim = 0;
            }

        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    });

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        hours.clear();

        view = inflater.inflate(R.layout.cp_monthfrag, container, false);
        cp_day = view.findViewById(R.id.mp_day); //하루에 집중한 시간
        cpm_all = view.findViewById(R.id.mpm_all); //그달 전체 집중시간
        aimPer = view.findViewById(R.id.aimPer); //하루 달성율

        Button btnPreviousCalendar = view.findViewById(R.id.mbtn_previous_calendar);
        Button btnNextCalendar = view.findViewById(R.id.mbtn_next_calendar);
        Button goToday = view.findViewById(R.id.mptoday1);

        tvCalendarTitle = view.findViewById(R.id.mtv_calendar_title);
        gvCalendar = view.findViewById(R.id.mgv_calendar);

        bar = (ProgressBar) view.findViewById(R.id.progressBar);
        barPercent = view.findViewById(R.id.barPercent);

        databaseReference.addValueEventListener(valueEventListener);
        databaseReferences2.addValueEventListener(percentListener);

        goToday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hours.clear();

                mThisMonthCalendar = Calendar.getInstance();
                getCalendar(mThisMonthCalendar.getTime());
                databaseReference.addValueEventListener(valueEventListener);
                databaseReferences2.addValueEventListener(percentListener);
            }
        });

        btnPreviousCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hours.clear();

                mThisMonthCalendar.add(Calendar.MONTH, -1);

                getCalendar(mThisMonthCalendar.getTime());
                databaseReference.addValueEventListener(valueEventListener);
                databaseReferences2.addValueEventListener(percentListener);
            }
        });
        btnNextCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hours.clear();

                mThisMonthCalendar.add(Calendar.MONTH, +1);

                getCalendar(mThisMonthCalendar.getTime());
                databaseReference.addValueEventListener(valueEventListener);
                databaseReferences2.addValueEventListener(percentListener);
            }
        });

        gvCalendar.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            DayInfo day;

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                setSelectedDate(((DayInfo) view.getTag()).getDate());
                day = arrayListDayInfo.get(position);
                i = day.getDay();
                if (day.isInMonth()) {
                    databaseReferences.addValueEventListener(pListener);
                    view.setBackgroundColor(Color.YELLOW);
                    View prevSelectedView = adapterView.getChildAt(preSelected);

                    if (preSelected != -1) {
                        //prevSelectedView.setClickable(false);
                        prevSelectedView.setSelected(false);
                        prevSelectedView.setBackgroundResource(R.drawable.bg_rect_border);
                    }

                    preSelected = position;

                    final long a = mThisMonthCalendar.get(Calendar.YEAR);
                    final long b = (mThisMonthCalendar.get(Calendar.MONTH) + 1);
                }


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

        arrayListDayInfo.clear();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateForCurrentMonth);

        calendar.set(Calendar.DATE, 1);//1일로 변경
        dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);//1일의 요일 구하기

        if (dayOfWeek == Calendar.SUNDAY) {//현재 달의 1일이 무슨 요일인지 검사
            dayOfWeek += 7;
        }

        thisMonthLastDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        setCalendarTitle();

        DayInfo day;

        calendar.add(Calendar.DATE, -1 * (dayOfWeek - 1)); //현재 달력화면에서 보이는 지난달의 시작일

        for (int i = 0; i < dayOfWeek - 1; i++) {
            day = new DayInfo();
            day.setDate(calendar.getTime());
            day.setInMonth(false);
            arrayListDayInfo.add(day);

            calendar.add(Calendar.DATE, +1);
        }

        for (int i = 1; i <= thisMonthLastDay; i++) {
            day = new DayInfo();
            day.setDate(calendar.getTime());
            day.setInMonth(true);
            arrayListDayInfo.add(day);

            calendar.add(Calendar.DATE, +1);
        }

        for (int i = 1; i < 42 - (thisMonthLastDay + dayOfWeek - 1) + 1; i++) {
            day = new DayInfo();
            day.setDate(calendar.getTime());
            day.setInMonth(false);
            arrayListDayInfo.add(day);

            calendar.add(Calendar.DATE, +1);
        }

        mCalendarAdapter2 = new CalendarAdapter(arrayListDayInfo, selectedDate);
        gvCalendar.setAdapter(mCalendarAdapter2);

        Handler m = new Handler(Looper.getMainLooper());
        m.postDelayed(new Runnable() {
            @Override
            public void run() {
                mCalendarAdapter = new CalendarAdapter(arrayListDayInfo, selectedDate);
                gvCalendar.setAdapter(mCalendarAdapter);
                mCalendarAdapter.setData(hours);
            }
        }, 4000);

    }

    private void setCalendarTitle() {
        StringBuilder sb = new StringBuilder();

        sb.append(mThisMonthCalendar.get(Calendar.YEAR))
                .append("년 ")
                .append((mThisMonthCalendar.get(Calendar.MONTH) + 1))
                .append("월");
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