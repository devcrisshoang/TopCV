package com.example.topcv.api;

import com.example.topcv.model.Resume;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.core.Observable;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiResumeService {

    // Logging interceptor to track request and response
    HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);

    // Using OkHttpClient to safely bypass SSL (if necessary)
    OkHttpClient okHttpClient = UnsafeOkHttpClient.getUnsafeOkHttpClient()
            .newBuilder()
            .addInterceptor(loggingInterceptor)
            .readTimeout(30, TimeUnit.SECONDS)
            .connectTimeout(30, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .build();

    // Using Retrofit to create API service
    ApiResumeService apiResumeService = new Retrofit.Builder()
            .baseUrl("https://10.0.2.2:7200/")  // Server URL
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build()
            .create(ApiResumeService.class);

    // POST method to create a new user
    @POST("api/Resume")
    Observable<Resume> createUser(@Body Resume resume);

    // GET method to fetch resumes by applicant ID
    @GET("api/Resume/GetResumeBy/{applicantId}")
    Observable<List<Resume>> getResumesByApplicantId(@Path("applicantId") int applicantId);
}
