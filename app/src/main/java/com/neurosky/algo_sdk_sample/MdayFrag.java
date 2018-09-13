package com.neurosky.algo_sdk_sample;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
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
import java.util.List;

public class MdayFrag extends Fragment {

    FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference("USERS");
    private DatabaseReference databaseReference2 = firebaseDatabase.getReference("USERS");

    private TextView tvCalendarTitle;

    private ArrayList<DayInfo> arrayListDayInfo;
    Calendar mThisMonthCalendar;

    View view;

    TextView ClickTime;
    TextView DTT;
    ListView time1List; //명상시간대
    ListView time2List; //측정시간

    String name = "";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        String email = user.getEmail();

        int idx = email.indexOf("@");
        name = email.substring(0, idx);

        final ValueEventListener valueEventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                // 측정시간 리스트 데이터
                final List<String> list2 = new ArrayList<>();

                ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(view.getContext(),
                        android.R.layout.simple_list_item_1, list2);

                time2List.setAdapter(adapter2);

                //명상시간대 리스트 데이터
                final List<String> list1 = new ArrayList<>();

                ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(view.getContext(),
                        android.R.layout.simple_list_item_1, list1);

                time1List.setAdapter(adapter1);

                // 명상 총 시간
                long totalTime = 0;

                for (DataSnapshot snapshot : dataSnapshot
                        .child(name)
                        .child("EEG DATA")
                        .child(mThisMonthCalendar.get(Calendar.YEAR) + "년년")
                        .child(String.valueOf(mThisMonthCalendar.get(Calendar.MONTH) + 1 + "월"))
                        .child(String.valueOf(mThisMonthCalendar.get(Calendar.DAY_OF_MONTH) + "일"))
                        .getChildren()) {

                    String msg = snapshot.getKey().toString();

                    //for문을 이용해 msg라는 long형 변수 저장(하위 목록의 개수:getChildrenCount이용), 이상한 점은 바로직후가 아닌 그 다음 목록이 저장됨.
                    for (DataSnapshot snapshot2 : dataSnapshot
                            .child(name)
                            .child("EEG DATA")
                            .child(mThisMonthCalendar.get(Calendar.YEAR) + "년년")
                            .child(String.valueOf(mThisMonthCalendar.get(Calendar.MONTH) + 1 + "월"))
                            .child(String.valueOf(mThisMonthCalendar.get(Calendar.DAY_OF_MONTH) + "일"))
                            .child(String.valueOf(msg))
                            .child("명상시간")
                            .getChildren()) {

                        long test = Long.parseLong(snapshot2.getValue().toString());
                        long DHour;

                        DHour = test / 1000 / 3600;
                        long DMin = (test / 1000) % 3600 / 60;
                        long DSec = ((test) / 1000) % 60;

                        if (DHour == 0 && DMin == 0) {
                            list2.add(msg);
                            list1.add(DSec + "초");
                        } else if (DHour == 0 && DMin != 0) {
                            list2.add(msg);
                            list1.add(DMin + "분 " + DSec + "초");
                        } else if (DHour != 0) {
                            list2.add(msg);
                            list1.add(DHour + "시간 " + DMin + "분 " + DSec + "초");
                        }


                        totalTime += test;
                    }

                    long DTTHour;

                    DTTHour = totalTime / 1000 / 3600;
                    long DTTMin = (totalTime / 1000) % 3600 / 60;
                    long DTTSec = ((totalTime) / 1000) % 60;

                    if (DTTHour == 0 && DTTMin == 0) {
                        DTT.setText(DTTSec + "초");
                    } else if (DTTHour == 0 && DTTMin != 0) {
                        DTT.setText(DTTMin + "분 " + DTTSec + "초");
                    } else if (DTTHour != 0) {
                        DTT.setText(DTTHour + "시간 " + DTTMin + "분 " + DTTSec + "초");
                    }

                }

                setData(totalTime, list1.size());

                time2List.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        ClickTime.setText(list1.get(position));
//                        //클릭한 아이템의 문자열을 가져옴
//                        final String selected_item = (String)parent.getItemAtPosition(position);

//                        long msg2=Long.parseLong(snapshot.getValue().toString());
//
//                        long DHour1;
//
//                        DHour1=msg2/1000/3600;
//                        long DMin1=(msg2/1000)%3600/60;
//                        long DSec1=((msg2)/1000)%60;
//
//                        if(DHour1==0 && DMin1==0){
//                            ClickTime.setText(DSec1+"초");
//                        }
//                        else if(DHour1==0 && DMin1!=0){
//                            ClickTime.setText(DMin1 + "분 " + DSec1 + "초");
//                        }
//                        else if(DHour1!=0){
//                            ClickTime.setText(DHour1+"시간 "+DMin1+"분 "+DSec1+"초");
//                        }

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        view = inflater.inflate(R.layout.mp_dayfrag, container, false);

        Button btnPreviousCalendar = view.findViewById(R.id.md_previous_calendar);
        Button btnNextCalendar = view.findViewById(R.id.md_next_calendar);
        Button goToday = view.findViewById(R.id.mdptoday);

        tvCalendarTitle = view.findViewById(R.id.md_calendar_title);
        ClickTime = view.findViewById(R.id.ws_hour);
        DTT = view.findViewById(R.id.daytotaltime);
        time1List = view.findViewById(R.id.listview_1);
        time2List = view.findViewById(R.id.listview_2);

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
                mThisMonthCalendar.add(Calendar.DAY_OF_MONTH, -1);

                getCalendar(mThisMonthCalendar.getTime());

                databaseReference.addValueEventListener(valueEventListener);
            }
        });
        btnNextCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mThisMonthCalendar.add(Calendar.DAY_OF_MONTH, +1);

                getCalendar(mThisMonthCalendar.getTime());

                databaseReference.addValueEventListener(valueEventListener);
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


    private void setData(long msg5, int size) {
        if (msg5 == 0 && size == 0) {
            DTT.setText("-");
            ClickTime.setText("-");
        } else {
            ClickTime.setText("-");
        }
    }

}