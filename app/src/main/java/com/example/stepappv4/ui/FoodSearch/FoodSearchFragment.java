package com.example.stepappv4.ui.FoodSearch;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.stepappv4.R;
import com.example.stepappv4.database.CalorieAppOpenHelper;

import java.util.Collections;
import java.util.List;

public class FoodSearchFragment extends Fragment {

    private SearchView searchView;
    private RecyclerView recyclerView;
    private FoodAdapter foodAdapter;
    private ImageButton navigateButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_food_search, container, false);

        // 初始化组件
        searchView = view.findViewById(R.id.searchView);
        recyclerView = view.findViewById(R.id.leaderboardRv);
        navigateButton = view.findViewById(R.id.backButton2);

        // 设置 RecyclerView 和其适配器
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        foodAdapter = new FoodAdapter();
        recyclerView.setAdapter(foodAdapter);

        // 设置搜索视图的监听器
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchFood(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // 如果需要实时搜索，可以在这里实现
                return false;
            }
        });

        // 设置导航按钮的监听器
        navigateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToDayFragment();
            }
        });

        return view;
    }

    private void searchFood(String foodName) {
        CalorieAppOpenHelper dbHelper = new CalorieAppOpenHelper(getContext());
        List<String> results = dbHelper.searchFoodByName(foodName);
        foodAdapter.setFoodList(results);
    }

    private void navigateToDayFragment() {
        NavController navController = NavHostFragment.findNavController(this);
        navController.navigate(R.id.action_nav_food_search_to_nav_calorie);
    }

    // 适配器类
    private class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodViewHolder> {

        private List<String> foodList;

        @NonNull
        @Override
        public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.leaderboard_rv_item, parent, false);
            return new FoodViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull FoodAdapter.FoodViewHolder holder, int position) {
            String foodName = foodList.get(position);
            holder.foodNameTextView.setText(foodName);
            holder.rankTextView.setText(String.valueOf(position + 1)); // 设置序号
        }

        @Override
        public int getItemCount() {
            return foodList == null ? 0 : foodList.size();
        }

        public void setFoodList(List<String> foodList) {
            this.foodList = foodList;
            notifyDataSetChanged();
        }

        class FoodViewHolder extends RecyclerView.ViewHolder {
            TextView foodNameTextView;
            TextView rankTextView; // 添加用于显示序号的TextView

            FoodViewHolder(View itemView) {
                super(itemView);
                foodNameTextView = itemView.findViewById(R.id.foodName);
                rankTextView = itemView.findViewById(R.id.rank); // 确保您的item布局中有id为rank的TextView
            }
        }
    }
}
