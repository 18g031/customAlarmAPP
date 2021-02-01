package com.example.alarmapp.activity;


import android.app.ProgressDialog;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.alarmapp.R;
import com.example.alarmapp.service.AlarmService;

import java.io.IOException;
import java.util.List;

//import android.content.DialogInterface;
//import android.content.DialogInterface.OnCancelListener;


public class AlarmActivity extends AppCompatActivity  implements SensorEventListener {


    Button button;
    AlarmService alarmServiceInstance;
    public ProgressDialog mDialog;
    int i;
    static MediaPlayer mp = new MediaPlayer();
    Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
    private Context context = this;
    Ringtone ringtone = RingtoneManager.getRingtone(context, uri);

    // センサーマネージャ
    private SensorManager mSensorManager;

    // シェイク判定用のメンバー変数
    private float beforeX;
    private float beforeY;
    private float beforeZ;
    private long beforeTime = -1;   // 前回の時間

    private float shakeSpeed = 80;  // 振ってると判断するスピード
    private float shakeCount = 0;   // 振ってると判断した回数



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarmstop);
            // MediaPlayer.createはprepareを実行してしまうのでnew MediaPlayer()を使う
            // MediaPlayer mp = MediaPlayer.create(context, uri);
            MediaPlayer mp = this.mp;
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


        // センサーマネージャのインスタンス作成
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        // ツールバーを非表示
       // requestWindowFeature(Window.FEATURE_NO_TITLE);







        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
            WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
            WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON |
            WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        //setContentView(R.layout.activity_main);

        //↓ここなければ一応動く
//        mDialog = new ProgressDialog(this);
//        alarmServiceInstance = new AlarmService(this, mDialog);
//        alarmServiceInstance.execute();

        findViewById(R.id.stopBtn).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                MediaPlayer mp = AlarmActivity.mp;
//                alarmServiceInstance.cancel(true);
                mp.stop();
                finish();
                //ringtone.stop(); // 停止
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();

        // センサーの登録を解除
        if(mSensorManager != null)
            mSensorManager.unregisterListener(this);
    }

    @Override
    protected void onResume(){
        super.onResume();

        // 加速度センサーのオブジェクト取得
        List<Sensor> sensors = mSensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER);

        // センサーを登録
        if(sensors.size() > 0){
            Sensor s = sensors.get(0);
            mSensorManager.registerListener(this, s, SensorManager.SENSOR_DELAY_UI);
        }

        // 初期化
        beforeX = 0;
        beforeY = 0;
        beforeZ = 0;
        beforeTime = -1;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        switch(event.sensor.getType()){

            // 加速度センサーのイベントをハンドリング
            case Sensor.TYPE_ACCELEROMETER:

                float x = event.values[0];
                float y = event.values[1];
                float z = event.values[2];
                long nowTime = System.currentTimeMillis();

                // 最初のイベント→値を保持するのみ
                if( beforeTime == -1 ){
                    beforeX = x;
                    beforeY = y;
                    beforeZ = z;

                    beforeTime = nowTime;
                    break;
                }

                // 0.5秒間隔でチェック
                long diffTime = nowTime - beforeTime;
                if( diffTime < 500 )
                    break;

                // 前回の値との差から、スピードを算出
                // すみません、どうしてこれでOKなのか、不勉強でまだ理解出来ていません。。。
                float speed = Math.abs(x + y + z - beforeX - beforeY - beforeZ) / diffTime * 10000;

                // スピードがしきい値以上の場合、振ってるとみなす
                if( speed > shakeSpeed ){
                    // 振ってると判断した回数が10以上、つまり5秒間振り続けたら、シャッフルする
                    if(++shakeCount >= 10){
                        shakeCount = 0;
                        MediaPlayer mp = AlarmActivity.mp;
//                alarmServiceInstance.cancel(true);
                        mp.stop();
                        finish();
                    }
                }
                else {
                    // 途中でフリが収まった場合は、カウントを初期化
                    shakeCount = 0;
                }

                // 前回の値を覚える
                beforeX = x;
                beforeY = y;
                beforeZ = z;

                beforeTime = nowTime;

                break;
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}


