package com.neurosky.algo_sdk_sample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UserInfo extends AppCompatActivity {

    TextView userTv;
    Button logout;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        setContentView(R.layout.activity_userinfo);

        userTv = (TextView) findViewById(R.id.tv1);
        logout = (Button) findViewById(R.id.logoutButton);

        if (mAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(this, MainActivity.class));
        }

        FirebaseUser user = mAuth.getCurrentUser();

        userTv.setText("Hello " + user.getEmail() + "!!");

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLogout();
            }
        });
    }

    private void setLogout() {
        mAuth.signOut();
        finish();
        startActivity(new Intent(this, MainActivity.class));
    }
}
