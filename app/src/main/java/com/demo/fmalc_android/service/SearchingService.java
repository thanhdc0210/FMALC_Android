package com.demo.fmalc_android.service;

import com.demo.fmalc_android.entity.Schedule;
import com.demo.fmalc_android.enumType.SearchTypeForDriverEnum;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface SearchingService {

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @GET("api/v1.0/schedules/search")
    Call<List<Schedule>> searchConsignment(@Query("searchType") SearchTypeForDriverEnum searchType, @Query("searchValue") String value);
}
