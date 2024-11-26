package com.example.topcv.api;

import com.example.topcv.model.Applicant;
import java.util.concurrent.TimeUnit;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiApplicantService {
    HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);

    OkHttpClient okHttpClient = UnsafeOkHttpClient.getUnsafeOkHttpClient()
            .newBuilder()
            .addInterceptor(loggingInterceptor)
            .readTimeout(30, TimeUnit.SECONDS)
            .connectTimeout(30, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .build();

    ApiApplicantService ApiApplicantService = new Retrofit.Builder()
            .baseUrl("https://10.0.2.2:7200/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build()
            .create(ApiApplicantService.class);

    @GET("api/Applicant/user/{id}")
    Observable <Applicant> getApplicantByUserId(@Path("id") int id);

    @POST("api/Applicant")
    Observable<Applicant> createApplicant(@Body Applicant applicant);

    @PUT("api/Applicant/{id}")
    Completable updateApplicantById(@Path("id") int id, @Body Applicant applicant);

}

