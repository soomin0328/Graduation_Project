package com.neurosky.algo_sdk_sample;

import android.os.Environment;
import java.util.ArrayList;
import java.util.Random;

import processing.core.PApplet;

public class Sketch extends PApplet {

    // data input (arraylist)
    ArrayList<String> result_alpha = new ArrayList<>(); // real-time alpha data
    ArrayList<String> result_low_beta = new ArrayList<>(); // real-time beta data
    ArrayList<String> result_delta = new ArrayList<>(); // real-time delta data
    ArrayList<String> result_gamma = new ArrayList<>(); // real-time gamma data
    ArrayList<String> result_theta = new ArrayList<>(); // real-time theta data
    ArrayList<String> result_smr = new ArrayList<>();
    ArrayList<String> result_high_beta = new ArrayList<>();

    int width = 0, height = 0, count = 0, number = 1, j = 0, num = 0, c = 0;
    int formResolution = 7;
    int stepSize = 6; // random size
    float centerX, centerY;

    public Sketch(int a, int b){
        this.width = a;
        this.height = b;
    }

    String n[] = new String[6];
    Random rand = new Random();

    // graph size
    float initRadius1 = 140;
    float initRadius2 = 170;
    float initRadius3 = 205;
    float initRadius4 = 250;
    float initRadius5 = 280;
    float initRadius6 = 300;
    float initRadius7 = 320;
    float initRadius8 = 340;
    float initRadius9 = 360;
    float initRadius10 = 380;
    float initRadius11 = 400;
    float initRadius12 = 420;
    float initRadius13 = 440;
    float initRadius14 = 460;
    float initRadius15 = 480;
    float initRadius16 = 500;
    float initRadius17 = 520;
    float initRadius18 = 540;
    float initRadius19 = 560;
    float initRadius20 = 580;
    float initRadius21 = 600;
    float initRadius22 = 620;

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

    // set color array
    static int[][][] colorList = {
            {{102, 0, 88}, {138, 36, 124}, {174, 72, 160}, {210, 108, 196}, {246, 144, 232}, {255, 180, 255}}
    };

    boolean filled = false;
    // data read counter

    double a = 51.4;

    public void getGraphData() {

        Nomalization nomalization = new Nomalization();

        String x[] = nomalization.getData();

        result_alpha.add(x[0]);
        result_low_beta.add(x[1]);
        result_delta.add(x[2]);
        result_gamma.add(x[3]);
        result_theta.add(x[4]);

        double hb1 = rand.nextInt(40 - 25 + 1) + 25;
        double smr1 = rand.nextInt(40 - 25 + 1) + 25;
        result_high_beta.add(String.valueOf(hb1));
        result_smr.add(String.valueOf(smr1));
        hb1 = 0;
        smr1 = 0;

    }

    public void setup() {

        centerX = width/2;
        centerY = height/2;

        smooth();

        // Get 7 points to draw figure.
        float angle = radians(360 / formResolution);

        // movement shape
        for (int i = 0; i < formResolution; i++) {
            a1[i]=cos(angle*i) * initRadius1;
            a2[i]=sin(angle*i) * initRadius1;
            b1[i]=cos(angle*i) * initRadius2;
            b2[i]=sin(angle*i) * initRadius2;
            c1[i]=cos(angle*i) * initRadius3;
            c2[i]=sin(angle*i) * initRadius3;
            d1[i]=cos(angle*i) * initRadius4;
            d2[i]=sin(angle*i) * initRadius4;
            e1[i]=cos(angle*i) * initRadius5;
            e2[i]=sin(angle*i) * initRadius5;
            f1[i]=cos(angle*i) * initRadius6;
            f2[i]=sin(angle*i) * initRadius6;
            g1[i]=cos(angle*i) * initRadius7;
            g2[i]=sin(angle*i) * initRadius7;
            h1[i]=cos(angle*i) * initRadius8;
            h2[i]=sin(angle*i) * initRadius8;
            i1[i]=cos(angle*i) * initRadius9;
            i2[i]=sin(angle*i) * initRadius9;
            j1[i]=cos(angle*i) * initRadius10;
            j2[i]=sin(angle*i) * initRadius10;
            k1[i]=cos(angle*i) * initRadius11;
            k2[i]=sin(angle*i) * initRadius11;
            l1[i]=cos(angle*i) * initRadius12;
            l2[i]=sin(angle*i) * initRadius12;
            m1[i]=cos(angle*i) * initRadius13;
            m2[i]=sin(angle*i) * initRadius13;
            n1[i]=cos(angle*i) * initRadius14;
            n2[i]=sin(angle*i) * initRadius14;
            o1[i]=cos(angle*i) * initRadius15;
            o2[i]=sin(angle*i) * initRadius15;
            p1[i]=cos(angle*i) * initRadius16;
            p2[i]=sin(angle*i) * initRadius16;
            q1[i]=cos(angle*i) * initRadius17;
            q2[i]=sin(angle*i) * initRadius17;
            r1[i]=cos(angle*i) * initRadius18;
            r2[i]=sin(angle*i) * initRadius18;
            s1[i]=cos(angle*i) * initRadius19;
            s2[i]=sin(angle*i) * initRadius19;
            t1[i]=cos(angle*i) * initRadius20;
            t2[i]=sin(angle*i) * initRadius20;
            u1[i]=cos(angle*i) * initRadius21;
            u2[i]=sin(angle*i) * initRadius21;
            v1[i]=cos(angle*i) * initRadius22;
            v2[i]=sin(angle*i) * initRadius22;
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

        strokeWeight(1);    //line size
        getGraphData();

        if (c == 6) {
            c = 0;  // reset value of c
            drawShape();
        } else {
            drawShape();
        }

        delay(100);

        if (count == 10) {
            String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString();
            saveFrame(path+"/test.jpg");
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
        text("Beta", centerX + (centerX - 150) * cos(radians((float) 51.6)), centerY - (centerX - 100) * sin(radians((float) 51.6)) - (centerX / 4));
        text(" Fest\nAlpha", centerX + (centerX - 130), centerY - (centerX / 4));
        text("Middle\n Alpha", centerX + (centerX - 160) * cos(radians((float) a * 1)), centerY + (centerX - 70) * sin(radians((float) a * 1)) - (centerX / 4));
        text(" Slow\nAlpha", centerX + (centerX + 50) * cos(radians((float) a * 2)), centerY + (centerX - 80) * sin(radians((float) a * 2)) - (centerX / 4));
        text("Theta", centerX + (centerX - 30) * cos(radians((float) a * 3)), centerY + (centerX - 50) * sin(radians((float) a * 3)) - (centerX / 4));
        text("Delta", centerX + (centerX - 20) * cos(radians((float) a * 4)), centerY + (centerX - 100) * sin(radians((float) a * 4)) - (centerX / 4));
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
            curveVertex(a1[6]+centerX, a2[6]+centerY);
        } else if (num == 2) {
            curveVertex(b1[6]+centerX, b2[6]+centerY);
        } else if (num == 3) {
            curveVertex(c1[6]+centerX, c2[6]+centerY);
        } else if (num == 4) {
            curveVertex(d1[6]+centerX, d2[6]+centerY);
        } else if (num == 5) {
            curveVertex(e1[6]+centerX, e2[6]+centerY);
        } else if (num == 6) {
            curveVertex(f1[6]+centerX, f2[6]+centerY);
        } else if (num == 7) {
            curveVertex(g1[6]+centerX, g2[6]+centerY);
        } else if (num == 8) {
            curveVertex(h1[6]+centerX, h2[6]+centerY);
        } else if (num == 9) {
            curveVertex(i1[6]+centerX, i2[6]+centerY);
        } else if (num == 10) {
            curveVertex(j1[6]+centerX, j2[6]+centerY);
        } else if (num == 11) {
            curveVertex(k1[6]+centerX, k2[6]+centerY);
        } else if (num == 12) {
            curveVertex(l1[6]+centerX, l2[6]+centerY);
        } else if (num == 13) {
            curveVertex(m1[6]+centerX, m2[6]+centerY);
        } else if (num == 14) {
            curveVertex(n1[6]+centerX, n2[6]+centerY);
        } else if (num == 15) {
            curveVertex(o1[6]+centerX, o2[6]+centerY);
        } else if (num == 16) {
            curveVertex(p1[6]+centerX, p2[6]+centerY);
        } else if (num == 17) {
            curveVertex(q1[6]+centerX, q2[6]+centerY);
        } else if (num == 18) {
            curveVertex(r1[6]+centerX, r2[6]+centerY);
        } else if (num == 19) {
            curveVertex(s1[6]+centerX, s2[6]+centerY);
        } else if (num == 20) {
            curveVertex(t1[6]+centerX, t2[6]+centerY);
        } else if (num == 21) {
            curveVertex(u1[6]+centerX, u2[6]+centerY);
        } else if (num == 22) {
            curveVertex(v1[6]+centerX, v2[6]+centerY);
        }

        // low beta
        if (0<=Float.parseFloat(result_low_beta.get(j)) && Float.parseFloat(result_low_beta.get(j))<3.0) {
            curveVertex(a1[0]+centerX, a2[0]+centerY);
            num = 1;
        } else if (3.0<=Float.parseFloat(result_low_beta.get(j)) && Float.parseFloat(result_low_beta.get(j))<6.0) {
            curveVertex(b1[0]+centerX, b2[0]+centerY);
            num = 2;
        } else if (6.0<=Float.parseFloat(result_low_beta.get(j)) && Float.parseFloat(result_low_beta.get(j))<9.0) {
            curveVertex(c1[0]+centerX, c2[0]+centerY);
            num = 3;
        } else if (9.0<=Float.parseFloat(result_low_beta.get(j)) && Float.parseFloat(result_low_beta.get(j))<12.0) {
            curveVertex(d1[0]+centerX, d2[0]+centerY);
            num = 4;
        } else if (12.0<=Float.parseFloat(result_low_beta.get(j)) && Float.parseFloat(result_low_beta.get(j))<15.0) {
            curveVertex(e1[0]+centerX, e2[0]+centerY);
            num = 5;
        } else if (15.0<=Float.parseFloat(result_low_beta.get(j)) && Float.parseFloat(result_low_beta.get(j))<18.0) {
            curveVertex(f1[0]+centerX, f2[0]+centerY);
            num = 6;
        } else if (18.0<=Float.parseFloat(result_low_beta.get(j)) && Float.parseFloat(result_low_beta.get(j))<21.0) {
            curveVertex(g1[0]+centerX, g2[0]+centerY);
            num = 7;
        } else if (21.0<=Float.parseFloat(result_low_beta.get(j)) && Float.parseFloat(result_low_beta.get(j))<24.0) {
            curveVertex(h1[0]+centerX, h2[0]+centerY);
            num = 8;
        } else if (24.0<=Float.parseFloat(result_low_beta.get(j)) && Float.parseFloat(result_low_beta.get(j))<27.0) {
            curveVertex(i1[0]+centerX, i2[0]+centerY);
            num = 9;
        } else if (27.0<=Float.parseFloat(result_low_beta.get(j)) && Float.parseFloat(result_low_beta.get(j))<30.0) {
            curveVertex(j1[0]+centerX, j2[0]+centerY);
            num = 10;
        } else if (30.0<=Float.parseFloat(result_low_beta.get(j)) && Float.parseFloat(result_low_beta.get(j))<33.0) {
            curveVertex(k1[0]+centerX, k2[0]+centerY);
            num = 11;
        } else if (33.0<=Float.parseFloat(result_low_beta.get(j)) && Float.parseFloat(result_low_beta.get(j))<36.0) {
            curveVertex(l1[0]+centerX, l2[0]+centerY);
            num = 12;
        } else if (36.0<=Float.parseFloat(result_low_beta.get(j)) && Float.parseFloat(result_low_beta.get(j))<39.0) {
            curveVertex(m1[0]+centerX, m2[0]+centerY);
            num = 13;
        } else if (39.0<=Float.parseFloat(result_low_beta.get(j)) && Float.parseFloat(result_low_beta.get(j))<42.0) {
            curveVertex(n1[0]+centerX, n2[0]+centerY);
            num = 14;
        } else if (42.0<=Float.parseFloat(result_low_beta.get(j)) && Float.parseFloat(result_low_beta.get(j))<45.0) {
            curveVertex(o1[0]+centerX, o2[0]+centerY);
            num = 15;
        } else if (45.0<=Float.parseFloat(result_low_beta.get(j)) && Float.parseFloat(result_low_beta.get(j))<48.0) {
            curveVertex(p1[0]+centerX, p2[0]+centerY);
            num = 16;
        } else if (48.0<=Float.parseFloat(result_low_beta.get(j)) && Float.parseFloat(result_low_beta.get(j))<51.0) {
            curveVertex(q1[0]+centerX, q2[0]+centerY);
            num = 17;
        } else if (51.0<=Float.parseFloat(result_low_beta.get(j)) && Float.parseFloat(result_low_beta.get(j))<54.0) {
            curveVertex(r1[0]+centerX, r2[0]+centerY);
            num = 18;
        } else if (54.0<=Float.parseFloat(result_low_beta.get(j)) && Float.parseFloat(result_low_beta.get(j))<57.0) {
            curveVertex(s1[0]+centerX, s2[0]+centerY);
            num = 19;
        } else if (57.0<=Float.parseFloat(result_low_beta.get(j)) && Float.parseFloat(result_low_beta.get(j))<60.0) {
            curveVertex(t1[0]+centerX, t2[0]+centerY);
            num = 20;
        } else if (60.0<=Float.parseFloat(result_low_beta.get(j)) && Float.parseFloat(result_low_beta.get(j))<75.0) {
            curveVertex(u1[0]+centerX, u2[0]+centerY);
            num = 21;
        } else if (75.0<=Float.parseFloat(result_low_beta.get(j)) && Float.parseFloat(result_low_beta.get(j))<=100.0) {
            curveVertex(v1[0]+centerX, v2[0]+centerY);
            num = 22;
        }

        // smr
        if (0<=Float.parseFloat(result_smr.get(j)) && Float.parseFloat(result_smr.get(j))<3.0) {
            curveVertex(a1[1]+centerX, a2[1]+centerY);
        } else if (3.0<=Float.parseFloat(result_smr.get(j)) && Float.parseFloat(result_smr.get(j))<6.0) {
            curveVertex(b1[1]+centerX, b2[1]+centerY);
        } else if (6.0<=Float.parseFloat(result_smr.get(j)) && Float.parseFloat(result_smr.get(j))<9.0) {
            curveVertex(c1[1]+centerX, c2[1]+centerY);
        } else if (9.0<=Float.parseFloat(result_smr.get(j)) && Float.parseFloat(result_smr.get(j))<12.0) {
            curveVertex(d1[1]+centerX, d2[1]+centerY);
        } else if (12.0<=Float.parseFloat(result_smr.get(j)) && Float.parseFloat(result_smr.get(j))<15.0) {
            curveVertex(e1[1]+centerX, e2[1]+centerY);
        } else if (15.0<=Float.parseFloat(result_smr.get(j)) && Float.parseFloat(result_smr.get(j))<18.0) {
            curveVertex(f1[1]+centerX, f2[1]+centerY);
        } else if (18.0<=Float.parseFloat(result_smr.get(j)) && Float.parseFloat(result_smr.get(j))<21.0) {
            curveVertex(g1[1]+centerX, g2[1]+centerY);
        } else if (21.0<=Float.parseFloat(result_smr.get(j)) && Float.parseFloat(result_smr.get(j))<24.0) {
            curveVertex(h1[1]+centerX, h2[1]+centerY);
        } else if (24.0<=Float.parseFloat(result_smr.get(j)) && Float.parseFloat(result_smr.get(j))<27.0) {
            curveVertex(i1[1]+centerX, i2[1]+centerY);
        } else if (27.0<=Float.parseFloat(result_smr.get(j)) && Float.parseFloat(result_smr.get(j))<30.0) {
            curveVertex(j1[1]+centerX, j2[1]+centerY);
        } else if (30.0<=Float.parseFloat(result_smr.get(j)) && Float.parseFloat(result_smr.get(j))<33.0) {
            curveVertex(k1[1]+centerX, k2[1]+centerY);
        } else if (33.0<=Float.parseFloat(result_smr.get(j)) && Float.parseFloat(result_smr.get(j))<36.0) {
            curveVertex(l1[1]+centerX, l2[1]+centerY);
        } else if (36.0<=Float.parseFloat(result_smr.get(j)) && Float.parseFloat(result_smr.get(j))<39.0) {
            curveVertex(m1[1]+centerX, m2[1]+centerY);
        } else if (39.0<=Float.parseFloat(result_smr.get(j)) && Float.parseFloat(result_smr.get(j))<42.0) {
            curveVertex(n1[1]+centerX, n2[1]+centerY);
        } else if (42.0<=Float.parseFloat(result_smr.get(j)) && Float.parseFloat(result_smr.get(j))<45.0) {
            curveVertex(o1[1]+centerX, o2[1]+centerY);
        } else if (45.0<=Float.parseFloat(result_smr.get(j)) && Float.parseFloat(result_smr.get(j))<48.0) {
            curveVertex(p1[1]+centerX, p2[1]+centerY);
        } else if (48.0<=Float.parseFloat(result_smr.get(j)) && Float.parseFloat(result_smr.get(j))<51.0) {
            curveVertex(q1[1]+centerX, q2[1]+centerY);
        } else if (51.0<=Float.parseFloat(result_smr.get(j)) && Float.parseFloat(result_smr.get(j))<54.0) {
            curveVertex(r1[1]+centerX, r2[1]+centerY);
        } else if (54.0<=Float.parseFloat(result_smr.get(j)) && Float.parseFloat(result_smr.get(j))<57.0) {
            curveVertex(s1[1]+centerX, s2[1]+centerY);
        } else if (57.0<=Float.parseFloat(result_smr.get(j)) && Float.parseFloat(result_smr.get(j))<60.0) {
            curveVertex(t1[1]+centerX, t2[1]+centerY);
        } else if (60.0<=Float.parseFloat(result_smr.get(j)) && Float.parseFloat(result_smr.get(j))<75.0) {
            curveVertex(u1[1]+centerX, u2[1]+centerY);
        } else if (75.0<=Float.parseFloat(result_smr.get(j)) && Float.parseFloat(result_smr.get(j))<=100.0) {
            curveVertex(v1[1]+centerX, v2[1]+centerY);
        }

        // alpha
        if (0<=Float.parseFloat(result_alpha.get(j)) && Float.parseFloat(result_smr.get(j))<3.0) {
            curveVertex(a1[2]+centerX, a2[2]+centerY);
        } else if (3.0<=Float.parseFloat(result_alpha.get(j)) && Float.parseFloat(result_alpha.get(j))<6.0) {
            curveVertex(b1[2]+centerX, b2[2]+centerY);
        } else if (6.0<=Float.parseFloat(result_alpha.get(j)) && Float.parseFloat(result_alpha.get(j))<9.0) {
            curveVertex(c1[2]+centerX, c2[2]+centerY);
        } else if (9.0<=Float.parseFloat(result_alpha.get(j)) && Float.parseFloat(result_alpha.get(j))<12.0) {
            curveVertex(d1[2]+centerX, d2[2]+centerY);
        } else if (12.0<=Float.parseFloat(result_alpha.get(j)) && Float.parseFloat(result_alpha.get(j))<15.0) {
            curveVertex(e1[2]+centerX, e2[2]+centerY);
        } else if (15.0<=Float.parseFloat(result_alpha.get(j)) && Float.parseFloat(result_alpha.get(j))<18.0) {
            curveVertex(f1[2]+centerX, f2[2]+centerY);
        } else if (18.0<=Float.parseFloat(result_alpha.get(j)) && Float.parseFloat(result_alpha.get(j))<21.0) {
            curveVertex(g1[2]+centerX, g2[2]+centerY);
        } else if (21.0<=Float.parseFloat(result_alpha.get(j)) && Float.parseFloat(result_alpha.get(j))<24.0) {
            curveVertex(h1[2]+centerX, h2[2]+centerY);
        } else if (24.0<=Float.parseFloat(result_alpha.get(j)) && Float.parseFloat(result_alpha.get(j))<27.0) {
            curveVertex(i1[2]+centerX, i2[2]+centerY);
        } else if (27.0<=Float.parseFloat(result_alpha.get(j)) && Float.parseFloat(result_alpha.get(j))<30.0) {
            curveVertex(j1[2]+centerX, j2[2]+centerY);
        } else if (30.0<=Float.parseFloat(result_alpha.get(j)) && Float.parseFloat(result_alpha.get(j))<33.0) {
            curveVertex(k1[2]+centerX, k2[2]+centerY);
        } else if (33.0<=Float.parseFloat(result_alpha.get(j)) && Float.parseFloat(result_alpha.get(j))<36.0) {
            curveVertex(l1[2]+centerX, l2[2]+centerY);
        } else if (36.0<=Float.parseFloat(result_alpha.get(j)) && Float.parseFloat(result_alpha.get(j))<39.0) {
            curveVertex(m1[2]+centerX, m2[2]+centerY);
        } else if (39.0<=Float.parseFloat(result_alpha.get(j)) && Float.parseFloat(result_alpha.get(j))<42.0) {
            curveVertex(n1[2]+centerX, n2[2]+centerY);
        } else if (42.0<=Float.parseFloat(result_alpha.get(j)) && Float.parseFloat(result_alpha.get(j))<45.0) {
            curveVertex(o1[2]+centerX, o2[2]+centerY);
        } else if (45.0<=Float.parseFloat(result_alpha.get(j)) && Float.parseFloat(result_alpha.get(j))<48.0) {
            curveVertex(p1[2]+centerX, p2[2]+centerY);
        } else if (48.0<=Float.parseFloat(result_alpha.get(j)) && Float.parseFloat(result_alpha.get(j))<51.0) {
            curveVertex(q1[2]+centerX, q2[2]+centerY);
        } else if (51.0<=Float.parseFloat(result_alpha.get(j)) && Float.parseFloat(result_alpha.get(j))<54.0) {
            curveVertex(r1[2]+centerX, r2[2]+centerY);
        } else if (54.0<=Float.parseFloat(result_alpha.get(j)) && Float.parseFloat(result_alpha.get(j))<57.0) {
            curveVertex(s1[2]+centerX, s2[2]+centerY);
        } else if (57.0<=Float.parseFloat(result_alpha.get(j)) && Float.parseFloat(result_alpha.get(j))<60.0) {
            curveVertex(t1[2]+centerX, t2[2]+centerY);
        } else if (60.0<=Float.parseFloat(result_alpha.get(j)) && Float.parseFloat(result_alpha.get(j))<75.0) {
            curveVertex(u1[2]+centerX, u2[2]+centerY);
        } else if (75.0<=Float.parseFloat(result_alpha.get(j)) && Float.parseFloat(result_alpha.get(j))<=100.0) {
            curveVertex(v1[2]+centerX, v2[2]+centerY);
        }

        // theta
        if (0<=Float.parseFloat(result_theta.get(j)) && Float.parseFloat(result_theta.get(j))<3.0) {
            curveVertex(a1[3]+centerX, a2[3]+centerY);
        } else if (3.0<=Float.parseFloat(result_theta.get(j)) && Float.parseFloat(result_theta.get(j))<6.0) {
            curveVertex(b1[3]+centerX, b2[3]+centerY);
        } else if (6.0<=Float.parseFloat(result_theta.get(j)) && Float.parseFloat(result_theta.get(j))<9.0) {
            curveVertex(c1[3]+centerX, c2[3]+centerY);
        } else if (9.0<=Float.parseFloat(result_theta.get(j)) && Float.parseFloat(result_theta.get(j))<12.0) {
            curveVertex(d1[3]+centerX, d2[3]+centerY);
        } else if (12.0<=Float.parseFloat(result_theta.get(j)) && Float.parseFloat(result_theta.get(j))<15.0) {
            curveVertex(e1[3]+centerX, e2[3]+centerY);
        } else if (15.0<=Float.parseFloat(result_theta.get(j)) && Float.parseFloat(result_theta.get(j))<18.0) {
            curveVertex(f1[3]+centerX, f2[3]+centerY);
        } else if (18.0<=Float.parseFloat(result_theta.get(j)) && Float.parseFloat(result_theta.get(j))<21.0) {
            curveVertex(g1[3]+centerX, g2[3]+centerY);
        } else if (21.0<=Float.parseFloat(result_theta.get(j)) && Float.parseFloat(result_theta.get(j))<24.0) {
            curveVertex(h1[3]+centerX, h2[3]+centerY);
        } else if (24.0<=Float.parseFloat(result_theta.get(j)) && Float.parseFloat(result_theta.get(j))<27.0) {
            curveVertex(i1[3]+centerX, i2[3]+centerY);
        } else if (27.0<=Float.parseFloat(result_theta.get(j)) && Float.parseFloat(result_theta.get(j))<30.0) {
            curveVertex(j1[3]+centerX, j2[3]+centerY);
        } else if (30.0<=Float.parseFloat(result_theta.get(j)) && Float.parseFloat(result_theta.get(j))<33.0) {
            curveVertex(k1[3]+centerX, k2[3]+centerY);
        } else if (33.0<=Float.parseFloat(result_theta.get(j)) && Float.parseFloat(result_theta.get(j))<36.0) {
            curveVertex(l1[3]+centerX, l2[3]+centerY);
        } else if (36.0<=Float.parseFloat(result_theta.get(j)) && Float.parseFloat(result_theta.get(j))<39.0) {
            curveVertex(m1[3]+centerX, m2[3]+centerY);
        } else if (39.0<=Float.parseFloat(result_theta.get(j)) && Float.parseFloat(result_theta.get(j))<42.0) {
            curveVertex(n1[3]+centerX, n2[3]+centerY);
        } else if (42.0<=Float.parseFloat(result_theta.get(j)) && Float.parseFloat(result_theta.get(j))<45.0) {
            curveVertex(o1[3]+centerX, o2[3]+centerY);
        } else if (45.0<=Float.parseFloat(result_theta.get(j)) && Float.parseFloat(result_theta.get(j))<48.0) {
            curveVertex(p1[3]+centerX, p2[3]+centerY);
        } else if (48.0<=Float.parseFloat(result_theta.get(j)) && Float.parseFloat(result_theta.get(j))<51.0) {
            curveVertex(q1[3]+centerX, q2[3]+centerY);
        } else if (51.0<=Float.parseFloat(result_theta.get(j)) && Float.parseFloat(result_theta.get(j))<54.0) {
            curveVertex(r1[3]+centerX, r2[3]+centerY);
        } else if (54.0<=Float.parseFloat(result_theta.get(j)) && Float.parseFloat(result_theta.get(j))<57.0) {
            curveVertex(s1[3]+centerX, s2[3]+centerY);
        } else if (57.0<=Float.parseFloat(result_theta.get(j)) && Float.parseFloat(result_theta.get(j))<60.0) {
            curveVertex(t1[3]+centerX, t2[3]+centerY);
        } else if (60.0<=Float.parseFloat(result_theta.get(j)) && Float.parseFloat(result_theta.get(j))<75.0) {
            curveVertex(u1[3]+centerX, u2[3]+centerY);
        } else if (75.0<=Float.parseFloat(result_theta.get(j)) && Float.parseFloat(result_theta.get(j))<=100.0) {
            curveVertex(v1[3]+centerX, v2[3]+centerY);
        }


        // delta
        if (0<=Float.parseFloat(result_delta.get(j)) && Float.parseFloat(result_delta.get(j))<3.0) {
            curveVertex(a1[4]+centerX, a2[4]+centerY);
        } else if (3.0<=Float.parseFloat(result_delta.get(j)) && Float.parseFloat(result_delta.get(j))<6.0) {
            curveVertex(b1[4]+centerX, b2[4]+centerY);
        } else if (6.0<=Float.parseFloat(result_delta.get(j)) && Float.parseFloat(result_delta.get(j))<9.0) {
            curveVertex(c1[4]+centerX, c2[4]+centerY);
        } else if (9.0<=Float.parseFloat(result_delta.get(j)) && Float.parseFloat(result_delta.get(j))<12.0) {
            curveVertex(d1[4]+centerX, d2[4]+centerY);
        } else if (12.0<=Float.parseFloat(result_delta.get(j)) && Float.parseFloat(result_delta.get(j))<15.0) {
            curveVertex(e1[4]+centerX, e2[4]+centerY);
        } else if (15.0<=Float.parseFloat(result_delta.get(j)) && Float.parseFloat(result_delta.get(j))<18.0) {
            curveVertex(f1[4]+centerX, f2[4]+centerY);
        } else if (18.0<=Float.parseFloat(result_delta.get(j)) && Float.parseFloat(result_delta.get(j))<21.0) {
            curveVertex(g1[4]+centerX, g2[4]+centerY);
        } else if (21.0<=Float.parseFloat(result_delta.get(j)) && Float.parseFloat(result_delta.get(j))<24.0) {
            curveVertex(h1[4]+centerX, h2[4]+centerY);
        } else if (24.0<=Float.parseFloat(result_delta.get(j)) && Float.parseFloat(result_delta.get(j))<27.0) {
            curveVertex(i1[4]+centerX, i2[4]+centerY);
        } else if (27.0<=Float.parseFloat(result_delta.get(j)) && Float.parseFloat(result_delta.get(j))<30.0) {
            curveVertex(j1[4]+centerX, j2[4]+centerY);
        } else if (30.0<=Float.parseFloat(result_delta.get(j)) && Float.parseFloat(result_delta.get(j))<33.0) {
            curveVertex(k1[4]+centerX, k2[4]+centerY);
        } else if (33.0<=Float.parseFloat(result_delta.get(j)) && Float.parseFloat(result_delta.get(j))<36.0) {
            curveVertex(l1[4]+centerX, l2[4]+centerY);
        } else if (36.0<=Float.parseFloat(result_delta.get(j)) && Float.parseFloat(result_delta.get(j))<39.0) {
            curveVertex(m1[4]+centerX, m2[4]+centerY);
        } else if (39.0<=Float.parseFloat(result_delta.get(j)) && Float.parseFloat(result_delta.get(j))<42.0) {
            curveVertex(n1[4]+centerX, n2[4]+centerY);
        } else if (42.0<=Float.parseFloat(result_delta.get(j)) && Float.parseFloat(result_delta.get(j))<45.0) {
            curveVertex(o1[4]+centerX, o2[4]+centerY);
        } else if (45.0<=Float.parseFloat(result_delta.get(j)) && Float.parseFloat(result_delta.get(j))<48.0) {
            curveVertex(p1[4]+centerX, p2[4]+centerY);
        } else if (48.0<=Float.parseFloat(result_delta.get(j)) && Float.parseFloat(result_delta.get(j))<51.0) {
            curveVertex(q1[4]+centerX, q2[4]+centerY);
        } else if (51.0<=Float.parseFloat(result_delta.get(j)) && Float.parseFloat(result_delta.get(j))<54.0) {
            curveVertex(r1[4]+centerX, r2[4]+centerY);
        } else if (54.0<=Float.parseFloat(result_delta.get(j)) && Float.parseFloat(result_delta.get(j))<57.0) {
            curveVertex(s1[4]+centerX, s2[4]+centerY);
        } else if (57.0<=Float.parseFloat(result_delta.get(j)) && Float.parseFloat(result_delta.get(j))<60.0) {
            curveVertex(t1[4]+centerX, t2[4]+centerY);
        } else if (60.0<=Float.parseFloat(result_delta.get(j)) && Float.parseFloat(result_delta.get(j))<75.0) {
            curveVertex(u1[4]+centerX, u2[4]+centerY);
        } else if (75.0<=Float.parseFloat(result_delta.get(j)) && Float.parseFloat(result_delta.get(j))<=100.0) {
            curveVertex(v1[4]+centerX, v2[4]+centerY);
        }


        // gamma
        if (0<=Float.parseFloat(result_gamma.get(j)) && Float.parseFloat(result_gamma.get(j))<3.0) {
            curveVertex(a1[5]+centerX, a2[5]+centerY);
        } else if (3.0<=Float.parseFloat(result_gamma.get(j)) && Float.parseFloat(result_gamma.get(j))<6.0) {
            curveVertex(b1[5]+centerX, b2[5]+centerY);
        } else if (6.0<=Float.parseFloat(result_gamma.get(j)) && Float.parseFloat(result_gamma.get(j))<9.0) {
            curveVertex(c1[5]+centerX, c2[5]+centerY);
        } else if (9.0<=Float.parseFloat(result_gamma.get(j)) && Float.parseFloat(result_gamma.get(j))<12.0) {
            curveVertex(d1[5]+centerX, d2[5]+centerY);
        } else if (12.0<=Float.parseFloat(result_gamma.get(j)) && Float.parseFloat(result_gamma.get(j))<15.0) {
            curveVertex(e1[5]+centerX, e2[5]+centerY);
        } else if (15.0<=Float.parseFloat(result_gamma.get(j)) && Float.parseFloat(result_gamma.get(j))<18.0) {
            curveVertex(f1[5]+centerX, f2[5]+centerY);
        } else if (18.0<=Float.parseFloat(result_gamma.get(j)) && Float.parseFloat(result_gamma.get(j))<21.0) {
            curveVertex(g1[5]+centerX, g2[5]+centerY);
        } else if (21.0<=Float.parseFloat(result_gamma.get(j)) && Float.parseFloat(result_gamma.get(j))<24.0) {
            curveVertex(h1[5]+centerX, h2[5]+centerY);
        } else if (24.0<=Float.parseFloat(result_gamma.get(j)) && Float.parseFloat(result_gamma.get(j))<27.0) {
            curveVertex(i1[5]+centerX, i2[5]+centerY);
        } else if (27.0<=Float.parseFloat(result_gamma.get(j)) && Float.parseFloat(result_gamma.get(j))<30.0) {
            curveVertex(j1[5]+centerX, j2[5]+centerY);
        } else if (30.0<=Float.parseFloat(result_gamma.get(j)) && Float.parseFloat(result_gamma.get(j))<33.0) {
            curveVertex(k1[5]+centerX, k2[5]+centerY);
        } else if (33.0<=Float.parseFloat(result_gamma.get(j)) && Float.parseFloat(result_gamma.get(j))<36.0) {
            curveVertex(l1[5]+centerX, l2[5]+centerY);
        } else if (36.0<=Float.parseFloat(result_gamma.get(j)) && Float.parseFloat(result_gamma.get(j))<39.0) {
            curveVertex(m1[5]+centerX, m2[5]+centerY);
        } else if (39.0<=Float.parseFloat(result_gamma.get(j)) && Float.parseFloat(result_gamma.get(j))<42.0) {
            curveVertex(n1[5]+centerX, n2[5]+centerY);
        } else if (42.0<=Float.parseFloat(result_gamma.get(j)) && Float.parseFloat(result_gamma.get(j))<45.0) {
            curveVertex(o1[5]+centerX, o2[5]+centerY);
        } else if (45.0<=Float.parseFloat(result_gamma.get(j)) && Float.parseFloat(result_gamma.get(j))<48.0) {
            curveVertex(p1[5]+centerX, p2[5]+centerY);
        } else if (48.0<=Float.parseFloat(result_gamma.get(j)) && Float.parseFloat(result_gamma.get(j))<51.0) {
            curveVertex(q1[5]+centerX, q2[5]+centerY);
        } else if (51.0<=Float.parseFloat(result_gamma.get(j)) && Float.parseFloat(result_gamma.get(j))<54.0) {
            curveVertex(r1[5]+centerX, r2[5]+centerY);
        } else if (54.0<=Float.parseFloat(result_gamma.get(j)) && Float.parseFloat(result_gamma.get(j))<57.0) {
            curveVertex(s1[5]+centerX, s2[5]+centerY);
        } else if (57.0<=Float.parseFloat(result_gamma.get(j)) && Float.parseFloat(result_gamma.get(j))<60.0) {
            curveVertex(t1[5]+centerX, t2[5]+centerY);
        } else if (60.0<=Float.parseFloat(result_gamma.get(j)) && Float.parseFloat(result_gamma.get(j))<75.0) {
            curveVertex(u1[5]+centerX, u2[5]+centerY);
        } else if (75.0<=Float.parseFloat(result_gamma.get(j)) && Float.parseFloat(result_gamma.get(j))<=100.0) {
            curveVertex(v1[5]+centerX, v2[5]+centerY);
        }


        // high beta
        if (0<=Float.parseFloat(result_high_beta.get(j)) && Float.parseFloat(result_high_beta.get(j))<3.0) {
            curveVertex(a1[6]+centerX, a2[6]+centerY);
        } else if (3.0<=Float.parseFloat(result_high_beta.get(j)) && Float.parseFloat(result_high_beta.get(j))<6.0) {
            curveVertex(b1[6]+centerX, b2[6]+centerY);
        } else if (6.0<=Float.parseFloat(result_high_beta.get(j)) && Float.parseFloat(result_high_beta.get(j))<9.0) {
            curveVertex(c1[6]+centerX, c2[6]+centerY);
        } else if (9.0<=Float.parseFloat(result_high_beta.get(j)) && Float.parseFloat(result_high_beta.get(j))<12.0) {
            curveVertex(d1[6]+centerX, d2[6]+centerY);
        } else if (12.0<=Float.parseFloat(result_high_beta.get(j)) && Float.parseFloat(result_high_beta.get(j))<15.0) {
            curveVertex(e1[6]+centerX, e2[6]+centerY);
        } else if (15.0<=Float.parseFloat(result_high_beta.get(j)) && Float.parseFloat(result_high_beta.get(j))<18.0) {
            curveVertex(f1[6]+centerX, f2[6]+centerY);
        } else if (18.0<=Float.parseFloat(result_high_beta.get(j)) && Float.parseFloat(result_high_beta.get(j))<21.0) {
            curveVertex(g1[6]+centerX, g2[6]+centerY);
        } else if (21.0<=Float.parseFloat(result_high_beta.get(j)) && Float.parseFloat(result_high_beta.get(j))<24.0) {
            curveVertex(h1[6]+centerX, h2[6]+centerY);
        } else if (24.0<=Float.parseFloat(result_high_beta.get(j)) && Float.parseFloat(result_high_beta.get(j))<27.0) {
            curveVertex(i1[6]+centerX, i2[6]+centerY);
        } else if (27.0<=Float.parseFloat(result_high_beta.get(j)) && Float.parseFloat(result_high_beta.get(j))<30.0) {
            curveVertex(j1[6]+centerX, j2[6]+centerY);
        } else if (30.0<=Float.parseFloat(result_high_beta.get(j)) && Float.parseFloat(result_high_beta.get(j))<33.0) {
            curveVertex(k1[6]+centerX, k2[6]+centerY);
        } else if (33.0<=Float.parseFloat(result_high_beta.get(j)) && Float.parseFloat(result_high_beta.get(j))<36.0) {
            curveVertex(l1[6]+centerX, l2[6]+centerY);
        } else if (36.0<=Float.parseFloat(result_high_beta.get(j)) && Float.parseFloat(result_high_beta.get(j))<39.0) {
            curveVertex(m1[6]+centerX, m2[6]+centerY);
        } else if (39.0<=Float.parseFloat(result_high_beta.get(j)) && Float.parseFloat(result_high_beta.get(j))<42.0) {
            curveVertex(n1[6]+centerX, n2[6]+centerY);
        } else if (42.0<=Float.parseFloat(result_high_beta.get(j)) && Float.parseFloat(result_high_beta.get(j))<45.0) {
            curveVertex(o1[6]+centerX, o2[6]+centerY);
        } else if (45.0<=Float.parseFloat(result_high_beta.get(j)) && Float.parseFloat(result_high_beta.get(j))<48.0) {
            curveVertex(p1[6]+centerX, p2[6]+centerY);
        } else if (48.0<=Float.parseFloat(result_high_beta.get(j)) && Float.parseFloat(result_high_beta.get(j))<51.0) {
            curveVertex(q1[6]+centerX, q2[6]+centerY);
        } else if (51.0<=Float.parseFloat(result_high_beta.get(j)) && Float.parseFloat(result_high_beta.get(j))<54.0) {
            curveVertex(r1[6]+centerX, r2[6]+centerY);
        } else if (54.0<=Float.parseFloat(result_high_beta.get(j)) && Float.parseFloat(result_high_beta.get(j))<57.0) {
            curveVertex(s1[6]+centerX, s2[6]+centerY);
        } else if (57.0<=Float.parseFloat(result_high_beta.get(j)) && Float.parseFloat(result_high_beta.get(j))<60.0) {
            curveVertex(t1[6]+centerX, t2[6]+centerY);
        } else if (60.0<=Float.parseFloat(result_high_beta.get(j)) && Float.parseFloat(result_high_beta.get(j))<75.0) {
            curveVertex(u1[6]+centerX, u2[6]+centerY);
        } else if (75.0<=Float.parseFloat(result_high_beta.get(j)) && Float.parseFloat(result_high_beta.get(j))<=100.0) {
            curveVertex(v1[6]+centerX, v2[6]+centerY);
        }


        // end controlpoint
        if (num == 1) {
            curveVertex(a1[0]+centerX, a2[0]+centerY);
            curveVertex(a1[1]+centerX, a2[1]+centerY);
        } else if (num == 2) {
            curveVertex(b1[0]+centerX, b2[0]+centerY);
            curveVertex(b1[1]+centerX, b2[1]+centerY);
        } else if (num == 3) {
            curveVertex(c1[0]+centerX, c2[0]+centerY);
            curveVertex(c1[1]+centerX, c2[1]+centerY);
        } else if (num == 4) {
            curveVertex(d1[0]+centerX, d2[0]+centerY);
            curveVertex(d1[1]+centerX, d2[1]+centerY);
        } else if (num == 5) {
            curveVertex(e1[0]+centerX, e2[0]+centerY);
            curveVertex(e1[1]+centerX, e2[1]+centerY);
        } else if (num == 6) {
            curveVertex(f1[0]+centerX, f2[0]+centerY);
            curveVertex(f1[1]+centerX, f2[1]+centerY);
        } else if (num == 7) {
            curveVertex(g1[0]+centerX, g2[0]+centerY);
            curveVertex(g1[1]+centerX, g2[1]+centerY);
        } else if (num == 8) {
            curveVertex(h1[0]+centerX, h2[0]+centerY);
            curveVertex(h1[1]+centerX, h2[1]+centerY);
        } else if (num == 9) {
            curveVertex(i1[0]+centerX, i2[0]+centerY);
            curveVertex(i1[1]+centerX, i2[1]+centerY);
        } else if (num == 10) {
            curveVertex(j1[0]+centerX, j2[0]+centerY);
            curveVertex(j1[1]+centerX, j2[1]+centerY);
        } else if (num == 11) {
            curveVertex(k1[0]+centerX, k2[0]+centerY);
            curveVertex(k1[1]+centerX, k2[1]+centerY);
        } else if (num == 12) {
            curveVertex(l1[0]+centerX, l2[0]+centerY);
            curveVertex(l1[1]+centerX, l2[1]+centerY);
        } else if (num == 13) {
            curveVertex(m1[0]+centerX, m2[0]+centerY);
            curveVertex(m1[1]+centerX, m2[1]+centerY);
        } else if (num == 14) {
            curveVertex(n1[0]+centerX, n2[0]+centerY);
            curveVertex(n1[1]+centerX, n2[1]+centerY);
        } else if (num == 15) {
            curveVertex(o1[0]+centerX, o2[0]+centerY);
            curveVertex(o1[1]+centerX, o2[1]+centerY);
        } else if (num == 16) {
            curveVertex(p1[0]+centerX, p2[0]+centerY);
            curveVertex(p1[1]+centerX, p2[1]+centerY);
        } else if (num == 17) {
            curveVertex(q1[0]+centerX, q2[0]+centerY);
            curveVertex(q1[1]+centerX, q2[1]+centerY);
        } else if (num == 18) {
            curveVertex(r1[0]+centerX, r2[0]+centerY);
            curveVertex(r1[1]+centerX, r2[1]+centerY);
        } else if (num == 19) {
            curveVertex(s1[0]+centerX, s2[0]+centerY);
            curveVertex(s1[1]+centerX, s2[1]+centerY);
        } else if (num == 20) {
            curveVertex(t1[0]+centerX, t2[0]+centerY);
            curveVertex(t1[1]+centerX, t2[1]+centerY);
        } else if (num == 21) {
            curveVertex(u1[0]+centerX, u2[0]+centerY);
            curveVertex(u1[1]+centerX, u2[1]+centerY);
        } else if (num == 22) {
            curveVertex(v1[0]+centerX, v2[0]+centerY);
            curveVertex(v1[1]+centerX, v2[1]+centerY);
        }

        if ( j==result_alpha.size()-1 ) {
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