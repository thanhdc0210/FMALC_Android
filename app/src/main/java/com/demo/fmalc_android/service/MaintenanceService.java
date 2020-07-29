package com.demo.fmalc_android.service;

import com.demo.fmalc_android.entity.DriverInformation;
import com.demo.fmalc_android.entity.MaintainResponse;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface MaintenanceService {

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @GET("api/v1.0/maintenances")
    Call<List<MaintainResponse>> getMaintenanceList(@Query("driverId") Integer driverId);


    @Multipart
    @PUT("api/v1.0/maintenances/update-maintaining-complete")
    Call<ResponseBody> updateMaintenance(
            @Query("id") Integer id,
            @Query("km") Integer kmOld,
            @Part MultipartBody.Part file);

}
