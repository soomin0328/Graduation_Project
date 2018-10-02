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
    String newmonth = "", newday = "";

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference("USERS");
    private DatabaseReference databaseReference2 = firebaseDatabase.getReference("USERS");
    private DatabaseReference databaseReference3 = firebaseDatabase.getReference("USERS");

    private TextView tvCalendarTitle;
    //private TextView tvSelectedDate;
    private ArrayList<DayInfo> arrayListDayInfo;
    Calendar mThisMonthCalendar;
    WeekCalendarAdapter mCalendarAdapter;
    Date selectedDate;
    ProgressBar bar;
    TextView barPercent;

    public void setSelectedDate(Date date) {
        selectedDate = date;
        if (mCalendarAdapter != null) {
            mCalendarAdapter.selectedDate = date;
        }
    }

    View view;
    TextView ClickHour;
    TextView ClickPercent;
    TextView DTT;
    ValueEventListener valueEventListener2;
    ValueEventListener valueEventListener3;

    @Nullable
    @Override
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
            // arrayListDayInfo.add(day);
            calendar.add(Calendar.DATE, +1);
        }
//여기까지 지우면 오늘기준날짜부터 일주일간격 날짜로 나옴.
        for (int i = 1; i <= thisWeekLastDay; i++) {
            day = new DayInfo();
            day.setDate(calendar.getTime());
            day.setInMonth(true);
            arrayListDayInfo.add(day);
            calendar.add(Calendar.DATE, +1);
        }
        /*for(int i=1; i<thisMonthLastDay+1; i++) {
            day = new DayInfo();
            day.setDate(calendar.getTime());
            day.setInMonth(false);
            arrayListDayInfo.add(day);
            calendar.add(Calendar.DATE, +1);
        }*/
        mCalendarAdapter = new WeekCalendarAdapter(arrayListDayInfo, selectedDate);
        // tvSelectedDate.setText(sdf.format(selectedDate));
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

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final ValueEventListener valueEventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DTT = view.findViewById(R.id.daytotaltime);
                //집중 시간대 : "시 + 분"
                ListView listview2 = (ListView) view.findViewById(R.id.listview_2);
                List<String> list2 = new ArrayList<>();
                ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(view.getContext(),
                        android.R.layout.simple_list_item_1, list2);
                listview2.setAdapter(adapter2);
                // 시간 count
                ListView listview1 = (ListView) view.findViewById(R.id.listview_1);
                //데이터를 저장하게 되는 리스트
                List<String> list1 = new ArrayList<>();
                //리스트뷰와 리스트를 연결하기 위해 사용되는 어댑터
                ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(view.getContext(),
                        android.R.layout.simple_list_item_1, list1);
                //리스트뷰의 어댑터를 지정해준다.
                listview1.setAdapter(adapter1);
                long msg4 = 0;
                long msg5 = 0;
                newmonth = newCal(mThisMonthCalendar.get(Calendar.MONTH) + 1);
                newday = newCal(mThisMonthCalendar.get(Calendar.DAY_OF_MONTH));
                for (DataSnapshot snapshot : dataSnapshot
                        .child("aa")
                        .child("EEG DATA")
                        .child(mThisMonthCalendar.get(Calendar.YEAR) + "년")
                        .child(String.valueOf(newmonth + "월"))
                        .child(String.valueOf(newday + "일"))
                        .child("집중시작시간")
                        .getChildren()) {
                    String msg = snapshot.getValue().toString();
                    list2.add(msg);
                }
                for (DataSnapshot snapshot : dataSnapshot
                        .child("aa")
                        .child("EEG DATA")
                        .child(mThisMonthCalendar.get(Calendar.YEAR) + "년")
                        .child(String.valueOf(newmonth + "월"))
                        .child(String.valueOf(newday + "일"))
                        .child("집중시간")
                        .getChildren()) {
                    long test = Long.parseLong(snapshot.getValue().toString());
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
                    msg4 += test;
                }
                for (DataSnapshot snapshot3 : dataSnapshot
                        .child("aa")
                        .child("EEG DATA")
                        .child(mThisMonthCalendar.get(Calendar.YEAR) + "년")
                        .child(String.valueOf(newmonth + "월"))
                        .child(String.valueOf(newday + "일"))
                        .child("하루달성율")
                        .getChildren()) {
                    long test = Long.parseLong(snapshot3.getValue().toString());
                    msg5 += test;
                }
                long DTTHour;
                DTTHour = msg4 / 1000 / 3600;
                long DTTMin = (msg4 / 1000) % 3600 / 60;
                long DTTSec = ((msg4) / 1000) % 60;
                if (DTTHour == 0 && DTTMin == 0) {
                    DTT.setText(DTTSec + "초");
                } else if (DTTHour == 0 && DTTMin != 0) {
                    DTT.setText(DTTMin + "분 " + DTTSec + "초");
                } else if (DTTHour != 0) {
                    DTT.setText(DTTHour + "시간 " + DTTMin + "분 " + DTTSec + "초");
                }

                setData(msg5, list1.size());
                listview2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView,
                                            View view, final int position, long id) {
                        final ArrayList<String> timeList = new ArrayList<>();
                        final ArrayList<String> percList = new ArrayList<>();
                        newmonth = newCal(mThisMonthCalendar.get(Calendar.MONTH) + 1);
                        newday = newCal(mThisMonthCalendar.get(Calendar.DAY_OF_MONTH));
                        valueEventListener2 = databaseReference2.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot snapshot : dataSnapshot
                                        .child("aa")
                                        .child("EEG DATA")
                                        .child(mThisMonthCalendar.get(Calendar.YEAR) + "년")
                                        .child(String.valueOf(newmonth + "월"))
                                        .child(String.valueOf(newday + "일"))
                                        .child("목표시간")
                                        .getChildren()) {
                                    timeList.add(snapshot.getValue().toString());
                                }
                                getTimeInfo(timeList, position);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                            }
                        });
                        valueEventListener3 = databaseReference3.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot snapshot : dataSnapshot
                                        .child("aa")
                                        .child("EEG DATA")
                                        .child(mThisMonthCalendar.get(Calendar.YEAR) + "년")
                                        .child(String.valueOf(newmonth + "월"))
                                        .child(String.valueOf(newday + "일"))
                                        .child("하루달성율")
                                        .getChildren()) {
                                    percList.add(snapshot.getValue().toString());
                                }
                                getPercInfo(percList, position);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                            }
                        });
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        view = inflater.inflate(R.layout.cp_dayfrag, container, false);
        Button btnPreviousCalendar = view.findViewById(R.id.w_previous_calendar);
        Button btnNextCalendar = view.findViewById(R.id.w_next_calendar);
        Button goToday = view.findViewById(R.id.wcptoday);
        bar = view.findViewById(R.id.wprogressBar);
        barPercent = view.findViewById(R.id.wbarPercent);
        tvCalendarTitle = view.findViewById(R.id.w_calendar_title);
        ClickHour = view.findViewById(R.id.ws_hour);
        ClickPercent = view.findViewById(R.id.tv_clickpercent);

        TextView c_hour = view.findViewById(R.id.ws_hour);
        final TextView s_hour = view.findViewById(R.id.ws_hour);

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

    private void getTimeInfo(ArrayList<String> timeList, int position) {
        String time = timeList.get(position);
        long msg2 = Long.parseLong(time);
        long DHour1;
        DHour1 = msg2 / 1000 / 3600;
        long DMin1 = (msg2 / 1000) % 3600 / 60;
        long DSec1 = ((msg2) / 1000) % 60;
        if (DHour1 == 0 && DMin1 == 0) {
            ClickHour.setText(DSec1 + "초");
        } else if (DHour1 == 0 && DMin1 != 0) {
            ClickHour.setText(DMin1 + "분 " + DSec1 + "초");
        } else if (DHour1 != 0) {
            ClickHour.setText(DHour1 + "시간 " + DMin1 + "분 " + DSec1 + "초");
        }
    }

    private void getPercInfo(ArrayList<String> percList, int position) {
        String perc = percList.get(position);
        long msg3 = Long.parseLong(perc);
        ClickPercent.setText(msg3 + "%");
    }

    private void setData(long msg5, int size) {
        if (msg5 == 0 && size == 0) {
            bar.setProgress(0);
            barPercent.setText("0%");
            DTT.setText("-");
            ClickHour.setText("-");
            ClickPercent.setText("-");
        } else {
            int percent = (int) (msg5 / size);
            bar.setProgress((int) (percent));
            barPercent.setText(String.valueOf(percent) + "%");
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