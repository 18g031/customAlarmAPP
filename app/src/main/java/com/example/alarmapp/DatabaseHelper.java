package com.example.alarmapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
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
        //テーブル作成用SQL文字列の作成
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE alarmList (");
        sb.append("_id INTEGER PRIMARY KEY,");      //アラームのidを主キーとする
        sb.append("tAlmHour INTEGER,");    //アラームの設定時間
        sb.append("tAlmMinute INTEGER,");
        sb.append("tAnnHour INTEGER,");    //アナウンスの設定時間
        sb.append("tAnnMinute INTEGER");
        sb.append(");");
        String sql = sb.toString();     //appendで結合された文字列をStringに

        db.execSQL(sql);        //SQLの実行。
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //特に処理はないけれど抽象メソッドなので必要

    }


}
