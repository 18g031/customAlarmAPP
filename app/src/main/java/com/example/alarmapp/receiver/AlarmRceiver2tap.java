package com.example.alarmapp.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.alarmapp.activity.AlarmActivity2tap;

public class AlarmRceiver2tap extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent startActivityIntent = new Intent(context, AlarmActivity2tap.class);
        startActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(startActivityIntent);

    }
}
