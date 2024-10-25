package com.example.topcv;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.FileOutputStream;
import java.io.IOException;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;
import com.example.topcv.api.ApiResumeService;
import com.example.topcv.model.Resume;

public class CvActivity extends AppCompatActivity {
    private ImageButton back_button, shareButton;
    private Button exportPdfButton;
    private ActivityResultLauncher<Intent> createPdfLauncher;
    private Uri savedPdfUri;

    private ImageView cv_logo;
    private TextView name, job_applying, introduction, experience, email, phone_number, education, skill, certification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cv);

        // Xử lý sự kiện hiển thị hệ thống padding
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setWidget();

        // Nhận resume_id từ Intent
        int resumeId = getIntent().getIntExtra("resume_id", -1); // Giá trị mặc định là -1 nếu không tìm thấy

        if (resumeId != -1) {
            // Gọi API để lấy dữ liệu Resume theo resumeId
            fetchResumeData(resumeId);
        } else {
            Log.d("CvActivity", "Resume ID not found.");
        }

        // Gọi API để lấy dữ liệu Resume với ID mặc định là 21
        fetchResumeData(resumeId);

        // Nút quay lại
        back_button.setOnClickListener(view -> finish());

        // Đăng ký launcher để mở file picker
        createPdfLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK) {
                Uri uri = result.getData().getData();
                if (uri != null) {
                    exportToPdf(uri); // Tiến hành lưu PDF vào nơi người dùng chọn
                    savedPdfUri = uri;  // Lưu URI của file đã được lưu
                }
            }
        });

        // Nút xuất PDF
        exportPdfButton.setOnClickListener(v -> openFilePicker());

        // Nút chia sẻ
        shareButton.setOnClickListener(v -> {
            if (savedPdfUri != null) {
                sharePdf(savedPdfUri);  // Chia sẻ PDF đã lưu
            } else {
                Toast.makeText(this, "Please export the PDF first", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Thiết lập các widget trên layout
    private void setWidget() {
        back_button = findViewById(R.id.back_button);
        exportPdfButton = findViewById(R.id.export_pdf_button);
        shareButton = findViewById(R.id.share_button);
        cv_logo = findViewById(R.id.cv_logo);
        name = findViewById(R.id.name);
        job_applying = findViewById(R.id.job_applying);
        introduction = findViewById(R.id.introduction);
        experience = findViewById(R.id.experience);
        email = findViewById(R.id.email);
        phone_number = findViewById(R.id.phone_number);
        education = findViewById(R.id.education);
        skill = findViewById(R.id.skill);
        certification = findViewById(R.id.certification);
    }

    // Gọi API để lấy dữ liệu Resume theo resumeId
    private void fetchResumeData(int resumeId) {
        ApiResumeService.apiResumeService.getResumeById(resumeId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resume -> {
                    if (resume != null) {
                        populateResumeData(resume);  // Gán dữ liệu vào view
                    } else {
                        Log.e("CvActivity", "No resume found with this resumeId.");
                        Toast.makeText(this, "No resume found", Toast.LENGTH_SHORT).show();
                    }
                }, throwable -> {
                    Log.e("CvActivity", "Error fetching resume data: " + throwable.getMessage());
                    throwable.printStackTrace(); // Để ghi chi tiết lỗi
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

            name.setText(resume.getApplicant_name());
            job_applying.setText(resume.getJob_applying());
            introduction.setText(resume.getIntroduction());
            experience.setText(resume.getExperience());
            email.setText(resume.getEmail());
            phone_number.setText(resume.getPhone_number());
            education.setText(resume.getEducation());
            skill.setText(resume.getSkills());
            certification.setText(resume.getCertificate());
            // Example for ImageView
            String imageUri = resume.getImage();
            if (imageUri != null && !imageUri.isEmpty()) {
                cv_logo.setImageURI(Uri.parse(imageUri));
            } else {
                cv_logo.setImageResource(R.drawable.account_ic);
                Log.w("CvActivity", "No image URL found, using default image");
            }
        } else {
            Log.e("CvActivity", "Resume data is null");
        }
    }

    // Mở bộ chọn file để lưu file PDF
    private void openFilePicker() {
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.setType("application/pdf");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.putExtra(Intent.EXTRA_TITLE, "cv_output.pdf");  // Tên file mặc định
        createPdfLauncher.launch(intent);
    }

    // Xuất nội dung Layout thành PDF và lưu vào vị trí người dùng chọn
    private void exportToPdf(Uri uri) {
        LinearLayout contentLayout = findViewById(R.id.information_content);
        int width = contentLayout.getWidth();
        int height = contentLayout.getHeight();
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        contentLayout.draw(canvas);

        PdfDocument pdfDocument = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(width, height, 1).create();
        PdfDocument.Page page = pdfDocument.startPage(pageInfo);
        Canvas pdfCanvas = page.getCanvas();
        pdfCanvas.drawBitmap(bitmap, 0, 0, null);
        pdfDocument.finishPage(page);

        try (ParcelFileDescriptor pfd = getContentResolver().openFileDescriptor(uri, "w");
             FileOutputStream fos = new FileOutputStream(pfd.getFileDescriptor())) {
            pdfDocument.writeTo(fos);
            Toast.makeText(this, "PDF saved successfully!", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error saving PDF", Toast.LENGTH_SHORT).show();
        }

        pdfDocument.close();
    }

    // Chia sẻ PDF
    private void sharePdf(Uri uri) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("application/pdf");
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(Intent.createChooser(shareIntent, "Share PDF using"));
    }
}
