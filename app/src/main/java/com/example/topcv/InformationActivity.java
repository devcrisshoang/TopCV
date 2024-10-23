package com.example.topcv;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);

        nameEditText = findViewById(R.id.editTextName);
        phoneEditText = findViewById(R.id.editTextPhone);
        submitButton = findViewById(R.id.Submit);

        // Lấy tên người dùng từ SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("TopCVPrefs", MODE_PRIVATE);
        String username = sharedPreferences.getString("currentUser", null);

        // Kiểm tra nếu người dùng đã nhập thông tin trước đó
        if (isUserInfoCompleted(username)) {
            // Nếu đã nhập thông tin, chuyển ngay đến MainActivity
            startActivity(new Intent(InformationActivity.this, MainActivity.class));
            finish();
        }

        submitButton.setOnClickListener(v -> submitApplicant(username));
    }

    private void submitApplicant(String username) {
        String name = nameEditText.getText().toString().trim();
        String phone = phoneEditText.getText().toString().trim();

        // Kiểm tra xem người dùng đã nhập tên và số điện thoại chưa
        if (name.isEmpty() || phone.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập cả tên và số điện thoại.", Toast.LENGTH_SHORT).show();
            return;
        }

        Applicant newApplicant = new Applicant();
        newApplicant.setApplicant_Name(name); // Tên người nộp đơn
        newApplicant.setPhone_Number(phone); // Số điện thoại
        newApplicant.setEmail(null); // Đặt các trường còn lại thành null
        newApplicant.setJob_Desire(null);
        newApplicant.setWorking_Location_Desire(null);
        newApplicant.setWorking_Experience(null);

        // Gọi API để thêm ứng viên
        ApiUserService.apiUserService.addApplicant(newApplicant)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    if (response.isSuccessful()) {
                        Toast.makeText(this, "Đã thêm ứng viên thành công!", Toast.LENGTH_SHORT).show();

                        // Lưu trạng thái thông tin người dùng đã được nhập
                        saveUserInformationStatus(username);

                        // Chuyển đến MainActivity
                        startActivity(new Intent(InformationActivity.this, MainActivity.class));
                        finish();
                    } else {
                        Toast.makeText(this, "Không thể thêm ứng viên: " + response.message(), Toast.LENGTH_SHORT).show();
                    }
                }, throwable -> {
                    Toast.makeText(this, "Lỗi: " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    // Lưu trạng thái đã nhập thông tin của người dùng
    private void saveUserInformationStatus(String username) {
        SharedPreferences sharedPreferences = getSharedPreferences("TopCVPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(username + "_isInfoCompleted", true); // Lưu trạng thái đã nhập thông tin
        editor.apply();
    }

    // Kiểm tra xem người dùng đã nhập thông tin hay chưa
    private boolean isUserInfoCompleted(String username) {
        SharedPreferences sharedPreferences = getSharedPreferences("TopCVPrefs", MODE_PRIVATE);
        return sharedPreferences.getBoolean(username + "_isInfoCompleted", false); // Mặc định là false nếu chưa nhập
    }
}
