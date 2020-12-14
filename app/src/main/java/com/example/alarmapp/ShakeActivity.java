package com.example.alarmapp;


import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import java.util.List;


public class ShakeActivity extends AppCompatActivity implements SensorEventListener {

    private static final int LOWEST_SPEED = 3000;  //1カウントに必要な端末の最低速度
    private static final int SHAKE_TIMEOUT = 1000;    //端末が最低速度で振られるまでの時間
    private static final int SHAKE_COUNT = 15; //端末が振るのに必要なカウント数
    private int shakeCount = 0;  //端末が振られたカウント数
    private long lastTimeDetectAcceleration = 0;    //一番最後に端末が最低速度以上の速度で振られた時間
    private float xDimen = 0;  //端末が一番最後に振られたときのX座標の位置
    private float yDimen = 0;  //端末が一番最後に振られたときのY座標の位置
    private float zDimen = 0;  //端末が一番最後に振られたときのZ座標の位置
    private SensorManager manager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //センサーサービスを利用する
        manager = (SensorManager)getSystemService(SENSOR_SERVICE);

        //バイブレーションのサービスを利用する
        //vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

    }

    @Override
    protected void onResume() {
        super.onResume();

        //加速度センサーの値を取得する
        List<Sensor> sensors = manager.getSensorList(Sensor.TYPE_ACCELEROMETER);

        if(sensors.size() > 0) {
            Sensor s = sensors.get(0);
            //リスナーの登録
            manager.registerListener(this, s, SensorManager.SENSOR_DELAY_UI);
        }
    }


    @Override
    protected void onPause(){
        super.onPause();

        //イベントリスナーの登録解除
        if(manager != null){
            manager.unregisterListener(this);
        }
    }

    /*
     * 端末が動いたときに呼び出される処理
     *  @param event : 端末の座標
     *  @return なし
     */
    @Override
    public void onSensorChanged(SensorEvent event) {

        // 現在の時間をチェック
        long now = System.currentTimeMillis();

        // 現在の時間と最後に加速度を検知した時間の差を計算する
        long diffTime = now - lastTimeDetectAcceleration;

        // 一定時間を越えていたとき
        if(diffTime > SHAKE_TIMEOUT){

            //カウントをリセットする
            shakeCount = 0;
        }

        //端末の座標位置を取得する
        float x = event.values[0];  //x座標
        float y = event.values[1];  //y座標
        float z = event.values[2];  //z座標

        //振られている端末の縦の速度を取得
        float speed = Math.abs(x + y + z - xDimen - yDimen - zDimen) / diffTime * 10000;

        //端末が動いた速度が一定速度よりも速い場合
        if(speed > LOWEST_SPEED){


            //カウントを1回増やす
            ++shakeCount;

            //端末のカウントが一定回数を超えた場合
            if(shakeCount >= SHAKE_COUNT){


                //MainActivityに遷移するように設定
                //Intent intent = new Intent(getApplicationContext(),MainActivity.class);

                //インテントの開始
                //startActivity(intent);

                //ShakeActivityの終了
                finish();
            }

        }

        //一番最後に端末が振られたときの時間と位置をセット
        lastTimeDetectAcceleration = now;
        xDimen = x;
        yDimen = y;
        zDimen = z;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}


