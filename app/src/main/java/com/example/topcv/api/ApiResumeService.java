package com.example.topcv.api;

import com.example.topcv.model.Resume;
import java.util.List;
import java.util.concurrent.TimeUnit;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiResumeService {

    HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);

    OkHttpClient okHttpClient = UnsafeOkHttpClient.getUnsafeOkHttpClient()
            .newBuilder()
            .addInterceptor(loggingInterceptor)
            .readTimeout(30, TimeUnit.SECONDS)
            .connectTimeout(30, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .build();

    ApiResumeService apiResumeService = new Retrofit.Builder()
            .baseUrl("https://10.0.2.2:7200/")  // Server URL
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build()
            .create(ApiResumeService.class);

    @POST("api/Resume")
    Observable<Resume> createResume(@Body Resume resume);

    @GET("api/Resume/GetResumeBy/{applicantId}")
    Observable<List<Resume>> getResumesByApplicantId(@Path("applicantId") int applicantId);

    @GET("api/Resume/GetResumeIdBy/{id}")
    Observable<Resume> getResumeById(@Path("id") int id);

    @PUT("api/Resume/{id}")
    Completable updateResumeById(@Path("id") int id, @Body Resume updatedResume);

    @DELETE("api/Resume/{id}")
    Completable deleteResumeById(@Path("id") int id);

}
