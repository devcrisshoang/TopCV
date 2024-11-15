package com.example.topcv;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.topcv.api.ApiApplicantService;
import com.example.topcv.api.ApiResumeService;
import com.example.topcv.api.ApiUserService;
import com.example.topcv.model.Applicant;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class InformationActivity extends AppCompatActivity {

    private EditText nameEditText;
    private EditText phoneEditText;
    private Button submitButton;
    private EditText editTextJobDesire;
    private EditText editTextWorkingLocationDesire;
    private EditText editTextWorkingExperience;

    private int id_User; // Biến lưu ID người dùng
    private String username; // Biến lưu tên người dùng

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);

        nameEditText = findViewById(R.id.editTextName);
        phoneEditText = findViewById(R.id.editTextPhone);
        submitButton = findViewById(R.id.Submit);
        editTextJobDesire = findViewById(R.id.editTextJobDesire);
        editTextWorkingLocationDesire = findViewById(R.id.editTextWorkingLocationDesire);
        editTextWorkingExperience = findViewById(R.id.editTextWorkingExperience);

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
        String job_desire = editTextJobDesire.getText().toString().trim();
        String working_location = editTextWorkingLocationDesire.getText().toString().trim();
        String working_experience = editTextWorkingExperience.getText().toString().trim();

        if (name.isEmpty() || phone.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập cả tên và số điện thoại.", Toast.LENGTH_SHORT).show();
            return;
        }

        Log.d("Applicant Info", "Name: " + name + ", Phone: " + phone);

        // Gửi thông tin đến server và chuyển Activity khi thành công
        sendApplicantData(name, phone,job_desire,working_location,working_experience);
    }

    private void sendApplicantData(String name, String phone, String job, String location, String experience) {
        if (id_User <= 0) {
            Toast.makeText(this, "ID người dùng không hợp lệ", Toast.LENGTH_SHORT).show();
            return;
        }

        Applicant newApplicant = new Applicant();
        newApplicant.setApplicantName(name);
        newApplicant.setPhoneNumber(phone);
        newApplicant.setJobDesire(job);
        newApplicant.setWorkingLocationDesire(location);
        newApplicant.setWorkingExperience(experience);
        newApplicant.setIs_Registered(true);
        Log.d("userId", "userId: " + id_User);
        newApplicant.setiD_User(id_User);

        ApiApplicantService.ApiApplicantService.createApplicant(newApplicant)
                .subscribeOn(Schedulers.io())  // Chạy trên luồng nền
                .observeOn(AndroidSchedulers.mainThread())  // Quan sát kết quả trên luồng chính
                .subscribe(
                        response -> {
                            // Xử lý khi thành công
                            Toast.makeText(this, "Applicant đã được tạo thành công!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(this, MainActivity.class);
                            intent.putExtra("user_id", id_User);
                            startActivity(intent);
                            finish();
                        },
                        throwable -> {
                            // Xử lý khi có lỗi
                            Toast.makeText(this, "Có lỗi xảy ra: " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                );
        finish();
    }
}
