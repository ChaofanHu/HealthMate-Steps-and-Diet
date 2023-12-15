package com.example.stepappv4.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class UserInfoDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "UserInfo.db";
    private static final int DATABASE_VERSION = 1;

    public UserInfoDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_USER_INFO_TABLE =
                "CREATE TABLE user_info (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "name TEXT NOT NULL," +
                        "weight REAL NOT NULL," +
                        "age REAL NOT NULL" +
                        ");";

        db.execSQL(SQL_CREATE_USER_INFO_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 此处用于处理数据库的升级，暂时留空
    }

    public void insertUser(String name, int weight, int age) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("weight", weight);
        values.put("age", age);

        db.insert("user_info", null, values);
    }

    public Cursor getAllUsers() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query("user_info", null, null, null, null, null, null);
    }

    public Cursor getLatestUserInfo() {
        SQLiteDatabase db = this.getReadableDatabase();
        // 查询最新的记录，假设"id"是自增主键
        String query = "SELECT * FROM user_info ORDER BY id DESC LIMIT 1";
        return db.rawQuery(query, null);
    }
}
