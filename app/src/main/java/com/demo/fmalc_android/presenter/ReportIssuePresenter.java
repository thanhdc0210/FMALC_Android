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
    public void createReportIssueForDelivery(ReportIssueRequest reportIssueRequest, String auth) {
        Call<ReportIssueRequest> call = reportIssueService.createReportIssue(reportIssueRequest, auth);
        call.enqueue(new Callback<ReportIssueRequest>() {
            @Override
            public void onResponse(Call<ReportIssueRequest> call, Response<ReportIssueRequest> response) {
                if (response.code() == 204) {
                    view.createReportIssueForDeliveryForFailure("Không thể lưu báo cáo");
                } else if (response.code() == 200) {
                    view.createReportIssueForDeliveryForSuccess(response.body());
                }else{
                    view.createReportIssueForDeliveryForFailure("Có lỗi xảy ra trong quá trình báo cáo");
                }
            }

            @Override
            public void onFailure(Call<ReportIssueRequest> call, Throwable t) {
                if (t.getMessage().contains("timed out")){
                    view.createReportIssueForDeliveryForFailure("Vui lòng kiểm tra lại kết nối mạng");
                }else if (t.getMessage().contains("Unable to resolve host")) {
                    view.createReportIssueForDeliveryForFailure("Mất kết nối mạng");
                }else{
                    view.createReportIssueForDeliveryForFailure("Xin thử lại sau ít phút");
                }
            }
        }
        );
    }
}
