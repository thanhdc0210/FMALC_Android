package com.demo.fmalc_android.presenter;

import com.demo.fmalc_android.contract.ReportIssueResponseContract;
import com.demo.fmalc_android.entity.ReportIssueResponse;
import com.demo.fmalc_android.retrofit.NetworkingUtils;
import com.demo.fmalc_android.service.ReportIssueService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReportIssueResponsePresenter implements ReportIssueResponseContract.Presenter {

    ReportIssueResponseContract.View view;

    public void setView(ReportIssueResponseContract.View view) {
        this.view = view;
    }

    ReportIssueService reportIssueService = NetworkingUtils.getReportIssueService();

    @Override
    public void getIssueInformationOfAVehicle(String username) {
        Call<ReportIssueResponse> call = reportIssueService.getIssueInformationOfAVehicle(username);

        call.enqueue(new Callback<ReportIssueResponse>() {
            @Override
            public void onResponse(Call<ReportIssueResponse> call, Response<ReportIssueResponse> response) {
                if (!response.isSuccessful()){
                    view.getIssueInformationOfAVehicleFailure("Có lỗi xảy ra trong quá trình lấy dữ liệu ");
                }else{
                    view.getIssueInformationOfAVehicleSuccess(response.body());
                }
            }

            @Override
            public void onFailure(Call<ReportIssueResponse> call, Throwable t) {
                view.getIssueInformationOfAVehicleFailure("Có lỗi xảy ra ở server");
            }
        });
    }
}