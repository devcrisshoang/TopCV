package com.example.topcv;


import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


import com.example.topcv.api.ApiResumeService;
import com.example.topcv.model.Resume;
import com.github.dhaval2404.imagepicker.ImagePicker;

import java.io.IOException;
import java.util.Objects;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class CreateCvActivity extends AppCompatActivity {
    private Button add_new_cv_button;
    private ImageButton information_back_button;
    private ImageView camera_imageview, user_avatar;
    private EditText position;
    private EditText name;
    private EditText introduction;
    private EditText email;
    private EditText phone_number;
    private EditText education;
    private EditText skills;
    private EditText certification;
    private EditText experience;
    private Uri resumeImageUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_cv);

        // Đặt padding cho các view để tránh overlap với system bars
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        setWidget();

        add_new_cv_button.setOnClickListener(v -> postResume());
        information_back_button.setOnClickListener(view -> {
            finish();
        });

        camera_imageview.setOnClickListener(view -> {
            ImagePicker.with(this)
                    .crop()	    			//Crop image(Optional), Check Customization for more option
                    .compress(1024)			//Final image size will be less than 1 MB(Optional)
                    .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                    .start();
        });
    }

    private void setWidget(){
        add_new_cv_button = findViewById(R.id.add_new_cv_button);
        information_back_button = findViewById(R.id.information_back_button);
        camera_imageview = findViewById(R.id.camera_imageview);
        user_avatar = findViewById(R.id.user_avatar);
        position = findViewById(R.id.position);
        name = findViewById(R.id.name);
        introduction = findViewById(R.id.introduction);
        email = findViewById(R.id.email);
        phone_number = findViewById(R.id.phone_number);
        education = findViewById(R.id.education);
        skills = findViewById(R.id.skills);
        certification = findViewById(R.id.certification);
        experience = findViewById(R.id.experience);
    }

    private void postResume() {
        // Lấy dữ liệu từ các trường nhập liệu
        String resumeName = name.getText().toString();
        String resumeEmail = email.getText().toString();
        String resumePhoneNumber = phone_number.getText().toString();
        String resumeEducation = education.getText().toString();
        String resumeSkills = skills.getText().toString();
        String resumeCertification = certification.getText().toString();
        String resumeJobApplication = position.getText().toString();
        String resumeIntroduction = introduction.getText().toString();
        String resumeExperience = experience.getText().toString();
        String resumeImage = resumeImageUri != null ? resumeImageUri.toString() : "";

        // Tạo đối tượng Resume
        Resume resume = new Resume(
                resumeName,
                resumeEmail,
                resumeEducation,
                resumePhoneNumber,
                resumeCertification,
                resumeSkills,
                resumeJobApplication,
                resumeIntroduction,
                resumeImage,
                resumeExperience,
                6
        );

        // Gọi API để đăng dữ liệu hồ sơ
        ApiResumeService.apiResumeService.createUser(resume)
                .subscribeOn(Schedulers.io())  // Chạy trên luồng nền
                .observeOn(AndroidSchedulers.mainThread())  // Quan sát kết quả trên luồng chính
                .subscribe(
                        response -> {
                            // Xử lý khi thành công
                            Toast.makeText(CreateCvActivity.this, "CV đã được tạo thành công!", Toast.LENGTH_SHORT).show();
                        },
                        throwable -> {
                            // Xử lý khi có lỗi
                            Toast.makeText(CreateCvActivity.this, "Có lỗi xảy ra: " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                );
        finish();
    }


    private void showStyleDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose CV Style")
                .setItems(new String[]{"Basic", "Professional"}, (dialog, which) -> {
                    if (which == 0) {
                        // Handle Basic CV
                        createBasicCV();
                    } else if (which == 1) {
                        // Handle Professional CV
                        createProfessionalCV();
                    }
                });
        builder.create().show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null && data.getData() != null) {
            resumeImageUri = data.getData();
            user_avatar.setImageURI(resumeImageUri);
        } else {
            Toast.makeText(this, "No image selected", Toast.LENGTH_SHORT).show();
        }
    }


    private void createBasicCV() {
        // Code to create a Basic CV
    }

    private void createProfessionalCV() {
        // Code to create a Professional CV
    }
}
