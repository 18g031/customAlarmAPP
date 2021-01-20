package com.example.alarmapp.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.alarmapp.Util.DatabaseHelper;
import com.example.alarmapp.activity.AlarmActivity;

public class AlarmReceiver extends BroadcastReceiver {

    /////このクラスはブロードキャストレシーバーを使って、受け取ったContextとIntentからAlarmReceiverHelperを用いて、アラームをハンドラに登録するための試行錯誤です
    /////要　このクラスと他クラスの　改善と修正
    /////前張ったサンプルを工夫すれば完成します（たぶん）




    private static final String TAG = AlarmReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent){
        Intent startActivityIntent = new Intent(context, AlarmActivity.class);
        startActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(startActivityIntent);
    }


//        // アラームを再登録
//        // 参考 PutExtraは使用できない
//        // https://stackoverflow.com/questions/12506391/retrieve-requestcode-from-alarm-broadcastreceiver
//        // リクエストコードに紐づくデータを取得
//        String requestCode = intent.getData().toString();
//        DatabaseHelper helper = DatabaseHelper.getInstance(context);
//        AlarmList item = AlarmReceiverHelper.getAlarmsByID(Integer.parseInt(requestCode), helper);
//
//        // アラームを設定
//        AlarmReceiverHelper.setAlarm(context, item);
//
//        Intent startActivityIntent = new Intent(context, AlarmActivity.class);
//        startActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        context.startActivity(startActivityIntent);
}
