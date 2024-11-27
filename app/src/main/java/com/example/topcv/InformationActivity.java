package com.example.topcv;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.topcv.api.ApiApplicantService;
import com.example.topcv.model.Applicant;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class InformationActivity extends AppCompatActivity {

    private EditText nameEditText;
    private EditText phoneEditText;
    private EditText editTextJobDesire;
    private EditText editTextWorkingLocationDesire;
    private EditText editTextWorkingExperience;

    private Button submitButton;

    private int id_User;

    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);

        setWidget();

        setClick();

    }

    private void setClick(){
        submitButton.setOnClickListener(v -> submitApplicant());
    }

    private void setWidget(){
        nameEditText = findViewById(R.id.editTextName);
        phoneEditText = findViewById(R.id.editTextPhone);
        submitButton = findViewById(R.id.Submit);
        editTextJobDesire = findViewById(R.id.editTextJobDesire);
        editTextWorkingLocationDesire = findViewById(R.id.editTextWorkingLocationDesire);
        editTextWorkingExperience = findViewById(R.id.editTextWorkingExperience);
        username = getIntent().getStringExtra("username");
        id_User = getIntent().getIntExtra("user_id", 0);
        if (username != null) {
            nameEditText.setText(username);
        }
    }

    private void submitApplicant() {
        String name = nameEditText.getText().toString().trim();
        String phone = phoneEditText.getText().toString().trim();
        String job_desire = editTextJobDesire.getText().toString().trim();
        String working_location = editTextWorkingLocationDesire.getText().toString().trim();
        String working_experience = editTextWorkingExperience.getText().toString().trim();

        if (name.isEmpty() || phone.isEmpty()) {
            Toast.makeText(this, "Please enter both name and phone number.", Toast.LENGTH_SHORT).show();
            return;
        }

        sendApplicantData(name, phone,job_desire,working_location,working_experience);
    }

    @SuppressLint("CheckResult")
    private void sendApplicantData(String name, String phone, String job, String location, String experience) {
        if (id_User <= 0) {
            Toast.makeText(this, "Invalid user ID", Toast.LENGTH_SHORT).show();
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
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        response -> {
                            Intent intent = new Intent(this, MainActivity.class);
                            intent.putExtra("user_id", id_User);
                            startActivity(intent);
                            finish();
                        },
                        throwable -> {
                            Toast.makeText(this, "Có lỗi xảy ra: " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                );
        finish();
    }
}
