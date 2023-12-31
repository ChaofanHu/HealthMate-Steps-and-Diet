package com.example.stepappv4.database;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
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

    public class FoodDetails {
        private String name;
        private double calories;
        private double amount;
        private String measureDescription;

        public FoodDetails(String name, double calories, double amount, String measureDescription) {
            this.name = name;
            this.calories = calories;
            this.amount = amount;
            this.measureDescription = measureDescription;
        }

        // Getters
        public String getName() { return name; }
        public double getCalories() { return calories; }
        public double getAmount() { return amount; }
        public String getMeasureDescription() { return measureDescription; }
    }

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

    public List<String> getFirstFewRows(int numberOfRows) {
        List<String> rows = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, null, String.valueOf(numberOfRows));
        if (cursor.moveToFirst()) {
            do {
                // 假设你想打印所有列
                StringBuilder row = new StringBuilder();
                for (int i = 0; i < cursor.getColumnCount(); i++) {
                    row.append(cursor.getString(i)).append(" | ");
                }
                rows.add(row.toString());
            } while (cursor.moveToNext());
        }
        cursor.close();
        return rows;
    }

    // 在 CalorieAppOpenHelper 类中添加
    public List<FoodDetails> searchFoodByName(String foodName) {
        List<FoodDetails> matchingFoods = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[]{COLUMN_DESC, COLUMN_CALORIES, COLUMN_AMOUNT, COLUMN_MSRE_DESC}, COLUMN_DESC + " LIKE ?", new String[]{"%" + foodName + "%"}, null, null, null);

        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex(COLUMN_DESC));
            double calories = cursor.getDouble(cursor.getColumnIndex(COLUMN_CALORIES));
            double amount = cursor.getDouble(cursor.getColumnIndex(COLUMN_AMOUNT));
            String measureDescription = cursor.getString(cursor.getColumnIndex(COLUMN_MSRE_DESC));

            matchingFoods.add(new FoodDetails(name, calories, amount, measureDescription));
        }
        cursor.close();
        return matchingFoods;
    }


}

