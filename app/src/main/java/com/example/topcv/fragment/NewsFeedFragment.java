package com.example.topcv.fragment;

import android.annotation.SuppressLint;
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
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class NewsFeedFragment extends Fragment {

    private ArticleAdapter articleAdapter;
    private CompanyTopAdapter companyTopAdapter;
    private TheBestJobAdapter theBestJobAdapter;
    private WorkAdapter workAdapter;

    private RecyclerView recyclerview_the_suitable_job;
    private RecyclerView recyclerview_the_best_job;
    private RecyclerView recyclerview_the_interesting_job;
    private RecyclerView recyclerview_the_top_companies;
    private RecyclerView recyclerview_the_article;

    private Button view_all_suitable_job;
    private Button view_all_best_job;
    private Button view_all_interesting_job;
    private Button view_all_top_company;
    private Button view_all_article;

    private TextView suggest_job_textview;
    private TextView best_job_textview;
    private TextView interesting_job_textview;
    private TextView top_company_textview;
    private TextView article_textview;

    private List<Job> workList;
    private List<Job> bestjobList;
    private List<Job> interestingList;
    private List<Company> companyList;
    private List<Article> articleList;

    private Applicant applicants;
    private int id_User;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news_feed, container, false);

        setWidget(view);

        getAPIJobData();

        getAPICompanyData();

        getAPIArticleData();

        getAPIApplicantData(id_User);

        TheSuitableJob();

        TheBestJob();

        TheInterestingJob();

        TheTopCompany();

        TheArticle();

        setClick();

        return view;
    }

    private void viewAllSuitableButton(){
        String text = suggest_job_textview.getText().toString();
        Intent intent = new Intent(getContext(), SeeAllActivity.class);
        intent.putExtra("suggestText", text);
        startActivity(intent);
    }

    private void viewAllBestButton(){
        String text = best_job_textview.getText().toString();
        Intent intent = new Intent(getContext(), SeeAllActivity.class);
        intent.putExtra("bestText", text);
        startActivity(intent);
    }

    private void viewAllInterestingButton(){
        String text = interesting_job_textview.getText().toString();
        Intent intent = new Intent(getContext(), SeeAllActivity.class);
        intent.putExtra("interestingText", text);
        startActivity(intent);
    }

    private void viewAllCompanyButton(){
        String text = top_company_textview.getText().toString();
        Intent intent = new Intent(getContext(), SeeAllActivity.class);
        intent.putExtra("companyText", text);
        startActivity(intent);
    }

    private void viewAllArticleButton(){
        String text = article_textview.getText().toString();
        Intent intent = new Intent(getContext(), SeeAllActivity.class);
        intent.putExtra("articleText", text);
        startActivity(intent);
    }

    private void setClick(){
        view_all_suitable_job.setOnClickListener(view1 -> viewAllSuitableButton());

        view_all_best_job.setOnClickListener(view1 -> viewAllBestButton());

        view_all_interesting_job.setOnClickListener(view1 -> viewAllInterestingButton());

        view_all_top_company.setOnClickListener(view1 -> viewAllCompanyButton());

        view_all_article.setOnClickListener(view1 -> viewAllArticleButton());

        articleAdapter.setOnItemClickListener(article -> {
            Intent intent = new Intent(getContext(), ArticleActivity.class);
            intent.putExtra("article_id", article.getId());
            startActivity(intent);
        });

        workAdapter.setOnItemClickListener(job -> {
            Intent intent = new Intent(getContext(), JobActivity.class);
            intent.putExtra("job_id", job.getId());
            intent.putExtra("id_User", id_User);
            startActivity(intent);
        });

        theBestJobAdapter.setOnItemClickListener(job -> {
            Intent intent = new Intent(getContext(), JobActivity.class);
            intent.putExtra("best_id", job.getId());
            intent.putExtra("id_User", id_User);
            startActivity(intent);
        });

        companyTopAdapter.setOnItemClickListener(company -> {
            Intent intent = new Intent(getContext(), CompanyActivity.class);
            intent.putExtra("company_id", company.getId());
            startActivity(intent);
        });
    }

    @SuppressLint("CheckResult")
    public void getAPIApplicantData(int UserID){
        ApiApplicantService.ApiApplicantService.getApplicantByUserId(UserID)
            .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(response -> {
                        if (response != null) {
                            applicants = response;
                        }
                    }, throwable -> Log.e("API Error", "Error fetching applicant: " + throwable.getMessage()));
        }

    public void getAPIJobData() {
        ApiJobService.ApiJobService.getAllJobs()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Job>>() {
                    @Override
                    public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {}

                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onNext(@io.reactivex.rxjava3.annotations.NonNull List<Job> jobs) {
                        Log.d("MessageActivity", "Received jobs: " + jobs.toString());

                        if (!jobs.isEmpty()) {
                            workList.clear();
                            bestjobList.clear();
                            interestingList.clear();

                            if (applicants != null) {
                                String jobDesire = applicants.getJobDesire() != null ? applicants.getJobDesire().toLowerCase() : "";
                                String locationDesire = applicants.getWorkingLocationDesire() != null ? applicants.getWorkingLocationDesire().toLowerCase() : "";

                                List<Job> work = new ArrayList<>();
                                for (Job job : jobs) {
                                    String jobName = job.getJobName() != null ? job.getJobName().toLowerCase() : "";
                                    String location = job.getLocation() != null ? job.getLocation().toLowerCase() : "";

                                    if (jobName.contains(jobDesire) && location.contains(locationDesire)) {
                                        work.add(job);
                                    }
                                }

                                List<Job> limitedWorkList = work.size() > 5 ? work.subList(0, 5) : new ArrayList<>(work);
                                workList.addAll(limitedWorkList);
                                while (workList.size() < 5 && work.size() > workList.size()) {
                                    workList.add(work.get(workList.size()));
                                }

                                List<Job> best = new ArrayList<>();
                                for (Job job : jobs) {
                                    String jobName = job.getJobName() != null ? job.getJobName().toLowerCase() : "";
                                    if (jobName.contains(jobDesire) && job.getSalary() > 1000) {
                                        best.add(job);
                                    }
                                }
                                List<Job> limitedBestList = best.size() > 10 ? best.subList(0, 10) : new ArrayList<>(best);
                                bestjobList.addAll(limitedBestList);
                                while (bestjobList.size() < 10 && best.size() > bestjobList.size()) {
                                    bestjobList.add(best.get(bestjobList.size()));
                                }

                                List<Job> interesting = new ArrayList<>();
                                for (Job job : jobs) {
                                    String jobName = job.getJobName() != null ? job.getJobName().toLowerCase() : "";
                                    if (jobName.contains(jobDesire) && job.getSalary() > 1000) {
                                        interesting.add(job);
                                    }
                                }
                                List<Job> limitedInterestingList = interesting.size() > 10 ? interesting.subList(0, 10) : new ArrayList<>(interesting);
                                interestingList.addAll(limitedInterestingList);
                                while (interestingList.size() < 10 && interesting.size() > interestingList.size()) {
                                    interestingList.add(interesting.get(interestingList.size()));
                                }

                                workAdapter.notifyDataSetChanged();
                                theBestJobAdapter.notifyDataSetChanged();
                            } else {
                                Log.e("NewsFeedFragment", "applicants is null, cannot filter jobs");
                            }
                        } else {
                            Log.d("NewsFeedFragment", "Received jobs list is empty");
                        }
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        e.printStackTrace();
                        Log.e("NewsFeedFragment", "Error fetching jobs: " + e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        Log.d("API", "Successfully fetched job data");
                    }
                });
    }

    public void getAPICompanyData() {
        ApiCompanyService.ApiCompanyService.getAllCompany()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Company>>() {
                    @Override
                    public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {

                    }

                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onNext(@io.reactivex.rxjava3.annotations.NonNull List<Company> companies) {
                        Log.d("NewsFeedFragment", "Received companies: " + companies.toString());

                        companyList.clear();
                        if (!companies.isEmpty()) {
                            List<Company> company = companies.size() > 4 ? companies.subList(0, 4) : companies;
                            companyList.addAll(company);
                            companyTopAdapter.setData(companyList);
                            companyTopAdapter.notifyDataSetChanged();
                        } else {
                            Log.d("NewsFeedFragment", "Received companies list is empty");
                        }
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void getAPIArticleData() {
        ApiArticleService.ApiArticleService.getAllArticle()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Article>>() {
                    @Override
                    public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {

                    }

                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onNext(@io.reactivex.rxjava3.annotations.NonNull List<Article> articles) {
                        Log.d("NewsFeedFragment", "Received articles: " + articles.toString());

                        if (!articles.isEmpty()) {
                            List<Article> company = articles.size() > 10 ? articles.subList(0, 10) : articles;
                            articleList.addAll(company);
                            articleAdapter.setData(articleList);
                            articleAdapter.notifyDataSetChanged();
                        } else {
                            Log.d("NewsFeedFragment", "Received articles list is empty");
                        }
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        e.printStackTrace();

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void setWidget(View view){


        workList = new ArrayList<>();
        bestjobList = new ArrayList<>();
        interestingList = new ArrayList<>();
        companyList = new ArrayList<>();
        articleList = new ArrayList<>();

        articleAdapter = new ArticleAdapter();
        companyTopAdapter = new CompanyTopAdapter();
        theBestJobAdapter = new TheBestJobAdapter();
        workAdapter = new WorkAdapter();

        recyclerview_the_suitable_job = view.findViewById(R.id.recyclerview_thesuitablejob);
        recyclerview_the_best_job = view.findViewById(R.id.recyclerview_thebestjob);
        recyclerview_the_interesting_job = view.findViewById(R.id.recyclerview_theinterestingjob);
        recyclerview_the_top_companies = view.findViewById(R.id.recyclerview_thetopcompanies);
        recyclerview_the_article = view.findViewById(R.id.recyclerview_thearticle);

        view_all_suitable_job = view.findViewById(R.id.view_all_suitable_job);
        view_all_best_job = view.findViewById(R.id.view_all_best_job);
        view_all_interesting_job = view.findViewById(R.id.view_all_interesting_job);
        view_all_top_company = view.findViewById(R.id.view_all_top_company);
        view_all_article = view.findViewById(R.id.view_all_article);

        suggest_job_textview = view.findViewById(R.id.suggest_job_textview);
        best_job_textview = view.findViewById(R.id.best_job_textview);
        interesting_job_textview = view.findViewById(R.id.interesting_job_textview);
        top_company_textview = view.findViewById(R.id.top_company_textview);
        article_textview = view.findViewById(R.id.article_textview);

        applicants = new Applicant();
        id_User = getArguments().getInt("user_id", 0);
    }

    private void TheSuitableJob(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerview_the_suitable_job.setLayoutManager(linearLayoutManager);
        recyclerview_the_suitable_job.setFocusable(false);
        recyclerview_the_suitable_job.setNestedScrollingEnabled(false);
        workAdapter.setData(workList);
        recyclerview_the_suitable_job.setAdapter(workAdapter);
    }

    private void TheBestJob(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerview_the_best_job.setLayoutManager(linearLayoutManager);
        recyclerview_the_best_job.setFocusable(false);
        recyclerview_the_best_job.setNestedScrollingEnabled(false);
        theBestJobAdapter.setData(bestjobList);
        recyclerview_the_best_job.setAdapter(theBestJobAdapter);
    }

    private void TheInterestingJob(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerview_the_interesting_job.setLayoutManager(linearLayoutManager);
        recyclerview_the_interesting_job.setFocusable(false);
        recyclerview_the_interesting_job.setNestedScrollingEnabled(false);
        theBestJobAdapter.setData(interestingList);
        recyclerview_the_interesting_job.setAdapter(theBestJobAdapter);
    }

    private void TheTopCompany(){
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        recyclerview_the_top_companies.setLayoutManager(gridLayoutManager);
        recyclerview_the_top_companies.setFocusable(false);
        recyclerview_the_top_companies.setNestedScrollingEnabled(false);
        companyTopAdapter.setData(companyList);
        recyclerview_the_top_companies.setAdapter(companyTopAdapter);
    }

    private void TheArticle(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerview_the_article.setLayoutManager(linearLayoutManager);
        recyclerview_the_article.setFocusable(false);
        recyclerview_the_article.setNestedScrollingEnabled(false);
        articleAdapter.setData(articleList);
        recyclerview_the_article.setAdapter(articleAdapter);
    }
}
