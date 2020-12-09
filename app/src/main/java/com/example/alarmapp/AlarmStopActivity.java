package com.example.alarmapp;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;

import java.util.Random;

//import com.example.soundgosei.service.SoundService;//ファイル内の別クラス（サウンドサービス）


public class MainActivity extends AppCompatActivity {

    Button button;
    SoundService soundServiceInstance;
    int i;

    SoundPool soundPool;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = (Button) findViewById(R.id.stopBtn);
        //↓ここなければ一応動く
        soundServiceInstance = new SoundService(this);
        soundServiceInstance.execute();

        soundPool = new SoundPool(3, AudioManager.STREAM_MUSIC, 0);
        i = soundPool.load(this, R.raw.sound1, 1);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                soundPool.play(i, 1.0f, 1.0f, 0, 0, 1.0f);
            }
        });
    }
}

