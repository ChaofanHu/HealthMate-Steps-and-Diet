package com.example.stepappv4.ui.Day;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.fragment.NavHostFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.core.cartesian.series.Column;
import com.anychart.enums.Anchor;
import com.anychart.enums.HoverMode;
import com.anychart.enums.Position;
import com.anychart.enums.TooltipPositionMode;
import com.example.stepappv4.database.StepAppOpenHelper;
import com.example.stepappv4.R;
import com.example.stepappv4.database.UserInfoDbHelper;
import com.google.android.material.card.MaterialCardView;

public class DayFragment extends Fragment {

    public int todaySteps = 0;
    TextView numStepsTextView;
    AnyChartView anyChartView;

    Date cDate = new Date();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat newSdf = new SimpleDateFormat("dd.MM.yyyy");
    String current_time = sdf.format(cDate);

    public Map<String, Integer> stepsByDay = null;
    private TextView ageTextView;
    private TextView weightTextView;
    private TextView genderTextView;
    private TextView heightTextView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_day, container, false);

        anyChartView = root.findViewById(R.id.dayBarChart);
        anyChartView.setProgressBar(root.findViewById(R.id.loadingBarDay));

        Cartesian cartesian = createColumnChart();
        anyChartView.setBackgroundColor("#00000000");
        anyChartView.setChart(cartesian);

        MaterialCardView materialCardView = root.findViewById(R.id.materialCardView3);
        MaterialCardView materialCardView2 = root.findViewById(R.id.rankCard);// 替换为实际的ID
        MaterialCardView materialCardView3 = root.findViewById(R.id.materialCardView4);
        MaterialCardView materialCardView4 = root.findViewById(R.id.materialCardView5);
        materialCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToAgeWeightFragment();
            }
        });
        materialCardView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToAgeWeightFragment();
            }
        });
        materialCardView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToAgeWeightFragment();
            }
        });
        materialCardView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToAgeWeightFragment();
            }
        });
        ageTextView = root.findViewById(R.id.ageTv);
        weightTextView = root.findViewById(R.id.rankingTv);
        genderTextView = root.findViewById(R.id.expTv);
        heightTextView=root.findViewById(R.id.weightTv);

        loadUserInfo();

        return root;
    }

    private void goToAgeWeightFragment() {
        NavController navController = NavHostFragment.findNavController(this);

        NavOptions navOptions = new NavOptions.Builder()
                .setPopUpTo(R.id.nav_day, true)
                .build();

        navController.navigate(R.id.action_nav_day_to_nav_age_weight, null, navOptions);
    }

    private void loadUserInfo() {
        UserInfoDbHelper dbHelper = new UserInfoDbHelper(getContext());
        Cursor userInfoCursor = dbHelper.getLatestUserInfo();

        if (userInfoCursor != null && userInfoCursor.moveToFirst()) {
            int age = userInfoCursor.getInt(userInfoCursor.getColumnIndex("age"));
            int weight = userInfoCursor.getInt(userInfoCursor.getColumnIndex("weight"));
            String gender = userInfoCursor.getString(userInfoCursor.getColumnIndex("gender"));
            int height = userInfoCursor.getInt(userInfoCursor.getColumnIndex("height"));

            // 确保TextView正确引用
            ageTextView.setText(String.valueOf(age));
            weightTextView.setText(weight + " kg");
            genderTextView.setText(gender);
            heightTextView.setText(height + " cm");

            userInfoCursor.close();
        }
    }

    public Cartesian createColumnChart() {
        stepsByDay = StepAppOpenHelper.loadStepsByDay(getContext());

        Map<String, Integer> graph_map = new TreeMap<>();

        for (int i = 0; i < 7; i++) {
            String day = sdf.format(new Date(cDate.getTime() - i * 24 * 60 * 60 * 1000));
            graph_map.put(day, 0);
        }

        if (stepsByDay != null) {
            for (String key : graph_map.keySet()) {
                if (stepsByDay.containsKey(key)) {
                    graph_map.put(key, stepsByDay.get(key));
                }
            }
        } else {
            Log.e("DayFragment", "stepsByDay is null! Unable to populate graph_map.");
        }

        Cartesian cartesian = AnyChart.column();

        List<DataEntry> data = new ArrayList<>();

        for (Map.Entry<String,Integer> entry : graph_map.entrySet()) {
            try {
                String formattedDate = newSdf.format(sdf.parse(entry.getKey()));
                data.add(new ValueDataEntry(formattedDate, entry.getValue()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        Column column = cartesian.column(data);

        column.fill("#1EB980");
        column.stroke("#1EB980");

        column.tooltip()
                .titleFormat("day: {%X}")
                .format("{%Value} steps")
                .anchor(Anchor.RIGHT_BOTTOM);

        column.tooltip()
                .position(Position.RIGHT_TOP).offsetX(0d)
                .offsetY(5);

        cartesian.tooltip().positionMode(TooltipPositionMode.POINT);
        cartesian.interactivity().hoverMode(HoverMode.BY_X);
        cartesian.yScale().minimum(0);

        int maxYValue = stepsByDay != null ? (stepsByDay.values().isEmpty() ? 100 : (int) Math.ceil(1.1 * stepsByDay.values().stream().max(Integer::compare).get())) : 100;
        cartesian.yScale().maximum(maxYValue);

        cartesian.yAxis(0).title("steps");
        cartesian.xAxis(0).title("day");
        cartesian.background().fill("#00000000");
        cartesian.animation(true);

        return cartesian;
    }

}