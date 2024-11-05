package com.example.topcv;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
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
    private RecyclerView recycler_view_see_all; //

    private ImageButton back_button;
    private TextView content_list;

    private ArticleAdapter articleAdapter;
    private CompanyTopAdapter companyTopAdapter;
    private TheBestJobAdapter theBestJobAdapter;
    private WorkAdapter workAdapter;//

    private List<Job> workList;//
    private List<Job> bestjobList;
    private List<Job> interestingList;
    private List<Company> companyList;
    private List<Article> articleList;

    private Disposable disposable;

    private boolean isLoading;//
    private boolean isLastPage;//
    private int totalPage;//
    private int currentPage = 1;//
    private List<Job> job_data;

    private LinearLayoutManager linearLayoutManager;
    private RecyclerView.ItemDecoration itemDecoration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_all);

        setWidget();

        back_button.setOnClickListener(view -> finish());

        getIntentData();

        // Gọi API
        getAllJobs();
        //getTotalPage();
        recycler_view_see_all.setLayoutManager(linearLayoutManager);
        recycler_view_see_all.setAdapter(workAdapter);
        recycler_view_see_all.addItemDecoration(itemDecoration);
        //setFirstData();

//        getAllCompanies();
//        getAllArticles();

        // Di chuyển setFirstData() vào cuối cùng sau khi lấy dữ liệu từ API
        recycler_view_see_all.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
            @Override
            public void loadMoreItem() {
                isLoading = true;
                currentPage += 1;
                loadNextPage();
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



    private void getTotalPage(){
        if(job_data.size() <= 10){
            totalPage = 1;
        }
        else if(job_data.size() % 10 == 0){
            totalPage = job_data.size()/10;
        }
        else if(job_data.size() % 10 != 0){
            totalPage = job_data.size()/10 +1;
        }
        Log.e("total page","total = " + totalPage);
    }

    private void getIntentData(){
        String suggestText = getIntent().getStringExtra("suggestText");
        String bestText = getIntent().getStringExtra("bestText");
        String interestingText = getIntent().getStringExtra("interestingText");
        String companyText = getIntent().getStringExtra("companyText");
        String articleText = getIntent().getStringExtra("articleText");
        // Kiểm tra các chuỗi và gán giá trị không rỗng cho content_list
        if (suggestText != null && !suggestText.isEmpty()) {
            content_list.setText(suggestText);
            recycler_view_see_all.setAdapter(workAdapter);
        } else if (bestText != null && !bestText.isEmpty()) {
            content_list.setText(bestText);
            recycler_view_see_all.setAdapter(theBestJobAdapter);
        } else if (interestingText != null && !interestingText.isEmpty()) {
            content_list.setText(interestingText);
            recycler_view_see_all.setAdapter(theBestJobAdapter);
        } else if (companyText != null && !companyText.isEmpty()) {
            content_list.setText(companyText);
            recycler_view_see_all.setAdapter(companyTopAdapter);
        } else if (articleText != null && !articleText.isEmpty()) {
            content_list.setText(articleText);
            recycler_view_see_all.setAdapter(articleAdapter);
        }else {
            // Nếu tất cả đều rỗng, có thể gán giá trị mặc định hoặc thông báo nào đó
            content_list.setText("Không có dữ liệu");
        }
    }

    private void setWidget(){
        // Thiết lập nút quay lại
        back_button = findViewById(R.id.back_button);
        content_list = findViewById(R.id.content_list);

        // Khởi tạo danh sách job và adapter
        workList = new ArrayList<>();
        bestjobList = new ArrayList<>();
        interestingList = new ArrayList<>();
        companyList = new ArrayList<>();
        articleList = new ArrayList<>();
        job_data = new ArrayList<>();

        workAdapter = new WorkAdapter();
        articleAdapter = new ArticleAdapter();
        theBestJobAdapter = new TheBestJobAdapter();
        companyTopAdapter = new CompanyTopAdapter();

        recycler_view_see_all = findViewById(R.id.recycler_view_see_all);

        linearLayoutManager = new LinearLayoutManager(this);
        itemDecoration = new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);
    }

    private void setFirstData(){
        workList = getListUser();
        workAdapter.setData(workList);

        if (currentPage < totalPage){
            workAdapter.addFooterLoading();
        } else {
            isLastPage = true;
        }
    }

    private void loadNextPage(){
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

    private List<Job> getListUser() {
        Toast.makeText(this, "Load data page" + currentPage, Toast.LENGTH_SHORT).show();
        List<Job> list = new ArrayList<>();

        int start = (currentPage - 1) * 10; // Tính chỉ số bắt đầu
        int end = Math.min(start + 10, job_data.size()); // Tính chỉ số kết thúc

        if (start < job_data.size()) {
            list.addAll(job_data.subList(start, end)); // Thêm các phần tử từ workList vào danh sách
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

                    @Override
                    public void onNext(@io.reactivex.rxjava3.annotations.NonNull List<Job> jobs) {
                        if (jobs != null && !jobs.isEmpty()) {
                            job_data.clear();
                            job_data.addAll(jobs);
                            getTotalPage(); // Tính tổng số trang sau khi có dữ liệu
                            setFirstData(); // Gọi setFirstData sau khi có dữ liệu
                        } else {
                            Toast.makeText(SeeAllActivity.this, "Không có job nào", Toast.LENGTH_SHORT).show();
                        }
                    }


                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        Toast.makeText(SeeAllActivity.this, "Lỗi khi gọi API: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {
                        Toast.makeText(SeeAllActivity.this, "Lấy dữ liệu thành công", Toast.LENGTH_SHORT).show();
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

                    @Override
                    public void onNext(@io.reactivex.rxjava3.annotations.NonNull List<Company> companies) {
                        if (companies != null && !companies.isEmpty()) {
                            // Làm sạch danh sách cũ
                            companyList.clear();
                            companyList.addAll(companies); // Thêm dữ liệu mới
                            companyTopAdapter.setData(companyList); // Cập nhật dữ liệu cho adapter
                        } else {
                            Toast.makeText(SeeAllActivity.this, "Không có công ty nào", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        Toast.makeText(SeeAllActivity.this, "Lỗi khi gọi API công ty: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {
                        Toast.makeText(SeeAllActivity.this, "Lấy dữ liệu công ty thành công", Toast.LENGTH_SHORT).show();
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

                    @Override
                    public void onNext(@io.reactivex.rxjava3.annotations.NonNull List<Article> articles) {
                        if (articles != null && !articles.isEmpty()) {
                            // Làm sạch danh sách cũ
                            articleList.clear();
                            articleList.addAll(articles); // Thêm dữ liệu mới
                            articleAdapter.setData(articleList); // Cập nhật dữ liệu cho adapter
                        } else {
                            Toast.makeText(SeeAllActivity.this, "Không có bài viết nào", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        Toast.makeText(SeeAllActivity.this, "Lỗi khi gọi API bài viết: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {
                        Toast.makeText(SeeAllActivity.this, "Lấy dữ liệu bài viết thành công", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();  // Giải phóng tài nguyên khi Activity bị hủy
        }
    }
}