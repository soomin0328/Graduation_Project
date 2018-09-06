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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CnActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference("USERS");

    private TextView theDate, aimTime;
    public Calendar cal = Calendar.getInstance();

    private LineChart chart;
    private Thread thread;

    final Handler mHandler = new Handler();
    float data_Value;

    int cyear = cal.get(Calendar.YEAR);
    int cmonth = (cal.get(Calendar.MONTH) + 1);
    int cday = cal.get(Calendar.DAY_OF_MONTH);

    TextView mEllapse, percent;
    Button mBtnStart, mBtnSplit;

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
        theDate = (TextView) findViewById(R.id.nowday);
        percent = findViewById(R.id.percent);
        aimTime = (TextView) findViewById(R.id.aimtime);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        String email = user.getEmail();

        int idx = email.indexOf("@");
        name = email.substring(0, idx);

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
         * */
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
        leftAxis.setDrawGridLines(false);                       // y축의 그리드 라인을 없앰

        YAxis rightAxis = chart.getAxisRight();
        rightAxis.setEnabled(false);                            // y축을 오른쪽에는 표시하지 않음

        LineData data = new LineData();
        chart.setData(data);                                    // LineData를 셋팅함

        feedMultiple();                                         // 쓰레드를 활용하여 실시간으로 데이터
    }

    private void addEntry() {
        LineData data = chart.getData();                        // onCreate에서 생성한 LineData를 가져옴
        if (data != null)                                        // 데이터가 널값이 아니면(비어있지 않으면) if문 실행
        {
            ILineDataSet set = data.getDataSetByIndex(0);       // 0번째 위치의 DataSet을 가져옴

            if (set == null)                                     // 0번에 위치한 값이 널값이면(값이 없으면) if문 실행
            {
                set = createSet();                              // createSet 실행
                data.addDataSet(set);                           // createSet 을 실행한 set을 DataSet에 추가함
            }

            data_Value = (float) Math.random(); //////랜덤값 대신에 실시간 데이터값 넣기.
            dia(data_Value);

            data.addEntry(new Entry(set.getEntryCount(), data_Value), 0);   // set의 맨 마지막에 랜덤값을 Entry로 data에 추가함
            //  data.addEntry(new Entry(set.getEntryCount(), (float) Math.random()), 0);   // set의 맨 마지막에 랜덤값을 Entry로 data에 추가함
            data.notifyDataChanged();                           // data의 값 변동을 감지함

            chart.notifyDataSetChanged();                       // chart의 값 변동을 감지함
            chart.setVisibleXRangeMaximum(30);                  // chart에서 한 화면에 x좌표를 최대 몇개까지 출력할 것인지 정함
            chart.moveViewToX(data.getEntryCount());


        }

    }

    private void dia(float data_Value) {
        if (0.2 < data_Value && data_Value < 0.4) { //여기서 범위를 수정해주기
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

    private LineDataSet createSet() {

        LineDataSet set = new LineDataSet(null, "Alpha");   // DataSet의 레이블 이름을 Alpha로 지정 후 기본 데이터 값은 null값
        set.setAxisDependency(YAxis.AxisDependency.LEFT);                 // y축은 왼쪽을 기본으로 설정
        set.setColor(Color.RED);                                          // 데이터의 라인색은 RED로 설정
        set.setCircleColor(Color.RED);                                    // 데이터의 점은 WHITE
        set.setLineWidth(2f);                                             // 라인의 두께는 2f
        set.setCircleRadius(1f);                                          // 데이터 점의 반지름은 1f
        set.setFillAlpha(65);                                             // 투명도 채우기는 65
        set.setDrawValues(false);                                         // 각 데이터값을 chart위에 표시하지 않음
        return set;                                                       // 이렇게 생성한 set값을 반환
    }

    private void feedMultiple() {
        if (thread != null)
            thread.interrupt();                                // 널이 아닌(살아있는) 쓰레드에 인터럽트를 검

        final Runnable runnable = new Runnable() {

            @Override
            public void run() {
                addEntry();
            }

        };

        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true)                                   // 무한히 반복
                {
                    runOnUiThread(runnable);                   // UI 쓰레드에서 위에 생성한 runnable을 실행
                    try {
                        Thread.sleep(2000);
                        // 차트를 그리는데 0.1초의 딜레이를 줌
                    } catch (InterruptedException ie) {
                        ie.printStackTrace();              // 표준 오류 스트림으로 돌아가는 이 스크롤 가능 공간을 인쇄한다
                    }                                         // 좀 더 알아보기.
                }
            }
        });
        thread.start();
    }                                                           // 쓰레드 시작

    @Override
    protected void onPause() {
        super.onPause();                                        // 정보를 영구적으로 저장
        if (thread != null)
            thread.interrupt();                                 // null이 아닌 쓰레드에 인터럽트를 걺
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
                switch (mStatus) {
                    //진행되고 있는데 끝내기를 누르면
                    case RUNNING: //진행되고있으면
                        mTimer.removeMessages(0);

                        long hour_l, min_l, sum, rtime, result;

                        hour_l = hour * 1000 * 3600; //설정한시간을 ms으로 바꾼거
                        min_l = min * 1000 * 60; //설정한 분을 ms로바꾼거

                        sum = (hour_l + min_l) / 100;

                        databaseReference.child(name).child("EEG DATA").child(String.valueOf(cyear + "년")).child(String.valueOf(cmonth + "월"))
                                .child(String.valueOf(cday + "일")).child("목표시간").push().setValue(sum * 100);

                        rtime = getEll2(); //아래에서 집중한 시간 받아온값 sum이랑 빼줄거임
                        //rtime이 공부 (명상) 다해서 끝내기 눌러서 가져오는 최종 집중 밀리세컨즈단위 시간->이걸디비에go.
                        databaseReference.child(name).child("EEG DATA").child(String.valueOf(cyear + "년")).child(String.valueOf(cmonth + "월"))
                                .child(String.valueOf(cday + "일")).child("집중시간").push().setValue(rtime);

                        result = rtime / sum;

                        percent.setText("달성률:" + result + "%");

                        databaseReference.child(name).child("EEG DATA").child(String.valueOf(cyear + "년")).child(String.valueOf(cmonth + "월"))
                                .child(String.valueOf(cday + "일")).child("하루달성율").push().setValue(String.valueOf(result));

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

                        mBtnSplit.setEnabled(false);
                        break;
                }
                break;
        }

    }

    Long getEll2() {
        long now = SystemClock.elapsedRealtime();
        long ell2 = now - mBaseTime;

        return ell2;
    }

    String getEllapse() {

        long now = SystemClock.elapsedRealtime();
        String sEll;
        long ell = now - mBaseTime;//현재 시간과 지난 시간을 빼서 ell값을 구하고

        sEll = String.format("%02d:%02d:%02d", ell / 1000 / 3600, (ell / 1000) % 3600 / 60, (ell / 1000) % 60);
        //시간 분 초 로 바꿔준걸 반환해주는거
        return sEll;

    }


}