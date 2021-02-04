package com.example.alarmapp.activity;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.alarmapp.R;
import com.example.alarmapp.Util.DatabaseHelper;
import com.example.alarmapp.receiver.AlarmReceiver1shake;
import com.example.alarmapp.receiver.AnnReceiver;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;


//import com.example.alarmapp.AlermBroadcastReceiver;

//アラーム、アナウンスの編集画面

public class AlarmCreateActivity extends AppCompatActivity {

    public static Context context;
    public boolean Alm = true;
    public boolean Ann = true;
    int RandInt = 1;//Random範囲格納変数

    private final boolean[] mWeekCheckedItems = {false,false,false,false,false,false,false};
    private final boolean[] mAlmCheckedItems = {false, false, false, false, false};
    private final boolean[] mAlmCheckedItems2 = {true, false};
    private final boolean[] mAlmCheckedItems3 = {false, false, false, false, false};
    private final boolean[] mAnnCheckedItems = {false, false, false, false, false, true};
    int checkedItem = 0;

    TextView tvAlmTimer, tvAnnTimer,
            tvWeek, tv_alm_checkbox, tv_alm_checkbox2, tv_alm_checkbox3, tv_ann_checkbox;
    int setTAlmHour, setTAlmMinute, setTAnnHour, setTAnnMinute;
    int alarmId = -1;
    int AnnID = 0;
    int AnnDEL = 0;
    int annId = -1;
    int rTime;
    int tapId=-1;
    private Switch AlmSwitch = null;
    private Switch AnnSwitch = null;
    boolean stopshake = true;

    //timePickerで使用している変数名（tAlmHour, tAlmMinute,tAnnHour, tAnnMinute）をデータベース保存時も使用

    //以下timePicker用フォーマット変数（複数回使っていたので頭にまとめました）
    SimpleDateFormat f24Hours = new SimpleDateFormat(
            "HH:mm"
    );
    SimpleDateFormat f12Hours = new SimpleDateFormat(
            "hh:mm aa"
    );
    int kekka;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.clock);
        AlmSwitch = findViewById(R.id.switchAlm);
        AnnSwitch = findViewById(R.id.switchAnn);
        tvAlmTimer = findViewById(R.id.tv_alm_timer);
        tvAnnTimer = findViewById(R.id.tv_ann_timer);
        tvWeek = findViewById(R.id.tv_week);
        tv_alm_checkbox = findViewById(R.id.tv_alm_checkbox);
        tv_alm_checkbox2 = findViewById(R.id.tv_alm_checkbox2);
        tv_alm_checkbox3 = findViewById(R.id.tv_alm_checkbox3);
        tv_ann_checkbox = findViewById(R.id.tv_ann_checkbox);

        final Intent intent = getIntent();
        //前の画面(MainActivity)でタップされたアラームの_idをtapIdに格納する。
        //_idが存在しない(新規作成)ならば、-1を格納する。
        //削除メソッド(DatabaseHelper.alarmDelete)にtapIdを渡すだけで削除できるはず。
        tapId = intent.getIntExtra("TAPID", -1);
        if (tapId != -1) {
            List<Integer> dataArray = new ArrayList<>();
            Log.v("ACA_76", "tapId is " + tapId);//確認用（削除予定）
            DatabaseHelper helper = new DatabaseHelper(AlarmCreateActivity.this);    //データベースヘルパーオブジェクトを作成
            SQLiteDatabase db = helper.getWritableDatabase();
            String sql = "SELECT * FROM alarmList";
            Cursor cursor = db.rawQuery(sql, null);//SQL文を実行して結果をcursorに格納
            while (cursor.moveToNext()) { //cursorの最後まで繰り返す
                int idx_id = cursor.getColumnIndex("_id");  //columnが_idの値を取り出す
                if (idx_id == tapId) {    //idが同じなら他の値も取り出してdataArrayに格納して繰り返しを終了する。
                    int idxAlTH = cursor.getColumnIndex("tAlmHour");
                    int idxAlTM = cursor.getColumnIndex("tAlmMinute");
                    int idxAnTH = cursor.getColumnIndex("tAnnHour");
                    int idxAnTM = cursor.getColumnIndex("tAnnMinute");
                    int alTH = cursor.getInt(idxAlTH);
                    int alTM = cursor.getInt(idxAlTM);
                    int anTH = cursor.getInt(idxAnTH);
                    int anTM = cursor.getInt(idxAnTM);
                    dataArray.add(alTH);
                    dataArray.add(alTM);
                    dataArray.add(anTH);
                    dataArray.add(anTM);

                    //変数に初期値入力（データベースのupdate文で変数名がかぶってしまったため、一部変数名を変更しています
                    setTAlmHour = alTH;
                    setTAlmMinute = alTM;
                    setTAnnHour = anTH;
                    setTAnnMinute = anTM;

                    alarmId = tapId;

                    //ここからtimePickerの初期データ登録
                    try {
                        //アラームデータ格納
                        String alTime = alTH + ":" + alTM;
                        Date alDate = f24Hours.parse(alTime);
                        tvAlmTimer.setText(f12Hours.format(alDate));
                        //アナウンスデータ格納
                        String anTime = anTH + ":" + anTM;
                        Date anDate = f24Hours.parse(anTime);
                        tvAnnTimer.setText(f12Hours.format(anDate));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }


                    break;
                }
            }
//            tAlmHour = timeArray.get(0);
//            tAlmMinute = timeArray.get(1);
//            tAnnHour = timeArray.get(2);
//            tAnnMinute = timeArray.get(3);
        }


        tvWeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder
                        (AlarmCreateActivity.this, android.R.style.Theme_Material_Dialog);

                final String[] items = {"日曜日", "月曜日", "火曜日", "水曜日", "木曜日", "金曜日", "土曜日"};
                alertDialog.setTitle("繰り返し曜日");
                alertDialog.setMultiChoiceItems(items, mWeekCheckedItems, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        mWeekCheckedItems[which] = isChecked;
                    }
                });
                alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int idx) {
                        String str = null;
                        for (int i = 0; i < mWeekCheckedItems.length; i++) {
                            if (mWeekCheckedItems[i] == true) {
                                str += items[i];
                            }
                        }
                        if (str == null) {
                            str = "No Selected";
                        }
                        Toast.makeText(AlarmCreateActivity.this, str, Toast.LENGTH_LONG).show();
                    }
                });
                alertDialog.show();
            }
        });

        tv_alm_checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder
                        (AlarmCreateActivity.this,android.R.style.Theme_Material_Dialog);

                final String[] almItems = {"30分前から", "20分前から", "15分前から", "10分前から", "5分前から", "無効"};
                alertDialog.setTitle("ランダム範囲");
                alertDialog.setSingleChoiceItems(almItems, checkedItem, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        checkedItem = item;
                    }
                });
                alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int idx) {
                        String str = null;
                        for (int i = 0; i < mAlmCheckedItems.length; i++) {
                            if (mAlmCheckedItems[i] == true) {
                                str += almItems[i];
                                if (i == 0) {
                                    RandInt = 31;
                                } else if (i == 1) {
                                    RandInt = 21;
                                } else if (i == 2) {
                                    RandInt = 16;
                                } else if (i == 3) {
                                    RandInt = 11;
                                } else if (i == 4) {
                                    RandInt = 6;
                                } else {
                                    RandInt = 0;
                                }
                            }
                        }
                        if (str == null) {
                            str = "No Selected";
                        }
                        Toast.makeText(AlarmCreateActivity.this, str, Toast.LENGTH_LONG).show();
                    }
                });
                alertDialog.show();
            }
        });
        tv_alm_checkbox2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder
                        (AlarmCreateActivity.this,android.R.style.Theme_Material_Dialog);

                final String[] stopItems = {"シェイク", "通常"};
                alertDialog.setTitle("停止方法");
                alertDialog.setSingleChoiceItems(stopItems, checkedItem, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        checkedItem = item;
                    }
                });
                alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int idx) {
                        String str = null;
                        for (int i = 0; i < mAlmCheckedItems2.length; i++) {
                            if (mAlmCheckedItems2[i] == true) {
                                str = stopItems[i];
                                if (i == 0) {
                                    stopshake = true;
                                } else if (i == 1) {
                                    stopshake = false;
                                }
                            }
                        }
                        if (str == null) {
                            str = "No Selected";
                        }
                        Toast.makeText(AlarmCreateActivity.this, str, Toast.LENGTH_LONG).show();
                    }
                });
                alertDialog.show();
            }
        });


        tv_ann_checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder
                        (AlarmCreateActivity.this, android.R.style.Theme_Material_Dialog);

                final String[] annItems = {"30分前", "20分前", "15分前", "10分前", "5分前", "設定時刻"};
                alertDialog.setTitle("通知タイミング");
                alertDialog.setMultiChoiceItems(annItems, mAnnCheckedItems, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        mAnnCheckedItems[which] = isChecked;
                    }
                });
                alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int idx) {
                        String str = null;
                        for (int i = 0; i < mAnnCheckedItems.length; i++) {
                            if (mAnnCheckedItems[i] == true) {
                                str += annItems[i];
                            }
                        }
                        if (str == null) {
                            str = "No Selected";
                        }
                        Toast.makeText(AlarmCreateActivity.this, str, Toast.LENGTH_LONG).show();
                    }
                });
                alertDialog.show();
            }
        });

        //アラームのTimePickerの処理
        tvAlmTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        AlarmCreateActivity.this,
                        android.R.style.Theme_Holo_Dialog,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker View, int hourOfDay, int minute) {
                                setTAlmHour = hourOfDay;
                                setTAlmMinute = minute;
                                String time = setTAlmHour + ":" + setTAlmMinute;

                                try {
                                    Date date = f24Hours.parse(time);
                                    tvAlmTimer.setText(f12Hours.format(date));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, 12, 0, false
                );

                timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                timePickerDialog.updateTime(setTAlmHour, setTAlmMinute);
                timePickerDialog.show();
            }
        });


        //アナウンスのTimePickerの処理
        tvAnnTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        AlarmCreateActivity.this,
                        android.R.style.Theme_Holo_Dialog,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker View, int hourOfDay, int minute) {
                                setTAnnHour = hourOfDay;
                                setTAnnMinute = minute;
                                String time = setTAnnHour + ":" + setTAnnMinute;

                                try {
                                    Date date = f24Hours.parse(time);
                                    tvAnnTimer.setText(f12Hours.format(date));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, 12, 0, false
                );

                timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                timePickerDialog.updateTime(setTAnnHour, setTAnnMinute);
                timePickerDialog.show();
            }
        });


/*        setContentView(R.layout.clock);
        Intent intent = getIntent();
*/

        findViewById(R.id.enter).setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                int flag = 0;
                //データベースヘルパーオブジェクトを作成
                DatabaseHelper helper = new DatabaseHelper(AlarmCreateActivity.this);
                SQLiteDatabase db = helper.getWritableDatabase();
                //AlarmListクラスでアラームデータをデータベースに保存
                ////AlarmListクラスでアラームデータをデータベースに保存
                //AlarmList.alarmAdd(tAlmHour,tAlmMinute,tAnnHour,tAnnMinute,db);
                //AlarmList.alarmAdd(tAlmHour,tAlmMinute,tAnnHour,tAnnMinute,ランダム化したアラームの時間(H)の変数名,ランダム化したアラームの時間(M)の変数名,db);

                try {
                    Log.v("try直下", "" + flag);
                    if (flag == 0) {
                        if (tapId == -1) {
                            Log.v("try", "try の先頭を実行");
                            //保存されている最大の_idを取得するSQL文
                            String sql = "SELECT * FROM alarmList";
                            Cursor cursor = db.rawQuery(sql, null);//SQL文を実行して結果をcursorに格納
                            String str;
                            while (cursor.moveToNext()) {
                                int idxId = cursor.getColumnIndex("_id");
                                str = cursor.getString(idxId);
                                alarmId = Integer.parseInt(str);
                                Log.v("try", "" + alarmId);
                            }
                            alarmId += 1;
                            //保存するためのＳＱＬ。変数によって値が変わる場所は？にする
                            String sqlInsert = "INSERT INTO alarmList (_id, tAlmHour, tAlmMinute, tAnnHour, tAnnMinute, randomTime) VALUES (?, ?, ?, ?, ?, ?)";
                            //String sqlInsert = "INSERT INTO alarmList (_id, tAlmHour, tAlmMinute, tAnnHour, tAnnMinute, randomTime, almRepeat, annRepeat, timing) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
                            SQLiteStatement stmt = db.compileStatement(sqlInsert);  //プリペアドステートメントを取得
                            stmt.bindLong(1, alarmId);       //alarmListの1つ目のVALUESにalarmIdを入れる
                            stmt.bindLong(2, setTAlmHour);
                            stmt.bindLong(3, setTAlmMinute);
                            stmt.bindLong(4, setTAnnHour);
                            stmt.bindLong(5, setTAnnMinute);
                            stmt.bindLong(6, rTime);
//            stmt.bindLong(7,繰り返し曜日設定(アラーム));
//            stmt.bindLong(8,繰り返し曜日設定(アナウンス));
//            stmt.bindLong(9,アナウンスタイミング);
//            stmt.bindLong(10,);

                            stmt.executeInsert();       //SQL文を実行（データベースに保存）
                        } else if (tapId != -1) {
                            //以下更新動作
                            ContentValues cv = new ContentValues();  //更新用
                            int alarmId = tapId;

                            cv.put("_id", alarmId);
                            cv.put("tAlmHour", setTAlmHour);
                            cv.put("tAlmMinute", setTAlmMinute);
                            cv.put("tAnnHour", setTAnnHour);
                            cv.put("tAnnMinute", setTAnnMinute);
/*
                        cv.put("rAlmHou", ランダム化したアラームの時間(H));
                        cv.put("rAlmMinute", ランダム化したアラームの時間(M));
                        cv.put("almRepeat", 繰り返し曜日設定(アラーム));
                        cv.put("annRepeat", 繰り返し曜日設定(アナウンス));
                        cv.put("timing", アナウンスタイミング);
*/
                            db.update("alarmList", cv, "_id = " + alarmId, null);
                        }
                    } else if (flag == 1) {
                        //Toast.makeText(context , "保存できませんでした(曜日にチェックを入れてください)", Toast.LENGTH_LONG).show();
                        Log.v("370", "保存できませんでした");
                    }
                } finally {

                }

                Calendar keisan=null;
                if (Alm == true) {
                    Random random = new Random();
                    int randomValue = random.nextInt(RandInt);

                    setContentView(R.layout.clock);
                    try {
                        //試し
                        Calendar tamesi = Calendar.getInstance();
                        int tYear = tamesi.get(Calendar.YEAR);
                        int tMonth = tamesi.get(Calendar.MONTH);
                        int tDate =tamesi.get(Calendar.DATE);
                        tamesi.set(tYear,tMonth,tDate,setTAlmHour,setTAlmMinute,0);
                        Date date = tamesi.getTime();

                        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm");//date型に変えるためのインスタンス
                        String strtime = setTAlmHour + ":" + setTAlmMinute;//Integer.toString(setTAlmMinute);//intをstringに直す
                        Log.v("aaaa1", "" + strtime);
                        Date dateeeeeeeeeeeeeeee = sdf.parse(strtime);//ｓｔｒDateをdate型に変換
                        Log.v("aaaa2", "" + date);
                        keisan = Calendar.getInstance();//計算処理+現在時刻比較（設定時間の範囲内の場合一週間後に
                        keisan.setTime(date);
                        keisan.add(Calendar.MINUTE, -randomValue);
                        Calendar now = Calendar.getInstance();
                        if (keisan.getTimeInMillis() > now.getTimeInMillis()) {
                            keisan.setTime(date);
                            keisan.add(Calendar.MINUTE, -randomValue);//minuteには鳴る時間がはいってる。
                            Log.v("aaaa1", "" + keisan.getTime());
                        } else {
                            Log.v("TEST", "test");
                            keisan.setTime(date);
                            keisan.add(Calendar.WEEK_OF_MONTH, +1);
                            Log.v("test", "" + keisan.getTime());
                        }
                        //データベース格納用型変換
                        rTime = keisan.get(Calendar.MINUTE);  //Calendarからintへ
//                    String strKekka = String.valueOf(dateKekka);   //DateからStringへ
//                    kekka = Integer.parseInt(strKekka);     //Stringからintへ

                    } catch (ParseException e) {

                    }
                    Intent intent = new Intent(getApplicationContext(),
                            AlarmReceiver1shake.class);
                    Log.v("tag", "" + stopshake);
                    if (stopshake == false) {//アラーム停止シェイクorボタン
                        //明示的なBroadCast
                        intent = new Intent(getApplicationContext(),
                                com.example.alarmapp.receiver.AlarmRceiver2tap.class);
                    }
                    PendingIntent pending = PendingIntent.getBroadcast(
                            getApplicationContext(), alarmId, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                    // アラームをセットする
                    Calendar calendar = Calendar.getInstance();
                    //設定した時間-現在時刻
                    AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
                    if (am != null) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            am.setAlarmClock(new AlarmManager.AlarmClockInfo(keisan.getTimeInMillis(), null), pending);
                        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                            am.setExact(AlarmManager.RTC_WAKEUP, keisan.getTimeInMillis(), pending);//getTimeInMillis:calendarと現在時刻の差分取得
                        } else {
                            am.set(AlarmManager.RTC_WAKEUP, keisan.getTimeInMillis(), pending);
                        }
                        Toast.makeText(getApplicationContext(),
                                "Set Alarm ", Toast.LENGTH_SHORT).show();
                        //am.setRepeating(AlarmManager.RTC, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY * 7, pending);


                        //ここにデータベースにランダム時間をセットする。//
                    }
                } else if (Alm == false) {
                    try {

                        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                        Intent intent = new Intent(getApplicationContext(),
                                AlarmReceiver1shake.class);
                        if (stopshake == false) {//アラーム停止シェイクorボタン
                            //明示的なBroadCast
                            intent = new Intent(getApplicationContext(),
                                    com.example.alarmapp.receiver.AlarmRceiver2tap.class);
                        }
                        PendingIntent pending = PendingIntent.getBroadcast(getApplicationContext(), alarmId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                        pending.cancel();
                        alarmManager.cancel(pending);
                    } finally {

                    }
                }
                if (Ann == true) {

                    int[] aaa = new int[6];

                    for (int i = 0; i < aaa.length; i++) {
                        aaa[i] = -1;
                    }

                    int annInt = 0;
                    int annListInt = 0;
                    //"30分前", "20分前", "15分前", "10分前", "5分前", "設定時刻"
                    for (int i = 0; i < mAnnCheckedItems.length; i++) {
                        if (mAnnCheckedItems[i] == true) {

                                if (i == 0) {
                                    annInt = 30;
                                } else if (i == 1) {
                                    annInt = 20;
                                } else if (i == 2) {
                                    annInt = 15;
                                } else if (i == 3) {
                                    annInt = 10;
                                } else if (i == 4) {
                                    annInt = 5;
                                } else {
                                    annInt = 0;
                                }
                                aaa[i] = annInt;

                            }

                    }

                    for (int g = 0; g < aaa.length; g++) {
                        AnnID = alarmId * 10 + g;


                        Calendar calendar2 = Calendar.getInstance();
                        try {

                            SimpleDateFormat sdf2 = new SimpleDateFormat("MM,dd,hh:mm");//date型に変えるためのインスタンス
                            String strtime2 = setTAnnHour + ":" + setTAnnMinute;//intをstringに直す
                            Date date2 = sdf2.parse(strtime2);//ｓｔｒDateをdate型に変換


                            calendar2.setTime(date2);
                            calendar2.add(Calendar.MINUTE, -aaa[g]);
                            if (calendar2.getTimeInMillis() > 0) {
                                calendar2.setTime(date2);
                                calendar2.add(Calendar.MINUTE, -aaa[g]);//minuteには鳴る時間がはいってる。
                            } else {
                                calendar2.setTime(date2);
                                calendar2.add(Calendar.MINUTE, -aaa[g]);//minuteには鳴る時間がはいってる。
                                calendar2.add(Calendar.DAY_OF_WEEK_IN_MONTH, +7);
                            }
                        } catch (ParseException e) {

                        }

                        if (aaa[g] != -1) {
                            calendar2.setTimeInMillis(System.currentTimeMillis());
                            calendar2.add(Calendar.SECOND, Calendar.MINUTE);
                            scheduleNotification("アナウンス通知", calendar2);
                        }

                    }
                } else if (Ann == false) {
                                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

                                for (AnnDEL = 0; AnnDEL < 6; AnnDEL++) {
                                    AnnID = alarmId * 10 + AnnDEL;
                                    try {
                                        Intent intent = new Intent(getApplicationContext(), AnnReceiver.class);
                                        PendingIntent pending = PendingIntent.getBroadcast(getApplicationContext(), AnnID, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                                        pending.cancel();
                                        alarmManager.cancel(pending);

                                    } finally {

                                    }
                                }
                }


                //設定後メインに戻る
                Intent intentmain = new Intent(AlarmCreateActivity.this, MainActivity.class); //保存を押したらメインにもどる
                startActivity(intentmain);
            }

            private void scheduleNotification(String content, Calendar calendar) {
                Intent notificationIntent = new Intent(getApplicationContext(), AnnReceiver.class);
                notificationIntent.putExtra(AnnReceiver.NOTIFICATION_ID, AnnID);
                notificationIntent.putExtra(AnnReceiver.NOTIFICATION_CONTENT, content);
                PendingIntent pending2 = PendingIntent.getBroadcast(getApplicationContext(), AnnID, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
                if (am != null) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        am.setAlarmClock(new AlarmManager.AlarmClockInfo(calendar.getTimeInMillis(), null), pending2);
                    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        am.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pending2);
                    } else {
                        am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pending2);
                    }
                }
            }


        });
        AlmSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Alm = isChecked;
            }
        });
        AnnSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Ann = isChecked;
            }
        });

    }
    //削除メソッド

    public void delAlarm(View view) {
        Log.v("tapid",""+tapId);
        Log.v("alarmid",""+alarmId);
        if (tapId!= -1) {
            //以下データベース削除
            DatabaseHelper helper = new DatabaseHelper(AlarmCreateActivity.this);
            SQLiteDatabase db = helper.getWritableDatabase();
            DatabaseHelper.alarmDelete(tapId, db);

            //以下アラームのキャンセル
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            Intent almintent = new Intent(getApplicationContext(),
                    AlarmReceiver1shake.class);
            if (stopshake == false) {//アラーム停止シェイクorボタン
                //明示的なBroadCast
                almintent = new Intent(getApplicationContext(),
                        com.example.alarmapp.receiver.AlarmRceiver2tap.class);
            }
            PendingIntent almpending = PendingIntent.getBroadcast(getApplicationContext(), tapId, almintent, PendingIntent.FLAG_UPDATE_CURRENT);
            almpending.cancel();
            alarmManager.cancel(almpending);

            for (AnnDEL = 0; AnnDEL < 6; AnnDEL++) {
                AnnID = tapId * 10 + AnnDEL;
                try {
                    Intent intent = new Intent(getApplicationContext(), AnnReceiver.class);
                    PendingIntent pending = PendingIntent.getBroadcast(getApplicationContext(), AnnID, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                    pending.cancel();
                    alarmManager.cancel(pending);

                } finally {

                }
            }

            Intent intent2 = new Intent(AlarmCreateActivity.this, MainActivity.class); //保存を押したらメインにもどる
            startActivity(intent2);
        } else {
            Intent intent2 = new Intent(AlarmCreateActivity.this, MainActivity.class); //保存を押したらメインにもどる
            startActivity(intent2);
        }
    }

}

