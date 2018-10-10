package com.neurosky.algo_sdk_sample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class CustomDialog extends AppCompatActivity {

    TextView tv;
    Button bt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpWindow.dimAmount = 0.8f;
        getWindow().setAttributes(lpWindow);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setContentView(R.layout.activity_dialog);

        tv = (TextView) findViewById(R.id.message);
        bt = (Button) findViewById(R.id.okButton);

        final Intent intent = getIntent();
        String activity = intent.getStringExtra("activity");

        switch (activity){
            case "loginFail":
                tv.setText("Your id or password is incorrect.");
                break;
            case "loginSuccess":
                tv.setText("Login Success!");
                break;
            case "signupSuccess":
                tv.setText("Sign Up Success!");
                break;
            case "signupFail":
                tv.setText("Sign Up Fail. Please fill all text.");
                break;
            case "enterEmail":
                tv.setText("Please enter your E-mail.");
                break;
            case "enterPassword":
                tv.setText("Please enter your Password.");
                break;
            case "enterPassword2":
                tv.setText("Please enter your Password checking.");
                break;
            case "enterNickname":
                tv.setText("Please enter your Nickname.");
                break;
            case "pwIncorrect":
                tv.setText("Please check your password.");
                break;
            default:
                break;

        }

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        if(event.getAction()==MotionEvent.ACTION_OUTSIDE){
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed(){
        return;
    }
}
