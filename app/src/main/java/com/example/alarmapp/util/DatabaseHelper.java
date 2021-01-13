package com.example.alarmapp.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper { //アプリ初回起動時　データベースのテーブル作成
    //データベースファイルの定数フィールド
    private static final String DATABASE_NAME = "alarmList.db";
    //バージョン情報の定数フィールド。
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context){
        //親クラスのコンストラクタ呼び出し
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){    //同じ名前のデータベースが存在しない場合に実行される。
        // 以下に変更を加えた場合はテスト前にアプリの再インストールをしないと反映されない
        //テーブル作成用SQL文字列の作成。　"_id"の主キーがほぼ必須（？）
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE alarmList (");
        sb.append("_id INTEGER PRIMARY KEY,");      //アラームのidを主キーとする
        sb.append("tAlmHour INTEGER,");    //アラームの設定時間
        sb.append("tAlmMinute INTEGER,");
        sb.append("tAnnHour INTEGER,");    //出発の設定時間
        sb.append("tAnnMinute INTEGER");
//        sb.append("anTiming INTEGER,");      //アナウンスの設定時間
//        sb.append("alFlag INTEGER,");       //アラームのオンオフ用フラグ（アラーム編集画面から操作するもの）
//        sb.append("anFlag INTEGER,");       //アナウンスのオンオフ用フラグ（アラーム編集画面から操作するもの）
//        sb.append("settingFlag INTEGER,");      //アラームとアナウンスのセットのオンオフフラグ(スタート画面から見えるもの)
        sb.append(");");
        String sql = sb.toString();     //appendで結合された文字列をStringに
        db.execSQL(sql);        //SQLの実行。
        Log.v("実行されたSQL文",sql);     //Logcatに実行されたSQL文を表示


//        StringBuilder anSb = new StringBuilder();       //アナウンスのタイミングのデータベース
//        anSb.append("CRETE TABLE announceList(");
//        anSb.append("_id INTEGER PRIMARY KEY,");
//        anSb.append("timing INTEGER,");
//        anSb.append(");");
//        sql = anSb.toString();
//        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //特に処理はないけれど抽象メソッドなので必須

    }


}
