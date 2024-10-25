package com.example.topcv;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.topcv.API.ApiUserService;
import com.example.topcv.model.Applicant;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class InformationActivity extends AppCompatActivity {

    private EditText nameEditText;
    private EditText phoneEditText;
    private Button submitButton;

    private int userId; // Biến lưu ID người dùng

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);

        nameEditText = findViewById(R.id.editTextName);
        phoneEditText = findViewById(R.id.editTextPhone);
        submitButton = findViewById(R.id.Submit);

        // Nhận dữ liệu từ Intent
        Intent intent = getIntent();
        String username = intent.getStringExtra("username"); // Tên tài khoản
        String password = intent.getStringExtra("password"); // Mật khẩu

        // Điền dữ liệu vào EditText nếu cần
        if (username != null) {
            nameEditText.setText(username); // Có thể muốn sử dụng tên tài khoản
        }

        // Nhận ID người dùng từ Intent
        userId = intent.getIntExtra("userId", -1);

        submitButton.setOnClickListener(v -> submitApplicant());
    }

    private void submitApplicant() {
        String name = nameEditText.getText().toString().trim();
        String phone = phoneEditText.getText().toString().trim();

        if (name.isEmpty() || phone.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập cả tên và số điện thoại.", Toast.LENGTH_SHORT).show();
            return;
        }

        Log.d("Applicant Info", "Name: " + name + ", Phone: " + phone);

        // Ghi lại tên và số điện thoại vào Intent để sử dụng sau này
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("applicantName", name);
        intent.putExtra("phoneNumber", phone);
        startActivity(intent);
        finish(); // Kết thúc Activity hiện tại

        // Gửi thông tin đến server
        sendApplicantData(name, phone);
    }

    private void sendApplicantData(String name, String phone) {
        Applicant newApplicant = new Applicant();
        newApplicant.setApplicant_Name(name);
        newApplicant.setPhone_Number(phone);

        ApiUserService.apiUserService.addApplicant(newApplicant)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    if (response.isSuccessful() && response.body() != null) {
                        Toast.makeText(this, "Đã thêm ứng viên thành công!", Toast.LENGTH_SHORT).show();

                        // Chuyển sang MainActivity
                        Intent intent = new Intent(this, MainActivity.class);
                        intent.putExtra("applicantName", name);
                        intent.putExtra("phoneNumber", phone);
                        startActivity(intent);
                        finish();
                    } else {
                        String errorMessage = response.errorBody() != null ? response.errorBody().string() : response.message();
                        Log.e("API Error", "Error: " + errorMessage);
                        Toast.makeText(this, "Không thể thêm ứng viên: " + errorMessage, Toast.LENGTH_SHORT).show();
                    }
                }, throwable -> {
                    Log.e("API Error", "Error: " + throwable.getMessage());
                    Toast.makeText(this, "Lỗi kết nối: " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
