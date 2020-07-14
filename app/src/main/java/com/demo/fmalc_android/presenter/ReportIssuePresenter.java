package com.demo.fmalc_android.presenter;

import com.demo.fmalc_android.contract.ReportIssueContract;
import com.demo.fmalc_android.entity.ReportIssueRequest;
import com.demo.fmalc_android.retrofit.NetworkingUtils;
import com.demo.fmalc_android.service.ReportIssueService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReportIssuePresenter implements ReportIssueContract.Presenter {
    ReportIssueContract.View view;

    public void setView(ReportIssueContract.View view) {
        this.view = view;
    }

    ReportIssueService reportIssueService = NetworkingUtils.getReportIssueService();

    @Override
    public void createReportIssueBeforeDelivery(ReportIssueRequest reportIssueRequest) {
        Call<ReportIssueRequest> call = reportIssueService.createReportIssue(reportIssueRequest);
        call.enqueue(new Callback<ReportIssueRequest>() {
            @Override
            public void onResponse(Call<ReportIssueRequest> call, Response<ReportIssueRequest> response) {
                if (!response.isSuccessful()) {
                    view.createReportIssueBeforeDeliveryForFailure("Có lỗi xảy ra trong quá trình báo cáo. Xin thử lại sau! " + response.code());
                } else {
                    view.createReportIssueBeforeDeliveryForSuccess(response.body());
                }
            }

            @Override
            public void onFailure(Call<ReportIssueRequest> call, Throwable t) {
                view.createReportIssueBeforeDeliveryForFailure("Server đang gặp sự cố. Xin thử lại sau!");
            }
        }
        );
    }
}
