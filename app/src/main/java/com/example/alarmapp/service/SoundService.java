package com.example.alarmapp.service;

//import android.app.IntentService;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.AsyncTask;

import com.example.soundgosei.R;

import java.util.Random;


public class SoundService extends AsyncTask<Integer, Integer, Integer> {
    Random r = new Random();
    int sound[] = new int[5];
    private Context context;
    public SoundPool soundPool;

    public SoundService(Context context){
        super();
        this.context = context;
        soundPool = new SoundPool(3, AudioManager.STREAM_MUSIC, 0);

        sound[0] = soundPool.load(context, R.raw.sound1, 1);
        sound[1] = soundPool.load(context, R.raw.sound2, 1);
        sound[2] = soundPool.load(context, R.raw.sound3, 1);
        sound[3] = soundPool.load(context, R.raw.sound4, 1);
        sound[4] = soundPool.load(context, R.raw.sound5, 1);
        soundPool.play(sound[0], 1.0f, 1.0f, 0, 0, 1.0f);
    }

    @Override
    protected Integer doInBackground(Integer... params) {


//        for(int i=0; i<10; i++){
//            int r1 = r.nextInt(3);
//            switch (r1){
//                case 0:
//                    soundPool.play(sound[0], 1.0f, 1.0f, 0, 0, 1.0f);
//                    break;
//                case 1:
//                    soundPool.play(sound[1], 1.0f, 1.0f, 0, 0, 1.0f);
//                    break;
//                case 2:
//                    soundPool.play(sound[2], 1.0f, 1.0f, 0, 0, 1.0f);
//                    break;
//                case 3:
//                    soundPool.play(sound[3], 1.0f, 1.0f, 0, 0, 1.0f);
//                    break;
//                case 4:
//                    soundPool.play(sound[4], 1.0f, 1.0f, 0, 0, 1.0f);
//                    break;
//            }
//        }



        soundPool.play(sound[0], 1.0f, 1.0f, 0, 0, 1.0f);

        soundPool.play(sound[1], 1.0f, 1.0f, 0, 0, 1.0f);

        soundPool.play(sound[2], 1.0f, 1.0f, 0, 0, 1.0f);

        soundPool.play(sound[3], 1.0f, 1.0f, 0, 0, 1.0f);

        soundPool.play(sound[4], 1.0f, 1.0f, 0, 0, 1.0f);

//        try {
//            Thread.sleep(1000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        return null;
    }

}
