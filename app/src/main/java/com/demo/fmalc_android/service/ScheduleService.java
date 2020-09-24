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

//    @Headers({ "Content-Type: application/json;charset=UTF-8"})
//    @GET("api/v1.0/schedules/{consignmentId}/{driverId}")
//    Call<DetailedSchedule> findScheduleByConsignment_IdAndDriver_Id(@Path("consignmentId") Integer consignmentId,
//                                                                    @Path("driverId") Integer driverId);

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @GET("api/v1.0/schedules/id/{id}")
    Call<DetailedSchedule> findScheduleById(@Path("id") Integer scheduleId);

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @GET("api/v1.0/schedules/driver")
    Call<List<Schedule>> findByConsignmentStatusAndUsernameForDriver(@Query("status") List<Integer> status, @Query("username") String username
            , @Header("Authorization") String auth);

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @POST("api/v1.0/schedules/id/{id}")
    Call<ListStatusUpdate> updateStatusAndUsernameForDriver(@Path("id") Integer id, @Body ListStatusUpdate statusUpdate);

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @GET("api/v1.0/schedules/driver/{id}")
    Call<Integer> countConsignment(@Path("id") Integer id);

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @GET("api/v1.0/schedules/driver/complete-consignment/{id}")
    Call<Integer> checkConsignmentInDay(@Path("id") Integer id);

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @GET("api/v1.0/schedules/driver/running/{id}")
    Call<DetailedSchedule> getScheduleRunningForDriver(@Path("id") Integer id);

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @GET("api/v1.0/schedules/first-consignment/{idDriver}")
    Call<Integer> getFirstConsignment(@Path("idDriver") Integer id);

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @GET("api/v1.0/schedules/schedule-detail")
    Call<Integer> findScheduleIdByContentOfNotificationAndDriverId(@Query("content") String content, @Query("driverId") Integer driverId
            , @Header("Authorization") String auth);
}
