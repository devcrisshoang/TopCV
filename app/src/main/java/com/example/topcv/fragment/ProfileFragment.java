package com.example.topcv.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.topcv.CreateCvActivity;
import com.example.topcv.R;
import com.example.topcv.adapter.ProfileAdapter;
import com.example.topcv.api.ApiResumeService;
import com.example.topcv.model.Resume;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ProfileFragment extends Fragment {
    private Button create_cv_button;
    private RecyclerView recyclerView;
    private ProfileAdapter adapter;
    private RecyclerView.LayoutManager mLayoutManager;

    // Danh sách để lưu resumes
    private List<Resume> appItems;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate layout cho Fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        create_cv_button = view.findViewById(R.id.create_cv_button);
        recyclerView = view.findViewById(R.id.recyclerView);

        // Khởi tạo danh sách resumes
        appItems = new ArrayList<>();

        // Thiết lập RecyclerView
        recyclerView.setHasFixedSize(true);
        mLayoutManager = new GridLayoutManager(getContext(), 1);
        recyclerView.setLayoutManager(mLayoutManager);

        // Thiết lập Button để chuyển đến CreateCvActivity
        create_cv_button.setOnClickListener(view1 -> {
            Intent intent = new Intent(getContext(), CreateCvActivity.class);
            startActivity(intent);
        });

        // Gọi API để lấy Resume của Applicant có ID = 6
        fetchResumesByApplicantId(6);

        return view;
    }

    private void fetchResumesByApplicantId(int applicantId) {
        ApiResumeService.apiResumeService.getResumesByApplicantId(applicantId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resumes -> {
                    // Kiểm tra nếu danh sách resumes rỗng
                    if (resumes.isEmpty()) {
                        Toast.makeText(getContext(), "Không có dữ liệu hồ sơ.", Toast.LENGTH_SHORT).show();
                    } else {
                        // Cập nhật danh sách Resume vào biến appItems
                        appItems.clear(); // Đảm bảo danh sách trống trước khi thêm dữ liệu mới
                        appItems.addAll(resumes); // Thêm tất cả resume nhận được vào danh sách
                    }

                    // Cập nhật adapter bằng cách gọi phương thức riêng
                    updateAdapter();
                }, throwable -> {
                    // Xử lý lỗi
                    throwable.printStackTrace();
                });
    }

    private void updateAdapter() {
        if (adapter == null) {
            // Tạo adapter mới nếu chưa có
            adapter = new ProfileAdapter(getContext(), appItems);
            recyclerView.setAdapter(adapter);
        } else {
            // Cập nhật dữ liệu cho adapter hiện tại
            adapter.notifyDataSetChanged();
        }
    }
}
