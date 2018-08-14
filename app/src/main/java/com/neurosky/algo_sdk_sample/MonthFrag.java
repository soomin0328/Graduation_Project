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
    //private TextView tvSelectedDate;
    private GridView gvCalendar;

    private ArrayList<DayInfo> arrayListDayInfo;
    Calendar mThisMonthCalendar;
    CalendarAdapter mCalendarAdapter;

    //디비
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference("USERS"); //월 전체 계산용
    private DatabaseReference databaseReferences = firebaseDatabase.getReference("USERS"); //하루계산용
    private DatabaseReference databaseRefer = firebaseDatabase.getReference("USERS");//달성율
    private DatabaseReference databaseReferences2 = firebaseDatabase.getReference("USERS");
    String dayAim_per;
    String i;
    long conTime;
    long conHour;
    long c_allTime;
    int thisMonthLastDay;
    int month_Aim; //한달 전체 달성울

    long migrate; //걍 값 이동시켜주는거
    Date selectedDate;
    ProgressBar bar;
    TextView barPercent;
    TextView aimPer;

    public void setSelectedDate(Date date) {
        selectedDate = date;

        if (mCalendarAdapter != null) {
            mCalendarAdapter.selectedDate = date;
        }
    }

    View view;
    TextView cpm_all;
    TextView cp_day; //달력의 하루하루

    ValueEventListener pListener = new ValueEventListener() { //달력에 하루하루 클릭시 databaseReferences꺼
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            for (DataSnapshot snapshot : dataSnapshot.child("aa").child("EEG DATA").child(mThisMonthCalendar.get(Calendar.YEAR) + "년").child(String.valueOf(mThisMonthCalendar.get(Calendar.MONTH) + 1 + "월")).child(String.valueOf(i + "일")).child("집중시간").getChildren()) {
                Log.d("값오니", "" + snapshot.getValue());
                long test = Long.parseLong(snapshot.getValue().toString());
                conTime += test;
                Log.d("값 합보기", "" + conTime);
            }
            conHour = conTime / 1000 / 3600;
            long conMin = (conTime / 1000) / 60;
            long conSec = ((conTime) / 1000) % 60;

            if (conHour == 0) {
                cp_day.setText(conMin + "분 " + conSec + "초");
                conTime = 0;
            } else if (conHour != 0 && conMin == 0) {
                cp_day.setText(conHour + "시간" + conMin + "분 " + conSec + "초");
                conTime = 0;
            } else
                cp_day.setText(conSec + "초");
            conTime = 0;


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
                for (DataSnapshot snapshot : dataSnapshot.child("aa").child("EEG DATA").child(mThisMonthCalendar.get(Calendar.YEAR) + "년").child(String.valueOf(mThisMonthCalendar.get(Calendar.MONTH) + 1 + "월")).child(String.valueOf(z + "일")).child("집중시간").getChildren()) {
                    Log.d("test22", snapshot.getValue().toString() + "");
                    if (snapshot.getValue().toString() == null) {
                        test2 = 0;
                    } else {
                        test2 = Long.parseLong(snapshot.getValue().toString());
                    }

                    c_allTime += test2;
                    Log.d("시간값 합친거", c_allTime + "");
                }
            }
            migrate = c_allTime; //다른데서 쓰려고 옮겨줌 아래에서 0으로해버리거든.

            long mediHour2 = c_allTime / 1000 / 3600;
            long mediMin2 = (c_allTime / 1000) / 60;
            long mediSec2 = ((c_allTime) / 1000) % 60;
            Log.d("시간값", mediHour2 + "시간 " + mediMin2 + "분 " + mediSec2 + "초");
            if (mediHour2 == 0) {
                cpm_all.setText(mediMin2 + "분 " + mediSec2 + "초");
                c_allTime = 0;
            } else if (mediHour2 != 0 && mediMin2 == 0) {
                cpm_all.setText(mediHour2 + "시간" + mediMin2 + "분 " + mediSec2 + "초");
                c_allTime = 0;
            } else
                cpm_all.setText(mediSec2 + "초");
            c_allTime = 0;

        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    });

    ValueEventListener dayAimListener = new ValueEventListener() {

        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            for (DataSnapshot snapshot : dataSnapshot.child("aa").child("EEG DATA").child(mThisMonthCalendar.get(Calendar.YEAR) + "년").child(String.valueOf(mThisMonthCalendar.get(Calendar.MONTH) + 1 + "월")).child(String.valueOf(i + "일")).child("하루달성율").getChildren())

            {
                Log.d("하루달성율 집중", snapshot.getValue() + "");
                dayAim_per = (snapshot.getValue().toString());
            }
            // barPercent.setText(String.valueOf(day_aim)); //숫자표현

            if (dayAim_per == null) {
                Log.d("하루 달성율집중", dayAim_per + "");
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

    ValueEventListener percentListener = databaseReference.addValueEventListener(new ValueEventListener() { //한달동안의 전체 퍼센트
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) { //c_allTime이 식 위에
            int testValue;
            for (int k = 1; k < thisMonthLastDay; k++) {
                for (DataSnapshot snapshot : dataSnapshot.child("aa").child("EEG DATA").child(mThisMonthCalendar.get(Calendar.YEAR) + "년").child(String.valueOf(mThisMonthCalendar.get(Calendar.MONTH) + 1 + "월")).child(String.valueOf(k + "일")).child("목표시간").getChildren()) {
                    Log.d("test33", snapshot.getValue().toString() + "");
                    if (snapshot.getValue().toString() == null) {
                        testValue = 0;
                    } else {
                        testValue = Integer.parseInt(snapshot.getValue().toString());

                    }
                    month_Aim += testValue; //목표했던시간 다 불러와서 더해
                    Log.d("시간값 합친거2", month_Aim + "");
                }
            }
            long migrate2 = migrate;
            long month_Aim2 = month_Aim;
            long monthPer;
            Log.d("바바", migrate2 + "랑  " + month_Aim2);
            // monthPer=migrate2 / month_Aim2;
            // Log.d("monthPer이 ",monthPer+"바바바바  "+migrate+"은ss c_allTime이고    "+month_Aim+"는 month_aim");
            Log.d("바바", migrate2 / 1L + "는 마이그레이트  " + month_Aim2 / 1L + "는 목표시간");


            if (month_Aim2 == 0 || migrate2 == 0 || migrate == 0 || month_Aim == 0) {
                bar.setProgress(0);
                barPercent.setText("0");
            }

            //  Log.d("바바바 계산값바바",((migrate2)/(month_Aim2/10L)*10L)+"///"+"는 imValue값");
            else {
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
        //inflater를 사용해 프래그먼트에 사용할 레이아웃 불러오고 리턴
        //해당 프래그먼트에 대한 기능적코드 여기에 넣으래
//..?
        // GridView gridview=(GridView)view.findViewById(R.id.gridview);

        view = inflater.inflate(R.layout.cp_monthfrag, container, false);
        cp_day = view.findViewById(R.id.s_hour); //하루에 집중한 시간
        cpm_all = view.findViewById(R.id.cpm_all); //그달 전체 집중시간
        aimPer = view.findViewById(R.id.aimPer); //하루 달성율

        Button btnPreviousCalendar = view.findViewById(R.id.btn_previous_calendar);
        Button btnNextCalendar = view.findViewById(R.id.btn_next_calendar);
        Button goToday = view.findViewById(R.id.cptoday);
        tvCalendarTitle = view.findViewById(R.id.tv_calendar_title);
        // tvSelectedDate = findViewById(R.id.tv_selected_date);
        gvCalendar = view.findViewById(R.id.gv_calendar);
        TextView c_hour = view.findViewById(R.id.c_hour); //몇시간 했는지 띄우는거 즉, 디비에서 불러온값을 달력하나에 띄우겟다는거임...어케하지?ㅠ;

        bar = (ProgressBar) view.findViewById(R.id.progressBar);//달성율을 프로그레스바로 표현해주려고
        barPercent = view.findViewById(R.id.barPercent);//달성률 프로그레스바의 구체적 수치표현해주는거

        goToday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mThisMonthCalendar = Calendar.getInstance();
                getCalendar(mThisMonthCalendar.getTime());
                databaseReference.addValueEventListener(valueEventListener);
                databaseReferences2.addValueEventListener(percentListener);
            }
        });

        databaseReference.addValueEventListener(valueEventListener);
        databaseReferences2.addValueEventListener(percentListener);
        // databaseReferences2.addValueEventListener(percentListener);

        btnPreviousCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mThisMonthCalendar.add(Calendar.MONTH, -1);

                getCalendar(mThisMonthCalendar.getTime());
                databaseReference.addValueEventListener(valueEventListener);
                databaseReferences2.addValueEventListener(percentListener);
            }
        });
        btnNextCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mThisMonthCalendar.add(Calendar.MONTH, +1);

                getCalendar(mThisMonthCalendar.getTime());
                databaseReference.addValueEventListener(valueEventListener);
                databaseReferences2.addValueEventListener(percentListener);
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
                    databaseRefer.addValueEventListener(dayAimListener);
                    databaseReferences.addValueEventListener(pListener);

                    final long a = mThisMonthCalendar.get(Calendar.YEAR);
                    final long b = (mThisMonthCalendar.get(Calendar.MONTH) + 1);
                    Log.d("cmonth test", a + "년" + b + "월" + i + "일");
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



