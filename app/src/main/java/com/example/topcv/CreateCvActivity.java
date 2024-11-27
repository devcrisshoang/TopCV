package com.example.topcv;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.example.topcv.api.ApiApplicantService;
import com.example.topcv.api.ApiNotificationService;
import com.example.topcv.api.ApiResumeService;
import com.example.topcv.model.Notification;
import com.example.topcv.model.Resume;
import com.github.dhaval2404.imagepicker.ImagePicker;
import java.time.LocalDateTime;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class CreateCvActivity extends AppCompatActivity {

    private Button add_new_cv_button;

    private ImageButton information_back_button;

    private ImageView camera_imageview;
    private ImageView user_avatar;

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

    private int id_User;
    public int id_Applicant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_cv);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setWidget();

        setClick();

    }

    private void setClick(){
        add_new_cv_button.setOnClickListener(v -> postResume(id_Applicant));
        information_back_button.setOnClickListener(view -> finish());
        camera_imageview.setOnClickListener(view -> ImagePicker.with(this)
                .crop()
                .compress(1024)
                .maxResultSize(1080, 1080)
                .start());
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
        id_User = getIntent().getIntExtra("id_User", 0);
        getApplicant(id_User);
    }

    @SuppressLint("CheckResult")
    private void getApplicant(int userId) {
        ApiApplicantService.ApiApplicantService.getApplicantByUserId(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        applicant -> {
                            if (applicant != null) {
                                id_Applicant = applicant.getId();
                            } else {
                                Toast.makeText(this, "Applicant null", Toast.LENGTH_SHORT).show();
                            }
                        },
                        throwable -> {
                            Log.e("MessengerAdapter", "Error fetching applicant: " + throwable.getMessage());
                            Toast.makeText(this, "Failed to load applicant", Toast.LENGTH_SHORT).show();
                        }
                );
    }

    @SuppressLint("CheckResult")
    private void postResume(int id_Applicant) {
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
                id_Applicant
        );

        ApiResumeService.apiResumeService.createResume(resume)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        response -> Toast.makeText(CreateCvActivity.this, "CV created successfully!", Toast.LENGTH_SHORT).show(),
                        throwable -> Toast.makeText(CreateCvActivity.this, "An error occurred: " + throwable.getMessage(), Toast.LENGTH_SHORT).show()
                );
        String content = "You just created a " + resumeJobApplication +" job resume.";
        LocalDateTime currentTime = LocalDateTime.now();

        Notification notification = new Notification(
                0,
                content,
                currentTime.toString(),
                id_User
        );
        ApiNotificationService.ApiNotificationService.createNotification(notification)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        response -> Toast.makeText(CreateCvActivity.this, "Notification created successfully!", Toast.LENGTH_SHORT).show(),
                        throwable -> Toast.makeText(CreateCvActivity.this, "An error occurred: " + throwable.getMessage(), Toast.LENGTH_SHORT).show()
                );
        finish();
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
}
