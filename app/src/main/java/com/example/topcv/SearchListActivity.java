package com.example.topcv;

import android.annotation.SuppressLint;
import android.os.Bundle;
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

    private String search_query;
    private String keyword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setWidget();

        setClick();

    }

    private void setClick(){
        back_button.setOnClickListener(view -> finish());
    }

    private void setWidget(){
        recyclerViewJobs = findViewById(R.id.recyclerViewJobs);
        search_edit_text = findViewById(R.id.search_edit_text);
        tvSearchCount = findViewById(R.id.tvSearchCount);
        back_button = findViewById(R.id.back_button);
        workAdapter = new WorkAdapter();
        recyclerViewJobs.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewJobs.setAdapter(workAdapter);
        search_query = getIntent().getStringExtra("search_query");
        if (search_query != null) {
            search_edit_text.setText(search_query);
            searchJobs(search_query);
        }
        keyword = getIntent().getStringExtra("keyword");
        if (keyword != null) {
            String keywordTrimmed = keyword.split(" - ")[0];
            search_edit_text.setText(keywordTrimmed);
            searchJobs(keywordTrimmed);
        }
    }

    @SuppressLint("CheckResult")
    private void searchJobs(String query) {
        ApiJobService.ApiJobService.getAllJobs()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(jobs -> filterJobs(jobs, query))
                .subscribe(jobs -> {
                    workAdapter.setData(jobs);
                    if(jobs.size() == 1){
                        tvSearchCount.setText(jobs.size() + " Result");
                    }
                    else {
                        tvSearchCount.setText(jobs.size() + " Results");
                    }
                }, throwable -> {
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
