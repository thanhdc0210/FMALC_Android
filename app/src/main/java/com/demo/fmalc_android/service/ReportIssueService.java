package com.demo.fmalc_android.service;

import com.demo.fmalc_android.entity.VehicleInspection;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface ReportIssueService {

@Multipart
    @POST("api/v1.0/inspection/upload-image")
    Call<ResponseBody> uploadImage(@Part MultipartBody.Part file);

}
