package com.neurosky.algo_sdk_sample;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
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
import java.util.List;

public class DayFrag extends Fragment {

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference("USERS");

    private TextView tvCalendarTitle;
    //private TextView tvSelectedDate;

    private ArrayList<DayInfo> arrayListDayInfo;
    Calendar mThisMonthCalendar;
    WeekCalendarAdapter mCalendarAdapter;

    Date selectedDate;
    ProgressBar bar;
    TextView barPercent;

    View view;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.cp_dayfrag, container, false);

        Button btnPreviousCalendar = view.findViewById(R.id.w_previous_calendar);
        Button btnNextCalendar = view.findViewById(R.id.w_next_calendar);
        Button goToday = view.findViewById(R.id.wcptoday);
        tvCalendarTitle = view.findViewById(R.id.w_calendar_title);
        // tvSelectedDate = findViewById(R.id.tv_selected_date);
        TextView c_hour = view.findViewById(R.id.ws_hour); //몇시간 했는지 띄우는거 즉, 디비에서 불러온값을 달력하나에 띄우겟다는거임...어케하지?ㅠ;
        final TextView s_hour = view.findViewById(R.id.ws_hour);
        //  bar=(ProgressBar)view.findViewById(R.id.wprogressBar);//달성율을 프로그레스바로 표현해주려고
        // barPercent=view.findViewById(R.id.barPercent);//달성률 프로그레스바의 구체적 수치표현해주는거

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


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                ListView listview = (ListView) view.findViewById(R.id.listview_1);


                //데이터를 저장하게 되는 리스트
                List<String> list = new ArrayList<>();

                //리스트뷰와 리스트를 연결하기 위해 사용되는 어댑터
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(view.getContext(),
                        android.R.layout.simple_list_item_1, list);

                //리스트뷰의 어댑터를 지정해준다.
                listview.setAdapter(adapter);

                //for문을 이용해 msg라는 변수에 데이터 저장 후 list에 msg추가 반복
                for (DataSnapshot snapshot : dataSnapshot.child("aa").child("EEG DATA").child("2018년").child("08월").child("03일").child("21시").child("47분").getChildren()) {
                    String msg = snapshot.getValue().toString();

                    list.add(msg);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

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

        arrayListDayInfo.clear();
        setCalendarTitle();

        mCalendarAdapter = new WeekCalendarAdapter(arrayListDayInfo, selectedDate);


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