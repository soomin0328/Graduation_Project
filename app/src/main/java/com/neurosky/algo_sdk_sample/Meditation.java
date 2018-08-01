package com.neurosky.algo_sdk_sample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.Locale;

public class Meditation extends AppCompatActivity {

    EditText setTime;
    Button btn,logout;

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference("USERS");

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    private FirebaseUser currentUser;

    String n[] = new String[4];

    String name = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meditation);

        currentUser = mAuth.getCurrentUser();
        String email = currentUser.getEmail();
        Toast.makeText(this,"현재로그인: "+email,Toast.LENGTH_LONG).show();

        int idx = email.indexOf("@");
        name = email.substring(0, idx);

        setTime = (EditText)findViewById(R.id.time);
        btn = (Button)findViewById(R.id.button);
        logout = (Button)findViewById(R.id.logout);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String t = setTime.getText().toString();

                getNow();

                databaseReference.child(name).child("EEG DATA").child(n[0] + "년")
                        .child(n[1] + "월")
                        .child(n[2] + "일").child("목표시간").setValue(t);

                databaseReference.child(name).child("EEG DATA").child(n[0] + "년")
                        .child(n[1] + "월")
                        .child(n[2] + "일").child("달성률").setValue(0);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                startActivity(new Intent(Meditation.this,MainActivity.class));
                finish();
            }
        });
    }
    public String[] getNow() {
        Calendar cal = Calendar.getInstance();

        n[0] = String.format(Locale.KOREA, "%04d", cal.get(Calendar.YEAR));
        n[1] = String.format(Locale.KOREA, "%02d", cal.get(Calendar.MONTH) + 1);
        n[2] = String.format(Locale.KOREA, "%02d", cal.get(Calendar.DAY_OF_MONTH));
        n[3] = String.format(Locale.KOREA, "%02d", cal.get(Calendar.HOUR_OF_DAY));

        return n;
    }
}