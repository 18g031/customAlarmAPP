package com.example.alarmapp.receiver;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.alarmapp.R;
import java.text.DateFormat;



public class AnnReceiver extends BroadcastReceiver {
    public static String NOTIFICATION_ID = "notificationId";
    public static String NOTIFICATION_CONTENT = "content";
    NotificationManager notificationManager;
    int important =NotificationManager.IMPORTANCE_HIGH;
    String channelID = "アナウンス通知";
    String name ="あああ";


    //通知オブジェクトの用意と初期化
    Notification notification = null;


    @Override
    public void onReceive(Context context, Intent intent){
    //システムから通知マネージャー取得
    notificationManager =(NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
    //アプリ名をチャンネルIDとして利用
    int chId = R.string.app_name;
    String chID = String.valueOf(chId);

    //アンドロイドのバージョンで振り分け
        if(android.os.Build.VERSION.SDK_INT >=android.os.Build.VERSION_CODES.O)

    {     //APIが「26」以上の場合

        //通知チャンネルIDを生成してインスタンス化
        NotificationChannel notificationChannel = new NotificationChannel(channelID, chID, important);
        //通知の説明のセット
        notificationChannel.setDescription(chID);
        //通知チャンネルの作成
        NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(notificationChannel);
        int id = intent.getIntExtra(NOTIFICATION_ID, 0);

        //通知の生成と設定とビルド
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelID)
                .setContentTitle("アナウンス通知")  //通知タイトル
                .setContentText("時刻を確認してください")        //通知内容
                .setSmallIcon(R.drawable.ic_launcher_background)           //通知用アイコン
                .setAutoCancel(true);
        NotificationManagerCompat.from(context).notify(id, builder.build());
                //通知のビルド
    } else{


        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        int id = intent.getIntExtra(NOTIFICATION_ID, 0);
        String content = intent.getStringExtra(NOTIFICATION_CONTENT);
        notificationManager.notify(id, buildNotification(context, content));
    }

}
    private Notification buildNotification(Context context, String content) {
        Notification.Builder builder = new Notification.Builder(context);
        builder.setContentTitle(content)
                .setContentText(content)
                .setSmallIcon(android.R.drawable.sym_def_app_icon)
                .setAutoCancel(true);
        return builder.build();
    }
}