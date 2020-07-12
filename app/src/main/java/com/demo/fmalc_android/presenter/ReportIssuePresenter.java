package com.demo.fmalc_android.presenter;

import android.content.Context;
import android.net.Uri;
import android.os.FileUtils;
import android.util.Log;
import android.widget.Toast;

import com.demo.fmalc_android.contract.ReportIssueContract;
import com.demo.fmalc_android.contract.VehicleContract;
import com.demo.fmalc_android.entity.ReportIssueRequest;
import com.demo.fmalc_android.entity.VehicleInspection;
import com.demo.fmalc_android.retrofit.NetworkingUtils;
import com.demo.fmalc_android.service.ReportIssueService;
import com.demo.fmalc_android.service.VehicleService;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Multipart;

public class ReportIssuePresenter implements ReportIssueContract.Presenter {
    ReportIssueContract.View view;

    public void setView(ReportIssueContract.View view) {
        this.view = view;
    }

    ReportIssueService reportIssueService = NetworkingUtils.getReportIssueService();

    @Override
    public void createReportIssue(ReportIssueRequest reportIssueRequest) {
        Call<ReportIssueRequest> call = reportIssueService.createReportIssue(reportIssueRequest);
        call.enqueue(new Callback<ReportIssueRequest>() {
            @Override
            public void onResponse(Call<ReportIssueRequest> call, Response<ReportIssueRequest> response) {
                if (!response.isSuccessful()) {
                    view.createReportIssueForFailure("Báo cáo thất bại. Xin thử lại sau!");
                } else {
                    view.createReportIssueForSuccess(response.body());
                }
            }

            @Override
            public void onFailure(Call<ReportIssueRequest> call, Throwable t) {
                view.createReportIssueForFailure("Báo cáo thất bại. Xin thử lại sau!");
            }
        }
        );
    }
}
