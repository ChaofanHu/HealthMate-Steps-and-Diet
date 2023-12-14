package com.example.stepappv4.ui.Search;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.stepappv4.R;

public class SearchViewFragment extends AppCompatActivity {
    private SearchView mSearchView = null;
    private ListView mListView = null;
    private String[] mDatas = {"aaa", "bbb", "ccc", "airsaid"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_food_search);

        mSearchView = (SearchView) findViewById(R.id.searchView);
        TextView searchText = mSearchView.findViewById(androidx.appcompat.R.id.search_src_text);

        // 设置输入文字的颜色为黑色
        searchText.setTextColor(getResources().getColor(R.color.black));

        mListView = (ListView) findViewById(R.id.listView);
        mListView.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, mDatas));
        mListView.setTextFilterEnabled(true);

        // 设置搜索文本监听
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            // 当点击搜索按钮时触发该方法            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            // 当搜索内容改变时触发该方法            @Override
            public boolean onQueryTextChange(String newText) {
                if (!TextUtils.isEmpty(newText)){
                    mListView.setFilterText(newText);
                }else{
                    mListView.clearTextFilter();
                }
                return false;
            }
        });
    }
}
