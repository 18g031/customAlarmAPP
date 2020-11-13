package com.example.alarmapp;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    private EditText edit = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

   // edit = (EditText)findViewById(R.id.edittext01_id);
  //  Button btn = (Button)findViewById(R.id.onButton1);
   // 　btn.setOnClickListener(new View.OnClickListener() {
    public void onButton1(View v) {
        Intent intent = new Intent(MainActivity.this,subActivity.class);
        intent.putExtra("keyword", edit.getText().toString());
        startActivity(intent);
    }
}
