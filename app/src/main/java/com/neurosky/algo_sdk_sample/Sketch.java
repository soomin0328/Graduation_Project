package com.neurosky.algo_sdk_sample;

import android.graphics.Bitmap;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import processing.core.PApplet;

public class Sketch extends PApplet {

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();

    int width = 0, height = 0;

    public Sketch(int w, int h) {
        this.width = w;
        this.height = h;
    }

    boolean recordPDF = false;

    int formResolution = 7;
    int stepSize = 6; // random size

    // graph size
    float initRadius1 = 140;
    float initRadius2 = 170;
    float initRadius3 = 205;
    float initRadius4 = 250;
    float initRadius5 = 280;

    float distortionFactor = 1;

    float centerX, centerY, centerX_2, centerY_2;
    float[] x = new float[formResolution];
    float[] y = new float[formResolution];

    // graper position
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

    // set color array
    static int[][][] colorList = {
            {{102, 0, 88}, {138, 36, 124}, {174, 72, 160}, {210, 108, 196}, {246, 144, 232}, {255, 180, 255}}
    };

    boolean filled = false;
    boolean freeze = false;

    // color counter
    int c = 0;
    // data read counter
    int j = 0;
    int num = 0;

    double a = 51.4;

    // data input (arraylist)
    ArrayList<String> result_alpha = new ArrayList<String>(); // real-time alpha data
    ArrayList<String> result_beta = new ArrayList<String>(); // real-time beta data
    ArrayList<String> result_delta = new ArrayList<String>(); // real-time delta data
    ArrayList<String> result_gamma = new ArrayList<String>(); // real-time gamma data
    ArrayList<String> result_theta = new ArrayList<String>(); // real-time theta data

    String n[] = new String[6];

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

    public void getData() {
        getNow();

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                getNow();

//                for (int s = 0; s < 10; s++) {
//                    for (DataSnapshot snapshot : dataSnapshot.child(n[0]).child(n[1]).child(n[2]).child(n[3]).child(n[4]).child(n[5]).child("Alpha").getChildren()) {
//                        result_alpha.add(snapshot.getValue().toString());
//                    }
//                    for (DataSnapshot snapshot : dataSnapshot.child(n[0]).child(n[1]).child(n[2]).child(n[3]).child(n[4]).child(n[5]).child("Beta").getChildren()) {
//                        result_beta.add(snapshot.getValue().toString());
//                    }
//                    for (DataSnapshot snapshot : dataSnapshot.child(n[0]).child(n[1]).child(n[2]).child(n[3]).child(n[4]).child(n[5]).child("Delta").getChildren()) {
//                        result_delta.add(snapshot.getValue().toString());
//                    }
//                    for (DataSnapshot snapshot : dataSnapshot.child(n[0]).child(n[1]).child(n[2]).child(n[3]).child(n[4]).child(n[5]).child("Gamma").getChildren()) {
//                        result_gamma.add(snapshot.getValue().toString());
//                    }
//                    for (DataSnapshot snapshot : dataSnapshot.child(n[0]).child(n[1]).child(n[2]).child(n[3]).child(n[4]).child(n[5]).child("Theta").getChildren()) {
//                        result_theta.add(snapshot.getValue().toString());
//                    }
//                }

                for (int s = 18; s < 30; s++) {
                    for (DataSnapshot snapshot : dataSnapshot.child("2018년").child("05월").child("13일").child("19시").child("58분").child(s + "초").child("Alpha").getChildren()) {
                        result_alpha.add(snapshot.getValue().toString());
                    }
                    for (DataSnapshot snapshot : dataSnapshot.child("2018년").child("05월").child("13일").child("19시").child("58분").child(s + "초").child("Beta").getChildren()) {
                        result_beta.add(snapshot.getValue().toString());
                    }
                    for (DataSnapshot snapshot : dataSnapshot.child("2018년").child("05월").child("13일").child("19시").child("58분").child(s + "초").child("Delta").getChildren()) {
                        result_delta.add(snapshot.getValue().toString());
                    }
                    for (DataSnapshot snapshot : dataSnapshot.child("2018년").child("05월").child("13일").child("19시").child("58분").child(s + "초").child("Gamma").getChildren()) {
                        result_gamma.add(snapshot.getValue().toString());
                    }
                    for (DataSnapshot snapshot : dataSnapshot.child("2018년").child("05월").child("13일").child("19시").child("58분").child(s + "초").child("Theta").getChildren()) {
                        result_theta.add(snapshot.getValue().toString());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void setup() {

        smooth();

        // init form
        centerX = width / 2;
        centerY = height / 2;
        centerX_2 = width / 2;
        centerY_2 = height / 2 - 135;

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
        }

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
        text("Beta", centerX + (centerX - 150) * cos(radians((float) 51.6)), centerY - (centerX - 100) * sin(radians((float) 51.6)) - (centerX / 4));
        text(" Fest\nAlpha", centerX + (centerX - 130), centerY - (centerX / 4));
        text("Middle\n Alpha", centerX + (centerX - 160) * cos(radians((float) a * 1)), centerY + (centerX - 70) * sin(radians((float) a * 1)) - (centerX / 4));
        text(" Slow\nAlpha", centerX + (centerX + 50) * cos(radians((float) a * 2)), centerY + (centerX - 80) * sin(radians((float) a * 2)) - (centerX / 4));
        text("Theta", centerX + (centerX - 30) * cos(radians((float) a * 3)), centerY + (centerX - 50) * sin(radians((float) a * 3)) - (centerX / 4));
        text("Delta", centerX + (centerX - 20) * cos(radians((float) a * 4)), centerY + (centerX - 100) * sin(radians((float) a * 4)) - (centerX / 4));

//        getData();

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
        }

        strokeWeight(1);    //line size

        if (j < result_alpha.size()) {

            if (result_alpha.size() != 0 && result_beta.size() != 0 && result_delta.size() != 0 && result_gamma.size() != 0 && result_theta.size() != 0) {

                if (c == 6) {
                    c = 0;  // reset value of c
                    drawShape();
                } else {
                    drawShape();
                }

            }

        } else {
            stop();
        }

        stroke(93, 93, 93);
        beginShape();
        fill(25, 25, 25);
        vertex(centerX, centerY - (centerX / 4));
        vertex(centerX + (centerX - 150) * cos(radians((float) 51.6)), centerY - (centerX - 150) * sin(radians((float) 51.6)) - (centerX / 4));
        endShape();

        //  It slows down the time that the next figure is drawn.
        delay(500);
    }

    // draw function
    public void drawShape() {

        // graph inside color
        if (filled)
            fill(random(255));
        else
            noFill();

        stroke(colorList[0][c][0], colorList[0][c][1], colorList[0][c][2]);

        // start
        beginShape();

        // start controlpoint
        if (num == 1) {
            curveVertex(c1[6] + centerX_2, c2[6] + centerY_2);
        } else if (num == 2) {
            curveVertex(d1[6] + centerX_2, d2[6] + centerY_2);
        } else if (num == 3 || num == 0) {
            curveVertex(a1[6] + centerX_2, a2[6] + centerY_2);
        }

        // fast alpha
        if (12.0 <= Float.parseFloat(result_alpha.get(j)) && Float.parseFloat(result_alpha.get(j)) < 12.89999) {
            curveVertex(c1[0] + centerX_2, c2[0] + centerY_2);
            num = 1;
        } else if (12.9 <= Float.parseFloat(result_alpha.get(j)) && Float.parseFloat(result_alpha.get(j)) < 13.99999) {
            curveVertex(d1[0] + centerX_2, d2[0] + centerY_2);
            num = 2;
        } else {
            curveVertex(a1[0] + centerX_2, a2[0] + centerY_2);
            num = 3;
        }

        // middle alpha
        if (10.0 <= Float.parseFloat(result_alpha.get(j)) && Float.parseFloat(result_alpha.get(j)) < 10.89999) {
            curveVertex(c1[1] + centerX_2, c2[1] + centerY_2);
        } else if (10.9 <= Float.parseFloat(result_alpha.get(j)) && Float.parseFloat(result_alpha.get(j)) < 11.99999) {
            curveVertex(d1[1] + centerX_2, d2[1] + centerY_2);
        } else {
            curveVertex(a1[1] + centerX_2, a2[1] + centerY_2);
        }

        // slow alpha
        if (7.0 <= Float.parseFloat(result_alpha.get(j)) && Float.parseFloat(result_alpha.get(j)) < 7.89999) {
            curveVertex(b1[2] + centerX_2, b2[2] + centerY_2);
        } else if (7.9 <= Float.parseFloat(result_alpha.get(j)) && Float.parseFloat(result_alpha.get(j)) < 8.89999) {
            curveVertex(c1[2] + centerX_2, c2[2] + centerY_2);
        } else if (8.9 <= Float.parseFloat(result_alpha.get(j)) && Float.parseFloat(result_alpha.get(j)) < 9.99999) {
            curveVertex(d1[2] + centerX_2, d2[2] + centerY_2);
        } else {
            curveVertex(a1[2] + centerX_2, a2[2] + centerY_2);
        }

        //theta
        if (4.0 <= Float.parseFloat(result_theta.get(j)) && Float.parseFloat(result_theta.get(j)) < 4.89999) {
            curveVertex(b1[3] + centerX_2, b2[3] + centerY_2);
        } else if (4.9 <= Float.parseFloat(result_theta.get(j)) && Float.parseFloat(result_theta.get(j)) < 5.89999) {
            curveVertex(c1[3] + centerX_2, c2[3] + centerY_2);
        } else if (5.9 <= Float.parseFloat(result_theta.get(j)) && Float.parseFloat(result_theta.get(j)) < 6.99999) {
            curveVertex(d1[3] + centerX_2, d2[3] + centerY_2);
        } else {
            curveVertex(a1[3] + centerX_2, a2[3] + centerY_2);
        }

        // delta
        if (0.5 <= Float.parseFloat(result_delta.get(j)) && Float.parseFloat(result_delta.get(j)) < 1.89999) {
            curveVertex(b1[4] + centerX_2, b2[4] + centerY_2);
        } else if (1.9 <= Float.parseFloat(result_delta.get(j)) && Float.parseFloat(result_delta.get(j)) < 2.89999) {
            curveVertex(c1[4] + centerX_2, c2[4] + centerY_2);
        } else if (2.9 <= Float.parseFloat(result_delta.get(j)) && Float.parseFloat(result_delta.get(j)) < 3.99999) {
            curveVertex(d1[4] + centerX_2, d2[4] + centerY_2);
        } else {
            curveVertex(a1[4] + centerX_2, a2[4] + centerY_2);
        }

        // gamma
        if (30.0 <= Float.parseFloat(result_gamma.get(j)) && Float.parseFloat(result_gamma.get(j)) < 34.99999) {
            curveVertex(b1[5] + centerX_2, b2[5] + centerY_2);
        } else if (35.0 <= Float.parseFloat(result_gamma.get(j)) && Float.parseFloat(result_gamma.get(j)) < 39.99999) {
            curveVertex(c1[5] + centerX_2, c2[5] + centerY_2);
        } else if (40.0 <= Float.parseFloat(result_gamma.get(j)) && Float.parseFloat(result_gamma.get(j)) < 44.99999) {
            curveVertex(d1[5] + centerX_2, d2[5] + centerY_2);
        } else if (45.0 <= Float.parseFloat(result_gamma.get(j)) && Float.parseFloat(result_gamma.get(j)) < 49.99999) {
            curveVertex(e1[5] + centerX_2, e2[5] + centerY_2);
        } else {
            curveVertex(a1[5] + centerX_2, a2[5] + centerY_2);
        }

        // beta
        if (14.0 <= Float.parseFloat(result_beta.get(j)) && Float.parseFloat(result_beta.get(j)) < 17.99999) {
            curveVertex(b1[6] + centerX_2, b2[6] + centerY_2);
        } else if (18.0 <= Float.parseFloat(result_beta.get(j)) && Float.parseFloat(result_beta.get(j)) < 21.99999) {
            curveVertex(c1[6] + centerX_2, c2[6] + centerY_2);
        } else if (22.0 <= Float.parseFloat(result_beta.get(j)) && Float.parseFloat(result_beta.get(j)) < 25.99999) {
            curveVertex(d1[6] + centerX_2, d2[6] + centerY_2);
        } else if (26.0 <= Float.parseFloat(result_beta.get(j)) && Float.parseFloat(result_beta.get(j)) < 29.99999) {
            curveVertex(e1[6] + centerX_2, e2[6] + centerY_2);
        } else {
            curveVertex(a1[6] + centerX_2, a2[6] + centerY_2);
        }


        // end controlpoint
        if (num == 1) {
            curveVertex(c1[0] + centerX_2, c2[0] + centerY_2);
            curveVertex(c1[1] + centerX_2, c2[1] + centerY_2);
        } else if (num == 2) {
            curveVertex(d1[0] + centerX_2, d2[0] + centerY_2);
            curveVertex(d1[1] + centerX_2, d2[1] + centerY_2);
        } else if (num == 3) {
            curveVertex(a1[0] + centerX_2, a2[0] + centerY_2);
            curveVertex(a1[1] + centerX_2, a2[1] + centerY_2);
        }

        //end
        endShape();

        // eeg count ++
        j++;

        // graph count ++
        c++;
    }
}
