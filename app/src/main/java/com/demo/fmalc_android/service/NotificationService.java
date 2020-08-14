package com.demo.fmalc_android.service;

import com.demo.fmalc_android.entity.Notification;
import com.demo.fmalc_android.entity.NotificationMobileResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.PATCH;
import retrofit2.http.PUT;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface NotificationService {

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @GET("api/v1.0/notification/account/{username}")
    Call<List<NotificationMobileResponse>> findNotificationByUsername(@Path("username") String username, @Header("Authorization") String auth);

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @GET("api/v1.0/notification/read")
    Call<Boolean> updateStatus(@Query("notificationId") Integer notificationId, @Query("username") String username, @Header("Authorization") String auth);


    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @POST("api/v1.0/notification/")
    Call<NotificationMobileResponse> takeDayOff(@Body Notification notification );
}
