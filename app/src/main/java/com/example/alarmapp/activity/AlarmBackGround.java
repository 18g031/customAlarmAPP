package com.example.alarmapp.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Button;

import com.example.alarmapp.R;

import java.util.Calendar;
import java.util.Date;

//アラーム動作のバックグラウンド

public class AlarmBackGround extends AppCompatActivity
        implements TextToSpeech.OnInitListener {


        private EditText edit = null;


        private int cal_hour;
        private int cal_minute;
        private TextToSpeech tts;
        private static final String TAG = "TestTTS";

        private String a = "一時間前です";
        private String b = "45分前です";
        private String c = "30分前です";
        private String d = "15分前です";
        private String e = "10分前です";
        private String f = "5分前です";
        private String g = "出発時刻です";

        //tts
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            Intent intent = getIntent();
        }


        @Override
        public void onInit(int status) {
            // TTS初期化
            if (TextToSpeech.SUCCESS == status) {
                Log.d(TAG, "initialized");
            } else {
                Log.e(TAG, "failed to initialize");
            }
        }


        public void onTime() {


            speechText();
        }

        private void shutDown() {
            if (null != tts) {
                // to release the resource of TextToSpeech
                tts.shutdown();
            }
        }
        public void annset(){


        }

        public void speechText() {

            String syuppatu = a;


            if (0 < syuppatu.length()) {
                if (tts.isSpeaking()) {
                    tts.stop();
                    return;
                }
                setSpeechRate();
                setSpeechPitch();

                if (Build.VERSION.SDK_INT >= 21) {
                    // SDK 21 以上
                    tts.speak(syuppatu, TextToSpeech.QUEUE_FLUSH, null, "messageID");
                }

                setTtsListener();
            }
        }

        // 読み上げのスピード
        private void setSpeechRate() {
            if (null != tts) {
                tts.setSpeechRate((float) 1.0);
            }
        }

        // 読み上げのピッチ
        private void setSpeechPitch() {
            if (null != tts) {
                tts.setPitch((float) 1.0);
            }
        }

        // 読み上げの始まりと終わりを取得
        private void setTtsListener() {
            if (Build.VERSION.SDK_INT >= 21) {
                int listenerResult =
                        tts.setOnUtteranceProgressListener(new UtteranceProgressListener() {
                            @Override
                            public void onDone(String utteranceId) {
                                Log.d(TAG, "progress on Done " + utteranceId);
                            }

                            @Override
                            public void onError(String utteranceId) {
                                Log.d(TAG, "progress on Error " + utteranceId);
                            }

                            @Override
                            public void onStart(String utteranceId) {
                                Log.d(TAG, "progress on Start " + utteranceId);
                            }
                        });

                if (listenerResult != TextToSpeech.SUCCESS) {
                    Log.e(TAG, "failed to add utterance progress listener");
                }
            } else {
                Log.e(TAG, "Build VERSION is less than API 15");
            }
        }

        protected void onDestroy() {
            super.onDestroy();
            shutDown();
        }



        //時間取得
        public void setCalendar() {

            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(System.currentTimeMillis());
            Date date = cal.getTime();
            cal_hour = date.getHours();
            cal_minute = date.getMinutes();
        }

        //アラーム削除
        public void alarmDelete(){
            // int alarmID = receiveIntent.getIntExtra(getString(R.string.alarm_id),-1);

            // alarmMgr = (AlarmManager)InputActivity.this.getSystemService(Context.ALARM_SERVICE);
            // Intent sendIntent = new Intent(InputActivity.this, AlarmReceiver.class);
            // alarmIntent = PendingIntent.getBroadcast(InputActivity.this, alarmID, sendIntent, 0);
            // alarmMgr.cancel(alarmIntent);
        }
    }


