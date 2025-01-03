package com.example.topcv.api;

import com.example.topcv.model.Message;
import com.example.topcv.model.User;
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

public interface ApiMessageService {

    HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);

    OkHttpClient okHttpClient = UnsafeOkHttpClient.getUnsafeOkHttpClient()
            .newBuilder()
            .addInterceptor(loggingInterceptor)
            .readTimeout(30, TimeUnit.SECONDS)
            .connectTimeout(30, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .build();

    ApiMessageService apiMessageService = new Retrofit.Builder()
            .baseUrl("https://10.0.2.2:7200/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build()
            .create(ApiMessageService.class);
    //
    @GET("api/Message/GetChatPartners/{id}")
    Observable<List<User>> getAllChatPartnersByUserId(@Path("id") int id);
    //
    @GET("api/Message/GetMessagesBetweenUsers/{idUser1}/{idUser2}")
    Observable<List<Message>> getAllMessageByTwoUserId(@Path("idUser1") int idUser1, @Path("idUser2") int idUser2);
    // POST
    @POST("api/Message")
    Observable<Message> postMessage(@Body Message message);
}
