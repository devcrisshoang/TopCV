package com.example.topcv;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.topcv.api.ApiArticleService;
import com.example.topcv.model.Article;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ArticleActivity extends AppCompatActivity {
    private int articleId;
    private TextView name;
    private TextView content;
    private Disposable disposable;
    private ImageView image;
    private ImageButton back_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_article);

        // Thiết lập padding để tránh trùng thanh trạng thái
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Nhận article_id từ Intent và khởi tạo view
        articleId = getIntent().getIntExtra("article_id", -1);
        name = findViewById(R.id.name);
        content = findViewById(R.id.content);
        image = findViewById(R.id.image);
        back_button = findViewById(R.id.back_button);

        // Gọi API để lấy thông tin bài viết
        getArticleById(articleId);
        back_button.setOnClickListener(view -> {
            finish();
        });
    }

    private void getArticleById(int id) {
        ApiArticleService.ApiArticleService.getArticleById(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Article>() {
                    @Override
                    public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {
                        disposable = d;
                    }

                    @Override
                    public void onNext(@io.reactivex.rxjava3.annotations.NonNull Article article) {
                        // Gán tên và nội dung của bài viết vào các TextView
                        name.setText(article.getName());
                        content.setText(article.getContent());

                        // Kiểm tra URL ảnh, nếu hợp lệ thì tải ảnh bằng Glide
                        String imageUrl = article.getImage();
                        if (imageUrl != null && !imageUrl.isEmpty()) {
                            Glide.with(ArticleActivity.this)
                                    .load(imageUrl)
                                    .placeholder(R.drawable.fpt_ic) // Ảnh hiển thị trong lúc tải
                                    .error(R.drawable.fpt_ic) // Ảnh hiển thị khi tải thất bại
                                    .into(image);
                        } else {
                            // Đặt ảnh mặc định nếu URL rỗng hoặc null
                            image.setImageResource(R.drawable.fpt_ic);
                        }
                    }


                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        // Xử lý lỗi khi gọi API
                        Toast.makeText(ArticleActivity.this, "Lỗi khi tải dữ liệu: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {
                        // Xử lý khi hoàn tất API call (nếu cần)
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
