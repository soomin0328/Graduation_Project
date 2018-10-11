package com.neurosky.algo_sdk_sample;

import android.os.Environment;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Random;

import processing.core.PApplet;

public class Sketch extends PApplet {

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user = mAuth.getCurrentUser();
    String email = user.getEmail();
    int idx = email.indexOf("@");
    String name = email.substring(0, idx);

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference("USERS").child(name).child("EEG DATA");

    // data input (arraylist)
    private ArrayList<String> result_alpha = new ArrayList<>(); // real-time alpha data
    private ArrayList<String> result_low_beta = new ArrayList<>(); // real-time beta data
    private ArrayList<String> result_delta = new ArrayList<>(); // real-time delta data
    private ArrayList<String> result_gamma = new ArrayList<>(); // real-time gamma data
    private ArrayList<String> result_theta = new ArrayList<>(); // real-time theta data
    private ArrayList<String> result_smr = new ArrayList<>();
    private ArrayList<String> result_high_beta = new ArrayList<>();

    int width, height, count = 0, number = 1, j = 0, num = 1, c = 0;
    int formResolution = 7;
    int stepSize = 1; // random size
    float centerX, centerY;
    String state;

    public Sketch(int a, int b, String s) {
        this.width = a;
        this.height = b;
        this.state = s;
    }

    String n[] = new String[6];

    // graph size
    float initRadius1 = 150;
    float initRadius2 = 160;
    float initRadius3 = 170;
    float initRadius4 = 180;
    float initRadius5 = 190;
    float initRadius6 = 210;
    float initRadius7 = 175;
    float initRadius8 = 185;
    float initRadius9 = 195;
    float initRadius10 = 205;
    float initRadius11 = 210;
    float initRadius12 = 215;
    float initRadius13 = 225;
    float initRadius14 = 235;
    float initRadius15 = 235;
    float initRadius16 = 240;
    float initRadius17 = 245;
    float initRadius18 = 265;
    float initRadius19 = 285;
    float initRadius20 = 305;
    float initRadius21 = 315;
    float initRadius22 = 325;

    // graph position
    float[] a1 = new float[formResolution];
    float[] a2 = new float[formResolution];
    float[] b1 = new float[formResolution];
    float[] b2 = new float[formResolution];
    float[] c1 = new float[formResolution];
    float[] c2 = new float[formResolution];
    float[] d1 = new float[formResolution];
    float[] d2 = new float[formResolution];
    float[] e1 = new float[formResolution];
    float[] e2 = new float[formResolution];
    float[] f1 = new float[formResolution];
    float[] f2 = new float[formResolution];
    float[] g1 = new float[formResolution];
    float[] g2 = new float[formResolution];
    float[] h1 = new float[formResolution];
    float[] h2 = new float[formResolution];
    float[] i1 = new float[formResolution];
    float[] i2 = new float[formResolution];
    float[] j1 = new float[formResolution];
    float[] j2 = new float[formResolution];
    float[] k1 = new float[formResolution];
    float[] k2 = new float[formResolution];
    float[] l1 = new float[formResolution];
    float[] l2 = new float[formResolution];
    float[] m1 = new float[formResolution];
    float[] m2 = new float[formResolution];
    float[] n1 = new float[formResolution];
    float[] n2 = new float[formResolution];
    float[] o1 = new float[formResolution];
    float[] o2 = new float[formResolution];
    float[] p1 = new float[formResolution];
    float[] p2 = new float[formResolution];
    float[] q1 = new float[formResolution];
    float[] q2 = new float[formResolution];
    float[] r1 = new float[formResolution];
    float[] r2 = new float[formResolution];
    float[] s1 = new float[formResolution];
    float[] s2 = new float[formResolution];
    float[] t1 = new float[formResolution];
    float[] t2 = new float[formResolution];
    float[] u1 = new float[formResolution];
    float[] u2 = new float[formResolution];
    float[] v1 = new float[formResolution];
    float[] v2 = new float[formResolution];

    String x[];

    // set color array
    static int[][][] colorList = {
            {{102, 0, 88}, {138, 36, 124}, {174, 72, 160}, {210, 108, 196}, {246, 144, 232}, {255, 180, 255}}
    };

    boolean filled = false;
    // data read counter

    double a = 51.4;

    public String[] getNow() {
        Calendar cal = Calendar.getInstance();

        n[0] = String.format(Locale.KOREA, "%04d", cal.get(Calendar.YEAR));
        n[1] = String.format(Locale.KOREA, "%02d", cal.get(Calendar.MONTH) + 1);
        n[2] = String.format(Locale.KOREA, "%02d", cal.get(Calendar.DAY_OF_MONTH));
        n[3] = String.format(Locale.KOREA, "%02d", cal.get(Calendar.HOUR_OF_DAY));
        n[4] = String.format(Locale.KOREA, "%02d", cal.get(Calendar.MINUTE));

        int count = Integer.parseInt(String.format(Locale.KOREA, "%02d", cal.get(Calendar.SECOND))) - 2;
        n[5] = String.valueOf(count);

        return n;
    }

    public void getGraphData() {

        getNow();

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            double alpha = 0, smr = 0;

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.child(n[0] + "년")
                        .child(n[1] + "월")
                        .child(n[2] + "일")
                        .child(n[3] + "시")
                        .child(n[4] + "분")
                        .child(n[5] + "초").getChildren()) {

                    double r1 = (Math.random() * ((45 - 35) + 1)) + 35;
                    double r2 = (Math.random() * ((40 - 25) + 1)) + 25;
                    double r3 = (Math.random() * ((40 - 25) + 1)) + 25;
                    double r4 = (Math.random() * ((40 - 25) + 1)) + 25;
                    double r5 = (Math.random() * ((40 - 25) + 1)) + 25;
                    double r6 = (Math.random() * ((40 - 20) + 1)) + 20;
                    double r7 = (Math.random() * ((40 - 25) + 1)) + 25;

                    int count = 0;

                    if (state.equals("cn")) {

                        switch (snapshot.getKey()) {
                            case "Alpha":
                                result_alpha.add(String.valueOf(r1));
//                                Log.e("alpha",String.valueOf(r1));
                                count++;
                                break;
                            case "Low Beta":
//                                double a = Double.parseDouble(snapshot.getValue().toString()) - rand8;
                                result_low_beta.add(String.valueOf(r2));
                                count++;
//                                Log.e("low beta",String.valueOf(r2));
                                break;
                            case "High Beta":
//                                double a4 = Double.parseDouble(snapshot.getValue().toString()) - rand6;
                                result_high_beta.add(String.valueOf(r3));
                                count++;
//                                Log.e("high beta",String.valueOf(r3));
                                break;
                            case "Gamma":
//                                double a5 = Double.parseDouble(snapshot.getValue().toString()) + rand7;
                                result_gamma.add(String.valueOf(r4));
                                count++;
//                                Log.e("gamma",String.valueOf(r4));
                                break;
                            case "Theta":
//                                double a1 = Double.parseDouble(snapshot.getValue().toString()) - rand2;
                                result_theta.add(String.valueOf(r5));
                                count++;
//                                Log.e("theta",String.valueOf(r5));
                                break;
                            case "SMR":
                                result_smr.add(String.valueOf(r6));
                                count++;
//                                Log.e("smr",String.valueOf(r6));
                                break;
                            case "Delta":
//                                double a3 = Double.parseDouble(snapshot.getValue().toString()) + rand4;
                                result_delta.add(String.valueOf(r7));
                                count++;
//                                Log.e("delta",String.valueOf(r7));
                                break;
                            case "명상도":
                                if (count == 0){
                                    Log.e("default","");
                                    result_alpha.add(String.valueOf(r1));
                                    result_low_beta.add(String.valueOf(r2));
                                    result_high_beta.add(String.valueOf(r3));
                                    result_gamma.add(String.valueOf(r4));
                                    result_theta.add(String.valueOf(r5));
                                    result_smr.add(String.valueOf(r6));
                                    result_delta.add(String.valueOf(r7));
                                }
                                break;
                            default:
                                break;
                        }
                    } else {
                        switch (snapshot.getKey()) {
                            case "Alpha":
                                double rand10 = (Math.random() * ((7 - 1) + 1)) + 1;
                                double a6 = Double.parseDouble(snapshot.getValue().toString()) + rand10;
                                result_alpha.add(String.valueOf(a6));
                                break;
                            case "Low Beta":
                                double rand1 = (Math.random() * (10 - 1) + 1) + 1;
                                double a2 = Double.parseDouble(snapshot.getValue().toString()) - rand1;
                                result_low_beta.add(String.valueOf(a2));
                                break;
                            case "High Beta":
                                double rand6 = (Math.random() * ((7 - 1) + 1)) + 1;
                                double a4 = Double.parseDouble(snapshot.getValue().toString()) + rand6;
                                result_high_beta.add(String.valueOf(a4));
                                break;
                            case "Gamma":
                                double rand7 = (Math.random() * ((10 - 5) + 1)) + 5;
                                double a5 = Double.parseDouble(snapshot.getValue().toString()) - rand7;
                                result_gamma.add(String.valueOf(a5));
                                break;
                            case "Theta":
                                double rand2 = (Math.random() * ((10 - 5) + 1)) + 5;
                                double a1 = Double.parseDouble(snapshot.getValue().toString()) + rand2;
                                result_theta.add(String.valueOf(a1));
                                break;
                            case "SMR":
                                result_smr.add(snapshot.getValue().toString());
                                break;
                            case "Delta":
                                double rand4 = (Math.random() * ((10 - 5) + 1)) + 5;
                                double a3 = Double.parseDouble(snapshot.getValue().toString()) - rand4;
                                result_delta.add(String.valueOf(a3));
                                break;
                            default:
                                break;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        delay(1100);
    }

    public void setup() {

        centerX = width / 2;
        centerY = height / 2;

        smooth();

        // Get 7 points to draw figure.
        float angle = radians(360 / formResolution);

        // movement shape
        for (int i = 0; i < formResolution; i++) {
            a1[i] = cos(angle * i) * initRadius1;
            a2[i] = sin(angle * i) * initRadius1;
            b1[i] = cos(angle * i) * initRadius2;
            b2[i] = sin(angle * i) * initRadius2;
            c1[i] = cos(angle * i) * initRadius3;
            c2[i] = sin(angle * i) * initRadius3;
            d1[i] = cos(angle * i) * initRadius4;
            d2[i] = sin(angle * i) * initRadius4;
            e1[i] = cos(angle * i) * initRadius5;
            e2[i] = sin(angle * i) * initRadius5;
            f1[i] = cos(angle * i) * initRadius6;
            f2[i] = sin(angle * i) * initRadius6;
            g1[i] = cos(angle * i) * initRadius7;
            g2[i] = sin(angle * i) * initRadius7;
            h1[i] = cos(angle * i) * initRadius8;
            h2[i] = sin(angle * i) * initRadius8;
            i1[i] = cos(angle * i) * initRadius9;
            i2[i] = sin(angle * i) * initRadius9;
            j1[i] = cos(angle * i) * initRadius10;
            j2[i] = sin(angle * i) * initRadius10;
            k1[i] = cos(angle * i) * initRadius11;
            k2[i] = sin(angle * i) * initRadius11;
            l1[i] = cos(angle * i) * initRadius12;
            l2[i] = sin(angle * i) * initRadius12;
            m1[i] = cos(angle * i) * initRadius13;
            m2[i] = sin(angle * i) * initRadius13;
            n1[i] = cos(angle * i) * initRadius14;
            n2[i] = sin(angle * i) * initRadius14;
            o1[i] = cos(angle * i) * initRadius15;
            o2[i] = sin(angle * i) * initRadius15;
            p1[i] = cos(angle * i) * initRadius16;
            p2[i] = sin(angle * i) * initRadius16;
            q1[i] = cos(angle * i) * initRadius17;
            q2[i] = sin(angle * i) * initRadius17;
            r1[i] = cos(angle * i) * initRadius18;
            r2[i] = sin(angle * i) * initRadius18;
            s1[i] = cos(angle * i) * initRadius19;
            s2[i] = sin(angle * i) * initRadius19;
            t1[i] = cos(angle * i) * initRadius20;
            t2[i] = sin(angle * i) * initRadius20;
            u1[i] = cos(angle * i) * initRadius21;
            u2[i] = sin(angle * i) * initRadius21;
            v1[i] = cos(angle * i) * initRadius22;
            v2[i] = sin(angle * i) * initRadius22;
        }
        back();
    }

    //draw
    public void draw() {

        // random setting
        for (int i = 0; i < formResolution; i++) {
            a1[i] += random(-stepSize, stepSize);
            a2[i] += random(-stepSize, stepSize);
            b1[i] += random(-stepSize, stepSize);
            b2[i] += random(-stepSize, stepSize);
            c1[i] += random(-stepSize, stepSize);
            c2[i] += random(-stepSize, stepSize);
            d1[i] += random(-stepSize, stepSize);
            d2[i] += random(-stepSize, stepSize);
            e1[i] += random(-stepSize, stepSize);
            e2[i] += random(-stepSize, stepSize);
            f1[i] += random(-stepSize, stepSize);
            f2[i] += random(-stepSize, stepSize);
            g1[i] += random(-stepSize, stepSize);
            g2[i] += random(-stepSize, stepSize);
            h1[i] += random(-stepSize, stepSize);
            h2[i] += random(-stepSize, stepSize);
            i1[i] += random(-stepSize, stepSize);
            i2[i] += random(-stepSize, stepSize);
            j1[i] += random(-stepSize, stepSize);
            j2[i] += random(-stepSize, stepSize);
            k1[i] += random(-stepSize, stepSize);
            k2[i] += random(-stepSize, stepSize);
            l1[i] += random(-stepSize, stepSize);
            l2[i] += random(-stepSize, stepSize);
            m1[i] += random(-stepSize, stepSize);
            m2[i] += random(-stepSize, stepSize);
            n1[i] += random(-stepSize, stepSize);
            n2[i] += random(-stepSize, stepSize);
            o1[i] += random(-stepSize, stepSize);
            o2[i] += random(-stepSize, stepSize);
            p1[i] += random(-stepSize, stepSize);
            p2[i] += random(-stepSize, stepSize);
            q1[i] += random(-stepSize, stepSize);
            q2[i] += random(-stepSize, stepSize);
            r1[i] += random(-stepSize, stepSize);
            r2[i] += random(-stepSize, stepSize);
            s1[i] += random(-stepSize, stepSize);
            s2[i] += random(-stepSize, stepSize);
            t1[i] += random(-stepSize, stepSize);
            t2[i] += random(-stepSize, stepSize);
            u1[i] += random(-stepSize, stepSize);
            u2[i] += random(-stepSize, stepSize);
            v1[i] += random(-stepSize, stepSize);
            v2[i] += random(-stepSize, stepSize);
        }

        strokeWeight(2);    //line size

        getGraphData();

        if (result_theta.size() != 0) {

            if (c == 6) {
                c = 0;  // reset value of c
                drawShape();
            } else {
                drawShape();
            }

            stroke(93, 93, 93);
            strokeWeight(3);
            beginShape();
            fill(25, 25, 25);
            vertex(centerX, centerY - (centerX / 4));
            vertex(centerX + (centerX - 150) * cos(radians((float) 51.6)), centerY - (centerX - 150) * sin(radians((float) 51.6)) - (centerX / 4));
            endShape();

            delay(100);
        }

        if (count == 10) {
            String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString();
            saveFrame(path + "/test.jpg");
            count = 0;
            number += 1;
            back();
        }
    }

    public void back() {

        // Draw background lines
        background(25, 25, 25);  // background color = black
        stroke(93, 93, 93);  // lines color = gray

        beginShape();

        fill(25, 25, 25);
        vertex(centerX + (centerX - 150), centerY - (centerX / 4));  // 1
        vertex(centerX + (centerX - 150) * cos(radians((float) a * 1)), centerY + (centerX - 150) * sin(radians((float) a * 1)) - (centerX / 4));  // 2
        vertex(centerX + (centerX - 150) * cos(radians((float) a * 2)), centerY + (centerX - 150) * sin(radians((float) a * 2)) - (centerX / 4));  // 3
        vertex(centerX + (centerX - 150) * cos(radians((float) a * 3)), centerY + (centerX - 150) * sin(radians((float) a * 3)) - (centerX / 4));  // 4
        vertex(centerX + (centerX - 150) * cos(radians((float) a * 4)), centerY + (centerX - 150) * sin(radians((float) a * 4)) - (centerX / 4));  // 5
        vertex(centerX + (centerX - 150) * cos(radians((float) a * 5)), centerY + (centerX - 150) * sin(radians((float) a * 5)) - (centerX / 4));  // 6
        vertex(centerX + (centerX - 150) * cos(radians((float) 51.6)), centerY - (centerX - 150) * sin(radians((float) 51.6)) - (centerX / 4));  // 7
        vertex(centerX + (centerX - 150), centerY - (centerX / 4));  // 1
        endShape();

        beginShape();
        fill(25, 25, 25);
        vertex(centerX + (centerX - 250), centerY - (centerX / 4));  // 1
        vertex(centerX + (centerX - 250) * cos(radians((float) a * 1)), centerY + (centerX - 250) * sin(radians((float) a * 1)) - (centerX / 4));  // 2
        vertex(centerX + (centerX - 250) * cos(radians((float) a * 2)), centerY + (centerX - 250) * sin(radians((float) a * 2)) - (centerX / 4));  // 3
        vertex(centerX + (centerX - 250) * cos(radians((float) a * 3)), centerY + (centerX - 250) * sin(radians((float) a * 3)) - (centerX / 4));  // 4
        vertex(centerX + (centerX - 250) * cos(radians((float) a * 4)), centerY + (centerX - 250) * sin(radians((float) a * 4)) - (centerX / 4));  // 5
        vertex(centerX + (centerX - 250) * cos(radians((float) a * 5)), centerY + (centerX - 250) * sin(radians((float) a * 5)) - (centerX / 4));  // 6
        vertex(centerX + (centerX - 250) * cos(radians((float) 51.6)), centerY - (centerX - 250) * sin(radians((float) 51.6)) - (centerX / 4));  // 7
        vertex(centerX + (centerX - 250), centerY - (centerX / 4));  // 1
        endShape();

        beginShape();
        fill(25, 25, 25);
        vertex(centerX + (centerX - 350), centerY - (centerX / 4));  // 1
        vertex(centerX + (centerX - 350) * cos(radians((float) a * 1)), centerY + (centerX - 350) * sin(radians((float) a * 1)) - (centerX / 4));  // 2
        vertex(centerX + (centerX - 350) * cos(radians((float) a * 2)), centerY + (centerX - 350) * sin(radians((float) a * 2)) - (centerX / 4));  // 3
        vertex(centerX + (centerX - 350) * cos(radians((float) a * 3)), centerY + (centerX - 350) * sin(radians((float) a * 3)) - (centerX / 4));  // 4
        vertex(centerX + (centerX - 350) * cos(radians((float) a * 4)), centerY + (centerX - 350) * sin(radians((float) a * 4)) - (centerX / 4));  // 5
        vertex(centerX + (centerX - 350) * cos(radians((float) a * 5)), centerY + (centerX - 350) * sin(radians((float) a * 5)) - (centerX / 4));  // 6
        vertex(centerX + (centerX - 350) * cos(radians((float) 51.6)), centerY - (centerX - 350) * sin(radians((float) 51.6)) - (centerX / 4));  // 7
        vertex(centerX + (centerX - 350), centerY - (centerX / 4));  // 1
        endShape();

        beginShape();
        fill(25, 25, 25);
        vertex(centerX + (centerX - 450), centerY - (centerX / 4));  // 1
        vertex(centerX + (centerX - 450) * cos(radians((float) a * 1)), centerY + (centerX - 450) * sin(radians((float) a * 1)) - (centerX / 4));  // 2
        vertex(centerX + (centerX - 450) * cos(radians((float) a * 2)), centerY + (centerX - 450) * sin(radians((float) a * 2)) - (centerX / 4));  // 3
        vertex(centerX + (centerX - 450) * cos(radians((float) a * 3)), centerY + (centerX - 450) * sin(radians((float) a * 3)) - (centerX / 4));  // 4
        vertex(centerX + (centerX - 450) * cos(radians((float) a * 4)), centerY + (centerX - 450) * sin(radians((float) a * 4)) - (centerX / 4));  // 5
        vertex(centerX + (centerX - 450) * cos(radians((float) a * 5)), centerY + (centerX - 450) * sin(radians((float) a * 5)) - (centerX / 4));  // 6
        vertex(centerX + (centerX - 450) * cos(radians((float) 51.6)), centerY - (centerX - 450) * sin(radians((float) 51.6)) - (centerX / 4));  // 7
        vertex(centerX + (centerX - 450), centerY - (centerX / 4));  // 1
        endShape();

        // inline
        // 1~6
        for (int i = 0; i < 6; i++) {
            beginShape();
            fill(25, 25, 25);
            vertex(centerX, centerY - (centerX / 4));
            vertex(centerX + (centerX - 150) * cos(radians((float) 51.4 * i)), centerY + (centerX - 150) * sin(radians((float) 51.4 * i)) - (centerX / 4));
            endShape();
        }

        // 7
        beginShape();
        fill(25, 25, 25);
        vertex(centerX, centerY - (centerX / 4));
        vertex(centerX + (centerX - 150) * cos(radians((float) 51.6)), centerY - (centerX - 150) * sin(radians((float) 51.6)) - (centerX / 4));
        endShape();

        // text location
        textSize(40);
        fill(255, 255, 255);
        text("Gamma", centerX + (centerX + 100) * cos(radians((float) a * 5)), centerY + (centerX - 110) * sin(radians((float) a * 5)) - (centerX / 4));
        text("High Beta", centerX + (centerX - 150) * cos(radians((float) 51.6)), centerY - (centerX - 100) * sin(radians((float) 51.6)) - (centerX / 4));
        text(" Low\nBeta", centerX + (centerX - 130), centerY - (centerX / 4));
        text("Alpha", centerX + (centerX - 160) * cos(radians((float) a * 1)), centerY + (centerX - 70) * sin(radians((float) a * 1)) - (centerX / 4));
        text("SMR", centerX + (centerX + 50) * cos(radians((float) a * 2)), centerY + (centerX - 80) * sin(radians((float) a * 2)) - (centerX / 4));
        text("Theta", centerX + (centerX - 30) * cos(radians((float) a * 3)), centerY + (centerX - 50) * sin(radians((float) a * 3)) - (centerX / 4));
        text("Delta", centerX + (centerX - 20) * cos(radians((float) a * 4)), centerY + (centerX - 100) * sin(radians((float) a * 4)) - (centerX / 4));
    }

    public void drawShape() {

        // graph inside color
        if (filled)
            fill(random(255));
        else
            noFill();

        stroke(colorList[0][c][0], colorList[0][c][1], colorList[0][c][2]);

        // start
        beginShape();
        Log.e("alpha",String.valueOf(result_alpha.get(j)));
        Log.e("low beta",String.valueOf(result_low_beta.get(j)));
        Log.e("delta",String.valueOf(result_delta.get(j)));
        Log.e("gamma",String.valueOf(result_gamma.get(j)));
        Log.e("high beta",String.valueOf(result_high_beta.get(j)));
        Log.e("smr",String.valueOf(result_smr.get(j)));
        Log.e("theta",String.valueOf(result_theta.get(j)));

        // start controlpoint
        if (num == 1) {
            curveVertex(a1[6] + centerX, a2[6] + centerY - (centerX / 4));
        } else if (num == 2) {
            curveVertex(b1[6] + centerX, b2[6] + centerY - (centerX / 4));
        } else if (num == 3) {
            curveVertex(c1[6] + centerX, c2[6] + centerY - (centerX / 4));
        } else if (num == 4) {
            curveVertex(d1[6] + centerX, d2[6] + centerY - (centerX / 4));
        } else if (num == 5) {
            curveVertex(e1[6] + centerX, e2[6] + centerY - (centerX / 4));
        } else if (num == 6) {
            curveVertex(f1[6] + centerX, f2[6] + centerY - (centerX / 4));
        } else if (num == 7) {
            curveVertex(g1[6] + centerX, g2[6] + centerY - (centerX / 4));
        } else if (num == 8) {
            curveVertex(h1[6] + centerX, h2[6] + centerY - (centerX / 4));
        } else if (num == 9) {
            curveVertex(i1[6] + centerX, i2[6] + centerY - (centerX / 4));
        } else if (num == 10) {
            curveVertex(j1[6] + centerX, j2[6] + centerY - (centerX / 4));
        } else if (num == 11) {
            curveVertex(k1[6] + centerX, k2[6] + centerY - (centerX / 4));
        } else if (num == 12) {
            curveVertex(l1[6] + centerX, l2[6] + centerY - (centerX / 4));
        } else if (num == 13) {
            curveVertex(m1[6] + centerX, m2[6] + centerY - (centerX / 4));
        } else if (num == 14) {
            curveVertex(n1[6] + centerX, n2[6] + centerY - (centerX / 4));
        } else if (num == 15) {
            curveVertex(o1[6] + centerX, o2[6] + centerY - (centerX / 4));
        } else if (num == 16) {
            curveVertex(p1[6] + centerX, p2[6] + centerY - (centerX / 4));
        } else if (num == 17) {
            curveVertex(q1[6] + centerX, q2[6] + centerY - (centerX / 4));
        } else if (num == 18) {
            curveVertex(r1[6] + centerX, r2[6] + centerY - (centerX / 4));
        } else if (num == 19) {
            curveVertex(s1[6] + centerX, s2[6] + centerY - (centerX / 4));
        } else if (num == 20) {
            curveVertex(t1[6] + centerX, t2[6] + centerY - (centerX / 4));
        } else if (num == 21) {
            curveVertex(u1[6] + centerX, u2[6] + centerY - (centerX / 4));
        } else if (num == 22) {
            curveVertex(v1[6] + centerX, v2[6] + centerY - (centerX / 4));
        }

        // low beta
        if (20 <= Float.parseFloat(result_low_beta.get(j)) && Float.parseFloat(result_low_beta.get(j)) < 22.0) {
            curveVertex(a1[0] + centerX, a2[0] + centerY - (centerX / 4));
            num = 1;
        } else if (22.0 <= Float.parseFloat(result_low_beta.get(j)) && Float.parseFloat(result_low_beta.get(j)) < 24.0) {
            curveVertex(b1[0] + centerX, b2[0] + centerY - (centerX / 4));
            num = 2;
        } else if (24.0 <= Float.parseFloat(result_low_beta.get(j)) && Float.parseFloat(result_low_beta.get(j)) < 26.0) {
            curveVertex(c1[0] + centerX, c2[0] + centerY - (centerX / 4));
            num = 3;
        } else if (26.0 <= Float.parseFloat(result_low_beta.get(j)) && Float.parseFloat(result_low_beta.get(j)) < 27.0) {
            curveVertex(d1[0] + centerX, d2[0] + centerY - (centerX / 4));
            num = 4;
        } else if (27.0 <= Float.parseFloat(result_low_beta.get(j)) && Float.parseFloat(result_low_beta.get(j)) < 28.0) {
            curveVertex(e1[0] + centerX, e2[0] + centerY - (centerX / 4));
            num = 5;
        } else if (28.0 <= Float.parseFloat(result_low_beta.get(j)) && Float.parseFloat(result_low_beta.get(j)) < 29.0) {
            curveVertex(f1[0] + centerX, f2[0] + centerY - (centerX / 4));
            num = 6;
        } else if (29.0 <= Float.parseFloat(result_low_beta.get(j)) && Float.parseFloat(result_low_beta.get(j)) < 30.0) {
            curveVertex(g1[0] + centerX, g2[0] + centerY - (centerX / 4));
            num = 7;
        } else if (30.0 <= Float.parseFloat(result_low_beta.get(j)) && Float.parseFloat(result_low_beta.get(j)) < 31.0) {
            curveVertex(h1[0] + centerX, h2[0] + centerY - (centerX / 4));
            num = 8;
        } else if (31.0 <= Float.parseFloat(result_low_beta.get(j)) && Float.parseFloat(result_low_beta.get(j)) < 32.0) {
            curveVertex(i1[0] + centerX, i2[0] + centerY - (centerX / 4));
            num = 9;
        } else if (32.0 <= Float.parseFloat(result_low_beta.get(j)) && Float.parseFloat(result_low_beta.get(j)) < 33.0) {
            curveVertex(j1[0] + centerX, j2[0] + centerY - (centerX / 4));
            num = 10;
        } else if (33.0 <= Float.parseFloat(result_low_beta.get(j)) && Float.parseFloat(result_low_beta.get(j)) < 34.0) {
            curveVertex(k1[0] + centerX, k2[0] + centerY - (centerX / 4));
            num = 11;
        } else if (34.0 <= Float.parseFloat(result_low_beta.get(j)) && Float.parseFloat(result_low_beta.get(j)) < 35.0) {
            curveVertex(l1[0] + centerX, l2[0] + centerY - (centerX / 4));
            num = 12;
        } else if (35.0 <= Float.parseFloat(result_low_beta.get(j)) && Float.parseFloat(result_low_beta.get(j)) < 36.0) {
            curveVertex(m1[0] + centerX, m2[0] + centerY - (centerX / 4));
            num = 13;
        } else if (36.0 <= Float.parseFloat(result_low_beta.get(j)) && Float.parseFloat(result_low_beta.get(j)) < 37.0) {
            curveVertex(n1[0] + centerX, n2[0] + centerY - (centerX / 4));
            num = 14;
        } else if (37.0 <= Float.parseFloat(result_low_beta.get(j)) && Float.parseFloat(result_low_beta.get(j)) < 38.0) {
            curveVertex(o1[0] + centerX, o2[0] + centerY - (centerX / 4));
            num = 15;
        } else if (38.0 <= Float.parseFloat(result_low_beta.get(j)) && Float.parseFloat(result_low_beta.get(j)) < 39.0) {
            curveVertex(p1[0] + centerX, p2[0] + centerY - (centerX / 4));
            num = 16;
        } else if (39.0 <= Float.parseFloat(result_low_beta.get(j)) && Float.parseFloat(result_low_beta.get(j)) < 40.0) {
            curveVertex(q1[0] + centerX, q2[0] + centerY - (centerX / 4));
            num = 17;
        } else if (40.0 <= Float.parseFloat(result_low_beta.get(j)) && Float.parseFloat(result_low_beta.get(j)) < 41.0) {
            curveVertex(r1[0] + centerX, r2[0] + centerY - (centerX / 4));
            num = 18;
        } else if (41.0 <= Float.parseFloat(result_low_beta.get(j)) && Float.parseFloat(result_low_beta.get(j)) < 42.0) {
            curveVertex(s1[0] + centerX, s2[0] + centerY - (centerX / 4));
            num = 19;
        } else if (42.0 <= Float.parseFloat(result_low_beta.get(j)) && Float.parseFloat(result_low_beta.get(j)) < 43.0) {
            curveVertex(t1[0] + centerX, t2[0] + centerY - (centerX / 4));
            num = 20;
        } else if (43.0 <= Float.parseFloat(result_low_beta.get(j)) && Float.parseFloat(result_low_beta.get(j)) < 44.0) {
            curveVertex(u1[0] + centerX, u2[0] + centerY - (centerX / 4));
            num = 21;
        } else if (44.0 <= Float.parseFloat(result_low_beta.get(j)) && Float.parseFloat(result_low_beta.get(j)) <= 45.0) {
            curveVertex(v1[0] + centerX, v2[0] + centerY - (centerX / 4));
            num = 22;
        } else {
            curveVertex(a1[0] + centerX, a2[0] + centerY - (centerX / 4));
            num = 1;
        }

        // smr
        if (20 <= Float.parseFloat(result_smr.get(j)) && Float.parseFloat(result_smr.get(j)) < 22.0) {
            curveVertex(a1[1] + centerX, a2[1] + centerY);
        } else if (22.0 <= Float.parseFloat(result_smr.get(j)) && Float.parseFloat(result_smr.get(j)) < 24.0) {
            curveVertex(b1[1] + centerX, b2[1] + centerY - (centerX / 4));
        } else if (24.0 <= Float.parseFloat(result_smr.get(j)) && Float.parseFloat(result_smr.get(j)) < 26.0) {
            curveVertex(c1[1] + centerX, c2[1] + centerY - (centerX / 4));
        } else if (26.0 <= Float.parseFloat(result_smr.get(j)) && Float.parseFloat(result_smr.get(j)) < 27.0) {
            curveVertex(d1[1] + centerX, d2[1] + centerY - (centerX / 4));
        } else if (27.0 <= Float.parseFloat(result_smr.get(j)) && Float.parseFloat(result_smr.get(j)) < 28.0) {
            curveVertex(e1[1] + centerX, e2[1] + centerY - (centerX / 4));
        } else if (28.0 <= Float.parseFloat(result_smr.get(j)) && Float.parseFloat(result_smr.get(j)) < 29.0) {
            curveVertex(f1[1] + centerX, f2[1] + centerY - (centerX / 4));
        } else if (29.0 <= Float.parseFloat(result_smr.get(j)) && Float.parseFloat(result_smr.get(j)) < 30.0) {
            curveVertex(g1[1] + centerX, g2[1] + centerY - (centerX / 4));
        } else if (30.0 <= Float.parseFloat(result_smr.get(j)) && Float.parseFloat(result_smr.get(j)) < 31.0) {
            curveVertex(h1[1] + centerX, h2[1] + centerY - (centerX / 4));
        } else if (31.0 <= Float.parseFloat(result_smr.get(j)) && Float.parseFloat(result_smr.get(j)) < 32.0) {
            curveVertex(i1[1] + centerX, i2[1] + centerY - (centerX / 4));
        } else if (32.0 <= Float.parseFloat(result_smr.get(j)) && Float.parseFloat(result_smr.get(j)) < 33.0) {
            curveVertex(j1[1] + centerX, j2[1] + centerY - (centerX / 4));
        } else if (33.0 <= Float.parseFloat(result_smr.get(j)) && Float.parseFloat(result_smr.get(j)) < 34.0) {
            curveVertex(k1[1] + centerX, k2[1] + centerY - (centerX / 4));
        } else if (34.0 <= Float.parseFloat(result_smr.get(j)) && Float.parseFloat(result_smr.get(j)) < 35.0) {
            curveVertex(l1[1] + centerX, l2[1] + centerY - (centerX / 4));
        } else if (35.0 <= Float.parseFloat(result_smr.get(j)) && Float.parseFloat(result_smr.get(j)) < 36.0) {
            curveVertex(m1[1] + centerX, m2[1] + centerY - (centerX / 4));
        } else if (36.0 <= Float.parseFloat(result_smr.get(j)) && Float.parseFloat(result_smr.get(j)) < 37.0) {
            curveVertex(n1[1] + centerX, n2[1] + centerY - (centerX / 4));
        } else if (37.0 <= Float.parseFloat(result_smr.get(j)) && Float.parseFloat(result_smr.get(j)) < 38.0) {
            curveVertex(o1[1] + centerX, o2[1] + centerY - (centerX / 4));
        } else if (38.0 <= Float.parseFloat(result_smr.get(j)) && Float.parseFloat(result_smr.get(j)) < 39.0) {
            curveVertex(p1[1] + centerX, p2[1] + centerY - (centerX / 4));
        } else if (39.0 <= Float.parseFloat(result_smr.get(j)) && Float.parseFloat(result_smr.get(j)) < 40.0) {
            curveVertex(q1[1] + centerX, q2[1] + centerY - (centerX / 4));
        } else if (40.0 <= Float.parseFloat(result_smr.get(j)) && Float.parseFloat(result_smr.get(j)) < 41.0) {
            curveVertex(r1[1] + centerX, r2[1] + centerY - (centerX / 4));
        } else if (41.0 <= Float.parseFloat(result_smr.get(j)) && Float.parseFloat(result_smr.get(j)) < 42.0) {
            curveVertex(s1[1] + centerX, s2[1] + centerY - (centerX / 4));
        } else if (42.0 <= Float.parseFloat(result_smr.get(j)) && Float.parseFloat(result_smr.get(j)) < 43.0) {
            curveVertex(t1[1] + centerX, t2[1] + centerY - (centerX / 4));
        } else if (43.0 <= Float.parseFloat(result_smr.get(j)) && Float.parseFloat(result_smr.get(j)) < 44.0) {
            curveVertex(u1[1] + centerX, u2[1] + centerY - (centerX / 4));
        } else if (44.0 <= Float.parseFloat(result_smr.get(j)) && Float.parseFloat(result_smr.get(j)) <= 46.0) {
            curveVertex(v1[1] + centerX, v2[1] + centerY - (centerX / 4));
        } else {
            curveVertex(a1[1] + centerX, a2[1] + centerY - (centerX / 4));
        }

        // alpha
        if (20 <= Float.parseFloat(result_alpha.get(j)) && Float.parseFloat(result_smr.get(j)) < 22.0) {
            curveVertex(a1[2] + centerX, a2[2] + centerY - (centerX / 4));
        } else if (22.0 <= Float.parseFloat(result_alpha.get(j)) && Float.parseFloat(result_alpha.get(j)) < 24.0) {
            curveVertex(b1[2] + centerX, b2[2] + centerY - (centerX / 4));
        } else if (24.0 <= Float.parseFloat(result_alpha.get(j)) && Float.parseFloat(result_alpha.get(j)) < 26.0) {
            curveVertex(c1[2] + centerX, c2[2] + centerY - (centerX / 4));
        } else if (26.0 <= Float.parseFloat(result_alpha.get(j)) && Float.parseFloat(result_alpha.get(j)) < 27.0) {
            curveVertex(d1[2] + centerX, d2[2] + centerY - (centerX / 4));
        } else if (27.0 <= Float.parseFloat(result_alpha.get(j)) && Float.parseFloat(result_alpha.get(j)) < 28.0) {
            curveVertex(e1[2] + centerX, e2[2] + centerY - (centerX / 4));
        } else if (28.0 <= Float.parseFloat(result_alpha.get(j)) && Float.parseFloat(result_alpha.get(j)) < 29.0) {
            curveVertex(f1[2] + centerX, f2[2] + centerY - (centerX / 4));
        } else if (29.0 <= Float.parseFloat(result_alpha.get(j)) && Float.parseFloat(result_alpha.get(j)) < 30.0) {
            curveVertex(g1[2] + centerX, g2[2] + centerY - (centerX / 4));
        } else if (30.0 <= Float.parseFloat(result_alpha.get(j)) && Float.parseFloat(result_alpha.get(j)) < 31.0) {
            curveVertex(h1[2] + centerX, h2[2] + centerY - (centerX / 4));
        } else if (31.0 <= Float.parseFloat(result_alpha.get(j)) && Float.parseFloat(result_alpha.get(j)) < 32.0) {
            curveVertex(i1[2] + centerX, i2[2] + centerY - (centerX / 4));
        } else if (32.0 <= Float.parseFloat(result_alpha.get(j)) && Float.parseFloat(result_alpha.get(j)) < 33.0) {
            curveVertex(j1[2] + centerX, j2[2] + centerY - (centerX / 4));
        } else if (33.0 <= Float.parseFloat(result_alpha.get(j)) && Float.parseFloat(result_alpha.get(j)) < 34.0) {
            curveVertex(k1[2] + centerX, k2[2] + centerY - (centerX / 4));
        } else if (34.0 <= Float.parseFloat(result_alpha.get(j)) && Float.parseFloat(result_alpha.get(j)) < 35.0) {
            curveVertex(l1[2] + centerX, l2[2] + centerY - (centerX / 4));
        } else if (35.0 <= Float.parseFloat(result_alpha.get(j)) && Float.parseFloat(result_alpha.get(j)) < 36.0) {
            curveVertex(m1[2] + centerX, m2[2] + centerY - (centerX / 4));
        } else if (36.0 <= Float.parseFloat(result_alpha.get(j)) && Float.parseFloat(result_alpha.get(j)) < 37.0) {
            curveVertex(n1[2] + centerX, n2[2] + centerY - (centerX / 4));
        } else if (37.0 <= Float.parseFloat(result_alpha.get(j)) && Float.parseFloat(result_alpha.get(j)) < 38.0) {
            curveVertex(o1[2] + centerX, o2[2] + centerY - (centerX / 4));
        } else if (38.0 <= Float.parseFloat(result_alpha.get(j)) && Float.parseFloat(result_alpha.get(j)) < 39.0) {
            curveVertex(p1[2] + centerX, p2[2] + centerY - (centerX / 4));
        } else if (39.0 <= Float.parseFloat(result_alpha.get(j)) && Float.parseFloat(result_alpha.get(j)) < 40.0) {
            curveVertex(q1[2] + centerX, q2[2] + centerY - (centerX / 4));
        } else if (40.0 <= Float.parseFloat(result_alpha.get(j)) && Float.parseFloat(result_alpha.get(j)) < 41.0) {
            curveVertex(r1[2] + centerX, r2[2] + centerY - (centerX / 4));
        } else if (41.0 <= Float.parseFloat(result_alpha.get(j)) && Float.parseFloat(result_alpha.get(j)) < 42.0) {
            curveVertex(s1[2] + centerX, s2[2] + centerY - (centerX / 4));
        } else if (42.0 <= Float.parseFloat(result_alpha.get(j)) && Float.parseFloat(result_alpha.get(j)) < 43.0) {
            curveVertex(t1[2] + centerX, t2[2] + centerY - (centerX / 4));
        } else if (43.0 <= Float.parseFloat(result_alpha.get(j)) && Float.parseFloat(result_alpha.get(j)) < 44.0) {
            curveVertex(u1[2] + centerX, u2[2] + centerY - (centerX / 4));
        } else if (44.0 <= Float.parseFloat(result_alpha.get(j)) && Float.parseFloat(result_alpha.get(j)) <= 45.0) {
            curveVertex(v1[2] + centerX, v2[2] + centerY - (centerX / 4));
        } else {
            curveVertex(a1[2] + centerX, a2[2] + centerY - (centerX / 4));
        }

        // theta
        if (20 <= Float.parseFloat(result_theta.get(j)) && Float.parseFloat(result_theta.get(j)) < 22.0) {
            curveVertex(a1[3] + centerX, a2[3] + centerY - (centerX / 4));
        } else if (22.0 <= Float.parseFloat(result_theta.get(j)) && Float.parseFloat(result_theta.get(j)) < 24.0) {
            curveVertex(b1[3] + centerX, b2[3] + centerY - (centerX / 4));
        } else if (24.0 <= Float.parseFloat(result_theta.get(j)) && Float.parseFloat(result_theta.get(j)) < 26.0) {
            curveVertex(c1[3] + centerX, c2[3] + centerY - (centerX / 4));
        } else if (26.0 <= Float.parseFloat(result_theta.get(j)) && Float.parseFloat(result_theta.get(j)) < 27.0) {
            curveVertex(d1[3] + centerX, d2[3] + centerY - (centerX / 4));
        } else if (27.0 <= Float.parseFloat(result_theta.get(j)) && Float.parseFloat(result_theta.get(j)) < 28.0) {
            curveVertex(e1[3] + centerX, e2[3] + centerY - (centerX / 4));
        } else if (28.0 <= Float.parseFloat(result_theta.get(j)) && Float.parseFloat(result_theta.get(j)) < 29.0) {
            curveVertex(f1[3] + centerX, f2[3] + centerY - (centerX / 4));
        } else if (29.0 <= Float.parseFloat(result_theta.get(j)) && Float.parseFloat(result_theta.get(j)) < 30.0) {
            curveVertex(g1[3] + centerX, g2[3] + centerY - (centerX / 4));
        } else if (30.0 <= Float.parseFloat(result_theta.get(j)) && Float.parseFloat(result_theta.get(j)) < 31.0) {
            curveVertex(h1[3] + centerX, h2[3] + centerY - (centerX / 4));
        } else if (31.0 <= Float.parseFloat(result_theta.get(j)) && Float.parseFloat(result_theta.get(j)) < 32.0) {
            curveVertex(i1[3] + centerX, i2[3] + centerY - (centerX / 4));
        } else if (32.0 <= Float.parseFloat(result_theta.get(j)) && Float.parseFloat(result_theta.get(j)) < 33.0) {
            curveVertex(j1[3] + centerX, j2[3] + centerY - (centerX / 4));
        } else if (33.0 <= Float.parseFloat(result_theta.get(j)) && Float.parseFloat(result_theta.get(j)) < 34.0) {
            curveVertex(k1[3] + centerX, k2[3] + centerY - (centerX / 4));
        } else if (34.0 <= Float.parseFloat(result_theta.get(j)) && Float.parseFloat(result_theta.get(j)) < 35.0) {
            curveVertex(l1[3] + centerX, l2[3] + centerY - (centerX / 4));
        } else if (35.0 <= Float.parseFloat(result_theta.get(j)) && Float.parseFloat(result_theta.get(j)) < 36.0) {
            curveVertex(m1[3] + centerX, m2[3] + centerY - (centerX / 4));
        } else if (36.0 <= Float.parseFloat(result_theta.get(j)) && Float.parseFloat(result_theta.get(j)) < 37.0) {
            curveVertex(n1[3] + centerX, n2[3] + centerY - (centerX / 4));
        } else if (37.0 <= Float.parseFloat(result_theta.get(j)) && Float.parseFloat(result_theta.get(j)) < 38.0) {
            curveVertex(o1[3] + centerX, o2[3] + centerY - (centerX / 4));
        } else if (38.0 <= Float.parseFloat(result_theta.get(j)) && Float.parseFloat(result_theta.get(j)) < 39.0) {
            curveVertex(p1[3] + centerX, p2[3] + centerY - (centerX / 4));
        } else if (39.0 <= Float.parseFloat(result_theta.get(j)) && Float.parseFloat(result_theta.get(j)) < 40.0) {
            curveVertex(q1[3] + centerX, q2[3] + centerY - (centerX / 4));
        } else if (40.0 <= Float.parseFloat(result_theta.get(j)) && Float.parseFloat(result_theta.get(j)) < 41.0) {
            curveVertex(r1[3] + centerX, r2[3] + centerY - (centerX / 4));
        } else if (41.0 <= Float.parseFloat(result_theta.get(j)) && Float.parseFloat(result_theta.get(j)) < 42.0) {
            curveVertex(s1[3] + centerX, s2[3] + centerY - (centerX / 4));
        } else if (42.0 <= Float.parseFloat(result_theta.get(j)) && Float.parseFloat(result_theta.get(j)) < 43.0) {
            curveVertex(t1[3] + centerX, t2[3] + centerY - (centerX / 4));
        } else if (43.0 <= Float.parseFloat(result_theta.get(j)) && Float.parseFloat(result_theta.get(j)) < 44.0) {
            curveVertex(u1[3] + centerX, u2[3] + centerY - (centerX / 4));
        } else if (44.0 <= Float.parseFloat(result_theta.get(j)) && Float.parseFloat(result_theta.get(j)) <= 45.0) {
            curveVertex(v1[3] + centerX, v2[3] + centerY - (centerX / 4));
        } else {
            curveVertex(a1[3] + centerX, a2[3] + centerY - (centerX / 4));
        }


        // delta
        if (20 <= Float.parseFloat(result_delta.get(j)) && Float.parseFloat(result_delta.get(j)) < 22.0) {
            curveVertex(a1[4] + centerX, a2[4] + centerY - (centerX / 4));
        } else if (22.0 <= Float.parseFloat(result_delta.get(j)) && Float.parseFloat(result_delta.get(j)) < 24.0) {
            curveVertex(b1[4] + centerX, b2[4] + centerY - (centerX / 4));
        } else if (24.0 <= Float.parseFloat(result_delta.get(j)) && Float.parseFloat(result_delta.get(j)) < 26.0) {
            curveVertex(c1[4] + centerX, c2[4] + centerY - (centerX / 4));
        } else if (26.0 <= Float.parseFloat(result_delta.get(j)) && Float.parseFloat(result_delta.get(j)) < 27.0) {
            curveVertex(d1[4] + centerX, d2[4] + centerY - (centerX / 4));
        } else if (27.0 <= Float.parseFloat(result_delta.get(j)) && Float.parseFloat(result_delta.get(j)) < 28.0) {
            curveVertex(e1[4] + centerX, e2[4] + centerY - (centerX / 4));
        } else if (28.0 <= Float.parseFloat(result_delta.get(j)) && Float.parseFloat(result_delta.get(j)) < 29.0) {
            curveVertex(f1[4] + centerX, f2[4] + centerY - (centerX / 4));
        } else if (29.0 <= Float.parseFloat(result_delta.get(j)) && Float.parseFloat(result_delta.get(j)) < 30.0) {
            curveVertex(g1[4] + centerX, g2[4] + centerY - (centerX / 4));
        } else if (30.0 <= Float.parseFloat(result_delta.get(j)) && Float.parseFloat(result_delta.get(j)) < 31.0) {
            curveVertex(h1[4] + centerX, h2[4] + centerY - (centerX / 4));
        } else if (31.0 <= Float.parseFloat(result_delta.get(j)) && Float.parseFloat(result_delta.get(j)) < 32.0) {
            curveVertex(i1[4] + centerX, i2[4] + centerY - (centerX / 4));
        } else if (32.0 <= Float.parseFloat(result_delta.get(j)) && Float.parseFloat(result_delta.get(j)) < 33.0) {
            curveVertex(j1[4] + centerX, j2[4] + centerY - (centerX / 4));
        } else if (33.0 <= Float.parseFloat(result_delta.get(j)) && Float.parseFloat(result_delta.get(j)) < 34.0) {
            curveVertex(k1[4] + centerX, k2[4] + centerY - (centerX / 4));
        } else if (34.0 <= Float.parseFloat(result_delta.get(j)) && Float.parseFloat(result_delta.get(j)) < 35.0) {
            curveVertex(l1[4] + centerX, l2[4] + centerY - (centerX / 4));
        } else if (35.0 <= Float.parseFloat(result_delta.get(j)) && Float.parseFloat(result_delta.get(j)) < 36.0) {
            curveVertex(m1[4] + centerX, m2[4] + centerY - (centerX / 4));
        } else if (36.0 <= Float.parseFloat(result_delta.get(j)) && Float.parseFloat(result_delta.get(j)) < 37.0) {
            curveVertex(n1[4] + centerX, n2[4] + centerY - (centerX / 4));
        } else if (37.0 <= Float.parseFloat(result_delta.get(j)) && Float.parseFloat(result_delta.get(j)) < 38.0) {
            curveVertex(o1[4] + centerX, o2[4] + centerY - (centerX / 4));
        } else if (38.0 <= Float.parseFloat(result_delta.get(j)) && Float.parseFloat(result_delta.get(j)) < 39.0) {
            curveVertex(p1[4] + centerX, p2[4] + centerY - (centerX / 4));
        } else if (39.0 <= Float.parseFloat(result_delta.get(j)) && Float.parseFloat(result_delta.get(j)) < 40.0) {
            curveVertex(q1[4] + centerX, q2[4] + centerY - (centerX / 4));
        } else if (40.0 <= Float.parseFloat(result_delta.get(j)) && Float.parseFloat(result_delta.get(j)) < 41.0) {
            curveVertex(r1[4] + centerX, r2[4] + centerY - (centerX / 4));
        } else if (41.0 <= Float.parseFloat(result_delta.get(j)) && Float.parseFloat(result_delta.get(j)) < 42.0) {
            curveVertex(s1[4] + centerX, s2[4] + centerY - (centerX / 4));
        } else if (42.0 <= Float.parseFloat(result_delta.get(j)) && Float.parseFloat(result_delta.get(j)) < 43.0) {
            curveVertex(t1[4] + centerX, t2[4] + centerY - (centerX / 4));
        } else if (43.0 <= Float.parseFloat(result_delta.get(j)) && Float.parseFloat(result_delta.get(j)) < 44.0) {
            curveVertex(u1[4] + centerX, u2[4] + centerY - (centerX / 4));
        } else if (44.0 <= Float.parseFloat(result_delta.get(j)) && Float.parseFloat(result_delta.get(j)) <= 45.0) {
            curveVertex(v1[4] + centerX, v2[4] + centerY - (centerX / 4));
        } else {
            curveVertex(a1[4] + centerX, a2[4] + centerY - (centerX / 4));
        }

        // gamma
        if (20.0 <= Float.parseFloat(result_gamma.get(j)) && Float.parseFloat(result_gamma.get(j)) < 22.0) {
            curveVertex(a1[5] + centerX, a2[5] + centerY - (centerX / 4));
        } else if (22.0 <= Float.parseFloat(result_gamma.get(j)) && Float.parseFloat(result_gamma.get(j)) < 24.0) {
            curveVertex(b1[5] + centerX, b2[5] + centerY - (centerX / 4));
        } else if (24.0 <= Float.parseFloat(result_gamma.get(j)) && Float.parseFloat(result_gamma.get(j)) < 26.0) {
            curveVertex(c1[5] + centerX, c2[5] + centerY - (centerX / 4));
        } else if (26.0 <= Float.parseFloat(result_gamma.get(j)) && Float.parseFloat(result_gamma.get(j)) < 27.0) {
            curveVertex(d1[5] + centerX, d2[5] + centerY - (centerX / 4));
        } else if (27.0 <= Float.parseFloat(result_gamma.get(j)) && Float.parseFloat(result_gamma.get(j)) < 28.0) {
            curveVertex(e1[5] + centerX, e2[5] + centerY - (centerX / 4));
        } else if (28.0 <= Float.parseFloat(result_gamma.get(j)) && Float.parseFloat(result_gamma.get(j)) < 29.0) {
            curveVertex(f1[5] + centerX, f2[5] + centerY - (centerX / 4));
        } else if (29.0 <= Float.parseFloat(result_gamma.get(j)) && Float.parseFloat(result_gamma.get(j)) < 30.0) {
            curveVertex(g1[5] + centerX, g2[5] + centerY - (centerX / 4));
        } else if (30.0 <= Float.parseFloat(result_gamma.get(j)) && Float.parseFloat(result_gamma.get(j)) < 31.0) {
            curveVertex(h1[5] + centerX, h2[5] + centerY - (centerX / 4));
        } else if (31.0 <= Float.parseFloat(result_gamma.get(j)) && Float.parseFloat(result_gamma.get(j)) < 32.0) {
            curveVertex(i1[5] + centerX, i2[5] + centerY - (centerX / 4));
        } else if (32.0 <= Float.parseFloat(result_gamma.get(j)) && Float.parseFloat(result_gamma.get(j)) < 33.0) {
            curveVertex(j1[5] + centerX, j2[5] + centerY - (centerX / 4));
        } else if (33.0 <= Float.parseFloat(result_gamma.get(j)) && Float.parseFloat(result_gamma.get(j)) < 34.0) {
            curveVertex(k1[5] + centerX, k2[5] + centerY - (centerX / 4));
        } else if (34.0 <= Float.parseFloat(result_gamma.get(j)) && Float.parseFloat(result_gamma.get(j)) < 35.0) {
            curveVertex(l1[5] + centerX, l2[5] + centerY - (centerX / 4));
        } else if (35.0 <= Float.parseFloat(result_gamma.get(j)) && Float.parseFloat(result_gamma.get(j)) < 36.0) {
            curveVertex(m1[5] + centerX, m2[5] + centerY - (centerX / 4));
        } else if (36.0 <= Float.parseFloat(result_gamma.get(j)) && Float.parseFloat(result_gamma.get(j)) < 37.0) {
            curveVertex(n1[5] + centerX, n2[5] + centerY - (centerX / 4));
        } else if (37.0 <= Float.parseFloat(result_gamma.get(j)) && Float.parseFloat(result_gamma.get(j)) < 38.0) {
            curveVertex(o1[5] + centerX, o2[5] + centerY - (centerX / 4));
        } else if (38.0 <= Float.parseFloat(result_gamma.get(j)) && Float.parseFloat(result_gamma.get(j)) < 39.0) {
            curveVertex(p1[5] + centerX, p2[5] + centerY - (centerX / 4));
        } else if (39.0 <= Float.parseFloat(result_gamma.get(j)) && Float.parseFloat(result_gamma.get(j)) < 40.0) {
            curveVertex(q1[5] + centerX, q2[5] + centerY - (centerX / 4));
        } else if (40.0 <= Float.parseFloat(result_gamma.get(j)) && Float.parseFloat(result_gamma.get(j)) < 41.0) {
            curveVertex(r1[5] + centerX, r2[5] + centerY - (centerX / 4));
        } else if (41.0 <= Float.parseFloat(result_gamma.get(j)) && Float.parseFloat(result_gamma.get(j)) < 42.0) {
            curveVertex(s1[5] + centerX, s2[5] + centerY - (centerX / 4));
        } else if (42.0 <= Float.parseFloat(result_gamma.get(j)) && Float.parseFloat(result_gamma.get(j)) < 43.0) {
            curveVertex(t1[5] + centerX, t2[5] + centerY - (centerX / 4));
        } else if (43.0 <= Float.parseFloat(result_gamma.get(j)) && Float.parseFloat(result_gamma.get(j)) < 44.0) {
            curveVertex(u1[5] + centerX, u2[5] + centerY - (centerX / 4));
        } else if (44.0 <= Float.parseFloat(result_gamma.get(j)) && Float.parseFloat(result_gamma.get(j)) <= 45.0) {
            curveVertex(v1[5] + centerX, v2[5] + centerY - (centerX / 4));
        } else {
            curveVertex(a1[5] + centerX, a2[5] + centerY - (centerX / 4));
        }


        // high beta
        if (20 <= Float.parseFloat(result_high_beta.get(j)) && Float.parseFloat(result_high_beta.get(j)) < 22.0) {
            curveVertex(a1[6] + centerX, a2[6] + centerY - (centerX / 4));
        } else if (22.0 <= Float.parseFloat(result_high_beta.get(j)) && Float.parseFloat(result_high_beta.get(j)) < 24.0) {
            curveVertex(b1[6] + centerX, b2[6] + centerY - (centerX / 4));
        } else if (24.0 <= Float.parseFloat(result_high_beta.get(j)) && Float.parseFloat(result_high_beta.get(j)) < 26.0) {
            curveVertex(c1[6] + centerX, c2[6] + centerY - (centerX / 4));
        } else if (26.0 <= Float.parseFloat(result_high_beta.get(j)) && Float.parseFloat(result_high_beta.get(j)) < 27.0) {
            curveVertex(d1[6] + centerX, d2[6] + centerY - (centerX / 4));
        } else if (27.0 <= Float.parseFloat(result_high_beta.get(j)) && Float.parseFloat(result_high_beta.get(j)) < 28.0) {
            curveVertex(e1[6] + centerX, e2[6] + centerY - (centerX / 4));
        } else if (28.0 <= Float.parseFloat(result_high_beta.get(j)) && Float.parseFloat(result_high_beta.get(j)) < 29.0) {
            curveVertex(f1[6] + centerX, f2[6] + centerY - (centerX / 4));
        } else if (29.0 <= Float.parseFloat(result_high_beta.get(j)) && Float.parseFloat(result_high_beta.get(j)) < 30.0) {
            curveVertex(g1[6] + centerX, g2[6] + centerY - (centerX / 4));
        } else if (30.0 <= Float.parseFloat(result_high_beta.get(j)) && Float.parseFloat(result_high_beta.get(j)) < 31.0) {
            curveVertex(h1[6] + centerX, h2[6] + centerY - (centerX / 4));
        } else if (31.0 <= Float.parseFloat(result_high_beta.get(j)) && Float.parseFloat(result_high_beta.get(j)) < 32.0) {
            curveVertex(i1[6] + centerX, i2[6] + centerY - (centerX / 4));
        } else if (32.0 <= Float.parseFloat(result_high_beta.get(j)) && Float.parseFloat(result_high_beta.get(j)) < 33.0) {
            curveVertex(j1[6] + centerX, j2[6] + centerY - (centerX / 4));
        } else if (33.0 <= Float.parseFloat(result_high_beta.get(j)) && Float.parseFloat(result_high_beta.get(j)) < 34.0) {
            curveVertex(k1[6] + centerX, k2[6] + centerY - (centerX / 4));
        } else if (34.0 <= Float.parseFloat(result_high_beta.get(j)) && Float.parseFloat(result_high_beta.get(j)) < 35.0) {
            curveVertex(l1[6] + centerX, l2[6] + centerY - (centerX / 4));
        } else if (35.0 <= Float.parseFloat(result_high_beta.get(j)) && Float.parseFloat(result_high_beta.get(j)) < 36.0) {
            curveVertex(m1[6] + centerX, m2[6] + centerY - (centerX / 4));
        } else if (36.0 <= Float.parseFloat(result_high_beta.get(j)) && Float.parseFloat(result_high_beta.get(j)) < 37.0) {
            curveVertex(n1[6] + centerX, n2[6] + centerY - (centerX / 4));
        } else if (37.0 <= Float.parseFloat(result_high_beta.get(j)) && Float.parseFloat(result_high_beta.get(j)) < 38.0) {
            curveVertex(o1[6] + centerX, o2[6] + centerY - (centerX / 4));
        } else if (38.0 <= Float.parseFloat(result_high_beta.get(j)) && Float.parseFloat(result_high_beta.get(j)) < 39.0) {
            curveVertex(p1[6] + centerX, p2[6] + centerY - (centerX / 4));
        } else if (39.0 <= Float.parseFloat(result_high_beta.get(j)) && Float.parseFloat(result_high_beta.get(j)) < 40.0) {
            curveVertex(q1[6] + centerX, q2[6] + centerY - (centerX / 4));
        } else if (40.0 <= Float.parseFloat(result_high_beta.get(j)) && Float.parseFloat(result_high_beta.get(j)) < 41.0) {
            curveVertex(r1[6] + centerX, r2[6] + centerY - (centerX / 4));
        } else if (41.0 <= Float.parseFloat(result_high_beta.get(j)) && Float.parseFloat(result_high_beta.get(j)) < 42.0) {
            curveVertex(s1[6] + centerX, s2[6] + centerY - (centerX / 4));
        } else if (42.0 <= Float.parseFloat(result_high_beta.get(j)) && Float.parseFloat(result_high_beta.get(j)) < 43.0) {
            curveVertex(t1[6] + centerX, t2[6] + centerY - (centerX / 4));
        } else if (43.0 <= Float.parseFloat(result_high_beta.get(j)) && Float.parseFloat(result_high_beta.get(j)) < 44.0) {
            curveVertex(u1[6] + centerX, u2[6] + centerY - (centerX / 4));
        } else if (44.0 <= Float.parseFloat(result_high_beta.get(j)) && Float.parseFloat(result_high_beta.get(j)) <= 45.0) {
            curveVertex(v1[6] + centerX, v2[6] + centerY - (centerX / 4));
        } else {
            curveVertex(a1[6] + centerX, a2[6] + centerY - (centerX / 4));
        }


        // end controlpoint
        if (num == 1) {
            curveVertex(a1[0] + centerX, a2[0] + centerY - (centerX / 4));
            curveVertex(a1[1] + centerX, a2[1] + centerY - (centerX / 4));
        } else if (num == 2) {
            curveVertex(b1[0] + centerX, b2[0] + centerY - (centerX / 4));
            curveVertex(b1[1] + centerX, b2[1] + centerY - (centerX / 4));
        } else if (num == 3) {
            curveVertex(c1[0] + centerX, c2[0] + centerY - (centerX / 4));
            curveVertex(c1[1] + centerX, c2[1] + centerY - (centerX / 4));
        } else if (num == 4) {
            curveVertex(d1[0] + centerX, d2[0] + centerY - (centerX / 4));
            curveVertex(d1[1] + centerX, d2[1] + centerY - (centerX / 4));
        } else if (num == 5) {
            curveVertex(e1[0] + centerX, e2[0] + centerY - (centerX / 4));
            curveVertex(e1[1] + centerX, e2[1] + centerY - (centerX / 4));
        } else if (num == 6) {
            curveVertex(f1[0] + centerX, f2[0] + centerY - (centerX / 4));
            curveVertex(f1[1] + centerX, f2[1] + centerY - (centerX / 4));
        } else if (num == 7) {
            curveVertex(g1[0] + centerX, g2[0] + centerY - (centerX / 4));
            curveVertex(g1[1] + centerX, g2[1] + centerY - (centerX / 4));
        } else if (num == 8) {
            curveVertex(h1[0] + centerX, h2[0] + centerY - (centerX / 4));
            curveVertex(h1[1] + centerX, h2[1] + centerY - (centerX / 4));
        } else if (num == 9) {
            curveVertex(i1[0] + centerX, i2[0] + centerY - (centerX / 4));
            curveVertex(i1[1] + centerX, i2[1] + centerY - (centerX / 4));
        } else if (num == 10) {
            curveVertex(j1[0] + centerX, j2[0] + centerY - (centerX / 4));
            curveVertex(j1[1] + centerX, j2[1] + centerY - (centerX / 4));
        } else if (num == 11) {
            curveVertex(k1[0] + centerX, k2[0] + centerY - (centerX / 4));
            curveVertex(k1[1] + centerX, k2[1] + centerY - (centerX / 4));
        } else if (num == 12) {
            curveVertex(l1[0] + centerX, l2[0] + centerY - (centerX / 4));
            curveVertex(l1[1] + centerX, l2[1] + centerY - (centerX / 4));
        } else if (num == 13) {
            curveVertex(m1[0] + centerX, m2[0] + centerY - (centerX / 4));
            curveVertex(m1[1] + centerX, m2[1] + centerY - (centerX / 4));
        } else if (num == 14) {
            curveVertex(n1[0] + centerX, n2[0] + centerY - (centerX / 4));
            curveVertex(n1[1] + centerX, n2[1] + centerY - (centerX / 4));
        } else if (num == 15) {
            curveVertex(o1[0] + centerX, o2[0] + centerY - (centerX / 4));
            curveVertex(o1[1] + centerX, o2[1] + centerY - (centerX / 4));
        } else if (num == 16) {
            curveVertex(p1[0] + centerX, p2[0] + centerY - (centerX / 4));
            curveVertex(p1[1] + centerX, p2[1] + centerY - (centerX / 4));
        } else if (num == 17) {
            curveVertex(q1[0] + centerX, q2[0] + centerY - (centerX / 4));
            curveVertex(q1[1] + centerX, q2[1] + centerY - (centerX / 4));
        } else if (num == 18) {
            curveVertex(r1[0] + centerX, r2[0] + centerY - (centerX / 4));
            curveVertex(r1[1] + centerX, r2[1] + centerY - (centerX / 4));
        } else if (num == 19) {
            curveVertex(s1[0] + centerX, s2[0] + centerY - (centerX / 4));
            curveVertex(s1[1] + centerX, s2[1] + centerY - (centerX / 4));
        } else if (num == 20) {
            curveVertex(t1[0] + centerX, t2[0] + centerY - (centerX / 4));
            curveVertex(t1[1] + centerX, t2[1] + centerY - (centerX / 4));
        } else if (num == 21) {
            curveVertex(u1[0] + centerX, u2[0] + centerY - (centerX / 4));
            curveVertex(u1[1] + centerX, u2[1] + centerY - (centerX / 4));
        } else if (num == 22) {
            curveVertex(v1[0] + centerX, v2[0] + centerY - (centerX / 4));
            curveVertex(v1[1] + centerX, v2[1] + centerY - (centerX / 4));
        } else {
            curveVertex(a1[0] + centerX, a2[0] + centerY - (centerX / 4));
            curveVertex(a1[1] + centerX, a2[1] + centerY - (centerX / 4));
        }

        if (j == result_alpha.size() - 1) {
            stop();
        }

        //end
        endShape();

        // eeg count ++
        j++;
        // graph count ++
        c++;
        count++;
    }
}