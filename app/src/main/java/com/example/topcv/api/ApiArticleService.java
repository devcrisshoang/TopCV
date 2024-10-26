package com.example.topcv.api;

import com.example.topcv.model.Article;
import com.example.topcv.model.Company;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.core.Observable;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public interface ApiArticleService {
    // Logging interceptor để theo dõi request và response
    HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);

    // Sử dụng OkHttpClient an toàn bỏ qua SSL
    OkHttpClient okHttpClient = UnsafeOkHttpClient.getUnsafeOkHttpClient()
            .newBuilder()
            .addInterceptor(loggingInterceptor)
            .readTimeout(30, TimeUnit.SECONDS)
            .connectTimeout(30, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .build();

    // Sử dụng Retrofit để tạo API service
    ApiArticleService ApiArticleService = new Retrofit.Builder()
            .baseUrl("https://10.0.2.2:7200/")  // Thay địa chỉ bằng IP của máy bạn hoặc server thật
            .client(okHttpClient)  // Áp dụng OkHttpClient bỏ qua SSL
            .addConverterFactory(GsonConverterFactory.create())  // Chuyển đổi JSON sang đối tượng Java
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())  // Sử dụng RxJava3
            .build()
            .create(ApiArticleService.class);

    // API lấy danh sách job
    @GET("api/Article")
    Observable<List<Article>> getAllArticle();
}

