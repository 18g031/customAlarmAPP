package com.example.alarmapp.util;

//アラームに使うリスト作成
//実際に今あるのは削除用メソッドとアラーム追加用メソッドとリスト作成メソッド未使用の変数

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class AlarmList{

//    public int alarmID = -1;           //アラーム識別用ID
//    private  String alarmTime = "null";     //アラーム設定時間
//    private String announceTime = "null";       //アナウンス設定時間
//    private int settingFlag = 1;        //アラームとアナウンスのセットのオンオフフラグ(スタート画面から見えるもの)
//    private int alarmFlag = 1;          //アラームのみのオンオフフラグ（アラーム編集画面から操作するもの）
//    private int announceFlag = 1;       //アナウンスのみのオンオフフラグ（アラーム編集画面から操作するもの）


    public static void alarmDelete(int listPosition ,SQLiteDatabase db){
        //データベースヘルパーオブジェクトの作成はメソッドの呼び出し元であらかじめ行う
//        DatabaseHelper helper = new DatabaseHelper(AlarmCreateActivity.context);
//        SQLiteDatabase db = helper.getWritableDatabase();
        ArrayList<Integer> idArray = new ArrayList<>();      //取得した_idを格納するリスト
        try{
            //MainActivityでタップされたpositionから削除したい_idを取得する
            String sql = "SELECT * FROM alarmList";
            Cursor cursor = db.rawQuery(sql, null);
            while(cursor.moveToNext()){
                int idx_id = cursor.getColumnIndex("_id");
                int alId = cursor.getInt(idx_id);
                idArray.add(alId);
            }
            int delId = idArray.get(listPosition);      //タップされたリストの_idを格納
            String sqlDelete = "DELETE FROM alarmList WHERE _id = ?";
            SQLiteStatement stmt = db.compileStatement(sqlDelete);
            stmt.bindLong(1, delId);
            stmt.executeUpdateDelete();     //削除SQL文の実行
        } finally {
            //データベースは明示的にcloseしない方がよい？　→　http://hobby.txt-nifty.com/t1000/2010/11/sqliteandroid-f.html
            //db.close();
        }
    }

    //AlarmCreateActivityでenterを押下したとき
    public static void alarmAdd(int tAlmHour, int tAlmMinute, int tAnnHour, int tAnnMinute, SQLiteDatabase db) {
        //データベースヘルパーオブジェクトの作成はメソッドの呼び出し元であらかじめ行う
        //DatabaseHelper helper = new DatabaseHelper(AlarmCreateActivity.context);
        //SQLiteDatabase db = helper.getWritableDatabase();
        try{
            Log.v("try","try の先頭を実行");
            //保存されている最大の_idを取得するSQL文
            String sql = "SELECT * FROM alarmList";
            Cursor cursor = db.rawQuery(sql, null);//SQL文を実行して結果をcursorに格納
            int alarmId = -1;
            String str ;
            while(cursor.moveToNext()){
                int idxId = cursor.getColumnIndex("_id");
                str = cursor.getString(idxId);
                alarmId = Integer.parseInt(str);
                Log.v("try",""+alarmId);
            }
            alarmId +=1;
            //保存するためのＳＱＬ。変数によって値が変わる場所は？にする
            String sqlInsert = "INSERT INTO alarmList (_id, tAlmHour, tAlmMinute, tAnnHour, tAnnMinute) VALUES (?, ?, ?, ?, ?)";
            SQLiteStatement stmt = db.compileStatement(sqlInsert);  //プリペアドステートメントを取得
            stmt.bindLong(1,alarmId);       //alarmListの1つ目のVALUESにalarmIdを入れる
            stmt.bindLong(2,tAlmHour);
            stmt.bindLong(3,tAlmMinute);
            stmt.bindLong(4,tAnnHour);
            stmt.bindLong(5,tAnnMinute);
            stmt.executeInsert();       //SQL文を実行（データベースに保存）
        }
        finally {
            // db.close();
        }
    }


    //MainActivityを表示したとき実行
    public static List<String> createList(SQLiteDatabase db) {
        List<String> alarmArray= new ArrayList<>();
        try{
            //データベースから読み込むためのＳＱＬ。
            String sql = "SELECT * FROM alarmList";
            Cursor cursor = db.rawQuery(sql, null);
            while(cursor.moveToNext()){
                int idxAlTH = cursor.getColumnIndex("tAlmHour");
                int idxAlTM = cursor.getColumnIndex("tAlmMinute");
                int idxAnTH = cursor.getColumnIndex("tAnnHour");
                int idxAnTM = cursor.getColumnIndex("tAnnMinute");
                int alTH= cursor.getInt(idxAlTH);
                int alTM= cursor.getInt(idxAlTM);
                int anTH= cursor.getInt(idxAnTH);
                int anTM= cursor.getInt(idxAnTM);
                String alT = String.format("%02d",alTH )+ ":" + String.format("%02d",alTM);     //それぞれの時間を0埋めして
                String anT = String.format("%02d",anTH) + ":" + String.format("%02d",anTM);     //hh:mmの形でString型に格納
                alarmArray.add(alT+"　　　"+anT);
            }
        } finally {
            //データベースは明示的にcloseしない方がよい？　→　http://hobby.txt-nifty.com/t1000/2010/11/sqliteandroid-f.html
            //db.close();
        }
        return alarmArray;
    }
}
