package com.example.alarmapp.activity;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
//import android.content.DialogInterface;
//import android.content.DialogInterface.OnCancelListener;
import android.view.WindowManager;
import com.example.alarmapp.R;
import com.example.alarmapp.service.AlarmService;


public class AlarmActivity extends AppCompatActivity {


    Button button;
    AlarmService alarmServiceInstance;
    public ProgressDialog mDialog;
    int i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
            WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
            WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON |
            WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.activity_main);
        button = (Button) findViewById(R.id.stopBtn);
        //↓ここなければ一応動く
        mDialog = new ProgressDialog(this);
        alarmServiceInstance = new AlarmService(this, mDialog);
        alarmServiceInstance.execute();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alarmServiceInstance.cancel(true);
            }
        });
    }
}

