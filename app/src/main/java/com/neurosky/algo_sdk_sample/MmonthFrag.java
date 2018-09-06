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
import android.widget.TextView;

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

public class MmonthFrag extends Fragment {

    FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference("USERS");
    private DatabaseReference databaseReferences = firebaseDatabase.getReference("USERS");

    private TextView tvCalendarTitle;
    private GridView gvCalendar;
    TextView barPercent, mpm_all, mp_day;

    Calendar mThisMonthCalendar;
    CalendarAdapter mCalendarAdapter, mCalendarAdapter2;
    Date selectedDate;

    View view;

    String i, h = "", name = "";
    long mediTime, mediHour, allTime, day_allTime;
    int thisMonthLastDay;
    private int preSelected = -1;

    private ArrayList<DayInfo> arrayListDayInfo;
    private ArrayList<String> mHours = new ArrayList<>();

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

        view = inflater.inflate(R.layout.mp_monthfrag, container, false);
        mp_day = view.findViewById(R.id.mp_day);
        mpm_all = view.findViewById(R.id.mpm_all);

        Button btnPreviousCalendar = view.findViewById(R.id.mbtn_previous_calendar);
        Button btnNextCalendar = view.findViewById(R.id.mbtn_next_calendar);
        Button goToday = view.findViewById(R.id.mptoday1);

        tvCalendarTitle = view.findViewById(R.id.mtv_calendar_title);
        gvCalendar = view.findViewById(R.id.mgv_calendar);

        databaseReference.addValueEventListener(valueEventListener);

        goToday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mHours.clear();

                mThisMonthCalendar = Calendar.getInstance();
                getCalendar(mThisMonthCalendar.getTime());
                databaseReference.addValueEventListener(valueEventListener);
            }
        });

        btnPreviousCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mHours.clear();

                mThisMonthCalendar.add(Calendar.MONTH, -1);

                getCalendar(mThisMonthCalendar.getTime());
                databaseReference.addValueEventListener(valueEventListener);

            }
        });
        btnNextCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mHours.clear();

                mThisMonthCalendar.add(Calendar.MONTH, +1);

                getCalendar(mThisMonthCalendar.getTime());
                databaseReference.addValueEventListener(valueEventListener);
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
                        prevSelectedView.setSelected(false);
                        prevSelectedView.setBackgroundResource(R.drawable.bg_rect_border);
                    }

                    preSelected = position;
                }
            }
        });

        arrayListDayInfo = new ArrayList<>();
        return view;
    }

    ValueEventListener pListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            for (DataSnapshot snapshot : dataSnapshot.child(name).child("EEG DATA").child(mThisMonthCalendar.get(Calendar.YEAR) + "년")
                    .child(String.valueOf(mThisMonthCalendar.get(Calendar.MONTH) + 1 + "월")).child(String.valueOf(i + "일"))
                    .child("명상시간").getChildren()) {
                long test = Long.parseLong(snapshot.getValue().toString());
                mediTime += test;
            }

            mediHour = mediTime / 1000 / 3600;
            long mediMin = (mediTime / 1000) / 60;
            long mediSec = ((mediTime) / 1000) % 60;

            if (mediHour != 0) {
                mp_day.setText(mediHour + "시간 " + mediMin + "분 " + mediSec + "초");
                mediTime = 0;
            } else if (mediMin != 0) {
                mp_day.setText(mediMin + "분 " + mediSec + "초");
                mediTime = 0;
            } else
                mp_day.setText(mediSec + "초");
            mediTime = 0;
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    ValueEventListener valueEventListener = new ValueEventListener() {

        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            long test2;

            for (int z = 1; z < thisMonthLastDay + 1; z++) {
                for (DataSnapshot snapshot : dataSnapshot.child(name).child("EEG DATA").child(mThisMonthCalendar.get(Calendar.YEAR) + "년")
                        .child(String.valueOf(mThisMonthCalendar.get(Calendar.MONTH) + 1 + "월")).child(String.valueOf(z + "일"))
                        .child("명상시간").getChildren()) {
                    if (snapshot.getValue().toString() == null) {
                        test2 = 0;
                    } else {
                        test2 = Long.parseLong(snapshot.getValue().toString());
                    }
                    day_allTime += test2;
                    allTime += test2;
                }
                divide(day_allTime);
                day_allTime = 0;
            }

            long mediHour2 = allTime / 1000 / 3600;
            long mediMin2 = (allTime / 1000) / 60;
            long mediSec2 = ((allTime) / 1000) % 60;

            if (mediHour2 != 0) {
                mpm_all.setText(mediHour2 + "시간 " + mediMin2 + "분 " + mediSec2 + "초");
                allTime = 0;
            } else if (mediMin2 != 0) {
                mpm_all.setText(mediMin2 + "분 " + mediSec2 + "초");
                allTime = 0;
            } else
                mpm_all.setText(mediSec2 + "초");
            allTime = 0;

        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

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
                mCalendarAdapter.setData(mHours);
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
                h = hour + "시간 " + min + "분 " + sec + "초";
            } else if (min != 0) {
                h = min + "분 " + sec + "초";
            } else
                h = sec + "초";

            mHours.add(h);
        } else {
            mHours.add("");
        }
    }
}