package com.example.topcv;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.DocumentsContract;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.FileOutputStream;
import java.io.IOException;

public class CvActivity extends AppCompatActivity {
    private ImageButton back_button, shareButton;
    private Button exportPdfButton;
    private ActivityResultLauncher<Intent> createPdfLauncher;
    private Uri savedPdfUri;  // Để lưu URI của PDF sau khi lưu

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

        // Nút quay lại
        back_button = findViewById(R.id.back_button);
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
        exportPdfButton = findViewById(R.id.export_pdf_button);
        exportPdfButton.setOnClickListener(v -> openFilePicker());

        // Nút chia sẻ
        shareButton = findViewById(R.id.share_button);
        shareButton.setOnClickListener(v -> {
            if (savedPdfUri != null) {
                sharePdf(savedPdfUri);  // Chia sẻ PDF đã lưu
            } else {
                Toast.makeText(this, "Please export the PDF first", Toast.LENGTH_SHORT).show();
            }
        });
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
