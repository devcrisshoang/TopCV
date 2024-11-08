package com.example.topcv.api;

import com.example.topcv.model.Applicant;
import com.example.topcv.model.User;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Response;
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

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://10.0.2.2:7200/")  // Địa chỉ máy chủ
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build();

    ApiApplicantService apiApplicantService = retrofit.create(ApiApplicantService.class);

    @PUT("api/Applicant/{id}")
    Single<Response<Void>> updateApplicant(@Path("id") int id, @Body Applicant applicant);

    // Thêm phương thức lấy ứng viên theo userId
    @GET("api/Applicant/getApplicantByUserId/{userId}")
    Single<Applicant> getApplicantByUserId(@Path("userId") int userId);

    @GET("api/Applicant/{id}")
    Single<Applicant> getApplicantById(@Path("id") int id);

    @POST("api/Applicant") // This should match your controller's create action
    Single<Response<Applicant>> addApplicant(@Body Applicant applicant);

}
