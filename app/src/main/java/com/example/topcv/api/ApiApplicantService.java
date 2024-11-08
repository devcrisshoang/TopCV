package com.example.topcv.api;

import com.example.topcv.model.Applicant;

import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.core.Observable;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiApplicantService {
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

    @PUT("api/Applicant/{id}")
    Single<Response<Void>> updateApplicant(@Path("id") int id, @Body Applicant applicant);

    // Thêm phương thức lấy ứng viên theo userId
    @GET("api/Applicant/getApplicantByUserId/{userId}")
    Single<Applicant> getApplicantByUserId(@Path("userId") int userId);

    @GET("api/Applicant/{id}")
    Single<Applicant> getApplicantById(@Path("id") int id);

    @POST("api/Applicant") // This should match your controller's create action
    Single<Response<Applicant>> addApplicant(@Body Applicant applicant);

    // Sử dụng Retrofit để tạo API service
    ApiApplicantService ApiApplicantService = new Retrofit.Builder()
            .baseUrl("https://10.0.2.2:7200/")  // Thay địa chỉ bằng IP của máy bạn hoặc server thật
            .client(okHttpClient)  // Áp dụng OkHttpClient bỏ qua SSL
            .addConverterFactory(GsonConverterFactory.create())  // Chuyển đổi JSON sang đối tượng Java
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())  // Sử dụng RxJava3
            .build()
            .create(ApiApplicantService.class);

    @GET("api/Applicant/user/{id}")
    Observable <Applicant> getApplicantByUserId(@Path("id") int id);

}

