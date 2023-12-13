package com.example.stepappv4.ui.UserDetails;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.stepappv4.R;

public class UserDetailsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_age_weight, container, false);

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
}
