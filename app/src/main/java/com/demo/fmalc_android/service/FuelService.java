package com.demo.fmalc_android.service;

import com.demo.fmalc_android.entity.FuelRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface FuelService {
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @POST("api/v1.0/fuels/fuel-filling")
    Call<FuelRequest> saveFuelFilling(@Body FuelRequest fuelRequest);
}
