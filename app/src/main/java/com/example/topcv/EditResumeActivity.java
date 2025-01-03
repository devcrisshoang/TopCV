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
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.example.topcv.api.ApiResumeService;
import com.example.topcv.model.Resume;
import com.github.dhaval2404.imagepicker.ImagePicker;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class EditResumeActivity extends AppCompatActivity {

    private Button edit_cv_button;

    private ImageButton back_button;
    private ImageButton delete_button;

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

    private int id;
    private int resume_id;

    private Uri resumeImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_cv);
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

        edit_cv_button.setOnClickListener(view -> editResumeData());

        camera_imageview.setOnClickListener(view -> ImagePicker.with(this)
                .crop()
                .compress(1024)
                .maxResultSize(1080, 1080)
                .start());

        delete_button.setOnClickListener(view -> showDeleteConfirmationDialog());
    }

    private void showDeleteConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Confirm")
                .setMessage("Are you sure you want to delete this profile?")
                .setPositiveButton("Yes", (dialog, which) -> deleteResume())
                .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                .show();
    }
    @SuppressLint("CheckResult")
    private void deleteResume() {
        Log.e("Delete","Delete");
        ApiResumeService.apiResumeService.deleteResumeById(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    Log.d("EditCvActivity", "Resume deleted successfully.");
                    Toast.makeText(EditResumeActivity.this, "CV deleted successfully", Toast.LENGTH_SHORT).show();
                    finish();
                }, throwable -> {
                    Log.e("EditCvActivity", "Error deleting resume: " + throwable.getMessage());
                    Toast.makeText(EditResumeActivity.this, "Failed to delete CV", Toast.LENGTH_SHORT).show();
                });
    }

    @SuppressLint("CheckResult")
    private void fetchResumeData(int resumeId) {
        ApiResumeService.apiResumeService.getResumeById(resumeId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resume -> {
                    if (resume != null) {
                        populateResumeData(resume);
                    } else {
                        Log.e("CvActivity", "No resume found with this resumeId.");
                        Toast.makeText(this, "No resume found", Toast.LENGTH_SHORT).show();
                    }
                }, throwable -> {
                    Log.e("CvActivity", "Error fetching resume data: " + throwable.getMessage());
                    throwable.printStackTrace();
                    Toast.makeText(this, "Error fetching resume data", Toast.LENGTH_SHORT).show();
                });
    }

    private void populateResumeData(Resume resume) {
        if (resume != null) {
            Log.d("CvActivity", "Populating resume data: " + resume.toString());

            if (resume.getApplicant_name() != null) {
                name.setText(resume.getApplicant_name());
            } else {
                Log.w("CvActivity", "Applicant name is null");
            }
            id = resume.getId();
            name.setText(resume.getApplicant_name());
            position.setText(resume.getJob_applying());
            introduction.setText(resume.getIntroduction());
            experience.setText(resume.getExperience());
            email.setText(resume.getEmail());
            phone_number.setText(resume.getPhone_number());
            education.setText(resume.getEducation());
            skills.setText(resume.getSkills());
            certification.setText(resume.getCertificate());
            String imageUri = resume.getImage();
            if (imageUri != null && !imageUri.isEmpty()) {
                user_avatar.setImageURI(Uri.parse(imageUri));
                resumeImageUri = Uri.parse(imageUri);
            } else {
                user_avatar.setImageResource(R.drawable.account_ic);
                Log.w("CvActivity", "No image URL found, using default image");
            }
        } else {
            Log.e("CvActivity", "Resume data is null");
        }
    }

    private String checkAndLogNull(String input, String fieldName) {
        if (input == null) {
            Log.e("EditCvActivity", fieldName + " is null.");
            return "Not provided";
        }
        return input;
    }

    @SuppressLint("CheckResult")
    private void editResumeData() {
        String updatedName = checkAndLogNull(name.getText().toString().trim(), "Name");
        String updatedPosition = checkAndLogNull(position.getText().toString().trim(), "Position");
        String updatedIntroduction = checkAndLogNull(introduction.getText().toString().trim(), "Introduction");
        String updatedEmail = checkAndLogNull(email.getText().toString().trim(), "Email");
        String updatedPhoneNumber = checkAndLogNull(phone_number.getText().toString().trim(), "Phone Number");
        String updatedEducation = checkAndLogNull(education.getText().toString().trim(), "Education");
        String updatedSkills = checkAndLogNull(skills.getText().toString().trim(), "Skills");
        String updatedCertification = checkAndLogNull(certification.getText().toString().trim(), "Certification");
        String updatedExperience = checkAndLogNull(experience.getText().toString().trim(), "Experience");
        String updatedImage = (resumeImageUri != null) ? resumeImageUri.toString() : "default_image_uri";

        Resume updatedResume = new Resume();
        updatedResume.setId(id);
        updatedResume.setApplicant_name(updatedName);
        updatedResume.setJob_applying(updatedPosition);
        updatedResume.setIntroduction(updatedIntroduction);
        updatedResume.setEmail(updatedEmail);
        updatedResume.setPhone_number(updatedPhoneNumber);
        updatedResume.setEducation(updatedEducation);
        updatedResume.setSkills(updatedSkills);
        updatedResume.setCertificate(updatedCertification);
        updatedResume.setExperience(updatedExperience);
        updatedResume.setImage(updatedImage);

        ApiResumeService.apiResumeService.updateResumeById(id, updatedResume)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    Log.d("EditCvActivity", "Resume updated successfully with 204 No Content.");
                    Toast.makeText(EditResumeActivity.this, "CV updated successfully", Toast.LENGTH_SHORT).show();
                    finish();
                }, throwable -> {
                    Log.e("EditCvActivity", "Error updating resume: " + throwable.getMessage());
                    Toast.makeText(EditResumeActivity.this, "Failed to update CV", Toast.LENGTH_SHORT).show();
                });
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
    private void setWidget(){
        edit_cv_button = findViewById(R.id.edit_cv_button);
        back_button = findViewById(R.id.back_button);
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
        delete_button = findViewById(R.id.delete_button);
        resume_id = getIntent().getIntExtra("resume_edit",0);
        fetchResumeData(resume_id);
    }
}