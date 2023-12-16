package com.example.stepappv4.ui.UserDetails;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import com.example.stepappv4.R;
import com.example.stepappv4.database.UserInfoDbHelper;

public class UserDetailsFragment extends Fragment {

    private TextView userAgeTextView;
    private SeekBar ageSeekBar;
    private TextView userWeightTextView;
    private SeekBar weightSeekBar;
    private TextView userHeightTextView;
    private SeekBar heightSeekBar;
    private TextView userGenderTextView;
    private RadioButton radioMale;
    private RadioButton radioFemale;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_age_weight, container, false);

        userAgeTextView = view.findViewById(R.id.userAge);
        ageSeekBar = view.findViewById(R.id.simpleSeekBarForAge);
        userWeightTextView = view.findViewById(R.id.userWeight);
        weightSeekBar = view.findViewById(R.id.simpleSeekBarForWeight);
        userHeightTextView = view.findViewById(R.id.userHeight);
        heightSeekBar = view.findViewById(R.id.simpleSeekBarForHeight);
        radioMale = view.findViewById(R.id.radio_male);
        radioFemale = view.findViewById(R.id.radio_female);

        ImageButton navigateButton = view.findViewById(R.id.backButton);
        AppCompatButton continueButton = view.findViewById(R.id.continueBtn);

        setSeekBarListeners();
        initializeSeekBar();

        navigateButton.setOnClickListener(v -> navigateToDayFragment());
        continueButton.setOnClickListener(v -> {
            saveUserInfo();
            navigateToDayFragment();
        });

        return view;
    }

    private void saveUserInfo() {
        int age = ageSeekBar.getProgress();
        int weight = weightSeekBar.getProgress();
        int height = heightSeekBar.getProgress(); // 获取身高
        String gender = radioMale.isChecked() ? "Male" : "Female"; // 获取性别

        UserInfoDbHelper dbHelper = new UserInfoDbHelper(getContext());
        dbHelper.insertUser("User", weight, age, gender, height); // 保存用户信息
    }

    private void navigateToDayFragment() {
        NavController navController = NavHostFragment.findNavController(this);
        navController.navigate(R.id.action_nav_age_weight_to_nav_day);
    }

    private void setSeekBarListeners() {
        ageSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                userAgeTextView.setText(progress + " yrs");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // 不需要实现
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // 不需要实现
            }
        });

        weightSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                userWeightTextView.setText(progress + " kg");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // 不需要实现
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // 不需要实现
            }
        });

        heightSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                userHeightTextView.setText(progress + " cm");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // 不需要实现
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // 不需要实现
            }
        });
    }

    private void initializeSeekBar() {
        int initialAge = 25; // 假设初始年龄为25岁
        ageSeekBar.setMax(100);
        ageSeekBar.setProgress(initialAge);
        userAgeTextView.setText(initialAge + " yrs");

        int initialWeight = 65; // 假设初始体重为65kg
        weightSeekBar.setMax(300);
        weightSeekBar.setProgress(initialWeight);
        userWeightTextView.setText(initialWeight + " kg");

        // 初始化性别单选按钮，默认选中“男性”
        radioMale.setChecked(true);

        // 初始化身高滑块
        int initialHeight = 175; // 假设初始身高为170cm
        heightSeekBar.setMax(235);
        heightSeekBar.setProgress(initialHeight);
        userHeightTextView.setText(initialHeight + " cm");
    }
}
