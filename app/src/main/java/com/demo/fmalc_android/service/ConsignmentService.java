package com.demo.fmalc_android.service;

import com.demo.fmalc_android.entity.Consignment;
import com.demo.fmalc_android.entity.ConsignmentDetail;
import com.demo.fmalc_android.entity.StatusRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ConsignmentService {

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @GET("api/v1.0/consignments/fleetManager")
    Call<List<Consignment>> findByConsignmentStatusAndUsernameForFleetManager(@Query("status") List<Integer> status, @Query("username") String username);

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @GET("api/v1.0/consignments/id/{id}")
    Call<ConsignmentDetail> findByConsignmentId(@Path("id") Integer id);

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @GET("api/v1.0/consignments/driver")
    Call<List<Consignment>> findByConsignmentStatusAndUsernameForDriver(@Query("allParams") StatusRequest allParams
    );
}
