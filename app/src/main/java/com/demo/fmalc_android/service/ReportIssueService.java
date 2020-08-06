package com.demo.fmalc_android.service;
import com.demo.fmalc_android.entity.ReportIssueInformationForUpdating;
import com.demo.fmalc_android.entity.ReportIssueRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

import com.demo.fmalc_android.entity.ReportIssueResponse;
import com.demo.fmalc_android.entity.VehicleInspection;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Query;
import retrofit2.http.POST;

public interface ReportIssueService {

    @POST("api/v1.0/report-issues/report-issue")
    Call<ReportIssueRequest> createReportIssue(@Body ReportIssueRequest reportIssueRequest, @Header("Authorization") String auth);

    @Multipart
    @POST("api/v1.0/inspection/upload-image")
    Call<ResponseBody> uploadImage(@Part MultipartBody.Part file, @Header("Authorization") String auth);

    @GET("api/v1.0/report-issues/information-report-issue")
    Call<ReportIssueResponse> getIssueInformationOfAVehicle(@Query("username") String username, @Query("status") List<Integer> status
            , @Header("Authorization") String auth);

    @PUT("api/v1.0/report-issues/report-issue")
    Call<ReportIssueInformationForUpdating> updateReportIssue(@Body ReportIssueInformationForUpdating reportIssueInformationForUpdating
            , @Header("Authorization") String auth);
}
