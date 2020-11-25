package com.example.alarmapp;

public class AlarmList {
    private int alarmID = -1;
    private  String alarmTime = "null";
    private String announceTime = "null";
    private int alarmFlag = 1;
    private int announceFlag = 1;

    public String getAlTime() {
        return alarmTime;
    }

    public String getAlHour(){
        return getAlTime().substring(0,2);
    }

    public String getAlMinitsu(){
        return getAlTime().substring(3,5);
    }

    public String getAnTime() {
        return announceTime;
    }

    public String getAnHour(){
        return getAnTime().substring(0,2);
    }

    public String getAnMinitsu(){
        return getAnTime().substring(3,5);
    }


    public void setAlTime(String time) {
        this.alarmTime = time;
    }

    public void setAnTime(String time) {
        this.announceTime = time;
    }

    public void setAlarmID(int alarmID) {
        this.alarmID = alarmID;
    }

    public int getAlarmID() {
        return alarmID;
    }
}
