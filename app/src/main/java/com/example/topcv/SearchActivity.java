package com.example.topcv;

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
    private EditText search_edit_text;
    private RecyclerView recycler_view_search;
    private WorkSearchAdapter workSearchAdapter;
    private WorkAdapter workAdapter;
    private SearchHistoryDatabaseHelper dbHelper;
    private TextView recent_search_textview;
    private ImageButton search_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        dbHelper = new SearchHistoryDatabaseHelper(this); // Khởi tạo database

        setWidget();
        setupRecyclerView();

        // Hiển thị tất cả từ khóa ngay khi ứng dụng khởi động
        showRecentKeywords();

        back_button.setOnClickListener(view -> finish());

        // Lắng nghe sự kiện khi nhấn nút search_button
        search_button.setOnClickListener(view -> {
            String query = search_edit_text.getText().toString().trim();
            if (!query.isEmpty()) {
                // Lưu từ khóa vào cơ sở dữ liệu chỉ khi nhấn nút tìm kiếm
                dbHelper.addKeyword(query);

                // Chuyển sang SearchListActivity và truyền từ khóa
                Intent intent = new Intent(SearchActivity.this, SearchListActivity.class);
                intent.putExtra("search_query", query);
                startActivity(intent);
            }
        });

        // Lắng nghe thay đổi văn bản
        search_edit_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String query = charSequence.toString().trim();

                if (query.isEmpty()) {
                    // Nếu không có văn bản, hiển thị từ khóa gần đây
                    showRecentKeywords();
                    recent_search_textview.setVisibility(View.VISIBLE);
                } else {
                    // Nếu có văn bản, thực hiện tìm kiếm (không lưu vào SQLite)
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
    }

    // Trong phương thức setupRecyclerView
    private void setupRecyclerView() {
        // Thiết lập WorkSearchAdapter để hiển thị từ khóa gần đây
        workSearchAdapter = new WorkSearchAdapter();
        // Thiết lập WorkAdapter để hiển thị kết quả tìm kiếm công việc
        workAdapter = new WorkAdapter();

        recycler_view_search.setLayoutManager(new LinearLayoutManager(this));

        // Thiết lập sự kiện click cho từ khóa
        workSearchAdapter.setOnKeywordClickListener(keyword -> {
            // Chuyển đến SearchListActivity và truyền từ khóa
            Intent intent = new Intent(SearchActivity.this, SearchListActivity.class);
            intent.putExtra("keyword", keyword);
            startActivity(intent);
        });
    }


    private void showRecentKeywords() {
        // Gọi dữ liệu từ SQLite qua dbHelper
        List<String> recentKeywords = dbHelper.getRecentKeywords();

        // Kiểm tra nếu danh sách từ khóa không rỗng
        if (!recentKeywords.isEmpty()) {
            recycler_view_search.setAdapter(workSearchAdapter);  // Thiết lập adapter
            workSearchAdapter.setKeywords(recentKeywords);      // Truyền danh sách từ khóa vào adapter
            fetchJobsAndUpdateKeywords(recentKeywords);         // Gọi hàm để cập nhật số lượng công việc
        } else {
            // Xử lý nếu không có từ khóa nào trong cơ sở dữ liệu
            Log.d("SearchActivity", "Không có từ khóa gần đây để hiển thị.");
        }
    }

    // Thực hiện tìm kiếm
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

    // Hàm lọc job theo query
    private List<Job> filterJobs(List<Job> jobs, String query) {
        return Observable.fromIterable(jobs)
                .filter(job -> job.getJobName().toLowerCase().contains(query.toLowerCase()))
                .toList()
                .blockingGet();
    }

    private void fetchJobsAndUpdateKeywords(List<String> keywords) {
        ApiJobService.ApiJobService.getAllJobs()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(jobs -> {
                    // Lưu số lượng công việc cho từng từ khóa
                    for (String keyword : keywords) {
                        int count = 0;
                        for (Job job : jobs) {
                            if (job.getJobName().toLowerCase().contains(keyword.toLowerCase())) {
                                count++;
                            }
                        }
                        // Cập nhật số lượng công việc cho từng từ khóa
                        workSearchAdapter.updateKeywordCount(keyword, count);
                    }
                }, throwable -> {
                    // Xử lý lỗi
                    Log.e("SearchActivity", "Lỗi khi gọi API để lấy công việc: " + throwable.getMessage());
                });
    }
}
