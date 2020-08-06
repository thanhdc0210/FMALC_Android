package com.demo.fmalc_android.presenter;

import com.demo.fmalc_android.contract.VehicleContract;
import com.demo.fmalc_android.entity.VehicleInspection;
import com.demo.fmalc_android.retrofit.NetworkingUtils;
import com.demo.fmalc_android.service.VehicleService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VehicleInspectionPresenter implements VehicleContract.Presenter {

    VehicleContract.View view;

    public void setView(VehicleContract.View view) {
        this.view = view;
    }

    VehicleService vehicleService = NetworkingUtils.getVehicleService();



    @Override
    public void getListLicensePlate(String username, String auth) {
        Call<VehicleInspection> call = vehicleService.findVehicleLicensePlatesAndInspectionForReportInspectionBeforeDelivery(username, auth);

        call.enqueue(new Callback<VehicleInspection>() {
            @Override
            public void onResponse(Call<VehicleInspection> call, Response<VehicleInspection> response) {
                if (response.code() == 204){
                    view.getListLicensePlateAndInspectionFailure("Dữ liệu bạn yêu cầu hiện không có");
                }else if (response.code() == 200){
                    view.getListLicensePlateAndInspectionSuccess(response.body());
                }else{
                    view.getListLicensePlateAndInspectionFailure("Có lỗi xảy ra trong quá trình lấy dữ liệu");
                }
            }

            @Override
            public void onFailure(Call<VehicleInspection> call, Throwable t) {
                if (t.getMessage().contains("timed out")){
                    view.getListLicensePlateAndInspectionFailure("Vui lòng kiểm tra lại kết nối mạng");
                }else {
                    view.getListLicensePlateAndInspectionFailure("Server đang gặp sự cố. Xin thử lại sau!");
                }
            }

        });
    }
}
