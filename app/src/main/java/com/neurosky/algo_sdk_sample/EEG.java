package com.neurosky.algo_sdk_sample;

/**
 * 주로 봐야 할 부분
 * 1. nskAlgoSdk.setOnBPAlgoIndexListener -> 측정된 뇌파 값이 파이어베이스에 저장됨.
 * 2. startButton.setOnClickListener -> start버튼을 누르면 실행됨. 뇌파 측정이 시작되고 그래프 그리는 화면으로 넘어감.
 * **/


import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.neurosky.AlgoSdk.NskAlgoDataType;
import com.neurosky.AlgoSdk.NskAlgoSdk;
import com.neurosky.AlgoSdk.NskAlgoSignalQuality;
import com.neurosky.AlgoSdk.NskAlgoState;
import com.neurosky.AlgoSdk.NskAlgoType;
import com.neurosky.connection.ConnectionStates;
import com.neurosky.connection.DataType.MindDataType;
import com.neurosky.connection.TgStreamHandler;
import com.neurosky.connection.TgStreamReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.Locale;

public class EEG extends Activity {

    final String TAG = "EEGTag";

    FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference("USERS");

    // COMM SDK handles
    private TgStreamReader tgStreamReader;
    private BluetoothAdapter mBluetoothAdapter;

    // internal variables
    private boolean bInited = false;
    private boolean bRunning = false;
    private NskAlgoType currentSelectedAlgo;

    // canned data variables
    private short raw_data[] = {0};
    private int raw_data_index = 0;
    private float output_data[];
    private int output_data_count = 0;
    private int raw_data_sec_len = 85;

    private Button headsetButton, setAlgosButton, startButton, stopButton, logoutButton;

    private CheckBox attCheckBox, medCheckBox, bpCheckBox;

    private TextView stateText, sqText;

    private NskAlgoSdk nskAlgoSdk;

    String n[] = new String[6];
    String eeg[] = new String[5];

    String name = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        setContentView(R.layout.activity_eeg);

        nskAlgoSdk = new NskAlgoSdk();

        FirebaseUser user = mAuth.getCurrentUser();
        String email = user.getEmail();

        int idx = email.indexOf("@");
        name = email.substring(0, idx);

        try {
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
//                Toast.makeText(
//                        this,
//                        "Please enable your Bluetooth and re-run this program !",
//                        Toast.LENGTH_LONG).show();
                //finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.i(TAG, "error:" + e.getMessage());
            return;
        }

        headsetButton = (Button) this.findViewById(R.id.headsetButton);
//        cannedButton = (Button) this.findViewById(R.id.cannedDatabutton);
        setAlgosButton = (Button) this.findViewById(R.id.setAlgosButton);
        startButton = (Button) this.findViewById(R.id.startButton);
        stopButton = (Button) this.findViewById(R.id.stopButton);
        logoutButton = (Button) this.findViewById(R.id.logoutBtn);

        attCheckBox = (CheckBox) this.findViewById(R.id.attCheckBox);
        medCheckBox = (CheckBox) this.findViewById(R.id.medCheckBox);
        bpCheckBox = (CheckBox) this.findViewById(R.id.bpCheckBox);

        stateText = (TextView) this.findViewById(R.id.stateText);
        sqText = (TextView) this.findViewById(R.id.sqText);

        headsetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                output_data_count = 0;
                output_data = null;

                raw_data = new short[512];
                raw_data_index = 0;

//                cannedButton.setEnabled(false);
                headsetButton.setEnabled(false);

                startButton.setEnabled(false);


                tgStreamReader = new TgStreamReader(mBluetoothAdapter, callback);

                if (tgStreamReader != null && tgStreamReader.isBTConnected()) {

                    // Prepare for connecting
                    tgStreamReader.stop();
                    tgStreamReader.close();
                }

                tgStreamReader.connect();
            }
        });

//        cannedButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                output_data_count = 0;
//                output_data = null;
//
//                System.gc();
//
//                headsetButton.setEnabled(false);
//                cannedButton.setEnabled(false);
//
//                AssetManager assetManager = getAssets();
//                InputStream inputStream = null;
//
//                try {
//                    int j;
//                    // check the output count first
//                    inputStream = assetManager.open("output_data.bin");
//                    output_data_count = 0;
//                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
//                    try {
//                        String line = reader.readLine();
//                        while (!(line == null || line.isEmpty())) {
//                            output_data_count++;
//                            line = reader.readLine();
//                        }
//                    } catch (IOException e) {
//
//                    }
//                    inputStream.close();
//
//                    if (output_data_count > 0) {
//                        inputStream = assetManager.open("output_data.bin");
//                        output_data = new float[output_data_count];
//                        //ap = new float[output_data_count];
//                        j = 0;
//                        reader = new BufferedReader(new InputStreamReader(inputStream));
//                        try {
//                            String line = reader.readLine();
//                            while (j < output_data_count) {
//                                output_data[j++] = Float.parseFloat(line);
//                                line = reader.readLine();
//                            }
//                        } catch (IOException e) {
//
//                        }
//                        inputStream.close();
//                    }
//                } catch (IOException e) {
//                }
//
//                Log.d(TAG, "Reading raw data");
//                try {
//                    inputStream = assetManager.open("raw_data_em.bin");
//                    raw_data = readData(inputStream, 512 * raw_data_sec_len);
//                    raw_data_index = 512 * raw_data_sec_len;
//                    inputStream.close();
//                    nskAlgoSdk.NskAlgoDataStream(NskAlgoDataType.NSK_ALGO_DATA_TYPE_BULK_EEG.value, raw_data, 512 * raw_data_sec_len);
//                } catch (IOException e) {
//
//                }
//                Log.d(TAG, "Finished reading data");
//            }
//        });

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bRunning == false) {
                    nskAlgoSdk.NskAlgoStart(false);     //Start EEG data measurement.

                    //Start drawing the graph.
                    Intent graphIntent = new Intent(getApplicationContext(), GraphActivity.class);
                    startActivity(graphIntent);

//                    Thread t = new Thread(new Runnable() {
//                        @Override
//                        public void run() {
//                            try{
//                                Thread.sleep(3000);
//                            }catch (InterruptedException e){
//
//                            }
//                            Intent graphIntent = new Intent(getApplicationContext(), GraphActivity.class);
//                            startActivity(graphIntent);
//                        }
//                    });
//                    t.start();
                } else {
                    nskAlgoSdk.NskAlgoPause();

                }
            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nskAlgoSdk.NskAlgoStop();
            }
        });

        setAlgosButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int algoTypes = 0;// = NskAlgoType.NSK_ALGO_TYPE_CR.value;

                startButton.setEnabled(false);
                stopButton.setEnabled(false);

                currentSelectedAlgo = NskAlgoType.NSK_ALGO_TYPE_INVALID;

                stateText.setText("");
                sqText.setText("");

                if (medCheckBox.isChecked()) {
                    algoTypes += NskAlgoType.NSK_ALGO_TYPE_MED.value;
                }
                if (attCheckBox.isChecked()) {
                    algoTypes += NskAlgoType.NSK_ALGO_TYPE_ATT.value;
                }
                if (bpCheckBox.isChecked()) {
                    algoTypes += NskAlgoType.NSK_ALGO_TYPE_BP.value;
                }

                if (algoTypes == 0) {
                    showDialog("Please select at least one algorithm");
                } else {
                    if (bInited) {
                        nskAlgoSdk.NskAlgoUninit();
                        bInited = false;
                    }
                    int ret = nskAlgoSdk.NskAlgoInit(algoTypes, getFilesDir().getAbsolutePath());
                    if (ret == 0) {
                        bInited = true;
                    }

                    Log.d(TAG, "NSK_ALGO_Init() " + ret);
                    String sdkVersion = "SDK ver.: " + nskAlgoSdk.NskAlgoSdkVersion();

                    if ((algoTypes & NskAlgoType.NSK_ALGO_TYPE_ATT.value) != 0) {
                        sdkVersion += "\nATT ver.: " + nskAlgoSdk.NskAlgoAlgoVersion(NskAlgoType.NSK_ALGO_TYPE_ATT.value);
                    }
                    if ((algoTypes & NskAlgoType.NSK_ALGO_TYPE_MED.value) != 0) {
                        sdkVersion += "\nMED ver.: " + nskAlgoSdk.NskAlgoAlgoVersion(NskAlgoType.NSK_ALGO_TYPE_MED.value);
                    }
                    if ((algoTypes & NskAlgoType.NSK_ALGO_TYPE_BLINK.value) != 0) {
                        sdkVersion += "\nBlink ver.: " + nskAlgoSdk.NskAlgoAlgoVersion(NskAlgoType.NSK_ALGO_TYPE_BLINK.value);
                    }
                    if ((algoTypes & NskAlgoType.NSK_ALGO_TYPE_BP.value) != 0) {
                        sdkVersion += "\nEEG Bandpower ver.: " + nskAlgoSdk.NskAlgoAlgoVersion(NskAlgoType.NSK_ALGO_TYPE_BP.value);
                    }
                    showToast(sdkVersion, Toast.LENGTH_LONG);
                }
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                startActivity(new Intent(EEG.this, MainActivity.class));
                finish();
                Toast.makeText(getApplicationContext(), "Logout Success!", Toast.LENGTH_LONG).show();
            }
        });

        nskAlgoSdk.setOnSignalQualityListener(new NskAlgoSdk.OnSignalQualityListener() {
            @Override
            public void onSignalQuality(int level) {
                final int fLevel = level;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // change UI elements here
                        String sqStr = NskAlgoSignalQuality.values()[fLevel].toString();
                        sqText.setText(sqStr);
                    }
                });
            }
        });

        nskAlgoSdk.setOnStateChangeListener(new NskAlgoSdk.OnStateChangeListener() {
            @Override
            public void onStateChange(int state, int reason) {
                String stateStr = "";
                String reasonStr = "";
                for (NskAlgoState s : NskAlgoState.values()) {
                    if (s.value == state) {
                        stateStr = s.toString();
                    }
                }
                for (NskAlgoState r : NskAlgoState.values()) {
                    if (r.value == reason) {
                        reasonStr = r.toString();
                    }
                }

                final String finalStateStr = stateStr + " | " + reasonStr;
                final int finalState = state;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // change UI elements here
                        stateText.setText(finalStateStr);

                        if (finalState == NskAlgoState.NSK_ALGO_STATE_RUNNING.value || finalState == NskAlgoState.NSK_ALGO_STATE_COLLECTING_BASELINE_DATA.value) {
                            bRunning = true;
                            startButton.setText("Pause");
                            startButton.setEnabled(true);
                            stopButton.setEnabled(true);
                        } else if (finalState == NskAlgoState.NSK_ALGO_STATE_STOP.value) {
                            bRunning = false;
                            raw_data = null;
                            raw_data_index = 0;
                            startButton.setText("Start");
                            startButton.setEnabled(true);
                            stopButton.setEnabled(false);

                            headsetButton.setEnabled(true);
//                            cannedButton.setEnabled(true);

                            if (tgStreamReader != null && tgStreamReader.isBTConnected()) {

                                // Prepare for connecting
                                tgStreamReader.stop();
                                tgStreamReader.close();
                            }

                            output_data_count = 0;
                            output_data = null;

                            System.gc();
                        } else if (finalState == NskAlgoState.NSK_ALGO_STATE_PAUSE.value) {
                            bRunning = false;
                            startButton.setText("Start");
                            startButton.setEnabled(true);
                            stopButton.setEnabled(true);
                        } else if (finalState == NskAlgoState.NSK_ALGO_STATE_ANALYSING_BULK_DATA.value) {
                            bRunning = true;
                            startButton.setText("Start");
                            startButton.setEnabled(false);
                            stopButton.setEnabled(true);
                        } else if (finalState == NskAlgoState.NSK_ALGO_STATE_INITED.value || finalState == NskAlgoState.NSK_ALGO_STATE_UNINTIED.value) {
                            bRunning = false;
                            startButton.setText("Start");
                            startButton.setEnabled(true);
                            stopButton.setEnabled(false);
                        }
                    }
                });
            }
        });

        nskAlgoSdk.setOnSignalQualityListener(new NskAlgoSdk.OnSignalQualityListener() {
            @Override
            public void onSignalQuality(final int level) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String sqStr = NskAlgoSignalQuality.values()[level].toString();
                        sqText.setText(sqStr);
                    }
                });
            }
        });

        nskAlgoSdk.setOnBPAlgoIndexListener(new NskAlgoSdk.OnBPAlgoIndexListener() {
            @Override
            public void onBPAlgoIndex(final float delta, final float theta, final float alpha, final float beta, final float gamma) {

                final float fDelta = delta, fTheta = theta, fAlpha = alpha, fBeta = beta, fGamma = gamma;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        getNow();

                        eeg[0] = String.valueOf(alpha);  //alpha
                        eeg[1] = String.valueOf(theta);  //low_beta
                        eeg[2] = String.valueOf(delta);  //delta
                        eeg[3] = String.valueOf(gamma);  //gamma
                        eeg[4] = String.valueOf(beta);  //theta

                        final Nomalization nz = new Nomalization(eeg[0], eeg[1], eeg[2], eeg[3], eeg[4]);

//                        Handler m = new Handler(Looper.getMainLooper());
//                        m.postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                String d = nz.getData();
//                            }
//                        }, 3000);

                        databaseReference.child(name).child("EEG DATA").child(n[0] + "년")
                                .child(n[1] + "월")
                                .child(n[2] + "일")
                                .child(n[3] + "시")
                                .child(n[4] + "분")
                                .child(n[5] + "초")
                                .child("Alpha").setValue(fAlpha);
                        databaseReference.child(name).child("EEG DATA").child(n[0] + "년")
                                .child(n[1] + "월")
                                .child(n[2] + "일")
                                .child(n[3] + "시")
                                .child(n[4] + "분")
                                .child(n[5] + "초")
                                .child("Beta").setValue(fTheta);
                        databaseReference.child(name).child("EEG DATA").child(n[0] + "년")
                                .child(n[1] + "월")
                                .child(n[2] + "일")
                                .child(n[3] + "시")
                                .child(n[4] + "분")
                                .child(n[5] + "초")
                                .child("Delta").setValue(fDelta);
                        databaseReference.child(name).child("EEG DATA").child(n[0] + "년")
                                .child(n[1] + "월")
                                .child(n[2] + "일")
                                .child(n[3] + "시")
                                .child(n[4] + "분")
                                .child(n[5] + "초")
                                .child("Gamma").setValue(fGamma);
                        databaseReference.child(name).child("EEG DATA").child(n[0] + "년")
                                .child(n[1] + "월")
                                .child(n[2] + "일")
                                .child(n[3] + "시")
                                .child(n[4] + "분")
                                .child(n[5] + "초")
                                .child("Theta").setValue(fBeta);

                    }
                });
            }
        });

        nskAlgoSdk.setOnAttAlgoIndexListener(new NskAlgoSdk.OnAttAlgoIndexListener() {
            @Override
            public void onAttAlgoIndex(final int value) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        getNow();

                        if (value != 0) {
                            databaseReference.child(name).child("EEG DATA").child(n[0] + "년")
                                    .child(n[1] + "월")
                                    .child(n[2] + "일")
                                    .child(n[3] + "시")
                                    .child(n[4] + "분")
                                    .child(n[5] + "초")
                                    .child("ATT").setValue(value);
                        }
                    }
                });
            }
        });

        nskAlgoSdk.setOnMedAlgoIndexListener(new NskAlgoSdk.OnMedAlgoIndexListener() {
            @Override
            public void onMedAlgoIndex(final int value) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        getNow();

                        if (value != 0) {
                            databaseReference.child(name).child("EEG DATA").child(n[0] + "년")
                                    .child(n[1] + "월")
                                    .child(n[2] + "일")
                                    .child(n[3] + "시")
                                    .child(n[4] + "분")
                                    .child(n[5] + "초")
                                    .child("MED").setValue(value);
                        }
                    }
                });
            }
        });
    }

    private short[] readData(InputStream is, int size) {
        short data[] = new short[size];
        int lineCount = 0;
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        try {
            while (lineCount < size) {
                String line = reader.readLine();
                if (line == null || line.isEmpty()) {
                    Log.d(TAG, "lineCount=" + lineCount);
                    break;
                }
                data[lineCount] = Short.parseShort(line);
                lineCount++;
            }
            Log.d(TAG, "lineCount=" + lineCount);
        } catch (IOException e) {

        }
        return data;
    }

    @Override
    public void onBackPressed() {
        nskAlgoSdk.NskAlgoUninit();
        finish();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    private TgStreamHandler callback = new TgStreamHandler() {

        @Override
        public void onStatesChanged(int connectionStates) {
            // TODO Auto-generated method stub
            Log.d(TAG, "connectionStates change to: " + connectionStates);
            switch (connectionStates) {
                case ConnectionStates.STATE_CONNECTING:
                    // Do something when connecting
                    break;
                case ConnectionStates.STATE_CONNECTED:
                    // Do something when connected
                    tgStreamReader.start();
                    showToast("Connected", Toast.LENGTH_SHORT);
                    break;
                case ConnectionStates.STATE_WORKING:
                    // Do something when working

                    //(9) demo of recording raw data , stop() will call stopRecordRawData,
                    //or you can add a button to control it.
                    //You can change the save path by calling setRecordStreamFilePath(String filePath) before startRecordRawData
                    //tgStreamReader.startRecordRawData();

                    EEG.this.runOnUiThread(new Runnable() {
                        public void run() {
                            Button startButton = (Button) findViewById(R.id.startButton);
                            startButton.setEnabled(true);
                        }

                    });

                    break;
                case ConnectionStates.STATE_GET_DATA_TIME_OUT:
                    // Do something when getting data timeout

                    //(9) demo of recording raw data, exception handling
                    //tgStreamReader.stopRecordRawData();

                    showToast("Get data time out!", Toast.LENGTH_SHORT);

                    if (tgStreamReader != null && tgStreamReader.isBTConnected()) {
                        tgStreamReader.stop();
                        tgStreamReader.close();
                    }

                    break;
                case ConnectionStates.STATE_STOPPED:
                    // Do something when stopped
                    // We have to call tgStreamReader.stop() and tgStreamReader.close() much more than
                    // tgStreamReader.connectAndstart(), because we have to prepare for that.

                    break;
                case ConnectionStates.STATE_DISCONNECTED:
                    // Do something when disconnected
                    break;
                case ConnectionStates.STATE_ERROR:
                    // Do something when you get error message
                    break;
                case ConnectionStates.STATE_FAILED:
                    // Do something when you get failed message
                    // It always happens when open the BluetoothSocket error or timeout
                    // Maybe the device is not working normal.
                    // Maybe you have to try again
                    break;
            }
        }

        @Override
        public void onRecordFail(int flag) {
            // You can handle the record error message here
            Log.e(TAG, "onRecordFail: " + flag);

        }

        @Override
        public void onChecksumFail(byte[] payload, int length, int checksum) {
            // You can handle the bad packets here.
        }

        @Override
        public void onDataReceived(int datatype, int data, Object obj) {
            // You can handle the received data here
            // You can feed the raw data to algo sdk here if necessary.
            //Log.i(TAG,"onDataReceived");
            switch (datatype) {
                case MindDataType.CODE_ATTENTION:
                    short attValue[] = {(short) data};
                    nskAlgoSdk.NskAlgoDataStream(NskAlgoDataType.NSK_ALGO_DATA_TYPE_ATT.value, attValue, 1);
                    break;
                case MindDataType.CODE_MEDITATION:
                    short medValue[] = {(short) data};
                    nskAlgoSdk.NskAlgoDataStream(NskAlgoDataType.NSK_ALGO_DATA_TYPE_MED.value, medValue, 1);
                    break;
                case MindDataType.CODE_POOR_SIGNAL:
                    short pqValue[] = {(short) data};
                    nskAlgoSdk.NskAlgoDataStream(NskAlgoDataType.NSK_ALGO_DATA_TYPE_PQ.value, pqValue, 1);
                    break;
                case MindDataType.CODE_RAW:
                    raw_data[raw_data_index++] = (short) data;
                    if (raw_data_index == 512) {
                        nskAlgoSdk.NskAlgoDataStream(NskAlgoDataType.NSK_ALGO_DATA_TYPE_EEG.value, raw_data, raw_data_index);
                        raw_data_index = 0;
                    }
                    break;
                default:
                    break;
            }
        }

    };

    public void showToast(final String msg, final int timeStyle) {
        EEG.this.runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(getApplicationContext(), msg, timeStyle).show();
            }

        });
    }

    private void showDialog(String message) {
        new AlertDialog.Builder(this)
                .setTitle("")
                .setMessage(message)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    public String[] getNow() {
        Calendar cal = Calendar.getInstance();

        n[0] = String.format(Locale.KOREA, "%04d", cal.get(Calendar.YEAR));
        n[1] = String.format(Locale.KOREA, "%02d", cal.get(Calendar.MONTH) + 1);
        n[2] = String.format(Locale.KOREA, "%02d", cal.get(Calendar.DAY_OF_MONTH));
        n[3] = String.format(Locale.KOREA, "%02d", cal.get(Calendar.HOUR_OF_DAY));
        n[4] = String.format(Locale.KOREA, "%02d", cal.get(Calendar.MINUTE));
        n[5] = String.format(Locale.KOREA, "%02d", cal.get(Calendar.SECOND));

        return n;
    }
}
