package com.demo.fmalc_android.presenter;

import com.demo.fmalc_android.contract.ReportIssueForUpdatingContract;
import com.demo.fmalc_android.entity.ReportIssueInformationForUpdating;
import com.demo.fmalc_android.retrofit.NetworkingUtils;
import com.demo.fmalc_android.service.ReportIssueService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReportIssueForUpdatingPresenter implements ReportIssueForUpdatingContract.Presenter {

    ReportIssueForUpdatingContract.View view;

    public void setView(ReportIssueForUpdatingContract.View view) {
        this.view = view;
    }

    ReportIssueService reportIssueService = NetworkingUtils.getReportIssueService();

    @Override
    public void updateReportIssue(ReportIssueInformationForUpdating reportIssueInformationForUpdating) {
        Call<ReportIssueInformationForUpdating> call = reportIssueService.updateReportIssue(reportIssueInformationForUpdating);

        call.enqueue(new Callback<ReportIssueInformationForUpdating>() {
            @Override
            public void onResponse(Call<ReportIssueInformationForUpdating> call, Response<ReportIssueInformationForUpdating> response) {
                if (!response.isSuccessful()){
                    view.updateReportIssueForFailure("Có lỗi xảy ra trong quá trình cập nhật thông tin " + response.code());
                }else{
                    view.updateReportIssueForSuccess();
                }
            }

            @Override
            public void onFailure(Call<ReportIssueInformationForUpdating> call, Throwable t) {
                view.updateReportIssueForFailure("Có lỗi xảy ra ở server");
            }
        });
    }
}
