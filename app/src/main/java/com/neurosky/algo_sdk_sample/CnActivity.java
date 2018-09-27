package com.neurosky.algo_sdk_sample;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CnActivity extends AppCompatActivity { //
    private TextView theDate;
    private TextView aimTime;
    public Calendar cal = Calendar.getInstance();

    long now = System.currentTimeMillis();
    Date date = new Date(now);

    SimpleDateFormat HNow = new SimpleDateFormat("HH");
    SimpleDateFormat mNow = new SimpleDateFormat("mm");

    String formatDate4 = HNow.format(date);
    String formatDate5 = mNow.format(date);

    final Handler mHandler = new Handler();

    private LineChart chart;

    int cyear = cal.get(Calendar.YEAR);
    int cmonth = (cal.get(Calendar.MONTH) + 1);
    int cday = cal.get(Calendar.DAY_OF_MONTH);
    String newmonth = "", newday = "";

    TextView mEllapse, percent;
    Button mBtnStart, mBtnSplit;

    FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference("USERS");
    private DatabaseReference graphRef = firebaseDatabase.getReference("USERS");
    private ValueEventListener valueEventListener;

    private PreferenceManager manager;

    private ArrayList<DataObj> dataList;

    //스톱워치의 상태를 위한 상수
    final static int IDLE = 0;
    final static int RUNNING = 1;
    final static int PAUSE = 2;

    int mStatus = IDLE;//처음 상태는 IDLE

    long mBaseTime, mPauseTime;
    int hour, min;
    String name = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cn);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        String email = user.getEmail();

        int idx = email.indexOf("@");
        name = email.substring(0, idx);

        theDate = (TextView) findViewById(R.id.nowday);
        percent = findViewById(R.id.percent);
        aimTime = (TextView) findViewById(R.id.aimtime);

        // 실수 값 Format
        final DecimalFormat decimalFormat = new DecimalFormat("###,###,##0");

        // preference manager 객체를 등록
        manager = new PreferenceManager(CnActivity.this);

        /*
         *         액티비티를 종료하거나, 액티비티에서 바로 앱을 종료했을 때, onStop()을 통해 preference를 삭제해줘야한다.
         *         하지만, 이 액티비티 상태에서 디버깅 시, 앱이 강제로 꺼지면서 디버깅 후 다시 실행되는데,
         *         이 때, onStop() 매소드가 실행이 되지 않게 되어 preference가 삭제가 안된다.
         *         그러므로, 앱이 시작되는 onCreate()에 preference를 삭제해줌
         */

        manager.clearPreference();

        Intent incomingIntent = getIntent(); //aimtime 클래스에서 얻어옴
        String times = incomingIntent.getStringExtra("data");

        hour = incomingIntent.getIntExtra("hours", 90); //설정한 목표 시간
        min = incomingIntent.getIntExtra("mins", 92); //설정한 목표시간 분

        aimTime.setText(times);

        //변수 hour min 을 long으로 바꾸서
        long hourn = hour * 1000 * 3600;
        long minn = min * 1000 * 60;
        long aim = hourn + minn; //ms단위인 목표시간

        /**
         * 타이머 변수
         * **/
        mEllapse = (TextView) findViewById(R.id.cellapse);  //초뜨는 텍스트
        mBtnStart = (Button) findViewById(R.id.cbtnstart);
        mBtnSplit = (Button) findViewById(R.id.cbtnsplit);  //스탑워치 멈추고 달성률뜨게ㅎㅏ는 버튼

        String text2 = cyear + "년" + cmonth + "월" + cday + "일 현재상태";

        theDate.setText(text2);


        /**
         * 그래프
         * */
        chart = (LineChart) findViewById(R.id.chart);           // chart를 xml에서 생성한 LineChart와 연결시킴

        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);          // x축의 위치는 하단
        xAxis.setTextSize(10f);                                 // x축 텍스트의 크기는 10f
        xAxis.setDrawGridLines(false);                          // x축의 그리드 라인을 없앰
        xAxis.setValueFormatter(new CustomValueForm()); //현재 시간 받아오기
        YAxis leftAxis = chart.getAxisLeft();
        // X축의 세분화를 활성화
        leftAxis.setGranularityEnabled(true);
        // 좌측 X축 데이터 간격을 1로 하였습니다.
        leftAxis.setGranularity(1);
        leftAxis.setDrawGridLines(false);                       // y축의 그리드 라인을 없앰

        YAxis rightAxis = chart.getAxisRight();
        rightAxis.setEnabled(false);                            // y축을 오른쪽에는 표시하지 않음

        LineData data = new LineData();
        chart.setData(data);                                    // LineData를 셋팅함

        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy-MM-dd");

        final String[] nowArray = sdfNow.format(date).split("-");

        valueEventListener = graphRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                // DB에서 가져온 데이터들이 저장되는 리스트
                dataList = new ArrayList<>();

                for (DataSnapshot dataSnapshot1 : dataSnapshot.child(name).child("EEG DATA")
                        .child(nowArray[0] + "년").child(nowArray[1] + "월").child(nowArray[2] + "일").getChildren()) {
                    //시
                    String hour = dataSnapshot1.getKey().toString();
                    hour = stringToNum(hour);

                    for (DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren()) {
                        //분
                        String min = dataSnapshot2.getKey().toString();
                        min = stringToNum(min);

                        for (DataSnapshot dataSnapshot3 : dataSnapshot2.getChildren()) {
                            //초
                            String sec = dataSnapshot3.getKey().toString();
                            sec = stringToNum(sec);

                            Map<String, Long> map = (Map<String, Long>) dataSnapshot3.getValue();

                            if (map.get("집중도") != null) {
                                dataList.add(new DataObj(hour + ":" + min + ":" + sec, String.valueOf(map.get("집중도"))));
                            }
                        }
                    }

                }

                DataObj obj = getRecentData(dataList);
                if (obj != null) {
                    addEntry(obj);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        graphRef.addValueEventListener(valueEventListener);

    }


    private String stringToNum(String sec) {
        String num_str = "";
        Pattern p = Pattern.compile("-?\\d+(,\\d+)*?\\.?\\d+?");
        Matcher m = p.matcher(sec);
        while (m.find()) {
            num_str += m.group();
        }
        return num_str;
    }

    // db 데이터 중 가장 최근 데이터를 가져와줌
    private DataObj getRecentData(ArrayList<DataObj> dataList) {
        DataObj data = null;

        for (int i = 0; i < dataList.size(); i++) {
            if (i == dataList.size() - 1) {
                data = dataList.get(i);
            }
        }

        return data;
    }

    // 데이터를 그래프에 나타내준다.
    private void addEntry(DataObj object) {
        LineData data = chart.getData();

        // onCreate에서 생성한 LineData를 가져옴
        if (data != null)                                        // 데이터가 널값이 아니면(비어있지 않으면) if문 실행
        {

            ILineDataSet set = data.getDataSetByIndex(0);       // 0번째 위치의 DataSet을 가져옴

            if (set == null)                                     // 0번에 위치한 값이 널값이면(값이 없으면) if문 실행
            {
                set = createSet();                              // createSet 실행
                data.addDataSet(set);                           // createSet 을 실행한 set을 DataSet에 추가함
            }


            DataObj prevObj = manager.getPreference();

            if (prevObj != null) {

                if (!prevObj.getTime().equals(object.getTime())) {

                    data.addEntry(new Entry(set.getEntryCount(), Float.parseFloat(object.getVal())), 0);   // set의 맨 마지막에 랜덤값을 Entry로 data에 추가함
                    dia(Float.parseFloat(object.getVal()));
                } else {
                    if (!prevObj.getVal().equals(object.getVal())) {

                        data.addEntry(new Entry(set.getEntryCount(), Float.parseFloat(object.getVal())), 0);   // set의 맨 마지막에 랜덤값을 Entry로 data에 추가함
                    } else {
                    }
                }
            } else {

                data.addEntry(new Entry(set.getEntryCount(), Float.parseFloat(object.getVal())), 0);   // set의 맨 마지막에 랜덤값을 Entry로 data에 추가함
                dia(Float.parseFloat(object.getVal()));
            }

            data.notifyDataChanged();                           // data의 값 변동을 감지함

            chart.notifyDataSetChanged();                       // chart의 값 변동을 감지함

            chart.setVisibleXRangeMaximum(30);                  // chart에서 한 화면에 x좌표를 최대 몇개까지 출력할 것인지 정함
            chart.moveViewToX(data.getEntryCount());

            manager.savePreference(object);
        }

    }

    private LineDataSet createSet() {

        LineDataSet set = new LineDataSet(null, "집중도");   // DataSet의 레이블 이름을 Alpha로 지정 후 기본 데이터 값은 null값
        set.setAxisDependency(YAxis.AxisDependency.LEFT);                 // y축은 왼쪽을 기본으로 설정
        set.setColor(Color.RED);                                          // 데이터의 라인색은 RED로 설정
        set.setCircleColor(Color.RED);                                    // 데이터의 점은 WHITE
        set.setLineWidth(2f);                                             // 라인의 두께는 2f
        set.setCircleRadius(1f);                                          // 데이터 점의 반지름은 1f
        set.setFillAlpha(65);                                             // 투명도 채우기는 65
        set.setDrawValues(false);                                         // 각 데이터값을 chart위에 표시하지 않음
        return set;                                                       // 이렇게 생성한 set값을 반환

    }

    private void dia(float data_Value) {
        if (data_Value < 50) {
            // final AlarmGraph ag=new AlarmGraph();
            //ag.show(getSupportFragmentManager(),"집중해");
            AlertDialog.Builder ad = new AlertDialog.Builder(CnActivity.this);
            ad.setMessage("집중하세요!!");

            final AlertDialog aaa = ad.create();
            aaa.show();
            Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            vibrator.vibrate(900);
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (aaa.isShowing())
                        aaa.dismiss();
                }

            }, 1000);
        }
    }

    /***
     * 아래 타이머
     */
    Handler mTimer = new Handler() {


        public void handleMessage(android.os.Message msg) {

            //텍스트뷰를 수정해준다.

            mEllapse.setText(getEllapse());

            //메시지를 다시 보낸다.

            mTimer.sendEmptyMessage(0);//0은 메시지를 구분하기 위한 것

        }

        ;

    };

    @Override
    public void onBackPressed() {

        // thread.interrupt();
        super.onBackPressed();
        //mHandler.removeCallbacksAndMessages(0);
        this.finish();
    }

    public void mOnClick(View v) {

        switch (v.getId()) {
            //시작 버튼이 눌리면

            case R.id.cbtnstart:

                switch (mStatus) {

                    //IDLE상태이면

                    case IDLE:

                        //현재 값을 세팅해주고

                        mBaseTime = SystemClock.elapsedRealtime();

                        //핸드러로 메시지를 보낸다

                        mTimer.sendEmptyMessage(0);


                        mBtnStart.setText("중지");//중지버튼으로 뜨게

                        //옆버튼의 Enable을 푼 다음

                        mBtnSplit.setEnabled(true);  //아예그만두고 달성률 ㅗ볼수 있는 버튼  명상에서는 걍 없음 걍 중지임

                        //상태를 RUNNING으로 바꾼다.

                        mStatus = RUNNING;

                        break;


                    case RUNNING:  //초시계 움직이고있으면

                        //핸들러 메시지를 없애고

                        mTimer.removeMessages(0);

                        //멈춘 시간을 파악

                        mPauseTime = SystemClock.elapsedRealtime();
                        //버튼 텍스트를 바꿔줌
                        mBtnStart.setText("이어하기");

                        mBtnSplit.setText("다시하기"); //종료하는거

                        mStatus = PAUSE;//상태를 멈춤으로 표시

                        break;


                    case PAUSE:  //멈춰있은 상태면

                        //현재값 가져옴

                        long now = SystemClock.elapsedRealtime();

                        //베이스타임 = 베이스타임 + (now - mPauseTime)
                        mBaseTime += (now - mPauseTime);


                        mTimer.sendEmptyMessage(0);

                        //텍스트 수정

                        mBtnStart.setText("중지");

                        mBtnSplit.setText("끝내기");

                        mStatus = RUNNING;  //멈춰있는 상태에서 초 올라간다

                        break;

                }

                break;

            case R.id.cbtnsplit:

                //진행되고 있는데 끝내기를 누르면
                switch (mStatus) {
                    case RUNNING: //진행되고있으면
                        mTimer.removeMessages(0);

                        // String sSplit = mSplit.getText().toString();
                        long hour_l, min_l, sum, rtime, sum1, rtime1, result1;
                        long result; //퍼센트 결과 값 (달성률)

                        hour_l = hour * 1000 * 3600; //설정한시간을 ms으로 바꾼거
                        min_l = min * 1000 * 60; //설정한 분을 ms로바꾼거

                        sum = (hour_l + min_l) / 100; ///결국 sum값이 사용자가 설정한 목표 시간!!!!!!!!!

                        newmonth = newCal(cmonth);
                        newday = newCal(cday);

                        databaseReference.child(name)
                                .child("EEG DATA")
                                .child(String.valueOf(cyear + "년"))
                                .child(String.valueOf(newmonth + "월"))
                                .child(String.valueOf(newday + "일"))
                                .child("목표시간").push().setValue(sum * 100);

                        rtime = getEll2(); //아래에서 집중한 시간 받아온값 sum이랑 빼줄거

                        databaseReference.child(name)
                                .child("EEG DATA")
                                .child(String.valueOf(cyear + "년"))
                                .child(String.valueOf(newmonth + "월"))
                                .child(String.valueOf(newday + "일"))
                                .child("집중시간").push().setValue(rtime);

                        result = rtime / sum;

                        percent.setText("달성률:" + result + "%");

                        databaseReference.child(name)
                                .child("EEG DATA")
                                .child(String.valueOf(cyear + "년"))
                                .child(String.valueOf(newmonth + "월"))
                                .child(String.valueOf(newday + "일"))
                                .child("하루달성율").push().setValue(String.valueOf(result)); //집중한 시간 long값

                        mStatus = IDLE;

                        mBtnStart.setText("시작");

                        mBtnStart.setEnabled(false);
                        mBtnSplit.setEnabled(false);

                        break;

                    case PAUSE://여기서는 초기화버튼이 됨

                        //핸들러를 없애고

                        mTimer.removeMessages(0);

                        //처음상태로 원상복귀시킴

                        mBtnStart.setText("시작");

                        mBtnSplit.setText("끝내기");

                        mEllapse.setText("00:00:00");

                        mStatus = IDLE;

                        //mSplit.setText("");

                        mBtnSplit.setEnabled(false);

                        break;
                }
                break;
        }

    }

    Long getEll2() {
        long now = SystemClock.elapsedRealtime();
        long ell2 = now - mBaseTime;
        //밀리세컨즈로 보내서 위에서 계산할거임
        return ell2;
    }

    String getEllapse() {

        long now = SystemClock.elapsedRealtime();

        long ell = now - mBaseTime;//현재 시간과 지난 시간을 빼서 ell값을 구하고

        // String sEll = String.format("%02d:%02d:%02d", ell / 1000 / 60, (ell/1000)%60, (ell %1000)/10);
        String sEll = String.format("%02d:%02d:%02d", ell / 1000 / 3600, (ell / 1000) / 60, (ell / 1000) % 60);
        //시간 분 초 로 바꿔준걸 반환해주는거
        return sEll;

    }

    // 액티비티가 종료하거나 액티비티상태에서 아예 앱을 종료하는 경우
    @Override
    protected void onStop() {
        super.onStop();
        // 액티비티를 나갈 때 preference 데이터를 지워줌(Manager 클래스 안에 상세 설명 되어있음)
        manager.clearPreference();
    }

    // 액티비티가 종료하는 시점
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // DB 리스너를 제거해줌
        graphRef.removeEventListener(valueEventListener);
    }

    private String newCal(int cal) {

        String str = String.valueOf(cal);

        if (str.length() == 1)
            return "0" + str;
        else
            return str;
    }

}