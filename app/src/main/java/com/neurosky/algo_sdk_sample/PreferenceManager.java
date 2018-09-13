package com.neurosky.algo_sdk_sample;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

public class PreferenceManager {

    Context mContext;

    //컨텍스트를 가져온다.
    public PreferenceManager(Context context) {
        this.mContext = context;
    }

    // preference를 저장한다.
    public void savePreference(DataObj obj) {
        SharedPreferences prefs = android.preference.PreferenceManager.getDefaultSharedPreferences(mContext);
        SharedPreferences.Editor editor = prefs.edit();

        Gson gson = new Gson();
        String json = gson.toJson(obj);
        editor.putString("MyObject", json);
        editor.commit();
    }

    // preference를 불러온다.
    public DataObj getPreference() {
        SharedPreferences prefs = android.preference.PreferenceManager.getDefaultSharedPreferences(mContext);
        // 저장되었던 코드를 불러와 다시 오브젝트 데이터로 변환하여 리턴
        Gson gson = new Gson();
        String json = prefs.getString("MyObject", "");
        DataObj obj = gson.fromJson(json, DataObj.class);
        return obj;
    }

    public void clearPreference() {
        SharedPreferences prefs = android.preference.PreferenceManager.getDefaultSharedPreferences(mContext);
        prefs.edit().clear().commit();
    }
}
