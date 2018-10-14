package com.neurosky.algo_sdk_sample;

import android.app.Fragment;
import android.graphics.Color;
import android.graphics.Typeface;
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

public class MonthFrag extends Fragment {

    private TextView tvCalendarTitle;
    private GridView gvCalendar;

    private ArrayList<DayInfo> arrayListDayInfo;
    private ArrayList<String> hours = new ArrayList<>();

    Calendar mThisMonthCalendar;
    CalendarAdapter mCalendarAdapter, mCalendarAdapter2;

    FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference("USERS");

    private int preSelected = -1;
    String dayAim_per, i, h = "", name = "";
    long conTime, conHour, c_allTime, day_allTime, migrate, migrate2, month_Aim2;
    int thisMonthLastDay, month_Aim;    //month_Aim: 한달 전체 달성울
    String newmonth = "", newday = "";

    View view;
    TextView cpm_all, barPercent, aimPer, c_hour, cp_day;
    Date selectedDate;
    ProgressBar bar;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        String email = user.getEmail();

        int idx = email.indexOf("@");
        name = email.substring(0, idx);

        hours.clear();

        view = inflater.inflate(R.layout.cp_monthfrag, container, false);
        Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "fonts/nanum.ttf");
        cp_day = view.findViewById(R.id.mp_day); //하루에 집중한 시간
        cp_day.setTypeface(tf);
        cpm_all = view.findViewById(R.id.mpm_all); //그달 전체 집중시간
        cpm_all.setTypeface(tf);
        aimPer = view.findViewById(R.id.aimPer); //하루 달성율
        aimPer.setTypeface(tf);
        TextView TextView12 = view.findViewById(R.id.textView12);
        TextView12.setTypeface(tf);
        TextView TextView6 = view.findViewById(R.id.textView6);
        TextView6.setTypeface(tf);
        TextView tv2 = view.findViewById(R.id.tv2);
        tv2.setTypeface(tf);
        TextView tv = view.findViewById(R.id.tv);
        tv.setTypeface(tf);
        TextView textView8 = view.findViewById(R.id.textView8);
        textView8.setTypeface(tf);

        Button btnPreviousCalendar = view.findViewById(R.id.mbtn_previous_calendar);
        Button btnNextCalendar = view.findViewById(R.id.mbtn_next_calendar);
        Button goToday = view.findViewById(R.id.mptoday1);
        goToday.setTypeface(tf);
        tvCalendarTitle = view.findViewById(R.id.mtv_calendar_title);
        tvCalendarTitle.setTypeface(tf);
        gvCalendar = view.findViewById(R.id.mgv_calendar);

        bar = (ProgressBar) view.findViewById(R.id.progressBar);
        barPercent = view.findViewById(R.id.barPercent);
        barPercent.setTypeface(tf);

        databaseReference.addValueEventListener(valueEventListener);
        databaseReference.addValueEventListener(percentListener);

        goToday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hours.clear();

                mThisMonthCalendar = Calendar.getInstance();
                getCalendar(mThisMonthCalendar.getTime());
                databaseReference.addValueEventListener(valueEventListener);
                databaseReference.addValueEventListener(percentListener);
            }
        });

        btnPreviousCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hours.clear();

                mThisMonthCalendar.add(Calendar.MONTH, -1);

                getCalendar(mThisMonthCalendar.getTime());
                databaseReference.addValueEventListener(valueEventListener);
                databaseReference.addValueEventListener(percentListener);
            }
        });
        btnNextCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hours.clear();

                mThisMonthCalendar.add(Calendar.MONTH, +1);

                getCalendar(mThisMonthCalendar.getTime());
                databaseReference.addValueEventListener(valueEventListener);
                databaseReference.addValueEventListener(percentListener);
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
                    databaseReference.addValueEventListener(pListener);
                    databaseReference.addValueEventListener(dayAimListener);
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

    public void onResume() {
        super.onResume();

        mThisMonthCalendar = Calendar.getInstance();
        getCalendar(mThisMonthCalendar.getTime());
    }

    //달력에서 특정 날짜 눌렀을 때 하루 집중시간 구하기
    ValueEventListener pListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            newmonth = newCal(mThisMonthCalendar.get(Calendar.MONTH) + 1);
            newday = newCal(Integer.parseInt(i));

            for (DataSnapshot snapshot : dataSnapshot.child(name).child("EEG DATA").child(mThisMonthCalendar.get(Calendar.YEAR) + "년")
                    .child(String.valueOf(newmonth + "월")).child(newday + "일")
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

            newmonth = newCal(mThisMonthCalendar.get(Calendar.MONTH) + 1);

            for (int z = 1; z < thisMonthLastDay + 1; z++) {
                newday = newCal(z);

                for (DataSnapshot snapshot : dataSnapshot.child(name).child("EEG DATA").child(mThisMonthCalendar.get(Calendar.YEAR) + "년")
                        .child(newmonth + "월").child(newday + "일")
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

            newmonth = newCal(mThisMonthCalendar.get(Calendar.MONTH) + 1);
            newday = newCal(Integer.parseInt(i));

            for (DataSnapshot snapshot : dataSnapshot.child(name).child("EEG DATA").child(mThisMonthCalendar.get(Calendar.YEAR) + "년")
                    .child(newmonth + "월").child(newday + "일")
                    .child("하루달성율").getChildren()) {
                dayAim_per = (snapshot.getValue().toString());
            }

            if (dayAim_per == null) {
                dayAim_per = "0";
                aimPer.setText(dayAim_per + "%");

            } else {
                aimPer.setText(dayAim_per + "%");
            }
            dayAim_per = "0";
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    //한 달 퍼센트 구하기
    ValueEventListener percentListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            int testValue;

            newmonth = newCal(mThisMonthCalendar.get(Calendar.MONTH) + 1);

            for (int k = 1; k < thisMonthLastDay + 1; k++) {
                newday = newCal(k);

                for (DataSnapshot snapshot : dataSnapshot.child(name).child("EEG DATA").child(mThisMonthCalendar.get(Calendar.YEAR) + "년")
                        .child(newmonth + "월").child(newday + "일")
                        .child("목표시간").getChildren()) {
                    if (snapshot.getValue().toString() == null) {
                        testValue = 0;
                    } else {
                        testValue = Integer.parseInt(snapshot.getValue().toString());

                    }
                    month_Aim += testValue;
                }
            }

            migrate2 = migrate; //집중시간
            month_Aim2 = month_Aim; //묙표 시간

            if (month_Aim2 == 0 || migrate2 == 0 || migrate == 0 || month_Aim == 0) {
                bar.setProgress(0);
                barPercent.setText("0");
            } else {
                double imValue = ((double) migrate2 / (double) month_Aim2) * 100;

                bar.setProgress((int) imValue);
                if ((int) imValue > 100) {
                    barPercent.setText("100%");
                } else {
                    barPercent.setText((int) imValue + "%");
                }
                month_Aim = 0;
                migrate2 = 0;
                month_Aim2 = 0;
            }
            month_Aim = 0;
            migrate2 = 0;
            month_Aim2 = 0;
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    //요일들 구하기 & 화면 띄우기
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

        //달력 화면을 띄움
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

    //현재 선택한 날짜
    public void setSelectedDate(Date date) {
        selectedDate = date;

        if (mCalendarAdapter != null) {
            mCalendarAdapter.selectedDate = date;
        }
    }

    private void setCalendarTitle() {
        StringBuilder sb = new StringBuilder();

        sb.append(mThisMonthCalendar.get(Calendar.YEAR))
                .append("년 ")
                .append((mThisMonthCalendar.get(Calendar.MONTH) + 1))
                .append("월");
        tvCalendarTitle.setText(sb.toString());
    }

    //Firebase에서 얻은 하루 총 공부 시간을 시간/분/초로 나눈 뒤, CalendarAdapter에 넘기기위해 hours라는 ArrayList에 넣음
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

    private String newCal(int cal) {

        String str = String.valueOf(cal);

        if (str.length() == 1)
            return "0" + str;
        else
            return str;
    }

}