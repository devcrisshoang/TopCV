package com.example.topcv;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.topcv.model.Job;

import java.util.ArrayList;

public class SeeAllActivity extends AppCompatActivity {
    private RecyclerView recycler_view_see_all;
    //private JobsAdapter jobsAdapter;
    private ImageButton back_button;
    private TextView content_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_all);

        // Thiết lập nút quay lại
        back_button = findViewById(R.id.back_button);
        content_list = findViewById(R.id.content_list);
        back_button.setOnClickListener(view -> finish());

        // Thiết lập RecyclerView
        recycler_view_see_all = findViewById(R.id.recycler_view_see_all);
        //jobsAdapter = new JobsAdapter(getApplicationContext());

        // Nhận dữ liệu từ Intent
        String categoryName = getIntent().getStringExtra("CATEGORY_NAME");
        ArrayList<Job> jobsList = getIntent().getParcelableArrayListExtra("JOBS_LIST");

        // Kiểm tra xem danh sách công việc có null không
        if (jobsList == null) {
            jobsList = new ArrayList<>();
        }

        // Hiển thị dữ liệu trong RecyclerView
        //jobsAdapter.setData(jobsList);
        recycler_view_see_all.setLayoutManager(new LinearLayoutManager(this));
        //recycler_view_see_all.setAdapter(jobsAdapter);
        content_list.setText(categoryName);

        // Hiển thị thông báo hoặc tên danh mục (tuỳ chọn)
        if (!jobsList.isEmpty()) {
            Toast.makeText(this, "Category: " + categoryName, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "No jobs available", Toast.LENGTH_SHORT).show();
        }
//        jobsAdapter.setOnItemClickListener(new JobsAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(Jobs job) {
//                Intent intent = new Intent(SeeAllActivity.this, CompanyInformationsActivity.class);
//                startActivity(intent);
//            }
//        });
    }

}
