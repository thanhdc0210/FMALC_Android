package com.demo.fmalc_android.service;

import com.demo.fmalc_android.entity.DriverInformation;
import com.demo.fmalc_android.entity.VehicleInspection;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface DriverService {

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @GET("api/v1.0/drivers/id/{id}")
    Call<DriverInformation> getDriverInformationById(@Path("id") Integer id);


}
