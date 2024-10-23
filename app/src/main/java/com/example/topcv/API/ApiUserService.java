package com.example.topcv.API;

import com.example.topcv.model.Applicant;
import com.example.topcv.model.User;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiUserService {

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
    ApiUserService apiUserService = new Retrofit.Builder()
            .baseUrl("https://10.0.2.2:7200/")  // Địa chỉ máy chủ
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build()
            .create(ApiUserService.class);

    // Phương thức để thêm một User
    @POST("api/User") // Đường dẫn đến API để thêm người dùng
    Observable<User> createUser(@Body User user);

    // Thêm phương thức POST để thêm Applicant
    @POST("api/Applicant") // This should match your controller's create action
    Single<Response<Applicant>> addApplicant(@Body Applicant applicant);

    // Phương thức lấy tất cả tên đăng nhập
    @GET("api/User/usernames")
    Observable<List<String>> getAllUsernames();

    // Phương thức lấy tất cả người dùng
    @GET("api/User")
    Observable<List<User>> getAllUser();

    @POST("api/User") // Đường dẫn đến API để thêm người dùng
    Single<Response<User>> addUser(@Body User user);
}
