package com.neurosky.algo_sdk_sample;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class CpActivity extends AppCompatActivity implements View.OnClickListener { //집중 과거
    Button btnday, btnweek, btnmonth; //차례대로 일별 주별 월별
    FragmentManager fm;
    FragmentTransaction tran;
    MonthFrag frag1;//월별
    WeekFrag frag2;//주별
    DayFrag frag3;//일별

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cp);
        btnmonth = findViewById(R.id.btnMonth);//월별
        btnweek = findViewById(R.id.btnweek); //주별
        btnday = findViewById(R.id.btnday); //일별

        btnday.setOnClickListener(this);
        btnweek.setOnClickListener(this);
        btnmonth.setOnClickListener(this);

        frag1 = new MonthFrag();
        frag2 = new WeekFrag();
        frag3 = new DayFrag();

        loading();
        setFrag(0);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnMonth:
                setFrag(0);
                loading();
                break;
            case R.id.btnweek:
                setFrag(1);
                loading();
                break;
            case R.id.btnday:
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
                tran.replace(R.id.cp_frame, frag1);
                tran.commit();
                break;
            case 1:
                tran.replace(R.id.cp_frame, frag2);
                tran.commit();
                break;
            case 2:
                tran.replace(R.id.cp_frame, frag3);
                tran.commit();
                break;
        }
    }

    public void loading() {
        final ProgressDialog dialog = ProgressDialog.show(CpActivity.this, "Loading", "Get calendar data...", true, true);
        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();
            }
        }, 4000);
    }
}
