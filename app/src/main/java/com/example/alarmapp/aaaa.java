package com.example.alarmapp;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class aaaa extends AppCompatActivity
        implements View.OnClickListener, TextToSpeech.OnInitListener {

    private TextToSpeech tts;
    private static final String TAG = "TestTTS";
    private String a = "60分前です";
    private String b = "45分前です";
    private String c = "30分前です";
    private String d = "15分前です";
    private String e = "10分前です";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // TTS インスタンス生成
        tts = new TextToSpeech(this, this);

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

    @Override
    public void onClick(View v) {
        speechText();
    }{

       speechText();

    }
    private void shutDown(){
        if (null != tts) {
            // to release the resource of TextToSpeech
            tts.shutdown();
        }
    }

    private void speechText() {

        String string = a;


        if (0 < string.length()) {
            if (tts.isSpeaking()) {
                tts.stop();
                return;
            }
            setSpeechRate();
            setSpeechPitch();

            if (Build.VERSION.SDK_INT >= 21){
                // SDK 21 以上
                tts.speak(string, TextToSpeech.QUEUE_FLUSH, null, "messageID");
            }

            setTtsListener();
        }
    }

    // 読み上げのスピード
    private void setSpeechRate(){
        if (null != tts) {
            tts.setSpeechRate((float) 1.0);
        }
    }

    // 読み上げのピッチ
    private void setSpeechPitch(){
        if (null != tts) {
            tts.setPitch((float) 1.0);
        }
    }

    // 読み上げの始まりと終わりを取得
    private void setTtsListener(){
        if (Build.VERSION.SDK_INT >= 21){
            int listenerResult =
                    tts.setOnUtteranceProgressListener(new UtteranceProgressListener() {
                        @Override
                        public void onDone(String utteranceId) {
                            Log.d(TAG,"progress on Done " + utteranceId);
                        }

                        @Override
                        public void onError(String utteranceId) {
                            Log.d(TAG,"progress on Error " + utteranceId);
                        }

                        @Override
                        public void onStart(String utteranceId) {
                            Log.d(TAG,"progress on Start " + utteranceId);
                        }
                    });

            if (listenerResult != TextToSpeech.SUCCESS) {
                Log.e(TAG, "failed to add utterance progress listener");
            }
        }
        else {
            Log.e(TAG, "Build VERSION is less than API 15");
        }
    }

    protected void onDestroy() {
        super.onDestroy();
        shutDown();
    }


}

