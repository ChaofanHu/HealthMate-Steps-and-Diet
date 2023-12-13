package com.example.stepappv4.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class FoodIntakeDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "FoodIntake.db";
    private static final int DATABASE_VERSION = 1;

    public FoodIntakeDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_FOOD_INTAKE_TABLE =
                "CREATE TABLE food_intake (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "food_name TEXT NOT NULL," +
                        "calories REAL NOT NULL," +
                        "unit TEXT NOT NULL," +
                        "meal_type TEXT NOT NULL" +
                        ");";

        db.execSQL(SQL_CREATE_FOOD_INTAKE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 这里处理数据库的升级
    }
}
