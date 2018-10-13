package com.neurosky.algo_sdk_sample;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class SelectActivity extends AppCompatActivity {

    FirebaseAuth mAuth;

    CheckBox concent, meditate, past, now;
    Button next, logoutButton2;
    TextView nameTv2, nameTv22, emailTv2, info;

    String name, email;

    //font
    String font[] = {"fonts/nanum.ttf", "fonts/MILKYWAY.TTF"};

    TimePickerDialog.OnTimeSetListener tt = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);

        mAuth = FirebaseAuth.getInstance();

        Intent getInfo = getIntent();
        name = getInfo.getExtras().getString("name");
        email = getInfo.getExtras().getString("email");

        concent = (CheckBox) findViewById(R.id.ccheck); //집중 체크박스
        meditate = (CheckBox) findViewById(R.id.mcheck); //명상 체크박스
        past = (CheckBox) findViewById(R.id.past); //과거
        now = (CheckBox) findViewById(R.id.now); //현재
        next = (Button) findViewById(R.id.next); //다음으로

        logoutButton2 = (Button) this.findViewById(R.id.logoutBtn2);

        nameTv2 = (TextView) this.findViewById(R.id.userName2);
        nameTv22 = (TextView) this.findViewById(R.id.nim2);
        emailTv2 = (TextView) this.findViewById(R.id.email);
        info = (TextView)this.findViewById(R.id.infoTv);

        nameTv2.setTypeface(Typeface.createFromAsset(getAssets(), font[0]));
        nameTv22.setTypeface(Typeface.createFromAsset(getAssets(), font[0]));
        emailTv2.setTypeface(Typeface.createFromAsset(getAssets(), font[0]));
        logoutButton2.setTypeface(Typeface.createFromAsset(getAssets(), font[0]));
        info.setTypeface(Typeface.createFromAsset(getAssets(), font[0]));

        nameTv2.setText(name);
        emailTv2.setText(email);

        logoutButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Intent logout = new Intent(SelectActivity.this, MainActivity.class);
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.GINGERBREAD_MR1) {
                    logout.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                } else {
                    logout.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                }
                startActivity(logout);
                overridePendingTransition(R.anim.fadein, R.anim.fadeout);
            }
        });

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
                    startActivity(cp);
                }

                //집중-현재
                else if (concent.isChecked() == true && now.isChecked() == true) {
                    AimTime at = new AimTime();
                    at.setListener(tt);
                    at.show(getSupportFragmentManager(), "picker");
                }

                //명상-과거
                else if (meditate.isChecked() == true && past.isChecked() == true) {
                    Intent mp = new Intent(SelectActivity.this, MpActivity.class);
                    startActivity(mp);
                }

                //명상 현재
                else if (meditate.isChecked() == true && now.isChecked() == true) {
                    Intent mn = new Intent(SelectActivity.this, GraphActivity.class);
                    mn.putExtra("pick", "mn");
                    startActivity(mn);
                } else {
                    info.setText("올바르지 않은 선택입니다!");
                }

            }
        });

    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(0, 0);
    }
}