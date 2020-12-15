package com.example.alarmapp.service;//package com.example.soundgosei.service;
//
//import android.content.Context;
//import android.media.AudioAttributes;
//import android.media.AudioManager;
//import android.media.SoundPool;
//
//import com.example.soundgosei.R;
//
//
//public class Sound{
//
////    public SoundPool soundPool;
//    public int sound[] = new int[5];
//
//    public Sound(Context context) {
//
//        soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
//
//        sound[0] = soundPool.load(context, R.raw.sound1, 1);
//        sound[1] = soundPool.load(context, R.raw.sound2, 1);
//        sound[2] = soundPool.load(context, R.raw.sound3, 1);
//        sound[3] = soundPool.load(context, R.raw.sound4, 1);
//        sound[4] = soundPool.load(context, R.raw.sound5, 1);
//    }
//
//
//    //音を鳴らすための"メソッド"を書いていく
//    public void play1() {
//        soundPool.play(sound[0], 1.0f, 1.0f, 0, 0, 1.0f);
//    }
//
//}
