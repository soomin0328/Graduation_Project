package com.neurosky.algo_sdk_sample;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Nomalization {

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference("USERS").child("aa").child("EEG DATA");

    private ArrayList<String> result_alpha = new ArrayList<>();
    private ArrayList<String> result_low_beta = new ArrayList<>();
    private ArrayList<String> result_delta = new ArrayList<>();
    private ArrayList<String> result_gamma = new ArrayList<>();
    private ArrayList<String> result_theta = new ArrayList<>();

    private String data[] = new String[5];
    private Double addData[] = new Double[5];
    private String data2[] = new String[5];

    String str[] = new String[5];

    Double nz_result_alpha = 0.0;
    Double nz_result_low_beta = 0.0;
    Double nz_result_delta = 0.0;
    Double nz_result_gamma = 0.0;
    Double nz_result_theta = 0.0;

    public Nomalization(){
    }

    public Nomalization(final String alpha, final String low_beta, final String delta, final String gamma, final String theta) {
        setData();

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);

                    addData[0] = Double.parseDouble(alpha);
                    addData[1] = Double.parseDouble(low_beta);
                    addData[2] = Double.parseDouble(delta);
                    addData[3] = Double.parseDouble(gamma);
                    addData[4] = Double.parseDouble(theta);

                    Double[] age_alpha = new Double[result_alpha.size() + 1];
                    age_alpha[result_alpha.size()] = addData[0];

                    for (int i = 0; i < result_alpha.size(); i++) {
                        age_alpha[i] = Double.parseDouble(result_alpha.get(i));
                    }

                    Double[] age_low_beta = new Double[result_low_beta.size() + 1];
                    age_low_beta[result_low_beta.size()] = addData[1];

                    for (int i = 0; i < result_low_beta.size(); i++) {
                        age_low_beta[i] = Double.parseDouble(result_low_beta.get(i));
                    }

                    Double[] age_delta = new Double[result_delta.size() + 1];
                    age_delta[result_delta.size()] = addData[2];

                    for (int i = 0; i < result_delta.size(); i++) {
                        age_delta[i] = Double.parseDouble(result_delta.get(i));
                    }

                    Double[] age_gamma = new Double[result_gamma.size() + 1];
                    age_gamma[result_gamma.size()] = addData[3];

                    for (int i = 0; i < result_gamma.size(); i++) {
                        age_gamma[i] = Double.parseDouble(result_gamma.get(i));
                    }

                    Double[] age_theta = new Double[result_theta.size() + 1];
                    age_theta[result_theta.size()] = addData[4];

                    for (int i = 0; i < result_theta.size(); i++) {
                        age_theta[i] = Double.parseDouble(result_theta.get(i));
                    }

                    double min = calc_min(age_alpha);
                    double max = calc_max(age_alpha);
                    double min1 = calc_min(age_low_beta);
                    double max1 = calc_max(age_low_beta);
                    double min2 = calc_min(age_delta);
                    double max2 = calc_max(age_delta);
                    double min3 = calc_min(age_gamma);
                    double max3 = calc_max(age_gamma);
                    double min4 = calc_min(age_theta);
                    double max4 = calc_max(age_theta);


                    nz_result_alpha = min_max(age_alpha, min, max);
                    nz_result_low_beta = min_max(age_low_beta, min1, max1);
                    nz_result_delta = min_max(age_delta, min2, max2);
                    nz_result_gamma = min_max(age_gamma, min3, max3);
                    nz_result_theta = min_max(age_theta, min4, max4);

                    result_alpha.clear();
                    result_low_beta.clear();
                    result_delta.clear();
                    result_gamma.clear();
                    result_theta.clear();

                } catch (InterruptedException e) {

                }
            }
        });
        thread.start();
    }

    // 최소값 출력
    public static Double calc_min(Double age[]) {
        int i, j;
        Double min = 0.0;
        min = age[0];
        for (i = 1; i < age.length; i++) {
            if (age[i] < min) {
                min = age[i];
            }
        }
        return min;
    }

    // 최대값 뽑기
    public static Double calc_max(Double age[]) {
        int i, j;
        Double max = 0.0;
        max = age[0];
        for (i = 0; i < age.length; i++) {
            if (age[i] > max) {
                max = age[i];
            }
        }
        return max;
    }

    public String[] getData() {
        str[0] = String.valueOf(nz_result_alpha);
        str[1] = String.valueOf(nz_result_low_beta);
        str[2] = String.valueOf(nz_result_delta);
        str[3] = String.valueOf(nz_result_gamma);
        str[4] = String.valueOf(nz_result_theta);

        return str;
    }

    public void setData() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int j = 0;
                for (int s = 10; s <= 59; s++) {
                    int i = 0;
                    for (DataSnapshot snapshot : dataSnapshot.child("2018년").child("07월").child("31일").child("22시").child("19분").child(String.valueOf(s) + "초").getChildren()) {
                        data[i] = snapshot.getValue().toString();
                        i++;
                    }
                    result_alpha.add(data[0]);
                    result_low_beta.add(data[1]);
                    result_delta.add(data[2]);
                    result_gamma.add(data[3]);
                    result_theta.add(data[4]);

//                    Log.d("alpha",result_alpha.get(j));
//                    Log.d("low beta",result_low_beta.get(j));
//                    Log.d("delta",result_delta.get(j));
//                    Log.d("gamma",result_gamma.get(j));
//                    Log.d("theta",result_theta.get(j));
                    j++;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    // Function to perform min_max normalization
    public Double min_max(Double age[], double min, double max) {

        double new_min = 20;
        double new_max = 45;
        double result[] = new double[age.length];

        for (int i = 0; i < age.length; i++) {
            result[i] = (((age[i] - min) / (max - min)) * (new_max - new_min)) + new_min;
        }

        return result[age.length-1];
    }

}