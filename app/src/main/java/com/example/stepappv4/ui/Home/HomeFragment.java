package com.example.stepappv4.ui.Home;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.stepappv4.R;
import com.example.stepappv4.database.FoodIntakeDbHelper;
import com.example.stepappv4.database.StepAppOpenHelper;
import com.example.stepappv4.database.UserInfoDbHelper;
import com.example.stepappv4.databinding.FragmentHomeBinding;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.progressindicator.CircularProgressIndicator;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    private TextView stepCountsView;
    private TextView calorieBurned;
    private CircularProgressIndicator progressBar;

    private MaterialButtonToggleGroup toggleButtonGroup;

    private Sensor accSensor;

    private SensorManager sensorManager;

    private StepCounterListener sensorListener;

    private Sensor stepDetectorSensor;
    private FoodIntakeDbHelper foodIntakeDbHelper;
    private  TextView calorieIntake;

    private TextView distanceTravelled;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private TextView goalText;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        foodIntakeDbHelper = new FoodIntakeDbHelper(this.getContext());

        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        stepCountsView = (TextView) root.findViewById(R.id.counter);
        stepCountsView.setText(Integer.toString(StepAppOpenHelper.getTotalRecordCount(this.getContext())));

        calorieBurned= (TextView) root.findViewById(R.id.text_calorie_burned);
        calorieBurned.setText("0 cal");

        calorieIntake = (TextView) root.findViewById(R.id.calorie_intake_text);


        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                // 由于这是在非UI线程执行，因此需要切换到UI线程来更新UI元素
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        calorieIntake.setText(foodIntakeDbHelper.getTotalCalories() + " cal");
                    }
                });
            }
        }, 0, 100);
        progressBar = (CircularProgressIndicator) root.findViewById(R.id.progressBar);
        progressBar.setMax(5000);
        progressBar.setProgress(StepAppOpenHelper.getTotalRecordCount(this.getContext()));


        UserInfoDbHelper userInfoDbHelper = new UserInfoDbHelper(this.getContext());
        Cursor latestUserInfo = userInfoDbHelper.getLatestUserInfo();
        // 检查 Cursor 是否包含数据
        if (latestUserInfo != null && latestUserInfo.moveToFirst()) {
            // 从 Cursor 中读取数据
            // 假设 user_info 表有 "name" 和 "age" 列
            String name = latestUserInfo.getString(latestUserInfo.getColumnIndex("name"));
            int age = latestUserInfo.getInt(latestUserInfo.getColumnIndex("age"));
            String gender = latestUserInfo.getString(latestUserInfo.getColumnIndex("gender"));
            double weight = latestUserInfo.getDouble(latestUserInfo.getColumnIndex("weight"));
            // 使用数据，例如显示在界面上或进行其他处理
            Timer timer1 = new Timer();
            timer1.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            // 假设 progressBar 已经某种方式更新了它的进度
                            double distance;
                            double calorieBurnedValue;
                            if ("Male".equals(gender)) {
                                distance = (1000l / 700l) * progressBar.getProgress();
                                Log.d("测试", "run: " + distance);
                            } else { // 假设只有男性和女性，简化逻辑
                                distance = (1000l / 1500l) * progressBar.getProgress();
                            }
                            calorieBurnedValue = distance * weight * 1.036;
                            calorieBurned.setText(calorieBurnedValue + " cal");
                        }
                    });
                }
            }, 0, 1000); // 调整间隔以减少性能压力

            // 最后关闭 Cursor
            latestUserInfo.close();
        } else {
            // Cursor 为空或没有数据
            // 处理空或无数据的情况
            // ...
        }





        goalText = (TextView) root.findViewById(R.id.goal);
        goalText.setText("Goal: " + Integer.toString(5000));
        progressBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSetGoalDialog();
            }
        });



        sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        accSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);

        stepDetectorSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);

        StepAppOpenHelper databaseOpenHelper = new StepAppOpenHelper(this.getContext());
        SQLiteDatabase database = databaseOpenHelper.getWritableDatabase();
        if (accSensor != null)
        {
            sensorListener = new StepCounterListener(stepCountsView, progressBar, database);
            sensorManager.registerListener(sensorListener, accSensor, SensorManager.SENSOR_DELAY_NORMAL);

        }
        else
        {
            Toast.makeText(getContext(), R.string.acc_sensor_not_available, Toast.LENGTH_LONG).show();
        }

        distanceTravelled = (TextView) root.findViewById(R.id.text_distance_travelled);

        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                updateCityName(location);
            }

            // Implement other LocationListener methods
        };

        // Request location updates
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }

        return root;
    }

    private void showSetGoalDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
        builder.setTitle("Set Your Goal");

        // 设置对话框内容，例如一个输入框
        final EditText input = new EditText(this.getContext());
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        input.setTextColor(Color.BLACK);
        builder.setView(input);

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                try {
                    final int goalSteps = Integer.parseInt(input.getText().toString());

                    // 确保在UI线程上执行UI更新
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setMax(goalSteps);
                            goalText.setText("Goal: " + Integer.toString(goalSteps));
                        }
                    });
                } catch (NumberFormatException e) {
                    // 处理错误的输入
                    Toast.makeText(getContext(), "Please Input the correct Number!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }



    private void updateCityName(Location location) {
        try {
            Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            if (addresses.size() > 0) {
                Address address = addresses.get(0);
                String locality = address.getLocality();       // 获取城市
                String country = address.getCountryName();     // 获取国家

                // 创建一个字符串来显示所有这些信息
                String addressText = (locality != null ? locality + ", " : "") +
                        (country != null ? country : "");

                distanceTravelled.setText(addressText);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (locationManager != null && locationListener != null) {
            locationManager.removeUpdates(locationListener);
        }
        binding = null;
    }
}

class  StepCounterListener implements SensorEventListener{

    private long lastSensorUpdate = 0;
    public static int accStepCounter = 0;
    ArrayList<Integer> accSeries = new ArrayList<Integer>();
    ArrayList<String> timestampsSeries = new ArrayList<String>();
    private double accMag = 0;
    private int lastAddedIndex = 1;
    int stepThreshold = 6;

    TextView stepCountsView;

    CircularProgressIndicator progressBar;
    private SQLiteDatabase database;

    private String timestamp;
    private String day;
    private String hour;


    public StepCounterListener(TextView stepCountsView, CircularProgressIndicator progressBar,  SQLiteDatabase databse)
    {
        this.stepCountsView = stepCountsView;
        this.database = databse;
        this.progressBar = progressBar;
        // 获取数据库中保存的步数
        int initialStepCount = StepAppOpenHelper.getTotalRecordCount(progressBar.getContext());

        // 使用获取的步数初始化计步器
        accStepCounter = initialStepCount;

        // 更新 UI 显示
        stepCountsView.setText(String.valueOf(accStepCounter));
        progressBar.setProgress(accStepCounter);
    }


    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        switch (sensorEvent.sensor.getType())
        {
            case Sensor.TYPE_LINEAR_ACCELERATION:

                float x = sensorEvent.values[0];
                float y = sensorEvent.values[1];
                float z = sensorEvent.values[2];

                long currentTimeInMilliSecond = System.currentTimeMillis();

                long timeInMillis = currentTimeInMilliSecond + (sensorEvent.timestamp - SystemClock.elapsedRealtimeNanos()) / 1000000;

                // Convert the timestamp to date
                SimpleDateFormat jdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
                jdf.setTimeZone(TimeZone.getTimeZone("GMT+2"));
                String sensorEventDate = jdf.format(timeInMillis);


                if ((currentTimeInMilliSecond - lastSensorUpdate) > 1000)
                {
                    lastSensorUpdate = currentTimeInMilliSecond;
                    String sensorRawValues = "  x = "+ String.valueOf(x) +"  y = "+ String.valueOf(y) +"  z = "+ String.valueOf(z);
//                    Log.d("Acc. Event", "last sensor update at " + String.valueOf(sensorEventDate) + sensorRawValues);
                }


                accMag = Math.sqrt(x*x+y*y+z*z);


                accSeries.add((int) accMag);

                // Get the date, the day and the hour
                timestamp = sensorEventDate;
                day = sensorEventDate.substring(0,10);
                hour = sensorEventDate.substring(11,13);

//                Log.d("SensorEventTimestampInMilliSecond", timestamp);


                timestampsSeries.add(timestamp);
                peakDetection();

                break;

        }


    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    private void peakDetection() {

        int windowSize = 20;
        /* Peak detection algorithm derived from: A Step Counter Service for Java-Enabled Devices Using a Built-In Accelerometer Mladenov et al.
         */
        int currentSize = accSeries.size(); // get the length of the series
        if (currentSize - lastAddedIndex < windowSize) { // if the segment is smaller than the processing window size skip it
            return;
        }

        List<Integer> valuesInWindow = accSeries.subList(lastAddedIndex,currentSize);
        List<String> timePointList = timestampsSeries.subList(lastAddedIndex,currentSize);
        lastAddedIndex = currentSize;

        for (int i = 1; i < valuesInWindow.size()-1; i++) {
            int forwardSlope = valuesInWindow.get(i + 1) - valuesInWindow.get(i);
            int downwardSlope = valuesInWindow.get(i) - valuesInWindow.get(i - 1);

            if (forwardSlope < 0 && downwardSlope > 0 && valuesInWindow.get(i) > stepThreshold) {
                accStepCounter += 1;
//                Log.d("ACC STEPS: ", String.valueOf(accStepCounter));
                stepCountsView.setText(String.valueOf(accStepCounter));
                progressBar.setProgress(accStepCounter);

                ContentValues databaseEntry = new ContentValues();
                databaseEntry.put(StepAppOpenHelper.KEY_TIMESTAMP, timePointList.get(i));

                databaseEntry.put(StepAppOpenHelper.KEY_DAY, this.day);
                databaseEntry.put(StepAppOpenHelper.KEY_HOUR, this.hour);

                database.insert(StepAppOpenHelper.TABLE_NAME, null, databaseEntry);

            }
        }
    }


}