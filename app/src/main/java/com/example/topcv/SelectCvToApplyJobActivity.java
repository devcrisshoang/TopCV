package com.example.topcv;

import static com.example.topcv.R.*;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
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
import androidx.recyclerview.widget.RecyclerView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_select_cv_to_apply_job);

        // Áp dụng padding cho main layout
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

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

        back_button.setOnClickListener(view -> finish());
        upload_layout.setVisibility(View.GONE);
        my_cv.setOnClickListener(view -> {
            select_cv_recyclerview.setVisibility(View.VISIBLE);
            upload_layout.setVisibility(View.GONE);
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
                // Hiển thị tên và kích thước file
                displayFileInfo(fileUri);
            }
        }
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
