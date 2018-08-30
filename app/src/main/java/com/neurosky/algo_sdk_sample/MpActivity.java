package com.neurosky.algo_sdk_sample;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MpActivity extends AppCompatActivity implements View.OnClickListener {  //명상 과거
    Button btnday, btnweek, btnmonth; //차례대로 일별 주별 월별

    FragmentManager fm;
    FragmentTransaction tran;

    MmonthFrag frag1;//월별                      이거 주별달력으로 연결되있으니까 바꿔야해애~~~!! MonthFrag아니고 mMonthFrang 이런식으로 명상전용달ㄹ력 하나 만들어서 ㅇㅇ
    MweekFrag frag2;//주별
    MdayFrag frag3;//일별

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mp);
        btnmonth = findViewById(R.id.mbtnMonth);//월별
        btnweek = findViewById(R.id.mbtnweek); //주별
        btnday = findViewById(R.id.mbtnday); //일별

        btnday.setOnClickListener(this);
        btnweek.setOnClickListener(this);
        btnmonth.setOnClickListener(this);

        frag1 = new MmonthFrag();
        frag2 = new MweekFrag();
        frag3 = new MdayFrag();

        loading();
        setFrag(0);
    }

    //명상쪽이라 앞에 m붙음
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mbtnMonth:
                setFrag(0);
                loading();
                break;
            case R.id.mbtnweek:
                setFrag(1);
                loading();
                break;
            case R.id.mbtnday:
                setFrag(2);
                loading();
                break;
        }
    }

    public void setFrag(int frag) { //프래그먼트 교체하는 작업
        fm = getFragmentManager();
        tran = fm.beginTransaction();
        switch (frag) {
            case 0:
                tran.replace(R.id.mp_frame, frag1);
                tran.commit();
                break;
            case 1:
                tran.replace(R.id.mp_frame, frag2);
                tran.commit();
                break;
            case 2:
                tran.replace(R.id.mp_frame, frag3);
                tran.commit();
                break;
        }
    }

    public void loading() {
        final ProgressDialog dialog = ProgressDialog.show(MpActivity.this, "Loading", "Get calendar data...", true, true);
        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();
            }
        }, 4000);
    }
}
