package com.neurosky.algo_sdk_sample;

/**
 * 파이어베이스를 이용한 회원가입 구현.
 * **/


import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUp extends AppCompatActivity {

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    Button enroll, back;
    EditText idText, pwText, pw2Text, nickname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        back = (Button) findViewById(R.id.backButton);
        enroll = (Button) findViewById(R.id.loginlButton);
        enroll.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/MILKYWAY.TTF"));

        idText = (EditText) findViewById(R.id.emailInput);
        pwText = (EditText) findViewById(R.id.passwordInput);
        pw2Text = (EditText) findViewById(R.id.passwordCheck);
        nickname = (EditText) findViewById(R.id.userNickname);

        enroll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createUser(idText.getText().toString().trim(), pwText.getText().toString().trim());
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void createUser(final String email, final String password) {

        String id = idText.getText().toString();
        String pw = pwText.getText().toString();
        String pw2 = pw2Text.getText().toString();
        String nick = nickname.getText().toString();

        Intent intent = new Intent(SignUp.this, CustomDialog.class);

        if (id.equals("")) {
            intent.putExtra("activity", "enterEmail");
            startActivity(intent);
        } else if (pw.equals("")) {
            intent.putExtra("activity", "enterPassword");
            startActivity(intent);
        } else if (pw2.equals("")) {
            intent.putExtra("activity", "enterPassword2");
            startActivity(intent);
        } else if (nick.equals("")) {
            intent.putExtra("activity", "enterNickname");
            startActivity(intent);
        } else {
            if (!checkPw(pw, pw2)) {
                intent.putExtra("activity", "pwIncorrect");
                startActivity(intent);
                pw2Text.setText("");
            } else {
                int idx = id.indexOf("@");
                final String userId = id.substring(0,idx);

                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (!task.isSuccessful()) {
                                    Toast.makeText(SignUp.this, "회원가입 실패", Toast.LENGTH_SHORT).show();
                                    idText.setText("");
                                    pwText.setText("");
                                    pw2Text.setText("");
                                } else {
                                    Toast.makeText(SignUp.this, "회원가입 성공", Toast.LENGTH_SHORT).show();

                                    databaseReference.child("USERS").child(userId).child("NICKNAME").setValue(nickname.getText().toString());
                                    databaseReference.child("USERS").child(userId).child("EMAIL").setValue(email);
                                    Intent goMain = new Intent(SignUp.this, MainActivity.class);
                                    startActivity(goMain);
                                    finish();
                                }
                            }
                        });
            }
        }
    }

    private boolean checkPw(String pw1, String pw2) {
        if (pw1.equals(pw2)) {
            return true;
        } else
            return false;
    }
}
