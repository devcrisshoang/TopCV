package com.example.topcv;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.example.topcv.api.ApiApplicantService;
import com.example.topcv.api.ApiJobDetailService;
import com.example.topcv.api.ApiJobService;
import com.example.topcv.model.Job;
import com.example.topcv.model.JobDetail;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.List;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.observers.DisposableObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class JobActivity extends AppCompatActivity {

    private ScrollView scrollView;

    private boolean isImageVisible = true;

    private LinearLayout header_title;

    private ImageView company_logo;

    private ImageButton back_button;
    private ImageButton informationBackButton;

    private int jobId;
    private int companyId;
    private int id_User;

    private Button apply_button;

    private TextView working_time;
    private TextView working_place;
    private TextView interesting;
    private TextView conditions;
    private TextView description;
    private TextView deadline;
    private TextView position;
    private TextView gender;
    private TextView number_of_people;
    private TextView work_method;
    private TextView experience_detail;
    private TextView salary;
    private TextView location;
    private TextView experience;
    private TextView company_name;
    private TextView job_name;
    private TextView work_name;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_company_informations);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setWidget();

        setClick();

    }

    @SuppressLint("CheckResult")
    private void applyButton(){
        ApiApplicantService.ApiApplicantService.getApplicantByUserId(id_User)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        applicant -> {
                            if (applicant != null) {
                                Intent intent = new Intent(this, ApplyActivity.class);
                                intent.putExtra("applicant_id",applicant.getId());
                                intent.putExtra("jobId",jobId);
                                intent.putExtra("job_name",job_name.getText().toString());
                                startActivity(intent);
                                Log.e("ProfileFragment","ID: " + applicant.getId());
                            } else {
                                Toast.makeText(this, "Applicant null", Toast.LENGTH_SHORT).show();
                            }
                        },
                        throwable -> {
                            Log.e("MessengerAdapter", "Error fetching applicant: " + throwable.getMessage());
                            Toast.makeText(this, "Failed to load applicant", Toast.LENGTH_SHORT).show();
                        }
                );
    }

    private void setClick(){

        apply_button.setOnClickListener(view -> applyButton());

        informationBackButton.setOnClickListener(v -> finish());

        back_button.setOnClickListener(v -> finish());
    }

    private void setWidget() {
        informationBackButton = findViewById(R.id.information_back_button);
        back_button = findViewById(R.id.back_button);
        scrollView = findViewById(R.id.scrollView);
        company_logo = findViewById(R.id.company_logo);
        header_title = findViewById(R.id.header_title);
        apply_button = findViewById(R.id.apply_button);
        working_time = findViewById(R.id.working_time);
        working_place = findViewById(R.id.working_place);
        interesting = findViewById(R.id.interesting);
        conditions = findViewById(R.id.conditions);
        description = findViewById(R.id.description);
        deadline = findViewById(R.id.deadline);
        position = findViewById(R.id.position);
        gender = findViewById(R.id.gender);
        number_of_people = findViewById(R.id.number_of_people);
        work_method = findViewById(R.id.work_method);
        experience_detail = findViewById(R.id.experience_detail);
        salary = findViewById(R.id.salary);
        location = findViewById(R.id.location);
        experience = findViewById(R.id.experience);
        company_name = findViewById(R.id.company_name);
        job_name = findViewById(R.id.job_name);
        work_name = findViewById(R.id.work_name);
        id_User = getIntent().getIntExtra("id_User",0);
        jobId = getIntent().getIntExtra("job_id", 0);
        int bestId = getIntent().getIntExtra("best_id", 0);
        companyId = getIntent().getIntExtra("company_id", 0);
        if (jobId != 0){
            getJobs(jobId);
            getJobDetails(jobId);
        }
        else if(bestId != 0){
            getJobs(bestId);
            getJobDetails(bestId);
        }
        header_title.setVisibility(View.GONE);
        isImageVisible = true;
        scrollView.getViewTreeObserver().addOnScrollChangedListener(() -> {
            if (isViewVisible(company_logo)) {
                if (!isImageVisible) {
                    Log.d("ButtonVisibility", "Button is visible again!");
                    header_title.setVisibility(View.GONE);
                    isImageVisible = true;
                }
            } else {
                if (isImageVisible) {
                    Log.d("ButtonVisibility", "Button is out of screen!");
                    header_title.setVisibility(View.VISIBLE);
                    isImageVisible = false;
                }
            }
        });
    }

    private void getJobs(int jobId) {
        compositeDisposable.add(
                ApiJobService.ApiJobService.getJobById(jobId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableObserver<Job>() {
                            @Override
                            public void onNext(@io.reactivex.rxjava3.annotations.NonNull Job job) {
                                bindJobDataToViews(job);
                            }

                            @Override
                            public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                                Toast.makeText(JobActivity.this, "Failed to load job details", Toast.LENGTH_SHORT).show();
                                Log.e("API Error", e.getMessage());
                            }

                            @Override
                            public void onComplete() {

                            }
                        })
        );
    }

    private void getJobDetails(int jobId) {
        compositeDisposable.add(
                ApiJobDetailService.ApiJobDetailService.getJobDetailById(jobId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableObserver<List<JobDetail>>() {
                            @Override
                            public void onNext(@io.reactivex.rxjava3.annotations.NonNull List<JobDetail> jobDetails) {
                                if (!jobDetails.isEmpty()) {
                                    bindJobDetailDataToViews(jobDetails.get(0));
                                } else {
                                    Toast.makeText(JobActivity.this, "No job details found", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                                Toast.makeText(JobActivity.this, "Failed to load job details", Toast.LENGTH_SHORT).show();
                                Log.e("API Error", e.getMessage());
                            }

                            @Override
                            public void onComplete() {

                            }
                        })
        );
    }

    private void bindJobDetailDataToViews(JobDetail jobDetail) {
        working_time.setText(jobDetail.getWorkingTime());
        interesting.setText(jobDetail.getBenefit());
        conditions.setText(jobDetail.getSkillRequire());
        description.setText(jobDetail.getJobDescription());
        position.setText(jobDetail.getWorkingPosition());
        gender.setText(jobDetail.getGenderRequire());
        number_of_people.setText(jobDetail.getNumberOfPeople());
        work_method.setText(jobDetail.getWorkingMethod());
    }

    private void bindJobDataToViews(Job job) {
        company_logo.setImageResource(R.drawable.workplace_ic);
        job_name.setText(job.getJobName());
        company_name.setText(job.getCompanyName());
        experience.setText(job.getExperience());
        working_place.setText(job.getLocation());
        salary.setText(String.valueOf(job.getSalary()));
        location.setText(job.getLocation());
        experience_detail.setText(job.getExperience());
        String applicationDateStr = job.getApplicationDate();
        LocalDate applicationDate = LocalDate.parse(applicationDateStr.substring(0, 10));
        LocalDate deadlineDate = applicationDate.plusDays(30);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        deadline.setText(formatter.format(deadlineDate));

        LocalDate today = LocalDate.now();
        Period period = Period.between(today, deadlineDate);
        String remainingDays = String.valueOf(period.getDays());
        deadline.setText(remainingDays + " days remaining");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }

    private boolean isViewVisible(View view) {
        Rect rect = new Rect();
        view.getGlobalVisibleRect(rect);
        return rect.height() > 0;
    }
}
