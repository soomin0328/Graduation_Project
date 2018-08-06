package com.neurosky.algo_sdk_sample;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

        FrameLayout frame = new FrameLayout(this);
        frame.setId(CompatUtils.getUniqueViewId());

        int width = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getWidth();
        int height = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getHeight();


        setContentView(frame, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        sketch = new com.neurosky.algo_sdk_sample.Sketch(width, height);

        final PFragment fragment = new PFragment(sketch);
        fragment.setView(frame,this);

        Thread th = new Thread(new Runnable() {
            @Override
            public void run() {
                int count = 0;
                while (count < 2) {
                    count++;
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                stateBitmap = screenshot();

                Intent meditation = new Intent(getApplicationContext(), TensorflowActivity.class);
                ByteArrayOutputStream bs = new ByteArrayOutputStream();
                stateBitmap.compress(Bitmap.CompressFormat.PNG,50,bs);
                meditation.putExtra("byteArray", bs.toByteArray());

                startActivity(meditation);
            }
        });
        th.start();
    }

    public Bitmap screenshot(){
        View v1 = getWindow().getDecorView().getRootView();
        v1.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
        return  bitmap;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResult) {
        if (sketch != null) {
            sketch.onRequestPermissionsResult(requestCode, permissions, grantResult);
        }
    }

    @Override
    public void onNewIntent(Intent intent) {
        if (sketch != null) {
            sketch.onNewIntent(intent);
        }
    }
}

