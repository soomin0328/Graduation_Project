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

    private PApplet sketch, sketch2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final FrameLayout frame = new FrameLayout(this);
        frame.setId(CompatUtils.getUniqueViewId());

        int width = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getWidth();
        int height = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getHeight();

        setContentView(frame, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

//        Intent get = getIntent();
//        String now = get.getStringExtra("now");
//
//        Log.e("now", now);

//        if (now.equals("Cn")){
//            sketch = new com.neurosky.algo_sdk_sample.Sketch(width, height);
//
//            final PFragment fragment = new PFragment(sketch);
//            fragment.setView(frame, this);
//        }else if (now.equals("Mn")){
//            sketch2 = new com.neurosky.algo_sdk_sample.Sketch(width, height);
//
//            final PFragment fragment = new PFragment(sketch2);
//            fragment.setView(frame, this);
//        }

        sketch = new com.neurosky.algo_sdk_sample.Sketch(width, height);

        final PFragment fragment = new PFragment(sketch);
        fragment.setView(frame, this);

        Thread th = new Thread(new Runnable() {
            @Override
            public void run() {
                double count = 0;
                while (count < 11) {
                    count++;
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                Intent goTensorflow = new Intent(getApplicationContext(), TensorflowActivity.class);
                startActivity(goTensorflow);
                finish();
            }
        });
        th.start();
    }
}

