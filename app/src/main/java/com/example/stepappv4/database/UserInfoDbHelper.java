package com.example.stepappv4.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class UserInfoDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "UserInfo.db";
    private static final int DATABASE_VERSION = 2;

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
                        "height REAL NOT NULL," + // 新增身高字段
                        "gender TEXT NOT NULL," +  // 新增性别字段
                        "age REAL NOT NULL" +
                        ");";

        db.execSQL(SQL_CREATE_USER_INFO_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 检查旧版本号和新版本号，根据需要执行数据库升级
        if (oldVersion < 2) {
            // 添加新列
            db.execSQL("ALTER TABLE user_info ADD COLUMN gender TEXT NOT NULL DEFAULT 'Unknown'");
            db.execSQL("ALTER TABLE user_info ADD COLUMN height REAL NOT NULL DEFAULT 0");
        }
        // 如果有更多版本的升级，可以继续添加 if 语句处理
    }


    public void insertUser(String name, int weight, int age, String gender, int height) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("weight", weight);
        values.put("age", age);
        values.put("gender", gender);  // 添加性别
        values.put("height", height);  // 添加身高

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
