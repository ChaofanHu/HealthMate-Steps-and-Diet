package com.example.stepappv4;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.LinkedList;
import java.util.List;

public class CalorieAppOpenHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "CalorieApp.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_NAME = "FoodCalories";
    public static final String COLUMN_ID = "foodId";
    public static final String COLUMN_DESC = "foodDesc";
    public static final String COLUMN_CALORIES = "Calories";
    public static final String COLUMN_AMOUNT = "amount1";
    public static final String COLUMN_MSRE_DESC = "msreDesc1";

    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY, " +
                    COLUMN_DESC + " TEXT, " +
                    COLUMN_CALORIES + " REAL, " +
                    COLUMN_AMOUNT + " REAL, " +
                    COLUMN_MSRE_DESC + " TEXT);";

    public CalorieAppOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop the old table if it exists
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        // Create the new table
        onCreate(db);
    }

    public void printAllData() {
        System.out.println("111111111111");
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(
                TABLE_NAME,
                new String[]{COLUMN_ID, COLUMN_DESC, COLUMN_CALORIES, COLUMN_AMOUNT, COLUMN_MSRE_DESC},
                null,
                null,
                null,
                null,
                null
        );

        if (cursor.moveToFirst()) {
            do {
                // 根据列的数据类型读取数据
                @SuppressLint("Range") int foodId = cursor.getInt(cursor.getColumnIndex(COLUMN_ID)); // 整数类型
                @SuppressLint("Range") String foodDesc = cursor.getString(cursor.getColumnIndex(COLUMN_DESC)); // 字符串类型
                @SuppressLint("Range") double calories = cursor.getDouble(cursor.getColumnIndex(COLUMN_CALORIES)); // 实数类型
                @SuppressLint("Range") double amount1 = cursor.getDouble(cursor.getColumnIndex(COLUMN_AMOUNT)); // 实数类型
                @SuppressLint("Range") String msreDesc1 = cursor.getString(cursor.getColumnIndex(COLUMN_MSRE_DESC)); // 字符串类型

                // 打印数据
                System.out.println(("ID: " + foodId + ", Description: " + foodDesc +
                        ", Calories: " + calories + ", Amount: " + amount1 +
                        ", Measure Description: " + msreDesc1));
            } while (cursor.moveToNext());
        }
        cursor.close();
    }
}

