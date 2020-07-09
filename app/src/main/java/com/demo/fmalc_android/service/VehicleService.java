package com.demo.fmalc_android.service;

import com.demo.fmalc_android.entity.VehicleInspection;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface VehicleService {

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @GET("api/v1.0/vehicles/report-inspection-before-delivery")
    Call<VehicleInspection> getVehiclePlateAndInspection(@Query("status") List<Integer> status, @Query("username") String username);

}
