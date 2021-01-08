package com.example.alarmapp;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class AnnBackGround extends AppCompatActivity {


        //通知オブジェクトの用意と初期化
        Notification notification = null;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            //レイアウトファイルをコンテントビューとしてセット
            setContentView(R.layout.activity_main);
            //システムから通知マネージャー取得
            NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
            //アプリ名をチャンネルIDとして利用
            String chID = getString(R.string.app_name);

            //アンドロイドのバージョンで振り分け
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {     //APIが「26」以上の場合

                //通知チャンネルIDを生成してインスタンス化
                NotificationChannel notificationChannel = new NotificationChannel(chID, chID, NotificationManager.IMPORTANCE_DEFAULT);
                //通知の説明のセット
                notificationChannel.setDescription(chID);
                //通知チャンネルの作成
                notificationManager.createNotificationChannel(notificationChannel);
                //通知の生成と設定とビルド
                notification = new Notification.Builder(this, chID)
                        .setContentTitle(getString(R.string.app_name))  //通知タイトル
                        .setContentText("アプリ通知テスト26以上")        //通知内容
                        .setSmallIcon(R.drawable.ic_launcher_background)                  //通知用アイコン
                        .build();                                       //通知のビルド
            } else if(android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN){
                //APIが「25」以下の場合
                //通知の生成と設定とビルド
                notification = new Notification.Builder(this)
                        .setContentTitle(getString(R.string.app_name))
                        .setContentText("アプリ通知テスト25まで")
                        .setSmallIcon(R.drawable.ic_launcher_background)
                        .build();
            }else if(android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                //APIが「25」以下の場合
                //通知の生成と設定とビルド
                notification = new Notification.Builder(this)
                        .setContentTitle(getString(R.string.app_name))
                        .setContentText("アプリ通知テスト16以下まで")
                        .setSmallIcon(R.drawable.ic_launcher_background)
                        .build();

            }
            //通知の発行
            notificationManager.notify(1, notification);
        }
    }
