package com.neurosky.algo_sdk_sample;

import android.app.Fragment;
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
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MmonthFrag extends Fragment {

    private TextView tvCalendarTitle;
    //private TextView tvSelectedDate;
    private GridView gvCalendar;
    //디비
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference("USERS");
    private DatabaseReference databaseReferences = firebaseDatabase.getReference("USERS");

    String i;
    long mediTime;
    long mediHour;
    long allTime;
    int thisMonthLastDay;
    private ArrayList<DayInfo> arrayListDayInfo;
    Calendar mThisMonthCalendar;

    CalendarAdapter mCalendarAdapter;
    // TextView mpm_all;
    Date selectedDate;

    TextView barPercent;

    public MmonthFrag() {
    }

    public void setSelectedDate(Date date) {
        selectedDate = date;

        if (mCalendarAdapter != null) {
            mCalendarAdapter.selectedDate = date;
        }
    }

    View view;
    TextView mpm_all;
    TextView mp_day;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        //inflater를 사용해 프래그먼트에 사용할 레이아웃 불러오고 리턴
        //해당 프래그먼트에 대한 기능적코드 여기에 넣으래
//..?
        // GridView gridview=(GridView)view.findViewById(R.id.gridview);

        view = inflater.inflate(R.layout.mp_monthfrag, container, false);
        mp_day = view.findViewById(R.id.mp_day);
        mpm_all = view.findViewById(R.id.mpm_all);
        //final TextView mpm_all=view.findViewById(R.id.mpm_all);

        Button btnPreviousCalendar = view.findViewById(R.id.mbtn_previous_calendar);
        Button btnNextCalendar = view.findViewById(R.id.mbtn_next_calendar);
        Button goToday = view.findViewById(R.id.mptoday1);
        tvCalendarTitle = view.findViewById(R.id.mtv_calendar_title);
        // tvSelectedDate = findViewById(R.id.tv_selected_date);
        gvCalendar = view.findViewById(R.id.mgv_calendar);
        // TextView c_hour=view.findViewById(R.id.c_hour); //몇시간 했는지 띄우는거 즉, 디비에서 불러온값을 달력하나에 띄우겟다는거임...어케하지?ㅠ;
        //final TextView s_hour=view.findViewById(R.id.s_hour);
        /////   bar=(ProgressBar)view.findViewById(R.id.mprogressBar);//달성율을 프로그레스바로 표현해주려고
        ////// barPercent=view.findViewById(R.id.mbarPercent);//달성률 프로그레스바의 구체적 수치표현해주는거
        goToday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mThisMonthCalendar = Calendar.getInstance();
                getCalendar(mThisMonthCalendar.getTime());
                databaseReference.addValueEventListener(valueEventListener);
            }
        });

        btnPreviousCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mThisMonthCalendar.add(Calendar.MONTH, -1);

                getCalendar(mThisMonthCalendar.getTime());
                databaseReference.addValueEventListener(valueEventListener);

            }
        });
        btnNextCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mThisMonthCalendar.add(Calendar.MONTH, +1);

                getCalendar(mThisMonthCalendar.getTime());
                databaseReference.addValueEventListener(valueEventListener);
            }
        });

        gvCalendar.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            DayInfo day;

            // String i;
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                setSelectedDate(((DayInfo) view.getTag()).getDate());
                day = arrayListDayInfo.get(position);
                i = day.getDay();

                if (day.isInMonth()) {
                    databaseReferences.addValueEventListener(pListener);

                    //누른 날짜가 2018/ 7/ 13이면 이거가져와서 디비에서 찾고 거기값 가져와야할듯 여긴 포지션만 되지않나 아닌가..흠


                    final long a = mThisMonthCalendar.get(Calendar.YEAR);
                    final long b = (mThisMonthCalendar.get(Calendar.MONTH) + 1);
                    Log.d("mmonth test", a + "년" + b + "월" + i + "일");
                }
            }
        });


        arrayListDayInfo = new ArrayList<>();
        return view;
    }

    ValueEventListener pListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            for (DataSnapshot snapshot : dataSnapshot.child("aa").child("EEG DATA").child(mThisMonthCalendar.get(Calendar.YEAR) + "년").child(String.valueOf(mThisMonthCalendar.get(Calendar.MONTH) + 1 + "월")).child(String.valueOf(i + "일")).child("명상시간").getChildren()) {
                Log.d("값오니", "" + snapshot.getValue());
                long test = Long.parseLong(snapshot.getValue().toString());
                mediTime += test;
                Log.d("값 합보기", "" + mediTime);
            }
            mediHour = mediTime / 1000 / 3600;
            long mediMin = (mediTime / 1000) / 60;
            long mediSec = ((mediTime) / 1000) % 60;

            if (mediHour == 0) {
                mp_day.setText(mediMin + "분 " + mediSec + "초");
                mediTime = 0;
            } else if (mediHour != 0 && mediMin == 0) {
                mp_day.setText(mediHour + "시간" + mediMin + "분 " + mediSec + "초");
                mediTime = 0;
            } else
                mp_day.setText(mediSec + "초");
            mediTime = 0;
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    ValueEventListener valueEventListener = databaseReference.addValueEventListener(new ValueEventListener() {

        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            long test2;
            for (int z = 1; z < thisMonthLastDay; z++) {
                for (DataSnapshot snapshot : dataSnapshot.child("aa").child("EEG DATA").child(mThisMonthCalendar.get(Calendar.YEAR) + "년").child(String.valueOf(mThisMonthCalendar.get(Calendar.MONTH) + 1 + "월")).child(String.valueOf(z + "일")).child("명상시간").getChildren()) {
                    Log.d("test22", snapshot.getValue().toString() + "");
                    if (snapshot.getValue().toString() == null) {
                        test2 = 0;
                    } else {
                        test2 = Long.parseLong(snapshot.getValue().toString());
                    }

                    allTime += test2;
                    Log.d("시간값 합친거", allTime + "");
                }
            }
            long mediHour2 = allTime / 1000 / 3600;
            long mediMin2 = (allTime / 1000) / 60;
            long mediSec2 = ((allTime) / 1000) % 60;
            Log.d("시간값", mediHour2 + "시간 " + mediMin2 + "분 " + mediSec2 + "초");
            if (mediHour2 == 0) {
                mpm_all.setText(mediMin2 + "분 " + mediSec2 + "초");
                allTime = 0;
            } else if (mediHour2 != 0 && mediMin2 == 0) {
                mpm_all.setText(mediHour2 + "시간" + mediMin2 + "분 " + mediSec2 + "초");
                allTime = 0;
            } else
                mpm_all.setText(mediSec2 + "초");
            allTime = 0;

        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    });

    public void onResume() {
        super.onResume();

        mThisMonthCalendar = Calendar.getInstance();
        getCalendar(mThisMonthCalendar.getTime());
        databaseReference.addValueEventListener(valueEventListener);
        /*ValueEventListener valueEventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            TextView mpm_all=view.findViewById(R.id.mpm_all);
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long test2;
                for (int z = 1; z < thisMonthLastDay; z++) {
                    for (DataSnapshot snapshot : dataSnapshot.child("aa").child("EEG DATA").child(mThisMonthCalendar.get(Calendar.YEAR) + "년").child(String.valueOf(mThisMonthCalendar.get(Calendar.MONTH) + 1 + "월")).child(String.valueOf(z + "일")).child("명상시간").getChildren()) {
                        Log.d("test22",snapshot.getValue().toString()+"");
                        if(snapshot.getValue().toString()==null){
                            test2=0;
                        }
                        else {
                            test2 = Long.parseLong(snapshot.getValue().toString());
                        }

                        allTime+=test2;
                        Log.d("시간값 합친거",allTime+"");
                    }
                }
                long mediHour2=allTime/1000/3600;
                long mediMin2=(allTime/1000)/60;
                long mediSec2=((allTime)/1000)%60;
                Log.d("시간값",mediHour2+"시간 "+mediMin2+"분 "+mediSec2+"초");
                if(mediHour2==0){
                    mpm_all.setText(mediMin2+"분 "+mediSec2+"초");
                    allTime=0;
                }
                else if(mediHour2!=0 && mediMin2==0){
                    mpm_all.setText(mediHour2+"시간"+mediMin2+"분 "+mediSec2+"초");
                    allTime=0;
                }
                else
                    mpm_all.setText(mediSec2+"초");
                allTime=0;

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/

    }

    private void getCalendar(Date dateForCurrentMonth) {
        int dayOfWeek;


        arrayListDayInfo.clear();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateForCurrentMonth);

        calendar.set(Calendar.DATE, 1);//1일로 변경
        dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);//1일의 요일 구하기
        Log.d("CalendarTest", "dayOfWeek = " + dayOfWeek + "");

        if (dayOfWeek == Calendar.SUNDAY) {//현재 달의 1일이 무슨 요일인지 검사
            Log.d("현재 달 1일 무슨 요일", dayOfWeek + "");
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

        mCalendarAdapter = new CalendarAdapter(arrayListDayInfo, selectedDate);
        gvCalendar.setAdapter(mCalendarAdapter);

        // tvSelectedDate.setText(sdf.format(selectedDate));
    }

    private void setCalendarTitle() {
        StringBuilder sb = new StringBuilder();

        sb.append(mThisMonthCalendar.get(Calendar.YEAR))
                .append("년 ")
                .append((mThisMonthCalendar.get(Calendar.MONTH) + 1))
                .append("월");
        tvCalendarTitle.setText(sb.toString());
    }
}



