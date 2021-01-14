package com.example.alarmapp.service;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.AsyncTask;
import com.example.alarmapp.R;


import java.util.Random;


public class AlarmService extends AsyncTask<Integer, Integer, Integer> implements DialogInterface.OnCancelListener {
    Random r = new Random();
    private int r1;
    private int sound[] = new int[10];
    private Context context;
    ProgressDialog mDialog;
    private SoundPool soundPool;

    public AlarmService(Context context, ProgressDialog mDialog){
        super();
        this.context = context;
        this.mDialog = mDialog;
        soundPool = new SoundPool(3, AudioManager.STREAM_MUSIC, 0);

        sound[0] = soundPool.load(context,R.raw.one, 1);
        sound[1] = soundPool.load(context, R.raw.two, 1);
        sound[2] = soundPool.load(context, R.raw.three, 1);
        sound[3] = soundPool.load(context, R.raw.four, 1);
        sound[4] = soundPool.load(context, R.raw.five, 1);
        sound[5] = soundPool.load(context, R.raw.six, 1);
        sound[6] = soundPool.load(context, R.raw.seven, 1);
        sound[7] = soundPool.load(context, R.raw.eight, 1);
        sound[8] = soundPool.load(context, R.raw.nine, 1);
        sound[9] = soundPool.load(context, R.raw.ten, 1);
//        sound[4] = soundPool.load(context, R.raw.sound5, 1);
    }

    // バックグラウンド処理の前に実行される処理
    @Override
    protected void onPreExecute() {
        // TODO Auto-generated method stub
        // Progress Dialogの表示
//    mDialog = new ProgressDialog(context);
//    mDialog.setTitle("しばらくお待ちください");
//    mDialog.setMessage("非同期処理テスト中....");
//    mDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
//    mDialog.setMax(100);
//    mDialog.setProgress(0);
//    mDialog.setCancelable(true);// Cancel有効に
//    mDialog.setOnCancelListener(this);
//    mDialog.show();
    }


    @Override
    protected Integer doInBackground(Integer... params) {

        soundPool.play(sound[0], 1.0f, 1.0f, 0, 0, 1.0f);
        try {
            Thread.sleep(300);
            soundPool.play(sound[0], 1.0f, 1.0f, 0, 0, 1.0f);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for(int i=0; i<40; i++){
            if(this.isCancelled()){
                for(int d=0;d<sound.length-1; d++)
                    soundPool.stop(sound[d]);
//                soundPool.unload(sound[d]);
                return null;
            }
            r1 = r.nextInt(10);
            switch (r1){
                case 0:
                    soundPool.play(sound[0], 1.0f, 1.0f, 0, 0, 1.0f);
                    break;
                case 1:
                    soundPool.play(sound[1], 1.0f, 1.0f, 0, 0, 1.0f);
                    break;
                case 2:
                    soundPool.play(sound[2], 1.0f, 1.0f, 0, 0, 1.0f);
                    break;
                case 3:
                    soundPool.play(sound[3], 1.0f, 1.0f, 0, 0, 1.0f);
                    break;
                case 4:
                    soundPool.play(sound[4], 1.0f, 1.0f, 0, 0, 1.0f);
                    break;
                case 5:
                    soundPool.play(sound[5], 1.0f, 1.0f, 0, 0, 1.0f);
                    break;
                case 6:
                    soundPool.play(sound[6], 1.0f, 1.0f, 0, 0, 1.0f);
                    break;
                case 7:
                    soundPool.play(sound[7], 1.0f, 1.0f, 0, 0, 1.0f);
                    break;
                case 8:
                    soundPool.play(sound[8], 1.0f, 1.0f, 0, 0, 1.0f);
                    break;
                case 9:
                    soundPool.play(sound[9], 1.0f, 1.0f, 0, 0, 1.0f);
                    break;
            }
            try {
                Thread.sleep(250);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onCancelled() {
        mDialog.dismiss();
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        cancel(true);
    }
}
