package com.demo.fmalc_android.service;

import com.demo.fmalc_android.entity.ReportIssueRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ReportIssueService {

    @POST("/api/v1.0/report-issues")
    Call<ReportIssueRequest> createReportIssue(@Body ReportIssueRequest reportIssueRequest);
}
