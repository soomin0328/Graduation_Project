package com.neurosky.algo_sdk_sample;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;

import java.io.ByteArrayOutputStream;

import processing.android.CompatUtils;
import processing.android.PFragment;
import processing.core.PApplet;

public class GraphActivity extends AppCompatActivity {

    private PApplet sketch;
    Bitmap stateBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final FrameLayout frame = new FrameLayout(this);
        frame.setId(CompatUtils.getUniqueViewId());

        int width = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getWidth();
        int height = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getHeight();


        setContentView(frame, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        sketch = new com.neurosky.algo_sdk_sample.Sketch(width, height);

        final PFragment fragment = new PFragment(sketch);
        fragment.setView(frame, this);

        Thread th = new Thread(new Runnable() {
            @Override
            public void run() {
                int count = 0;
                while (count < 3) {
                    count++;
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                Intent meditation = new Intent(getApplicationContext(), TensorflowActivity.class);
//                ByteArrayOutputStream bs = new ByteArrayOutputStream();
//                stateBitmap.compress(Bitmap.CompressFormat.PNG, 50, bs);
//                meditation.putExtra("byteArray", bs.toByteArray());

                startActivity(meditation);
            }
        });
        th.start();
    }
}

