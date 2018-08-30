package com.neurosky.algo_sdk_sample;

import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TimePicker;
import android.widget.Toast;

public class SelectActivity extends AppCompatActivity {
    CheckBox concent, meditate, past, now;
    Button next;

    TimePickerDialog.OnTimeSetListener tt = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            Log.d("test", "hour:" + hourOfDay + "minute" + minute);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);

        concent = (CheckBox) findViewById(R.id.ccheck); //집중 체크박스
        meditate = (CheckBox) findViewById(R.id.mcheck); //명상 체크박스
        past = (CheckBox) findViewById(R.id.past); //과거
        now = (CheckBox) findViewById(R.id.now); //현재
        next = (Button) findViewById(R.id.next); //다음으로

        concent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (meditate.isChecked() == true) {
                    meditate.setChecked(false);
                }
            }
        });
        meditate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (concent.isChecked() == true) {
                    concent.setChecked(false);
                }
            }
        });
        past.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (now.isChecked() == true) {
                    now.setChecked(false);
                }
            }
        });
        now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (past.isChecked() == true) {
                    past.setChecked(false);
                }
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //집중-과거데이터
                if (concent.isChecked() == true && past.isChecked() == true) {
                    Intent cp = new Intent(SelectActivity.this, CpActivity.class);
                    Toast.makeText(getApplicationContext(), "집중-과거", Toast.LENGTH_SHORT).show();
                    startActivity(cp);
                }

                //집중-현재
                else if (concent.isChecked() == true && now.isChecked() == true) {
                    AimTime at = new AimTime();
                    at.setListener(tt);
                    at.show(getSupportFragmentManager(), "picker");
                    Toast.makeText(getApplicationContext(), "집중 현재", Toast.LENGTH_SHORT).show();

                }

                //명상-과거
                else if (meditate.isChecked() == true && past.isChecked() == true) {
                    Intent mp = new Intent(SelectActivity.this, MpActivity.class);
                    Toast.makeText(getApplicationContext(), "명상 과거", Toast.LENGTH_SHORT).show();
                    startActivity(mp);
                }

                //명상 현재
                else if (meditate.isChecked() == true && now.isChecked() == true) {
                    Intent goEEG = new Intent(SelectActivity.this, EEG.class);
                    Toast.makeText(getApplicationContext(), "명상 현재", Toast.LENGTH_SHORT).show();
                    startActivity(goEEG);
                } else {
                    Toast.makeText(getApplicationContext(), "올바르지 않은 선택입니다", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }
}