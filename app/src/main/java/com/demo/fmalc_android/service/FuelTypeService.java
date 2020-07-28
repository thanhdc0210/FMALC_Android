package com.demo.fmalc_android.service;

import com.demo.fmalc_android.entity.FuelType;
import com.demo.fmalc_android.entity.FuelTypeResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface FuelTypeService {

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @GET("api/v1.0/fuel-types/fuel-type")
    Call<FuelTypeResponse> getFuelTypeResponse(@Query("username") String username, @Query("status") List<Integer> status);

}
