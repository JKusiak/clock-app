package com.example.alarmclockapp;

import java.util.Calendar;

public class Alarm {
    private int alarmId;
    private int hour;
    private int minute;
    private boolean started;

    public Alarm(int alarmId, int hour, int minute, boolean started) {
        this.alarmId = alarmId;
        this.hour = hour;
        this.minute = minute;
        this.started = started;
    }

//    public void schedule() {
//
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTimeInMillis(System.currentTimeMillis());
//        calendar.set(Calendar.HOUR_OF_DAY, hour);
//        calendar.set(Calendar.MINUTE, minute);
//        calendar.set(Calendar.SECOND, 0);
//
//        // if alarm time has already passed, increment day by 1
//        if (calendar.getTimeInMillis() <= System.currentTimeMillis()) {
//            calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + 1);
//        }
//
//        this.started = true;
//    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }
}