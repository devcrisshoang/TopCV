package com.example.topcv.API;

import com.example.topcv.model.SortOfUser;

import io.reactivex.rxjava3.core.Single;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

import java.util.concurrent.TimeUnit;

public interface ApiSortOfUser {

    // Logging interceptor to monitor request and response
    HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);

    // Safe OkHttpClient with SSL bypass (for development purposes)
    OkHttpClient okHttpClient = UnsafeOkHttpClient.getUnsafeOkHttpClient()
            .newBuilder()
            .addInterceptor(loggingInterceptor)
            .readTimeout(30, TimeUnit.SECONDS)
            .connectTimeout(30, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .build();

    // Create Retrofit instance for SortOfUser API service
    ApiSortOfUser apiSortOfUser = new Retrofit.Builder()
            .baseUrl("https://10.0.2.2:7200/") // Server base URL
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build()
            .create(ApiSortOfUser.class);

    // POST method to create SortOfUser entry
    @POST("api/SortOfUser")
    Single<Response<SortOfUser>> addSortOfUser(@Body SortOfUser sortOfUser);

    // PUT method to update SortOfUser for Applicant
    @PUT("api/SortOfUser/{id}Applicant")
    Single<Response<Void>> updateApplicantInSortOfUser(@Body SortOfUser sortOfUser);

    @PUT("api/SortOfUser/{id}")
    Single<Response<Void>> updateApplicantInSortOfUser(@Path("id") int applicantId, @Body SortOfUser sortOfUser);
}
