package com.example.topcv;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SignUpActivity extends AppCompatActivity {
    private Button Next_Button;
    private EditText usernameInput, passwordInput, confirmPasswordInput; // Thêm EditText cho tên tài khoản, mật khẩu và xác nhận mật khẩu
    private ImageView iconNumber1;
    private ImageView iconNumber2;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Next_Button = findViewById(R.id.Next_Button);
        usernameInput = findViewById(R.id.txtDangkymail);
        passwordInput = findViewById(R.id.txtDangkymatkhau);
        confirmPasswordInput = findViewById(R.id.txtConfirmPassword);
        iconNumber1 = findViewById(R.id.iconNumber1);
        iconNumber2 = findViewById(R.id.iconNumber2);

        Intent intent = getIntent();
        if (intent.getBooleanExtra("isSignUpButtonClicked", false)) {
            iconNumber1.setColorFilter(getResources().getColor(R.color.green_color), android.graphics.PorterDuff.Mode.SRC_IN);
        }

        Next_Button.setOnClickListener(view -> {
            String username = usernameInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();
            String confirmPassword = confirmPasswordInput.getText().toString().trim();

            // Kiểm tra tên tài khoản đã tồn tại hay chưa
            if (isUsernameExists(username)) {
                Toast.makeText(SignUpActivity.this, "Tên tài khoản đã tồn tại. Vui lòng chọn tên khác.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!password.equals(confirmPassword)) {
                Toast.makeText(SignUpActivity.this, "Mật khẩu không khớp. Vui lòng thử lại.", Toast.LENGTH_SHORT).show();
                return;
            }

            saveUserCredentials(username, password);

            Intent policyIntent = new Intent(SignUpActivity.this, PolicyActivity.class);
            policyIntent.putExtra("isSignUpButtonClicked", true);
            policyIntent.putExtra("isPolicyButtonClicked", true);
            startActivity(policyIntent);
            finish();
        });
    }

    private boolean isUsernameExists(String username) {
        SharedPreferences sharedPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
        return sharedPreferences.contains(username + "_password"); // Kiểm tra xem tên tài khoản đã tồn tại trong SharedPreferences hay chưa
    }

    private void saveUserCredentials(String username, String password) {
        SharedPreferences sharedPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(username + "_password", password); // Lưu mật khẩu với key là username_password
        editor.putInt(username + "_login_count", 0); // Khởi tạo số lần đăng nhập là 0
        editor.apply();
    }
}
