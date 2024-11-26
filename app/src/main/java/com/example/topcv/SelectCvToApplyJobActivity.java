package com.example.topcv;

import static com.example.topcv.R.*;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.topcv.adapter.AppliedResumeAdapter;
import com.example.topcv.adapter.ProfileAdapter;
import com.example.topcv.api.ApiApplicantJobService;
import com.example.topcv.api.ApiApplicantService;
import com.example.topcv.api.ApiResumeService;
import com.example.topcv.model.Applicant;
import com.example.topcv.model.ApplicantJob;
import com.example.topcv.model.Company;
import com.example.topcv.model.Job;
import com.example.topcv.model.Resume;
import com.example.topcv.utils.PaginationScrollListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SelectCvToApplyJobActivity extends AppCompatActivity {
    private static final int PICK_FILE_REQUEST = 1; // Mã yêu cầu để mở hộp thoại chọn file
    private RecyclerView select_cv_recyclerview;
    private RadioButton my_cv;
    private RadioButton upload_from_device;
    private LinearLayout upload_layout;
    private LinearLayout file_layout;
    private Button button_upload;
    private TextView file_name, size_of_file, warning;
    private ImageButton close_ic;
    private ImageButton back_button;
    private AppliedResumeAdapter appliedResumeAdapter;
    private List<Resume> resumeList;
    private boolean isLoading;//
    private boolean isLastPage;//
    private int totalPage;//
    private int currentPage = 1;//
    private List<Resume> resume_data;
    private LinearLayoutManager linearLayoutManager;
    private RecyclerView.ItemDecoration itemDecoration;
    private String filePath;
    private Button Apply_Button;

    private int applicant_id;
    private int jobId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_select_cv_to_apply_job);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        applicant_id = getIntent().getIntExtra("applicant_id", 0);
        Log.e("SelectCvToApply","applicant_id " + applicant_id);
        setWidget();
        fetchResumesByApplicantId(applicant_id);

        back_button.setOnClickListener(view -> finish());
        upload_layout.setVisibility(View.GONE);
        my_cv.setOnClickListener(view -> {
            select_cv_recyclerview.setVisibility(View.VISIBLE);
            upload_layout.setVisibility(View.GONE);
            fetchResumesByApplicantId(applicant_id);

        });
        upload_from_device.setOnClickListener(view -> {
            select_cv_recyclerview.setVisibility(View.GONE);
            upload_layout.setVisibility(View.VISIBLE);
            file_layout.setVisibility(View.GONE);
        });
        button_upload.setOnClickListener(v -> {
            Toast.makeText(this, "Đang mở hộp thoại chọn file...", Toast.LENGTH_SHORT).show();
            openFileChooser();
        });
        close_ic.setOnClickListener(view -> {
            file_layout.setVisibility(View.GONE);
        });
        select_cv_recyclerview.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
            @Override
            public void loadMoreItem() {
                isLoading = true;
                currentPage += 1;
                loadNextPageResume();
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }
        });
        Apply_Button.setOnClickListener(view -> sendApplicantJobData());
    }

    private void sendApplicantJobData() {
        // Tạo đối tượng Calendar và định dạng thời gian
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String formattedTime = sdf.format(calendar.getTime());

        // Lấy thông tin Resume đã chọn
        Resume selectedResume = appliedResumeAdapter.getSelectedItem();
        Log.e("SelectCvToApply", "IDD: " + selectedResume.getId());

        // Tạo đối tượng ApplicantJob
        ApplicantJob applicantJob = new ApplicantJob(jobId, applicant_id, selectedResume.getId(), false, false, formattedTime);

        // Kiểm tra lại dữ liệu đã được gán đúng chưa
        Log.e("SelectCvToApply", "ApplicantJob Data: " +
                "ApplicantId: " + applicantJob.getApplicantId() +
                ", JobId: " + applicantJob.getJobId() +
                ", ResumeId: " + applicantJob.getResumeId() +
                ", Time: " + applicantJob.getTime());

        // Gửi dữ liệu đến API bằng Retrofit
        ApiApplicantJobService.ApiApplicantJobService.createApplicantJob(applicantJob)
                .subscribeOn(Schedulers.io())  // Chạy trên luồng nền
                .observeOn(AndroidSchedulers.mainThread())  // Quan sát kết quả trên luồng chính
                .subscribe(
                        response -> {
                            // Xử lý thành công
                            Log.e("SelectCvToApply", "Successfully");
                            Toast.makeText(this, "Dữ liệu đã được gửi thành công!", Toast.LENGTH_SHORT).show();
                        },
                        throwable -> {
                            // Xử lý lỗi
                            Toast.makeText(this, "Có lỗi xảy ra: " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.e("SelectCvToApply", "Error: " + throwable.getMessage());
                        }
                );
        finish();
    }

    private void getTotalPageResume(){
        if(resume_data.size() <= 10){
            totalPage = 1;
        }
        else if(resume_data.size() % 10 == 0){
            totalPage = resume_data.size()/10;
        }
        else if(resume_data.size() % 10 != 0){
            totalPage = resume_data.size()/10 +1;
        }
        Log.e("total page","total = " + totalPage);
    }

    private void setFirstResumeData(){
        resumeList = getListResume();
        appliedResumeAdapter.setData(resumeList);

        if (currentPage < totalPage){
            appliedResumeAdapter.addFooterLoading();
        } else {
            isLastPage = true;
        }
    }

    private void loadNextPageResume(){
        new Handler().postDelayed(() -> {
            List<Resume> list = getListResume();
            appliedResumeAdapter.removeFooterLoading();
            resumeList.addAll(list);
            appliedResumeAdapter.notifyDataSetChanged();
            isLoading = false;

            if (currentPage < totalPage) {
                appliedResumeAdapter.addFooterLoading();
            }
            else {
                isLastPage = true;
            }
        }, 2000);
    }

    private List<Resume> getListResume() {
        Toast.makeText(this, "Load data page" + currentPage, Toast.LENGTH_SHORT).show();
        List<Resume> list = new ArrayList<>();

        int start = (currentPage - 1) * 5; // Tính chỉ số bắt đầu
        int end = Math.min(start + 5, resume_data.size()); // Tính chỉ số kết thúc

        if (start < resume_data.size()) {
            list.addAll(resume_data.subList(start, end)); // Thêm các phần tử từ workList vào danh sách
        }
        return list;
    }

    private void fetchResumesByApplicantId(int applicantId) {
        ApiResumeService.apiResumeService.getResumesByApplicantId(applicantId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resumes -> {
                    if (resumes.isEmpty()) {
                        Toast.makeText(this, "Không có dữ liệu hồ sơ.", Toast.LENGTH_SHORT).show();
                    } else {
                        // Clear the existing list and add new data
                        resume_data.clear();
                        List<Resume> filteredResumes = new ArrayList<>();
                        for (Resume resume : resumes) {
                            if (resume.getFile_path().isEmpty()) {
                                filteredResumes.add(resume);
                            }
                        }
                        resume_data.addAll(filteredResumes);

                        // Pass the data to the adapter
                        //appliedResumeAdapter.setData(resumeList);

                        getTotalPageResume(); // Tính tổng số trang sau khi có dữ liệu
                        setFirstResumeData(); // Gọi setFirstData sau khi có dữ liệu
                        select_cv_recyclerview.setLayoutManager(linearLayoutManager);
                        select_cv_recyclerview.setAdapter(appliedResumeAdapter);
                        select_cv_recyclerview.addItemDecoration(itemDecoration);
                        appliedResumeAdapter.notifyDataSetChanged();
                    }
                }, throwable -> {
                    // Handle errors
                    throwable.printStackTrace();
                });
    }

    private void setWidget(){
        Apply_Button = findViewById(R.id.Apply_Button);
        my_cv = findViewById(R.id.my_cv);
        upload_from_device = findViewById(R.id.upload_from_device);
        select_cv_recyclerview = findViewById(R.id.select_cv_recyclerview);
        upload_layout = findViewById(R.id.upload_layout);
        file_layout = findViewById(R.id.file_layout);
        button_upload = findViewById(R.id.button_upload);
        file_name = findViewById(R.id.file_name);
        size_of_file = findViewById(R.id.size_of_file);
        warning = findViewById(R.id.warning);
        close_ic = findViewById(R.id.close_ic);
        back_button = findViewById(R.id.back_button);

        // Khởi tạo adapter với danh sách trống ban đầu
        appliedResumeAdapter = new AppliedResumeAdapter();
        appliedResumeAdapter.setData(new ArrayList<>()); // Set dữ liệu trống ban đầu

        // Khởi tạo LinearLayoutManager và gán nó cho RecyclerView
        linearLayoutManager = new LinearLayoutManager(this);  // Khởi tạo layout manager
        select_cv_recyclerview.setLayoutManager(linearLayoutManager);  // Gắn layout manager cho RecyclerView

        // Khởi tạo item decoration
        itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);

        // Gắn adapter cho RecyclerView
        select_cv_recyclerview.setAdapter(appliedResumeAdapter);

        resumeList = new ArrayList<>();
        resume_data = new ArrayList<>();

        //Log.e("SelectCV","ID1: "+ applicant_id);
        jobId = getIntent().getIntExtra("jobId",0);
    }

    // Mở hộp thoại chọn file
    private void openFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*"); // Chọn tất cả loại file
        startActivityForResult(Intent.createChooser(intent, "Chọn file"), PICK_FILE_REQUEST);
    }

    // Nhận kết quả từ hộp thoại chọn file
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_FILE_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri fileUri = data.getData();
            if (fileUri != null) {
                // Hiển thị thông tin file
                displayFileInfo(fileUri);

                // Lưu đường dẫn file vào biến filePath
                filePath = getRealPathFromUri(fileUri);
                Log.d("FilePath", "File path: " + filePath); // Để kiểm tra đường dẫn
            }
        }
    }

    private void postResume(String filePath) {
        // Tạo đối tượng Resume
        Resume resume = new Resume(
                filePath,
                applicant_id
        );

        // Gọi API để đăng dữ liệu hồ sơ
        ApiResumeService.apiResumeService.createResume(resume)
                .subscribeOn(Schedulers.io())  // Chạy trên luồng nền
                .observeOn(AndroidSchedulers.mainThread())  // Quan sát kết quả trên luồng chính
                .subscribe(
                        response -> {
                            // Xử lý khi thành công
                            Toast.makeText(this, "CV đã được tạo thành công!", Toast.LENGTH_SHORT).show();
                        },
                        throwable -> {
                            // Xử lý khi có lỗi
                            Toast.makeText(this, "Có lỗi xảy ra: " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                );
        finish();
    }

    private String getRealPathFromUri(Uri uri) {
        String filePath = null;
        if (uri.getScheme().equals("content")) {
            try (Cursor cursor = getContentResolver().query(uri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    int columnIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    if (columnIndex != -1) {
                        String fileName = cursor.getString(columnIndex);
                        filePath = getExternalFilesDir(null) + "/" + fileName;

                    }
                }
            }
        } else if (uri.getScheme().equals("file")) {
            filePath = uri.getPath();
        }
        return filePath;
    }

    // Hiển thị thông tin file
    private void displayFileInfo(Uri fileUri) {
        String fileName = getFileName(fileUri);
        long fileSize = getFileSize(fileUri);

        file_name.setText(fileName);
        size_of_file.setText(String.format("%d KB", fileSize / 1024)); // Chuyển sang KB

        // Kiểm tra định dạng file
        if (isValidFileType(fileName)) {
            warning.setVisibility(View.GONE);
        } else {
            warning.setVisibility(View.VISIBLE);
            warning.setText("File không hợp lệ. Vui lòng chọn file .doc, .docx, hoặc .pdf.");
        }

        // Hiển thị layout
        file_layout.setVisibility(View.VISIBLE);
    }

    // Lấy tên file từ Uri
    private String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            try (Cursor cursor = getContentResolver().query(uri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    if (nameIndex != -1) { // Kiểm tra nếu cột tồn tại
                        result = cursor.getString(nameIndex);
                    }
                }
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    // Lấy kích thước file từ Uri
    private long getFileSize(Uri uri) {
        long size = 0;
        if (uri.getScheme().equals("content")) {
            try (Cursor cursor = getContentResolver().query(uri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    int sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE);
                    if (sizeIndex != -1) { // Kiểm tra nếu cột tồn tại
                        size = cursor.getLong(sizeIndex);
                    }
                }
            }
        }
        return size;
    }

    // Kiểm tra định dạng file
    private boolean isValidFileType(String fileName) {
        return fileName.endsWith(".doc") || fileName.endsWith(".docx") || fileName.endsWith(".pdf");
    }
}
