package com.example.alarmapp.activity;


import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
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
import android.view.WindowManager;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.alarmapp.R;
import com.example.alarmapp.Util.DatabaseHelper;
import com.example.alarmapp.service.AlarmService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

//import android.content.DialogInterface;
//import android.content.DialogInterface.OnCancelListener;


public class AlarmActivity1 extends AppCompatActivity implements SensorEventListener {//シェイクのみ


    static MediaPlayer mp = new MediaPlayer();
    public ProgressDialog mDialog;
    Button button;
    AlarmService alarmServiceInstance;
    int i;
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

    //繰り返し曜日でアラームの再設定をしたい
    DatabaseHelper helper = new DatabaseHelper(AlarmActivity1.this);
    SQLiteDatabase db = helper.getWritableDatabase();
    int revivalId;//どこかから鳴らす_idを持ってきたい(ごめんなさいまだできていません。



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarmstop);
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


    }

    @Override
    protected void onPause() {
        super.onPause();

        // センサーの登録を解除
        if (mSensorManager != null)
            mSensorManager.unregisterListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // 加速度センサーのオブジェクト取得
        List<Sensor> sensors = mSensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER);

        // センサーを登録
        if (sensors.size() > 0) {
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

        switch (event.sensor.getType()) {

            // 加速度センサーのイベントをハンドリング
            case Sensor.TYPE_ACCELEROMETER:

                float x = event.values[0];
                float y = event.values[1];
                float z = event.values[2];
                long nowTime = System.currentTimeMillis();

                // 最初のイベント→値を保持するのみ
                if (beforeTime == -1) {
                    beforeX = x;
                    beforeY = y;
                    beforeZ = z;

                    beforeTime = nowTime;
                    break;
                }

                // 0.5秒間隔でチェック
                long diffTime = nowTime - beforeTime;
                if (diffTime < 500)
                    break;

                // 前回の値との差から、スピードを算出
                // すみません、どうしてこれでOKなのか、不勉強でまだ理解出来ていません。。。
                float speed = Math.abs(x + y + z - beforeX - beforeY - beforeZ) / diffTime * 10000;

                // スピードがしきい値以上の場合、振ってるとみなす
                if (speed > shakeSpeed) {
                    // 振ってると判断した回数が10以上、つまり5秒間振り続けたら、シャッフルする
                    if (++shakeCount >= 10) {
                        shakeCount = 0;
                        MediaPlayer mp = AlarmActivity1.mp;
//                alarmServiceInstance.cancel(true);
                        mp.stop();
                        //revival();
                        finish();
                    }
                } else {
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
        findViewById(R.id.stopBtn).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                MediaPlayer mp = AlarmActivity1.mp;
//                alarmServiceInstance.cancel(true);
                mp.stop();
                //revival();
                finish();
                //ringtone.stop(); // 停止
            }
        });

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
    public void revival(){
        List<String> alarmArray= new ArrayList<>();

        try{
            //データベースから読み込むためのＳＱＬ。_idがrevivalIdと一致するアラームのセット時間、曜日を取得する
            String sql = "SELECT _id ,tAlmHour ,tAlmMinute ,aSun ,aMon ,aTue ,aWed ,aThu ,aFri ,aSat FROM alarmList WHERE _id =?";
            SQLiteStatement stmt = db.compileStatement(sql);
            stmt.bindLong(1, revivalId);
            stmt.executeUpdateDelete(); //SQL文の実行

            Cursor cursor = db.rawQuery(sql, null);
            while(cursor.moveToNext()){
                int idxAlTH = cursor.getColumnIndex("tAlmHour");
                int idxAlTM = cursor.getColumnIndex("tAlmMinute");
                int idxASun = cursor.getColumnIndex("aSun");
                int idxAMon = cursor.getColumnIndex("aMon");
                int idxATue = cursor.getColumnIndex("aTue");
                int idxAWed = cursor.getColumnIndex("aWed");
                int idxAThu = cursor.getColumnIndex("aThu");
                int idxAFri = cursor.getColumnIndex("aFri");
                int idxASat = cursor.getColumnIndex("aSat");
                int alTH= cursor.getInt(idxAlTH);
                int alTM= cursor.getInt(idxAlTM);
                int alASun= cursor.getInt(idxASun);
                int alAMon= cursor.getInt(idxAMon);
                int alATue= cursor.getInt(idxATue);
                int alAWed= cursor.getInt(idxAWed);
                int alAThu= cursor.getInt(idxAThu);
                int alAaFri= cursor.getInt(idxAFri);
                int alASat= cursor.getInt(idxASat);

                alarmArray.add(alTH+alTM+alASun+alAMon+alATue+alAWed+alAThu+alAaFri+alASat+"");
            }
        } finally {
            //データベースは明示的にcloseしない方がよい？　→　http://hobby.txt-nifty.com/t1000/2010/11/sqliteandroid-f.html
            //db.close();
        }


    }


}


