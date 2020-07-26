package com.demo.fmalc_android.service;

import com.demo.fmalc_android.entity.Location;
import com.demo.fmalc_android.entity.Notification;
import com.demo.fmalc_android.entity.VehicleDetail;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface LocationConsignmentService {
    @POST("location/sendLocation")
    Call<ResponseBody> trackingLocation(@Body Location latLng);

    @POST("notification/")
    Call<Notification> notifyWorkingHours(@Body Notification notification);

    @GET("api/v1.0/vehicles/detail/{licensePlates}")
    Call<VehicleDetail> getDetailVehicleByLicense(@Path("licensePlates") String licensePlates);


}