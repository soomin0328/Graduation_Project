package com.neurosky.algo_sdk_sample;

import android.app.Fragment;
import android.graphics.Color;
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

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
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
    //private TextView tvSelectedDate;
    private GridView gvCalendar;
    private LineChart mChart;           //mChart 라는 LineChart를 선언해준다.

    //디비
    String i;
    long mediTime;
    long mediHour;
    //디비

    private ArrayList<DayInfo> arrayListDayInfo;
    Calendar mThisMonthCalendar;
    WeekCalendarAdapter mCalendarAdapter;

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference("USERS");


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
    TextView mp_week;

    @Nullable

    @Override

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        //inflater를 사용해 프래그먼트에 사용할 레이아웃 불러오고 리턴
        //해당 프래그먼트에 대한 기능적코드 여기에 넣으래
//..?
        view = inflater.inflate(R.layout.mp_weekfrag, container, false);

        mp_week = view.findViewById(R.id.mp_week);

        Button btnPreviousCalendar = view.findViewById(R.id.mw_previous_calendar); //과거 명상 주별 이라 mv
        Button btnNextCalendar = view.findViewById(R.id.mw_next_calendar);
        Button goToday = view.findViewById(R.id.mwptoday);  //주별 명상 past
        tvCalendarTitle = view.findViewById(R.id.mw_calendar_title);
        // tvSelectedDate = findViewById(R.id.tv_selected_date);
        gvCalendar = view.findViewById(R.id.mw_gv_calendar);
        // TextView ws_hour=view.findViewById(R.id.ws_hour); //몇시간 했는지 띄우는거 즉, 디비에서 불러온값을 달력하나에 띄우겟다는거임...어케하지?ㅠ; //하루공부시간옆숫자
        // final TextView ws_hour=view.findViewById(R.id.ww_hour);

        mChart = view.findViewById(R.id.mwChart);    //mChart를 Linechart의 기능을 하게 findViewById로 불러와준다.

        mChart.setDragEnabled(true);

        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setEnabled(false);                //오른쪽에 y축의 데이터를 표시하지 않음.

        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);          //x축 값을 밑에 표시.
        xAxis.setDrawGridLines(false);                  //x축의 그리드라인을 없앰.

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setDrawGridLines(false);               //y축의 그리드라인을 없앰.
        ArrayList<Entry> yValues = new ArrayList<>();
        // ArrayList<Entry> xValues = new ArrayList<>();
        //ArrayList<Entry> zValues = new ArrayList<>();           //각 그래프 별 데이터를 입력받을 배열을 만듦.

        //x축을 일월화수목금토 할거임 숫자를->한글로바꾸는거 알아보기

        yValues.add(new Entry(1, 60));
        yValues.add(new Entry(2, 50));
        yValues.add(new Entry(3, 70));
        yValues.add(new Entry(4, 30));
        yValues.add(new Entry(5, 50));
        yValues.add(new Entry(6, 60));
        yValues.add(new Entry(7, 20));              //Alpha 값 입력


        LineDataSet set1 = new LineDataSet(yValues, "Alpha");

        set1.setColor(Color.RED);
        // set2.setColor(Color.BLUE);
        //set3.setColor(Color.GREEN);                 //그래프 별 색 지정


        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);
        //   dataSets.add(set2);
        //  dataSets.add(set3);                         //3개의 그래프를 하나의 dataSet이란 배열로 만듦

        LineData data = new LineData(dataSets);     //dataSet이란 배열을 data로 선언

        data.setDrawValues(true);                  //data의 수치를 그래프 위에 나타내지 않기.

        mChart.setData(data);                       //mChart를 이용하여 data 표시


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
                mThisMonthCalendar.add(Calendar.WEEK_OF_MONTH, -1);

                getCalendar(mThisMonthCalendar.getTime());
            }
        });
        btnNextCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mThisMonthCalendar.add(Calendar.WEEK_OF_MONTH, +1);

                getCalendar(mThisMonthCalendar.getTime());
            }
        });

        gvCalendar.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            ValueEventListener pListener = new ValueEventListener() {

                @Override

                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Log.d("작동되니", "");
                    for (DataSnapshot snapshot : dataSnapshot.child("aa").child("EEG DATA").child(mThisMonthCalendar.get(Calendar.YEAR) + "년").child(String.valueOf(mThisMonthCalendar.get(Calendar.MONTH) + 1 + "월")).child(String.valueOf(i + "일")).child("명상시간").getChildren()) {
                        Log.d("레아아", "" + snapshot.getValue());
                        long test = Long.parseLong(snapshot.getValue().toString());
                        mediTime += test;
                        Log.d("값더한거", "" + mediTime);
                    }

                    //for(DataSnapshot snapshot:dataSnapshot.child("aa").child("EEG DATA").child(String.valueOf(mThisMonthCalendar.get(Calendar.YEAR)+"년")).child(String.valueOf(mThisMonthCalendar.get(Calendar.MONTH) + 1+"월")).child(String.valueOf(i+"일")).child("집중시간").getChildren()){
//                  Log.d("뭐냐고요2", dataSnapshot.child("aa").child("EEG DATA").child(mThisMonthCalendar.get(Calendar.YEAR)+"년").child(String.valueOf(mThisMonthCalendar.get(Calendar.MONTH) + 1+"월")).child(String.valueOf(i+"일")).child("명상시간").getValue().toString()+"");
                    // Log.d("레알",snapshot.getValue()+"");
                    //}
                    // String [] data={"9"};
                    //   Log.d("뭐냐고요", dataSnapshot.child("aa").child("EEG DATA").child(mThisMonthCalendar.get(Calendar.YEAR)+"년").child(String.valueOf(mThisMonthCalendar.get(Calendar.MONTH) + 1+"월")).child(String.valueOf(i+"일")).child("명상시간").getValue().toString()+"");
                    // ell/1000/3600, (ell/1000)/60, (ell/1000)%60)

                    mediHour = mediTime / 1000 / 3600;
                    long mediMin = (mediTime / 1000) / 60;
                    long mediSec = ((mediTime) / 1000) % 60;

                    Log.d("아아테스트", mediTime / 1000 / 3600 + "시간" + (mediTime / 1000) / 60 + "분  " + mediTime + "는메디타임" + "medihour은" + mediHour);

                    if (mediHour == 0) {
                        mp_week.setText(mediMin + "분 " + mediSec + "초");
                        mediTime = 0;
                    } else if (mediHour != 0 && mediMin == 0) {
                        mp_week.setText(mediHour + "시간" + mediMin + "분 " + mediSec + "초");
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
                databaseReference.addValueEventListener(pListener);

                setSelectedDate(((DayInfo) view.getTag()).getDate());
                DayInfo day = arrayListDayInfo.get(position);
                i = day.getDay();

                final long a = mThisMonthCalendar.get(Calendar.YEAR);
                final long b = (mThisMonthCalendar.get(Calendar.MONTH) + 1);
                Log.d("ㅇㅅㅇ", a + "년" + b + "월" + i + "일");


                // String ccc= String.valueOf(dataSnapshot.child("aa").child("EEG DATA").child(String.valueOf(a+"년")).child(String.valueOf(b+"월")).child(String.valueOf(i+"일")).child("명상시간").getValue());
                //mp_week.setText(ccc);
                //   Log.d("머지",databaseReference+"");
                //  Log.d("아아아",dataSnapshot.getChildren()+"");
                //Log.d("뭐냐고", dataSnapshot.child("aa").child("EEG DATA").child(String.valueOf(a+"년")).child(String.valueOf(b+"월")).child(String.valueOf(i+"일")).child("명상시간").getValue()+"");

            }
        });
       /* FirebaseDatabase.getInstance().getReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot:dataSnapshot.child("aa").child("EEG DATA").child("2018년").child("8월").child("03일").child("21시").child("47분").child("57초").getChildren()){
                 Log.d("tqtq",snapshot.getValue()+"");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
*/
        arrayListDayInfo = new ArrayList<>();
        return view;
    }

   /* ValueEventListener weekListener=new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };*/

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
        dayOfWeek = calendar.get(Calendar.WEEK_OF_MONTH);//
        //  calendar.set(Calendar.WEEK_OF_MONTH,dayOfweek);
        calendar.set(Calendar.DATE, dayOfWeek);//1일로 변경--->오늘날짜로..

        Log.d("CalendarTest", "dayOfWeek = " + dayOfWeek + "");


        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        thisWeekLastDay = calendar.getActualMaximum(Calendar.DAY_OF_WEEK);
        Log.d("calendar", thisWeekLastDay + "는 디스위크 라스트데이   " + Calendar.DAY_OF_WEEK);

        setCalendarTitle();

        DayInfo day;

        for (int i = 1; i <= thisWeekLastDay; i++) {
            day = new DayInfo();
            day.setDate(calendar.getTime());
            Log.d("값", calendar.getTime() + ""); // Sun Jul 29 23:05:10 GMT+09:00 2018  이런형식으로 나옴
            //Mon Jul 30 23:05:10 GMT+09:00 2018 +1 DATE하니까 ㅇㅣ렇게 나오넹
            //차일드에 넣을때 mThisMonthCalendar 가 아닌 Calendar.MONTH?d 이ㅓ걸로 넣어서?ㄱ ?
            Log.d("값2", day + " 는 day 입니다.ㅋ");
            day.setInMonth(true);

            arrayListDayInfo.add(day);
            databaseReference.addValueEventListener(weekListener);
            Log.d("값 테스트", String.valueOf(day) + "");


            calendar.add(Calendar.DATE, +1);
            Log.d("date 테스트", Calendar.DATE + "");
        }


        mCalendarAdapter = new WeekCalendarAdapter(arrayListDayInfo, selectedDate);
        gvCalendar.setAdapter(mCalendarAdapter);


    }

    ValueEventListener weekListener = new ValueEventListener() { //달력에 하루하루 클릭시 databaseReferences꺼
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
          /*  for(DataSnapshot snapshot: dataSnapshot.child("aa").child("EEG DATA").child(mThisMonthCalendar.get(Calendar.YEAR)+"년").child(String.valueOf(mThisMonthCalendar.get(Calendar.MONTH) + 1+"월")).child(String.valueOf(i+"일")).child("집중시간").getChildren())
            {
                Log.d("값오니",""+snapshot.getValue());
                long test=Long.parseLong(snapshot.getValue().toString());
                conTime+=test;
                Log.d("값 합보기",""+conTime);
            }

*/

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
}



