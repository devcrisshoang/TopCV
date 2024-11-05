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
import com.example.topcv.CompanyInformationsActivity;
import com.example.topcv.InformationActivity;
import com.example.topcv.LoginActivity;
import com.example.topcv.MessageActivity;
import com.example.topcv.R;
import com.example.topcv.SeeAllActivity;
import com.example.topcv.adapter.ArticleAdapter;
import com.example.topcv.adapter.CompanyTopAdapter;
import com.example.topcv.adapter.ProfileAdapter;
import com.example.topcv.adapter.TheBestJobAdapter;
import com.example.topcv.adapter.WorkAdapter;
import com.example.topcv.api.ApiArticleService;
import com.example.topcv.api.ApiCompanyService;
import com.example.topcv.api.ApiJobService;
import com.example.topcv.api.ApiMessageService;
import com.example.topcv.api.ApiResumeService;
import com.example.topcv.model.Article;
import com.example.topcv.model.Company;
import com.example.topcv.model.Job;
import com.example.topcv.model.Message;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news_feed, container, false);
        setWidget(view);
        //api
        getAPIJobData();
        getAPICompanyData();
        getAPIArticleData();
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
            startActivity(intent);
        });
        theBestJobAdapter.setOnItemClickListener(job -> {
            Intent intent = new Intent(getContext(), CompanyInformationsActivity.class);
            intent.putExtra("best_id", job.getId()); // Truyền dữ liệu cần thiết (như id công việc)
            startActivity(intent);
        });

        return view;
    }
    public void getAPIJobData() {
        ApiJobService.ApiJobService.getAllJobs()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Job>>() {
                    @Override
                    public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {

                    }
                    @Override
                    public void onNext(@io.reactivex.rxjava3.annotations.NonNull List<Job> jobs) {
                        Log.d("MessageActivity", "Received jobs: " + jobs.toString());

                        if (jobs != null && !jobs.isEmpty()) {
                            // Xóa dữ liệu cũ
                            workList.clear();
                            bestjobList.clear();
                            interestingList.clear();
                            // Giới hạn danh sách chỉ còn 5 phần tử đầu tiên
                            List<Job> work = jobs.size() > 5 ? jobs.subList(0, 5) : jobs;
                            List<Job> best = jobs.size() > 10 ? jobs.subList(0, 10) : jobs;
                            List<Job> interesting = jobs.size() > 5 ? jobs.subList(0, 5) : jobs;
                            // Thêm dữ liệu mới giới hạn
                            workList.addAll(work);
                            bestjobList.addAll(best);
                            interestingList.addAll(interesting);

                            workAdapter.notifyDataSetChanged();
                            theBestJobAdapter.notifyDataSetChanged();

                        } else {
                            Log.d("MessageActivity", "Received jobs list is empty");
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
