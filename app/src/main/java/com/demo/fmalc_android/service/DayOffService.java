package com.demo.fmalc_android.service;

import com.demo.fmalc_android.entity.DayOffDriverRequestDTO;
import com.demo.fmalc_android.entity.DayOffResponseDTO;
import com.demo.fmalc_android.entity.FuelRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface DayOffService {
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @POST("api/v1.0/dayOffs/")
    Call<DayOffResponseDTO> checkDayOff(@Body DayOffDriverRequestDTO dto, @Header("Authorization") String auth);

    @POST("api/v1.0/dayOffs/{id}")
    Call<DayOffResponseDTO> updateDayOff(@Path ("id") Integer id, @Body DayOffDriverRequestDTO dto, @Header("Authorization") String auth);


}
