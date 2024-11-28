package com.example.topcv;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.topcv.adapter.WorkAdapter;
import com.example.topcv.adapter.WorkSearchAdapter;
import com.example.topcv.api.ApiJobService;
import com.example.topcv.database.SearchHistoryDatabaseHelper;
import com.example.topcv.model.Job;
import java.util.List;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SearchActivity extends AppCompatActivity {

    private ImageButton back_button;
    private ImageButton search_button;

    private EditText search_edit_text;

    private RecyclerView recycler_view_search;

    private WorkSearchAdapter workSearchAdapter;
    private WorkAdapter workAdapter;

    private SearchHistoryDatabaseHelper dbHelper;

    private TextView recent_search_textview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        setWidget();

        setClick();

        setupRecyclerView();

        showRecentKeywords();

    }

    private void searchButton(){
        String query = search_edit_text.getText().toString().trim();
        if (!query.isEmpty()) {
            dbHelper.addKeyword(query);
            Intent intent = new Intent(SearchActivity.this, SearchListActivity.class);
            intent.putExtra("search_query", query);
            startActivity(intent);
        }
    }

    private void setClick(){
        back_button.setOnClickListener(view -> finish());

        search_button.setOnClickListener(view -> searchButton());

        search_edit_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String query = charSequence.toString().trim();

                if (query.isEmpty()) {
                    showRecentKeywords();
                    recent_search_textview.setVisibility(View.VISIBLE);
                } else {
                    searchJobs(query);
                    recent_search_textview.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });
    }

    private void setWidget() {
        search_edit_text = findViewById(R.id.search_edit_text);
        back_button = findViewById(R.id.back_button);
        recycler_view_search = findViewById(R.id.recycler_view_search);
        recent_search_textview = findViewById(R.id.recent_search_textview);
        search_button = findViewById(R.id.search_button);
        dbHelper = new SearchHistoryDatabaseHelper(this);
    }

    private void setupRecyclerView() {
        workSearchAdapter = new WorkSearchAdapter();
        workAdapter = new WorkAdapter();
        recycler_view_search.setLayoutManager(new LinearLayoutManager(this));
        workSearchAdapter.setOnKeywordClickListener(keyword -> {
            Intent intent = new Intent(SearchActivity.this, SearchListActivity.class);
            intent.putExtra("keyword", keyword);
            startActivity(intent);
        });
    }

    private void showRecentKeywords() {
        List<String> recentKeywords = dbHelper.getRecentKeywords();
        if (!recentKeywords.isEmpty()) {
            recycler_view_search.setAdapter(workSearchAdapter);
            workSearchAdapter.setKeywords(recentKeywords);
            fetchJobsAndUpdateKeywords(recentKeywords);
        } else {
            Log.d("SearchActivity", "Không có từ khóa gần đây để hiển thị.");
        }
    }

    @SuppressLint("CheckResult")
    private void searchJobs(String query) {
        ApiJobService.ApiJobService.getAllJobs()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(jobs -> filterJobs(jobs, query))
                .subscribe(jobs -> {
                    if (!jobs.isEmpty()) {
                        recycler_view_search.setAdapter(workAdapter); // Dùng adapter công việc
                        workAdapter.setData(jobs); // Cập nhật công việc tìm thấy
                    } else {
                        // Nếu không có kết quả tìm kiếm, hiển thị từ khóa gần đây
                        showRecentKeywords();
                    }
                }, throwable -> {
                    Log.e("SearchActivity", "Lỗi khi tải dữ liệu công việc: " + throwable.getMessage());
                });
    }

    private List<Job> filterJobs(List<Job> jobs, String query) {
        return Observable.fromIterable(jobs)
                .filter(job -> job.getJobName().toLowerCase().contains(query.toLowerCase()))
                .toList()
                .blockingGet();
    }

    @SuppressLint("CheckResult")
    private void fetchJobsAndUpdateKeywords(List<String> keywords) {
        ApiJobService.ApiJobService.getAllJobs()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(jobs -> {
                    for (String keyword : keywords) {
                        int count = 0;
                        for (Job job : jobs) {
                            if (job.getJobName().toLowerCase().contains(keyword.toLowerCase())) {
                                count++;
                            }
                        }
                        workSearchAdapter.updateKeywordCount(keyword, count);
                    }
                }, throwable -> {
                    Log.e("SearchActivity", "Lỗi khi gọi API để lấy công việc: " + throwable.getMessage());
                });
    }
}
