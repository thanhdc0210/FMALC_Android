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
    public void updateReportIssue(ReportIssueInformationForUpdating reportIssueInformationForUpdating, String auth) {
        Call<ReportIssueInformationForUpdating> call = reportIssueService.updateReportIssue(reportIssueInformationForUpdating, auth);

        call.enqueue(new Callback<ReportIssueInformationForUpdating>() {
            @Override
            public void onResponse(Call<ReportIssueInformationForUpdating> call, Response<ReportIssueInformationForUpdating> response) {
                if (response.code() == 204) {
                    view.updateReportIssueForFailure("Không thể lưu báo cáo");
                } else if (response.code() == 200) {
                    view.updateReportIssueForSuccess();
                }else{
                    view.updateReportIssueForFailure("Có lỗi xảy ra trong quá trình báo cáo");
                }
            }

            @Override
            public void onFailure(Call<ReportIssueInformationForUpdating> call, Throwable t) {
                if (t.getMessage().contains("timed out")){
                    view.updateReportIssueForFailure("Vui lòng kiểm tra lại kết nối mạng");
                }else if (t.getMessage().contains("Unable to resolve host")) {
                    view.updateReportIssueForFailure("Mất kết nối mạng");
                }else{
                    view.updateReportIssueForFailure("Xin thử lại sau ít phút");
                }
            }
        });
    }
}
