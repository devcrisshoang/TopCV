package com.example.topcv;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
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
import com.example.topcv.api.ApiApplicantJobService;
import com.example.topcv.api.ApiResumeService;
import com.example.topcv.model.ApplicantJob;
import com.example.topcv.model.Resume;
import com.example.topcv.utils.DateTimeUtils;
import com.example.topcv.utils.PaginationScrollListener;
import java.util.ArrayList;
import java.util.List;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ApplyActivity extends AppCompatActivity {

    private static final int PICK_FILE_REQUEST = 1;

    private RecyclerView select_cv_recyclerview;

    private RadioButton my_cv;
    private RadioButton upload_from_device;

    private LinearLayout upload_layout;
    private LinearLayout file_layout;

    private Button button_upload;
    private Button Apply_Button;

    private TextView file_name, size_of_file, warning;

    private ImageButton close_ic;
    private ImageButton back_button;

    private AppliedResumeAdapter appliedResumeAdapter;

    private List<Resume> resumeList;
    private List<Resume> resume_data;

    private boolean isLoading;
    private boolean isLastPage;

    private int totalPage;
    private int currentPage = 1;
    private int applicant_id;
    private int jobId;

    private LinearLayoutManager linearLayoutManager;

    private RecyclerView.ItemDecoration itemDecoration;

    private String filePath;

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

        setWidget();

        setClick();

    }

    private void setClick(){
        back_button.setOnClickListener(view -> finish());

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
        button_upload.setOnClickListener(v -> openFileChooser());

        close_ic.setOnClickListener(view -> file_layout.setVisibility(View.GONE));

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

    @SuppressLint("CheckResult")
    private void sendApplicantJobData() {
        ApiApplicantJobService.ApiApplicantJobService.getAllApplicantJob()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        applicantJobs -> {
                            boolean isAlreadyApplied = false;
                            for (ApplicantJob job : applicantJobs) {
                                if (job.getJobId() == jobId && job.getApplicantId() == applicant_id) {
                                    isAlreadyApplied = true;
                                    break;
                                }
                            }

                            if (isAlreadyApplied) {
                                Toast.makeText(this, "You have already applied for this job", Toast.LENGTH_SHORT).show();
                            } else {
                                Resume selectedResume = appliedResumeAdapter.getSelectedItem();
                                String time = DateTimeUtils.getCurrentTime();

                                ApplicantJob applicantJob = new ApplicantJob(jobId, applicant_id, selectedResume.getId(), false, false, time);

                                ApiApplicantJobService.ApiApplicantJobService.createApplicantJob(applicantJob)
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(
                                                response -> {
                                                    Log.e("SelectCvToApply", "Successfully");
                                                    Toast.makeText(this, "Apply Successfully", Toast.LENGTH_SHORT).show();
                                                },
                                                throwable -> Log.e("SelectCvToApply", "Error: " + throwable.getMessage())
                                        );
                                finish();
                            }
                        },
                        throwable -> Log.e("SelectCvToApply", "Error fetching applicant jobs: " + throwable.getMessage())
                );
    }


    private void getTotalPageResume(){
        if(resume_data.size() <= 10){
            totalPage = 1;
        }
        else if(resume_data.size() % 10 == 0){
            totalPage = resume_data.size()/10;
        }
        else {
            resume_data.size();
            totalPage = resume_data.size() / 10 + 1;
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

    @SuppressLint("NotifyDataSetChanged")
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
        int start = (currentPage - 1) * 5;
        int end = Math.min(start + 5, resume_data.size());
        if (start < resume_data.size()) {
            list.addAll(resume_data.subList(start, end));
        }
        return list;
    }

    @SuppressLint("CheckResult")
    private void fetchResumesByApplicantId(int applicantId) {
        ApiResumeService.apiResumeService.getResumesByApplicantId(applicantId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resumes -> {
                    if (resumes.isEmpty()) {
                        Toast.makeText(this, "No profile data available.", Toast.LENGTH_SHORT).show();
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
                        getTotalPageResume();
                        setFirstResumeData();
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
        applicant_id = getIntent().getIntExtra("applicant_id", 0);
        appliedResumeAdapter = new AppliedResumeAdapter();
        appliedResumeAdapter.setData(new ArrayList<>());
        linearLayoutManager = new LinearLayoutManager(this);
        select_cv_recyclerview.setLayoutManager(linearLayoutManager);
        itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        select_cv_recyclerview.setAdapter(appliedResumeAdapter);
        resumeList = new ArrayList<>();
        resume_data = new ArrayList<>();
        jobId = getIntent().getIntExtra("jobId",0);
        fetchResumesByApplicantId(applicant_id);
        upload_layout.setVisibility(View.GONE);
    }

    private void openFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        startActivityForResult(Intent.createChooser(intent, "Select file"), PICK_FILE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_FILE_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri fileUri = data.getData();
            if (fileUri != null) {
                displayFileInfo(fileUri);
                filePath = getRealPathFromUri(fileUri);
                Log.d("FilePath", "File path: " + filePath);
            }
        }
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

    private void displayFileInfo(Uri fileUri) {
        String fileName = getFileName(fileUri);
        long fileSize = getFileSize(fileUri);

        file_name.setText(fileName);
        size_of_file.setText(String.format("%d KB", fileSize / 1024));

        if (isValidFileType(fileName)) {
            warning.setVisibility(View.GONE);
        } else {
            warning.setVisibility(View.VISIBLE);
            warning.setText("Invalid file. Please select a .doc, .docx, or .pdf file.");
        }

        file_layout.setVisibility(View.VISIBLE);
    }

    private String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            try (Cursor cursor = getContentResolver().query(uri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    if (nameIndex != -1) {
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

    private long getFileSize(Uri uri) {
        long size = 0;
        if (uri.getScheme().equals("content")) {
            try (Cursor cursor = getContentResolver().query(uri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    int sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE);
                    if (sizeIndex != -1) {
                        size = cursor.getLong(sizeIndex);
                    }
                }
            }
        }
        return size;
    }

    private boolean isValidFileType(String fileName) {
        return fileName.endsWith(".doc") || fileName.endsWith(".docx") || fileName.endsWith(".pdf");
    }
}
