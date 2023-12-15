package com.example.stepappv4.ui.FoodSearch;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stepappv4.R;
import com.example.stepappv4.database.CalorieAppOpenHelper;
import com.example.stepappv4.database.FoodIntakeDbHelper;

import java.util.List;

public class FoodSearchFragment extends Fragment {

    private SearchView searchView;
    private RecyclerView recyclerView;
    private FoodAdapter foodAdapter;
    private ImageButton navigateButton;
    private FoodIntakeDbHelper dbHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        dbHelper = new FoodIntakeDbHelper(this.getContext());
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

        //after click the plus button, then one window show


        return view;
    }

    private void searchFood(String foodName) {
        CalorieAppOpenHelper dbHelper = new CalorieAppOpenHelper(getContext());
        List<CalorieAppOpenHelper.FoodDetails> results = dbHelper.searchFoodByName(foodName);
        foodAdapter.setFoodList(results);
    }

    private void navigateToDayFragment() {
        NavController navController = NavHostFragment.findNavController(this);
        navController.navigate(R.id.action_nav_food_search_to_nav_calorie);
    }
    private void showPopupDialog(CalorieAppOpenHelper.FoodDetails foodDetails) {
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_food_info, null);

        TextView tvFoodName = dialogView.findViewById(R.id.tvFoodName);
        TextView tvFoodUnit = dialogView.findViewById(R.id.tvFoodUnit);
        TextView tvCaloriePerUnit = dialogView.findViewById(R.id.tvCaloriePerUnit);
        EditText etQuantity = dialogView.findViewById(R.id.etQuantity);

        tvFoodName.setText("Food: " + foodDetails.getName());
        tvFoodUnit.setText("Calorie per Unit: " + foodDetails.getCalories() + " cal");
        tvCaloriePerUnit.setText("Unit: " + foodDetails.getAmount() + " " + foodDetails.getMeasureDescription());

        new AlertDialog.Builder(getContext())
                .setTitle("Food Information")
                .setView(dialogView)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String quantity = etQuantity.getText().toString();
                        // TODO: 使用食物名、单位、热量和数量进行相关操作
                        dbHelper.addFoodIntake(foodDetails.getName(),foodDetails.getCalories()*Double.parseDouble(quantity));
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }


    // 适配器类
    private class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodViewHolder> {

        private List<CalorieAppOpenHelper.FoodDetails> foodList;

        // 更新方法以接受 FoodDetails 列表
        public void setFoodList(List<CalorieAppOpenHelper.FoodDetails> foodList) {
            this.foodList = foodList;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.leaderboard_rv_item, parent, false);
            return new FoodViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull FoodAdapter.FoodViewHolder holder, int position) {
            CalorieAppOpenHelper.FoodDetails foodDetails = foodList.get(position);
            holder.foodNameTextView.setText(foodDetails.getName());
            holder.rankTextView.setText(String.valueOf(position + 1)); // 设置序号
            holder.plusButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 显示带有食品详细信息的弹窗
                    showPopupDialog(foodDetails);
                }
            });
        }

        @Override
        public int getItemCount() {
            return foodList == null ? 0 : foodList.size();
        }

        class FoodViewHolder extends RecyclerView.ViewHolder {
            TextView foodNameTextView;
            TextView rankTextView;
            ImageButton plusButton;

            FoodViewHolder(View itemView) {
                super(itemView);
                foodNameTextView = itemView.findViewById(R.id.foodName);
                rankTextView = itemView.findViewById(R.id.rank);
                plusButton = itemView.findViewById(R.id.addButtonForCalorie);
            }
        }
    }
}
