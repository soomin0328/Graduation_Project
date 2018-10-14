package com.neurosky.algo_sdk_sample;

/**
 * 프로세싱 화면을 지정한 시간동안 띄워주는 코드.
 * thread를 이용해서 구현했음.
 **/

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;

import processing.android.CompatUtils;
import processing.android.PFragment;
import processing.core.PApplet;

public class GraphActivity extends AppCompatActivity {

    private PApplet sketch;

    int hour, min;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        final FrameLayout frame = new FrameLayout(this);
        frame.setId(CompatUtils.getUniqueViewId());

        int width = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getWidth();
        int height = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getHeight();

        Intent get = getIntent();
        final String pick = get.getExtras().getString("pick");

        setContentView(frame, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        sketch = new com.neurosky.algo_sdk_sample.Sketch(width, height, pick);

        final PFragment fragment = new PFragment(sketch);
        fragment.setView(frame, this);

        Intent incomingIntent = getIntent(); //aimtime 클래스에서 얻어옴
        final String data = incomingIntent.getStringExtra("data");
        hour = incomingIntent.getIntExtra("hours", 90); //설정한 목표 시간
        min = incomingIntent.getIntExtra("mins", 92); //설정한 목표시간 분

        Thread th = new Thread(new Runnable() {
            @Override
            public void run() {
                double count = 0;
                while (count < 13) {
                    count++;
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                Intent goTensorflow = new Intent(getApplicationContext(), TensorflowActivity.class);

                goTensorflow.putExtra("pick", pick);
                goTensorflow.putExtra("data", data);
                goTensorflow.putExtra("hours", hour);
                goTensorflow.putExtra("mins", min);

                startActivity(goTensorflow);
                finish();
            }
        });
        th.start();
    }
}

