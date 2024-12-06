package com.example.topcv;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.topcv.adapter.ArticleAdapter;
import com.example.topcv.adapter.CompanyTopAdapter;
import com.example.topcv.adapter.TheBestJobAdapter;
import com.example.topcv.adapter.WorkAdapter;
import com.example.topcv.api.ApiArticleService;
import com.example.topcv.api.ApiCompanyService;
import com.example.topcv.api.ApiJobService;
import com.example.topcv.model.Article;
import com.example.topcv.model.Company;
import com.example.topcv.model.Job;
import com.example.topcv.utils.PaginationScrollListener;
import java.util.ArrayList;
import java.util.List;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SeeAllActivity extends AppCompatActivity {

    private boolean WORK = false;
    private boolean JOB = false;
    private boolean COMPANY = false;
    private boolean ARTICLE = false;
    private boolean isLoading;
    private boolean isLastPage;

    private RecyclerView recycler_view_see_all;

    private ImageButton back_button;

    private TextView content_list;

    private ArticleAdapter articleAdapter;
    private CompanyTopAdapter companyTopAdapter;
    private TheBestJobAdapter theBestJobAdapter;
    private WorkAdapter workAdapter;

    private List<Job> workList;
    private List<Job> bestjobList;
    private List<Company> companyList;
    private List<Article> articleList;
    private List<Job> job_data;
    private List<Company> company_data;
    private List<Article> article_data;

    private Disposable disposable;

    private int totalPage;
    private int currentPage = 1;

    private LinearLayoutManager linearLayoutManager;

    private RecyclerView.ItemDecoration itemDecoration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_all);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setWidget();

        setClick();

        getIntentData();

    }

    private void setClick(){
        back_button.setOnClickListener(view -> finish());
        articleAdapter.setOnItemClickListener(article -> {
            Intent intent = new Intent(this, ArticleActivity.class);
            intent.putExtra("article_id", article.getId());
            startActivity(intent);
        });
        workAdapter.setOnItemClickListener(job -> {
            Intent intent = new Intent(this, JobActivity.class);
            intent.putExtra("job_id", job.getId());
            startActivity(intent);
        });
        theBestJobAdapter.setOnItemClickListener(job -> {
            Intent intent = new Intent(this, JobActivity.class);
            intent.putExtra("best_id", job.getId());
            startActivity(intent);
        });
        recycler_view_see_all.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
            @Override
            public void loadMoreItem() {
                if(WORK){
                    isLoading = true;
                    currentPage += 1;
                    loadNextPageWork();
                } else if (JOB) {
                    isLoading = true;
                    currentPage += 1;
                    loadNextPageJob();
                } else if (COMPANY) {
                    isLoading = true;
                    currentPage += 1;
                    loadNextPageCompany();
                } else if (ARTICLE) {
                    isLoading = true;
                    currentPage += 1;
                    loadNextPageArticle();
                }
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }
        });
    }

    private void getTotalPageJob(){
        if(job_data.size() <= 10){
            totalPage = 1;
        }
        else if(job_data.size() % 10 == 0){
            totalPage = job_data.size()/10;
        }
        else {
            job_data.size();
            totalPage = job_data.size() / 10 + 1;
        }
        Log.e("total page","total = " + totalPage);
    }

    private void getTotalPageCompany(){
        if(company_data.size() <= 10){
            totalPage = 1;
        }
        else if(company_data.size() % 10 == 0){
            totalPage = company_data.size()/10;
        }
        else {
            company_data.size();
            totalPage = company_data.size() / 10 + 1;
        }
        Log.e("total page","total = " + totalPage);
    }

    private void getTotalPageArticle(){
        if(article_data.size() <= 10){
            totalPage = 1;
        }
        else if(article_data.size() % 10 == 0){
            totalPage = article_data.size()/10;
        }
        else {
            article_data.size();
            totalPage = article_data.size() / 10 + 1;
        }
        Log.e("total page","total = " + totalPage);
    }

    @SuppressLint("SetTextI18n")
    private void getIntentData() {
        String suggestText = getIntent().getStringExtra("suggestText");
        String bestText = getIntent().getStringExtra("bestText");
        String interestingText = getIntent().getStringExtra("interestingText");
        String companyText = getIntent().getStringExtra("companyText");
        String articleText = getIntent().getStringExtra("articleText");

        if (suggestText != null && !suggestText.isEmpty()) {
            content_list.setText(suggestText);
            WORK = true;
            setRecyclerViewAdapter(workAdapter);
            getAllWorks();
        } else if (bestText != null && !bestText.isEmpty()) {
            content_list.setText(bestText);
            JOB = true;
            setRecyclerViewAdapter(theBestJobAdapter);
            getAllJobs();
        } else if (interestingText != null && !interestingText.isEmpty()) {
            content_list.setText(interestingText);
            JOB = true;
            setRecyclerViewAdapter(theBestJobAdapter);
            getAllJobs();
        } else if (companyText != null && !companyText.isEmpty()) {
            content_list.setText(companyText);
            COMPANY = true;
            setRecyclerViewAdapter(companyTopAdapter);
            getAllCompanies(); // Gọi API lấy dữ liệu công ty
        } else if (articleText != null && !articleText.isEmpty()) {
            content_list.setText(articleText);
            ARTICLE = true;
            setRecyclerViewAdapter(articleAdapter);
            getAllArticles(); // Gọi API lấy dữ liệu bài viết
        } else {
            content_list.setText("No Data");
        }
    }

    private void setRecyclerViewAdapter(RecyclerView.Adapter<?> adapter) {
        recycler_view_see_all.setLayoutManager(new LinearLayoutManager(this)); // Sử dụng LinearLayoutManager
        recycler_view_see_all.setAdapter(adapter);
        recycler_view_see_all.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }

    private void setWidget(){
        back_button = findViewById(R.id.back_button);
        content_list = findViewById(R.id.content_list);
        workList = new ArrayList<>();
        bestjobList = new ArrayList<>();
        companyList = new ArrayList<>();
        articleList = new ArrayList<>();
        job_data = new ArrayList<>();
        company_data = new ArrayList<>();
        article_data = new ArrayList<>();

        workAdapter = new WorkAdapter();
        articleAdapter = new ArticleAdapter();
        theBestJobAdapter = new TheBestJobAdapter();
        companyTopAdapter = new CompanyTopAdapter();

        recycler_view_see_all = findViewById(R.id.recycler_view_see_all);

        linearLayoutManager = new LinearLayoutManager(this);
        itemDecoration = new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);
    }

    private void setFirstWorkData(){
        workList = getListUser();
        workAdapter.setData(workList);

        if (currentPage < totalPage){
            workAdapter.addFooterLoading();
        } else {
            isLastPage = true;
        }
    }

    private void setFirstJobData(){
        bestjobList = getListUser();
        theBestJobAdapter.setData(bestjobList);

        if (currentPage < totalPage){
            theBestJobAdapter.addFooterLoading();
        } else {
            isLastPage = true;
        }
    }

    private void setFirstCompanyData(){
        companyList = getListCompany();
        companyTopAdapter.setData(companyList);

        if (currentPage < totalPage){
            companyTopAdapter.addFooterLoading();
        } else {
            isLastPage = true;
        }
    }

    private void setFirstArticleData(){
        articleList = getListArticle();
        articleAdapter.setData(articleList);

        if (currentPage < totalPage){
            articleAdapter.addFooterLoading();
        } else {
            isLastPage = true;
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private void loadNextPageWork(){
        new Handler().postDelayed(() -> {
            List<Job> list = getListUser();
            workAdapter.removeFooterLoading();
            workList.addAll(list);
            workAdapter.notifyDataSetChanged();
            isLoading = false;

            if (currentPage < totalPage) {
                workAdapter.addFooterLoading();
            }
            else {
                isLastPage = true;
            }
        }, 2000);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void loadNextPageJob(){
        new Handler().postDelayed(() -> {
            List<Job> list = getListUser();
            theBestJobAdapter.removeFooterLoading();
            bestjobList.addAll(list);
            theBestJobAdapter.notifyDataSetChanged();
            isLoading = false;

            if (currentPage < totalPage) {
                theBestJobAdapter.addFooterLoading();
            }
            else {
                isLastPage = true;
            }
        }, 2000);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void loadNextPageCompany(){
        new Handler().postDelayed(() -> {
            List<Company> list = getListCompany();
            companyTopAdapter.removeFooterLoading();
            companyList.addAll(list);
            companyTopAdapter.notifyDataSetChanged();
            isLoading = false;

            if (currentPage < totalPage) {
                companyTopAdapter.addFooterLoading();
            }
            else {
                isLastPage = true;
            }
        }, 2000);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void loadNextPageArticle(){
        new Handler().postDelayed(() -> {
            List<Article> list = getListArticle();
            articleAdapter.removeFooterLoading();
            articleList.addAll(list);
            articleAdapter.notifyDataSetChanged();
            isLoading = false;

            if (currentPage < totalPage) {
                articleAdapter.addFooterLoading();
            }
            else {
                isLastPage = true;
            }
        }, 2000);
    }

    private List<Job> getListUser() {
        List<Job> list = new ArrayList<>();

        int start = (currentPage - 1) * 10; // Tính chỉ số bắt đầu
        int end = Math.min(start + 10, job_data.size()); // Tính chỉ số kết thúc

        if (start < job_data.size()) {
            list.addAll(job_data.subList(start, end));
        }
        return list;
    }

    private List<Company> getListCompany() {
        List<Company> list = new ArrayList<>();

        int start = (currentPage - 1) * 10; // Tính chỉ số bắt đầu
        int end = Math.min(start + 10, company_data.size()); // Tính chỉ số kết thúc

        if (start < company_data.size()) {
            list.addAll(company_data.subList(start, end));
        }
        return list;
    }

    private List<Article> getListArticle() {
        List<Article> list = new ArrayList<>();

        int start = (currentPage - 1) * 10; // Tính chỉ số bắt đầu
        int end = Math.min(start + 10, article_data.size()); // Tính chỉ số kết thúc

        if (start < article_data.size()) {
            list.addAll(article_data.subList(start, end));
        }
        return list;
    }

    private void getAllJobs() {
        ApiJobService.ApiJobService.getAllJobs()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Job>>() {
                    @Override
                    public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {
                        disposable = d;
                    }

                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onNext(@io.reactivex.rxjava3.annotations.NonNull List<Job> jobs) {
                        if (jobs != null && !jobs.isEmpty()) {
                            job_data.clear();
                            job_data.addAll(jobs);
                            getTotalPageJob();
                            setFirstJobData();
                            recycler_view_see_all.setLayoutManager(linearLayoutManager);
                            recycler_view_see_all.setAdapter(theBestJobAdapter);
                            recycler_view_see_all.addItemDecoration(itemDecoration);
                            theBestJobAdapter.notifyDataSetChanged();
                        }
                    }


                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        //Toast.makeText(SeeAllActivity.this, "Lỗi khi gọi API: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {
                        //Toast.makeText(SeeAllActivity.this, "Lấy dữ liệu thành công", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void getAllWorks() {
        ApiJobService.ApiJobService.getAllJobs()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Job>>() {
                    @Override
                    public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {
                        disposable = d;
                    }

                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onNext(@io.reactivex.rxjava3.annotations.NonNull List<Job> jobs) {
                        if (jobs != null && !jobs.isEmpty()) {
                            job_data.clear();
                            job_data.addAll(jobs);
                            getTotalPageJob();
                            setFirstWorkData();
                            recycler_view_see_all.setLayoutManager(linearLayoutManager);
                            recycler_view_see_all.setAdapter(workAdapter);
                            recycler_view_see_all.addItemDecoration(itemDecoration);
                            workAdapter.notifyDataSetChanged();
                        }
                    }


                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        //Toast.makeText(SeeAllActivity.this, "Lỗi khi gọi API: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {
                        //Toast.makeText(SeeAllActivity.this, "Lấy dữ liệu thành công", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void getAllCompanies() {
        ApiCompanyService.ApiCompanyService.getAllCompany()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Company>>() {
                    @Override
                    public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {
                        disposable = d;
                    }

                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onNext(@io.reactivex.rxjava3.annotations.NonNull List<Company> companies) {
                        if (companies != null && !companies.isEmpty()) {
                            company_data.clear();
                            company_data.addAll(companies);
                            getTotalPageCompany();
                            setFirstCompanyData();
                            recycler_view_see_all.setLayoutManager(linearLayoutManager);
                            recycler_view_see_all.setAdapter(companyTopAdapter);
                            recycler_view_see_all.addItemDecoration(itemDecoration);
                            companyTopAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        //Toast.makeText(SeeAllActivity.this, "Lỗi khi gọi API công ty: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {
                        //Toast.makeText(SeeAllActivity.this, "Lấy dữ liệu công ty thành công", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void getAllArticles() {
        ApiArticleService.ApiArticleService.getAllArticle()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Article>>() {
                    @Override
                    public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {
                        disposable = d;
                    }

                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onNext(@io.reactivex.rxjava3.annotations.NonNull List<Article> articles) {
                        if (articles != null && !articles.isEmpty()) {
                            article_data.clear();
                            article_data.addAll(articles);
                            getTotalPageArticle();
                            setFirstArticleData();
                            recycler_view_see_all.setLayoutManager(linearLayoutManager);
                            recycler_view_see_all.setAdapter(articleAdapter);
                            recycler_view_see_all.addItemDecoration(itemDecoration);
                            articleAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        //Toast.makeText(SeeAllActivity.this, "Lỗi khi gọi API bài viết: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {
                        //Toast.makeText(SeeAllActivity.this, "Lấy dữ liệu bài viết thành công", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }
}