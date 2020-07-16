package com.demo.fmalc_android.service;

import com.demo.fmalc_android.entity.FuelType;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;

public interface FuelTypeService {

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @GET("api/v1.0/fuel-type")
    Call<List<FuelType>> getFuelTypeList();

}
