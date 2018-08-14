package com.neurosky.algo_sdk_sample;

import android.graphics.Color;
import android.icu.util.Calendar;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;

public class MnActivity extends AppCompatActivity {
    Button playbtn; //음악재생
    Button prev;
    Button nextbtn; //다음곡
    SeekBar seekbar;
    MediaPlayer music;
    TextView theDay;
    int[] a;
    static int count = 0;
    private LineChart chart2;
    private Thread thread2;
    Calendar cal = Calendar.getInstance();
    int myear = cal.get(Calendar.YEAR);
    int mmonth = (cal.get(Calendar.MONTH) + 1);
    int mday = cal.get(Calendar.DAY_OF_MONTH);
    long ell;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference("USERS");

    /**
     * 스톱워치 변수
     */
    TextView mEllapse;

    Button mBtnStart;

    Button mBtnSplit;

    //스톱워치의 상태를 위한 상수

    final static int IDLE = 0;

    final static int RUNNING = 1;

    final static int PAUSE = 2;

    int mStatus = IDLE;//처음 상태는 IDLE

    long mBaseTime;

    long mPauseTime;


    private SeekBar.OnSeekBarChangeListener soundcontrollListner = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mn);
/**
 * 타이머
 * */
        theDay = findViewById(R.id.mntoday);
        String text = myear + "년 " + mmonth + "월 " + mday + "일 명상상태";
        theDay.setText(text);

        mEllapse = (TextView) findViewById(R.id.ellapse);  //초뜨는 텍스트 00:00:00

        // mSplit = (TextView)findViewById(R.id.split);  //스탑워치 기록 시간표시해주는거

        mBtnStart = (Button) findViewById(R.id.btnstart);

        mBtnSplit = (Button) findViewById(R.id.btnsplit);  //스탑워치 멈추고 달성률뜨게ㅎㅏ는 버튼
        /**그래프소스
         *
         */

        chart2 = (LineChart) findViewById(R.id.chart2);           // chart를 xml에서 생성한 LineChart와 연결시킴

        XAxis xAxis = chart2.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);          // x축의 위치는 하단
        xAxis.setTextSize(10f);                                 // x축 텍스트의 크기는 10f
        xAxis.setDrawGridLines(false);                          // x축의 그리드 라인을 없앰

        YAxis leftAxis = chart2.getAxisLeft();
        xAxis.setValueFormatter(new CustomValueForm());

        leftAxis.setDrawGridLines(false);                       // y축의 그리드 라인을 없앰

        YAxis rightAxis = chart2.getAxisRight();
        rightAxis.setEnabled(false);                            // y축을 오른쪽에는 표시하지 않음

        LineData data = new LineData();
        chart2.setData(data);                                    // LineData를 셋팅함

        feedMultiple();


        /*******음악재생
         *
         */
        nextbtn = (Button) findViewById(R.id.next);
        prev = findViewById(R.id.prev);
        //***
        //
        /*
         **************/

        //랜덤값으로 숫자 받고 인텐트받아온값이 1이면 function(다른액티비티에서 INTENT로 받아온값); 해서 만들고... function에 뮤직에들어갈 음악 정해주ㅁ면되겟지?
        final int[] a = musicSetting(1);

        music = MediaPlayer.create(this, a[count]);

        playbtn = (Button) findViewById(R.id.play);
        playbtn.setBackgroundResource(R.drawable.play);

        seekbar = (SeekBar) findViewById(R.id.seekBar);

        seekbar.setMax(music.getDuration());
        seekbar.setOnSeekBarChangeListener(soundcontrollListner);
        seekbar.incrementProgressBy(1);

        // int duration = music.getDuration();
        //seekbar.setMax(100);

        //music.isLooping();
        music.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer music) {
                playbtn.setBackgroundResource(R.drawable.play);
                //music.stop();
                //music.release();

            }
        });


        prev.setOnClickListener(new View.OnClickListener() {

                                    public void onClick(View v) {

                                        if (count == 0) {
                                            count = 2;
                                            if (music.isPlaying()) {
                                                music.stop();
                                                music.release();
                                                //music.seekTo(0);
                                                music = MediaPlayer.create(MnActivity.this, a[count]);
                                                seekbar.setMax(music.getDuration());
                                                music.seekTo(0);

                                                music.start();
                                                // music.setOnCompletionListener(null);
                                                Toast.makeText(getApplicationContext(), (count + 1) + "번째 곡", Toast.LENGTH_SHORT).show();

                                            } else {  //멈추잇을때


                                                music = MediaPlayer.create(MnActivity.this, a[count]);
                                                playbtn.setBackgroundResource(R.drawable.play);
                                                seekbar.setMax(music.getDuration());
                                                music.seekTo(0);
                                                // music.start();
                                                Toast.makeText(getApplicationContext(), (count + 1) + "번째 곡", Toast.LENGTH_SHORT).show();
                                            }
                                        } else { //카운트2아닐때
                                            if (music.isPlaying()) {
                                                music.stop();
                                                music.release();
                                                count--;
                                                Toast.makeText(getApplicationContext(), (count + 1) + "번째 곡", Toast.LENGTH_SHORT).show();

                                                music = MediaPlayer.create(MnActivity.this, a[count]);
                                                seekbar.setMax(music.getDuration());
                                                music.seekTo(0);
                                                //int a= music.getDuration();
                                                // Log.d("seebar",a+"");

                                                music.start();

                                            } else {
                                                count--;
                                                Toast.makeText(getApplicationContext(), (count + 1) + "번째 곡", Toast.LENGTH_SHORT).show();

                                                music = MediaPlayer.create(MnActivity.this, a[count]);
                                                seekbar.setMax(music.getDuration());
                                                music.seekTo(0);

                                                // playbtn.setBackgroundResource(R.drawable.stop);
                                                // music.start();

                                            }
                                        }
                                    }

                                }
        );


        nextbtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if (count == 2) {
                    count = 0;
                    if (music.isPlaying()) {
                        music.stop();
                        music.release();

                        music = MediaPlayer.create(MnActivity.this, a[count]);
                        seekbar.setMax(music.getDuration());
                        music.seekTo(0);
                        music.start();
                        //  Thread();///////
                        Toast.makeText(getApplicationContext(), (count + 1) + "번째 곡", Toast.LENGTH_SHORT).show();
                    } else {  //멈추잇을E때
                        music = MediaPlayer.create(MnActivity.this, a[count]);

                        seekbar.setMax(music.getDuration());
                        playbtn.setBackgroundResource(R.drawable.play);
                        music.seekTo(0);
                        // music.start();
                        Toast.makeText(getApplicationContext(), (count + 1) + "번째 곡", Toast.LENGTH_SHORT).show();


                    }
                } else { //카운트2아닐때
                    if (music.isPlaying()) {
                        count++;
                        music.stop();
                        music.release();

                        Toast.makeText(getApplicationContext(), (count + 1) + "번째 곡", Toast.LENGTH_SHORT).show();

                        music = MediaPlayer.create(MnActivity.this, a[count]);
                        seekbar.setMax(music.getDuration());
                        music.seekTo(0);
                        //int a= music.getDuration();
                        // Log.d("seebar",a+"");

                        music.start();  ////////
                        // Thread();
                    } else {
                        count++;
                        Toast.makeText(getApplicationContext(), (count + 1) + "번째 곡", Toast.LENGTH_SHORT).show();

                        music = MediaPlayer.create(MnActivity.this, a[count]);
                        seekbar.setMax(music.getDuration());
                        music.seekTo(0);

                        // playbtn.setBackgroundResource(R.drawable.stop);
                        // music.start();

                    }
                }
            }
        });


        playbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (music.isPlaying()) {
                    music.pause();
                    playbtn.setBackgroundResource(R.drawable.play);
                    // music.release();
                    try {
                        music.prepare(); //데이터 로드할수있게하는 메소드
                    } catch (IllegalStateException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    // music.seekTo(0);
                    //seekbar.setProgress(0);
                } else {
                    music.start();
                    Toast.makeText(getApplicationContext(), (count + 1) + "번째 곡", Toast.LENGTH_SHORT).show();
                    Thread();
                    playbtn.setBackgroundResource(R.drawable.stop);
                }

            }
        });

        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    //Log.d("seekbar", String.valueOf(a));
                    music.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //Log.d("seekbar", String.valueOf(a));
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //Log.d("seekbar", String.valueOf(a));
            }
        });
    }

    @Override
    public void onBackPressed() {  //뒤로가기누르면 꺼지게한거임
        music.stop();
        super.onBackPressed();
    }

    public void onDestroy() {
        super.onDestroy();
        if (music != null) {
            music.release();
        }
    }


    private void Thread() {
        Runnable task = new Runnable() {
            @Override
            public void run() {
                while (music.isPlaying()) {
                    { //Log.d("seekbar", String.valueOf(a));
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        seekbar.setProgress(music.getCurrentPosition());//현재 음악재싱 위치가져오기
                    }

                }
            }
        };

        Thread thread = new Thread(task);
        // button.setText("stop");

        thread.start();
    }

    /**
     * 그래프
     ***/

    private void addEntry() {
        LineData data = chart2.getData();                        // onCreate에서 생성한 LineData를 가져옴
        if (data != null)                                        // 데이터가 널값이 아니면(비어있지 않으면) if문 실행
        {
            ILineDataSet set = data.getDataSetByIndex(0);       // 0번째 위치의 DataSet을 가져옴

            if (set == null)                                     // 0번에 위치한 값이 널값이면(값이 없으면) if문 실행
            {
                set = createSet();                              // createSet 실행
                data.addDataSet(set);                           // createSet 을 실행한 set을 DataSet에 추가함
            }

            data.addEntry(new Entry(set.getEntryCount(), (float) Math.random()), 0);   // set의 맨 마지막에 랜덤값을 Entry로 data에 추가함
            data.notifyDataChanged();                           // data의 값 변동을 감지함

            chart2.notifyDataSetChanged();                       // chart의 값 변동을 감지함
            chart2.setVisibleXRangeMaximum(30);                  // chart에서 한 화면에 x좌표를 최대 몇개까지 출력할 것인지 정함
            chart2.moveViewToX(data.getEntryCount());            // 가장 최근에 추가한 데이터로 위치 이동
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
        if (thread2 != null)
            thread2.interrupt();                                // 널이 아닌(살아있는) 쓰레드에 인터럽트를 검

        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                addEntry();                                    // addEntry 함수를 실행
            }
        };

        thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true)                                   // 무한히 반복
                {
                    runOnUiThread(runnable);                   // UI 쓰레드에서 위에 생성한 runnable을 실행
                    try {
                        Thread.sleep(1000);          // 차트를 그리는데 0.1초의 딜레이를 줌
                    } catch (InterruptedException ie) {
                        ie.printStackTrace();              // 표준 오류 스트림으로 돌아가는 이 스크롤 가능 공간을 인쇄한다
                    }                                         // 좀 더 알아보기.
                }
            }
        });
        thread2.start();
    }                                                           // 쓰레드 시작

    @Override
    protected void onPause() {
        super.onPause();                                        // 정보를 영구적으로 저장
        if (thread2 != null)
            thread2.interrupt();                                 // null이 아닌 쓰레드에 인터럽트를 걺
    }


    /**
     * 아래는 타이머
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


    public void mOnClick(View v) {

        switch (v.getId()) {
            //시작 버튼이 눌리면

            case R.id.btnstart:

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

            case R.id.btnsplit:

                switch (mStatus) {
//진행되고 있는데 끝내기를 누르면
                    case RUNNING: //진행되고있으면
                        mTimer.removeMessages(0);

                        // String sSplit = mSplit.getText().toString();
                        String result;
                        //long realing=getEll2(); //실제 진행한시간  밀리세컨즈
                        result = String.valueOf(ell);   ///result를 디비에 넣으면돼

                        databaseReference.child("aa").child("EEG DATA").child(String.valueOf(myear + "년")).child(String.valueOf(mmonth + "월")).child(String.valueOf(mday + "일")).child("명상시간").push().setValue(String.valueOf(result)); //집중한 시간 long값

                        Log.d("명상한 총시간", result + "명상함");
                        //sSplit = String.format("달성률"+"%s\n", realing);

                        mStatus = IDLE;
                        mBtnStart.setText("시작");
                        mBtnStart.setEnabled(false);

                        mBtnSplit.setEnabled(false);
                        //텍스트뷰의 값을 바꿔줌

                        //mSplit.setText(sSplit);  //텅 비어있다가 값 뜰거임


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

/*
    Long getEll2(){
        long now = SystemClock.elapsedRealtime();
        long ell2=now-mBaseTime;
        //밀리세컨즈로 보내서 위에서 계산할거임
        return  ell2;
    }*/

    String getEllapse() {

        long now = SystemClock.elapsedRealtime();

        ell = now - mBaseTime;//현재 시간과 지난 시간을 빼서 ell값을 구하고

        // String sEll = String.format("%02d:%02d:%02d", ell / 1000 / 60, (ell/1000)%60, (ell %1000)/10);
        String sEll = String.format("%02d:%02d:%02d", ell / 1000 / 3600, (ell / 1000) / 60, (ell / 1000) % 60);
        //시간 분 초 로 바꿔준걸 반환해주는거
        return sEll;

    }

    int[] musicSetting(int value) {
        if (value == 1) {
            a = new int[]{R.raw.hawool, R.raw.lovepoem, R.raw.reisenberg};
        } else if (value == 2) {
            a = new int[]{R.raw.sugar, R.raw.reisenberg13, R.raw.reisenberg5};
        } else {
            a = a;
        }
        return a;
    }


}
