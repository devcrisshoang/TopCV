package com.example.topcv;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.topcv.API.ApiSortOfUser; // Thêm import cho ApiSortOfUser
import com.example.topcv.API.ApiUserService;
import com.example.topcv.model.SortOfUser; // Thêm import cho SortOfUser
import com.example.topcv.model.User;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class PolicyActivity extends AppCompatActivity {
    private Button Register_Button;
    private ImageView iconNumber2;
    private ScrollView scrollView;
    private View linearLayoutAgreement;
    private CheckBox agreeCheckBox;
    private ApiUserService apiUserService;

    private boolean isAgreementVisible = false;
    private String username;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_policy);

        Register_Button = findViewById(R.id.Register_Button);
        iconNumber2 = findViewById(R.id.iconNumber2);
        scrollView = findViewById(R.id.scrollView);
        linearLayoutAgreement = findViewById(R.id.linearLayoutAgreement);
        agreeCheckBox = findViewById(R.id.agreeCheckBox);

        // Nhận username và password từ Intent
        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        password = intent.getStringExtra("password");
        if (intent.getBooleanExtra("isSignUpButtonClicked", false)) {
            iconNumber2.setColorFilter(getResources().getColor(R.color.green_color), android.graphics.PorterDuff.Mode.SRC_IN);
        }
        // Lắng nghe sự kiện cuộn của ScrollView
        scrollView.setOnScrollChangeListener((v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            if (scrollY > oldScrollY && !isAgreementVisible) {
                linearLayoutAgreement.setVisibility(View.VISIBLE);
                isAgreementVisible = true;
            } else if (scrollY < oldScrollY && isAgreementVisible) {
                linearLayoutAgreement.setVisibility(View.GONE);
                isAgreementVisible = false;
            }
        });

        Register_Button.setOnClickListener(view -> {
            // Kiểm tra người dùng có đồng ý chính sách không
            if (!agreeCheckBox.isChecked()) {
                Toast.makeText(PolicyActivity.this, "Bạn chưa đồng ý với chính sách của chúng tôi.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Nếu đồng ý, tiến hành tạo tài khoản
            createUserAndRedirect();
        });
    }

    private void createUserAndRedirect() {
        // Create a new User object
        User newUser = new User(username, password, 0, 0, 0, null);

        // Call API to create User
        ApiUserService.apiUserService.createUser(newUser)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(user -> {
                    Toast.makeText(PolicyActivity.this, "Tạo người dùng thành công!", Toast.LENGTH_SHORT).show();

                    // Save User ID to SortOfUser
                    saveUserIdToSortOfUser(user.getId());
                }, throwable -> {
                    Toast.makeText(PolicyActivity.this, "Không thể tạo người dùng: " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void saveUserIdToSortOfUser(int userId) {
        SortOfUser sortOfUser = new SortOfUser();
        sortOfUser.setID_User(userId); // Save User ID

        // Call API to add SortOfUser
        ApiSortOfUser.apiSortOfUser.addSortOfUser(sortOfUser)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    if (response.isSuccessful()) {
                        Toast.makeText(PolicyActivity.this, "Lưu ID người dùng vào SortOfUser thành công!", Toast.LENGTH_SHORT).show();

                        // Lưu tên tài khoản và mật khẩu vào Intent
                        Intent intent = new Intent(PolicyActivity.this, LoginActivity.class);
                        intent.putExtra("username", username);
                        intent.putExtra("password", password);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(PolicyActivity.this, "Không thể lưu SortOfUser: " + response.message(), Toast.LENGTH_SHORT).show();
                    }
                }, throwable -> {
                    Toast.makeText(PolicyActivity.this, "Lỗi khi lưu SortOfUser: " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

}
