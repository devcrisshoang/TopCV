package com.example.topcv;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.topcv.api.ApiApplicantService;
import com.example.topcv.api.ApiUserService;
import com.example.topcv.model.Applicant;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class InformationActivity extends AppCompatActivity {

    private EditText nameEditText;
    private EditText phoneEditText;
    private Button submitButton;

    private int id_User; // Biến lưu ID người dùng
    private String username; // Biến lưu tên người dùng

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);

        nameEditText = findViewById(R.id.editTextName);
        phoneEditText = findViewById(R.id.editTextPhone);
        submitButton = findViewById(R.id.Submit);

        // Nhận dữ liệu từ Intent
        Intent intent = getIntent();
        username = intent.getStringExtra("username"); // Tên tài khoản
        id_User = intent.getIntExtra("user_id", -1); // Nhận ID người dùng

        // Điền dữ liệu vào EditText nếu cần
        if (username != null) {
            nameEditText.setText(username);
        }

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

        // Gửi thông tin đến server và chuyển Activity khi thành công
        sendApplicantData(name, phone);
    }

    private void sendApplicantData(String name, String phone) {
        if (id_User <= 0) {
            Toast.makeText(this, "ID người dùng không hợp lệ", Toast.LENGTH_SHORT).show();
            return;
        }

        Applicant newApplicant = new Applicant();
        newApplicant.setApplicant_Name(name);
        newApplicant.setPhone_Number(phone);
        Log.d("userId", "userId: " + id_User);
        newApplicant.setId_User(id_User);

        ApiApplicantService.apiApplicantService.addApplicant(newApplicant)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    if (response.isSuccessful() && response.body() != null) {
                        Toast.makeText(this, "Đã thêm ứng viên thành công!", Toast.LENGTH_SHORT).show();

                        // Chuyển sang MainActivity với thông tin applicant
                        Intent intent = new Intent(this, MainActivity.class);
                        intent.putExtra("applicantName", name);
                        intent.putExtra("phoneNumber", phone);
                        intent.putExtra("user_id", id_User);
                        startActivity(intent);
                        finish();
                    } else {
                        // Xử lý lỗi
                        String errorMessage = response.errorBody() != null ? response.errorBody().string() : response.message();
                        Log.e("API Error", "Error: " + errorMessage);
                        Toast.makeText(this, "Thêm ứng viên thất bại: " + errorMessage, Toast.LENGTH_SHORT).show();
                    }
                }, throwable -> {
                    Log.e("API Error", "Error: " + throwable.getMessage());
                    Toast.makeText(this, "Có lỗi xảy ra: " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
