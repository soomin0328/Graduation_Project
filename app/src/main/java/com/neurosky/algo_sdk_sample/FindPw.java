package com.neurosky.algo_sdk_sample;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class FindPw extends AppCompatActivity{

    private Button sendEmail;
    private EditText email;
    private FirebaseAuth mAuth;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find);

        mAuth = FirebaseAuth.getInstance();

        email = (EditText)findViewById(R.id.emailForFind);
        sendEmail = (Button)findViewById(R.id.sendButton);

        sendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailAddress = email.getText().toString().trim();
                Toast.makeText(FindPw.this, emailAddress, Toast.LENGTH_SHORT).show();
                sendEmail(emailAddress);
            }
        });
    }

    private void sendEmail(String e){
        mAuth.sendPasswordResetEmail(e)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(FindPw.this, "이메일을 보냈습니다.", Toast.LENGTH_SHORT).show();
                            finish();
                            startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        }else{
                            Toast.makeText(FindPw.this, "메일 보내기 실패.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
