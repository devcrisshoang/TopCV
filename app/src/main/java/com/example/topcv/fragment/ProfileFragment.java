package com.example.topcv.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.example.topcv.api.ApiApplicantService;
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
    private int id_User;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate layout cho Fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        create_cv_button = view.findViewById(R.id.create_cv_button);
        recyclerView = view.findViewById(R.id.recyclerView);

        // Khởi tạo danh sách resumes
        appItems = new ArrayList<>();
        id_User = getArguments().getInt("user_id", -1);


        // Thiết lập RecyclerView
        recyclerView.setHasFixedSize(true);
        mLayoutManager = new GridLayoutManager(getContext(), 1);
        recyclerView.setLayoutManager(mLayoutManager);

        create_cv_button.setOnClickListener(view1 -> {
            if (appItems.size() >= 5) {
                // Hiển thị AlertDialog nếu danh sách đã đủ 5 Resume
                new androidx.appcompat.app.AlertDialog.Builder(getContext())
                        .setTitle("Notification")
                        .setMessage("The resume list is full 5. Please delete unnecessary resumes before adding new ones.")
                        .setPositiveButton("OK", (dialog, which) -> dialog.dismiss()) // Nút OK để đóng dialog
                        .show();
            } else {
                // Nếu chưa đủ, chuyển đến CreateCvActivity để thêm resume mới
                Intent intent = new Intent(getContext(), CreateCvActivity.class);
                intent.putExtra("id_User",id_User);
                startActivity(intent);
            }
        });


        getApplicant(id_User);
        Log.e("id_User", "id_User" + id_User);

        return view;
    }

    private void getApplicant(int userId) {
        ApiApplicantService.ApiApplicantService.getApplicantByUserId(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        applicant -> {
                            if (applicant != null) {
                                fetchResumesByApplicantId(applicant.getId());
                                Log.e("ProfileFragment","ID: " + applicant.getId());
                            } else {
                                Toast.makeText(getContext(), "Applicant null", Toast.LENGTH_SHORT).show();
                            }
                        },
                        throwable -> {
                            Log.e("MessengerAdapter", "Error fetching applicant: " + throwable.getMessage());
                            Toast.makeText(getContext(), "Failed to load applicant", Toast.LENGTH_SHORT).show();
                        }
                );
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
                        // Lọc những Resume có file_path là rỗng
                        List<Resume> filteredResumes = new ArrayList<>();
                        for (Resume resume : resumes) {
                            if (resume.getFile_path().isEmpty()) {
                                filteredResumes.add(resume);
                            }
                        }
                        appItems.addAll(filteredResumes); // Thêm tất cả resume lọc được vào danh sách
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
            adapter = new ProfileAdapter(getContext(), appItems,id_User);
            recyclerView.setAdapter(adapter);
        } else {
            // Cập nhật dữ liệu cho adapter hiện tại
            adapter.notifyDataSetChanged();
        }
    }
}
