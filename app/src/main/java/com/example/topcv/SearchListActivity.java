package com.example.topcv;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.topcv.adapter.WorkAdapter;
import com.example.topcv.api.ApiJobService;
import com.example.topcv.model.Job;
import java.util.List;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SearchListActivity extends AppCompatActivity {
    private RecyclerView recyclerViewJobs;
    private EditText search_edit_text;
    private TextView tvSearchCount;
    private WorkAdapter workAdapter;
    private ImageButton back_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_list);

        // Áp dụng Edge-to-Edge
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Ánh xạ các view
        recyclerViewJobs = findViewById(R.id.recyclerViewJobs);
        search_edit_text = findViewById(R.id.search_edit_text);
        tvSearchCount = findViewById(R.id.tvSearchCount);
        back_button = findViewById(R.id.back_button);

        // Thiết lập RecyclerView
        workAdapter = new WorkAdapter();
        recyclerViewJobs.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewJobs.setAdapter(workAdapter);

        // Lấy từ khóa tìm kiếm từ Intent
        String search_query = getIntent().getStringExtra("search_query");
        if (search_query != null) {
            search_edit_text.setText(search_query);
            searchJobs(search_query);
        }

        String keyword = getIntent().getStringExtra("keyword");
        if (keyword != null) {
            // Tách chuỗi để chỉ lấy phần trước dấu "-"
            String keywordTrimmed = keyword.split(" - ")[0];
            search_edit_text.setText(keywordTrimmed);
            searchJobs(keywordTrimmed);
        }

        back_button.setOnClickListener(view -> finish());
    }


    private void searchJobs(String query) {
        ApiJobService.ApiJobService.getAllJobs()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(jobs -> filterJobs(jobs, query))
                .subscribe(jobs -> {
                    workAdapter.setData(jobs); // Cập nhật danh sách công việc tìm thấy
                    if(jobs.size() == 1){
                        tvSearchCount.setText(jobs.size() + " Result"); // Hiển thị số lượng kết quả
                    }
                    else {
                        tvSearchCount.setText(jobs.size() + " Results"); // Hiển thị số lượng kết quả
                    }
                }, throwable -> {
                    // Xử lý lỗi
                    tvSearchCount.setText("Lỗi khi tải dữ liệu.");
                });
    }

    private List<Job> filterJobs(List<Job> jobs, String query) {
        return Observable.fromIterable(jobs)
                .filter(job -> job.getJobName().toLowerCase().contains(query.toLowerCase()))
                .toList()
                .blockingGet();
    }
}
