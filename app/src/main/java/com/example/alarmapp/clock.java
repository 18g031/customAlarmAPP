package com.example.alarmapp;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.app.AlarmManager;

import java.util.Calendar;

public class clock extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.clock);
        Intent intent = getIntent();

        findViewById(R.id.enter).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeConf(mhours(), mminutes());
            }
        });
    }

    //時を取得
    private String mhours() {
        EditText Ehour = (EditText) this.findViewById(R.id.hour);
        String hour = Ehour.getText().toString();
        return hour;
    }

    //分を取得
    private String mminutes() {
        EditText Eminute = (EditText) this.findViewById(R.id.minute);
        String minute = Eminute.getText().toString();
        return minute;
    }

    //設定した時刻を表示
    private void timeConf(String h, String m) {
        TextView confview = (TextView) this.findViewById(R.id.confview);
        confview.setText("設定した時間は　" + h + "：" + m);
    }
    public void timerSet(Calendar calendar){
   //実行するサービスを指定
        Intent intent = new Intent(getApplicationContext(), messageService.class);
        Context ct = getApplication();
        PendingIntent pendingIntent = PendingIntent.getService(ct, 0,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        // AlarmManager の設定・開始
        AlarmManager am = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        am.set(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),pendingIntent);
    }

}
