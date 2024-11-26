package com.example.topcv.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.topcv.ArticleActivity;
import com.example.topcv.CompanyActivity;
import com.example.topcv.CompanyInformationsActivity;
import com.example.topcv.LoginActivity;
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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
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

    private RecyclerView recyclerview_thesuitablejob;
    private RecyclerView recyclerview_thebestjob;
    private RecyclerView recyclerview_theinterestingjob;
    private RecyclerView recyclerview_thetopcompanies;
    private RecyclerView recyclerview_thearticle;

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

        Bundle bundle = getArguments();
        if (bundle != null) {
            id_User = bundle.getInt("user_id", 0);  // 0 là giá trị mặc định
            Log.e("NewsFeedFragment", "User ID received: " + id_User);
        } else {
            Log.e("NewsFeedFragment", "Bundle is null, user ID not received " + id_User);
        }

        //api
        getAPIJobData();
        getAPICompanyData();
        getAPIArticleData();

        getAPIApplicantData(id_User);
        if(applicants == null){
            Log.e("applicants", "applicants null: " + applicants);
        }
        //data
        TheSuitableJob();
        TheBestJob();
        TheInterestingJob();
        TheTopCompany();
        TheArticle();

        view_all_suitable_job.setOnClickListener(view1 -> {
            String text = suggest_job_textview.getText().toString(); // Lấy nội dung của suggest_job_textview
            Intent intent = new Intent(getContext(), SeeAllActivity.class);
            intent.putExtra("suggestText", text); // Truyền text qua Intent
            startActivity(intent);
        });
        view_all_best_job.setOnClickListener(view1 -> {
            String text = best_job_textview.getText().toString(); // Lấy nội dung của suggest_job_textview
            Intent intent = new Intent(getContext(), SeeAllActivity.class);
            intent.putExtra("bestText", text); // Truyền text qua Intent
            startActivity(intent);
        });
        view_all_interesting_job.setOnClickListener(view1 -> {
            String text = interesting_job_textview.getText().toString(); // Lấy nội dung của suggest_job_textview
            Intent intent = new Intent(getContext(), SeeAllActivity.class);
            intent.putExtra("interestingText", text); // Truyền text qua Intent
            startActivity(intent);
        });
        view_all_top_company.setOnClickListener(view1 -> {
            String text = top_company_textview.getText().toString(); // Lấy nội dung của suggest_job_textview
            Intent intent = new Intent(getContext(), SeeAllActivity.class);
            intent.putExtra("companyText", text); // Truyền text qua Intent
            startActivity(intent);
        });
        view_all_article.setOnClickListener(view1 -> {
            String text = article_textview.getText().toString(); // Lấy nội dung của suggest_job_textview
            Intent intent = new Intent(getContext(), SeeAllActivity.class);
            intent.putExtra("articleText", text); // Truyền text qua Intent
            startActivity(intent);
        });
        articleAdapter.setOnItemClickListener(article -> {
            Intent intent = new Intent(getContext(), ArticleActivity.class);
            intent.putExtra("article_id", article.getId()); // Truyền ID của bài viết
            startActivity(intent);
        });
        workAdapter.setOnItemClickListener(job -> {
            Intent intent = new Intent(getContext(), CompanyInformationsActivity.class);
            intent.putExtra("job_id", job.getId()); // Truyền dữ liệu cần thiết (như id công việc)
            intent.putExtra("id_User", id_User);
            startActivity(intent);
        });
        theBestJobAdapter.setOnItemClickListener(job -> {
            Intent intent = new Intent(getContext(), CompanyInformationsActivity.class);
            intent.putExtra("best_id", job.getId()); // Truyền dữ liệu cần thiết (như id công việc)
            intent.putExtra("id_User", id_User);
            startActivity(intent);
        });
        companyTopAdapter.setOnItemClickListener(company -> {
            Intent intent = new Intent(getContext(), CompanyActivity.class);
            intent.putExtra("company_id", company.getId()); // Truyền dữ liệu cần thiết (như id công việc)
            startActivity(intent);
        });

        return view;
    }

    public void getAPIApplicantData(int UserID){
        ApiApplicantService.ApiApplicantService.getApplicantByUserId(UserID)
            .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(response -> {
                        if (response != null) {
                            applicants = response;
                        }
                    }, throwable -> {
                        Log.e("API Error", "Error fetching applicant: " + throwable.getMessage());
                    });
        }

    public void getAPIJobData() {
        ApiJobService.ApiJobService.getAllJobs()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Job>>() {
                    @Override
                    public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {}

                    @Override
                    public void onNext(@io.reactivex.rxjava3.annotations.NonNull List<Job> jobs) {
                        Log.d("MessageActivity", "Received jobs: " + jobs.toString());

                        // Kiểm tra nếu jobs là null hoặc rỗng
                        if (jobs != null && !jobs.isEmpty()) {
                            // Xóa dữ liệu cũ trước khi thêm mới
                            workList.clear();
                            bestjobList.clear();
                            interestingList.clear();

                            // Kiểm tra ứng viên có null không
                            if (applicants != null) {
                                String jobDesire = applicants.getJobDesire() != null ? applicants.getJobDesire().toLowerCase() : "";
                                String locationDesire = applicants.getWorkingLocationDesire() != null ? applicants.getWorkingLocationDesire().toLowerCase() : "";

                                // Lọc công việc theo yêu cầu của ứng viên
                                List<Job> work = new ArrayList<>();
                                for (Job job : jobs) {
                                    // Kiểm tra job.getJobName() và job.getLocation() không null trước khi sử dụng
                                    String jobName = job.getJobName() != null ? job.getJobName().toLowerCase() : "";
                                    String location = job.getLocation() != null ? job.getLocation().toLowerCase() : "";

                                    if (jobName.contains(jobDesire) && location.contains(locationDesire)) {
                                        work.add(job);
                                    }
                                }

                                // Giới hạn danh sách công việc tối đa là 5
                                List<Job> limitedWorkList = work.size() > 5 ? work.subList(0, 5) : new ArrayList<>(work);
                                workList.addAll(limitedWorkList);
                                while (workList.size() < 5 && work.size() > workList.size()) {
                                    workList.add(work.get(workList.size()));
                                }

                                // Danh sách công việc có lương cao hơn 1000
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

                                // Danh sách công việc thú vị có lương cao hơn 1000
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

                                // Notify các adapter cập nhật dữ liệu mới
                                workAdapter.notifyDataSetChanged();
                                theBestJobAdapter.notifyDataSetChanged();
                            } else {
                                Log.e("applicants", "applicants is null, cannot filter jobs");
                            }
                        } else {
                            Log.d("MessageActivity", "Received jobs list is empty");
                        }
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        e.printStackTrace();
                        // Thông báo lỗi nếu có
                        Log.e("API Error", "Error fetching jobs: " + e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        // Thông báo khi gọi API thành công
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

                    @Override
                    public void onNext(@io.reactivex.rxjava3.annotations.NonNull List<Company> companies) {
                        Log.d("NewsFeedFragment", "Received companies: " + companies.toString());

                        companyList.clear();
                        if (companies != null && !companies.isEmpty()) {
                            // Chỉ lấy 4 công ty đầu tiên
                            List<Company> company = companies.size() > 4 ? companies.subList(0, 4) : companies;
                            companyList.addAll(company);
                            companyTopAdapter.setData(companyList); // Set lại dữ liệu cho adapter
                            companyTopAdapter.notifyDataSetChanged(); // Cập nhật giao diện
                        } else {
                            Log.d("NewsFeedFragment", "Received companies list is empty");
                        }
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        e.printStackTrace();
                        //Toast.makeText(requireContext(), "Call API error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {
                        //Toast.makeText(getContext(), "Call API successful", Toast.LENGTH_SHORT).show();
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

                    @Override
                    public void onNext(@io.reactivex.rxjava3.annotations.NonNull List<Article> articles) {
                        Log.d("NewsFeedFragment", "Received articles: " + articles.toString());

                        if (articles != null && !articles.isEmpty()) {
                            // Cập nhật dữ liệu cho adapter
                            List<Article> company = articles.size() > 10 ? articles.subList(0, 10) : articles;
                            articleList.addAll(company);
                            articleAdapter.setData(articleList);
                            articleAdapter.notifyDataSetChanged(); // Cập nhật giao diện
                        } else {
                            Log.d("NewsFeedFragment", "Received articles list is empty");
                        }
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        e.printStackTrace();
                        //Toast.makeText(requireContext(), "Call API error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {
                        //Toast.makeText(getContext(), "Thông báo", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void setWidget(View view){
        //list
        workList = new ArrayList<>();
        bestjobList = new ArrayList<>();
        interestingList = new ArrayList<>();
        companyList = new ArrayList<>();
        articleList = new ArrayList<>();
        //adapter
        articleAdapter = new ArticleAdapter();
        companyTopAdapter = new CompanyTopAdapter();
        theBestJobAdapter = new TheBestJobAdapter();
        workAdapter = new WorkAdapter();
        //recyclerview
        recyclerview_thesuitablejob = view.findViewById(R.id.recyclerview_thesuitablejob);
        recyclerview_thebestjob = view.findViewById(R.id.recyclerview_thebestjob);
        recyclerview_theinterestingjob = view.findViewById(R.id.recyclerview_theinterestingjob);
        recyclerview_thetopcompanies = view.findViewById(R.id.recyclerview_thetopcompanies);
        recyclerview_thearticle = view.findViewById(R.id.recyclerview_thearticle);
        //button
        view_all_suitable_job = view.findViewById(R.id.view_all_suitable_job);
        view_all_best_job = view.findViewById(R.id.view_all_best_job);
        view_all_interesting_job = view.findViewById(R.id.view_all_interesting_job);
        view_all_top_company = view.findViewById(R.id.view_all_top_company);
        view_all_article = view.findViewById(R.id.view_all_article);
        //textview
        suggest_job_textview = view.findViewById(R.id.suggest_job_textview);
        best_job_textview = view.findViewById(R.id.best_job_textview);
        interesting_job_textview = view.findViewById(R.id.interesting_job_textview);
        top_company_textview = view.findViewById(R.id.top_company_textview);
        article_textview = view.findViewById(R.id.article_textview);

        applicants = new Applicant();
    }
    private void TheSuitableJob(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerview_thesuitablejob.setLayoutManager(linearLayoutManager);
        recyclerview_thesuitablejob.setFocusable(false);
        recyclerview_thesuitablejob.setNestedScrollingEnabled(false);
        workAdapter.setData(workList);
        recyclerview_thesuitablejob.setAdapter(workAdapter);
    }
    private void TheBestJob(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerview_thebestjob.setLayoutManager(linearLayoutManager);
        recyclerview_thebestjob.setFocusable(false);
        recyclerview_thebestjob.setNestedScrollingEnabled(false);
        theBestJobAdapter.setData(bestjobList);
        recyclerview_thebestjob.setAdapter(theBestJobAdapter);
    }
    private void TheInterestingJob(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerview_theinterestingjob.setLayoutManager(linearLayoutManager);
        recyclerview_theinterestingjob.setFocusable(false);
        recyclerview_theinterestingjob.setNestedScrollingEnabled(false);
        theBestJobAdapter.setData(interestingList);
        recyclerview_theinterestingjob.setAdapter(theBestJobAdapter);
    }
    private void TheTopCompany(){
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        recyclerview_thetopcompanies.setLayoutManager(gridLayoutManager);
        recyclerview_thetopcompanies.setFocusable(false);
        recyclerview_thetopcompanies.setNestedScrollingEnabled(false);
        companyTopAdapter.setData(companyList);
        recyclerview_thetopcompanies.setAdapter(companyTopAdapter);
    }
    private void TheArticle(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerview_thearticle.setLayoutManager(linearLayoutManager);
        recyclerview_thearticle.setFocusable(false);
        recyclerview_thearticle.setNestedScrollingEnabled(false);
        articleAdapter.setData(articleList);
        recyclerview_thearticle.setAdapter(articleAdapter);
    }
}
