package com.example.alarmapp;

//アラームに使うリスト作成

public class AlarmList {
    public static  int dummyID = -1;           //デモ用
    public static String dummyList[];
    private int alarmID = -1;           //アラーム識別用ID
    private  String alarmTime = "null";     //アラーム設定時間
    private String announceTime = "null";       //アナウンス設定時間
    private int settingFlag = 1;        //アラームとアナウンスのセットのオンオフフラグ(スタート画面から見えるもの)
    private int alarmFlag = 1;          //アラームのみのオンオフフラグ（アラーム編集画面から操作するもの）
    private int announceFlag = 1;       //アナウンスのみのオンオフフラグ（アラーム編集画面から操作するもの）

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


    String[] listData;


}
