package com.neurosky.algo_sdk_sample;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);

        Handler hd = new Handler();
        hd.postDelayed(new splashhandler(), 1200);

    }

    private class splashhandler implements Runnable {
        public void run() {
            startActivity(new Intent(getApplication(), MainActivity.class));
            overridePendingTransition(R.anim.fadein,R.anim.fadeout);
            SplashActivity.this.finish();
        }
    }

    @Override
    public void onBackPressed() {
        //초반 플래시 화면에서 넘어갈때 뒤로가기 버튼 못누르게 함
    }

}