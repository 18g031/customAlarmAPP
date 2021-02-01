package com.example.alarmapp.activity;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.alarmapp.R;

public class AnnBackGround extends BroadcastReceiver {

    public static String NOTIFICATION_ID = "notificationId";
    public static String NOTIFICATION_CONTENT = "content";
    NotificationManager notificationManager;

    @Override
    public void onReceive(Context context, Intent intent) {
        notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        int id = intent.getIntExtra(NOTIFICATION_ID, 0);
        String content = intent.getStringExtra(NOTIFICATION_CONTENT);
        notificationManager.notify(id, buildNotification(context, content));
    }


        //通知オブジェクトの用意と初期化
        Notification.Builder notification = null;

       // @Override
        private Notification buildNotification(Context context, String content) {
            Notification.Builder builder = new Notification.Builder(context);
            //builder.setContentTitle("Notification!!")
             //       .setContentText(content)
            //        .setSmallIcon(android.R.drawable.sym_def_app_icon);

            //return builder.build();


    //レイアウトファイルをコンテントビューとしてセット
            //アプリ名をチャンネルIDとして利用
            String chID = String.valueOf(R.string.app_name);
            String name = String.valueOf(R.string.app_name);

            //アンドロイドのバージョンで振り分け
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {     //APIが「26」以上の場合

                //通知チャンネルIDを生成してインスタンス化
                NotificationChannel notificationChannel = new NotificationChannel(chID, chID, NotificationManager.IMPORTANCE_DEFAULT);
                //通知の説明のセット
                notificationChannel.setDescription(chID);
                //通知チャンネルの作成
                notificationManager.createNotificationChannel(notificationChannel);
                //通知の生成と設定とビルド
                notification = new Notification.Builder(context, chID);
                notification.setContentTitle(name);//通知タイトル
                notification.setContentText(content) ;       //通知内容
                notification.setSmallIcon(R.drawable.ic_launcher_background);                  //通知用アイコン
                notification.build();                                       //通知のビルド
            } else if(android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN){
                //APIが「25」以下の場合
                //通知の生成と設定とビルド
                notification = new Notification.Builder(context);
                notification.setContentTitle(name);
                notification.setContentText(content);
                notification.setSmallIcon(R.drawable.ic_launcher_background);
                notification.build();
            }else if(android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                //APIが「25」以下の場合
                //通知の生成と設定とビルド
                notification = new Notification.Builder(context);
                notification.setContentTitle(name);
                notification.setContentText(content);
                notification.setSmallIcon(R.drawable.ic_launcher_background);
                notification.build();

            }
            return builder.build();
        }
    }
