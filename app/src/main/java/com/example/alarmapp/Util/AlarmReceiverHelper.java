package com.example.alarmapp.Util;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.Build;

import com.example.alarmapp.receiver.*;

import java.util.ArrayList;
import java.util.Calendar;

public class AlarmReceiverHelper {
/*
    // アラームのデータを取得
    public static AlarmList getAlarmsByID(int alarmID, SQLiteOpenHelper helper){

        ArrayList<AlarmList> data = new ArrayList<>();

        AlarmList item = null;
        try(SQLiteDatabase db = helper.getReadableDatabase()) {

            String[] cols ={"alarmid","name","alarttime"};
            String[] params = {String.valueOf(alarmID)};

            Cursor cs = db.query("alarms",cols,"alarmid = ?",params,
                    null,null,"alarmid",null);
            cs.moveToFirst();
            item = new AlarmList();
            item.setAlarmID(cs.getInt(0));
            item.setAlarmName(cs.getString(1));
            item.setTime(cs.getString(2));
        }
        return item;
    }

    // アラームをセット
    public static void setAlarm(Context context, ListItem item){

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(item.getHour()));
        calendar.set(Calendar.MINUTE, Integer.parseInt(item.getMinitsu()));
        calendar.set(Calendar.SECOND, 0);

        // 現在時刻を取得
        Calendar nowCalendar = Calendar.getInstance();
        nowCalendar.setTimeInMillis(System.currentTimeMillis());

        // 比較
        int diff = calendar.compareTo(nowCalendar);

        // 日付を設定
        if(diff <= 0){
            calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) + 1);
        }

        AlarmManager alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.setData(Uri.parse(String.valueOf(item.getAlarmID())));
        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, item.getAlarmID(), intent, 0);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            alarmMgr.setAlarmClock(new AlarmManager.AlarmClockInfo(calendar.getTimeInMillis(), null), alarmIntent);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmMgr.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), alarmIntent);
        } else {
            alarmMgr.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), alarmIntent);
        }
    }
*/


}
