package com.example.stepappv4.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class FoodIntakeDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "FoodIntake.db";
    private static final int DATABASE_VERSION = 3;

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
                        "meal_type TEXT," +
                        "date TEXT" +
                        ");";

        db.execSQL(SQL_CREATE_FOOD_INTAKE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 这里处理数据库的升级
        // 仅在版本号增加时执行
        if (oldVersion < newVersion) {
            // 删除旧的表
            db.execSQL("DROP TABLE IF EXISTS food_intake");

            // 重新创建新的表
            onCreate(db);
        }
    }
    public boolean addFoodIntake(String foodName, double calories) {
        // 获取数据库实例
        SQLiteDatabase db = this.getWritableDatabase();
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = today.format(formatter);

        ContentValues values = new ContentValues();
        values.put("food_name", foodName);
        values.put("calories", calories);
        values.put("date", formattedDate);


        // 插入数据
        long result = db.insert("food_intake", null, values);
        Log.d("addFoodIntake: ",Long.toString(result));

        // 检查插入是否成功
        if (result == -1) {
            db.close();
            return false; // 插入失败
        } else {
            db.close();
            return true; // 插入成功
        }
    }

    public double getTotalCalories() {
        SQLiteDatabase db = this.getReadableDatabase();
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = today.format(formatter);
        double totalCalories = 0;

        final String SQL_QUERY_TOTAL = "SELECT SUM(calories) as Total FROM food_intake WHERE date = '" + formattedDate + "'";
        Cursor cursor = db.rawQuery(SQL_QUERY_TOTAL, null);

        // 检查Cursor是否有数据
        if (cursor != null && cursor.moveToFirst()) {
            totalCalories = cursor.getDouble(cursor.getColumnIndex("Total"));
        }

        if (cursor != null) {
            cursor.close();
        }
        db.close();

        return totalCalories;
    }
}
