package com.example.stepappv4;

import android.content.Context;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.stepappv4.database.CalorieAppOpenHelper;
import com.example.stepappv4.database.FoodDataImporter;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

@RunWith(AndroidJUnit4.class)
public class DatabaseTest {

    @Test
    public void testImportAndPrintData() {
        // 上下文
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

        // 实例化数据库帮助类
        CalorieAppOpenHelper dbHelper = new CalorieAppOpenHelper(appContext);

        // 实例化数据导入类
        FoodDataImporter importer = new FoodDataImporter(dbHelper);

        // 导入数据
        importer.importDataFromExcel(appContext, "MyNetDiary_Food_Samples.xls");

        // 获取并打印前几行数据
        List<String> rows = dbHelper.getFirstFewRows(5);
        for (String row : rows) {
            System.out.println(row);
        }
    }
}
