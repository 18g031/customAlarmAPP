package com.example.alarmapp.activity;


import android.app.ProgressDialog;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.alarmapp.R;
import com.example.alarmapp.service.AlarmService;

import java.io.IOException;

//import android.content.DialogInterface;
//import android.content.DialogInterface.OnCancelListener;


public class AlarmActivity2tap extends AppCompatActivity {//ボタン のみ


    static MediaPlayer mp = new MediaPlayer();
    public ProgressDialog mDialog;
    Button button;
    AlarmService alarmServiceInstance;
    int i;
    Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
    private Context context = this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarmstop2tap);
        // MediaPlayer.createはprepareを実行してしまうのでnew MediaPlayer()を使う
        // MediaPlayer mp = MediaPlayer.create(context, uri);
        try {
            mp.setDataSource(context, uri);
            // setAudioStreamTypeはprepare前に実行する必要がある
            mp.setAudioStreamType(AudioManager.STREAM_ALARM);
            mp.setLooping(true);
            // prepareの前後で使えるメソッドが異なる
            mp.prepare();
            mp.seekTo(0);
            mp.start();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        // ツールバーを非表示
        // requestWindowFeature(Window.FEATURE_NO_TITLE);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON |
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        findViewById(R.id.stopBtn).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
//                alarmServiceInstance.cancel(true);
                mp.stop();
                finish();
                //ringtone.stop(); // 停止
            }
        });

    }

}


