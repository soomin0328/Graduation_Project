package com.neurosky.algo_sdk_sample;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DayInfo {
    private boolean inMonth;
    private Date date;


    public String getDay() {
        SimpleDateFormat sdf = new SimpleDateFormat("d", Locale.getDefault());
        return sdf.format(date);
    }

    public boolean isInMonth() {
        return inMonth;
    }

    public void setInMonth(boolean inMonth) {
        this.inMonth = inMonth;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean isSameDay(Date date) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        //   cal1.setTime(date);
        cal2.setTime(this.date);

        boolean sameDay = cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) && cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);

        return sameDay;
    }
}




