package com.example.stepappv4.ui.UserDetails;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.stepappv4.R;

public class UserDetailsFragment extends Fragment {

    private TextView userAgeTextView;
    private SeekBar ageSeekBar;
    private TextView userWeightTextView;
    private SeekBar weightSeekBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_age_weight, container, false);

        userAgeTextView = view.findViewById(R.id.userAge);
        ageSeekBar = view.findViewById(R.id.simpleSeekBarForAge);
        userWeightTextView = view.findViewById(R.id.userWeight);
        weightSeekBar = view.findViewById(R.id.simpleSeekBarForWeight);

        // 设置年龄SeekBar的监听器
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

        // 设置体重SeekBar的监听器
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

        initializeSeekBar();

        // 假设您有一个按钮用于触发导航
        ImageButton navigateButton = view.findViewById(R.id.backButton);
        navigateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToDayFragment();
            }
        });

        return view;
    }

    private void navigateToDayFragment() {
        NavController navController = NavHostFragment.findNavController(this);
        navController.navigate(R.id.action_nav_age_weight_to_nav_day);
    }

    private void initializeSeekBar() {
        // 假设初始年龄为25岁
        int initialAge = 25;
        ageSeekBar.setMax(100); // 最大年龄100
        ageSeekBar.setProgress(initialAge); // 设置初始进度
        userAgeTextView.setText(initialAge + " yrs");

        // 假设初始体重为65kg
        int initialWeight = 65;
        weightSeekBar.setMax(300); // 最大体重300
        weightSeekBar.setProgress(initialWeight); // 设置初始进度
        userWeightTextView.setText(initialWeight + " kg");
    }
}


