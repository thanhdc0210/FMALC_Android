package com.demo.fmalc_android.service;

import com.demo.fmalc_android.entity.Location;
import com.demo.fmalc_android.entity.Notification;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface LocationConsignmentService {
    @POST("location/sendLocation")
    Call<ResponseBody> trackingLocation(@Body Location latLng);

    @POST("notification/")
    Call<ResponseBody> notifyWorkingHours(@Body Notification notification);
}
