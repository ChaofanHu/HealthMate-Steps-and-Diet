package com.example.stepappv4.ui.CalorieCalculator;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.fragment.NavHostFragment;

import com.example.stepappv4.R;
import com.example.stepappv4.database.FoodIntakeDbHelper;
import com.example.stepappv4.database.UserInfoDbHelper;
import com.skydoves.progressview.ProgressView;

import java.util.Timer;
import java.util.TimerTask;

public class CalorieCalculatorFragment extends Fragment {

    private ProgressView progressBarRemaining;
    private ListView listViewMeals;
    private TextView textViewToday;
    private TextView goalText;
    private TextView calorieIntake;
    private FoodIntakeDbHelper foodIntakeDbHelper;

    public CalorieCalculatorFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_caloriecalculator, container, false);

        textViewToday = rootView.findViewById(R.id.textViewToday);
        calorieIntake = rootView.findViewById(R.id.calorie_intake_in_calorie_view);
        //wrong
        progressBarRemaining = rootView.findViewById(R.id.progress);
        foodIntakeDbHelper = new FoodIntakeDbHelper(this.getContext());
        UserInfoDbHelper userInfoDbHelper = new UserInfoDbHelper(this.getContext());
        goalText = rootView.findViewById(R.id.goal_calorie);
        Cursor latestUserInfo = userInfoDbHelper.getLatestUserInfo();
//         检查 Cursor 是否包含数据
        if (latestUserInfo != null && latestUserInfo.moveToFirst()) {
            // 从 Cursor 中读取数据
            // 假设 user_info 表有 "name" 和 "age" 列
            String name = latestUserInfo.getString(latestUserInfo.getColumnIndex("name"));
            int age = latestUserInfo.getInt(latestUserInfo.getColumnIndex("age"));
            String gender = latestUserInfo.getString(latestUserInfo.getColumnIndex("gender"));
            double weight = latestUserInfo.getDouble(latestUserInfo.getColumnIndex("weight"));
            double height = latestUserInfo.getDouble(latestUserInfo.getColumnIndex("height"));
               if ("Male".equals(gender)){
                   goalText.setText("/  "+ Double.toString(10*weight + 6.25*height - 5*age + 5));
                   progressBarRemaining.setMax((int)(10*weight + 6.25*height - 5*age + 5));
               }else {
                   goalText.setText("/  "+ Double.toString(10*weight + 6.25*height - 5*age - 161));
                   progressBarRemaining.setMax((int)(10*weight + 6.25*height - 5*age - 161));
               }
        }

        Timer timer1 = new Timer();
        timer1.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                FragmentActivity activity = getActivity();
                if (activity != null) {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(isAdded()) { // 进一步检查 Fragment 是否附加
                                calorieIntake.setText(Double.toString(foodIntakeDbHelper.getTotalCalories()));
                                progressBarRemaining.setProgress((int) foodIntakeDbHelper.getTotalCalories());
                            }
                        }
                    });
                }
            }
        }, 0, 1000); // 调整间隔以减少性能压力


        // Set up your ListView adapter here
        // listViewMeals.setAdapter(new YourCustomAdapter());

        // 配置ImageButton的点击事件
        ImageButton imageButton = rootView.findViewById(R.id.addButton);
        ImageButton imageButton2 = rootView.findViewById(R.id.addButton2);
        ImageButton imageButton3 = rootView.findViewById(R.id.addButton3);
        ImageButton imageButton4 = rootView.findViewById(R.id.addButton4);
        // 替换为您的ImageButton的ID
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToFoodSearchFragment();
            }
        });
        imageButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToFoodSearchFragment();
            }
        });
        imageButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToFoodSearchFragment();
            }
        });
        imageButton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToFoodSearchFragment();
            }
        });

        return rootView;
    }

    private void goToFoodSearchFragment() {
        NavController navController = NavHostFragment.findNavController(this);

        // 设置导航选项，以便在导航到 FoodSearchFragment 后从回退栈中移除 CalorieCalculatorFragment
        NavOptions navOptions = new NavOptions.Builder()
                .setPopUpTo(R.id.nav_calorie, true) // 移除 CalorieCalculatorFragment
                .build();

        navController.navigate(R.id.action_nav_calorie_to_nav_food_search, null, navOptions);
    }
    // Add other methods to interact with ProgressBar, ListView, etc.
}


