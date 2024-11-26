package com.example.topcv.api;

import com.example.topcv.model.User;
import com.example.topcv.utils.NetworkUtils;
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
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiUserService {

    HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);

    OkHttpClient okHttpClient = UnsafeOkHttpClient.getUnsafeOkHttpClient()
            .newBuilder()
            .addInterceptor(loggingInterceptor)
            .readTimeout(30, TimeUnit.SECONDS)
            .connectTimeout(30, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .build();

    String BASE_URL = NetworkUtils.getBaseUrl();

    ApiUserService apiUserService = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build()
            .create(ApiUserService.class);


    @POST("api/User")
    Observable<User> createUser(@Body User user);

    @GET("api/User/usernames")
    Observable<List<String>> getAllUsernames();

    @GET("api/User/{id}")
    Observable<User> getUserById(@Path("id") int id);

    @GET("api/User")
    Observable<List<User>> getAllUser();

    @PUT("api/User/{id}")
    Completable updateUserById(@Path("id") int id, @Body User user);
}
