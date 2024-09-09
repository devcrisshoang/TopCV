package com.example.topcv;

import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.example.topcv.adapter.ListInformationlAdapter;


public class CompanyInformationsActivity extends AppCompatActivity {
    private ListInformationlAdapter listInformationlAdapter;
    private ImageButton informationBackButton;
    private ScrollView scrollView;
    private boolean isImageVisible = true;
    private ImageButton information_back_button;
    private LinearLayout header_title;
    private ImageView company_logo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_company_informations);

        // Khởi tạo các view với ID chính xác
        informationBackButton = findViewById(R.id.information_back_button);
        scrollView = findViewById(R.id.scrollView);
        company_logo = findViewById(R.id.company_logo); // Đảm bảo ID chính xác
        header_title = findViewById(R.id.header_title);

        // Đảm bảo các view không phải là null trước khi sử dụng
        if (informationBackButton == null || scrollView == null || company_logo == null || header_title == null) {
            Log.e("CompanyInformationsActivity", "Some views are not properly initialized");
            return;
        }

        // Thêm padding cho View chính với Insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Ẩn tiêu đề ban đầu
        header_title.setVisibility(View.GONE);
        isImageVisible = true;

        // Thêm OnScrollChangedListener vào ScrollView
        scrollView.getViewTreeObserver().addOnScrollChangedListener(() -> {
            // Kiểm tra xem company_logo có nằm trong vùng nhìn thấy không
            if (isViewVisible(company_logo)) {
                if (!isImageVisible) {
                    Log.d("ButtonVisibility", "Button is visible again!");
                    // Nút đã xuất hiện trở lại
                    header_title.setVisibility(View.GONE);
                    isImageVisible = true;
                }
            } else {
                if (isImageVisible) {
                    Log.d("ButtonVisibility", "Button is out of screen!");
                    // Nút đã bị kéo ra khỏi màn hình
                    header_title.setVisibility(View.VISIBLE);
                    isImageVisible = false;
                }
            }
        });

        // Xử lý sự kiện nhấp vào nút quay lại
        informationBackButton.setOnClickListener(v -> finish());
    }

    private boolean isViewVisible(View view) {
        if (view == null) {
            return false;
        }
        Rect rect = new Rect();
        return view.getLocalVisibleRect(rect);
    }
}
