package com.example.topcv;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.example.topcv.api.ApiUserService;
import com.example.topcv.model.User;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class PolicyActivity extends AppCompatActivity {
    private Button Register_Button;

    private ImageView iconNumber2;

    private ScrollView scrollView;

    private View linearLayoutAgreement;

    private CheckBox agreeCheckBox;

    private boolean isAgreementVisible = false;

    private String username;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_policy);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setWidget();

        setClick();

    }

    private void registerButton(){
        if (!agreeCheckBox.isChecked()) {
            Toast.makeText(PolicyActivity.this, "You have not agreed to our policy.", Toast.LENGTH_SHORT).show();
            return;
        }
        createUserAndRedirect();
    }

    private void setClick(){
        Register_Button.setOnClickListener(view -> registerButton());
    }

    private void setWidget(){
        Register_Button = findViewById(R.id.Register_Button);
        iconNumber2 = findViewById(R.id.iconNumber2);
        scrollView = findViewById(R.id.scrollView);
        linearLayoutAgreement = findViewById(R.id.linearLayoutAgreement);
        agreeCheckBox = findViewById(R.id.agreeCheckBox);
        username = getIntent().getStringExtra("username");
        password = getIntent().getStringExtra("password");
        if (getIntent().getBooleanExtra("isSignUpButtonClicked", false)) {
            iconNumber2.setColorFilter(getResources().getColor(R.color.green_color), android.graphics.PorterDuff.Mode.SRC_IN);
        }
        scrollView.setOnScrollChangeListener((v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            if (scrollY > oldScrollY && !isAgreementVisible) {
                linearLayoutAgreement.setVisibility(View.VISIBLE);
                isAgreementVisible = true;
            } else if (scrollY < oldScrollY && isAgreementVisible) {
                linearLayoutAgreement.setVisibility(View.GONE);
                isAgreementVisible = false;
            }
        });
    }

    @SuppressLint("CheckResult")
    private void createUserAndRedirect() {
        User newUser = new User(username, password, "", "", null,true,false);
        ApiUserService.apiUserService.createUser(newUser)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(user -> {
                    Toast.makeText(PolicyActivity.this, "User created successfully!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(PolicyActivity.this, LoginActivity.class);
                    intent.putExtra("username", username);
                    intent.putExtra("password", password);
                    startActivity(intent);
                    finish();
                }, throwable -> {
                    Toast.makeText(PolicyActivity.this, "Failed to create user: " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}