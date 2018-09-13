package com.neurosky.algo_sdk_sample;

public class DataObj {
    public String time;
    public String val;

    public DataObj(String time, String val) {
        this.time = time;
        this.val = val;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val;
    }
}
