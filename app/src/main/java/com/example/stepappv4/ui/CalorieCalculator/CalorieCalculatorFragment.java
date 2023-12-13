package com.example.stepappv4.ui.CalorieCalculator;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.fragment.NavHostFragment;

import com.example.stepappv4.R;
import com.example.stepappv4.ui.FoodSearch.FoodSearchFragment;

public class CalorieCalculatorFragment extends Fragment {

    private ProgressBar progressBarRemaining;
    private ListView listViewMeals;
    private TextView textViewToday;

    public CalorieCalculatorFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_caloriecalculator, container, false);

        textViewToday = rootView.findViewById(R.id.textViewToday);

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


