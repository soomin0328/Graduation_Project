package com.neurosky.algo_sdk_sample;

import android.app.Fragment;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
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

public class MdayFrag extends Fragment {
    String newmonth = "", newday = "";

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference("USERS");

    private TextView tvCalendarTitle;

    private ArrayList<DayInfo> arrayListDayInfo;
    Calendar mThisMonthCalendar;

    View view;
    TextView ClickTime;
    TextView DTT;
    ListView time1List; //명상시간대
    ListView time2List; //측정시간

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {


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
                newmonth = newCal(mThisMonthCalendar.get(Calendar.MONTH) + 1);
                newday = newCal(mThisMonthCalendar.get(Calendar.DAY_OF_MONTH));
                for (DataSnapshot snapshot : dataSnapshot
                        .child("aa")
                        .child("EEG DATA")
                        .child(mThisMonthCalendar.get(Calendar.YEAR) + "년")
                        .child(String.valueOf(newmonth + "월"))
                        .child(String.valueOf(newday + "일"))
                        .child("명상시작시간")
                        .getChildren()) {
                    String msg = snapshot.getValue().toString();
                    list2.add(msg);
                }
                for (DataSnapshot snapshot2 : dataSnapshot
                        .child("aa")
                        .child("EEG DATA")
                        .child(mThisMonthCalendar.get(Calendar.YEAR) + "년")
                        .child(String.valueOf(newmonth + "월"))
                        .child(String.valueOf(newday + "일"))
                        .child("명상시간")
                        .getChildren()) {
                    long test = Long.parseLong(snapshot2.getValue().toString());
                    long DHour;
                    DHour = test / 1000 / 3600;
                    long DMin = (test / 1000) % 3600 / 60;
                    long DSec = ((test) / 1000) % 60;
                    if (DHour == 0 && DMin == 0) {
                        list1.add(DSec + "초");
                    } else if (DHour == 0 && DMin != 0) {
                        list1.add(DMin + "분 " + DSec + "초");
                    } else if (DHour != 0) {
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
                setData(totalTime, list1.size());
                time2List.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        ClickTime.setText(list1.get(position));

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        view = inflater.inflate(R.layout.mp_dayfrag, container, false);
        Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "fonts/nanum.ttf");
        Button btnPreviousCalendar = view.findViewById(R.id.md_previous_calendar);
        Button btnNextCalendar = view.findViewById(R.id.md_next_calendar);
        Button goToday = view.findViewById(R.id.mdptoday);
        goToday.setTypeface(tf);
        tvCalendarTitle = view.findViewById(R.id.md_calendar_title);
        tvCalendarTitle.setTypeface(tf);
        //  tvCalendarTitle.setTypeface(Typeface.createFromAsset(this.getAssets(),"nanum.ttf");
        ClickTime = view.findViewById(R.id.ws_hour);
        ClickTime.setTypeface(tf);
        DTT = view.findViewById(R.id.daytotaltime);
        DTT.setTypeface(tf);

        TextView textView5 = view.findViewById(R.id.textView5);
        textView5.setTypeface(tf);
        TextView textView7 = view.findViewById(R.id.textView7);
        textView7.setTypeface(tf);
        TextView textView13 = view.findViewById(R.id.textView13);
        textView13.setTypeface(tf);
        TextView tvTime = view.findViewById(R.id.Tv_time);
        tvTime.setTypeface(tf);
        TextView tvState = view.findViewById(R.id.Tv_state);
        tvState.setTypeface(tf);


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

        /*if(dayOfWeek == Calendar.SUNDAY){//현재 달의 1일이 무슨 요일인지 검사
            Log.d("현재 달 1일 무슨 요일",dayOfWeek+"");
            dayOfWeek += 7;
        }*/
        thisWeekLastDay = calendar.getActualMaximum(Calendar.DAY_OF_WEEK);
        setCalendarTitle();
        DayInfo day;
        //여기 아래부터
        calendar.add(Calendar.DATE, -1 * (dayOfWeek - 1)); //현재 달력화면에서 보이는 지난달의 시작일
        for (int i = 0; i < dayOfWeek - 1; i++) {
            day = new DayInfo();
            day.setDate(calendar.getTime());
            day.setInMonth(true);
            calendar.add(Calendar.DATE, +1);
        }
//여기까지 지우면 오늘기준날짜부터 일주일간격 날짜로 나옴.

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

    private String newCal(int cal) {
        String str = String.valueOf(cal);
        if (str.length() == 1)
            return "0" + str;
        else
            return str;
    }
}