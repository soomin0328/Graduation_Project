package com.neurosky.algo_sdk_sample;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.File;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class TensorflowActivity extends AppCompatActivity {

    private static final int INPUT_SIZE = 300;
    private static final int IMAGE_MEAN = 128;
    private static final float IMAGE_STD = 128f;
    private static final String INPUT_NAME = "Mul";
    private static final String OUTPUT_NAME = "final_result";

    private static final String MODEL_FILE = "file:///android_asset/stripped_graph.pb";
    private static final String LABEL_FILE =
            "file:///android_asset/eeg_labels.txt";

    private Classifier classifier;
    private Executor executor = Executors.newSingleThreadExecutor();
    private TextView textViewResult, stateR;
    private Button btnDetectObject, btnNext;
    private ImageView imageView;

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tf);

        mContext = this;

        stateR = (TextView)findViewById(R.id.state);
        textViewResult = (TextView) findViewById(R.id.textViewResult);
        textViewResult.setMovementMethod(new ScrollingMovementMethod());

        btnDetectObject = (Button) findViewById(R.id.btnDetect);
        btnNext = (Button) findViewById(R.id.btnNext);

        imageView = (ImageView) findViewById(R.id.stateImage);

        Intent get = getIntent();
        final String pick = get.getExtras().getString("pick");

        importImage();

        btnDetectObject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //image -> bitmap
                Drawable drawable = imageView.getDrawable();
                Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
                bitmap = Bitmap.createScaledBitmap(bitmap, INPUT_SIZE, INPUT_SIZE, true);

                try{
                    //Classifier
                    if (bitmap != null) {
                        final List<Classifier.Recognition> results = classifier.recognizeImage(bitmap);

                        String state = getState(results.toString());

                        textViewResult.setText(state);
                    } else {
                        Toast.makeText(TensorflowActivity.this, "not null", Toast.LENGTH_LONG).show();
                    }
                }catch (NullPointerException e){
                    e.printStackTrace();
                }

            }
        });

        //Go to the next screen.
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pick.equals("mn")) {
                    Toast.makeText(getApplicationContext(), "명상 현재", Toast.LENGTH_LONG).show();
                    Intent goMn = new Intent(TensorflowActivity.this, MnActivity.class);
                    startActivity(goMn);
                    finish();
                } else if (pick.equals("cn")) {
                    Toast.makeText(getApplicationContext(), "집중 현재", Toast.LENGTH_LONG).show();
                    Intent goCn = new Intent(TensorflowActivity.this, CnActivity.class);
                    startActivity(goCn);
                    finish();
                }
            }
        });

        initTensorFlowAndLoadModel();
    }

    //Import a captured image from the phone.
    private void importImage() {
        File imgFile = new File(Environment.
                getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString()
                + "/test.jpg");

        if (imgFile.exists()) {
            Bitmap imgBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            imageView.setImageBitmap(imgBitmap);
        }
    }

    private void initTensorFlowAndLoadModel() {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    classifier = TensorFlowImageClassifier.create(
                            getAssets(),
                            MODEL_FILE,
                            LABEL_FILE,
                            INPUT_SIZE,
                            IMAGE_MEAN,
                            IMAGE_STD,
                            INPUT_NAME,
                            OUTPUT_NAME);
                } catch (final Exception e) {
                    throw new RuntimeException("Error initializing TensorFlow!", e);
                }
            }
        });
    }

    private String getState(String s) {
        String s2 = s.substring(2,3);
        String s3 = null;

        if (s2.equals("0")){
            s3 = s.substring(5,19);
            return s3;
        }if(s2.equals("1")){
            if (s.substring(14,15).equals("%")){
                s3 = s.substring(5,16);
                stateR.setText("집중 최대 상태입니당^^.");
            }else {
                s3 = s.substring(5,17);
            }

            return s3;
        }else {
            return s2;
        }
    }
}
