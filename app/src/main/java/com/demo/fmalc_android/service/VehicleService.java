package com.demo.fmalc_android.service;

import com.demo.fmalc_android.entity.AlertRequestDTO;
import com.demo.fmalc_android.entity.VehicleInspection;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface VehicleService {

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @GET("api/v1.0/vehicles/report-inspection-before-delivery")
    Call<VehicleInspection> findVehicleLicensePlatesAndInspectionForReportInspectionBeforeDelivery(@Query("status") List<Integer> status,
            @Query("username") String username, @Header("Authorization") String auth);

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @GET("api/v1.0/vehicles/report-inspection-after-delivery")
    Call<VehicleInspection> findVehicleLicensePlatesAndInspectionForReportInspectionAfterDelivery(@Query("status") List<Integer> status,
          @Query("username") String username, @Header("Authorization") String auth);

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @GET("api/v1.0/vehicles/running")
    Call<Integer> getVehicleRunning(@Query("username") String username
            , @Header("Authorization") String auth);


    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @POST("api/v1.0/alerts/driver-send")
    Call<Boolean> sendRequestWhileRunning(@Body AlertRequestDTO alertRequest
            , @Header("Authorization") String auth);





}
