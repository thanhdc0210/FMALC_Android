package com.demo.fmalc_android.service;

import com.demo.fmalc_android.entity.NotificationMobileResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Path;

public interface NotificationService {

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @GET("api/v1.0/notification/driver/{id}")
    Call<List<NotificationMobileResponse>> findNotificationByDriverId(@Path("id") Integer id, @Header("Authorization") String auth);
}
