package com.example.alarmapp;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

//バックグラウンド動作？

public class MessageService extends Service{

        public IBinder onBind(Intent intent)
        {
            return null;
        }

        public void onCreate()
        {
            Thread thread = new Thread(null, task, "sentMessageService");
            thread.start();
        }

        Runnable task = new Runnable()
        {
            public void run() {
                Intent messageBroadcast = new Intent();
                messageBroadcast.setAction("activityAction");
                sendBroadcast(messageBroadcast);
                stopSelf();
            }

        };
    }

