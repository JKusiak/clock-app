package com.example.alarmclockapp;

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


    public int getAlarmId() {
        return alarmId;
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }
}