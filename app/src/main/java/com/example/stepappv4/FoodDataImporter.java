package com.example.stepappv4;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.util.Iterator;

public class FoodDataImporter {

    // 假设你的SQLiteOpenHelper类叫做DatabaseHelper
    private SQLiteOpenHelper dbHelper;

    public FoodDataImporter(SQLiteOpenHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    public void importDataFromExcel(String filePath) {
        try {
            FileInputStream file = new FileInputStream(new File(filePath));

            // 创建工作簿
            Workbook workbook = new XSSFWorkbook(file);
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();

            // 跳过标题行
            if (rowIterator.hasNext()) {
                rowIterator.next();
            }

            // 遍历行
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                String foodId = getCellData(row, 1);     // B列，foodId
                String foodDesc = getCellData(row, 4);   // E列，foodDesc
                String calories = getCellData(row, 8);   // I列，Calories
                String amount1 = getCellData(row, 53);   // BB列，amount1
                String msreDesc1 = getCellData(row, 54); // BC列，msreDesc1

                // 保存到数据库
                saveToDatabase(foodId, foodDesc, calories, amount1, msreDesc1);
            }

            file.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getCellData(Row row, int cellIndex) {
        Cell cell = row.getCell(cellIndex);
        return cell != null ? cell.toString() : "";
    }

    private void saveToDatabase(String foodId, String foodDesc, String calories, String amount1, String msreDesc1) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("foodId", foodId);
        values.put("foodDesc", foodDesc);
        values.put("Calories", calories);
        values.put("amount1", amount1);
        values.put("msreDesc1", msreDesc1);

        db.insert("FoodCalories", null, values); // 假设表名为 FoodTable
    }

}

