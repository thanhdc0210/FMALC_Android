package com.demo.fmalc_android.service;

import com.demo.fmalc_android.entity.ListStatusUpdate;
import com.demo.fmalc_android.entity.Schedule;
import com.demo.fmalc_android.entity.DetailedSchedule;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ScheduleService {

//    @Headers({ "Content-Type: application/json;charset=UTF-8"})
//    @GET("api/v1.0/consignments/fleetManager")
//    Call<List<Schedule>> findByConsignmentStatusAndUsernameForFleetManager(@Query("status") List<Integer> status, @Query("username") String username);

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @GET("api/v1.0/schedules/id/{id}")
    Call<DetailedSchedule> findByScheduleId(@Path("id") Integer id);

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @GET("api/v1.0/schedules/driver")
    Call<List<Schedule>> findByConsignmentStatusAndUsernameForDriver(@Query("status") List<Integer> status, @Query("username") String username
            , @Header("Authorization") String auth);

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @POST("api/v1.0/schedules/id/{id}")
    Call<ListStatusUpdate> updateStatusAndUsernameForDriver(@Path("id") Integer id, @Body ListStatusUpdate statusUpdate);
}
