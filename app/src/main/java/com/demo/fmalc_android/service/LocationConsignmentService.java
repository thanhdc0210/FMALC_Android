package com.demo.fmalc_android.service;

import com.demo.fmalc_android.entity.Location;
import com.demo.fmalc_android.entity.Notification;
import com.demo.fmalc_android.entity.Place;
import com.demo.fmalc_android.entity.VehicleDetail;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface LocationConsignmentService {
    @POST("api/v1.0/location/sendLocation")
    Call<ResponseBody> trackingLocation(@Body Location latLng);

    @POST("api/v1.0/notification/")
    Call<Notification> notifyWorkingHours(@Body Notification notification);

    @GET("api/v1.0/vehicles/detail/{licensePlates}")
    Call<VehicleDetail> getDetailVehicleByLicense(@Path("licensePlates") String licensePlates);

    @GET("api/v1.0/location/stop-tracking/{id}")
    Call<String> stopTracking(@Path("id") int id);

    @POST("api/v1.0/consignments/places/id")
    Call<Place> updateActualTime(@Query("id") int id, @Query("schedule") int idSchedule);

    @POST("api/v1.0/notification/")
    Call<ResponseBody> notifyForManager(@Body Notification notification);
}
