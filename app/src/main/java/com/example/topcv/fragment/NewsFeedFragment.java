package com.example.topcv.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.topcv.ArticleActivity;
import com.example.topcv.CompanyActivity;
import com.example.topcv.JobActivity;
import com.example.topcv.R;
import com.example.topcv.SeeAllActivity;
import com.example.topcv.adapter.ArticleAdapter;
import com.example.topcv.adapter.CompanyTopAdapter;
import com.example.topcv.adapter.TheBestJobAdapter;
import com.example.topcv.adapter.WorkAdapter;
import com.example.topcv.api.ApiApplicantService;
import com.example.topcv.api.ApiArticleService;
import com.example.topcv.api.ApiCompanyService;
import com.example.topcv.api.ApiJobService;
import com.example.topcv.model.Applicant;
import com.example.topcv.model.Article;
import com.example.topcv.model.Company;
import com.example.topcv.model.Job;
import java.util.ArrayList;
import java.util.List;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class NewsFeedFragment extends Fragment {
    private ArticleAdapter articleAdapter;
    private CompanyTopAdapter companyTopAdapter;
    private TheBestJobAdapter theBestJobAdapter;
    private WorkAdapter workAdapter;

    private RecyclerView recyclerviewSuitableJob, recyclerviewBestJob, recyclerviewInterestingJob, recyclerviewTopCompanies, recyclerviewArticles;
    private Button btnViewAllSuitableJob, btnViewAllBestJob, btnViewAllInterestingJob, btnViewAllTopCompany, btnViewAllArticles;

    private TextView suggestJobTextView, bestJobTextView, interestingJobTextView, topCompanyTextView, articleTextView;

    private List<Job> workList = new ArrayList<>();
    private List<Job> bestJobList = new ArrayList<>();
    private List<Job> interestingList = new ArrayList<>();
    private List<Company> companyList = new ArrayList<>();
    private List<Article> articleList = new ArrayList<>();

    private Applicant applicant = new Applicant();
    private int userId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news_feed, container, false);

        initViews(view);

        initAdapters();

        initRecyclerViews();

        fetchData();

        setClickListeners();

        return view;
    }

    private void initViews(View view) {
        // Initialize views
        recyclerviewSuitableJob = view.findViewById(R.id.recyclerview_thesuitablejob);
        recyclerviewBestJob = view.findViewById(R.id.recyclerview_thebestjob);
        recyclerviewInterestingJob = view.findViewById(R.id.recyclerview_theinterestingjob);
        recyclerviewTopCompanies = view.findViewById(R.id.recyclerview_thetopcompanies);
        recyclerviewArticles = view.findViewById(R.id.recyclerview_thearticle);

        btnViewAllSuitableJob = view.findViewById(R.id.view_all_suitable_job);
        btnViewAllBestJob = view.findViewById(R.id.view_all_best_job);
        btnViewAllInterestingJob = view.findViewById(R.id.view_all_interesting_job);
        btnViewAllTopCompany = view.findViewById(R.id.view_all_top_company);
        btnViewAllArticles = view.findViewById(R.id.view_all_article);

        suggestJobTextView = view.findViewById(R.id.suggest_job_textview);
        bestJobTextView = view.findViewById(R.id.best_job_textview);
        interestingJobTextView = view.findViewById(R.id.interesting_job_textview);
        topCompanyTextView = view.findViewById(R.id.top_company_textview);
        articleTextView = view.findViewById(R.id.article_textview);

        // Get user ID from arguments
        userId = getArguments().getInt("user_id", 0);
    }

    private void initAdapters() {
        // Initialize adapters
        articleAdapter = new ArticleAdapter();
        companyTopAdapter = new CompanyTopAdapter();
        theBestJobAdapter = new TheBestJobAdapter();
        workAdapter = new WorkAdapter();
    }

    private void initRecyclerViews() {
        // Set up RecyclerViews
        setupRecyclerView(recyclerviewSuitableJob, workAdapter, false);
        setupRecyclerView(recyclerviewBestJob, theBestJobAdapter, true);
        setupRecyclerView(recyclerviewInterestingJob, theBestJobAdapter, false);
        setupRecyclerView(recyclerviewTopCompanies, companyTopAdapter, false, 2);
        setupRecyclerView(recyclerviewArticles, articleAdapter, true);
    }

    private void setupRecyclerView(RecyclerView recyclerView, RecyclerView.Adapter<?> adapter, boolean isHorizontal) {
        setupRecyclerView(recyclerView, adapter, isHorizontal, 1);
    }

    private void setupRecyclerView(RecyclerView recyclerView, RecyclerView.Adapter<?> adapter, boolean isHorizontal, int spanCount) {
        if (spanCount > 1) {
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), spanCount));
        } else {
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), isHorizontal ? LinearLayoutManager.HORIZONTAL : LinearLayoutManager.VERTICAL, false));
        }
        recyclerView.setFocusable(false);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(adapter);
    }

    private void fetchData() {
        fetchApplicantData(userId);
        fetchJobData();
        fetchCompanyData();
        fetchArticleData();
    }

    private void fetchApplicantData(int userId) {
        ApiApplicantService.ApiApplicantService.getApplicantByUserId(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> applicant = response != null ? response : new Applicant(),
                        throwable -> Log.e("API Error", "Error fetching applicant: " + throwable.getMessage()));
    }

    private void fetchJobData() {
        ApiJobService.ApiJobService.getAllJobs()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::processJobData,
                        throwable -> Log.e("API Error", "Error fetching jobs: " + throwable.getMessage()));
    }

    private void processJobData(List<Job> jobs) {
        if (jobs.isEmpty()) {
            Log.d("NewsFeedFragment", "No jobs found");
            return;
        }

        String jobDesire = applicant.getJobDesire() != null ? applicant.getJobDesire().toLowerCase() : "";
        String locationDesire = applicant.getWorkingLocationDesire() != null ? applicant.getWorkingLocationDesire().toLowerCase() : "";
        String experienceDesire = applicant.getWorkingExperience() != null ? applicant.getWorkingExperience().toLowerCase() : "";

        workList.clear();
        bestJobList.clear();
        interestingList.clear();

        for (Job job : jobs) {
            String jobName = job.getJobName() != null ? job.getJobName().toLowerCase() : "";
            String location = job.getLocation() != null ? job.getLocation().toLowerCase() : "";
            String experience = job.getExperience() != null ? job.getExperience().toLowerCase() : "";

            int maxSizeSuitableJob = 5;

            if (workList.size() <= maxSizeSuitableJob && jobName.contains(jobDesire) && location.contains(locationDesire) && experience.contains(experienceDesire)) {
                workList.add(job);
            }

            int maxSizeBestJob = 10;

            if (bestJobList.size() <= maxSizeBestJob && job.getSalary() > 1000 && jobName.contains(jobDesire)) {
                bestJobList.add(job);
            }

            int maxSizeInterestingJob = 5;

            if(interestingList.size() <= maxSizeInterestingJob && job.getSalary() > 3000){
                interestingList.add(job);
            }
        }
        updateRecyclerViews();
    }

    private void updateRecyclerViews() {
        workAdapter.setData(workList);
        workAdapter.notifyDataSetChanged();

        theBestJobAdapter.setData(bestJobList);
        theBestJobAdapter.notifyDataSetChanged();

    }

    private void fetchCompanyData() {
        ApiCompanyService.ApiCompanyService.getAllCompany()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(companies -> {
                    companyList = companies.size() > 4 ? companies.subList(0, 4) : companies;
                    companyTopAdapter.setData(companyList);
                    companyTopAdapter.notifyDataSetChanged();
                }, throwable -> Log.e("API Error", "Error fetching companies: " + throwable.getMessage()));
    }

    private void fetchArticleData() {
        ApiArticleService.ApiArticleService.getAllArticle()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(articles -> {
                    articleList = articles.size() > 10 ? articles.subList(0, 10) : articles;
                    articleAdapter.setData(articleList);
                    articleAdapter.notifyDataSetChanged();
                }, throwable -> Log.e("API Error", "Error fetching articles: " + throwable.getMessage()));
    }

    private void setClickListeners() {
        btnViewAllSuitableJob.setOnClickListener(v -> navigateToSeeAll(suggestJobTextView.getText().toString(), "suggestText"));
        btnViewAllBestJob.setOnClickListener(v -> navigateToSeeAll(bestJobTextView.getText().toString(), "bestText"));
        btnViewAllInterestingJob.setOnClickListener(v -> navigateToSeeAll(interestingJobTextView.getText().toString(), "interestingText"));
        btnViewAllTopCompany.setOnClickListener(v -> navigateToSeeAll(topCompanyTextView.getText().toString(), "companyText"));
        btnViewAllArticles.setOnClickListener(v -> navigateToSeeAll(articleTextView.getText().toString(), "articleText"));
        articleAdapter.setOnItemClickListener(article -> {
            Intent intent = new Intent(getContext(), ArticleActivity.class);
            intent.putExtra("article_id", article.getId());
            startActivity(intent);
        });

        workAdapter.setOnItemClickListener(job -> {
            Intent intent = new Intent(getContext(), JobActivity.class);
            intent.putExtra("job_id", job.getId());
            intent.putExtra("id_User", userId);
            startActivity(intent);
        });

        theBestJobAdapter.setOnItemClickListener(job -> {
            Intent intent = new Intent(getContext(), JobActivity.class);
            intent.putExtra("best_id", job.getId());
            intent.putExtra("id_User", userId);
            startActivity(intent);
        });

        companyTopAdapter.setOnItemClickListener(company -> {
            Intent intent = new Intent(getContext(), CompanyActivity.class);
            intent.putExtra("company_id", company.getId());
            startActivity(intent);
        });
    }

    private void navigateToSeeAll(String text, String extraKey) {
        Intent intent = new Intent(getContext(), SeeAllActivity.class);
        intent.putExtra(extraKey, text);
        startActivity(intent);
    }
}

